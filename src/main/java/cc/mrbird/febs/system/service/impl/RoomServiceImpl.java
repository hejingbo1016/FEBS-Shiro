package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.Room;
import cc.mrbird.febs.system.mapper.RoomMapper;
import cc.mrbird.febs.system.service.IFileService;
import cc.mrbird.febs.system.service.IRoomService;
import org.apache.commons.lang3.StringUtils;
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
 * Service实现
 *
 * @author Hejingbo
 * @date 2020-08-05 23:39:18
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements IRoomService {

    private final RoomMapper roomMapper;

    @Override
    public IPage<Room> findRooms(QueryRequest request, Room room, String hotelId) {
        LambdaQueryWrapper<Room> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<Room> page = new Page<>(request.getPageNum(), request.getPageSize());
        page.setTotal(baseMapper.countRoom(room, hotelId));
        selectRoom(queryWrapper, room);
        return this.page(page, queryWrapper);
    }

    /**
     * 设置查询条件
     *
     * @param queryWrapper
     * @param room
     */
    private void selectRoom(LambdaQueryWrapper<Room> queryWrapper, Room room) {

        if (StringUtils.isNotBlank(room.getRoomType())) {
            queryWrapper.like(Room::getRoomType, room.getRoomType());
        }

    }

    @Override
    public List<Room> findRooms(Room room) {
        LambdaQueryWrapper<Room> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRoom(Room room) {
        this.save(room);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoom(Room room) {
        this.saveOrUpdate(room);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoom(Room room) {
        LambdaQueryWrapper<Room> wrapper = new LambdaQueryWrapper<>();
        // TODO 设置删除条件
        this.remove(wrapper);
    }
}
