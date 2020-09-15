package cc.mrbird.febs.common.utils;

import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * MultipartFile转File的Util
 */
public class MultipartFileUtil {


    public static BufferedInputStream multipartFileToFile(MultipartFile file) throws Exception {

        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        }
        InputStream ins = null;
        ins = file.getInputStream();
        return new BufferedInputStream(ins);
    }

    //获取流文件
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除本地临时文件
     *
     * @param file
     */
    public static void delteTempFile(File file) {
        if (file != null) {
            File del = new File(file.toURI());
            del.delete();
        }
    }

    /**
     * 获取照片的md5
     *
     * @param fis
     * @return
     */
    public static String md5HashCode(BufferedInputStream fis) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = fis.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, length);
            }
            fis.close();
            //转换并返回包含16个元素字节数组,返回数值范围为-128到127
            byte[] md5Bytes = md.digest();
            BigInteger bigInt = new BigInteger(1, md5Bytes);//1代表绝对值
            return bigInt.toString(16);//转换为16进制
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }


    @SneakyThrows
    public static String md5HashCodes(MultipartFile file) {
        BufferedInputStream stream = null;
        try {
            stream = multipartFileToFile(file);
            return md5HashCode(stream);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("解析图片信息出异常");
        } finally {
            stream.close();
        }
    }


    /**
     * 判断图片格式
     *
     * @param suffixName
     * @param originalFilename
     * @return
     */
    public static Boolean isPhotoType(String suffixName, String originalFilename) {

        if (PhotoUtil.IMG_TYPE_DMG.equals(suffixName.toUpperCase()) ||
                PhotoUtil.IMG_TYPE_GIF.equals(suffixName.toUpperCase()) ||
                PhotoUtil.IMG_TYPE_JPEG.equals(suffixName.toUpperCase()) ||
                PhotoUtil.IMG_TYPE_JPG.equals(suffixName.toUpperCase()) ||
                PhotoUtil.IMG_TYPE_PNG.equals(suffixName.toUpperCase()) ||
                PhotoUtil.IMG_TYPE_SVG.equals(suffixName.toUpperCase()) ||
                PhotoUtil.IMG_TYPE_TIF.equals(suffixName.toUpperCase())) {
            return true;
        } else {
            throw new RuntimeException("图片" + originalFilename + "格式有误");
        }

    }

}
