package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.entity.Phone;

import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  Service接口
 *
 * @author Hejingbo
 * @date 2020-08-04 22:24:45
 */
public interface IPhoneService extends IService<Phone> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param phone phone
     * @return IPage<Phone>
     */
    IPage<Phone> findPhones(QueryRequest request, Phone phone);

    /**
     * 查询（所有）
     *
     * @param phone phone
     * @return List<Phone>
     */
    List<Phone> findPhones(Phone phone);

    /**
     * 新增
     *
     * @param phone phone
     */
    void createPhone(Phone phone);

    /**
     * 修改
     *
     * @param phone phone
     */
    void updatePhone(Phone phone);

    /**
     * 删除
     *
     * @param phone phone
     */
    void deletePhone(Phone phone);

    void deletePhones(String phoneIds);
}
