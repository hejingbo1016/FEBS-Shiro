package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.entity.RoomOrder;

import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  Service接口
 *
 * @author Hejingbo
 * @date 2020-08-05 23:39:31
 */
public interface IRoomOrderService extends IService<RoomOrder> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param roomOrder roomOrder
     * @return IPage<RoomOrder>
     */
    IPage<RoomOrder> findRoomOrders(QueryRequest request, RoomOrder roomOrder);

    /**
     * 查询（所有）
     *
     * @param roomOrder roomOrder
     * @return List<RoomOrder>
     */
    List<RoomOrder> findRoomOrders(RoomOrder roomOrder);

    /**
     * 新增
     *
     * @param roomOrder roomOrder
     */
    void createRoomOrder(RoomOrder roomOrder);

    /**
     * 修改
     *
     * @param roomOrder roomOrder
     */
    void updateRoomOrder(RoomOrder roomOrder);

    /**
     * 删除
     *
     * @param roomOrder roomOrder
     */
    void deleteRoomOrder(RoomOrder roomOrder);
}
