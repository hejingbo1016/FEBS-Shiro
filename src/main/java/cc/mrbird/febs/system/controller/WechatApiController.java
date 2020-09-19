package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.dto.ResponseDTO;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.*;
import cc.mrbird.febs.system.service.IFileService;
import cc.mrbird.febs.system.service.IHotelService;
import cc.mrbird.febs.system.service.IMeetingService;
import cc.mrbird.febs.system.service.IPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    private final IHotelService hotelService;
    private final IFileService fileService;

    @ControllerEndpoint(operation = "微信端获取会议列表", exceptionMessage = "微信获取会议列表失败")
    @GetMapping("weChatMettingList")
    @ResponseBody
    public FebsResponse weChatMettingList(QueryRequest request, Meeting meeting) {
        Map<String, Object> dataTable = getDataTable(this.meetingService.weChatMettingList(request, meeting));
        return new FebsResponse().success().data(dataTable);
    }


    @ControllerEndpoint(operation = "微信端通过会议id获取详细信息", exceptionMessage = "微信端通过会议id获取详细信息")
    @GetMapping("" +
            "")
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


    @ControllerEndpoint(operation = "微信端下单功能", exceptionMessage = "微信端下单功能失败")
    @PostMapping("placOrders")
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO placOrders(@RequestBody OrderPay orderPay) {
        return paymentService.placOrders(orderPay);
    }

    /**
     * 微信那边支付处理完毕之后，会进行回调，通知系统支付的结果
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("payNotify")
    public String weiChatPayNotify(HttpServletRequest request, HttpServletResponse response) {
        String resXml = "";
        try {
            InputStream is = request.getInputStream();
            //将InputStream转换成String
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();

            resXml = sb.toString();
            return paymentService.weiChatPayNotify(resXml);
        } catch (Exception e) {
            log.error("微信支付回调通知失败", e);
            String result = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文获取失败]]></return_msg>" + "</xml> ";
            return result;
        }

    }

    @ControllerEndpoint(operation = "新增addFile", exceptionMessage = "新增addFile失败")
    @PostMapping("addFile")
//    @RequiresPermissions("hotel:add")
    public FebsResponse addHotel(@RequestParam(value = "file", required = false) MultipartFile[] file, Hotel hotel) {
        this.hotelService.createHotel(hotel);
        Long id = hotel.getId();
        if (id != null) {
            fileService.uploadFile(file, id);
        }
        return new FebsResponse().success();
    }
}
