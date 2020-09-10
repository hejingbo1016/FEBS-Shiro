package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.entity.Hotel;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.HotelName;
import cc.mrbird.febs.system.entity.Room;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * Service接口
 *
 * @author Hejingbo
 * @date 2020-08-11 21:01:27
 */
public interface IHotelService extends IService<Hotel> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param hotel   hotel
     * @return IPage<Hotel>
     */
    IPage<Hotel> findHotels(QueryRequest request, Hotel hotel);

    /**
     * 查询（所有）
     *
     * @param hotel hotel
     * @return List<Hotel>
     */
    List<Hotel> findHotels(Hotel hotel);

    /**
     * 新增
     *
     * @param hotel hotel
     */
    void createHotel(Hotel hotel);

    /**
     * 修改
     *
     * @param hotel hotel
     */
    void updateHotel(Hotel hotel);

    /**
     * 删除
     *
     * @param hotel hotel
     */
    void deleteHotel(Hotel hotel);

    void deleteHotels(String deleteIds);

    List<HotelName> getHotels();

    List<Room> getHotelRooms(Long hotelId);
}
