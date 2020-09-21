package cc.mrbird.febs.system.mapper;

import cc.mrbird.febs.system.entity.WechatUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 *  Mapper
 *
 * @author Hejingbo
 * @date 2020-08-05 23:39:42
 */
public interface WechatUserMapper extends BaseMapper<WechatUser> {

    Long countWeChatUsers(@Param("wechatUser") WechatUser wechatUser);

    int inserts(WechatUser wechatUser);

    void deleteWeChatUsersByIds(@Param("ids") String weChatUserIds);
}
