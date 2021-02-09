package com.cc.sdk2.jsdk.commons.image;

import java.awt.image.BufferedImage;

/**
 * All rights reserved, copyright@cc.hu
 * 字符串工具类
 *
 * @author cc
 * @version 1.0
 * @date 2020/4/27 23:21
 **/
public class ThumbnailUtils {

    private static final ThumbnailScale thumbnailScale = new ThumbnailScale();

    private ThumbnailUtils() {
    }

    /**
     * http://www.cnblogs.com/XL-Liang/archive/2011/12/14/2287566.html
     *
     *
     *
     */

    /**
     * 按比例生成缩略图
     * @param bufferedImage
     * @param iW    缩略图宽
     * @param iH    缩略图高
     * @return  缩略图
     */
    public static BufferedImage thumbnail(BufferedImage bufferedImage, int iW, int iH, boolean isRatio) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        if (iW > width || iH > height) {
            return bufferedImage;
        }
        int tW = iW;
        int tH = iH;
        if (isRatio) {
            double wScale = (double) iW / width;
            double hScale = (double) iH / height;
            if (wScale < hScale) {
                tH = (int) Math.round(height * wScale);
            } else if (wScale > hScale) {
                tW = (int) Math.round(width * hScale);
            }
        }
        return thumbnailScale.imageZoomOut(bufferedImage, tW, tH);
    }



}
