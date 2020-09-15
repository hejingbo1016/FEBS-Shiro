package cc.mrbird.febs.system.service.impl;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.BusinessRuntimeException;
import cc.mrbird.febs.common.service.ImgHelperService;
import cc.mrbird.febs.common.utils.*;
import cc.mrbird.febs.system.entity.File;
import cc.mrbird.febs.system.mapper.FileMapper;
import cc.mrbird.febs.system.service.IFileService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Service实现
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
    @Autowired
    private FilePathConfig filePathConfig;
    @Value("${minio.imageUrl}")
    private String imgUrl = ""; //读取图片保存路径
    @Value("${minio.imageShowUrl}")
    private String imageShowUrl = ""; //读取图片保存路径
    private static Snowflake snowflake = Snowflake.getInstanceSnowflake();

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
        queryWrapper.eq(File::getForeignId, file.getForeignId());
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

        if (!StringUtils.isEmpty(file) && file.size() > 0) {
            for (MultipartFile multipartFile : file) {
                fileList.add(this.upload(multipartFile, dir, fId));
            }
        } else {
            return null;
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
            if (!StringUtils.isEmpty(fileName)) {
                file1.setFileName(fileName.substring(0, fileName.indexOf('.')));
            }
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

    /**
     * 批量上传
     *
     * @param file
     * @param fId
     * @return
     */
    @Override
    public List<File> uploadFile(MultipartFile[] file, Long fId) {
        //用于接收上传到服务器的照片，后续用于批量传入到附件表中
        List<File> fileList = new ArrayList<>();
        if (!StringUtils.isEmpty(fId)) {
            if (!StringUtils.isEmpty(file)) {
                //上传通用方法
                setFileList(file, fId, fileList);
                if (!Objects.isNull(fileList) && fileList.size() > 0) {
                    //因生成的mapper方法是只能适应自增id模式，所以不支持该批量插入操作
                    fileList.stream().forEach(f -> {
                        fileMapper.inserts(f);
                    });
                }
            } else {
                throw new BusinessRuntimeException("上传的图片为空,请检查后上传");
            }
        } else {
            throw new BusinessRuntimeException("上传图片异常,生成的关联主键不能为空");
        }
        return fileList;
    }

    private void setFileList(MultipartFile[] multipartFile, Long fId, List<File> fileList) {
        for (int i = 0; i < multipartFile.length; i++) {
            File file = new File();
            //图片名称
            String originalFilename = multipartFile[i].getOriginalFilename();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            //获取文件后缀名
            String suffixName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            //获取文件名，不带后缀
            String fileName = originalFilename.substring(0, originalFilename.lastIndexOf("."));
            //获取文件的哈希值
            String md5 = MultipartFileUtil.md5HashCodes(multipartFile[i]);
            //判断是否是图片
            if (MultipartFileUtil.isPhotoType(suffixName, originalFilename)) {
                ImgHelperService imgHelperService = new ImgHelperService();
                String uuid = UUID.randomUUID().toString();//文件唯一标识uuid
                //判断图片是否相同
                try {
                    //获取静态资源的路径
                    String staticFile = filePathConfig.getBasePath();
                    String filePath = imgUrl + sdf.format(new Date()) + "/" + uuid + "/" + originalFilename;
                    //缩略图
                    String thumbnail = imgUrl + sdf.format(new Date()) + "/" + uuid + "/" + "1_" + originalFilename;
                    //完整图片路径
                    String imgUrls = staticFile + filePath;
                    //缩略图完整路径
                    String thumbnailFath = staticFile + thumbnail;
                    //生成图片和缩略图
                    Map<String, Map> integerMap = imgHelperService.setUpThumbnailPhoto(imgUrls, thumbnailFath, originalFilename, multipartFile[i], imgHelperService);
                    file.setId(snowflake.nextId());
                    file.setFileName(originalFilename);
                    file.setFileExtension(suffixName);
                    file.setNoSuffixFileName(fileName);
                    file.setUrl(filePath.replace(imgUrl, ""));
                    file.setThumbnailUrl(thumbnail.replace(imgUrl, ""));
                    file.setForeignId(fId);
                    file.setCreateTime(new Date());
                    file.setAttachTime(String.valueOf(integerMap.get("createTime").get("imageCreateTime")));
                    file.setFileSize(multipartFile[i].getSize());
                    file.setMd5Val(md5);
                    fileList.add(file);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new BusinessRuntimeException("上传图片异常");
                }
            }
        }
    }
}
