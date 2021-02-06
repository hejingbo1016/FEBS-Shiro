package cc.mrbird.febs.system.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.MeetingHotel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  Service接口
 *
 * @author Hejingbo
 * @date 2020-08-05 23:39:59
 */
public interface IMeetingHotelService extends IService<MeetingHotel> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param meetingHotel meetingHotel
     * @return IPage<MeetingHotel>
     */
    IPage<MeetingHotel> findMeetingHotels(QueryRequest request, MeetingHotel meetingHotel);

    /**
     * 查询（所有）
     *
     * @param meetingHotel meetingHotel
     * @return List<MeetingHotel>
     */
    List<MeetingHotel> findMeetingHotels(MeetingHotel meetingHotel);



    /**
     * 修改
     *
     * @param meetingHotel meetingHotel
     */
    void updateMeetingHotel(MeetingHotel meetingHotel);

    /**
     * 删除
     *
     * @param meetingHotel meetingHotel
     */
    void deleteMeetingHotel(MeetingHotel meetingHotel);

    void deleteMeetingHotels(String ids);

    MeetingHotel selectMeetingHotelById(Long id);

    void addMeetingHotelDate(MeetingHotel meetingHotel);

    void addMeetingHotelNotDate(MeetingHotel meetingHotel);

    List<MeetingHotel> meetingHotelEdits(Long id);

}
