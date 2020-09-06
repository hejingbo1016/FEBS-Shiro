package cc.mrbird.febs.system.mapper;

import cc.mrbird.febs.system.entity.Payment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 *  Mapper
 *
 * @author Hejingbo
 * @date 2020-09-05 21:06:30
 */
public interface PaymentMapper extends BaseMapper<Payment> {

    Long countPayment(@Param("payment") Payment payment);

    <T> IPage<Payment> findPaymentPage(Page<T> page, @Param("payment") Payment payment);

    int deletePayments(@Param("ids") String paymentIds);

    void insertPayment(Payment payment);

    Payment selectByPaymentId(Long id);
}
