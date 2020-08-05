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
 * @date 2020-08-05 23:39:31
 */
@Data
@TableName("t_room_order")
public class RoomOrder {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 酒店id
     */
    @TableField("hotel_id")
    private Long hotelId;

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
     * 房间类型
     */
    @TableField("room_type")
    private String roomType;

    /**
     * 房间数量
     */
    @TableField("room_count")
    private Integer roomCount;

    /**
     * 剩余数量
     */
    @TableField("surplus_number")
    private Integer surplusNumber;

    /**
     * 单价
     */
    @TableField("univalence")
    private Double univalence;

    /**
     * 总价
     */
    @TableField("total_price")
    private Double totalPrice;

    /**
     * 入住人
     */
    @TableField("occupants")
    private String occupants;

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
     * 联系方式
     */
    @TableField("phone")
    private String phone;

    /**
     * 备注信息
     */
    @TableField("remarks")
    private String remarks;

}
