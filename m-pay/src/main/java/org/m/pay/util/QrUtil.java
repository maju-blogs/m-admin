package org.m.pay.util;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;

public class QrUtil {


    public static String beautificationQr(ResourceLoader resourceLoader, String oldBase64Str, String logoPath) {
        try {
            BufferedImage bufferedImage = convertBase64StrToImage(oldBase64Str);
            String decode = QrCodeUtil.decode(bufferedImage);
            int width = 300;
            int height = 300;
            Resource resource = resourceLoader.getResource("classpath:" + logoPath);
            InputStream inputStream = null;
            QrConfig config = new QrConfig();
            inputStream = resource.getInputStream();
            BufferedImage logoImg = ImageIO.read(inputStream);
            config.setImg(logoImg);
            config.setHeight(height);
            config.setWidth(width);
            BufferedImage generate = QrCodeUtil.generate(decode, config);
            String png = convertImageToBase64Str(generate, "png");
            return png;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 图片转Base64字符串
     *
     * @return
     */
    public static String convertImageToBase64Str(BufferedImage bufferedImage, String suffix) {
        ByteArrayOutputStream baos = null;
        try {
            //通过ImageIO把文件读取成BufferedImage对象
            //构建字节数组输出流
            baos = new ByteArrayOutputStream();
            //写入流
            ImageIO.write(bufferedImage, suffix, baos);
            //通过字节数组流获取字节数组
            byte[] bytes = baos.toByteArray();
            //获取JDK8里的编码器Base64.Encoder转为base64字符
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * Base64字符串转图片
     *
     * @param base64String
     */
    public static BufferedImage convertBase64StrToImage(String base64String) {
        ByteArrayInputStream bais = null;
        try {
            if (base64String.startsWith("data:image/png;base64,")) {
                base64String = base64String.replace("data:image/png;base64,", "");
            }
            byte[] bytes = Base64.getDecoder().decode(base64String);
            //构建字节数组输入流
            bais = new ByteArrayInputStream(bytes);
            //通过ImageIO把字节数组输入流转为BufferedImage
            BufferedImage bufferedImage = ImageIO.read(bais);
            return bufferedImage;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bais != null) {
                    bais.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
