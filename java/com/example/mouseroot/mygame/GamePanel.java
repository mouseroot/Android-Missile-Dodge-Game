package com.example.mouseroot.mygame;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mouseroot on 4/9/17.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private Random rand = new Random();
    private Background bg;
    private Player player;
    private ArrayList<SmokePuff> smoke;
    private ArrayList<Missle> misiles;
    private int lastScore = 0;
    private int highScore = 0;

    private long smokeStartTime;
    private long missileStartTime;

    public static final int WIDTH = 856;
    public static final int HEIGHT = 480;

    private Bitmap misileBmp;
    private Bitmap bgBmp;
    private Bitmap heliBmp;
    private Bitmap explodeBmp;
    private Bitmap pauseBmp;
    private Bitmap titleBmp;

    private boolean hasStarted = false;
    private boolean dead = false;

    private Explosion explosion;
    private int prog = 0;
    private boolean paused;


    private SharedPreferences settings;
    private SharedPreferences.Editor edit;

    private Typeface typeFace;

    public GamePanel(Context cx, SharedPreferences settings) {
        super(cx);
        this.settings = settings;

        edit = settings.edit();

        getHolder().addCallback(this);
        setFocusable(true);

        misileBmp = BitmapFactory.decodeResource(getResources(),R.drawable.missile);
        bgBmp = BitmapFactory.decodeResource(getResources(), R.drawable.grassbg1);
        heliBmp = BitmapFactory.decodeResource(getResources(),R.drawable.helicopter);
        explodeBmp = BitmapFactory.decodeResource(getResources(),R.drawable.explosion);
        pauseBmp = BitmapFactory.decodeResource(getResources(),R.drawable.pause);
        titleBmp = BitmapFactory.decodeResource(getResources(),R.drawable.title);

        typeFace = Typeface.createFromAsset(getContext().getAssets(),"fonts/armalite_rifle.ttf");


    }

    public void saveScore(int newScore) {
        edit.putInt("score", newScore);
        int hs = settings.getInt("highScore",0);
        System.out.println("High Score: " + hs);
        if(newScore > hs) {
            edit.putInt("highScore",newScore);
            System.out.println("New Highscore: " + hs);
        }
        edit.commit();
        highScore = hs;

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        int hscore = settings.getInt("highScore",0);
        //edit.putInt("highScore",0);

        highScore = hscore;
        //Setup Background
        bg = new Background(bgBmp);
        bg.setVector(-5);

        //Setup Player
        player = new Player(heliBmp,65,25,3);

        //Setup Smoke
        smoke = new ArrayList<SmokePuff>();
        misiles = new ArrayList<Missle>();
        smokeStartTime = System.nanoTime();
        missileStartTime = System.nanoTime();

        explosion = new Explosion(explodeBmp,-200,-200,100,100,25);


        //Start Gameloop
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while(retry) {
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
                thread = null;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Keydown
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            if(!player.isPlaying()) {
                if(!hasStarted) {
                    hasStarted = true;
                }
                player.setPlaying(true);
                dead = false;

            }
            else {
                player.setUp(true);
                int _x = WIDTH - 150;
                int _y = HEIGHT - 100;
                Rect pauseRect = new Rect(_x,_y,_x+64,_y+64);
                if(pauseRect.contains((int)event.getX(), (int)event.getY())) {
                    paused = !paused;
                }
            }
            return true;
        }
        //Keyup
        if(event.getAction() == MotionEvent.ACTION_UP) {
            player.setUp(false);
            return true;
        }
        return super.onTouchEvent(event);
    }


    public void update() {
        if(player.isPlaying() && !paused) {

            bg.update();
            player.update();
            if(player.getUp() == true) {
                player.setAnimDelay(5);
            }
            else {
                player.setAnimDelay(50);
            }
            if(player.getScore() > 100) {
                prog = 1;
            }
            else if(player.getScore() > 300) {
                prog = 2;
            }


            //Missiles Update
            long misileElapsed = (System.nanoTime() - missileStartTime) / 1000000;
            if(misileElapsed > (2000 - player.getScore() / 4)) {
                int _y = (int) (rand.nextDouble()*HEIGHT-100);
                Missle newMissile = new Missle(misileBmp,WIDTH+10,_y,45,15,player.getScore(),13);

                //Progression 1 - 100 meters
                if(prog == 1) {
                    int homing = rand.nextInt(4);
                    if(homing == 2) {
                        //Set the Y to the player
                        System.out.println("Homing Missle Progression 1");
                        newMissile.setY(player.getY());
                    }
                    newMissile.setSpeed(20);
                }
                //Progression 2 - 300 meters
                else if(prog == 2) {
                    int home = rand.nextInt(4);
                    if(home == 2 || home == 3) {
                        System.out.println("Homeing Missle Progression 2");
                        newMissile.setY(player.getY());
                    }
                    newMissile.setSpeed(32);
                }
                misiles.add(newMissile);
                missileStartTime = System.nanoTime();

            }
            for(int i=0;i < misiles.size();i++) {

                misiles.get(i).update();
                if(collision(misiles.get(i),player)) {
                    dead = true;
                    misiles.remove(i);
                    player.setPlaying(false);
                    lastScore = player.getScore();
                    saveScore(lastScore);
                    prog = 0;
                    player.resetPlayer();
                    explosion.resetAnim();
                    explosion.setX(player.getX());
                    explosion.setY(player.getY());
                    explosion.startAnim();
                    break;
                }
                if(misiles.get(i).getX() < -100) {
                    misiles.remove(i);
                    break;
                }
            }

            //SmokePuff Update
            long elapsed = (System.nanoTime() - smokeStartTime) / 1000000;
            if(elapsed > 120) {
                if(player.getUp() == true) {
                    smoke.add(new SmokePuff(player.getX(), player.getY() + 10));
                }
                smokeStartTime = System.nanoTime();
            }
            for(int i=0; i < smoke.size();i++) {
                smoke.get(i).update();
                if(smoke.get(i).getX() < - 10) {
                    smoke.remove(i);
                }
            }


        }
        else {
            if(dead) {
                //Explosion
                explosion.update();
            }
            else {

            }
        }
    }

    private boolean collision(GameObject a, GameObject b) {
        if(Rect.intersects(a.getRectangle(),b.getRectangle())) {
            return true;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {

        super.draw(canvas);

        if(canvas != null) {

            Paint textPaint =new Paint();
            textPaint.setTextSize(25);
            textPaint.setColor(Color.BLACK);
            textPaint.setTypeface(typeFace);

            //Draw BG
            if(!hasStarted) {
                canvas.drawBitmap(titleBmp,0,0,null);
            }
            else {
                bg.draw(canvas);
            }

            //Not Playing
            if(!player.isPlaying()) {
                if(!hasStarted) {
                    textPaint.setColor(Color.BLACK);
                    textPaint.setTextSize(45);
                    canvas.drawText("Tap to Begin", (GamePanel.WIDTH / 2) / 2, GamePanel.HEIGHT / 2, textPaint);
                    textPaint.setTextSize(28);
                    canvas.drawText("High Score: " +highScore,(WIDTH / 4) + 35, (GamePanel.HEIGHT / 2) + 30, textPaint);
                }
                else {
                    textPaint.setColor(Color.BLACK);
                    textPaint.setTextSize(40);
                    canvas.drawText("You scored... " + lastScore,(GamePanel.WIDTH / 2) / 2, 70, textPaint);

                    textPaint.setTextSize(28);
                    canvas.drawText("High Score: " +highScore,(GamePanel.WIDTH / 2) / 2, 110, textPaint);

                    textPaint.setColor(Color.RED);
                    textPaint.setTextSize(30);
                    canvas.drawText("Tap to try again",((GamePanel.WIDTH / 2) / 2) + 10,190,textPaint);

                }
            }
            else {
                textPaint.setColor(Color.WHITE);
                textPaint.setTextSize(25);
                //canvas.drawText("Score: " + player.getScore(),25,25,textPaint);
                if(prog == 0) {
                    canvas.drawText("Level Progress: " + player.getScore() + "/100",25,25,textPaint);
                }
                else if(prog == 1) {
                    textPaint.setColor(Color.YELLOW);
                    canvas.drawText("Level Progress: " + player.getScore() + "/300",25,25,textPaint);
                }
            }
            //Player
            if(!dead) {

                if(paused) {
                    textPaint.setColor(Color.WHITE);
                    textPaint.setTextSize(25);
                    canvas.drawText("Paused",WIDTH - 165,HEIGHT - 100,textPaint);
                }
                if(player.isPlaying()) {
                    player.draw(canvas);
                }


                //Draw a pause button
                if(hasStarted) {
                    canvas.drawBitmap(pauseBmp, WIDTH - 150, HEIGHT - 100, null);
                }


                //Smoke
                for(SmokePuff puff: smoke) {
                    puff.draw(canvas);
                }
            }

            //Missiles
            for(Missle missile: misiles) {
                missile.draw(canvas);
            }

            if(!player.isPlaying() && dead) {
                explosion.draw(canvas);
            }

        }

    }


}
