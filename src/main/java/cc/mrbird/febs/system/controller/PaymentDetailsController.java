package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.PaymentDetails;
import cc.mrbird.febs.system.service.IPaymentDetailsService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 *  Controller
 *
 * @author Hejingbo
 * @date 2020-09-05 21:06:26
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class PaymentDetailsController extends BaseController {

    private final IPaymentDetailsService paymentDetailsService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "paymentDetails")
    public String paymentDetailsIndex(){
        return FebsUtil.view("paymentDetails/paymentDetails");
    }

    @GetMapping("paymentDetails")
    @ResponseBody
    @RequiresPermissions("paymentDetails:list")
    public FebsResponse getAllPaymentDetailss(PaymentDetails paymentDetails) {
        return new FebsResponse().success().data(paymentDetailsService.findPaymentDetailss(paymentDetails));
    }

    @GetMapping("paymentDetails/list")
    @ResponseBody
    @RequiresPermissions("paymentDetails:list")
    public FebsResponse paymentDetailsList(QueryRequest request, PaymentDetails paymentDetails) {
        Map<String, Object> dataTable = getDataTable(this.paymentDetailsService.findPaymentDetailss(request, paymentDetails));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增PaymentDetails", exceptionMessage = "新增PaymentDetails失败")
    @PostMapping("paymentDetails")
    @ResponseBody
    @RequiresPermissions("paymentDetails:add")
    public FebsResponse addPaymentDetails(@Valid PaymentDetails paymentDetails) {
        this.paymentDetailsService.createPaymentDetails(paymentDetails);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除PaymentDetails", exceptionMessage = "删除PaymentDetails失败")
    @GetMapping("paymentDetails/delete")
    @ResponseBody
    @RequiresPermissions("paymentDetails:delete")
    public FebsResponse deletePaymentDetails(PaymentDetails paymentDetails) {
        this.paymentDetailsService.deletePaymentDetails(paymentDetails);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改PaymentDetails", exceptionMessage = "修改PaymentDetails失败")
    @PostMapping("paymentDetails/update")
    @ResponseBody
    @RequiresPermissions("paymentDetails:update")
    public FebsResponse updatePaymentDetails(PaymentDetails paymentDetails) {
        this.paymentDetailsService.updatePaymentDetails(paymentDetails);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改PaymentDetails", exceptionMessage = "导出Excel失败")
    @PostMapping("paymentDetails/excel")
    @ResponseBody
    @RequiresPermissions("paymentDetails:export")
    public void export(QueryRequest queryRequest, PaymentDetails paymentDetails, HttpServletResponse response) {
        List<PaymentDetails> paymentDetailss = this.paymentDetailsService.findPaymentDetailss(queryRequest, paymentDetails).getRecords();
        ExcelKit.$Export(PaymentDetails.class, response).downXlsx(paymentDetailss, false);
    }
}
