package com.ruoyi.common.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.ruoyi.common.utils.sign.Base64;
import org.springframework.util.FastByteArrayOutputStream;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;


/**
 * Created by HFY on 2023/6/6
 */
public class MyQrCodeUtil {
    private static final int CODE_WIDTH = 125;
    private static final int CODE_HEIGHT = 125;
    private static final String CODE_CHERSET = "UTF-8";

    public static BufferedImage createImage(String content){
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 0);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE,CODE_WIDTH , CODE_HEIGHT, // 修改二维码底部高度
                    hints);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return image;
    }

    /**
     * 对已经生成好的二维码设置logo
     * @param source 二维码
     * @param logo logo图片
     * @param logoWidth logo宽度
     * @param logoHeight logo高度
     */
    public static void insertLogoImage(BufferedImage source,Image logo,int logoWidth,int logoHeight){
        Graphics2D graph = source.createGraphics();
        int qrWidth = source.getWidth();
        int qrHeight = source.getHeight();
        int x = (qrWidth - logoWidth) / 2;
        int y = (qrHeight - logoHeight) / 2;
        graph.drawImage(logo, x, y, logoWidth, logoHeight, null);
        Shape shape = new RoundRectangle2D.Float(x, y, logoWidth, logoHeight, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    /**
     * 缩小logo图片
     * @param logoPath
     * @param logoWidth
     * @param logoHeight
     * @return
     */
    public static Image compressLogo(String logoPath, int logoWidth, int logoHeight){
        File file = new File(logoPath);
        if (!file.exists()) {
            System.err.println("" + logoPath + "   该文件不存在！");
            return null;
        }
        Image original = null;
        try {
            original = ImageIO.read(new File(logoPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int width = original.getWidth(null);
        int height = original.getHeight(null);
        if (width > logoWidth) {
            width = logoWidth;
        }
        if (height > logoHeight) {
            height = logoHeight;
        }
        Image image = original.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = tag.getGraphics();
        g.drawImage(image, 0, 0, null); // 绘制缩小后的图
        g.dispose();
        return image;
    }


    /**
     * 生成二维码图片
     * @param charSet 二维码编码方式
     * @param content 内容
     * @param qrWidth 宽度
     * @param qrHeight 长度
     * @param formatName jpg等图片格式
     * @param imgPath 二维码存放路径
     */
    public static void encode(String charSet,String content,int qrWidth,int qrHeight,String formatName,String imgPath){
        BufferedImage image = MyQrCodeUtil.createImage(content);
        try {
            ImageIO.write(image, formatName, new File(imgPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成二维码图片流
     * @param charSet 二维码编码方式
     * @param content 内容
     * @param qrWidth 宽度
     * @param qrHeight 长度
     * @return
     */
    public static BufferedImage encode(String charSet,String content,int qrWidth,int qrHeight) {
        BufferedImage image = MyQrCodeUtil.createImage(content);
        return image;
    }

    public static void encode( BufferedImage image,String formatName,String imgPath){
        try {

            ImageIO.write(image, formatName, new File(imgPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void mkdirs(String destPath) {
        File file = new File(destPath);
        // 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }

    /*
     * 解析二维码
     */
    public static String decode(BufferedImage image) throws Exception {
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable hints = new Hashtable();
        hints.put(DecodeHintType.CHARACTER_SET, CODE_CHERSET);
        result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        return resultStr;
    }

}
