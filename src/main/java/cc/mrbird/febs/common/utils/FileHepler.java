package cc.mrbird.febs.common.utils;

import cc.mrbird.febs.common.exception.BusinessRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * @author TP-110
 * @des 附件操作
 */
@Component
public class FileHepler {
    private static Logger log = LoggerFactory.getLogger(FileHepler.class);
    @Autowired
    private FilePathConfig filePathConfig;
    public static final String THUMBNAIL1_HEIGHT_KEY = "height1";
    public static final String THUMBNAIL2_HEIGHT_KEY = "height2";
    public static final String THUMBNAIL3_HEIGHT_KEY = "height3";
    public static final String THUMBNAIL1_WIDTH_KEY = "width1";
    public static final String THUMBNAIL2_WIDTH_KEY = "width2";
    public static final String THUMBNAIL3_WIDTH_KEY = "width3";

    /***
     * 删除文件
     * @param path
     * @return
     */
    public static boolean delFile(String path) {

        boolean result = true;
        try {
            File file = new File(path);

            if (file.exists()) {
                //如果是文件直接删除
                if (file.isFile()) {
                    file.delete();
                } else {
                    //文件夹
                    File[] files = file.listFiles();
                    if (files == null) {
                        file.delete();
                    } else {
                        for (int i = 0; i < files.length; i++) {
                            delFile(files[i].getAbsolutePath());
                        }
                        file.delete();
                    }
                }

            }
        } catch (Exception e) {
            log.error("删除图片异常", e);
            result = false;
        }

        return result;
    }

    /**
     * 删除多个文件
     *
     * @param paths
     * @return
     */
    public static boolean delListFile(String[] paths) {

        boolean result = true;
        try {
            for (String path : paths) {
                File file = new File(path);

                if (file.exists()) {
                    //如果是文件直接删除
                    if (file.isFile()) {
                        file.delete();
                    } else {
                        //文件夹
                        File[] files = file.listFiles();
                        if (files == null) {
                            file.delete();
                        } else {
                            for (int i = 0; i < files.length; i++) {
                                delFile(files[i].getAbsolutePath());
                            }
                            file.delete();
                        }
                    }

                }

            }
        } catch (Exception e) {
            log.error("删除图片异常", e);
            result = false;
        }

        return result;
    }

    /**
     * 从当前文件夹复制文件到另一个文件夹
     *
     * @param oldPath,旧文件名地址
     * @param newPath,新文件名地址
     * @return
     */
    public static boolean copyTo(String oldPath, String newPath) {
        boolean result = true;
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            File file = new File(oldPath);

            if (file.exists()) {

                String dirPath = newPath.substring(0, newPath.lastIndexOf("/"));
                //创建文件夹
                createDir(dirPath);
                File newFile = new File(newPath);
                //复制图片
//                Files.copy(file.toPath(),newFile.toPath());
                //网上说这个速度快
                inputChannel = new FileInputStream(oldPath).getChannel();
                outputChannel = new FileOutputStream(newPath).getChannel();
                outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
                inputChannel.close();
                outputChannel.close();
            }
        } catch (Exception e) {
            log.error("复制图片异常", e);

            result = false;
        }

