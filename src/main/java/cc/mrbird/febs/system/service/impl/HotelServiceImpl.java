package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.constants.AdminConstants;
import cc.mrbird.febs.system.entity.Hotel;
import cc.mrbird.febs.system.entity.HotelName;
import cc.mrbird.febs.system.entity.Room;
import cc.mrbird.febs.system.mapper.FileMapper;
import cc.mrbird.febs.system.mapper.HotelMapper;
import cc.mrbird.febs.system.service.IHotelService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * Service实现
 *
 * @author Hejingbo
 * @date 2020-08-11 21:01:27
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class HotelServiceImpl extends ServiceImpl<HotelMapper, Hotel> implements IHotelService {

    private final HotelMapper hotelMapper;
    private final FileMapper fileMapper;

    @Override
    public IPage<Hotel> findHotels(QueryRequest request, Hotel hotel) {
        LambdaQueryWrapper<Hotel> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<Hotel> page = new Page<>(request.getPageNum(), request.getPageSize());
        page.setSearchCount(false);
        page.setTotal(baseMapper.countHotel(hotel));
        setselectHotel(queryWrapper, hotel);
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_ASC, true);


        return this.page(page, queryWrapper);
    }

    private void setselectHotel(LambdaQueryWrapper<Hotel> queryWrapper, Hotel hotel) {
        if (StringUtils.isNotBlank(hotel.getHotelName())) {
            queryWrapper.like(Hotel::getHotelName, hotel.getHotelName());
        }
        if (StringUtils.isNotBlank(hotel.getHotelPrincipal())) {
            queryWrapper.like(Hotel::getHotelPrincipal, hotel.getHotelPrincipal());
        }
        if (StringUtils.isNotBlank(hotel.getContactPhone())) {
            queryWrapper.like(Hotel::getContactPhone, hotel.getContactPhone());
        }
        if (StringUtils.isNotBlank(hotel.getReceptionService())) {
            queryWrapper.like(Hotel::getReceptionService, hotel.getReceptionService());
        }
        if (StringUtils.isNotBlank(hotel.getHotelAddress())) {
            queryWrapper.like(Hotel::getHotelAddress, hotel.getHotelAddress());
        }
        queryWrapper.eq(Hotel::getDeleted, AdminConstants.DATA_N_DELETED);


    }

    @Override
    public List<Hotel> findHotels(Hotel hotel) {
        LambdaQueryWrapper<Hotel> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createHotel(Hotel hotel) {
        this.save(hotel);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateHotel(Hotel hotel) {
        this.saveOrUpdate(hotel);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteHotel(Hotel hotel) {
        LambdaQueryWrapper<Hotel> wrapper = new LambdaQueryWrapper<>();
        // TODO 设置删除条件
        this.remove(wrapper);
    }

    @Override
    public void deleteHotels(String deleteIds) {
        List<String> list = Arrays.asList(deleteIds.split(StringPool.COMMA));
        this.baseMapper.delete(new QueryWrapper<Hotel>().lambda().in(Hotel::getId, list));
        //删除酒店对应的附件
        String[] ids = deleteIds.split(",");
        fileMapper.deletesByFids(ids);
    }

    @Override
    public List<HotelName> getHotels() {
        List<HotelName> list = baseMapper.getHotels();
        return list;
    }

    @Override
    public List<Room> getHotelRooms(Long hotelId) {
        List<Room> roomList = baseMapper.getHotelRooms(hotelId);
        return roomList;
    }
}
