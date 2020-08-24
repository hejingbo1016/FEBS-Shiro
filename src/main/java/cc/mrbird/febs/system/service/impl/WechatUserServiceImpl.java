package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.system.entity.Meeting;
import cc.mrbird.febs.system.entity.WechatUser;
import cc.mrbird.febs.system.mapper.WechatUserMapper;
import cc.mrbird.febs.system.service.IWechatUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import lombok.RequiredArgsConstructor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Arrays;
import java.util.List;

/**
 * Service实现
 *
 * @author Hejingbo
 * @date 2020-08-05 23:39:42
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class WechatUserServiceImpl extends ServiceImpl<WechatUserMapper, WechatUser> implements IWechatUserService {

    private final WechatUserMapper wechatUserMapper;

    @Override
    public IPage<WechatUser> findWeChatUsers(QueryRequest request, WechatUser wechatUser) {
        LambdaQueryWrapper<WechatUser> queryWrapper = new LambdaQueryWrapper<>();
        //设置查询条件
        Page<WechatUser> page = new Page<>(request.getPageNum(), request.getPageSize());
        page.setSearchCount(false);
        page.setTotal(baseMapper.countWeChatUsers(wechatUser));
        selectWeChatUsers(queryWrapper, wechatUser);
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_ASC, true);
        return this.page(page, queryWrapper);
    }

    /**
     * 设置查询条件
     *
     * @param queryWrapper
     * @param wechatUser
     */
    private void selectWeChatUsers(LambdaQueryWrapper<WechatUser> queryWrapper, WechatUser wechatUser) {


        if (StringUtils.isNotBlank(wechatUser.getNickname())) {
            queryWrapper.like(WechatUser::getNickname, wechatUser.getNickname());

        }
        if (StringUtils.isNotBlank(wechatUser.getCity())) {
            queryWrapper.like(WechatUser::getCity, wechatUser.getCity());

        }
        if (StringUtils.isNotBlank(wechatUser.getProvince())) {
            queryWrapper.like(WechatUser::getProvince, wechatUser.getProvince());

        }

    }

    @Override
    public List<WechatUser> findWeChatUsers(WechatUser wechatUser) {
        LambdaQueryWrapper<WechatUser> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createWechatUser(WechatUser wechatUser) {
        this.save(wechatUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWechatUser(WechatUser wechatUser) {
        this.saveOrUpdate(wechatUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteWechatUser(WechatUser wechatUser) {
        LambdaQueryWrapper<WechatUser> wrapper = new LambdaQueryWrapper<>();
        // TODO 设置删除条件
        this.remove(wrapper);
    }

    @Override
    public void deleteWeChatUsers(String weChatUserIds) {
        List<String> list = Arrays.asList(weChatUserIds.split(StringPool.COMMA));
        this.baseMapper.delete(new QueryWrapper<WechatUser>().lambda().in(WechatUser::getId, list));

    }
}
