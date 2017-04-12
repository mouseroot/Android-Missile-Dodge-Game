package com.example.mouseroot.mygame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

import java.util.Random;

/**
 * Created by mouseroot on 4/10/17.
 */

public class Missle extends GameObject {

    private int score;
    private int speed;
    private Random rand = new Random();
    private Animation anim = new Animation();
    private Bitmap spritesheet;

    public Missle(Bitmap res, int x, int y, int w, int h, int s, int numFrames) {
        spritesheet = res;
        super.x = x;
        super.y = y;

        super.width = w;
        super.height = h;

        score = s;

        speed = 15;
        if(speed > 40) {
            speed = 40;
        }

        Bitmap[] image = new Bitmap[numFrames];

        for(int i=0;i < image.length;i++) {
            image[i] = Bitmap.createBitmap(spritesheet, 0, i*height,width,height);

        }
        anim.setFrames(image);
        anim.setDelay(100 - speed);

    }

    public void setSpeed(int speed) {
        if(speed > 40) {
            speed = 40;
        }
        this.speed = speed;
    }

    public void update() {
        x-= speed;
        anim.update();
    }

    public void draw(Canvas canvas) {
        try {
            canvas.drawBitmap(anim.getImage(),x,y,null);
        }catch (Exception e) {

        }

    }

    @Override
    public int getWidth() {
        return width-10;
    }
}
