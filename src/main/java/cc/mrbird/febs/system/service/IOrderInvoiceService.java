package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.entity.OrderInvoice;

import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  Service接口
 *
 * @author Hejingbo
 * @date 2020-09-20 11:13:52
 */
public interface IOrderInvoiceService extends IService<OrderInvoice> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param orderInvoice orderInvoice
     * @return IPage<OrderInvoice>
     */
    IPage<OrderInvoice> findOrderInvoices(QueryRequest request, OrderInvoice orderInvoice);

    /**
     * 查询（所有）
     *
     * @param orderInvoice orderInvoice
     * @return List<OrderInvoice>
     */
    List<OrderInvoice> findOrderInvoices(OrderInvoice orderInvoice);

    /**
     * 新增
     *
     * @param orderInvoice orderInvoice
     */
    void createOrderInvoice(OrderInvoice orderInvoice);

    /**
     * 修改
     *
     * @param orderInvoice orderInvoice
     */
    void updateOrderInvoice(OrderInvoice orderInvoice);

    /**
     * 删除
     *
     * @param orderInvoice orderInvoice
     */
    void deleteOrderInvoice(OrderInvoice orderInvoice);
}
