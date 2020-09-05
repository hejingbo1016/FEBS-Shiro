package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.PaymentDetails;
import cc.mrbird.febs.system.mapper.PaymentDetailsMapper;
import cc.mrbird.febs.system.service.IPaymentDetailsService;
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
 * @date 2020-09-05 21:06:26
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class PaymentDetailsServiceImpl extends ServiceImpl<PaymentDetailsMapper, PaymentDetails> implements IPaymentDetailsService {

    private final PaymentDetailsMapper paymentDetailsMapper;

    @Override
    public IPage<PaymentDetails> findPaymentDetailss(QueryRequest request, PaymentDetails paymentDetails) {
        LambdaQueryWrapper<PaymentDetails> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<PaymentDetails> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
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
}
