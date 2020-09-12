package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.Snowflake;
import cc.mrbird.febs.common.utils.SortUtil;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createMeetingHotel(MeetingHotel meetingHotel) {
        meetingHotel.setId(snowflake.nextId());
        this.save(meetingHotel);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMeetingHotel(MeetingHotel meetingHotel) {
        this.saveOrUpdate(meetingHotel);
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
}
