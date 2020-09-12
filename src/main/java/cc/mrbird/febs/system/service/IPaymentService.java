package cc.mrbird.febs.system.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.Payment;
import cc.mrbird.febs.system.entity.PaymentDetails;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  Service接口
 *
 * @author Hejingbo
 * @date 2020-09-05 21:06:30
 */
public interface IPaymentService  extends IService<Payment> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param payment payment
     * @return IPage<Payment>
     */
    IPage<Payment> findPayments(QueryRequest request, Payment payment);

    /**
     * 查询（所有）
     *
     * @param payment payment
     * @return List<Payment>
     */
    List<Payment> findPayments(Payment payment);

    /**
     * 新增
     *
     * @param payment payment
     */
    void createPayment(Payment payment);

    /**
     * 修改
     *
     * @param payment payment
     */
    void updatePayment(Payment payment);


    void deletePayments(String paymentIds);

    Payment selectByPaymentId(Long id);

    List<Payment> getPaymentListByUserId(Long userId);

    List<PaymentDetails> getPaymentDetailsByCode(String paymentCode);

    void placOrders(List<PaymentDetails> paymentDetails);
}
