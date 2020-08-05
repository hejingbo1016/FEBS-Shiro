package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.MeetingHotel;
import cc.mrbird.febs.system.service.IMeetingHotelService;
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
 * @date 2020-08-05 23:39:59
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class MeetingHotelController extends BaseController {

    private final IMeetingHotelService meetingHotelService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "meetingHotel")
    public String meetingHotelIndex(){
        return FebsUtil.view("meetingHotel/meetingHotel");
    }

    @GetMapping("meetingHotel")
    @ResponseBody
    @RequiresPermissions("meetingHotel:list")
    public FebsResponse getAllMeetingHotels(MeetingHotel meetingHotel) {
        return new FebsResponse().success().data(meetingHotelService.findMeetingHotels(meetingHotel));
    }

    @GetMapping("meetingHotel/list")
    @ResponseBody
    @RequiresPermissions("meetingHotel:list")
    public FebsResponse meetingHotelList(QueryRequest request, MeetingHotel meetingHotel) {
        Map<String, Object> dataTable = getDataTable(this.meetingHotelService.findMeetingHotels(request, meetingHotel));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增MeetingHotel", exceptionMessage = "新增MeetingHotel失败")
    @PostMapping("meetingHotel")
    @ResponseBody
    @RequiresPermissions("meetingHotel:add")
    public FebsResponse addMeetingHotel(@Valid MeetingHotel meetingHotel) {
        this.meetingHotelService.createMeetingHotel(meetingHotel);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除MeetingHotel", exceptionMessage = "删除MeetingHotel失败")
    @GetMapping("meetingHotel/delete")
    @ResponseBody
    @RequiresPermissions("meetingHotel:delete")
    public FebsResponse deleteMeetingHotel(MeetingHotel meetingHotel) {
        this.meetingHotelService.deleteMeetingHotel(meetingHotel);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改MeetingHotel", exceptionMessage = "修改MeetingHotel失败")
    @PostMapping("meetingHotel/update")
    @ResponseBody
    @RequiresPermissions("meetingHotel:update")
    public FebsResponse updateMeetingHotel(MeetingHotel meetingHotel) {
        this.meetingHotelService.updateMeetingHotel(meetingHotel);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改MeetingHotel", exceptionMessage = "导出Excel失败")
    @PostMapping("meetingHotel/excel")
    @ResponseBody
    @RequiresPermissions("meetingHotel:export")
    public void export(QueryRequest queryRequest, MeetingHotel meetingHotel, HttpServletResponse response) {
        List<MeetingHotel> meetingHotels = this.meetingHotelService.findMeetingHotels(queryRequest, meetingHotel).getRecords();
        ExcelKit.$Export(MeetingHotel.class, response).downXlsx(meetingHotels, false);
    }
}
