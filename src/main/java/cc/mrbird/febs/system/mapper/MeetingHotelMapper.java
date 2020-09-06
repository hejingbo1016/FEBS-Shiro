package cc.mrbird.febs.system.mapper;

import cc.mrbird.febs.system.entity.MeetingHotel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 *  Mapper
 *
 * @author Hejingbo
 * @date 2020-08-05 23:39:59
 */
public interface MeetingHotelMapper extends BaseMapper<MeetingHotel> {

    Long countMeetHotels(@Param("meetingHotel") MeetingHotel meetingHotel);

    <T>IPage<MeetingHotel> findMeetingHotelsPage(Page<T> page, @Param("meetingHotel") MeetingHotel meetingHotel);
}
