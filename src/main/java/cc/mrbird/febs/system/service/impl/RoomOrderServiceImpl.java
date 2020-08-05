package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.RoomOrder;
import cc.mrbird.febs.system.mapper.RoomOrderMapper;
import cc.mrbird.febs.system.service.IRoomOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import lombok.RequiredArgsConstructor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 *  Service实现
 *
 * @author Hejingbo
 * @date 2020-08-05 23:39:31
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class RoomOrderServiceImpl extends ServiceImpl<RoomOrderMapper, RoomOrder> implements IRoomOrderService {

    private final RoomOrderMapper roomOrderMapper;

    @Override
    public IPage<RoomOrder> findRoomOrders(QueryRequest request, RoomOrder roomOrder) {
        LambdaQueryWrapper<RoomOrder> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<RoomOrder> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<RoomOrder> findRoomOrders(RoomOrder roomOrder) {
	    LambdaQueryWrapper<RoomOrder> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRoomOrder(RoomOrder roomOrder) {
        this.save(roomOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoomOrder(RoomOrder roomOrder) {
        this.saveOrUpdate(roomOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoomOrder(RoomOrder roomOrder) {
        LambdaQueryWrapper<RoomOrder> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}
}
