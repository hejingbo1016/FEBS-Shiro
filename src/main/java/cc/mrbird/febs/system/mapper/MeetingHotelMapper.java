package cc.mrbird.febs.system.mapper;

import cc.mrbird.febs.system.entity.MeetingHotel;
import cc.mrbird.febs.system.entity.PaymentDetails;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Mapper
 *
 * @author Hejingbo
 * @date 2020-08-05 23:39:59
 */
public interface MeetingHotelMapper extends BaseMapper<MeetingHotel> {

    Long countMeetHotels(@Param("meetingHotel") MeetingHotel meetingHotel);

    <T> IPage<MeetingHotel> findMeetingHotelsPage(Page<T> page, @Param("meetingHotel") MeetingHotel meetingHotel);

    MeetingHotel selectMeetingHotelById(@Param("id") Long id);

    List<MeetingHotel> selectFeeLists(@Param("meetingId") Long meetingId, @Param("hotelId") Long hotelId, @Param("feeId") Long feeId,@Param("startTime") String startTime,@Param("endTime") String endTime);

    MeetingHotel isExistMeetingHotel(MeetingHotel meetingHotel);

    int updateFeePrice(MeetingHotel meetingHotel);

    int updateInvetory(PaymentDetails t);

    int reduceInvetory(PaymentDetails t);

    int updateMeetingHotelById(Long id);

    int updateMeetingHotel(MeetingHotel meetingHotel);

    int updateChildrenMeetingHotel(MeetingHotel meetingHotel);

    List<MeetingHotel> meetingHotelEdits(@Param("id") Long id);

    List<MeetingHotel> selectWeChatFeeLists(@Param("meetingId") Long meetingId, @Param("hotelId") Long hotelId, @Param("feeId") Long feeId,@Param("startTime") String startTime,@Param("endTime") String endTime);
}
