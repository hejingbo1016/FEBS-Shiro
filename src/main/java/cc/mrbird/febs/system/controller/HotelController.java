package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.entity.File;
import cc.mrbird.febs.system.entity.Hotel;
import cc.mrbird.febs.system.entity.HotelName;
import cc.mrbird.febs.system.entity.Room;
import cc.mrbird.febs.system.service.IFileService;
import cc.mrbird.febs.system.service.IHotelService;
import cn.hutool.http.server.HttpServerRequest;
import com.wuwenze.poi.ExcelKit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author Hejingbo
 * @date 2020-08-11 21:01:27
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("hotel")
public class HotelController extends BaseController {

    private final IHotelService hotelService;
    private final IFileService fileService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "hotel")
    public String hotelIndex() {
        return FebsUtil.view("hotel/hotel");
    }

    @GetMapping
    public FebsResponse getAllHotels(Hotel hotel) {
        return new FebsResponse().success().data(hotelService.findHotels(hotel));
    }

    @GetMapping("list")
    @ResponseBody
    @RequiresPermissions("hotel:view")
    public FebsResponse hotelList(QueryRequest request, Hotel hotel) {
        Map<String, Object> dataTable = getDataTable(this.hotelService.findHotels(request, hotel));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增Hotel", exceptionMessage = "新增Hotel失败")
    @PostMapping
    @ResponseBody
    @RequiresPermissions("hotel:add")
    public FebsResponse addHotel(@Valid Hotel hotel, List<MultipartFile> file) {
        this.hotelService.createHotel(hotel);

        Long id = hotel.getId();
        if (id != null) {
            File fileEn = new File();
            fileEn.setForeignId(hotel.getId());
            fileService.deleteFile(fileEn);

            fileService.upload(file, "hotel", id);
        }

        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除Hotel", exceptionMessage = "删除Hotel失败")
    @GetMapping("delete/{deleteIds}")
    @ResponseBody
    @RequiresPermissions("hotel:delete")
    public FebsResponse deleteHotels(@NotBlank(message = "{required}") @PathVariable String deleteIds) {
        this.hotelService.deleteHotels(deleteIds);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改Hotel", exceptionMessage = "修改Hotel失败")
    @PostMapping("update")
    @ResponseBody
    @RequiresPermissions("hotel:update")
    @Transactional(rollbackFor = Exception.class)
    public FebsResponse updateHotel(Hotel hotel, List<MultipartFile> file) {
        this.hotelService.updateHotel(hotel);

        Long id = hotel.getId();
        if (id != null) {
            File fileEn = new File();
            fileEn.setForeignId(hotel.getId());
            fileService.deleteFile(fileEn);

            fileService.upload(file, "hotel", id);
        }

        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改Hotel", exceptionMessage = "导出Excel失败")
    @PostMapping("excel")
    @ResponseBody
    @RequiresPermissions("hotel:export")
    public void export(QueryRequest queryRequest, Hotel hotel, HttpServletResponse response) {
        List<Hotel> hotels = this.hotelService.findHotels(queryRequest, hotel).getRecords();
        ExcelKit.$Export(Hotel.class, response).downXlsx(hotels, false);
    }


    @ControllerEndpoint(operation = "异步加载所有的酒店", exceptionMessage = "异步加载所有的酒店失败")
    @GetMapping("getHotels")
    @ResponseBody
    @RequiresPermissions("hotel:view")
    public FebsResponse getHotels() {
        List<HotelName> hotels = hotelService.getHotels();
        return new FebsResponse().success().data(hotels);
    }


    @ControllerEndpoint(operation = "异步加载所有的酒店费用名称", exceptionMessage = "异步加载所有的酒店费用名称失败")
    @GetMapping("getHotelRooms")
    @ResponseBody
    @RequiresPermissions("hotel:view")
    public FebsResponse getHotelRooms(@Valid Long hotelId) {
        List<Room> roomList = hotelService.getHotelRooms(hotelId);
        return new FebsResponse().success().data(roomList);
    }


}
