package com.cc.sdk2.test.jsdk.commons;

import com.cc.sdk2.jsdk.commons.image.CaptchaUtils;
import com.cc.sdk2.jsdk.commons.image.ThumbnailUtils;
import com.cc.sdk2.jsdk.commons.image.WatermarkUtils;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * All rights reserved, copyright@cc.hu
 *
 * @author cc
 * @version 1.0
 * @date 2021/2/9 10:14
 **/
public class TestImage {
    @Test
    public void testCaptchaUtil() {
        String code = CaptchaUtils.genCaptchaCode();
        try {
            ImageIO.write(CaptchaUtils.genCaptchaImage(code), "jpg", new FileOutputStream(new File("C:\\Users\\Administrator\\Desktop\\test.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testThumbnail() {
        try {
            BufferedImage bufferedImage = ImageIO.read(new File("D:\\Documents\\person\\sen.hu\\桌面\\20.jpg"));
            BufferedImage dest = ThumbnailUtils.thumbnail(bufferedImage, 400, 200, true);
            ImageIO.write(dest, "jpg", new File("D:\\Documents\\person\\sen.hu\\桌面\\201.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDrawText() {
        int x = 16, y = 20;
        int outWidth = 700, outHeight = 1000;
        int contetWidth = 668;
        BufferedImage bufferedImage = new BufferedImage(outWidth, outHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(Color.white);
        graphics2D.fillRect(0, 0, outWidth, outHeight);//填充背景色
        graphics2D.setColor(Color.black);
        Font font = new Font("Microsoft YaHei", Font.BOLD, 14);
        graphics2D.setFont(font);
        String text = "中部六省其实近年来风头很劲，河南安徽湖北湖南地区都算是经济明星，连江西都经常出来刷存在感。中部省份这些年来一直在努力承接东部的产业转移，但是从数据上看，这种产业承接工作算不上成功。当然，事实上我们也知道，东部的企业主们的优先选择是迁往东南亚国家。因为这样的原因，中部地区的财政亏空规模一直持续放大。2014年的财政亏空规模6509亿，到2017年提升到9897亿，涨幅达到52%。";
        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        System.out.println(fontMetrics.getHeight());
        System.out.println(font);
        int lines = 0;
        if (fontMetrics.stringWidth(text) < contetWidth) {
            graphics2D.drawString(text, x, y);
        } else {
            java.util.List<String> charList = new java.util.ArrayList<String>();
            for (int i = 0; i < text.length(); i++) {
                charList.add(String.valueOf(text.charAt(i)));
            }
            while (charList.size() > 0) {
                StringBuilder oneLine = new StringBuilder();

                while (fontMetrics.stringWidth(oneLine.toString()) < contetWidth) {
                    if (charList.size() > 0) {
                        oneLine.append(charList.remove(0));
                    } else {
                        break;
                    }
                }
                System.out.println(fontMetrics.stringWidth(oneLine.toString()) + "\n" + oneLine.toString().toCharArray().length);
                if (lines == 0) {
                    graphics2D.drawString(oneLine.toString(), x, y);
                } else {
                    y = lines * fontMetrics.getHeight() + 20;
                    graphics2D.drawString(oneLine.toString(), x, y);
                }
                lines++;
            }
        }
        y = y + 20;
        //测试画图片
        try {
            BufferedImage test = ImageIO.read(new File("D:\\Documents\\person\\sen.hu\\桌面\\uploads\\test.jpg"));
            int width = test.getWidth();
            int height = test.getHeight();
            int left = (outWidth - width) / 2;
            graphics2D.drawImage(test, left, y, width, height, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ImageIO.write(bufferedImage, "JPEG", new File("D:\\Documents\\person\\sen.hu\\桌面\\uploads\\drawing.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWartermark() {
        try {
            BufferedImage image = ImageIO.read(new File("C:\\Users\\Administrator\\Desktop\\test.jpg"));
            BufferedImage outImage = WatermarkUtils.addTextMark("我喜欢美女", image);
            ImageIO.write(outImage, "jpg", new File("C:\\Users\\Administrator\\Desktop\\test1.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testImageWartermark() {
        try {
            BufferedImage image = ImageIO.read(new File("C:\\Users\\Administrator\\Desktop\\test.jpg"));
            ImageIcon imageIcon = new ImageIcon("C:\\Users\\Administrator\\Desktop\\logo.jpg");
            BufferedImage outImage = WatermarkUtils.addImageMark(imageIcon, image);
            ImageIO.write(outImage, "jpg", new File("C:\\Users\\Administrator\\Desktop\\test1.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
