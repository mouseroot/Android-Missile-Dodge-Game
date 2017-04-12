package com.example.mouseroot.mygame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by mouseroot on 4/10/17.
 */

public class Explosion extends GameObject {
    private Animation anim = new Animation();
    private Bitmap spritesheet;
    private int row;
    private boolean started = false;

    public Explosion(Bitmap res, int x, int y, int w, int h, int numFrames) {
        spritesheet = res;
        super.x = x;
        super.y = y;
        super.width = w;
        super.height = h;

        Bitmap[] images = new Bitmap[numFrames];
        for(int i=0;i < images.length;i++) {
            if(i % 5 == 0 && i > 0) {
                row++;
            }
            images[i] = Bitmap.createBitmap(spritesheet,(i-(5*row))*width,row*height,width,height);


        }
        anim.setFrames(images);
        anim.setDelay(10);
    }

    public void update() {
        if(!anim.hasPlayedOnce() && started) {
            anim.update();
        }
        else {
            super.x = -200;
            super.y = -200;
        }
    }

    public void startAnim() {
        started = true;
    }

    public void resetAnim() {
        started = false;
        anim.setFrame(0);
        anim.resetPlayed();

    }

    public void draw(Canvas canvas) {
        if(!anim.hasPlayedOnce()) {
            canvas.drawBitmap(anim.getImage(), x,y,null);
        }
    }
}
