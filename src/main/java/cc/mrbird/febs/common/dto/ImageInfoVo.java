package cc.mrbird.febs.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/***
 * 图片属性相关信息
 */
@Data
public class ImageInfoVo {

    //图像参数==========Exif类信息

    @ApiModelProperty(name = "x_resolution",value = "Exif IFD0  XResolution元素,水平分辨率")
    private  String xResolution;

    @ApiModelProperty(name = "y_resolution",value = "Exif IFD0 YResolutio,垂直分辨率")
    private  String yResolution;

    @ApiModelProperty(name = "image_height",value = "ImageHengt,图像高度")
    private  String imageHeight;

    @ApiModelProperty(name = "image_width",value = " ImageWidth,图像宽度")
    private  String imageWidth;

    @ApiModelProperty(name = "color_space",value = " Colorspace,色彩空间")
    private  String colorSpace;

    @ApiModelProperty(name = "ycbcr",value = "YcbCrCoefficients，这个值表示转换成 RGB格式的一个常量. 通常, 这个值是'0.299/0.587/0.114',YCbCr分量")
    private  String ycbcr;

    @ApiModelProperty(name = "samples_per_pixel",value = "SamplesPerPiexl, 每像素样本数")
    private  String samplesPerPixel;

    @ApiModelProperty(name = "bits_per_sample",value = "BitsPerSample,每样本位数")
    private  String bitsPerSample;

    @ApiModelProperty(name = "image_compression_scheme",value = " Compression,压缩方案")
    private  String imageCompressionScheme;

    @ApiModelProperty(name = "image_compression_rate",value = " CompressedBitsPerPixel,压缩率")
    private  String imageCompressionRate;

    ///捕获设备
    @ApiModelProperty(name = "device_manufacturer",value = " Make,设备制造商")
    private  String deviceManufacturer;

    @ApiModelProperty(name = "device_type",value = " Model,设备型号")
    private  String deviceType;

    @ApiModelProperty(name = "device_model_number",value = " SensingMethod,设备感光器")
    private  String deviceModelNumber;

    @ApiModelProperty(name = "device_sensor",value = " SensingMethod,软件信息")
    private  String deviceSensor;

    //全球定位信息
    @ApiModelProperty(name = "gps_version",value = "该元数据元素对应Exif2.2的GPS versionID元素")
    private  String gpsVersion;

    @ApiModelProperty(name = "gps_latitude_reference",value = "GPSLatitudeRef, 纬度基准")
    private  String gpsLatitudeReference;

    @ApiModelProperty(name = "latitude",value = "GPSLatitude ,纬度")
    private  String latitude;

    @ApiModelProperty(name = "longitude_reference",value = "GPSLongitudeRef ,经度基准")
    private  String longitudeReference;

    @ApiModelProperty(name = "longitude",value = "GPSLongitudeRef 经度")
    private  String longitude;

    @ApiModelProperty(name = "altitude_reference",value = "GPSAltitudeRef ,海拔基准")
    private  String altitudeReference;

    @ApiModelProperty(name = "altitude",value = "GPSAltitude ,海拔")
    private  String altitude;

    @ApiModelProperty(name = "image_direction_reference",value = "GPSImgDirectionRef ,方向基准")
    private  String imageDirectionReference;

    @ApiModelProperty(name = "image_direction",value = "GPSImgDirection ,镜头方向")
    private  String imageDirection;

    //图片基本属性
    @ApiModelProperty(name = "imageSize",value = "图片大小")
    private  Long imageSize;

    @ApiModelProperty(name = "imageType",value = "图片格式")
    private  String imageType;

    @ApiModelProperty(name = "imageMD5",value = "MD5值")
    private  String imageMD5;

    @ApiModelProperty(name = "imageName",value = "图片名称")
    private  String imageName;

    @ApiModelProperty(name = "imageName",value = "图片名称，不包括后缀")
    private  String imageNa;

    @ApiModelProperty(name = "imageCreateTime",value = "图片创建时间")
    private Date imageCreateTime;

    @ApiModelProperty(name = "Artist",value = "作者、艺术家、摄影师")
    private  String Artist;

    @ApiModelProperty(name = "tiftoJpgUrlList",value = "tif文件转换成JPG之后的地址")
    private List<String> tiftoJpgUrlList;
}
