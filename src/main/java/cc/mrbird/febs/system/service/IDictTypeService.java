package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.entity.DictType;

import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  Service接口
 *
 * @author Hejingbo
 * @date 2020-08-05 23:40:43
 */
public interface IDictTypeService extends IService<DictType> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param dictType dictType
     * @return IPage<DictType>
     */
    IPage<DictType> findDictTypes(QueryRequest request, DictType dictType);

    /**
     * 查询（所有）
     *
     * @param dictType dictType
     * @return List<DictType>
     */
    List<DictType> findDictTypes(DictType dictType);

    /**
     * 新增
     *
     * @param dictType dictType
     */
    void createDictType(DictType dictType);

    /**
     * 修改
     *
     * @param dictType dictType
     */
    void updateDictType(DictType dictType);

    /**
     * 删除
     *
     * @param dictType dictType
     */
    void deleteDictType(DictType dictType);
}
