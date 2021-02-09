package com.cc.sdk2.jsdk.commons.image;

import com.cc.sdk2.jsdk.commons.Assert;
import com.cc.sdk2.jsdk.commons.utils.StringUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * All rights reserved, copyright@cc.hu
 * 水印工具
 *
 * @author cc
 * @version 1.0
 * @date 2019/5/11 19:03
 **/
public class WatermarkUtils {

    /**
     *  文本水印
     * @param markText  文本
     * @param originImage   原始图片
     * @return  添加水印后的图片
     */
    public static BufferedImage addTextMark(String markText, BufferedImage originImage) {
        if (originImage == null || StringUtil.isNullOrEmpty(markText)) {
            Assert.notNull("parameter is  null");
        }
        int width = originImage.getWidth(null);
        int height = originImage.getHeight(null);

        BufferedImage markedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 得到画笔对象!
        Graphics2D g = markedImage.createGraphics();
        // 设置对线段的锯齿状边缘处理
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(originImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        // 设置水印旋转
//        g.rotate(Math.toRadians(-45), (double) markedImage.getWidth() / 2, (double) markedImage.getHeight() / 2);
        // 设置颜色
        g.setColor(Color.red);
        // 设置 Font
        g.setFont(new Font("宋体", Font.BOLD | Font.ITALIC, 25));
        float alpha = 0.5f;
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));

        FontMetrics fontMetrics = g.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(markText) + 15;
        // 第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y) .
        g.drawString(markText, width - textWidth, height - 15);
        g.dispose();
        return markedImage;
    }

    /**
     * 添加图片水印
     * @param imageIcon
     * @param originImage
     * @return
     */
    public static BufferedImage addImageMark(ImageIcon imageIcon, BufferedImage originImage) {
        if (imageIcon == null || originImage == null) {
            Assert.notNull("parameters is null");
        }
        int width = originImage.getWidth(null);
        int height = originImage.getHeight(null);
        BufferedImage markedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = markedImage.createGraphics();
        // 设置对线段的锯齿状边缘处理
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(originImage.getScaledInstance(originImage.getWidth(null), originImage.getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);
        //透明度
        float alpha = 0.5f;
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
        // 表示水印图片的位置
        Image image = imageIcon.getImage();
        int x = width - (image.getWidth(null) + 15);
        int y = height - (image.getHeight(null) + 15);
        g.drawImage(image, x, y, null);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        g.dispose();
        return markedImage;
    }

}
