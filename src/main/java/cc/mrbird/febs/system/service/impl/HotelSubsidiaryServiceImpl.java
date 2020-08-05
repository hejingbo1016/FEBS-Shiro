package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.HotelSubsidiary;
import cc.mrbird.febs.system.mapper.HotelSubsidiaryMapper;
import cc.mrbird.febs.system.service.IHotelSubsidiaryService;
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
 * @date 2020-08-05 23:36:25
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class HotelSubsidiaryServiceImpl extends ServiceImpl<HotelSubsidiaryMapper, HotelSubsidiary> implements IHotelSubsidiaryService {

    private final HotelSubsidiaryMapper hotelSubsidiaryMapper;

    @Override
    public IPage<HotelSubsidiary> findHotelSubsidiarys(QueryRequest request, HotelSubsidiary hotelSubsidiary) {
        LambdaQueryWrapper<HotelSubsidiary> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<HotelSubsidiary> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<HotelSubsidiary> findHotelSubsidiarys(HotelSubsidiary hotelSubsidiary) {
	    LambdaQueryWrapper<HotelSubsidiary> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createHotelSubsidiary(HotelSubsidiary hotelSubsidiary) {
        this.save(hotelSubsidiary);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateHotelSubsidiary(HotelSubsidiary hotelSubsidiary) {
        this.saveOrUpdate(hotelSubsidiary);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteHotelSubsidiary(HotelSubsidiary hotelSubsidiary) {
        LambdaQueryWrapper<HotelSubsidiary> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}
}
