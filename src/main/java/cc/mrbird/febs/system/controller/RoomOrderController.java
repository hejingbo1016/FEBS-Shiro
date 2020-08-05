package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.RoomOrder;
import cc.mrbird.febs.system.service.IRoomOrderService;
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
 * @date 2020-08-05 23:39:31
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class RoomOrderController extends BaseController {

    private final IRoomOrderService roomOrderService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "roomOrder")
    public String roomOrderIndex(){
        return FebsUtil.view("roomOrder/roomOrder");
    }

    @GetMapping("roomOrder")
    @ResponseBody
    @RequiresPermissions("roomOrder:list")
    public FebsResponse getAllRoomOrders(RoomOrder roomOrder) {
        return new FebsResponse().success().data(roomOrderService.findRoomOrders(roomOrder));
    }

    @GetMapping("roomOrder/list")
    @ResponseBody
    @RequiresPermissions("roomOrder:list")
    public FebsResponse roomOrderList(QueryRequest request, RoomOrder roomOrder) {
        Map<String, Object> dataTable = getDataTable(this.roomOrderService.findRoomOrders(request, roomOrder));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增RoomOrder", exceptionMessage = "新增RoomOrder失败")
    @PostMapping("roomOrder")
    @ResponseBody
    @RequiresPermissions("roomOrder:add")
    public FebsResponse addRoomOrder(@Valid RoomOrder roomOrder) {
        this.roomOrderService.createRoomOrder(roomOrder);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除RoomOrder", exceptionMessage = "删除RoomOrder失败")
    @GetMapping("roomOrder/delete")
    @ResponseBody
    @RequiresPermissions("roomOrder:delete")
    public FebsResponse deleteRoomOrder(RoomOrder roomOrder) {
        this.roomOrderService.deleteRoomOrder(roomOrder);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改RoomOrder", exceptionMessage = "修改RoomOrder失败")
    @PostMapping("roomOrder/update")
    @ResponseBody
    @RequiresPermissions("roomOrder:update")
    public FebsResponse updateRoomOrder(RoomOrder roomOrder) {
        this.roomOrderService.updateRoomOrder(roomOrder);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改RoomOrder", exceptionMessage = "导出Excel失败")
    @PostMapping("roomOrder/excel")
    @ResponseBody
    @RequiresPermissions("roomOrder:export")
    public void export(QueryRequest queryRequest, RoomOrder roomOrder, HttpServletResponse response) {
        List<RoomOrder> roomOrders = this.roomOrderService.findRoomOrders(queryRequest, roomOrder).getRecords();
        ExcelKit.$Export(RoomOrder.class, response).downXlsx(roomOrders, false);
    }
}
