package cc.mrbird.febs.common.service;


import cc.mrbird.febs.common.dto.ImageInfoVo;
import cc.mrbird.febs.common.exception.BusinessRuntimeException;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.DateUtils;
import cc.mrbird.febs.common.utils.FileHepler;
import cc.mrbird.febs.common.utils.ImageUtil;
import cn.hutool.core.io.FileUtil;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.bmp.BmpHeaderDirectory;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.gif.GifHeaderDirectory;
import com.drew.metadata.jfif.JfifDirectory;
import com.drew.metadata.jpeg.JpegDirectory;
import com.drew.metadata.png.PngDirectory;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/***
 * 使用Thumbnails类进行图片相关处理
 */
public class ImgHelperService {

    //CommonsMultipartFile files
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FileHepler fileHepler;

    /***
     * 是否按比例缩放还是变大图片
     * @param fromPic,原图片路径
     * @param toPic,需要保存的图片路径
     * @param scale,缩放比例,大于1就是变大,小于1就是缩小
     */
    public void thumbnailsOfScale(String fromPic, String toPic, Double scale) {

        try {
            Thumbnails.of(fromPic).scale(StringUtils.isEmpty(scale) ? 1f : scale).toFile(toPic);
        } catch (Exception ex) {
            String msg = "按比例绽放/变大图片异常，path".concat(fromPic).concat(",toPath：").concat(toPic)
                    .concat(",scale：").concat(scale.toString());
            log.error(msg, ex);
        }

    }

    /***
     * 控制图片质量,图片尺寸不变
     * @param fromPic,原图片路径
     * @param toPic,需要保存的图片路径
     * @param quality,图片质量
     */
    public void thumbnailsOfQuality(String fromPic, String toPic, Double quality) {
        try {
            Thumbnails.of(fromPic).scale(1f).outputQuality(StringUtils.isEmpty(quality) ? 1f : quality).toFile(toPic);
        } catch (Exception ex) {
            String msg = "控制图片质量,图片尺寸不变，path".concat(fromPic).concat(",toPath：").concat(toPic)
                    .concat(",scale：").concat(quality.toString());
            log.error(msg, ex);
        }
    }

    /***
     * 给图片添加水印
     * @param fromPic,原图片路径
     * @param toPic,需要保存的图片路径
     * @param waterPic,水印图片路径
     * @param scale,缩放比例,大于1就是变大,小于1就是缩小
     * @param quality,图片质量
     * @param positions,水印位置
     */
    public void addWatermark(String fromPic, String toPic, String waterPic, Double scale, Double quality, Positions positions) {
        try {

            Thumbnails.of(fromPic)
                    .scale(StringUtils.isEmpty(scale) ? 1f : scale)
                    .watermark(positions, ImageIO.read(new File(waterPic)), 0.5f)
                    .outputQuality(StringUtils.isEmpty(quality) ? 1f : quality).toFile(toPic);
        } catch (Exception ex) {
            String msg = "给图片添加水印，path".concat(fromPic).concat(",toPath：").concat(toPic)
                    .concat(",waterPic：").concat(waterPic.toString());
            log.error(msg, ex);
        }
    }

    /***
     * 旋转图片
     * @param fromPic,原图片路径
     * @param toPic,需要保存的图片路径
     * @param rotate,正数向右,负数向左
     */
    public void rotate(String fromPic, String toPic, Integer rotate) {
        try {
            Thumbnails.of(fromPic).scale(1f).rotate(rotate).toFile(toPic);
        } catch (Exception ex) {
            String msg = "旋转图片，path".concat(fromPic).concat(",toPath：").concat(toPic)
                    .concat(",rotate：").concat(rotate.toString());
            log.error(msg, ex);
        }
    }

    /***
     * 按高宽缩放图片
     * @param fromPic,原图片路径
     * @param toPic,需要保存的图片路径
     * @param width,高度
     * @param height,宽度
     */
    public void thumbnailsOfSize(String fromPic, String toPic, Integer width, Integer height) {
        try {
            if (!ObjectUtils.isEmpty(width) && !ObjectUtils.isEmpty(height)) {
                Thumbnails.of(fromPic).forceSize(width, height).toFile(toPic);
            }

            if (!ObjectUtils.isEmpty(width) && ObjectUtils.isEmpty(height)) {
                Thumbnails.of(fromPic).width(width).keepAspectRatio(true).toFile(toPic);
            }

            if (ObjectUtils.isEmpty(width) && !ObjectUtils.isEmpty(height)) {
                Thumbnails.of(fromPic).height(height).keepAspectRatio(true).toFile(toPic);
            }
        } catch (Exception ex) {
            String msg = "按高宽缩放图片，path".concat(fromPic).concat(",toPath：").concat(toPic)
                    .concat(",width：").concat(width.toString())
                    .concat(",height：").concat(height.toString());
            log.error(msg, ex);
        }
    }

