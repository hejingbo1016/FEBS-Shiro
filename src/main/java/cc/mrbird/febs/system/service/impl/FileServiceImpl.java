package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.system.entity.File;
import cc.mrbird.febs.system.mapper.FileMapper;
import cc.mrbird.febs.system.service.IFileService;
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
 * @date 2020-08-05 23:40:32
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService {

    private final FileMapper fileMapper;

    @Override
    public IPage<File> findFiles(QueryRequest request, File file) {
        LambdaQueryWrapper<File> queryWrapper = new LambdaQueryWrapper<>();
        // TODO 设置查询条件
        Page<File> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<File> findFiles(File file) {
	    LambdaQueryWrapper<File> queryWrapper = new LambdaQueryWrapper<>();
		// TODO 设置查询条件
		return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createFile(File file) {
        this.save(file);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFile(File file) {
        this.saveOrUpdate(file);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFile(File file) {
        LambdaQueryWrapper<File> wrapper = new LambdaQueryWrapper<>();
	    // TODO 设置删除条件
	    this.remove(wrapper);
	}
}
