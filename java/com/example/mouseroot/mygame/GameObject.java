package com.example.mouseroot.mygame;

import android.graphics.Rect;

/**
 * Created by mouseroot on 4/10/17.
 */

public abstract class GameObject {
    protected int x;
    protected int y;
    protected int dx;
    protected int dy;
    protected int width;
    protected int height;

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return this.x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return this.y;
    }

    public void setDX(int dx) {
        this.dx = dx;
    }

    public int getDX() {
        return this.dx;
    }

    public void setDY(int dy) {
        this.dy = dy;
    }

    public int getDY() {
        return this.dy;
    }

    public void setWidth(int w) {
        this.width = w;
    }

    public int getWidth() {
        return this.width;
    }

    public void setHeight(int h) {
        this.height = h;
    }

    public int getHeight() {
        return this.height;
    }

    public Rect getRectangle() {
        return new Rect(x,y,x+width,y+height);
    }

}
