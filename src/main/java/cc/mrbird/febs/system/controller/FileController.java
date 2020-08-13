package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.system.entity.File;
import cc.mrbird.febs.system.service.IFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 *  Controller
 *
 * @author Hejingbo
 * @date 2020-08-05 23:40:32
 */
@Slf4j
@Validated
@RestController("FileController")
@RequiredArgsConstructor
@RequestMapping("file")
public class FileController extends BaseController {

    private final IFileService fileService;

    /**
     * 单一文件上传
     * @param fId 关联表id
     * @param dir 上传的目录
     * @param file
     * @return
     */
    @PostMapping("upload/{dir}/{fId}")
    public FebsResponse upload(@PathVariable Long fId, @PathVariable String dir,
                           @RequestParam MultipartFile file) {

        FebsResponse failResponse = new FebsResponse().fail();
        if (file.getSize() == 0) {
            return failResponse.message("文件为空！");
        }

        File upload = fileService.upload(file, dir, fId);
        if (upload == null) {
            return failResponse.message("文件上传失败！");
        }

        return new FebsResponse().success().data(upload);
    }
}
