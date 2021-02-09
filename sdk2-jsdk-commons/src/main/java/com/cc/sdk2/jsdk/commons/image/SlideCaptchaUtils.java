package com.cc.sdk2.jsdk.commons.image;

import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * slide captcha image utility
 *
 * @author sen.hu
 * @date 2019/9/26 15:33
 **/
public class SlideCaptchaUtils {

    private static final int CIRCLE_R = 10;
    private static final int R1 = 5;

    private static final int MARGIN = 30;

    private static final int PUZZLE_WIDTH = 65;
    private static final int PUZZLE_HEIGHT = 45;


    private static int[][] getPuzzleData(int pw, int ph) {
        int[][] ret = new int[pw][ph];

        double opaX = pw - CIRCLE_R;

        double h1 = CIRCLE_R + Math.random() * (ph - 3 * CIRCLE_R - R1);
        double po = CIRCLE_R * CIRCLE_R;

        double opaXBegin = pw - CIRCLE_R - R1;
        double opaYBegin = ph - CIRCLE_R - R1;

        for (int i = 0; i < pw; i++) {
            for (int j = 0; j < ph; j++) {
                double d3 = Math.pow(i - opaX, 2) + Math.pow(j - h1, 2);
                double d2 = Math.pow(j - 2, 2) + Math.pow(i - h1, 2);
                if ((j <= opaYBegin && d2 <= po) || (i >= opaXBegin && d3 >= po)) {
                    ret[i][j] = 0;
                } else {
                    ret[i][j] = 1;
                }

            }
        }
        return ret;
    }


    public static Coordinate randomPuzzleCorner(BufferedImage bgImage, int pw, int ph) {
        int maxWidth = bgImage.getWidth(null) - MARGIN - pw;
        int maxHeight = bgImage.getHeight(null) - MARGIN - ph;
        Random random = new Random();
        int rndX = random.nextInt(maxWidth);
        int rndY = random.nextInt(maxHeight);
        Coordinate ret = new Coordinate();
        ret.setX(rndX <= MARGIN ? rndX + MARGIN : rndX);
        ret.setY(rndY <= MARGIN ? rndY + MARGIN : rndY);
        return ret;
    }

    public static BufferedImage createPuzzleAndShadowBackground(BufferedImage background) {
        return createPuzzleAndShadowBackground(background, randomPuzzleCorner(background, PUZZLE_WIDTH, PUZZLE_HEIGHT), PUZZLE_WIDTH, PUZZLE_HEIGHT);
    }

    public static BufferedImage createPuzzleAndShadowBackground(BufferedImage background, int puzzleX, int puzzleY, int puzzleWidth, int puzzleHeight) {
        return createPuzzleAndShadowBackground(background, new Coordinate(puzzleX, puzzleY), puzzleWidth, puzzleHeight);
    }


    public static BufferedImage createPuzzleAndShadowBackground(BufferedImage background, Coordinate coordinate, int puzzleWidth, int puzzleHeight) {
        if (background == null) {
            return null;
        }
        BufferedImage puzzle = new BufferedImage(puzzleWidth, puzzleHeight, BufferedImage.TYPE_4BYTE_ABGR);

        int[][] puzzleData = getPuzzleData(puzzleWidth, puzzleHeight);

        for (int i = 0; i < puzzleWidth; i++) {
            for (int j = 0; j < puzzleHeight; j++) {
                int rgb = puzzleData[i][j];
                // 原图中对应位置变色处理
                int rgb_ori = background.getRGB(coordinate.getX() + i, coordinate.getY() + j);
                if (rgb == 1) {
                    //抠图上复制对应颜色值
                    puzzle.setRGB(i, j, rgb_ori);
                    //原图对应位置颜色变化
                    background.setRGB(coordinate.getX() + i, coordinate.getY() + j, rgb_ori & 0x666666);
                } else {
                    //这里把背景设为透明
                    puzzle.setRGB(i, j, rgb_ori & 0x00ffffff);
                }
            }
        }
        return puzzle;
    }
}
