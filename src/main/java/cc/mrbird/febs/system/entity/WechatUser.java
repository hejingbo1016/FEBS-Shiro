package cc.mrbird.febs.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Entity
 *
 * @author Hejingbo
 * @date 2020-08-24 16:51:24
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
     * 用户的标识，对当前公众号唯一
     */
    @TableField("openid")
    private String openid;

    /**
     * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
     */
    @TableField("headimgurl")
    private String headimgurl;

    /**
     * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
     */
    @TableField("unionid")
    private String unionid;

    /**
     * 返回用户关注的渠道来源，ADD_SCENE_SEARCH 公众号搜索，ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，ADD_SCENE_PROFILE_CARD 名片分享，ADD_SCENE_QR_CODE 扫描二维码，ADD_SCENE_PROFILE_LINK 图文页内名称点击，ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，ADD_SCENE_PAID 支付后关注，ADD_SCENE_WECHAT_ADVERTISEMENT 微信广告，ADD_SCENE_OTHERS 其他
     */
    @TableField("subscribe_scene")
    private String subscribeScene;

    /**
     * 微信名称/用户的昵称
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 用户名称
     */
    @TableField("user_name")
    private String userName;

    /**
     * 二维码扫码场景（开发者自定义）
     */
    @TableField("qr_scene")
    private Integer qrScene;

    /**
     * 二维码扫码场景描述（开发者自定义）
     */
    @TableField("qr_scene_str")
    private String qrSceneStr;

    /**
     * 用户被打上的标签ID列表
     */
    @TableField("tagid_list")
    private String tagidList;

    /**
     * 用户的语言，简体中文为zh_CN
     */
    @TableField("language")
    private String language;

    /**
     * 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
     */
    @TableField("subscribe")
    private Integer subscribe;

    /**
     * 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
     */
    @TableField("subscribe_time")
    private Date subscribeTime;

    /**
     * 用户所在省份
     */
    @TableField("province")
    private String province;

    /**
     * 用户所在国家
     */
    @TableField("country")
    private String country;

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
     * 用户所在城市
     */
    @TableField("city")
    private String city;

    /**
     * 性别 0未知，1男 2女
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
