package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.Meeting;
import cc.mrbird.febs.system.mapper.MeetingMapper;
import cc.mrbird.febs.system.service.IMeetingService;
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
 * @date 2020-08-05 23:40:11
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MeetingServiceImpl extends ServiceImpl<MeetingMapper, Meeting> implements IMeetingService {

    private final MeetingMapper meetingMapper;

    @Override
    public IPage<Meeting> findMeetings(QueryRequest request, Meeting meeting) {
        LambdaQueryWrapper<Meeting> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<Meeting> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Meeting> findMeetings(Meeting meeting) {
	    LambdaQueryWrapper<Meeting> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createMeeting(Meeting meeting) {
        this.save(meeting);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMeeting(Meeting meeting) {
        this.saveOrUpdate(meeting);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMeeting(Meeting meeting) {
        LambdaQueryWrapper<Meeting> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}
}
