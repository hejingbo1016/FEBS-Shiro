package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.entity.Room;
import cc.mrbird.febs.system.service.IRoomService;
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
 * @date 2020-08-05 23:39:18
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@RequestMapping("room")
public class RoomController extends BaseController {

    private final IRoomService roomService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "room")
    public String roomIndex() {
        return FebsUtil.view("room/room");
    }

    @GetMapping
    public FebsResponse getAllRooms(Room room) {
        return new FebsResponse().success().data(roomService.findRooms(room));
    }

    @GetMapping("list/{hotelId}")
    @ResponseBody
    @RequiresPermissions("hotel:room")
    public FebsResponse roomList(QueryRequest request, Room room) {
        Map<String, Object> dataTable = getDataTable(this.roomService.findRooms(request, room));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增Room", exceptionMessage = "新增Room失败")
    @PostMapping("add")
    @ResponseBody
    @RequiresPermissions("room:add")
    public FebsResponse addRoom(@Valid Room room) {
        if (!Objects.isNull(room)) {
            if (!StringUtils.isEmpty(room.getId())) {
                this.roomService.updateRoom(room);
            } else {
                this.roomService.createRoom(room);
            }
        }
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除Room", exceptionMessage = "删除Room失败")
    @GetMapping("delete/{roomIds}")
    @ResponseBody
    @RequiresPermissions("room:delete")
    public FebsResponse deleteRoom(@NotBlank(message = "{required}") @PathVariable String roomIds) {
        this.roomService.deleteRooms(roomIds);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改Room", exceptionMessage = "修改Room失败")
    @PostMapping("update")
    @ResponseBody
    @RequiresPermissions("room:update")
    public FebsResponse updateRoom(Room room) {
        this.roomService.updateRoom(room);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改Room", exceptionMessage = "导出Excel失败")
    @PostMapping("room/excel")
    @ResponseBody
    @RequiresPermissions("room:export")
    public void export(QueryRequest queryRequest, Room room, HttpServletResponse response, String hotelId) {
        List<Room> rooms = this.roomService.findRooms(queryRequest, room).getRecords();
        ExcelKit.$Export(Room.class, response).downXlsx(rooms, false);
    }
}
