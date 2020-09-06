package cc.mrbird.febs.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.util.Date;

/**
 * Entity
 *
 * @author Hejingbo
 * @date 2020-09-06 15:13:52
 */
@Data
@TableName("t_meeting_hotel")
public class MeetingHotel {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会议id
     */
    @TableField("meeting_id")
    private Long meetingId;

    /**
     * 费用类型（1:房间，2其他费用）
     */
    @TableField("fee_type")
    private Integer feeType;

    /**
     * 费用名称
     */
    @TableField("fee_name")
    private String feeName;

    /**
     * 剩余数量
     */
    @TableField("surplus_number")
    private Integer surplusNumber;

    /**
     * 费用价格
     */
    @TableField("fee_price")
    private Double feePrice;

    /**
     * 可用数量
     */
    @TableField("available_number")
    private Integer availableNumber;

    /**
     * 酒店id
     */
    @TableField("hotel_id")
    private Long hotelId;


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
     * 入住日期
     */
    @TableField(exist = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    /**
     * 截止日期
     */
    @TableField(exist = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 日期
     */
    @TableField("date_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date dateTime;


    /**
     * 酒店名称
     */
    @ExcelField(value = "酒店名称")
    @TableField(exist = false)
    private String hotelName;

    /**
     * 会议名称
     */
    @ExcelField(value = "会议名称")
    @TableField(exist = false)
    private String meetingName;

}
