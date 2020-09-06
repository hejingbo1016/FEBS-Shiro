package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.dto.JsonObjectPage;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.entity.Meeting;
import cc.mrbird.febs.system.service.IMeetingService;
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
 * Controller
 *
 * @author Hejingbo
 * @date 2020-08-05 23:40:11
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("meeting")
public class MeetingController extends BaseController {

    private final IMeetingService meetingService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "meeting")
    public String meetingIndex() {
        return FebsUtil.view("meeting/meeting");
    }

    @GetMapping
    public FebsResponse getAllMeetings(Meeting meeting) {
        return new FebsResponse().success().data(meetingService.findMeetings(meeting));
    }

    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("meeting:view")
    public FebsResponse meetingList(QueryRequest request, Meeting meeting) {
        Map<String, Object> dataTable = getDataTable(this.meetingService.findMeetings(request, meeting));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增Meeting", exceptionMessage = "新增Meeting失败")
    @PostMapping
    @ResponseBody
    @RequiresPermissions("meeting:add")
    public FebsResponse addMeeting(@Valid Meeting meeting) {
        this.meetingService.createMeeting(meeting);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除Meeting", exceptionMessage = "删除Meeting失败")
    @GetMapping("delete/{meetingIds}")
    @ResponseBody
    @RequiresPermissions("meeting:delete")
    public FebsResponse deletePhones(@NotBlank(message = "{required}") @PathVariable String meetingIds) {
        this.meetingService.deleteMeetings(meetingIds);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改Meeting", exceptionMessage = "修改Meeting失败")
    @PostMapping("update")
    @ResponseBody
    @RequiresPermissions("meeting:update")
    public FebsResponse updateMeeting(Meeting meeting) {
        this.meetingService.updateMeeting(meeting);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改Meeting", exceptionMessage = "导出Excel失败")
    @PostMapping("excel")
    @ResponseBody
    @RequiresPermissions("meeting:export")
    public void export(QueryRequest queryRequest, Meeting meeting, HttpServletResponse response) {
        List<Meeting> meetings = this.meetingService.findMeetings(queryRequest, meeting).getRecords();
        ExcelKit.$Export(Meeting.class, response).downXlsx(meetings, false);
    }


    @ControllerEndpoint(operation = "审核Meeting", exceptionMessage = "审核Meeting失败")
    @PostMapping("audit")
    @ResponseBody
    @RequiresPermissions("meeting:audit")
    public FebsResponse auditPhones(Meeting meeting) {
        this.meetingService.auditMeeting(meeting);
        return new FebsResponse().success();
    }


    @ControllerEndpoint(operation = "微信端获取会议列表", exceptionMessage = "微信获取会议列表失败")
    @GetMapping("weChatMettingList")
    @ResponseBody
    public FebsResponse weChatMettingList(QueryRequest request, Meeting meeting) {
        Map<String, Object> dataTable = getDataTable(this.meetingService.weChatMettingList(request, meeting));
        return new FebsResponse().success().data(dataTable);
    }


    @ControllerEndpoint(operation = "微信端通过会议id获取详细信息", exceptionMessage = "微信端通过会议id获取详细信息")
    @PostMapping("getWeChatMettingById")
    @ResponseBody
    public JsonObjectPage getWeChatMettingById(Long id) {
        return meetingService.getWeChatMettingById(id);
    }


    

}
