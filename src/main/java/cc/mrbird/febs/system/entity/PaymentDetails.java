package cc.mrbird.febs.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.util.Date;

/**
 * Entity
 *
 * @author Hejingbo
 * @date 2020-09-05 21:06:26
 */
@Data
@TableName("t_payment_details")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentDetails {

    /**
     * 主键
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单编号
     */
    @ExcelField(value = "订单编号")
    @TableField("payment_Code")
    private Long paymentCode;


    @ExcelField(value = "姓名")
    private String occupants;

    /**
     * 联系方式
     */
    @ExcelField(value = "电话")
    private String contactPhone;


    /**
     * 费用名称
     */
    @ExcelField(value = "费用名称")
    @TableField("fee_name")
    private String feeName;

    /**
     * 入住日期
     */
    @ExcelField(value = "入住时间")
    @TableField("start_time")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    /**
     * 截止日期
     */
    @ExcelField(value = "离店时间")
    @TableField("end_time")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;


    /**
     * 单价
     */
    @ExcelField(value = "单价")
    @TableField("interval_price")
    private Double intervalPrice;

    /**
     * 数量
     */
    @ExcelField(value = "数量")
    @TableField("number")
    private Integer number;


    /**
     * 小计
     */
    @ExcelField(value = "小计")
    @TableField("subtotal")
    private Double subtotal;


    /**
     * 支付状态（1未支付,2已支付,3申请退款,4已退款）
     */
    @ExcelField(value = "状态")
    private String payTypeValue;


    /**
     * 备注
     */
    @ExcelField(value = "备注")
    private String description;

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
     * 费用类型（1房费，2其他费用）
     */
    @TableField("fee_type")
    private Integer feeType;

    /**
     * 酒店id
     */
    @TableField("hotel_id")
    private Long hotelId;

    /**
     * 酒店名称
     */
    @TableField(exist = false)
    private String hotelName;

    /**
     * 支付金额
     */
    @TableField(exist = false)
    private Double paymentAmount;


    /**
     * 会议id
     */
    @TableField("meeting_id")
    private Long meetingId;

    /**
     * 支付状态（1未支付,2已支付,3申请退款,4已退款）
     */
    @TableField(exist = false)
    private Integer payType;

}
