package cc.mrbird.febs.system.mapper;

import cc.mrbird.febs.system.entity.Hotel;
import cc.mrbird.febs.system.entity.HotelName;
import cc.mrbird.febs.system.entity.Room;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  Mapper
 *
 * @author Hejingbo
 * @date 2020-08-11 21:01:27
 */
public interface HotelMapper extends BaseMapper<Hotel> {

    Long countHotel(@Param("hotel") Hotel hotel);

    List<HotelName> getHotels();

    List<Room> getHotelRooms(@Param("hotelId") Long hotelId);
}
