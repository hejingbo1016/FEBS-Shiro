package cc.mrbird.febs.common.utils;


import com.google.common.collect.Lists;
import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.JPEGEncodeParam;
import com.sun.media.jai.codec.TIFFEncodeParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

public class ImageUtil {

    private static Logger log = LoggerFactory.getLogger(ImageUtil.class);

    /**
     * 生成背景透明的 文字水印，文字位于透明区域正中央，可设置旋转角度
     *
     * @param width  生成图片宽度
     * @param heigth 生成图片高度
     * @param text   水印文字
     * @param color  颜色对象
     * @param font   awt字体
     * @param degree 水印文字旋转角度
     * @param alpha  水印不透明度0f-1.0f
     */
    public static BufferedImage waterMarkByText(int width, int heigth, String text, Color color,
                                                Font font, Double degree, float alpha) {


        BufferedImage buffImg = new BufferedImage(width, heigth, BufferedImage.TYPE_INT_RGB);
        /**2、得到画笔对象*/
        Graphics2D g2d = buffImg.createGraphics();
        // ----------  增加下面的代码使得背景透明  -----------------
        buffImg = g2d.getDeviceConfiguration()
                .createCompatibleImage(width, heigth, Transparency.TRANSLUCENT);
        g2d.dispose();
        g2d = buffImg.createGraphics();
        // ----------  背景透明代码结束  -----------------

        // 设置对线段的锯齿状边缘处理
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // 设置水印旋转
        if (null != degree) {
            //注意rotate函数参数theta，为弧度制，故需用Math.toRadians转换一下
            //以矩形区域中央为圆心旋转
            g2d.rotate(Math.toRadians(degree), (double) buffImg.getWidth() / 2,
                    (double) buffImg.getHeight() / 2);
        }

        // 设置颜色
        g2d.setColor(color);

        // 设置 Font
        g2d.setFont(font);

        //设置透明度:1.0f为透明度 ，值从0-1.0，依次变得不透明
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        //获取真实宽度
        float realWidth = getRealFontWidth(text);
        float fontSize = font.getSize();
        //计算绘图偏移x、y，使得使得水印文字在图片中居中
        //这里需要理解x、y坐标是基于Graphics2D.rotate过后的坐标系
        float x = 0.5f * width - 0.5f * fontSize * realWidth;
        float y = 0.5f * heigth + 0.5f * fontSize;
        //取绘制的字串宽度、高度中间点进行偏移，使得文字在图片坐标中居中
        g2d.drawString(text, x, y);
        //释放资源
        g2d.dispose();
//        System.out.println("添加水印文字完成!");
        return buffImg;

    }

    /**
     * 根据要生成的图片宽度设置水印大小、并返回水印路径
     *
     * @param imgWidht
     * @param text
     * @return
     */
    public static String createWaterMarkByTextAndImgWidht(String basePath, int imgWidht, String text) {

        String filePath = basePath + "/watermark/";

        FileHepler.createDir(filePath);
        try {

            int fontSize = 18;
            if (!StringUtils.isEmpty(imgWidht)) {
                if (imgWidht >= 600) {
                    fontSize = imgWidht / 300 * fontSize;
                }

            }
            Font font = new Font("黑体", Font.ROMAN_BASELINE, fontSize);//字体

            //获取font的样式应用在str上的整个矩形
            Rectangle2D r = font.getStringBounds(text, new FontRenderContext(AffineTransform.getScaleInstance(1, 1),
                    false, false));
//        System.out.println("宽度："+r.getWidth());
            //获取单个字符的高度
            int unitHeight = (int) Math.floor(r.getHeight());
            //获取整个str用了font样式的宽度这里用四舍五入后+1保证宽度绝对能容纳这个字符串作为图片的宽度
            int width = (int) Math.round(r.getWidth()) + 1;
            //把单个字符的高度+3保证高度绝对能容纳字符串作为图片的高度
            int height = unitHeight + 3;

            BufferedImage bi1 = waterMarkByText(width, height, text, Color.DARK_GRAY, font, 0d,
                    0.85f);//给图片添加文字水印

            filePath = filePath + System.currentTimeMillis() + ".png";

            ImageIO.write(bi1, "png", new File(filePath));//写入文件
        } catch (IOException e) {
            e.printStackTrace();
            log.error("生成水印异常！", e.getMessage());
        }

        return filePath;
    }


