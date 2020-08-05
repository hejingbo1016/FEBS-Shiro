package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.WechatUser;
import cc.mrbird.febs.system.mapper.WechatUserMapper;
import cc.mrbird.febs.system.service.IWechatUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import lombok.RequiredArgsConstructor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 *  Service实现
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
    public IPage<WechatUser> findWechatUsers(QueryRequest request, WechatUser wechatUser) {
        LambdaQueryWrapper<WechatUser> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<WechatUser> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<WechatUser> findWechatUsers(WechatUser wechatUser) {
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
}
