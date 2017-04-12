package com.example.mouseroot.mygame;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Background {
    private Bitmap image;
    private int x,y,dx;

    public Background(Bitmap image) {

        this.image = image;

    }

    public void update() {
        x += dx;
        if(x <= -image.getWidth()) {
            x = 0;
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image,x,y,null);
        if(x <= 0) {
            canvas.drawBitmap(image, x + image.getWidth(),y,null);
        }

    }

    public void setVector(int dx) {
        this.dx = dx;
    }
}
