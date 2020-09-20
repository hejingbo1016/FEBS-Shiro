package cc.mrbird.febs.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
public class OrderInvoice {

    /**
     * 主键id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 订单编号
     */
    @TableField("payment_Code")
    private String paymentCode;

    /**
     * 微信用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 发票抬头
     */
    @TableField("invoice_title")
    private String invoiceTitle;

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
     * 联系方式
     */
    @TableField("phone")
    private Integer phone;

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
