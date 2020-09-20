package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.BusinessRuntimeException;
import cc.mrbird.febs.common.utils.Snowflake;
import cc.mrbird.febs.system.constants.AdminConstants;
import cc.mrbird.febs.system.entity.OrderInvoice;
import cc.mrbird.febs.system.mapper.OrderInvoiceMapper;
import cc.mrbird.febs.system.service.IOrderInvoiceService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * Service实现
 *
 * @author Hejingbo
 * @date 2020-09-20 11:13:52
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class OrderInvoiceServiceImpl extends ServiceImpl<OrderInvoiceMapper, OrderInvoice> implements IOrderInvoiceService {

    private final OrderInvoiceMapper orderInvoiceMapper;
    private static Snowflake snowflake = Snowflake.getInstanceSnowflake();

    @Override
    public IPage<OrderInvoice> findOrderInvoices(QueryRequest request, OrderInvoice orderInvoice) {
        LambdaQueryWrapper<OrderInvoice> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<OrderInvoice> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<OrderInvoice> findOrderInvoices(OrderInvoice orderInvoice) {
        LambdaQueryWrapper<OrderInvoice> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOrderInvoice(OrderInvoice orderInvoice) {
        if (!StringUtils.isEmpty(orderInvoice.getPaymentCode())) {
            LambdaQueryWrapper<OrderInvoice> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OrderInvoice::getPaymentCode, orderInvoice.getPaymentCode());
            wrapper.eq(OrderInvoice::getDeleted, AdminConstants.DATA_N_DELETED);
            //通过订单编号查是否存在该发票信息
            OrderInvoice invoice = orderInvoiceMapper.selectOne(wrapper);
            if (Objects.isNull(invoice)) {
                orderInvoice.setId(snowflake.nextId());
            } else {
                orderInvoice.setId(invoice.getId());
            }
            this.saveOrUpdate(orderInvoice);
        } else {
            throw new BusinessRuntimeException("订单编号不能为空!");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderInvoice(OrderInvoice orderInvoice) {
        this.saveOrUpdate(orderInvoice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrderInvoice(OrderInvoice orderInvoice) {
        LambdaQueryWrapper<OrderInvoice> wrapper = new LambdaQueryWrapper<>();
        // TODO 设置删除条件
        this.remove(wrapper);
    }
}
