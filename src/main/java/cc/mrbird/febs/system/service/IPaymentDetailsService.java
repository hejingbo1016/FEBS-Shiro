package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.entity.PaymentDetails;

import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  Service接口
 *
 * @author Hejingbo
 * @date 2020-09-05 21:06:26
 */
public interface IPaymentDetailsService extends IService<PaymentDetails> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param paymentDetails paymentDetails
     * @return IPage<PaymentDetails>
     */
    IPage<PaymentDetails> findPaymentDetailss(QueryRequest request, PaymentDetails paymentDetails);

    /**
     * 查询（所有）
     *
     * @param paymentDetails paymentDetails
     * @return List<PaymentDetails>
     */
    List<PaymentDetails> findPaymentDetailss(PaymentDetails paymentDetails);

    /**
     * 新增
     *
     * @param paymentDetails paymentDetails
     */
    void createPaymentDetails(PaymentDetails paymentDetails);

    /**
     * 修改
     *
     * @param paymentDetails paymentDetails
     */
    void updatePaymentDetails(PaymentDetails paymentDetails);

    /**
     * 删除
     *
     * @param paymentDetails paymentDetails
     */
    void deletePaymentDetails(PaymentDetails paymentDetails);
}
