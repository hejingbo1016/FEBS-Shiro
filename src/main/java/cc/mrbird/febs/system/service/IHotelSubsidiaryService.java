package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.entity.HotelSubsidiary;

import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  Service接口
 *
 * @author Hejingbo
 * @date 2020-08-05 23:36:25
 */
public interface IHotelSubsidiaryService extends IService<HotelSubsidiary> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param hotelSubsidiary hotelSubsidiary
     * @return IPage<HotelSubsidiary>
     */
    IPage<HotelSubsidiary> findHotelSubsidiarys(QueryRequest request, HotelSubsidiary hotelSubsidiary);

    /**
     * 查询（所有）
     *
     * @param hotelSubsidiary hotelSubsidiary
     * @return List<HotelSubsidiary>
     */
    List<HotelSubsidiary> findHotelSubsidiarys(HotelSubsidiary hotelSubsidiary);

    /**
     * 新增
     *
     * @param hotelSubsidiary hotelSubsidiary
     */
    void createHotelSubsidiary(HotelSubsidiary hotelSubsidiary);

    /**
     * 修改
     *
     * @param hotelSubsidiary hotelSubsidiary
     */
    void updateHotelSubsidiary(HotelSubsidiary hotelSubsidiary);

    /**
     * 删除
     *
     * @param hotelSubsidiary hotelSubsidiary
     */
    void deleteHotelSubsidiary(HotelSubsidiary hotelSubsidiary);
}
