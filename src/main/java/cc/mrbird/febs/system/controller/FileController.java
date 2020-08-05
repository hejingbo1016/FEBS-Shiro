package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.File;
import cc.mrbird.febs.system.service.IFileService;
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
 * @date 2020-08-05 23:40:32
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class FileController extends BaseController {

    private final IFileService fileService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "file")
    public String fileIndex(){
        return FebsUtil.view("file/file");
    }

    @GetMapping("file")
    @ResponseBody
    @RequiresPermissions("file:list")
    public FebsResponse getAllFiles(File file) {
        return new FebsResponse().success().data(fileService.findFiles(file));
    }

    @GetMapping("file/list")
    @ResponseBody
    @RequiresPermissions("file:list")
    public FebsResponse fileList(QueryRequest request, File file) {
        Map<String, Object> dataTable = getDataTable(this.fileService.findFiles(request, file));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增File", exceptionMessage = "新增File失败")
    @PostMapping("file")
    @ResponseBody
    @RequiresPermissions("file:add")
    public FebsResponse addFile(@Valid File file) {
        this.fileService.createFile(file);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除File", exceptionMessage = "删除File失败")
    @GetMapping("file/delete")
    @ResponseBody
    @RequiresPermissions("file:delete")
    public FebsResponse deleteFile(File file) {
        this.fileService.deleteFile(file);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改File", exceptionMessage = "修改File失败")
    @PostMapping("file/update")
    @ResponseBody
    @RequiresPermissions("file:update")
    public FebsResponse updateFile(File file) {
        this.fileService.updateFile(file);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改File", exceptionMessage = "导出Excel失败")
    @PostMapping("file/excel")
    @ResponseBody
    @RequiresPermissions("file:export")
    public void export(QueryRequest queryRequest, File file, HttpServletResponse response) {
        List<File> files = this.fileService.findFiles(queryRequest, file).getRecords();
        ExcelKit.$Export(File.class, response).downXlsx(files, false);
    }
}
