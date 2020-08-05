package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.HistoryPrice;
import cc.mrbird.febs.system.mapper.HistoryPriceMapper;
import cc.mrbird.febs.system.service.IHistoryPriceService;
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
 * @date 2020-08-05 23:36:39
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class HistoryPriceServiceImpl extends ServiceImpl<HistoryPriceMapper, HistoryPrice> implements IHistoryPriceService {

    private final HistoryPriceMapper historyPriceMapper;

    @Override
    public IPage<HistoryPrice> findHistoryPrices(QueryRequest request, HistoryPrice historyPrice) {
        LambdaQueryWrapper<HistoryPrice> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<HistoryPrice> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<HistoryPrice> findHistoryPrices(HistoryPrice historyPrice) {
	    LambdaQueryWrapper<HistoryPrice> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createHistoryPrice(HistoryPrice historyPrice) {
        this.save(historyPrice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateHistoryPrice(HistoryPrice historyPrice) {
        this.saveOrUpdate(historyPrice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteHistoryPrice(HistoryPrice historyPrice) {
        LambdaQueryWrapper<HistoryPrice> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}
}
