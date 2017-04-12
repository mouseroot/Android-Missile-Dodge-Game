package com.example.mouseroot.mygame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.provider.Settings;


public class Player extends GameObject {
    private Bitmap spritesheet;
    private int score;
    private boolean up;
    private boolean playing;
    private Animation anim = new Animation();
    private long startTime;
    private int speed;


    public Player(Bitmap res, int w, int h, int numFrames) {
        spritesheet = res;
        x = 100;
        y = GamePanel.HEIGHT/2;
        dy = 0;
        score = 0;
        height = h;
        width = w;
        speed = 2;

        Bitmap[] images = new Bitmap[numFrames];

        for(int i=0;i < images.length;i++) {
            images[i] = Bitmap.createBitmap(spritesheet, i*width,0,width,height);
        }
        anim.setFrames(images);
        anim.setDelay(10);
    }

    public void resetPlayer() {
        x= 100;
        dy = 0;
        score = 0;
        speed = 2;
        startTime = System.nanoTime();
    }

    public void setAnimDelay(int delay) {
        this.anim.setDelay(delay);
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean getUp() {
        return this.up;
    }

    public void update() {
        long elapsed = (System.nanoTime() - startTime) / 1000000;
        if(elapsed > 150) {
            score++;
            startTime = System.nanoTime();
        }
        anim.update();
        if(up) {
            dy -= speed-1;
        }
        else {
            dy += speed*1.1;
        }

        if(dy > 14) {
            dy = 14;
        }
        if(dy < -14) {
            dy = -14;
        }

        y += dy;
        if(y >= GamePanel.HEIGHT - 100) {
            y = (int) ((GamePanel.HEIGHT - 100));
            dy = 0;

        }
        if(y <= 25) {
            y = 25;
            dy = 0;
        }
        //dy = 0;



    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(anim.getImage(),x,y,null);
    }

    public int getScore() {
        return score;
    }

    public void setPlaying(boolean play) {
        this.playing = play;
    }

    public void resetScore() {
        score = 0;
    }

    public boolean isPlaying() {
        return this.playing;
    }
}
