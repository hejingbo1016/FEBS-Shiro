package cc.mrbird.febs.system.mapper;

import cc.mrbird.febs.system.entity.OrderInvoice;
import cc.mrbird.febs.system.entity.PaymentDetails;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 *  Mapper
 *
 * @author Hejingbo
 * @date 2020-09-20 11:13:52
 */
public interface OrderInvoiceMapper extends BaseMapper<OrderInvoice> {

    List<OrderInvoice> orderInvoiceExport(PaymentDetails paymentDetails);
}
