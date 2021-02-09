package com.cc.sdk2.jsdk.commons.image;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;

/**
 * 验证码图片生成工具类
 *
 * @author cc.hu
 * @date 2019-05-11
 */
public class CaptchaUtils {
    /**
     * 去除0 O等不容易分辨字符
     */
    private static final char[] CHAPTCHA_RESOURCE = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F', 'g', 'G', 'h', 'H', 'i', 'I', 'j', 'J', 'k', 'K', 'l', 'L',
            'm', 'M', 'n', 'N', 'p', 'P', 'q', 'Q', 'r', 'R', 's', 'S', 't', 'T', 'u', 'U', 'v', 'V', 'w', 'W', 'x', 'X',
            'y', 'Y', 'z', 'Z'};
    private static final Color[] COLOR_SPACE = new Color[]{Color.WHITE, Color.CYAN, Color.GRAY, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.YELLOW};

    private static final int DEFAULT_WIDTH = 200;
    private static final int DEFAULT_HEIGHT = 80;
    private static final int DEFAULT_CODE_LENGTH = 4;

    public static String genCaptchaCode() {
        return genCaptchaCode(DEFAULT_CODE_LENGTH);
    }

    public static String genCaptchaCode(int codeLength) {
        StringBuilder codeBuf = new StringBuilder();
        Random random = new Random();
        int i = 0;
        while (i < codeLength) {
            int index = random.nextInt(CHAPTCHA_RESOURCE.length);
            codeBuf.append(CHAPTCHA_RESOURCE[index]);
            i++;
        }
        return codeBuf.toString();
    }

    public static BufferedImage genCaptchaImage(String code) {
        return genCaptchaImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, code);
    }

    public static BufferedImage genCaptchaImage(int width, int height, String code) {
        int codeLength = code.length();
        BufferedImage captchaIamge = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D gcs2d = captchaIamge.createGraphics();
        gcs2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //生成随机颜色
        Random rnd = new Random();
        Color[] colors = new Color[5];
        float[] fractions = new float[colors.length];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = COLOR_SPACE[rnd.nextInt(COLOR_SPACE.length)];
            fractions[i] = rnd.nextFloat();
        }
        Arrays.sort(fractions);
        //设置边框颜色
        gcs2d.setColor(Color.gray);
        //填充矩形
        gcs2d.fillRect(0, 0, width, height);

        Color c = getRandomColor(200, 250);
        // 设置背景色
        gcs2d.setColor(c);
        gcs2d.fillRect(0, 2, width, height - 4);

        //绘制干扰线
        Random random = new Random();
        // 设置线条的颜色
        gcs2d.setColor(getRandomColor(160, 200));
        for (int i = 0; i < 20; i++) {
            int x = random.nextInt(width - 1);
            int y = random.nextInt(height - 1);
            int xl = random.nextInt(6) + 1;
            int yl = random.nextInt(12) + 1;
            gcs2d.drawLine(x, y, x + xl + 40, y + yl + 20);
        }
        // 添加噪点
        // 噪声率
        float yawpRate = 0.05f;
        int area = (int) (yawpRate * width * height);
        for (int i = 0; i < area; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int rgb = getRandomIntColor();
            captchaIamge.setRGB(x, y, rgb);
        }
        shearX(gcs2d, width, height, c);
        shearY(gcs2d, width, height, c);

        gcs2d.setColor(getRandomColor(100, 160));
        int fontSize = height - 4;
        //阿尔及利亚只有大写
        Font font = new Font("Algerian", Font.ITALIC, fontSize);
        //Font font = new Font("Times New Roman", Font.ITALIC, fontSize);
        gcs2d.setFont(font);
        char[] chars = code.toCharArray();
        for (int i = 0; i < codeLength; i++) {
            AffineTransform affine = new AffineTransform();
            affine.setToRotation(Math.PI / 4 * rnd.nextDouble() * (rnd.nextBoolean() ? 1 : -1), (width / codeLength) * i + fontSize / 2, height / 2);
            gcs2d.setTransform(affine);
            gcs2d.drawChars(chars, i, 1, ((width - 10) / codeLength) * i + 5, height / 2 + fontSize / 2 - 10);
        }
        gcs2d.dispose();
        return captchaIamge;
    }

    private static void shearX(Graphics g, int w1, int h1, Color color) {
        Random random = new Random();
        int period = random.nextInt(2);
        boolean borderGap = true;
        int frames = 1;
        int phase = random.nextInt(2);

        for (int i = 0; i < h1; i++) {
            double d = (double) (period >> 1)
                    * Math.sin((double) i / (double) period
                    + (6.2831853071795862D * (double) phase)
                    / (double) frames);
            g.copyArea(0, i, w1, 1, (int) d, 0);
            if (borderGap) {
                g.setColor(color);
                g.drawLine((int) d, i, 0, i);
                g.drawLine((int) d + w1, i, w1, i);
            }
        }

    }

    private static void shearY(Graphics g, int w1, int h1, Color color) {
        Random random = new Random();
         // 50;
        int period = random.nextInt(40) + 10;
        boolean borderGap = true;
        int frames = 20;
        int phase = 7;
        for (int i = 0; i < w1; i++) {
            double d = (double) (period >> 1)
                    * Math.sin((double) i / (double) period
                    + (6.2831853071795862D * (double) phase)
                    / (double) frames);
            g.copyArea(i, 0, 1, h1, 0, (int) d);
            if (borderGap) {
                g.setColor(color);
                g.drawLine(i, (int) d, i, 0);
                g.drawLine(i, (int) d + h1, i, h1);
            }

        }

    }

    //生成随机颜色
    private static Color getRandomColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    private static int getRandomIntColor() {
        Random random = new Random();
        int[] rgb = new int[3];
        for (int i = 0; i < 3; i++) {
            rgb[i] = random.nextInt(255);
        }
        int color = 0;
        for (int c : rgb) {
            color = color << 8;
            color = color | c;
        }
        return color;
    }



}
