package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.entity.Dict;

import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  Service接口
 *
 * @author Hejingbo
 * @date 2020-08-05 23:40:51
 */
public interface IDictService extends IService<Dict> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param dict dict
     * @return IPage<Dict>
     */
    IPage<Dict> findDicts(QueryRequest request, Dict dict);

    /**
     * 查询（所有）
     *
     * @param dict dict
     * @return List<Dict>
     */
    List<Dict> findDicts(Dict dict);

    /**
     * 新增
     *
     * @param dict dict
     */
    void createDict(Dict dict);

    /**
     * 修改
     *
     * @param dict dict
     */
    void updateDict(Dict dict);

    /**
     * 删除
     *
     * @param dict dict
     */
    void deleteDict(Dict dict);
}
