package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.entity.Room;

import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  Service接口
 *
 * @author Hejingbo
 * @date 2020-08-05 23:39:18
 */
public interface IRoomService extends IService<Room> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param room room
     * @param hotelId
     * @return IPage<Room>
     */
    IPage<Room> findRooms(QueryRequest request, Room room, String hotelId);

    /**
     * 查询（所有）
     *
     * @param room room
     * @return List<Room>
     */
    List<Room> findRooms(Room room);

    /**
     * 新增
     *
     * @param room room
     */
    void createRoom(Room room);

    /**
     * 修改
     *
     * @param room room
     */
    void updateRoom(Room room);

    /**
     * 删除
     *
     * @param room room
     */
    void deleteRoom(Room room);

    void deleteRooms(String roomIds);
}
