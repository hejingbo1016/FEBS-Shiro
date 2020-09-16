package cc.mrbird.febs.system.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class HotelName implements Serializable {

    private static final long serialVersionUID = 1551439676541651054L;


    /**
     * 酒店名称
     */
    private String hotelName;


    /**
     * 会议id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long meetingId;

    /**
     * 酒店id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long hotelId;


    /**
     * 酒店负责人
     */
    private String hotelPrincipal;

    /**
     * 联系方式
     */
    private String contactPhone;

    /**
     * 备注
     */
    private String receptionService;

    /**
     * 酒店地址
     */
    private String hotelAddress;

    /**
     * 路线指引
     */
    private String metroGuidelines;

    /**
     * 照片集
     */
    private List<File> fileList;

    /**
     * 房间费用项
     */
    private List<MeetingHotel> roomList;

    /**
     * 其他费用项
     */
    private List<MeetingHotel> otherList;


}
