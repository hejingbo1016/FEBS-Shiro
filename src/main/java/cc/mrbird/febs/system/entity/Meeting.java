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
 * @date 2020-08-05 23:40:11
 */
@Data
@TableName("t_meeting")
public class Meeting {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会议名称
     */
    @TableField("meeting_name")
    private String meetingName;

    /**
     * 主办方
     */
    @TableField("sponsor")
    private String sponsor;

    /**
     * 承办方
     */
    @TableField("organizer")
    private String organizer;

    /**
     * 会议负责人
     */
    @TableField("meeting_principal")
    private String meetingPrincipal;


    /**
     * 会议地址
     */
    @TableField("meeting_address")
    private String meetingAddress;

    /**
     * 联系方式
     */
    @TableField("contact_phone")
    private String contactPhone;

    /**
     * 开始时间
     */
    @TableField("start_time")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private Date endTime;

    /**
     * 活动简介
     */
    @TableField("synopsis")
    private String synopsis;

    /**
     * 活动须知
     */
    @TableField("activity_notes")
    private String activityNotes;

    /**
     * 参会人员
     */
    @TableField("participants")
    private String participants;

    /**
     * 活动流程
     */
    @TableField("activity_flow")
    private String activityFlow;

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
