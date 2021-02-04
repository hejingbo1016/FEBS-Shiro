package cc.mrbird.febs.system.entity;

import lombok.Data;

import java.util.List;

@Data
public class OrderPay2 {

    private String openid;

    private Double paymentAmount;

    private String orderCode;

}
