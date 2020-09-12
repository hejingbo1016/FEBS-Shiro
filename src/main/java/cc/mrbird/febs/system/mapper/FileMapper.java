package cc.mrbird.febs.system.mapper;

import cc.mrbird.febs.system.entity.File;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  Mapper
 *
 * @author Hejingbo
 * @date 2020-08-05 23:40:32
 */
public interface FileMapper extends BaseMapper<File> {

    List<File> selectFileByHotelId(@Param("foreignId") Long foreignId);
}
