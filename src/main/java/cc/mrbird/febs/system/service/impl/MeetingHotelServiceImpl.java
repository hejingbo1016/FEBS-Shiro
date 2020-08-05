package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.MeetingHotel;
import cc.mrbird.febs.system.mapper.MeetingHotelMapper;
import cc.mrbird.febs.system.service.IMeetingHotelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import lombok.RequiredArgsConstructor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 *  Service实现
 *
 * @author Hejingbo
 * @date 2020-08-05 23:39:59
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MeetingHotelServiceImpl extends ServiceImpl<MeetingHotelMapper, MeetingHotel> implements IMeetingHotelService {

    private final MeetingHotelMapper meetingHotelMapper;

    @Override
    public IPage<MeetingHotel> findMeetingHotels(QueryRequest request, MeetingHotel meetingHotel) {
        LambdaQueryWrapper<MeetingHotel> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<MeetingHotel> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
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
}
