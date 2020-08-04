package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.Phone;
import cc.mrbird.febs.system.entity.Role;
import cc.mrbird.febs.system.entity.UserRole;
import cc.mrbird.febs.system.mapper.PhoneMapper;
import cc.mrbird.febs.system.service.IPhoneService;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import lombok.RequiredArgsConstructor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Service实现
 *
 * @author Hejingbo
 * @date 2020-08-04 22:24:45
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class PhoneServiceImpl extends ServiceImpl<PhoneMapper, Phone> implements IPhoneService {

    private final PhoneMapper phoneMapper;

    @Override
    public IPage<Phone> findPhones(QueryRequest request, Phone phone) {
        QueryWrapper<Phone> queryWrapper = new QueryWrapper<>();
        // TODO 设置查询条件
        Page<Phone> page = new Page<>(request.getPageNum(), request.getPageSize());
        page.setSearchCount(false);
        page.setTotal(baseMapper.countPhone(phone));
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Phone> findPhones(Phone phone) {
        QueryWrapper<Phone> queryWrapper = new QueryWrapper<>();
        // TODO 设置查询条件
        if (StringUtils.isNotBlank(phone.getPhoneName())) {
            queryWrapper.lambda().like(Phone::getPhoneName, phone.getPhoneName());
        }
        if (StringUtils.isNotBlank(phone.getPhoneType())) {
            queryWrapper.lambda().like(Phone::getPhoneType, phone.getPhoneType());
        }
        if (StringUtils.isNotBlank(phone.getPhoneColour())) {
            queryWrapper.lambda().like(Phone::getPhoneType, phone.getPhoneColour());
        }
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPhone(Phone phone) {
        this.save(phone);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePhone(Phone phone) {
        this.saveOrUpdate(phone);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePhone(Phone phone) {
        LambdaQueryWrapper<Phone> wrapper = new LambdaQueryWrapper<>();
        // TODO 设置删除条件
        this.remove(wrapper);
    }

    @Override
    public void deletePhones(String phoneIds) {
        List<String> list = Arrays.asList(phoneIds.split(StringPool.COMMA));
        this.baseMapper.delete(new QueryWrapper<Phone>().lambda().in(Phone::getId, list));
    }
}
