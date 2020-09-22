package cc.mrbird.febs.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.util.Date;

/**
 * Entity
 *
 * @author Hejingbo
 * @date 2020-09-05 21:39:06
 */
@Data
@TableName("t_payment")
public class Payment {

    /**
     * 主键
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单编号
     */
    @TableField("payment_Code")
    private String paymentCode;

    /**
     * 会议id
     */
    @TableField("meeting_id")
    private Long meetingId;

    /**
     * 酒店id
     */
    @TableField("hotel_id")
    private Long hotelId;

    /**
     * 支付金额
     */
    @TableField("payment_Amount")
    private Double paymentAmount;

    /**
     * 支付状态（1未支付,2已支付,3申请退款,4已退款）
     */
    @TableField("pay_type")
    private Integer payType;

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

    /**
     * 备注
     */
    @TableField("description")
    private String description;

    /**
     * 会议名称
     */
    @ExcelField(value = "会议名称")
    @TableField(exist = false)
    private String meetingName;

    /**
     * 酒店名称
     */
    @ExcelField(value = "酒店名称")
    @TableField(exist = false)
    private String hotelName;


    /**
     * 微信用户id
     */
    @TableField("user_id")
    private Long userId;


    /**
     * 联系方式
     */

    @TableField("contact_phone")
    private String contactPhone;


    /**
     * 入住人
     */
    @TableField("occupants")
    private String occupants;

    /**
     * 性别 0未知，1男 2女
     */
    @TableField("sex")
    private String sex;


}
