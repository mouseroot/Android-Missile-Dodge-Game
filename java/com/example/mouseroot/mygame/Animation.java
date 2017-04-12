package com.example.mouseroot.mygame;


import android.graphics.Bitmap;

class Animation {
    private Bitmap[] frames;
    private int currentFrame;
    private long startTime;
    private long delay;
    private boolean playedOnce;

    public void setFrames(Bitmap[] frames) {
        this.frames = frames;
        currentFrame = 0;
        startTime = System.nanoTime();
    }

    public void setDelay(long delay) {

        this.delay = delay;
    }

    public void setFrame(int i) {

        this.currentFrame = i;
    }

    public void resetPlayed() {
        this.playedOnce = false;
    }

    public void update() {
        long elapsed = (System.nanoTime() - startTime) / 1000000;
        if(elapsed > delay) {
            currentFrame++;
            startTime = System.nanoTime();
        }
        if(currentFrame == frames.length) {
            currentFrame = 0;
            playedOnce = true;
        }
    }

    public Bitmap getImage() {

        return frames[currentFrame];
    }

    public int getFrame() {

        return currentFrame;
    }

    public boolean hasPlayedOnce() {

        return this.playedOnce;
    }
}