    public void thumbnailsOfSize(InputStream is, String toPic, Integer width, Integer height) {
        try {
            if (!ObjectUtils.isEmpty(width) && !ObjectUtils.isEmpty(height)) {
                Thumbnails.of(is).forceSize(width, height).toFile(toPic);
            }

            if (!ObjectUtils.isEmpty(width) && ObjectUtils.isEmpty(height)) {
                Thumbnails.of(is).width(width).keepAspectRatio(true).toFile(toPic);
            }

            if (ObjectUtils.isEmpty(width) && !ObjectUtils.isEmpty(height)) {
                Thumbnails.of(is).height(height).keepAspectRatio(true).toFile(toPic);
            }
        } catch (Exception ex) {
            log.error("按高宽缩放图片异常:", ex);
        }
    }

    /***
     *
     * @return
     * @param path,图片路径,先上传到服务器再读取
     */
    public ImageInfoVo getImgInfo(String path) {
        try {
            File jpegFile = new File(path);//("D:/117-YJ-2016-0002-0001.JPG");
            Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);//读取图片
            return getModel(path, jpegFile, metadata);
        } catch (Exception ex) {
            log.error("读取图片信息异常:", ex);
            throw new RuntimeException("读取图片信息异常: " + ex.getMessage());
        }
    }

    /**
     * 获取照片属性信息
     *
     * @param path
     * @param is
     * @return
     */
    public ImageInfoVo getImgInfo(String path, InputStream is) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(is);
            ImageInfoVo model = getModelNew(metadata, is);
            if (!ObjectUtils.isEmpty(model)) {
                String sufixx = path.substring(path.lastIndexOf(".") + 1);
                model.setImageType(sufixx);
                //tif的需要转换成JGP
                if (model.getImageType().equals("tif") || model.getImageType().equals("tiff")) {
                    model.setTiftoJpgUrlList(ImageUtil.tifToJpg(path));
                }
            }


            return model;
        } catch (Exception ex) {
            log.error("读取图片信息异常:", ex);
            throw new RuntimeException("读取图片信息异常: " + ex.getMessage());
        }
    }


    private ImageInfoVo getModelNew(Metadata metadata, InputStream is) {
        ImageInfoVo model = new ImageInfoVo();
        // 取具体某一项的信息
        Iterable<ExifIFD0Directory> exifIFD0Directories = metadata.getDirectoriesOfType(ExifIFD0Directory.class);

        if (exifIFD0Directories != null) {
            Iterator<ExifIFD0Directory> iterable = exifIFD0Directories.iterator();

            if (iterable != null && iterable.hasNext()) {

                ExifIFD0Directory dr = iterable.next();
                String value = dr.getDescription(ExifIFD0Directory.TAG_X_RESOLUTION);
                //水平分辨率
                model.setXResolution(StringUtils.isEmpty(value) ? "" : value.replace("dots", "")
                        .replace("per", "").replace("inch", "").trim());
                //垂直分辨率
                value = dr.getDescription(ExifIFD0Directory.TAG_Y_RESOLUTION);

                model.setYResolution(StringUtils.isEmpty(value) ? "" : value.replace("dots", "")
                        .replace("per", "").replace("inch", "").trim());

                //色彩空间
                value = dr.getDescription(ExifIFD0Directory.TAG_COLOR_SPACE);

                model.setColorSpace(StringUtils.isEmpty(value) ? "" : value);

                //YCbCr分量
                value = dr.getDescription(ExifIFD0Directory.TAG_YCBCR_SUBSAMPLING);

                model.setYcbcr(StringUtils.isEmpty(value) ? "" : value);
                //每像素样本数
                value = dr.getDescription(ExifIFD0Directory.TAG_SAMPLES_PER_PIXEL);

                model.setSamplesPerPixel(StringUtils.isEmpty(value) ? "" : value.replace("samples/pixel", "").trim());

                //每样本位数
                value = dr.getDescription(ExifIFD0Directory.TAG_BITS_PER_SAMPLE);
                value = StringUtils.isEmpty(value) ? "" : value
                        .replace(" ", ",").trim().replace(",bits/component/pixel", "");
                model.setBitsPerSample(StringUtils.isEmpty(value) ? "" : value);

                //压缩方案
                value = dr.getDescription(ExifIFD0Directory.TAG_COMPRESSION);
                model.setImageCompressionScheme(StringUtils.isEmpty(value) ? "" : value);

                //压缩率
                value = dr.getDescription(ExifIFD0Directory.TAG_COMPRESSED_AVERAGE_BITS_PER_PIXEL);
                model.setImageCompressionRate(StringUtils.isEmpty(value) ? "" : value);

                //设备制造商
                value = dr.getDescription(ExifIFD0Directory.TAG_MAKE);
                model.setDeviceManufacturer(StringUtils.isEmpty(value) ? "" : value);

                //设备型号
                value = dr.getDescription(ExifIFD0Directory.TAG_MODEL);
                model.setDeviceType(StringUtils.isEmpty(value) ? "" : value);

                //设备感光器
                value = dr.getDescription(ExifIFD0Directory.TAG_SENSING_METHOD);
                model.setDeviceModelNumber(StringUtils.isEmpty(value) ? "" : value);

                //软件信息
                value = dr.getDescription(ExifIFD0Directory.TAG_SOFTWARE);
                model.setDeviceSensor(StringUtils.isEmpty(value) ? "" : value);

                //图像高度
                value = dr.getDescription(ExifIFD0Directory.TAG_IMAGE_HEIGHT);
                model.setImageHeight(StringUtils.isEmpty(value) ? "" : value.replace("pixels", "").trim());
                //宽度
                value = dr.getDescription(ExifIFD0Directory.TAG_IMAGE_WIDTH);
                model.setImageWidth(StringUtils.isEmpty(value) ? "" : value.replace("pixels", "").trim());
            }
        }

        Iterable<JpegDirectory> jpegDirectories = metadata.getDirectoriesOfType(JpegDirectory.class);

        if (jpegDirectories != null) {
            Iterator<JpegDirectory> iterable = jpegDirectories.iterator();
            if (iterable != null && iterable.hasNext()) {
                JpegDirectory dr = iterable.next();
                String value = "";
                if (StringUtils.isEmpty(model.getImageHeight())) {
                    //图像高度
                    value = dr.getDescription(JpegDirectory.TAG_IMAGE_HEIGHT);
                    model.setImageHeight(StringUtils.isEmpty(value) ? "" : value.replace("pixels", "").trim());
                }

                if (StringUtils.isEmpty(model.getImageWidth())) {
                    //宽度
                    value = dr.getDescription(JpegDirectory.TAG_IMAGE_WIDTH);
                    model.setImageWidth(StringUtils.isEmpty(value) ? "" : value.replace("pixels", "").trim());
                }
            }
        }

        //定位
        Iterable<GpsDirectory> gpsDirectories = metadata.getDirectoriesOfType(GpsDirectory.class);
        if (gpsDirectories != null) {
            Iterator<GpsDirectory> iterable = gpsDirectories.iterator();
            if (iterable != null && iterable.hasNext()) {
                GpsDirectory dr = iterable.next();
                //全球定位系统版本
                String value = dr.getDescription(GpsDirectory.TAG_VERSION_ID);

                model.setGpsVersion(StringUtils.isEmpty(value) ? "" : value);
                //纬度基准
                value = dr.getDescription(GpsDirectory.TAG_LATITUDE_REF);

//                model.setGpsLatitudeReference(StringUtils.isEmpty(value) ? "" : value);
                if (!StringUtils.isEmpty(value)) {
                    switch (value.toUpperCase()) {
                        case "S":
                            model.setGpsLatitudeReference("South");
                            break;
                        case "N":
                            model.setGpsLatitudeReference("North");
                            break;
                    }
                } else {
                    model.setGpsLatitudeReference("");
                }
                //纬度
                value = dr.getDescription(GpsDirectory.TAG_LATITUDE);
                model.setLatitude(StringUtils.isEmpty(value) ? "" : value);
//                model.setLatitude(StringUtils.isEmpty(value) ? "" : value.replace("°",",")
//                        .replace("'",",").replace("\"","").trim());

                //经度基准
                value = dr.getDescription(GpsDirectory.TAG_LONGITUDE_REF);

                model.setLongitudeReference(StringUtils.isEmpty(value) ? "" : value);

                if (!StringUtils.isEmpty(value)) {
                    switch (value.toUpperCase()) {
                        case "E":
                            model.setLongitudeReference("East");
                            break;
                        case "W":
                            model.setLongitudeReference("West");
                            break;
                    }
                } else {
                    model.setLongitudeReference("");
                }

                //经度
                value = dr.getDescription(GpsDirectory.TAG_LONGITUDE);

                model.setLongitude(StringUtils.isEmpty(value) ? "" : value);
//                model.setLongitude(StringUtils.isEmpty(value) ? "" : value.replace("°",",")
//                        .replace("'",",").replace("\"","").trim());

                //海拔基准
                value = dr.getDescription(GpsDirectory.TAG_ALTITUDE_REF);

                model.setAltitudeReference(StringUtils.isEmpty(value) ? "" : value);

                //海拔
                value = dr.getDescription(GpsDirectory.TAG_ALTITUDE);

                model.setAltitude(StringUtils.isEmpty(value) ? "" : value.replace("metres", "").trim());

                //方向基准
//                    value=dr.getDescription(GpsDirectory.TAG_IMAGE_d);
//
//                    model.setImageDirectionReference(StringUtils.isEmpty(value)?"":value);

                //镜头方向
                value = dr.getDescription(GpsDirectory.TAG_IMAGE_DESCRIPTION);

                model.setImageDirection(StringUtils.isEmpty(value) ? "" : value.replace("degrees", "").trim());

            }
        }
      /*  //读取图片大小
        model.setImageSize(jpegFile.length());
        //图片格式
        model.setImageType(jpegFile.getName().substring(jpegFile.getName().lastIndexOf(".") + 1).trim().toLowerCase());*/
        //MD5值
        if (is != null) {
            model.setImageMD5(getmd5HashCode(is));
        }
        Iterable<ExifSubIFDDirectory> subIFDDirectories = metadata.getDirectoriesOfType(ExifSubIFDDirectory.class);
        if (subIFDDirectories != null) {
            Iterator<ExifSubIFDDirectory> iterable = subIFDDirectories.iterator();
            if (iterable != null && iterable.hasNext()) {
                ExifSubIFDDirectory dr = iterable.next();

                String tt = dr.getDescription(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                if (!StringUtils.isEmpty(tt)) {
                    String date = tt.split(" ")[0];
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        model.setImageCreateTime(sdf.parse(date.replace(":", "-") + " " + tt.split(" ")[1]));
                    } catch (ParseException e) {
                        throw new BusinessRuntimeException("格式转换异常:", e.getMessage());
                    }
                } else {
                    model.setImageCreateTime(new Date());
                }

                String value = "";

                if (StringUtils.isEmpty(model.getColorSpace())) {
                    //色彩空间
                    value = dr.getDescription(ExifSubIFDDirectory.TAG_COLOR_SPACE);

                    model.setColorSpace(StringUtils.isEmpty(value) ? "Uncalibrated" : value);
                }

                if (StringUtils.isEmpty(model.getYcbcr())) {
                    //YCbCr分量
                    value = dr.getDescription(ExifSubIFDDirectory.TAG_YCBCR_SUBSAMPLING);

                    model.setYcbcr(StringUtils.isEmpty(value) ? "" : value);
                }

                if (StringUtils.isEmpty(model.getImageHeight())) {
                    //图像高度
                    value = dr.getDescription(ExifSubIFDDirectory.TAG_IMAGE_HEIGHT);
                    model.setImageHeight(StringUtils.isEmpty(value) ? "" : value.replace("pixels", ""));
                }

                if (StringUtils.isEmpty(model.getImageWidth())) {
                    //宽度
                    value = dr.getDescription(ExifSubIFDDirectory.TAG_IMAGE_WIDTH);
                    model.setImageWidth(StringUtils.isEmpty(value) ? "" : value.replace("pixels", ""));
                }


            }
        }


        Iterable<BmpHeaderDirectory> bmpHeaderDirectories = metadata.getDirectoriesOfType(BmpHeaderDirectory.class);

        if (bmpHeaderDirectories != null) {
            Iterator<BmpHeaderDirectory> iterable = bmpHeaderDirectories.iterator();
            if (iterable != null && iterable.hasNext()) {
                BmpHeaderDirectory dr = iterable.next();

                String value = "";
                if (StringUtils.isEmpty(model.getImageHeight())) {
                    //图像高度
                    value = dr.getDescription(BmpHeaderDirectory.TAG_IMAGE_HEIGHT);
                    model.setImageHeight(StringUtils.isEmpty(value) ? "" : value.replace("pixels", ""));
                }

                if (StringUtils.isEmpty(model.getImageWidth())) {
                    //宽度
                    value = dr.getDescription(BmpHeaderDirectory.TAG_IMAGE_WIDTH);
                    model.setImageWidth(StringUtils.isEmpty(value) ? "" : value.replace("pixels", ""));
                }


            }
        }

        Iterable<PngDirectory> pngDirectories = metadata.getDirectoriesOfType(PngDirectory.class);
        if (pngDirectories != null) {
            Iterator<PngDirectory> iterable = pngDirectories.iterator();
            if (iterable != null && iterable.hasNext()) {
                PngDirectory dr = iterable.next();

                String value = "";
                if (StringUtils.isEmpty(model.getImageHeight())) {
                    //图像高度
                    value = dr.getDescription(PngDirectory.TAG_IMAGE_HEIGHT);
                    model.setImageHeight(StringUtils.isEmpty(value) ? "" : value.replace("pixels", ""));
                }

                if (StringUtils.isEmpty(model.getImageWidth())) {
                    //宽度
                    value = dr.getDescription(PngDirectory.TAG_IMAGE_WIDTH);
                    model.setImageWidth(StringUtils.isEmpty(value) ? "" : value.replace("pixels", ""));
                }

                if (StringUtils.isEmpty(model.getBitsPerSample())) {
                    value = dr.getDescription(PngDirectory.TAG_BITS_PER_SAMPLE);
                    model.setBitsPerSample(StringUtils.isEmpty(value) ? "" : value);
                }
            }
        }


        Iterable<GifHeaderDirectory> gifHeaderDirectories = metadata.getDirectoriesOfType(GifHeaderDirectory.class);
        if (gifHeaderDirectories != null) {
            Iterator<GifHeaderDirectory> iterable = gifHeaderDirectories.iterator();
            if (iterable != null && iterable.hasNext()) {
                GifHeaderDirectory dr = iterable.next();

                String value = "";
                if (StringUtils.isEmpty(model.getImageHeight())) {
                    //图像高度
                    value = dr.getDescription(GifHeaderDirectory.TAG_IMAGE_HEIGHT);
                    model.setImageHeight(StringUtils.isEmpty(value) ? "" : value.replace("pixels", ""));
                }

                if (StringUtils.isEmpty(model.getImageWidth())) {
                    //宽度
                    value = dr.getDescription(GifHeaderDirectory.TAG_IMAGE_WIDTH);
                    model.setImageWidth(StringUtils.isEmpty(value) ? "" : value.replace("pixels", ""));
                }

                if (StringUtils.isEmpty(model.getBitsPerSample())) {
                    value = dr.getDescription(GifHeaderDirectory.TAG_BITS_PER_PIXEL);
                    model.setBitsPerSample(StringUtils.isEmpty(value) ? "" : value);
                }
            }
        }


        Iterable<JfifDirectory> jfifDescriptors = metadata.getDirectoriesOfType(JfifDirectory.class);
        if (jfifDescriptors != null) {
            Iterator<JfifDirectory> iterable = jfifDescriptors.iterator();
            if (iterable != null && iterable.hasNext()) {
                JfifDirectory dr = iterable.next();

                String value = "";

                if (StringUtils.isEmpty(model.getXResolution())) {
                    value = dr.getDescription(JfifDirectory.TAG_RESX);
                    model.setXResolution(StringUtils.isEmpty(value) ? "" : value.replace("dots", "").trim());
                }

                if (StringUtils.isEmpty(model.getYResolution())) {
                    value = dr.getDescription(JfifDirectory.TAG_RESY);
                    model.setYResolution(StringUtils.isEmpty(value) ? "" : value.replace("dots", "").trim());
                }
            }
        }

        if (StringUtils.isEmpty(model.getImageCreateTime())) {
            model.setImageCreateTime(DateUtil.getDate());
        }

        if (StringUtils.isEmpty(model.getColorSpace())) {
            model.setColorSpace("Uncalibrated");
        }
        return model;
    }

    private ImageInfoVo getModel(String path, File jpegFile, Metadata metadata) {
        ImageInfoVo model = new ImageInfoVo();
        // 取具体某一项的信息
        {
            Iterable<ExifIFD0Directory> exifIFD0Directories = metadata.getDirectoriesOfType(ExifIFD0Directory.class);
            Iterator<ExifIFD0Directory> iterable = exifIFD0Directories.iterator();
            if (iterable.hasNext()) {

                ExifIFD0Directory dr = iterable.next();
                String value = dr.getDescription(ExifIFD0Directory.TAG_X_RESOLUTION);
                //水平分辨率
                model.setXResolution(StringUtils.isEmpty(value) ? "" : value.replace("dots", "")
                        .replace("per", "").replace("inch", "").trim());
                //垂直分辨率
                value = dr.getDescription(ExifIFD0Directory.TAG_Y_RESOLUTION);

                model.setYResolution(StringUtils.isEmpty(value) ? "" : value.replace("dots", "")
                        .replace("per", "").replace("inch", "").trim());

                //色彩空间
                value = dr.getDescription(ExifIFD0Directory.TAG_COLOR_SPACE);

                model.setColorSpace(StringUtils.isEmpty(value) ? "" : value);

                //YCbCr分量
                value = dr.getDescription(ExifIFD0Directory.TAG_YCBCR_POSITIONING);

                model.setYcbcr(StringUtils.isEmpty(value) ? "" : value);
                //每像素样本数
                value = dr.getDescription(ExifIFD0Directory.TAG_SAMPLES_PER_PIXEL);

                model.setSamplesPerPixel(StringUtils.isEmpty(value) ? "" : value.replace("samples/pixel", "").trim());

                //每样本位数
                value = dr.getDescription(ExifIFD0Directory.TAG_BITS_PER_SAMPLE);
                value = StringUtils.isEmpty(value) ? "" : value
                        .replace(" ", ",").trim().replace(",bits/component/pixel", "");
//                System.out.println(" bits/component/pixel:"+value);
                model.setBitsPerSample(value);

                //压缩方案
                value = dr.getDescription(ExifIFD0Directory.TAG_COMPRESSION);
                model.setImageCompressionScheme(StringUtils.isEmpty(value) ? "" : value);

                //压缩率
                value = dr.getDescription(ExifIFD0Directory.TAG_COMPRESSED_AVERAGE_BITS_PER_PIXEL);
                model.setImageCompressionRate(StringUtils.isEmpty(value) ? "" : value);

                //设备制造商
                value = dr.getDescription(ExifIFD0Directory.TAG_MAKE);
                model.setDeviceManufacturer(StringUtils.isEmpty(value) ? "" : value);

                //设备型号
                value = dr.getDescription(ExifIFD0Directory.TAG_MODEL);
                model.setDeviceType(StringUtils.isEmpty(value) ? "" : value);

                //设备感光器
                value = dr.getDescription(ExifIFD0Directory.TAG_SENSING_METHOD);
                model.setDeviceModelNumber(StringUtils.isEmpty(value) ? "" : value);

                //软件信息
                value = dr.getDescription(ExifIFD0Directory.TAG_SOFTWARE);
                model.setDeviceSensor(StringUtils.isEmpty(value) ? "" : value);

                //作者
                value = dr.getDescription(ExifIFD0Directory.TAG_ARTIST);
                model.setArtist(StringUtils.isEmpty(value) ? "" : value.replace(";", ","));


                //图像高度
                value = dr.getDescription(ExifIFD0Directory.TAG_IMAGE_HEIGHT);
                model.setImageHeight(StringUtils.isEmpty(value) ? "" : value.replace("pixels", ""));

                //宽度
                value = dr.getDescription(ExifIFD0Directory.TAG_IMAGE_WIDTH);
                model.setImageWidth(StringUtils.isEmpty(value) ? "" : value.replace("pixels", ""));
            }
        }

        {
            Iterable<JpegDirectory> jpegDirectories = metadata.getDirectoriesOfType(JpegDirectory.class);
            Iterator<JpegDirectory> iterable = jpegDirectories.iterator();
            if (iterable.hasNext()) {
                JpegDirectory dr = iterable.next();
                //图像高度
                String value = "";
                if (StringUtils.isEmpty(model.getImageHeight())) {
                    value = dr.getDescription(JpegDirectory.TAG_IMAGE_HEIGHT);
                    model.setImageHeight(StringUtils.isEmpty(value) ? "" : value.replace("pixels", ""));
                }
                if (StringUtils.isEmpty(model.getImageWidth())) {
                    //宽度
                    value = dr.getDescription(JpegDirectory.TAG_IMAGE_WIDTH);
                    model.setImageWidth(StringUtils.isEmpty(value) ? "" : value.replace("pixels", ""));
                }
            }
        }

        {
            //定位
            Iterable<GpsDirectory> gpsDirectories = metadata.getDirectoriesOfType(GpsDirectory.class);
            Iterator<GpsDirectory> iterable = gpsDirectories.iterator();
            if (iterable.hasNext()) {
                GpsDirectory dr = iterable.next();
                //全球定位系统版本
                String value = dr.getDescription(GpsDirectory.TAG_VERSION_ID);

                model.setGpsVersion(StringUtils.isEmpty(value) ? "" : value);
                //纬度基准
                value = dr.getDescription(GpsDirectory.TAG_LATITUDE_REF);

                model.setGpsLatitudeReference(StringUtils.isEmpty(value) ? "" : value);

                //纬度
                value = dr.getDescription(GpsDirectory.TAG_LATITUDE);

                model.setLatitude(StringUtils.isEmpty(value) ? "" : value.replace("°", ",")
                        .replace("'", ",").replace("\"", "").trim());

                //经度基准
                value = dr.getDescription(GpsDirectory.TAG_LONGITUDE_REF);

                model.setLongitudeReference(StringUtils.isEmpty(value) ? "" : value);

                //经度
                value = dr.getDescription(GpsDirectory.TAG_LONGITUDE);

                model.setLongitude(StringUtils.isEmpty(value) ? "" : value.replace("°", ",")
                        .replace("'", ",").replace("\"", "").trim());

                //海拔基准
                value = dr.getDescription(GpsDirectory.TAG_ALTITUDE_REF);

                model.setAltitudeReference(StringUtils.isEmpty(value) ? "" : value);

                //海拔
                value = dr.getDescription(GpsDirectory.TAG_ALTITUDE);

                model.setAltitude(StringUtils.isEmpty(value) ? "" : value.replace("metres", "").trim());

                //方向基准
//                    value=dr.getDescription(GpsDirectory.TAG_IMAGE_d);
//
//                    model.setImageDirectionReference(StringUtils.isEmpty(value)?"":value);

                //镜头方向
                value = dr.getDescription(GpsDirectory.TAG_IMAGE_DESCRIPTION);

                model.setImageDirection(StringUtils.isEmpty(value) ? "" : value.replace("degrees", "").trim());

            }
            //读取图片大小
            model.setImageSize(jpegFile.length());
            //图片格式
            model.setImageType(jpegFile.getName().substring(jpegFile.getName().lastIndexOf(".") + 1).trim().toLowerCase());
            //MD5值
            if (!path.isEmpty()) {
                model.setImageMD5(getmd5HashCode(path));
            }
        }
        {
            Iterable<ExifSubIFDDirectory> subIFDDirectories = metadata.getDirectoriesOfType(ExifSubIFDDirectory.class);
            Iterator<ExifSubIFDDirectory> iterable = subIFDDirectories.iterator();
            if (iterable.hasNext()) {
                ExifSubIFDDirectory dr = iterable.next();
                String tt = dr.getDescription(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                if (!StringUtils.isEmpty(tt)) {
                    String date = tt.split(" ")[0];
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        model.setImageCreateTime(sdf.parse(date.replace(":", "-") + " " + tt.split(" ")[1]));
                    } catch (ParseException e) {
                        throw new BusinessRuntimeException("格式转换异常:", e.getMessage());
                    }
                }

                String value = "";


                if (StringUtils.isEmpty(model.getColorSpace())) {
                    //色彩空间
                    value = dr.getDescription(ExifIFD0Directory.TAG_COLOR_SPACE);

                    if (StringUtils.isEmpty(value) || value.equals("Undefined")) {
                        value = "Uncalibrated";
                    }

                    model.setColorSpace(value);
                }
                if (StringUtils.isEmpty(model.getYcbcr())) {
                    //YCbCr分量
                    value = dr.getDescription(ExifSubIFDDirectory.TAG_YCBCR_SUBSAMPLING);

                    model.setYcbcr(StringUtils.isEmpty(value) ? "" : value);
                }

                if (StringUtils.isEmpty(model.getImageHeight())) {
                    //图像高度
                    value = dr.getDescription(ExifSubIFDDirectory.TAG_IMAGE_HEIGHT);
                    model.setImageHeight(StringUtils.isEmpty(value) ? "" : value.replace("pixels", ""));
                }

                if (StringUtils.isEmpty(model.getImageWidth())) {
                    //宽度
                    value = dr.getDescription(ExifSubIFDDirectory.TAG_IMAGE_WIDTH);
                    model.setImageWidth(StringUtils.isEmpty(value) ? "" : value.replace("pixels", ""));
                }

            }
        }
        return model;
    }

    /**
     * 获取文件的md5值 ，有可能不是32位
     *
     * @param is
     * @return
     * @throws FileNotFoundException
     */
    public String getmd5HashCode(InputStream is) {
        return md5HashCode(is);
    }

    /**
     * 获取文件的md5值 ，有可能不是32位
     *
     * @param filePath 文件路径
     * @return
     * @throws FileNotFoundException
     */
    public String getmd5HashCode(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            return md5HashCode(fis);
        } catch (IOException e) {
            log.error("读取文件路径异常 ==>", e);
            throw new BusinessRuntimeException("读取文件路径异常", e.getMessage());
        }
    }

    /**
     * java获取文件的md5值
     *
     * @param is 输入流
     * @return
     */
    public String md5HashCode(InputStream is) {

        try {
            //拿到一个MD5转换器,如果想使用SHA-1或SHA-256，则传入SHA-1,SHA-256
            MessageDigest md = MessageDigest.getInstance("MD5");

            //分多次将一个文件读入，对于大型文件而言，比较推荐这种方式，占用内存比较少。
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = is.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, length);
            }
            //转换并返回包含16个元素字节数组,返回数值范围为-128到127
            byte[] md5Bytes = md.digest();
            BigInteger bigInt = new BigInteger(1, md5Bytes);//1代表绝对值
            return bigInt.toString(16);//转换为16进制
        } catch (Exception e) {
            log.error("流读取异常:", e);
            return "";
        }
    }

    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    public Map<String, Map> setUpThumbnailPhoto(String imgUrls, String thumbnailFath, String originalFilename, MultipartFile multipartFile, ImgHelperService imgHelperService) throws IOException {
        ImageInfoVo imageInfoVo = null;
        Map<String, Integer> imageMap = new HashMap<>();
        Map<String, String> createTime = new HashMap<>();

        HashMap<String, Map> hashMap = new HashMap<>();
        String stringDates = null;
        //判断文件目录是否存在
        File isExistsFile = new File(imgUrls.replace(originalFilename, ""));//静态资源的绝对路径 + 临时文件的路径
        if (!isExistsFile.exists()) {
            isExistsFile.mkdirs();//创建目录
        }
        File file = new File(imgUrls);
        file.setWritable(true);
        file.setReadable(true);
        FileUtil.writeBytes(multipartFile.getBytes(), file);
        //生成缩略图
        imageInfoVo = imgHelperService.getImgInfo(imgUrls, multipartFile.getInputStream());
        if (!StringUtils.isEmpty(imageInfoVo.getImageHeight()) && !StringUtils.isEmpty(imageInfoVo.getImageWidth())) {
            imageMap = FileHepler.getImgWidthHeight(imageInfoVo.getImageHeight(), imageInfoVo.getImageWidth());
            imgHelperService.thumbnailsOfSize(imgUrls, thumbnailFath, imageMap.get("width1"), imageMap.get("height1"));
        }
        if (!Objects.isNull(imageInfoVo) && !StringUtils.isEmpty(imageInfoVo.getImageCreateTime())) {
            stringDates = DateUtils.getStringDates(imageInfoVo.getImageCreateTime());
        } else {
            //假如找不到照片属性中的创建时间，则以系统当前时间为照片创建时间
            stringDates = DateUtils.getStringDateShort();
        }
        createTime.put("imageCreateTime", stringDates);
        hashMap.put("imageMap", imageMap);
        hashMap.put("createTime", createTime);
        return hashMap;
    }
}
