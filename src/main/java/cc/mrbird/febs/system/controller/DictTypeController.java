package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.DictType;
import cc.mrbird.febs.system.service.IDictTypeService;
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
 * @date 2020-08-05 23:40:43
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class DictTypeController extends BaseController {

    private final IDictTypeService dictTypeService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "dictType")
    public String dictTypeIndex(){
        return FebsUtil.view("dictType/dictType");
    }

    @GetMapping("dictType")
    @ResponseBody
    @RequiresPermissions("dictType:list")
    public FebsResponse getAllDictTypes(DictType dictType) {
        return new FebsResponse().success().data(dictTypeService.findDictTypes(dictType));
    }

    @GetMapping("dictType/list")
    @ResponseBody
    @RequiresPermissions("dictType:list")
    public FebsResponse dictTypeList(QueryRequest request, DictType dictType) {
        Map<String, Object> dataTable = getDataTable(this.dictTypeService.findDictTypes(request, dictType));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增DictType", exceptionMessage = "新增DictType失败")
    @PostMapping("dictType")
    @ResponseBody
    @RequiresPermissions("dictType:add")
    public FebsResponse addDictType(@Valid DictType dictType) {
        this.dictTypeService.createDictType(dictType);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除DictType", exceptionMessage = "删除DictType失败")
    @GetMapping("dictType/delete")
    @ResponseBody
    @RequiresPermissions("dictType:delete")
    public FebsResponse deleteDictType(DictType dictType) {
        this.dictTypeService.deleteDictType(dictType);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改DictType", exceptionMessage = "修改DictType失败")
    @PostMapping("dictType/update")
    @ResponseBody
    @RequiresPermissions("dictType:update")
    public FebsResponse updateDictType(DictType dictType) {
        this.dictTypeService.updateDictType(dictType);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改DictType", exceptionMessage = "导出Excel失败")
    @PostMapping("dictType/excel")
    @ResponseBody
    @RequiresPermissions("dictType:export")
    public void export(QueryRequest queryRequest, DictType dictType, HttpServletResponse response) {
        List<DictType> dictTypes = this.dictTypeService.findDictTypes(queryRequest, dictType).getRecords();
        ExcelKit.$Export(DictType.class, response).downXlsx(dictTypes, false);
    }
}
