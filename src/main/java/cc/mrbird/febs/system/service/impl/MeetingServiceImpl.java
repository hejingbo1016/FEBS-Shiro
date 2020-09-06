package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.dto.JsonObjectPage;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.constants.AdminConstants;
import cc.mrbird.febs.system.entity.Meeting;
import cc.mrbird.febs.system.mapper.MeetingMapper;
import cc.mrbird.febs.system.service.IMeetingService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * Service实现
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
        page.setSearchCount(false);
        page.setTotal(baseMapper.countMeeting(meeting));
        setselectMeeting(queryWrapper, meeting);
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_ASC, true);
        return this.page(page, queryWrapper);
    }

    /**
     * 设置查询条件
     *
     * @param queryWrapper
     * @param meeting
     */
    private void setselectMeeting(LambdaQueryWrapper<Meeting> queryWrapper, Meeting meeting) {

        if (StringUtils.isNotBlank(meeting.getMeetingName())) {
            queryWrapper.like(Meeting::getMeetingName, meeting.getMeetingName());
        }
        if (StringUtils.isNotBlank(meeting.getSponsor())) {
            queryWrapper.like(Meeting::getSponsor, meeting.getSponsor());
        }
        if (StringUtils.isNotBlank(meeting.getOrganizer())) {
            queryWrapper.like(Meeting::getOrganizer, meeting.getOrganizer());
        }
        if (StringUtils.isNotBlank(meeting.getMeetingAddress())) {
            queryWrapper.like(Meeting::getMeetingAddress, meeting.getMeetingAddress());
        }
        if (StringUtils.isNotBlank(meeting.getMeetingPrincipal())) {
            queryWrapper.like(Meeting::getMeetingPrincipal, meeting.getMeetingPrincipal());
        }
        if (meeting.getStatus() != null) {
            queryWrapper.eq(Meeting::getStatus, meeting.getStatus());
        }
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

    @Override
    public void deleteMeetings(String meetingIds) {
        List<String> list = Arrays.asList(meetingIds.split(StringPool.COMMA));
        this.baseMapper.delete(new QueryWrapper<Meeting>().lambda().in(Meeting::getId, list));

    }

    @Override
    public void auditMeeting(Meeting meeting) {
        this.meetingMapper.auditMeeting(meeting);
    }

    @Override
    public IPage<Meeting> weChatMettingList(QueryRequest request, Meeting meeting) {
        if (org.springframework.util.StringUtils.isEmpty(meeting.getStatus())) {
            meeting.setStatus(AdminConstants.AUDIT_T_TYPE);
        }
        return findMeetings(request, meeting);
    }

    @Override
    public JsonObjectPage getWeChatMettingById(Long id) {
        Meeting meeting = meetingMapper.selectById(id);
        return JsonObjectPage.createJsonObjectPage(meeting);
    }
}
