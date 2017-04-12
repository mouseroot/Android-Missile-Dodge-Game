package com.example.mouseroot.mygame;

/**
 * Created by mouseroot on 4/10/17.
 */
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


public class SmokePuff extends GameObject {
    public int r;

    public SmokePuff(int x, int y) {
        r = 5;
        super.x = x;
        super.y = y;

    }

    public void update() {
        x-=8;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.argb(150,100,100,100));
        paint.setStyle(Paint.Style.FILL);

        paint.setColor(Color.argb(150,100,100,100));
        canvas.drawCircle(x-r,y-r,r,paint);
        paint.setColor(Color.argb(150,100,100,100));
        canvas.drawCircle(x-r+2,y-r-4,r,paint);
        paint.setColor(Color.argb(50,100,100,100));
        canvas.drawCircle(x-r-4,y-r+4,r,paint);
        paint.setColor(Color.argb(50,100,100,100));
        canvas.drawCircle(x-r+4,y-r+2,r,paint);


    }
}
