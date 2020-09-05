package cc.mrbird.febs.system.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface BaseDaoMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
