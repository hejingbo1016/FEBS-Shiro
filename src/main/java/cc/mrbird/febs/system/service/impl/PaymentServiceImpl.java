package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.dto.ResponseDTO;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.BusinessRuntimeException;
import cc.mrbird.febs.common.utils.DateUtils;
import cc.mrbird.febs.common.utils.DistributedRedisLock;
import cc.mrbird.febs.common.utils.Snowflake;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.constants.AdminConstants;
import cc.mrbird.febs.system.entity.MeetingHotel;
import cc.mrbird.febs.system.entity.OrderPay;
import cc.mrbird.febs.system.entity.Payment;
import cc.mrbird.febs.system.entity.PaymentDetails;
import cc.mrbird.febs.system.mapper.MeetingHotelMapper;
import cc.mrbird.febs.system.mapper.PaymentDetailsMapper;
import cc.mrbird.febs.system.mapper.PaymentMapper;
import cc.mrbird.febs.system.service.IPaymentService;
import cc.mrbird.febs.wechat.utils.WeiChatRequestUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service实现
 *
 * @author Hejingbo
 * @date 2020-09-05 21:06:30
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements IPaymentService {
    private static Snowflake snowflake = Snowflake.getInstanceSnowflake();

    private final PaymentMapper paymentMapper;
    private final PaymentDetailsMapper detailsMapper;

    private final WeiChatRequestUtils weiChatRequestUtils;

    private final MeetingHotelMapper hotelMapper;

    @Autowired
    private static DistributedRedisLock distributedRedisLock;


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


        if (!StringUtils.isEmpty(paymentIds)) {
            String[] split = paymentIds.split(",");
            paymentMapper.deletePayments(split);
        } else {
            throw new BusinessRuntimeException("所传id为空，检查是否传值有误");
        }
    }

    @Override
    public Payment selectByPaymentId(Long id) {
        return paymentMapper.selectByPaymentId(id);
    }

    @Override
    public List<Payment> getPaymentListByUserId(Long userId) {
        return paymentMapper.getPaymentListByUserId(userId);
    }

    @Override
    public List<PaymentDetails> getPaymentDetailsByCode(String paymentCode) {
        return detailsMapper.getPaymentDetailsByCode(paymentCode);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO placOrders(OrderPay orderPay) {
        Double totalFee = new Double(0);
        List<PaymentDetails> paymentDetails = orderPay.getPaymentDetails();
        List<PaymentDetails> addPaymentDetails = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        List<String> dates = new ArrayList<>();
        Boolean flag = false;
        sb.append("以下房型库存不足:");
        if (!Objects.isNull(paymentDetails) && paymentDetails.size() > 0) {
            //生成订单主表
            PaymentDetails details = paymentDetails.get(0);
            Long paymentCode = snowflake.nextId();
            Payment payment = new Payment();
            BeanUtils.copyProperties(details, payment);
            payment.setId(snowflake.nextId());
            payment.setPaymentCode(String.valueOf(paymentCode));
            payment.setUserId(orderPay.getUserId());
            payment.setDescription(orderPay.getDescription());
            payment.setDataStatus(AdminConstants.DATA_N_DELETED);
            payment.setSex(orderPay.getSex());
            totalFee = details.getPaymentAmount();
            int count = paymentMapper.insertPayment(payment);
            //算出区间内所有日期
            String endTime = DateUtils.getCalculateDay(details.getEndTime(), -1);
            String startTime = DateUtils.getStringDates(details.getStartTime());
            try {
                dates = DateUtils.findDates(startTime, endTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (count > 0) {
                //生成订单明细表
                for (PaymentDetails p : paymentDetails) {
                    //通过会议id、酒店id和费用项id 查费用项库存
                    List<MeetingHotel> roomList = hotelMapper.selectFeeLists(p.getMeetingId(), p.getHotelId(), p.getFeeId());
                    //分组
                    Map<Long, List<MeetingHotel>> listMap = roomList.stream().collect(Collectors.groupingBy(MeetingHotel::getFeeId));
                    Integer surplusNumber = listMap.get(p.getFeeId()).stream().min(Comparator.comparing(MeetingHotel::getSurplusNumber)).get().getSurplusNumber();
                    if (surplusNumber > p.getNumber()) {
                        for (String s : dates) {
                            //库存充足
                            p.setId(snowflake.nextId());
                            p.setPaymentCode(paymentCode);
                            p.setDateTime(s);
                            addPaymentDetails.add(p);
                        }
                    } else {
                        flag = true;
                        sb.append(p.getFeeName()).append(";");
                    }
                }
                if (flag) {
                    return ResponseDTO.failture(sb.toString());
                }
                for (PaymentDetails addPaymentDetail : addPaymentDetails) {
                    //插入订单详情
                    detailsMapper.addPaymentDetails(addPaymentDetail);
                    //更新库存(减库存)
                    hotelMapper.reduceInvetory(addPaymentDetail);
                }
            }
            return onlinePay("下单", String.valueOf(paymentCode), totalFee, orderPay.getOpenid());
        }
        return ResponseDTO.failture();
    }

    @Override
    public String weiChatPayNotify(String resXml) {
        log.info("微信支付通知。。。。。。。。。。。。");
        log.info(resXml);

        String xmlBack = "<xml>" + "<return_code><![CDATA[%s]]></return_code>" + "<return_msg><![CDATA[%s]]></return_msg>" + "</xml> ";
        Map<String, String> notifyMap = null;
        try {
            notifyMap = WXPayUtil.xmlToMap(resXml);         // 转换成map
            if (weiChatRequestUtils.isPayResultNotifySignatureValid(notifyMap)) {
                String out_trade_no = notifyMap.get("out_trade_no");//订单号

                if (out_trade_no == null) {
                    log.info("微信支付回调失败订单号: {}", notifyMap);
                    return String.format(xmlBack, "FAIL", "订单号为空");
                }
                // 业务
                if (!"SUCCESS".equals(notifyMap.get("result_code"))) {
                    return String.format(xmlBack, "FAIL", "");
                }
                UpdateWrapper<Payment> wrapper = new UpdateWrapper<>();
                String stringDate = DateUtils.getStringDate();
                wrapper.eq("payment_Code", out_trade_no)
                        .set("pay_type", 2)
                        .set("payment_time", stringDate);
                paymentMapper.update(null, wrapper);
                return String.format(xmlBack, "SUCCESS", "OK");
            } else {
                log.error("微信支付回调通知签名错误");
                return String.format(xmlBack, "FAIL", "签名错误");
            }
        } catch (Exception e) {
            log.error("微信支付回调通知失败", e);
            return String.format(xmlBack, "FAIL", "失败");
        }
    }

    @Override
    public void paymentAudit(Payment payment) {

        this.paymentMapper.paymentAudit(payment);
    }

    /**
     * 调用微信支付统一下单接口
     *
     * @param orderName
     * @param orderNo
     * @param totalFee
     * @param openid
     * @return
     * @throws Exception
     */
    private ResponseDTO onlinePay(String orderName, String orderNo, Double totalFee, String openid) {
        try {
            log.info("openid: " + openid);
            log.info("开始进行调用统一下单接口");
            Map<String, String> map = weiChatRequestUtils.wxPay(orderName, orderNo, totalFee, openid);
            log.info("调用统一下单接口返回参数" + map);
            if (!"SUCCESS".equals(map.get("return_code"))) {
                return ResponseDTO.failture(map.get("return_msg"));
            }
            if (!"SUCCESS".equals(map.get("result_code"))) {
                return ResponseDTO.failture(map.get("err_code_des"));
            }
            return ResponseDTO.success("订单创建成功", weiChatRequestUtils.sign(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseDTO.failture();
    }

}
