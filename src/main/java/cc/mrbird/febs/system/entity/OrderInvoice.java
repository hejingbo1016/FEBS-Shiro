package cc.mrbird.febs.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.util.Date;

/**
 * Entity
 *
 * @author Hejingbo
 * @date 2020-09-20 11:13:52
 */
@Data
@TableName("t_order_invoice")
@Excel("会议订单发票表")
public class OrderInvoice {

    /**
     * 主键id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id")
    private Long id;

    /**
     * 订单编号
     */
    @ExcelField(value = "订单编号")
    @TableField("payment_Code")
    private String paymentCode;

    /**
     * 微信用户id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("user_id")
    private Long userId;

    /**
     * 发票抬头
     */
    @ExcelField(value = "发票信息")
    @TableField("invoice_title")
    private String invoiceTitle;

    /**
     * 支付金额
     */
    @ExcelField(value = "金额")
    @TableField(exist = false)
    private Double paymentAmount;


    /**
     * 发票类型 ( 1专票，2普票)
     */
    @TableField("invoiceType")
    private Integer invoiceType;


    /**
     * 发票类型 ( 1专票，2普票)
     */
    @ExcelField(value = "发票类型")
    @TableField(exist = false)
    private String invoiceTypeValue;


    /**
     * 邮箱
     */
    @ExcelField(value = "接收邮箱")
    @TableField("email")
    private String email;


    /**
     * 联系方式
     */
    @ExcelField(value = "接收电话")
    @TableField("phone")
    private String phone;


    /**
     * 支付状态（1未支付,2已支付,3申请退款,4已退款）
     */
    @TableField(exist = false)
    private Integer payType;


    /**
     * 支付状态（1未支付,2已支付,3申请退款,4已退款）
     */
    @ExcelField(value = "状态")
    @TableField(exist = false)
    private String payTypeValue;


    /**
     * 纳税人识别号
     */
    @TableField("identification_code")
    private String identificationCode;

    /**
     * 地址
     */
    @TableField("address")
    private String address;


    /**
     * 开户行
     */
    @TableField("bank_of_deposit")
    private String bankOfDeposit;

    /**
     * 银行账号
     */
    @TableField("bank_account")
    private String bankAccount;


    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField("modify_time")
    private Date modifyTime;

    /**
     * 修改人员
     */
    @TableField("modifier")
    private String modifier;

    /**
     * 创建人员
     */
    @TableField("creater")
    private String creater;

    /**
     * 是否删除 ( 0：非删除;； 1： 删除 )
     */
    @TableField("deleted")
    private Integer deleted;

}
