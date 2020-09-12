package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.*;
import cc.mrbird.febs.system.service.IMeetingService;
import cc.mrbird.febs.system.service.IPaymentService;
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
    private final IPaymentService paymentService;

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
        //酒店列表
        List<HotelName> hotelNameList = meetingService.weChatHotelsByMeetingId(id);
        return new FebsResponse().success().data(hotelNameList);
    }


    @ControllerEndpoint(operation = "根据用户id查自己的订单列表", exceptionMessage = "根据用户id查自己的订单列表失败")
    @GetMapping("getPaymentListByUserId")
    @ResponseBody
    public FebsResponse getPaymentListByUserId(@Valid Long userId) {
        List<Payment> paymentList = paymentService.getPaymentListByUserId(userId);
        return new FebsResponse().success().data(paymentList);
    }


    @ControllerEndpoint(operation = "根据订单编号查详情", exceptionMessage = "根据订单编号查详情失败")
    @GetMapping("getPaymentDetailsByCode")
    public FebsResponse getPaymentDetailsByCode(@Valid String paymentCode) {
        List<PaymentDetails> paymentDetails = paymentService.getPaymentDetailsByCode(paymentCode);
        return new FebsResponse().success().data(paymentDetails);
    }


}
