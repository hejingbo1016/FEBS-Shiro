package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.HotelSubsidiary;
import cc.mrbird.febs.system.service.IHotelSubsidiaryService;
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
 * @date 2020-08-05 23:36:25
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class HotelSubsidiaryController extends BaseController {

    private final IHotelSubsidiaryService hotelSubsidiaryService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "hotelSubsidiary")
    public String hotelSubsidiaryIndex(){
        return FebsUtil.view("hotelSubsidiary/hotelSubsidiary");
    }

    @GetMapping("hotelSubsidiary")
    @ResponseBody
    @RequiresPermissions("hotelSubsidiary:list")
    public FebsResponse getAllHotelSubsidiarys(HotelSubsidiary hotelSubsidiary) {
        return new FebsResponse().success().data(hotelSubsidiaryService.findHotelSubsidiarys(hotelSubsidiary));
    }

    @GetMapping("hotelSubsidiary/list")
    @ResponseBody
    @RequiresPermissions("hotelSubsidiary:list")
    public FebsResponse hotelSubsidiaryList(QueryRequest request, HotelSubsidiary hotelSubsidiary) {
        Map<String, Object> dataTable = getDataTable(this.hotelSubsidiaryService.findHotelSubsidiarys(request, hotelSubsidiary));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增HotelSubsidiary", exceptionMessage = "新增HotelSubsidiary失败")
    @PostMapping("hotelSubsidiary")
    @ResponseBody
    @RequiresPermissions("hotelSubsidiary:add")
    public FebsResponse addHotelSubsidiary(@Valid HotelSubsidiary hotelSubsidiary) {
        this.hotelSubsidiaryService.createHotelSubsidiary(hotelSubsidiary);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除HotelSubsidiary", exceptionMessage = "删除HotelSubsidiary失败")
    @GetMapping("hotelSubsidiary/delete")
    @ResponseBody
    @RequiresPermissions("hotelSubsidiary:delete")
    public FebsResponse deleteHotelSubsidiary(HotelSubsidiary hotelSubsidiary) {
        this.hotelSubsidiaryService.deleteHotelSubsidiary(hotelSubsidiary);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改HotelSubsidiary", exceptionMessage = "修改HotelSubsidiary失败")
    @PostMapping("hotelSubsidiary/update")
    @ResponseBody
    @RequiresPermissions("hotelSubsidiary:update")
    public FebsResponse updateHotelSubsidiary(HotelSubsidiary hotelSubsidiary) {
        this.hotelSubsidiaryService.updateHotelSubsidiary(hotelSubsidiary);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改HotelSubsidiary", exceptionMessage = "导出Excel失败")
    @PostMapping("hotelSubsidiary/excel")
    @ResponseBody
    @RequiresPermissions("hotelSubsidiary:export")
    public void export(QueryRequest queryRequest, HotelSubsidiary hotelSubsidiary, HttpServletResponse response) {
        List<HotelSubsidiary> hotelSubsidiarys = this.hotelSubsidiaryService.findHotelSubsidiarys(queryRequest, hotelSubsidiary).getRecords();
        ExcelKit.$Export(HotelSubsidiary.class, response).downXlsx(hotelSubsidiarys, false);
    }
}
