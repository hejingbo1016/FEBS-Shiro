package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.Room;
import cc.mrbird.febs.system.mapper.RoomMapper;
import cc.mrbird.febs.system.service.IRoomService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import lombok.RequiredArgsConstructor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.util.StringUtils;

import java.util.Arrays;
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
    public IPage<Room> findRooms(QueryRequest request, Room room) {
        LambdaQueryWrapper<Room> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<Room> page = new Page<>(request.getPageNum(), request.getPageSize());
        page.setTotal(baseMapper.countRoom(room));
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

        if (!StringUtils.isEmpty(room.getRoomName())) {
            queryWrapper.like(Room::getRoomName, room.getRoomName());
        }
        if (!StringUtils.isEmpty(room.getHotelId())) {
            queryWrapper.eq(Room::getHotelId, room.getHotelId());

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

    @Override
    public void deleteRooms(String roomIds) {
        List<String> list = Arrays.asList(roomIds.split(StringPool.COMMA));
        this.baseMapper.delete(new QueryWrapper<Room>().lambda().in(Room::getId, list));

    }
}
