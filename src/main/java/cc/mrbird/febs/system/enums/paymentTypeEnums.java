package cc.mrbird.febs.system.enums;


public enum paymentTypeEnums {

    UNPAID(1, "未支付"),
    PAID(2, "已支付"),
    REFUND(3, "申请退款"),
    REFUNDED(4, "已退款");


    public Integer key;
    public String value;

    paymentTypeEnums(Integer key, String value) {
        this.key = key;
        this.value = value;
    }
}
