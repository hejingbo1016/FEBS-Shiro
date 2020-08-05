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
 * @date 2020-08-05 23:39:42
 */
@Data
@TableName("t_wechat_user")
public class WechatUser {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 授权id
     */
    @TableField("account_id")
    private String accountId;

    /**
     * 微信头像url
     */
    @TableField("avatar_url")
    private String avatarUrl;

    /**
     * 微信名称
     */
    @TableField("wechat_user_name")
    private String wechatUserName;

    /**
     * 用户名称
     */
    @TableField("user_name")
    private String userName;

    /**
     * 联系方式
     */
    @TableField("phone")
    private String phone;

    /**
     * 用户类型
     */
    @TableField("user_type")
    private String userType;

    /**
     * 性别 0男 1女 2保密
     */
    @TableField("sex")
    private String sex;

    /**
     * 是否开启tab，0关闭 1开启
     */
    @TableField("is_tab")
    private String isTab;

    /**
     * 备注
     */
    @TableField("description")
    private String description;

    /**
     * 客户类型
     */
    @TableField("customer_type")
    private String customerType;

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
