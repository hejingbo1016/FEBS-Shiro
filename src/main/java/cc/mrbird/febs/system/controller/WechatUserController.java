package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.WechatUser;
import cc.mrbird.febs.system.service.IWechatUserService;
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
 * @date 2020-08-05 23:39:42
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class WechatUserController extends BaseController {

    private final IWechatUserService wechatUserService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "wechatUser")
    public String wechatUserIndex(){
        return FebsUtil.view("wechatUser/wechatUser");
    }

    @GetMapping("wechatUser")
    @ResponseBody
    @RequiresPermissions("wechatUser:list")
    public FebsResponse getAllWechatUsers(WechatUser wechatUser) {
        return new FebsResponse().success().data(wechatUserService.findWechatUsers(wechatUser));
    }

    @GetMapping("wechatUser/list")
    @ResponseBody
    @RequiresPermissions("wechatUser:list")
    public FebsResponse wechatUserList(QueryRequest request, WechatUser wechatUser) {
        Map<String, Object> dataTable = getDataTable(this.wechatUserService.findWechatUsers(request, wechatUser));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增WechatUser", exceptionMessage = "新增WechatUser失败")
    @PostMapping("wechatUser")
    @ResponseBody
    @RequiresPermissions("wechatUser:add")
    public FebsResponse addWechatUser(@Valid WechatUser wechatUser) {
        this.wechatUserService.createWechatUser(wechatUser);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除WechatUser", exceptionMessage = "删除WechatUser失败")
    @GetMapping("wechatUser/delete")
    @ResponseBody
    @RequiresPermissions("wechatUser:delete")
    public FebsResponse deleteWechatUser(WechatUser wechatUser) {
        this.wechatUserService.deleteWechatUser(wechatUser);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改WechatUser", exceptionMessage = "修改WechatUser失败")
    @PostMapping("wechatUser/update")
    @ResponseBody
    @RequiresPermissions("wechatUser:update")
    public FebsResponse updateWechatUser(WechatUser wechatUser) {
        this.wechatUserService.updateWechatUser(wechatUser);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改WechatUser", exceptionMessage = "导出Excel失败")
    @PostMapping("wechatUser/excel")
    @ResponseBody
    @RequiresPermissions("wechatUser:export")
    public void export(QueryRequest queryRequest, WechatUser wechatUser, HttpServletResponse response) {
        List<WechatUser> wechatUsers = this.wechatUserService.findWechatUsers(queryRequest, wechatUser).getRecords();
        ExcelKit.$Export(WechatUser.class, response).downXlsx(wechatUsers, false);
    }
}
