package cc.mrbird.febs.system.service;

import cc.mrbird.febs.common.dto.ResponseDTO;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  Service接口
 *
 * @author Hejingbo
 * @date 2020-08-05 23:40:11
 */
public interface IMeetingService extends IService<Meeting> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param meeting meeting
     * @return IPage<Meeting>
     */
    IPage<Meeting> findMeetings(QueryRequest request, Meeting meeting);

    /**
     * 查询（所有）
     *
     * @param meeting meeting
     * @return List<Meeting>
     */
    List<Meeting> findMeetings(Meeting meeting);

    /**
     * 新增
     *
     * @param meeting meeting
     */
    void createMeeting(Meeting meeting);

    /**
     * 修改
     *
     * @param meeting meeting
     */
    void updateMeeting(Meeting meeting);

    /**
     * 删除
     *
     * @param meeting meeting
     */
    void deleteMeeting(Meeting meeting);

    void deleteMeetings(String meetingIds);

    void auditMeeting(Meeting meeting);

    IPage<Meeting> weChatMettingList(QueryRequest request, Meeting meeting);

    Meeting getWeChatMettingById(Long id);

    List<HotelName> weChatHotelsByMeetingId(Long id);

    ResponseDTO generateQRCode(GenerateQRCodeDTO generateQRCodeDTO);

    List<HotelName>  weChatHotelsFeeIds(PaymentDetails details);
}
