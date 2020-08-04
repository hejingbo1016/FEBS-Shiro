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
 * @date 2020-08-04 22:59:38
 */
@Data
@TableName("t_phone")
public class Phone {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 手机名称
     */
    @TableField("phone_name")
    private String phoneName;

    /**
     * 手机型号
     */
    @TableField("phone_type")
    private String phoneType;

    /**
     * 手机配置
     */
    @TableField("phone_configuration")
    private String phoneConfiguration;

    /**
     * 手机颜色
     */
    @TableField("phone_colour")
    private String phoneColour;

    /**
     * 零售价
     */
    @TableField("retail_price")
    private Double retailPrice;

    /**
     * 代理价
     */
    @TableField("agency_price")
    private Double agencyPrice;

    /**
     * 手机数量
     */
    @TableField("phone_count")
    private Integer phoneCount;

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
