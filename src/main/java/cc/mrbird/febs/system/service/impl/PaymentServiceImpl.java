package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.Snowflake;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.entity.Payment;
import cc.mrbird.febs.system.mapper.PaymentMapper;
import cc.mrbird.febs.system.service.IPaymentService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service实现
 *
 * @author Hejingbo
 * @date 2020-09-05 21:06:30
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements IPaymentService {
    private static Snowflake snowflake = Snowflake.getInstanceSnowflake();

    private final PaymentMapper paymentMapper;

    @Override
    public IPage<Payment> findPayments(QueryRequest request, Payment payment) {
        LambdaQueryWrapper<Payment> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<Payment> page = new Page<>(request.getPageNum(), request.getPageSize());
        page.setSearchCount(false);
        page.setTotal(paymentMapper.countPayment(payment));

        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_ASC, true);
        IPage<Payment> paymentPage = paymentMapper.findPaymentPage(page, payment);

        return paymentPage;
    }

    @Override
    public List<Payment> findPayments(Payment payment) {
        return null;
    }


    /**
     * 设置查询条件
     *
     * @param queryWrapper
     * @param payment
     */
    private void setSelectLike(LambdaQueryWrapper<Payment> queryWrapper, Payment payment) {

        if (StringUtils.isNotBlank(payment.getMeetingName())) {
            queryWrapper.like(Payment::getMeetingName, payment.getMeetingName());
        }
        if (StringUtils.isNotBlank(payment.getHotelName())) {
            queryWrapper.like(Payment::getHotelName, payment.getHotelName());
        }
        if (StringUtils.isNotBlank(payment.getPaymentCode())) {
            queryWrapper.like(Payment::getPaymentCode, payment.getPaymentCode());
        }
        if (payment.getPayType() != null) {
            queryWrapper.eq(Payment::getPayType, payment.getPayType());
        }


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPayment(Payment payment) {
        payment.setId(snowflake.nextId());
        paymentMapper.insertPayment(payment);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePayment(Payment payment) {
        this.saveOrUpdate(payment);
    }


    @Override
    public void deletePayments(String paymentIds) {
        paymentMapper.deletePayments(paymentIds);

    }

    @Override
    public Payment selectByPaymentId(Long id) {
        return paymentMapper.selectByPaymentId(id);
    }


}
