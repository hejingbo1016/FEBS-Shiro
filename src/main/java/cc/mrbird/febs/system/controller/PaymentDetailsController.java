package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.entity.PaymentDetails;
import cc.mrbird.febs.system.service.IPaymentDetailsService;
import com.wuwenze.poi.ExcelKit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Controller
 *
 * @author Hejingbo
 * @date 2020-09-05 21:06:26
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@RequestMapping("paymentDetails")
public class PaymentDetailsController extends BaseController {

    private final IPaymentDetailsService paymentDetailsService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "paymentDetails")
    public String paymentDetailsIndex() {
        return FebsUtil.view("paymentDetails/paymentDetails");
    }

    @GetMapping
    public FebsResponse getAllPaymentDetailss(PaymentDetails paymentDetails) {
        return new FebsResponse().success().data(paymentDetailsService.findPaymentDetailss(paymentDetails));
    }

    @GetMapping("list/{paymentCode}")
    @ResponseBody
    @RequiresPermissions("payment:details")
    public FebsResponse paymentDetailsList(QueryRequest request, PaymentDetails paymentDetails) {
        Map<String, Object> dataTable = getDataTable(this.paymentDetailsService.findPaymentDetailss(request, paymentDetails));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增PaymentDetails", exceptionMessage = "新增PaymentDetails失败")
    @PostMapping("add")
    @ResponseBody
    @RequiresPermissions("paymentDetails:add")
    public FebsResponse addPaymentDetails(@Valid PaymentDetails paymentDetails) {
        if (!Objects.isNull(paymentDetails)) {

            if (!StringUtils.isEmpty(paymentDetails.getId())) {
                this.paymentDetailsService.updatePaymentDetails(paymentDetails);
            } else {
                this.paymentDetailsService.addPaymentDetails(paymentDetails);
            }
        }

        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除PaymentDetails", exceptionMessage = "删除PaymentDetails失败")
    @GetMapping("delete/{ids}")
    @ResponseBody
    @RequiresPermissions("paymentDetails:delete")
    public FebsResponse deletePaymentDetails(@NotBlank(message = "{required}") @PathVariable String ids) {
        this.paymentDetailsService.deletePaymentDetailsByIds(ids);
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
    public void export(QueryRequest queryRequest, PaymentDetails paymentDetails, HttpServletResponse response, String paymentCode) {
        List<PaymentDetails> paymentDetailss = this.paymentDetailsService.findPaymentDetailss(queryRequest, paymentDetails).getRecords();
        ExcelKit.$Export(PaymentDetails.class, response).downXlsx(paymentDetailss, false);
    }


    @ControllerEndpoint(operation = "导出会议订单表Excel", exceptionMessage = "导出会议订单表Excel失败")
    @PostMapping("paymentExport")
    @ResponseBody
    public void paymentExport(PaymentDetails paymentDetails, HttpServletResponse response) {
        List<PaymentDetails> paymentDetailsList = paymentDetailsService.selectPaymentExport(paymentDetails);
        ExcelKit.$Export(PaymentDetails.class, response).downXlsx(paymentDetailsList, false);
    }



    @ControllerEndpoint(operation = "导出发票表", exceptionMessage = "导出发票表失败")
    @PostMapping("orderInvoiceExport")
    @ResponseBody
    public void orderInvoiceExport(PaymentDetails paymentDetails, HttpServletResponse response) {
        List<PaymentDetails> paymentDetailsList = paymentDetailsService.selectPaymentExport(paymentDetails);
        ExcelKit.$Export(PaymentDetails.class, response).downXlsx(paymentDetailsList, false);
    }
}
