package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.Snowflake;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.entity.PaymentDetails;
import cc.mrbird.febs.system.mapper.PaymentDetailsMapper;
import cc.mrbird.febs.system.enums.paymentTypeEnums;
import cc.mrbird.febs.system.service.IPaymentDetailsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service实现
 *
 * @author Hejingbo
 * @date 2020-09-05 21:06:26
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class PaymentDetailsServiceImpl extends ServiceImpl<PaymentDetailsMapper, PaymentDetails> implements IPaymentDetailsService {
    private static Snowflake snowflake = Snowflake.getInstanceSnowflake();

    private final PaymentDetailsMapper paymentDetailsMapper;

    @Override
    public IPage<PaymentDetails> findPaymentDetailss(QueryRequest request, PaymentDetails paymentDetails) {
        Page<PaymentDetails> page = new Page<>(request.getPageNum(), request.getPageSize());
        page.setTotal(baseMapper.countPaymentDetails(paymentDetails));
        IPage<PaymentDetails> paymentDetailsIPage = paymentDetailsMapper.findPaymentDetailsPage(page, paymentDetails);
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_ASC, true);
        return paymentDetailsIPage;
    }

    @Override
    public List<PaymentDetails> findPaymentDetailss(PaymentDetails paymentDetails) {
        LambdaQueryWrapper<PaymentDetails> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPaymentDetails(PaymentDetails paymentDetails) {
        this.save(paymentDetails);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePaymentDetails(PaymentDetails paymentDetails) {
        this.saveOrUpdate(paymentDetails);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePaymentDetails(PaymentDetails paymentDetails) {
        LambdaQueryWrapper<PaymentDetails> wrapper = new LambdaQueryWrapper<>();
        // TODO 设置删除条件
        this.remove(wrapper);
    }

    @Override
    public PaymentDetails selectDetailsByPaymentCode(String paymentCode) {
        return paymentDetailsMapper.selectDetailsByPaymentCode(paymentCode);
    }

    @Override
    public void addPaymentDetails(PaymentDetails paymentDetails) {

        paymentDetails.setId(snowflake.nextId());
        paymentDetailsMapper.addPaymentDetails(paymentDetails);
    }

    @Override
    public void deletePaymentDetailsByIds(String ids) {
        paymentDetailsMapper.deletePaymentDetailsByIds(ids);

    }

    @Override
    public List<PaymentDetails> selectPaymentExport(PaymentDetails paymentDetails) {

        List<PaymentDetails> detailsList = paymentDetailsMapper.selectPaymentExport(paymentDetails);
        detailsList.stream().forEach(d -> {
            setPaymentTypeValue(d);
        });
        return detailsList;
    }


    /**
     * 支付状态
     *
     * @param d
     */
    private void setPaymentTypeValue(PaymentDetails d) {
        if (paymentTypeEnums.UNPAID.key.equals(d.getPayType())) {
            d.setPayTypeValue(paymentTypeEnums.UNPAID.value);
        }
        if (paymentTypeEnums.PAID.key.equals(d.getPayType())) {
            d.setPayTypeValue(paymentTypeEnums.PAID.value);
        }
        if (paymentTypeEnums.REFUND.key.equals(d.getPayType())) {
            d.setPayTypeValue(paymentTypeEnums.REFUND.value);
        }
        if (paymentTypeEnums.REFUNDED.key.equals(d.getPayType())) {
            d.setPayTypeValue(paymentTypeEnums.REFUNDED.value);
        }
    }
}
