package cc.mrbird.febs.system.entity;

import lombok.Data;

import java.util.List;

@Data
public class OrderPay {

    private List<PaymentDetails> paymentDetails;

    private String openid;

    private Double paymentAmount;

    private Long userId;

    private String sex;

    private String description;
}
