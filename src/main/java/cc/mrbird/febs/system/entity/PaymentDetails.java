package cc.mrbird.febs.system.entity;

import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 *  Entity
 *
 * @author Hejingbo
 * @date 2020-09-05 21:06:26
 */
@Data
@TableName("t_payment_details")
public class PaymentDetails {

    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    @TableField("payment_Code")
    private Long paymentCode;

    /**
     * 
     */
    @TableField("fee_name")
    private String feeName;

    /**
     * 
     */
    @TableField("number")
    private Integer number;

    /**
     * 
     */
    @TableField("interval_price")
    private Double intervalPrice;

    /**
     * 入住日期
     */
    @TableField("start_time")
    private Date startTime;

    /**
     * 截止日期
     */
    @TableField("end_time")
    private Date endTime;

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