    /**
     * 生成一个新的非JPG图片，根据文字生成带水印的图片
     *
     * @param width,生成宽度
     * @param height,高度
     * @param waterMarkText,水印文字
     * @param sourPath,原图片
     * @param savePath,照片保存路径
     * @return
     */
    public static void createNewImgByWaterMarkText(Integer width, Integer height,
                                                   String waterMarkText, String sourPath, String savePath, String basePath,
                                                   double quality) {
        ImageWriter writer = null;
        ImageOutputStream ios = null;

        try {
            File sourFile = new File(sourPath);
            String suffix = sourPath.substring(sourPath.lastIndexOf(".") + 1);
            Image image = ImageIO.read(sourFile);

            int imageWidth = image.getWidth(null);//原图片宽度
            int imageHeight = image.getHeight(null);//原图片高度

            Integer[] widthAndHeight = setCreateWidthHeight(imageWidth, imageHeight, width, height, quality);

            int createWidth = widthAndHeight[0];//生成图片的高度
            int createHeight = widthAndHeight[1];//生成图片的高度

            //把图片放入缓存区
            BufferedImage bufimg = new BufferedImage(createWidth, createHeight, BufferedImage.TYPE_INT_RGB);

            // 获取图像对象，来对图片进行处理
            Graphics2D g = bufimg.createGraphics();


            // ----------  增加下面的代码使得背景透明  -----------------
            bufimg = g.getDeviceConfiguration()
                    .createCompatibleImage(createWidth, createHeight, Transparency.TRANSLUCENT);
            g.dispose();
            g = bufimg.createGraphics();
            // ----------  背景透明代码结束  -----------------


            // 开始处理图片
            g.drawImage(image, 0, 0, createWidth, createHeight, null);

            RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            rh.put(RenderingHints.KEY_STROKE_CONTROL
                    , RenderingHints.VALUE_STROKE_PURE);
            rh.put(RenderingHints.KEY_ALPHA_INTERPOLATION
                    , RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g.setRenderingHints(rh);

            //需要填加水印
            if (!StringUtils.isEmpty(waterMarkText)) {
                String waterMarkPath = createWaterMarkByTextAndImgWidht(basePath, createWidth, waterMarkText);
                setICON(waterMarkPath, createWidth, createHeight, g);
//                Image icon=ImageIO.read(new File(waterMarkPath));
//
//                int iconH=icon.getHeight(null);
//                int iconW=icon.getWidth(null);
//
//                int  x = createWidth - iconW-5;
//                int  y = createHeight - iconH-5;
//
//                g.drawImage(icon,x,y,iconW,iconH,null);
            }

            g.dispose();
            ImageIO.write(bufimg, suffix, new FileOutputStream(savePath));

        } catch (IOException e) {
            e.printStackTrace();
            log.error("生成非JPG照片异常", e.getMessage());
        } finally {
            try {
                if (writer != null) {
                    writer.dispose();
                }

                if (ios != null) {
                    ios.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 生成一个新的非JPG图片，根据水印图片生成新照片
     *
     * @param width,生成宽度
     * @param height,高度
     * @param waterMarkPath,水印文字
     * @param sourPath,原图片
     * @param savePath,照片保存路径
     * @return
     */
    public static void createNewImgByWaterMarkPath(Integer width, Integer height,
                                                   String waterMarkPath, String sourPath, String savePath,
                                                   double quality) {
        ImageWriter writer = null;
        ImageOutputStream ios = null;

        try {
            File sourFile = new File(sourPath);
            String suffix = sourPath.substring(sourPath.lastIndexOf(".") + 1);
            Image image = ImageIO.read(sourFile);

            int imageWidth = image.getWidth(null);//原图片宽度
            int imageHeight = image.getHeight(null);//原图片高度

            Integer[] widthAndHeight = setCreateWidthHeight(imageWidth, imageHeight, width, height, quality);

            int createWidth = widthAndHeight[0];//生成图片的高度
            int createHeight = widthAndHeight[1];//生成图片的高度

            //把图片放入缓存区
            BufferedImage bufimg = new BufferedImage(createWidth, createHeight, BufferedImage.TYPE_INT_RGB);

//            // 获取图像对象，来对图片进行处理
            // 获取图像对象，来对图片进行处理
            Graphics2D g = bufimg.createGraphics();


            // ----------  增加下面的代码使得背景透明  -----------------
            bufimg = g.getDeviceConfiguration()
                    .createCompatibleImage(createWidth, createHeight, Transparency.TRANSLUCENT);
            g.dispose();
            g = bufimg.createGraphics();
            // ----------  背景透明代码结束  -----------------


            // 开始处理图片
            g.drawImage(image, 0, 0, createWidth, createHeight, null);

            RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            rh.put(RenderingHints.KEY_STROKE_CONTROL
                    , RenderingHints.VALUE_STROKE_PURE);
            rh.put(RenderingHints.KEY_ALPHA_INTERPOLATION
                    , RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g.setRenderingHints(rh);
            //需要填加水印
            if (!StringUtils.isEmpty(waterMarkPath)) {
                setICON(waterMarkPath, createWidth, createHeight, g);
//                Image icon=ImageIO.read(new File(waterMarkPath));
//
//                int iconH=icon.getHeight(null);
//                int iconW=icon.getWidth(null);
//
//                int  x = createWidth - iconW-5;
//                int  y = createHeight - iconH-5;
//
//                g.drawImage(icon,x,y,iconW,iconH,null);
            }

            g.dispose();
            ImageIO.write(bufimg, suffix, new FileOutputStream(savePath));

        } catch (IOException e) {
            e.printStackTrace();
            log.error("生成非JPG照片异常", e.getMessage());
        } finally {
            try {
                if (writer != null) {
                    writer.dispose();
                }

                if (ios != null) {
                    ios.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 生成一个新的JPG图片，根据文字生成带水印的图片
     *
     * @param width,生成宽度
     * @param height,高度
     * @param waterMarkText,水印文字
     * @param sourPath,原图片
     * @param savePath,照片保存路径
     * @param isRemoveExif,1、去掉EXIF信息，0、保留
     * @return
     */
    public static void createNewJpgImgByWaterMarkText(Integer width, Integer height,
                                                      String waterMarkText, String sourPath,
                                                      String basePath, String savePath, Integer isRemoveExif,
                                                      double quality) {
        ImageWriter writer = null;
        ImageOutputStream ios = null;

        try {
            File sourFile = new File(sourPath);
            Image image = ImageIO.read(sourFile);

            int imageWidth = image.getWidth(null);//原图片宽度
            int imageHeight = image.getHeight(null);//原图片高度

            Integer[] widthAndHeight = setCreateWidthHeight(imageWidth, imageHeight, width, height, quality);

            int createWidth = widthAndHeight[0];//生成图片的高度
            int createHeight = widthAndHeight[1];//生成图片的高度

            //把图片放入缓存区
            BufferedImage bufimg = new BufferedImage(createWidth, createHeight, BufferedImage.TYPE_INT_RGB);

//             获取图像对象，来对图片进行处理
            Graphics2D g = bufimg.createGraphics();


            // 开始处理图片
            g.drawImage(image, 0, 0, createWidth, createHeight, null);

            RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            rh.put(RenderingHints.KEY_STROKE_CONTROL
                    , RenderingHints.VALUE_STROKE_PURE);
            rh.put(RenderingHints.KEY_ALPHA_INTERPOLATION
                    , RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g.setRenderingHints(rh);

            //需要填加水印
            if (!StringUtils.isEmpty(waterMarkText)) {
                String waterMarkPath = createWaterMarkByTextAndImgWidht(basePath, createWidth, waterMarkText);
                setICON(waterMarkPath, createWidth, createHeight, g);
            }


            // 为了保证原质量输出，我们用ImageWriter输出图片

            // 获取ImageWriter对象
            Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpg");

            writer = iter.next();
            // 指定输出路径
            File f = new File(savePath);
            ios = ImageIO.createImageOutputStream(f);
            // 修改ImageWriteParam，原质量输出图片
            ImageWriteParam imageWriteParam = writer.getDefaultWriteParam();
            imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            imageWriteParam.setCompressionQuality(0.95f);

            // 将BufferedImage转换为IIOImage,进而输出图片
            IIOImage iio_image = setIIOImage(sourPath, isRemoveExif, bufimg);
//            if(isRemoveExif.equals(1)){
//                iio_image = new IIOImage(bufimg, null, null);
//            }else {
//                ImageReader reader = ImageIO.getImageReadersBySuffix("jpeg").next();
//                reader.setInput(ImageIO.createImageInputStream(new File(sourPath)));
//                IIOMetadata metadata = reader.getImageMetadata(0);
//                // 将BufferedImage转换为IIOImage,进而输出图片
//                iio_image = new IIOImage(bufimg, null, metadata);
//            }

            // 输出
            writer.setOutput(ios);
            writer.write(null, iio_image, imageWriteParam);

            g.dispose();

        } catch (IOException e) {
            e.printStackTrace();
            log.error("生成JPG照片异常", e);
        } finally {
            try {
                if (writer != null) {
                    writer.dispose();
                }

                if (ios != null) {
                    ios.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 生成一个新的JPG图片，根据水印图片生成新照片
     *
     * @param width,生成宽度
     * @param height,高度
     * @param waterMarkPath,水印文字
     * @param sourPath,原图片
     * @param savePath,照片保存路径
     * @param isRemoveExif,1、去掉EXIF信息，0、保留
     * @return
     */
    public static void createNewJpgImgByWaterMarkPath(Integer width, Integer height,
                                                      String waterMarkPath, String sourPath, String savePath, Integer isRemoveExif,
                                                      double quality) {
        ImageWriter writer = null;
        ImageOutputStream ios = null;

        try {
            File sourFile = new File(sourPath);
            Image image = ImageIO.read(sourFile);

            int imageWidth = image.getWidth(null);//原图片宽度
            int imageHeight = image.getHeight(null);//原图片高度


            Integer[] widthAndHeight = setCreateWidthHeight(imageWidth, imageHeight, width, height, quality);

            int createWidth = widthAndHeight[0];//生成图片的高度
            int createHeight = widthAndHeight[1];//生成图片的高度


            //把图片放入缓存区
            BufferedImage bufimg = new BufferedImage(createWidth, createHeight, BufferedImage.TYPE_INT_RGB);

            // 获取图像对象，来对图片进行处理
            Graphics2D g = bufimg.createGraphics();

            // 开始处理图片
            g.drawImage(image, 0, 0, createWidth, createHeight, null);

            RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            rh.put(RenderingHints.KEY_STROKE_CONTROL
                    , RenderingHints.VALUE_STROKE_PURE);
            rh.put(RenderingHints.KEY_ALPHA_INTERPOLATION
                    , RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g.setRenderingHints(rh);

            //需要填加水印
            if (!StringUtils.isEmpty(waterMarkPath)) {
                setICON(waterMarkPath, createWidth, createHeight, g);
//                Image icon=ImageIO.read(new File(waterMarkPath));
//
//                int iconH=icon.getHeight(null);
//                int iconW=icon.getWidth(null);
//
//                int  x = createWidth - iconW-5;
//                int  y = createHeight - iconH-5;
//
//                g.drawImage(icon,x,y,iconW,iconH,null);
            }

            // 获取ImageWriter对象
            Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpg");

            writer = iter.next();
            // 指定输出路径
            File f = new File(savePath);
            ios = ImageIO.createImageOutputStream(f);
            // 修改ImageWriteParam，原质量输出图片
            ImageWriteParam imageWriteParam = writer.getDefaultWriteParam();
            imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            imageWriteParam.setCompressionQuality(0.95f);

            // 将BufferedImage转换为IIOImage,进而输出图片
            IIOImage iio_image = setIIOImage(sourPath, isRemoveExif, bufimg);
//            if(isRemoveExif.equals(1)){
//                iio_image = new IIOImage(bufimg, null, null);
//            }else {
//                ImageReader reader = ImageIO.getImageReadersBySuffix("jpeg").next();
//                reader.setInput(ImageIO.createImageInputStream(new File(sourPath)));
//                IIOMetadata metadata = reader.getImageMetadata(0);
//                // 将BufferedImage转换为IIOImage,进而输出图片
//                iio_image = new IIOImage(bufimg, null, metadata);
//            }

            // 输出
            writer.setOutput(ios);
            writer.write(null, iio_image, imageWriteParam);

            g.dispose();

        } catch (IOException e) {
            e.printStackTrace();
            log.error("生成JPG照片异常", e.getMessage());
        } finally {
            try {
                if (writer != null) {
                    writer.dispose();
                }

                if (ios != null) {
                    ios.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 生成一个新的非JPG图片，根据水印图片生成新照片
     *
     * @param width,生成宽度
     * @param height,高度
     * @param sourPath,原图片
     * @param savePath,照片保存路径
     * @return
     */
    public static void createNewImg(Integer width, Integer height, String sourPath, String savePath,
                                    double quality) {
        ImageWriter writer = null;
        ImageOutputStream ios = null;

        try {
            File sourFile = new File(sourPath);
            String suffix = sourPath.substring(sourPath.lastIndexOf(".") + 1);
            Image image = ImageIO.read(sourFile);

            int imageWidth = image.getWidth(null);//原图片宽度
            int imageHeight = image.getHeight(null);//原图片高度

            Integer[] widthAndHeight = setCreateWidthHeight(imageWidth, imageHeight, width, height, quality);

            int createWidth = widthAndHeight[0];//生成图片的高度
            int createHeight = widthAndHeight[1];//生成图片的高度


            //把图片放入缓存区
            BufferedImage bufimg = new BufferedImage(createWidth, createHeight, BufferedImage.TYPE_INT_RGB);

            // 获取图像对象，来对图片进行处理
            Graphics2D g = bufimg.createGraphics();


            // ----------  增加下面的代码使得背景透明  -----------------
            bufimg = g.getDeviceConfiguration()
                    .createCompatibleImage(createWidth, createHeight, Transparency.TRANSLUCENT);
            g.dispose();
            g = bufimg.createGraphics();
            // ----------  背景透明代码结束  -----------------


            // 开始处理图片
            g.drawImage(image, 0, 0, createWidth, createHeight, null);

            RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            rh.put(RenderingHints.KEY_STROKE_CONTROL
                    , RenderingHints.VALUE_STROKE_PURE);
            rh.put(RenderingHints.KEY_ALPHA_INTERPOLATION
                    , RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g.setRenderingHints(rh);
            ImageIO.write(bufimg, suffix, new FileOutputStream(savePath));

        } catch (IOException e) {
            e.printStackTrace();
            log.error("生成非JPG照片异常", e.getMessage());
        } finally {
            try {
                if (writer != null) {
                    writer.dispose();
                }

                if (ios != null) {
                    ios.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 生成一个新的JPG图片
     *
     * @param width,生成宽度
     * @param height,高度
     * @param sourPath,原图片
     * @param savePath,照片保存路径
     * @param isRemoveExif,1、去掉EXIF信息，0、保留
     * @return
     */
    public static void createNewJpgImg(Integer width, Integer height, String sourPath, String savePath, Integer isRemoveExif,
                                       double quality) {
        ImageWriter writer = null;
        ImageOutputStream ios = null;

        try {
            File sourFile = new File(sourPath);
            Image image = ImageIO.read(sourFile);

            int imageWidth = image.getWidth(null);//原图片宽度
            int imageHeight = image.getHeight(null);//原图片高度


            Integer[] widthAndHeight = setCreateWidthHeight(imageWidth, imageHeight, width, height, quality);

            int createWidth = widthAndHeight[0];//生成图片的高度
            int createHeight = widthAndHeight[1];//生成图片的高度

//            if(!ObjectUtils.isEmpty(width)||!ObjectUtils.isEmpty(height)){
//                if(!ObjectUtils.isEmpty(height)){
//                    //如果要生成图片的高度比原图小，按新值生成
//                    if(height<imageHeight){
//                        createHeight=height;
//                        if(ObjectUtils.isEmpty(width)){
//                            createWidth=(int)Math.ceil(height*imageWidth*1.0/imageHeight);
//                        }
//
//                    }
//                }
//
//                if(!ObjectUtils.isEmpty(width)){
//                    if(width<imageWidth){
//                        createWidth=width;
//                        if(ObjectUtils.isEmpty(height)) {
//                            createHeight=(int)Math.ceil((width*imageHeight*1.0/imageWidth));
//                        }
//
//                    }
//                }
//            }

            //把图片放入缓存区
            //创建画板==将一副图片加载到内存中
            BufferedImage bufimg = new BufferedImage(createWidth, createHeight, BufferedImage.TYPE_INT_RGB);

            // 获取图像对象，来对图片进行处理
            Graphics2D g = bufimg.createGraphics();

            // 开始处理图片
            g.drawImage(image, 0, 0, createWidth, createHeight, null);

            RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            rh.put(RenderingHints.KEY_STROKE_CONTROL
                    , RenderingHints.VALUE_STROKE_PURE);
            rh.put(RenderingHints.KEY_ALPHA_INTERPOLATION
                    , RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g.setRenderingHints(rh);

            // 获取ImageWriter对象
            Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpg");

            writer = iter.next();
            // 指定输出路径
            File f = new File(savePath);
            ios = ImageIO.createImageOutputStream(f);
            // 修改ImageWriteParam，原质量输出图片
            ImageWriteParam imageWriteParam = writer.getDefaultWriteParam();
            imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            imageWriteParam.setCompressionQuality(0.95f);

            // 将BufferedImage转换为IIOImage,进而输出图片
            IIOImage iio_image = setIIOImage(sourPath, isRemoveExif, bufimg);

            // 输出
            writer.setOutput(ios);
            writer.write(null, iio_image, imageWriteParam);

            g.dispose();

        } catch (IOException e) {
            e.printStackTrace();
            log.error("生成JPG照片异常", e.getMessage());
        } finally {
            try {
                if (writer != null) {
                    writer.dispose();
                }

                if (ios != null) {
                    ios.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置IIOImage属性，用于写回照片里面
     *
     * @param sourPath
     * @param isRemoveExif
     * @param bufimg
     * @return
     */
    private static IIOImage setIIOImage(String sourPath, Integer isRemoveExif, BufferedImage bufimg) {
        IIOImage iio_image = null;
        if (isRemoveExif.equals(1)) {
            iio_image = new IIOImage(bufimg, null, null);
        } else {
            IIOMetadata metadata = null;
            try {
                ImageReader reader = ImageIO.getImageReadersBySuffix("jpeg").next();
                reader.setInput(ImageIO.createImageInputStream(new File(sourPath)));
                metadata = reader.getImageMetadata(0);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("读取图片属性有异常！" + e.getMessage());
            }
            // 将BufferedImage转换为IIOImage,进而输出图片
            iio_image = new IIOImage(bufimg, null, metadata);

        }
        return iio_image;
    }

    /**
     * 设置水印图片位置跟大小
     *
     * @param waterMarkPath
     * @param createWidth
     * @param createHeight
     * @param g
     * @throws IOException
     */
    private static void setICON(String waterMarkPath, int createWidth, int createHeight, Graphics2D g) throws IOException {

        Image icon = ImageIO.read(new File(waterMarkPath));

        int iconH = icon.getHeight(null);
        int iconW = icon.getWidth(null);

        int x = createWidth - iconW - 5;
        int y = createHeight - iconH - 5;

        g.drawImage(icon, x, y, iconW, iconH, null);
    }

    /**
     * 设置要生成照片的宽高
     *
     * @param imageWidth,原图片宽度
     * @param imageHeight,现图片高度
     * @param width,要生成的宽度
     * @param height,要生成的高度
     * @return Integer[宽，高]
     */
    private static Integer[] setCreateWidthHeight(int imageWidth, int imageHeight, Integer width, Integer height, double quality) {
        int createWidth = imageWidth;//生成图片的高度
        int createHeight = imageHeight;//生成图片的高度

        if (!ObjectUtils.isEmpty(width) || !ObjectUtils.isEmpty(height)) {
            if (!ObjectUtils.isEmpty(height)) {
                //如果要生成图片的高度比原图小，按新值生成
                if (height <= imageHeight) {
                    createHeight = height;
                    if (ObjectUtils.isEmpty(width)) {
                        createWidth = (int) Math.ceil(height * imageWidth * 1.0 / imageHeight);
                    } else {
                        if (height > imageHeight) {
                            createWidth = (int) Math.ceil((width * imageHeight * 1.0 / imageWidth));
                        }
                    }

                }
            }

            if (!ObjectUtils.isEmpty(width)) {
                if (width <= imageWidth) {
                    createWidth = width;
                    if (ObjectUtils.isEmpty(height)) {
                        createHeight = (int) Math.ceil((width * imageHeight * 1.0 / imageWidth));
                    } else {
                        if (height > imageHeight) {
                            createHeight = (int) Math.ceil((width * imageHeight * 1.0 / imageWidth));
                        }
                    }

                }
            }
        } else {
            createWidth = (int) Math.ceil(imageWidth * quality);
            createHeight = (int) Math.ceil(imageHeight * quality);
        }
        Integer[] wh = new Integer[]{createWidth, createHeight};
        return wh;
    }

    public static BufferedImage waterMarkByText(int width, int heigth, String text, Color color, float alpha) {
        //jdk默认字体
        Font font = new Font("Dialog", Font.ROMAN_BASELINE, 33);
        return waterMarkByText(width, heigth, text, color, font, -30d, alpha);
    }

    public static BufferedImage waterMarkByText(int width, int heigth, String text, float alpha) {
        return waterMarkByText(width, heigth, text, Color.GRAY, alpha);
    }


    public static BufferedImage waterMarkByText(int width, int heigth, String text) {
        return waterMarkByText(width, heigth, text, 0.2f);
    }

    public static BufferedImage waterMarkByText(String text) {
        return waterMarkByText(200, 150, text);
    }

    /**
     * 获取真实字符串宽度，ascii字符占用0.5，中文字符占用1.0
     */
    private static float getRealFontWidth(String text) {
        int len = text.length();
        float width = 0f;
        for (int i = 0; i < len; i++) {
            if (text.charAt(i) < 256) {
                width += 0.5f;
            } else {
                width += 1.0f;
            }
        }
        return width;
    }

    /**
     * TIF照片转换成JPG照片
     *
     * @param fileAbsolutePath
     * @return
     */
    public static java.util.List<String> tifToJpg(String fileAbsolutePath) {
        java.util.List<String> filePath = Lists.newArrayList();
        FileSeekableStream fileSeekStream = null;
        try {

            fileSeekStream = new FileSeekableStream(fileAbsolutePath);
            TIFFEncodeParam tiffEncodeParam = new TIFFEncodeParam();
            JPEGEncodeParam jpegEncodeParam = new JPEGEncodeParam();
            com.sun.media.jai.codec.ImageDecoder dec = ImageCodec.createImageDecoder("tiff", fileSeekStream, null);
            int count = dec.getNumPages();
            tiffEncodeParam.setCompression(TIFFEncodeParam.COMPRESSION_GROUP4);
            tiffEncodeParam.setLittleEndian(false);

            String filePathPrefix = fileAbsolutePath.substring(0, fileAbsolutePath.lastIndexOf("."));
            for (int i = 0; i < count; i++) {
                RenderedImage renderedImage = dec.decodeAsRenderedImage(i);
                String fileUrl = filePathPrefix + "_" + i + ".jpg";
                File imgFile = new File(fileUrl);

                ParameterBlock pb = new ParameterBlock();
                pb.addSource(renderedImage);
                pb.add(imgFile.toString());
                pb.add("JPEG");
                pb.add(jpegEncodeParam);
                RenderedOp renderedOp = JAI.create("filestore", pb);
                renderedOp.dispose();

                filePath.add(fileUrl);
            }
        } catch (IOException e) {
            log.error("TIF转换JPG异常," + e.getMessage());
        } finally {
            if (fileSeekStream != null) {
                try {
                    fileSeekStream.close();
                } catch (IOException e) {
                    log.error("TIF转换JPG异常," + e.getMessage());
                }
            }
        }

        return filePath;
    }
}
