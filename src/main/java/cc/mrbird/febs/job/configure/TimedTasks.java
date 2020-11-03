package cc.mrbird.febs.job.configure;

import cc.mrbird.febs.system.constants.AdminConstants;
import cc.mrbird.febs.system.entity.Payment;
import cc.mrbird.febs.system.entity.PaymentDetails;
import cc.mrbird.febs.system.mapper.MeetingHotelMapper;
import cc.mrbird.febs.system.mapper.PaymentDetailsMapper;
import cc.mrbird.febs.system.mapper.PaymentMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class TimedTasks {
    @Autowired
    private PaymentDetailsMapper detailsMapper;
    @Autowired
    private PaymentMapper paymentMapper;
    @Autowired
    private MeetingHotelMapper hotelMapper;


    /**
     * 每分钟进行更新库存
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void UpdateInventory() {

        //未支付状态、未入库原数据、数据创建时间大于等于5分钟的数据  
        List<Payment> payments = paymentMapper.slectPaymentByTask();
        List<PaymentDetails> paymentDetails = new ArrayList<>();
        //更新订单表把已经还原库存的数据 状态更改为已入库原数据状态
        List<String> updatePaymentList = new ArrayList<>();
        if (!Objects.isNull(payments) && payments.size() > 0) {
            //获取所有的明细
            payments.stream().forEach(p -> {
                List<PaymentDetails> paymentDetailsByCode = detailsMapper.getPaymentDetailsByCode(p.getPaymentCode());
                if (!Objects.isNull(paymentDetailsByCode) && paymentDetailsByCode.size() > 0) {
                    paymentDetails.addAll(paymentDetailsByCode);
                }
            });
        }
        //更新会议费用项表，把未支付订单的库存还原回去
        if (paymentDetails.size() > 0) {
            paymentDetails.stream().forEach(t -> {
                //更新库存(加库存)
                int count = hotelMapper.updateInvetory(t);
                if (count > 0) {
                    updatePaymentList.add(String.valueOf(t.getPaymentCode()));
                }
            });
        }
        if (updatePaymentList.size() > 0) {
            //更新订单表把已经还原库存的数据 状态更改为已入库原数据状态
            Payment payment = new Payment();
            payment.setDataStatus(AdminConstants.DATA_VALID);
            //BUG 该订单超过5分钟后才进行支付，会导致库存变多
            paymentMapper.update(payment, new LambdaQueryWrapper<Payment>().in(Payment::getPaymentCode, updatePaymentList));
        }
    }

}
