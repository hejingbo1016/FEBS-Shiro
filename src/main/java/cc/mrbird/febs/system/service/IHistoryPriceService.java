package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.entity.HistoryPrice;

import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  Service接口
 *
 * @author Hejingbo
 * @date 2020-08-05 23:36:39
 */
public interface IHistoryPriceService extends IService<HistoryPrice> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param historyPrice historyPrice
     * @return IPage<HistoryPrice>
     */
    IPage<HistoryPrice> findHistoryPrices(QueryRequest request, HistoryPrice historyPrice);

    /**
     * 查询（所有）
     *
     * @param historyPrice historyPrice
     * @return List<HistoryPrice>
     */
    List<HistoryPrice> findHistoryPrices(HistoryPrice historyPrice);

    /**
     * 新增
     *
     * @param historyPrice historyPrice
     */
    void createHistoryPrice(HistoryPrice historyPrice);

    /**
     * 修改
     *
     * @param historyPrice historyPrice
     */
    void updateHistoryPrice(HistoryPrice historyPrice);

    /**
     * 删除
     *
     * @param historyPrice historyPrice
     */
    void deleteHistoryPrice(HistoryPrice historyPrice);
}
