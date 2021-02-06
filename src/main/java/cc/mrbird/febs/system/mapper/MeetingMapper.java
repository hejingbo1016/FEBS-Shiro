package cc.mrbird.febs.system.mapper;

import cc.mrbird.febs.system.entity.HotelName;
import cc.mrbird.febs.system.entity.Meeting;
import cc.mrbird.febs.system.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Mapper
 *
 * @author Hejingbo
 * @date 2020-08-05 23:40:11
 */
public interface MeetingMapper extends BaseMapper<Meeting> {


    Long countMeeting(@Param("meeting") Meeting meeting, @Param("user") User user);

    void auditMeeting(Meeting meeting);

    List<HotelName> weChatHotelsByMeetingId(@Param("meetingId") Long meetingId);

    int deleteMeetingByIds(@Param("ids") String[] ids);

    <T> IPage<Meeting> findMeetings(Page<T> page, @Param("meeting") Meeting meeting, @Param("user") User user);

    int meetingDaterange(Meeting meeting);
}
