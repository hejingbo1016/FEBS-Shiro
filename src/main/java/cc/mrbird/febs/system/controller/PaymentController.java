package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.entity.Payment;
import cc.mrbird.febs.system.service.IPaymentService;
import com.wuwenze.poi.ExcelKit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author Hejingbo
 * @date 2020-09-05 21:06:30
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("payment")
public class PaymentController extends BaseController {

    private final IPaymentService paymentService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "payment")
    public String paymentIndex() {
        return FebsUtil.view("payment/payment");
    }

    @GetMapping
    public FebsResponse getAllPayments(Payment payment) {
        return new FebsResponse().success().data(paymentService.findPayments(payment));
    }

    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("payment:view")
    public FebsResponse paymentList(QueryRequest request, Payment payment) {
        Map<String, Object> dataTable = getDataTable(this.paymentService.findPayments(request, payment));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增Payment", exceptionMessage = "新增Payment失败")
    @PostMapping
    @ResponseBody
    @RequiresPermissions("payment:add")
    public FebsResponse addPayment(@Valid Payment payment) {
        this.paymentService.createPayment(payment);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除Payment", exceptionMessage = "删除Payment失败")
    @GetMapping("delete/{paymentIds}")
    @ResponseBody
    @RequiresPermissions("payment:delete")
    public FebsResponse deletePayments(@NotBlank(message = "{required}") @PathVariable String paymentIds) {
        this.paymentService.deletePayments(paymentIds);
        return new FebsResponse().success();
    }


    @ControllerEndpoint(operation = "修改Payment", exceptionMessage = "修改Payment失败")
    @PostMapping("update")
    @ResponseBody
    @RequiresPermissions("payment:update")
    public FebsResponse updatePayment(Payment payment) {
        this.paymentService.updatePayment(payment);
        return new FebsResponse().success();
    }


    @ControllerEndpoint(operation = "修改Payment", exceptionMessage = "导出Excel失败")
    @PostMapping("excel")
    @ResponseBody
    @RequiresPermissions("payment:export")
    public void export(QueryRequest queryRequest, Payment payment, HttpServletResponse response) {
        List<Payment> payments = this.paymentService.findPayments(queryRequest, payment).getRecords();
        ExcelKit.$Export(Payment.class, response).downXlsx(payments, false);
    }
}
