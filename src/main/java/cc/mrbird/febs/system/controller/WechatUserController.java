package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.entity.WechatUser;
import cc.mrbird.febs.system.service.IWechatUserService;
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
 *  Controller
 *
 * @author Hejingbo
 * @date 2020-08-05 23:39:42
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("weChatUser")
public class WechatUserController extends BaseController {

    private final IWechatUserService wechatUserService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "weChatUser")
    public String wechatUserIndex(){
        return FebsUtil.view("weChatUser/weChatUser");
    }

    @GetMapping
    public FebsResponse getAllWechatUsers(WechatUser wechatUser) {
        return new FebsResponse().success().data(wechatUserService.findWeChatUsers(wechatUser));
    }

    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("weChatUser:view")
    public FebsResponse weChatUserList(QueryRequest request, WechatUser wechatUser) {
        Map<String, Object> dataTable = getDataTable(this.wechatUserService.findWeChatUsers(request, wechatUser));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增WechatUser", exceptionMessage = "新增WechatUser失败")
    @PostMapping
    @ResponseBody
    @RequiresPermissions("weChatUser:add")
    public FebsResponse addWeChatUser(@Valid WechatUser wechatUser) {
        this.wechatUserService.createWechatUser(wechatUser);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除WechatUser", exceptionMessage = "删除WechatUser失败")
    @GetMapping("delete/{weChatUserIds}")
    @ResponseBody
    @RequiresPermissions("weChatUser:delete")
    public FebsResponse deleteWeChatUsers(@NotBlank(message = "{required}") @PathVariable String weChatUserIds) {

        this.wechatUserService.deleteWeChatUsersByIds(weChatUserIds);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改WechatUser", exceptionMessage = "修改WechatUser失败")
    @PostMapping("update")
    @ResponseBody
    @RequiresPermissions("weChatUser:update")
    public FebsResponse updateWechatUser(WechatUser wechatUser) {
        this.wechatUserService.updateWechatUser(wechatUser);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改WechatUser", exceptionMessage = "导出Excel失败")
    @PostMapping("excel")
    @ResponseBody
    @RequiresPermissions("weChatUser:export")
    public void export(QueryRequest queryRequest, WechatUser wechatUser, HttpServletResponse response) {
        List<WechatUser> wechatUsers = this.wechatUserService.findWeChatUsers(queryRequest, wechatUser).getRecords();
        ExcelKit.$Export(WechatUser.class, response).downXlsx(wechatUsers, false);
    }
}
