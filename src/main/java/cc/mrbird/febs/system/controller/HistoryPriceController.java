package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.HistoryPrice;
import cc.mrbird.febs.system.service.IHistoryPriceService;
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
 * @date 2020-08-05 23:36:39
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class HistoryPriceController extends BaseController {

    private final IHistoryPriceService historyPriceService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "historyPrice")
    public String historyPriceIndex(){
        return FebsUtil.view("historyPrice/historyPrice");
    }

    @GetMapping("historyPrice")
    @ResponseBody
    @RequiresPermissions("historyPrice:list")
    public FebsResponse getAllHistoryPrices(HistoryPrice historyPrice) {
        return new FebsResponse().success().data(historyPriceService.findHistoryPrices(historyPrice));
    }

    @GetMapping("historyPrice/list")
    @ResponseBody
    @RequiresPermissions("historyPrice:list")
    public FebsResponse historyPriceList(QueryRequest request, HistoryPrice historyPrice) {
        Map<String, Object> dataTable = getDataTable(this.historyPriceService.findHistoryPrices(request, historyPrice));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增HistoryPrice", exceptionMessage = "新增HistoryPrice失败")
    @PostMapping("historyPrice")
    @ResponseBody
    @RequiresPermissions("historyPrice:add")
    public FebsResponse addHistoryPrice(@Valid HistoryPrice historyPrice) {
        this.historyPriceService.createHistoryPrice(historyPrice);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除HistoryPrice", exceptionMessage = "删除HistoryPrice失败")
    @GetMapping("historyPrice/delete")
    @ResponseBody
    @RequiresPermissions("historyPrice:delete")
    public FebsResponse deleteHistoryPrice(HistoryPrice historyPrice) {
        this.historyPriceService.deleteHistoryPrice(historyPrice);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改HistoryPrice", exceptionMessage = "修改HistoryPrice失败")
    @PostMapping("historyPrice/update")
    @ResponseBody
    @RequiresPermissions("historyPrice:update")
    public FebsResponse updateHistoryPrice(HistoryPrice historyPrice) {
        this.historyPriceService.updateHistoryPrice(historyPrice);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改HistoryPrice", exceptionMessage = "导出Excel失败")
    @PostMapping("historyPrice/excel")
    @ResponseBody
    @RequiresPermissions("historyPrice:export")
    public void export(QueryRequest queryRequest, HistoryPrice historyPrice, HttpServletResponse response) {
        List<HistoryPrice> historyPrices = this.historyPriceService.findHistoryPrices(queryRequest, historyPrice).getRecords();
        ExcelKit.$Export(HistoryPrice.class, response).downXlsx(historyPrices, false);
    }
}
