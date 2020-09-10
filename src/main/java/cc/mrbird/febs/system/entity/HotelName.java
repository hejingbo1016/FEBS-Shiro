package cc.mrbird.febs.system.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

@Data
public class HotelName implements Serializable {

    private static final long serialVersionUID = 1551439676541651054L;


    private String hotelName;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long hotelId;

}
