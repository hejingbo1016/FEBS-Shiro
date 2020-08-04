package cc.mrbird.febs.system.mapper;

import cc.mrbird.febs.system.entity.Phone;
import cc.mrbird.febs.system.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 *  Mapper
 *
 * @author Hejingbo
 * @date 2020-08-04 22:24:45
 */
public interface PhoneMapper extends BaseMapper<Phone> {

    Long countPhone(@Param("phone") Phone phone);

}
