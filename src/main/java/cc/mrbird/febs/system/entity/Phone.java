package cc.mrbird.febs.system.entity;

import java.io.Serializable;
import java.util.Date;

import cc.mrbird.febs.common.converter.TimeConverter;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * Entity
 *
 * @author Hejingbo
 * @date 2020-08-04 22:59:38
 */
@Data
@TableName("t_phone")
@Excel("手机信息表")
public class Phone implements Serializable {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 手机名称
     */
    @TableField("phone_name")
    @ExcelField(value = "手机名称")
    private String phoneName;

    /**
     * 手机型号
     */
    @TableField("phone_type")
    @ExcelField(value = "手机型号")
    private String phoneType;

    /**
     * 手机配置
     */
    @TableField("phone_configuration")
    @ExcelField(value = "手机配置")
    private String phoneConfiguration;

    /**
     * 手机颜色
     */
    @TableField("phone_colour")
    @ExcelField(value = "手机颜色")
    private String phoneColour;

    /**
     * 零售价
     */
    @TableField("retail_price")
    @ExcelField(value = "零售价")
    private Double retailPrice;

    /**
     * 批发价
     */
    @TableField("agency_price")
    @ExcelField(value = "批发价")
    private Double agencyPrice;

    /**
     * 手机数量
     */
    @TableField("phone_count")
    @ExcelField(value = "手机数量")
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
    @ExcelField(value = "修改时间", writeConverter = TimeConverter.class)
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
