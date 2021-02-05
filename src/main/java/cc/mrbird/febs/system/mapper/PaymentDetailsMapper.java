package cc.mrbird.febs.system.mapper;

import cc.mrbird.febs.system.entity.PaymentDetails;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Mapper
 *
 * @author Hejingbo
 * @date 2020-09-05 21:06:26
 */
public interface PaymentDetailsMapper extends BaseMapper<PaymentDetails> {
    PaymentDetails selectDetailsByPaymentCode(String paymentCode);

    Long countPaymentDetails(@Param("paymentDetails") PaymentDetails paymentDetails);

    <T> IPage<PaymentDetails> findPaymentDetailsPage(Page<T> page, @Param("paymentDetails") PaymentDetails paymentDetails);

    void addPaymentDetails(PaymentDetails paymentDetails);

    void deletePaymentDetailsByIds(String ids);

    List<PaymentDetails> getPaymentDetailsByCode(@Param("paymentCode") String paymentCode);

    List<PaymentDetails> selectPaymentExport(PaymentDetails paymentDetails);

    int updateCode(@Param("orderCode") String orderCode, @Param("newCode") String newCode);
}
