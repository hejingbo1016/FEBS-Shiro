package cc.mrbird.febs.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 *  Entity
 *
 * @author Hejingbo
 * @date 2020-08-11 21:01:27
 */
@Data
@TableName("t_hotel")
public class Hotel {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 酒店名称
     */
    @TableField("hotel_name")
    private String hotelName;

    /**
     * 酒店负责人
     */
    @TableField("hotel_principal")
    private String hotelPrincipal;

    /**
     * 联系方式
     */
    @TableField("contact_phone")
    private String contactPhone;

    /**
     * 接待服务
     */
    @TableField("reception_service")
    private String receptionService;

    /**
     * 酒店地址
     */
    @TableField("hotel_address")
    private String hotelAddress;

    /**
     * 地铁指引
     */
    @TableField("metro_guidelines")
    private String metroGuidelines;

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
