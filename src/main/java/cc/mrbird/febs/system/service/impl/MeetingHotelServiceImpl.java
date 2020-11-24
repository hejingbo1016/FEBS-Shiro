package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.BusinessRuntimeException;
import cc.mrbird.febs.common.utils.DateUtils;
import cc.mrbird.febs.common.utils.Snowflake;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.constants.AdminConstants;
import cc.mrbird.febs.system.entity.MeetingHotel;
import cc.mrbird.febs.system.mapper.MeetingHotelMapper;
import cc.mrbird.febs.system.service.IMeetingHotelService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Service实现
 *
 * @author Hejingbo
 * @date 2020-08-05 23:39:59
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MeetingHotelServiceImpl extends ServiceImpl<MeetingHotelMapper, MeetingHotel> implements IMeetingHotelService {

    private final MeetingHotelMapper meetingHotelMapper;
    private static Snowflake snowflake = Snowflake.getInstanceSnowflake();

    @Override
    public IPage<MeetingHotel> findMeetingHotels(QueryRequest request, MeetingHotel meetingHotel) {
        Page<MeetingHotel> page = new Page<>(request.getPageNum(), request.getPageSize());
        page.setTotal(baseMapper.countMeetHotels(meetingHotel));
        IPage<MeetingHotel> paymentDetailsIPage = meetingHotelMapper.findMeetingHotelsPage(page, meetingHotel);
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_ASC, true);
        return paymentDetailsIPage;
    }

    @Override
    public List<MeetingHotel> findMeetingHotels(MeetingHotel meetingHotel) {
        LambdaQueryWrapper<MeetingHotel> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        return this.baseMapper.selectList(queryWrapper);
    }


    private void isExistMeetingHotel(MeetingHotel meetingHotel, List<String> dates, List<MeetingHotel> saveOrUpdates) {

        dates.stream().forEach(d -> {
            MeetingHotel fee = new MeetingHotel();
            BeanUtils.copyProperties(meetingHotel, fee);
            fee.setDateTime(d);
            //  根据会议id、酒店id、费用id、日期时间 去查费用项是否存在，存在则更新，不存在则新增
            MeetingHotel feeHotel = meetingHotelMapper.isExistMeetingHotel(fee);
            if (!Objects.isNull(feeHotel)) {
                fee.setId(feeHotel.getId());
                saveOrUpdates.add(fee);
            } else {
                saveOrUpdates.add(fee);
            }
        });

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMeetingHotel(MeetingHotel meetingHotel) {


        if (AdminConstants.FEETYPE_TWO.equals(meetingHotel.getFeeType())) {
            meetingHotel.setDateTime(null);
            this.saveOrUpdate(meetingHotel);
        } else {

            if (StringUtils.isEmpty(meetingHotel.getDateTime())) {
                throw new BusinessRuntimeException("日期不能为空！");
            }

            //根据会议id、酒店id、费用id、日期时间 去查费用项是否存在,存在当价格不变则 只更新当前记录。
            MeetingHotel fee = new MeetingHotel();
            BeanUtils.copyProperties(meetingHotel, fee);
            MeetingHotel existMeetingHotel = meetingHotelMapper.isExistMeetingHotel(fee);
            if (!Objects.isNull(existMeetingHotel)) {
                if (!meetingHotel.getFeePrice().equals(existMeetingHotel.getFeePrice())) {
                    //费用费用改变，更新该会议id和费用项id 下的所有费用保持一致
                    meetingHotelMapper.updateFeePrice(meetingHotel);
                }
                meetingHotel.setId(existMeetingHotel.getId());
            } else {
                MeetingHotel selectById = meetingHotelMapper.selectById(meetingHotel.getId());
                if (!selectById.getFeePrice().equals(meetingHotel.getFeePrice())) {
                    //费用费用改变，更新该会议id和费用项id 下的所有费用保持一致
                    meetingHotelMapper.updateFeePrice(meetingHotel);
                }
            }
            this.saveOrUpdate(meetingHotel);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMeetingHotel(MeetingHotel meetingHotel) {
        LambdaQueryWrapper<MeetingHotel> wrapper = new LambdaQueryWrapper<>();
        // TODO 设置删除条件
        this.remove(wrapper);
    }

    @Override
    public void deleteMeetingHotels(String ids) {
        List<String> list = Arrays.asList(ids.split(StringPool.COMMA));
        this.baseMapper.delete(new QueryWrapper<MeetingHotel>().lambda().in(MeetingHotel::getId, list));

    }

    @Override
    public MeetingHotel selectMeetingHotelById(Long id) {
        return baseMapper.selectMeetingHotelById(id);
    }

    @Override
    public void addMeetingHotelDate(MeetingHotel meetingHotel) {
        String dateRange = meetingHotel.getDateRange();
        if (!StringUtils.isEmpty(dateRange)) {
            //当类型是其他
            if (AdminConstants.FEETYPE_TWO.equals(meetingHotel.getFeeType())) {
                meetingHotel.setDateTime(null);
                this.save(meetingHotel);
            } else {
                //需要新增/修改的集
                List<MeetingHotel> saveOrUpdates = new ArrayList<>();
                //截取-把该范围内的所有时间整成时间集合
                String[] split = dateRange.split("~");
                try {
                    //该范围内的所有日期
                    List<String> dates = DateUtils.findDates(split[0], split[1]);
                    //根据会议id、酒店id、费用id、时间 去查费用项是否存在，存在则更新，不存在则新增
                    isExistMeetingHotel(meetingHotel, dates, saveOrUpdates);
                    this.saveOrUpdateBatch(saveOrUpdates);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void addMeetingHotelNotDate(MeetingHotel meetingHotel) {
        int count = 0;
        //根据当id为空时新增，否则编辑
        if (StringUtils.isEmpty(meetingHotel.getId())) {
            //新增
            MeetingHotel existMeetingHotel = meetingHotelMapper.isExistMeetingHotel(meetingHotel);
            if (!Objects.isNull(existMeetingHotel)) {
                meetingHotel.setId(existMeetingHotel.getId());
            }
            this.saveOrUpdate(meetingHotel);
        } else {
            //编辑
            MeetingHotel oldMeetingHotel = meetingHotelMapper.selectById(meetingHotel.getId());
            if (!Objects.isNull(oldMeetingHotel)) {
                //会议id、酒店id和费用id 是否一致
                if (oldMeetingHotel.getFeeId().equals(meetingHotel.getFeeId()) && oldMeetingHotel.getMeetingId().equals(meetingHotel.getMeetingId())
                        && oldMeetingHotel.getHotelId().equals(meetingHotel.getHotelId())) {
                } else {
                    //如果存在了，则拿到该id，进行编辑
                    MeetingHotel meetingHotels = meetingHotelMapper.isExistMeetingHotel(meetingHotel);
                    if (!Objects.isNull(meetingHotels)) {
                        //删除旧 上级数据
                        count = meetingHotelMapper.updateMeetingHotelById(meetingHotels.getId());
                        //删除  下级数据
                        count = meetingHotelMapper.updateChildrenMeetingHotel(meetingHotels);
                        if (count > 0) {
                            //修改 上级
                            count = getCountUpdate(meetingHotel);
                        }
                    } else {
                        meetingHotel.setId(oldMeetingHotel.getId());
                        //修改 上级
                        count = getCountUpdate(meetingHotel);
                    }
                    //修改下级
                    if (count > 0) {
                        meetingHotelMapper.updateMeetingHotel(meetingHotel);
                    }
                }
            } else {
                throw new BusinessRuntimeException("所编辑的数据已被删除");
            }
        }
    }

    @Override
    public List<MeetingHotel> meetingHotelEdits(Long id) {
        return meetingHotelMapper.meetingHotelEdits(id);
    }

    private int getCountUpdate(MeetingHotel meetingHotel) {
        int count;
        MeetingHotel whereMeetings = new MeetingHotel();
        whereMeetings.setId(meetingHotel.getId());
        whereMeetings.setDeleted(AdminConstants.DATA_N_DELETED);
        count = meetingHotelMapper.update(meetingHotel, new LambdaQueryWrapper<>(whereMeetings));
        return count;
    }
}
