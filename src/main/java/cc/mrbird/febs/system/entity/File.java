package cc.mrbird.febs.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

/**
 * Entity
 *
 * @author Hejingbo
 * @date 2020-08-05 23:40:32
 */
@Data
@TableName("t_file")
public class File {

    /**
     * 主键id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 外键id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("foreign_id")
    private Long foreignId;

    /**
     * 附件url
     */
    @TableField("url")
    private String url;

    /**
     * 附件后缀
     */
    @TableField("file_extension")
    private String fileExtension;

    /**
     * 附件名称
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 附件大小
     */
    @TableField("file_size")
    private Long fileSize;

    /**
     * 缩略图url
     */
    @TableField("thumbnail_url")
    private String thumbnailUrl;

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
     * md5
     */
    @TableField("md5Val")
    private String md5Val;

    /**
     * 拍摄时间
     */
    @TableField("attachTime")
    private String attachTime;

    /**
     * 文件名不带后缀
     */
    @TableField("noSuffixFileName")
    private String noSuffixFileName;


}
