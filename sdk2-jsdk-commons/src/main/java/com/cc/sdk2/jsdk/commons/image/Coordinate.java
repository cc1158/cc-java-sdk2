package com.cc.sdk2.jsdk.commons.image;

/**
 * 坐标轴
 * @author sen.hu
 * @date 2019/9/26 15:44
 **/
public class Coordinate {

    private int x;
    private int y;

    public Coordinate() {
    }

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
