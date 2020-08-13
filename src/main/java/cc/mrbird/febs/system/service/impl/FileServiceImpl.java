package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.CommonConstants;
import cc.mrbird.febs.common.utils.ConfigUtil;
import cc.mrbird.febs.system.entity.File;
import cc.mrbird.febs.system.mapper.FileMapper;
import cc.mrbird.febs.system.service.IFileService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import lombok.RequiredArgsConstructor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
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

    private final String ROOT_DIR = ConfigUtil.getProperty("attachment.upload.root");

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
        wrapper.eq(File::getForeignId, file.getForeignId());
	    this.remove(wrapper);
	}

    @Override
    public List<File> upload(List<MultipartFile> file, String dir, Long fId) {
        List<File> fileList = CollUtil.newArrayList();

        for (MultipartFile multipartFile : file) {
            fileList.add(this.upload(multipartFile, dir, fId));
        }

        return fileList;
    }

	@Override
    @Transactional(rollbackFor = Exception.class)
	public File upload(MultipartFile file, String dir, Long fId) {
        String pathSeparator = CommonConstants.PATH_SEPARATOR;
        String fullFilePath = null;
        try (InputStream stream = file.getInputStream()) {
            String nowDate = DateUtil.format(new Date(), "yyyy-MM-dd");
            String fileName = file.getOriginalFilename();
            String fileSuffix = fileName.substring(fileName.indexOf('.') + 1);
            String newFileName = System.currentTimeMillis() + "." + fileSuffix;

            // 上传目录是否存在
            String dirPathStr = StrUtil.join(pathSeparator, dir, nowDate);
            java.io.File dirPathFire = new java.io.File(ROOT_DIR + pathSeparator + dirPathStr);
            if (!dirPathFire.exists()) dirPathFire.mkdirs();

            String filePath = StrUtil.join(pathSeparator, dirPathStr, newFileName);
            fullFilePath = StrUtil.join(pathSeparator, ROOT_DIR, filePath);

            file.transferTo(new java.io.File(fullFilePath));

            File file1 = new File();
            file1.setForeignId(fId);
            file1.setFileExtension(fileSuffix);
            file1.setFileName(fileName.substring(0, fileName.indexOf('.')));
            file1.setFileSize(file.getSize());
            file1.setUrl(filePath);

            fileMapper.insert(file1);
            return file1;
        } catch (IOException e) {
            log.error("读取流失败！", e);

            // 出现异常删除文件
            if (StrUtil.isNotBlank(fullFilePath)) {
                new java.io.File(fullFilePath).deleteOnExit();
            }

        }

        return null;
    }
}
