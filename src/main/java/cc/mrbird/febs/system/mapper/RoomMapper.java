package cc.mrbird.febs.system.mapper;

import cc.mrbird.febs.system.entity.Room;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 *  Mapper
 *
 * @author Hejingbo
 * @date 2020-08-05 23:39:18
 */
public interface RoomMapper extends BaseMapper<Room> {

    Long countRoom(@Param("room")Room room, @Param("hotelId") String hotelId);
}
