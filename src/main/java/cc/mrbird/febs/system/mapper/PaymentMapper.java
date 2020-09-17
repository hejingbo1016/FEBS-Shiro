package cc.mrbird.febs.system.mapper;

import cc.mrbird.febs.system.entity.Payment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Mapper
 *
 * @author Hejingbo
 * @date 2020-09-05 21:06:30
 */
public interface PaymentMapper extends BaseMapper<Payment> {

    Long countPayment(@Param("payment") Payment payment);

    <T> IPage<Payment> findPaymentPage(Page<T> page, @Param("payment") Payment payment);

    int deletePayments(@Param("ids") String paymentIds);

    int insertPayment(Payment payment);

    Payment selectByPaymentId(Long id);

    List<Payment> getPaymentListByUserId(@Param("userId") Long userId);


    /**
     * 更改订单支付状态
     *
     * @param paymentCode
     * @param payType
     * @return
     */
    int updatePayType(@Param("paymentCode") String paymentCode, @Param("payType") Integer payType);
}
