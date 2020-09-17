package cc.mrbird.febs.system.service;

import cc.mrbird.febs.system.entity.File;

import cc.mrbird.febs.common.entity.QueryRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 *  Service接口
 *
 * @author Hejingbo
 * @date 2020-08-05 23:40:32
 */
public interface IFileService extends IService<File> {
    /**
     * 查询（分页）
     *
     * @param request QueryRequest
     * @param file file
     * @return IPage<File>
     */
    IPage<File> findFiles(QueryRequest request, File file);

    /**
     * 查询（所有）
     *
     * @param file file
     * @return List<File>
     */
    List<File> findFiles(File file);

    /**
     * 新增
     *
     * @param file file
     */
    void createFile(File file);

    /**
     * 修改
     *
     * @param file file
     */
    void updateFile(File file);

    /**
     * 删除
     *
     * @param file file
     */
    void deleteFile(File file);

    List<File> upload(List<MultipartFile> file, String dir, Long fId);

    File upload(MultipartFile file, String dir, Long fId);

    List<File> uploadFile(MultipartFile[] file,Long fId);

    void deleteFiles(String ids);
}
