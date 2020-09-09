package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.HotelName;
import cc.mrbird.febs.system.entity.Meeting;
import cc.mrbird.febs.system.service.IMeetingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/weChatUser")
public class WechatApiController extends BaseController {

    private final IMeetingService meetingService;

    @ControllerEndpoint(operation = "微信端获取会议列表", exceptionMessage = "微信获取会议列表失败")
    @GetMapping("weChatMettingList")
    @ResponseBody
    public FebsResponse weChatMettingList(QueryRequest request, Meeting meeting) {
        Map<String, Object> dataTable = getDataTable(this.meetingService.weChatMettingList(request, meeting));
        return new FebsResponse().success().data(dataTable);
    }


    @ControllerEndpoint(operation = "微信端通过会议id获取详细信息", exceptionMessage = "微信端通过会议id获取详细信息")
    @GetMapping("getWeChatMettingById")
    @ResponseBody
    public FebsResponse getWeChatMettingById(@Valid Long id) {
        Meeting mettings = meetingService.getWeChatMettingById(id);
        return new FebsResponse().success().data(mettings);
    }


    @ControllerEndpoint(operation = "微信端通过会议id查酒店列表", exceptionMessage = "微信端通过会议id查酒店列表失败")
    @GetMapping("weChatHotelsByMeetingId")
    @ResponseBody
    public FebsResponse weChatHotelsByMeetingId(@Valid Long id) {
        List<HotelName> hotelNameList = meetingService.weChatHotelsByMeetingId(id);
        return null;
    }


}