        return result;
    }

    /***
     * 复制文件夹下面的所有文件到另一个文件夹下
     * @param oldDir,旧文件夹地址
     * @param newDir,新文件夹地址
     * @return
     */
    public static boolean copyDirTo(String oldDir, String newDir) {

        boolean result = true;
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {

            File file = new File(oldDir);

            if (file.exists()) {


                if (file.isFile()) {
//                    String newPath=newDir+"\\"+file.getName();
                    //创建文件夹
                    newDir = newDir.replace("\\", "/");
                    String createDir = newDir.substring(0, newDir.lastIndexOf("/"));
                    createDir(createDir);
                    File newFile = new File(newDir);
                    //复制图片
//                Files.copy(file.toPath(),newFile.toPath());
                    //网上说这个速度快
                    inputChannel = new FileInputStream(file.getAbsolutePath()).getChannel();
                    outputChannel = new FileOutputStream(newDir).getChannel();
                    outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
                    inputChannel.close();
                    outputChannel.close();
                } else {
                    File[] files = file.listFiles();

                    if (files != null) {
                        for (int i = 0; i < files.length; i++) {
//                            System.out.println("oldDir:"+files[i].getAbsolutePath());
//                            System.out.println("newDir:"+newDir+"\\"+files[i].getName());
                            copyDirTo(files[i].getAbsolutePath(), newDir + "\\" + files[i].getName());
//                            String newPath=newDir+"\\"+files[i].getName();
//                            File newFile=new File(newPath);
//                            //复制图片
////                Files.copy(file.toPath(),newFile.toPath());
//                            //网上说这个速度快
//                            inputChannel = new FileInputStream(files[i].getAbsolutePath()).getChannel();
//                            outputChannel = new FileOutputStream(newPath).getChannel();
//                            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
//                            inputChannel.close();
//                            outputChannel.close();
                        }
                    }
                }


            }
        } catch (Exception e) {
            log.error("复制夹异常", e);

            result = false;
        }

        return result;
    }


    /***
     * 创建文件夹
     * @param path
     */
    public static void createDir(String path) {

        File file = new File(path);
        //如果文件夹不存在则创建
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 文件是否存在
     *
     * @param path
     * @return
     */
    public static Boolean isExist(String path) {
//        System.out.println("fileDir");
//        path=getDir()+"/"+path;
//        path=path.replace("//","/");
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }

        return true;
    }

    /**
     * 获取项目路径
     *
     * @return
     */
    public String getDir() {
//        String dir = System.getProperty("user.dir");
//        String staticFile = new File(dir + "\\archives-fs\\archives-fs-service\\src\\main\\resources\\static").getPath().replaceAll("\\\\", "/");
        return filePathConfig.getBasePath();
    }

    /**
     * 判断文件夹下面是否存在文件
     *
     * @param filePath
     * @return
     */
    public static boolean isExistFiles(String filePath) {

        File file = new File(filePath);
        Boolean reslut = false;
        if (file.exists()) {
            File[] files = file.listFiles();

            if (files.length <= 0) {
                return false;
            } else {
                for (int i = 0; i < file.length(); i++) {
                    if (files[i].isFile()) {
                        reslut = true;
                        break;
                    } else {
                        reslut = isExistFiles(files[i].getAbsolutePath());
                    }
                }
            }
        }

        return reslut;
    }

    /**
     * 获取缩略图的宽高
     *
     * @return
     */
    public static Map<String, Integer> getImgWidthHeight(String imageHeight, String imageWidth) {
        if (!StringUtils.isEmpty(imageHeight) && !StringUtils.isEmpty(imageWidth)) {
            double height = Double.parseDouble(imageHeight.trim());
            double width = Double.parseDouble(imageWidth.trim());
            double height1 = 180.00;
            double height2 = 300.00;
            double height3 = 600.00;
            double ys1 = height / height1;
            double ys2 = height / height2;
            double ys3 = height / height3;
            Integer width1 = new Double(width / ys1).intValue();
            Integer width2 = new Double(width / ys2).intValue();
            Integer width3 = new Double(width / ys3).intValue();
            if (width1 > width) {
                height1 = height;
                width1 = new Double(width).intValue();
            }
            if (width2 > width) {
                height2 = height;
                width2 = new Double(width).intValue();
            }
            if (width3 > width) {
                height3 = height;
                width3 = new Double(width).intValue();
            }
            Map image = new HashMap<String, Integer>();
            image.put(FileHepler.THUMBNAIL1_HEIGHT_KEY, new Double(height1).intValue());
            image.put(FileHepler.THUMBNAIL2_HEIGHT_KEY, new Double(height2).intValue());
            image.put(FileHepler.THUMBNAIL3_HEIGHT_KEY, new Double(height3).intValue());
            image.put(FileHepler.THUMBNAIL1_WIDTH_KEY, width1);
            image.put(FileHepler.THUMBNAIL2_WIDTH_KEY, width2);
            image.put(FileHepler.THUMBNAIL3_WIDTH_KEY, width3);
            return image;
        } else {
            return null;
        }
    }

    /**
     * 给文件夹权限
     */
    public static void getRuntime(String path) {
        try {
            Runtime.getRuntime().exec("chmod -R 644 " + path);
        } catch (Exception ex) {
            throw new BusinessRuntimeException("设置文件权限异常:", ex.getMessage());
        }
    }


    /**
     * 重新命名照片
     *
     * @param oldPath
     */
    public static Boolean renameTo(String oldPath, String newFileName) {
        try {

            File oldFile = new File(oldPath);

            if (oldFile.exists() && oldFile.isFile()) {

                String newPath = oldFile.getParent() + "/" + newFileName;
                newPath = newPath.replace("\\", "/");
//                System.out.println(newPath);
                File newFile = new File(newPath);
                oldFile.renameTo(newFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("重新命名归档之后的照片异常！路径为：" + oldPath, e.getMessage());
            return false;
        }

        return true;
    }
}
