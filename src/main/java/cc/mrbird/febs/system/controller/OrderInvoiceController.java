package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.OrderInvoice;
import cc.mrbird.febs.system.service.IOrderInvoiceService;
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
 * @date 2020-09-20 11:13:52
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class OrderInvoiceController extends BaseController {

    private final IOrderInvoiceService orderInvoiceService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "orderInvoice")
    public String orderInvoiceIndex(){
        return FebsUtil.view("orderInvoice/orderInvoice");
    }

    @GetMapping("orderInvoice")
    @ResponseBody
    @RequiresPermissions("orderInvoice:list")
    public FebsResponse getAllOrderInvoices(OrderInvoice orderInvoice) {
        return new FebsResponse().success().data(orderInvoiceService.findOrderInvoices(orderInvoice));
    }

    @GetMapping("orderInvoice/list")
    @ResponseBody
    @RequiresPermissions("orderInvoice:list")
    public FebsResponse orderInvoiceList(QueryRequest request, OrderInvoice orderInvoice) {
        Map<String, Object> dataTable = getDataTable(this.orderInvoiceService.findOrderInvoices(request, orderInvoice));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增OrderInvoice", exceptionMessage = "新增OrderInvoice失败")
    @PostMapping("orderInvoice")
    @ResponseBody
    @RequiresPermissions("orderInvoice:add")
    public FebsResponse addOrderInvoice(@Valid OrderInvoice orderInvoice) {
        this.orderInvoiceService.createOrderInvoice(orderInvoice);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除OrderInvoice", exceptionMessage = "删除OrderInvoice失败")
    @GetMapping("orderInvoice/delete")
    @ResponseBody
    @RequiresPermissions("orderInvoice:delete")
    public FebsResponse deleteOrderInvoice(OrderInvoice orderInvoice) {
        this.orderInvoiceService.deleteOrderInvoice(orderInvoice);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改OrderInvoice", exceptionMessage = "修改OrderInvoice失败")
    @PostMapping("orderInvoice/update")
    @ResponseBody
    @RequiresPermissions("orderInvoice:update")
    public FebsResponse updateOrderInvoice(OrderInvoice orderInvoice) {
        this.orderInvoiceService.updateOrderInvoice(orderInvoice);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改OrderInvoice", exceptionMessage = "导出Excel失败")
    @PostMapping("orderInvoice/excel")
    @ResponseBody
    @RequiresPermissions("orderInvoice:export")
    public void export(QueryRequest queryRequest, OrderInvoice orderInvoice, HttpServletResponse response) {
        List<OrderInvoice> orderInvoices = this.orderInvoiceService.findOrderInvoices(queryRequest, orderInvoice).getRecords();
        ExcelKit.$Export(OrderInvoice.class, response).downXlsx(orderInvoices, false);
    }
}
