package com.example.zane.catchball;

import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class main extends AppCompatActivity {

    private TextView scoreLabel;
    private TextView startLabel;
    private ImageView pacman;   //box
    private ImageView dot;      //orange
    private ImageView blinky;   //black
    private ImageView cherry;   //pink


    //size
    private int frameHeight;
    private int pacmanSize;
    private int screenWidth;
    private int screenHeight;

    //position
    private int pacmanY;
    private int dotX;
    private int dotY;
    private int cherryX;
    private int cherryY;
    private int blinkyX;
    private int blinkyY;


    //score
    private int score;


    //initialize class
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    //status check
    private boolean action_flag = false;
    private boolean start_flag = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        startLabel = (TextView) findViewById(R.id.startLabel);
        pacman = (ImageView) findViewById(R.id.pacman);
        dot = (ImageView) findViewById(R.id.dot);
        blinky = (ImageView) findViewById(R.id.blinky);
        cherry = (ImageView) findViewById(R.id.cherry);


        //get screen size
        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        //move out of screen
        dot.setX(-80);
        dot.setY(-80);
        cherry.setX(-80);
        cherry.setY(-80);
        blinky.setX(-80);
        blinky.setY(-80);

        scoreLabel.setText("Score : 0");

        pacmanY = 500;

    }


    public void changePos() {

        hitCheck();


        //dot
        dotX -= 12;
        if(dotX < 0) {
            dotX = screenWidth + 20;
            dotY = (int) Math.floor(Math.random()*(frameHeight - dot.getHeight()));
        }
        dot.setX(dotX);
        dot.setY(dotY);

        //blinky
        blinkyX -= 16;
        if(blinkyX < 0) {
            blinkyX = screenWidth + 10;
            blinkyY = (int) Math.floor(Math.random()*(frameHeight - blinky.getHeight()));
        }
        blinky.setX(blinkyX);
        blinky.setY(blinkyY);


        //cherry
        cherryX -= 20;
        if(cherryX < 0) {
            cherryX = screenWidth + 3000;
            cherryY = (int) Math.floor(Math.random()*(frameHeight - cherry.getHeight()));
        }
        cherry.setX(cherryX);
        cherry.setY(cherryY);



        //move box
        if (action_flag == true) {
            //touch
            pacmanY -= 20;
        } else {
            //relase
            pacmanY += 20;
        }

        //check pac position
        if(pacmanY < 0) {
            pacmanY = 0;
        }

        if(pacmanY > frameHeight - pacmanSize){
            pacmanY = frameHeight - pacmanSize;
        }

        pacman.setY(pacmanY);

        scoreLabel.setText("Score : " + score);

    }


    public void hitCheck() {

        //check if center of dot hits pacman

        //dot
        int dotCenterX = dotX + dot.getWidth() / 2;
        int dotCenterY = dotY + dot.getHeight() / 2;

        if(0 <= dotCenterX && dotCenterX <= pacmanSize && pacmanY <= dotCenterY && dotCenterY <= pacmanY + pacmanSize) {

            score += 10;
            dotX = -10;

        }

        //Cherry
        int cherryCenterX = cherryX + cherry.getWidth() / 2;
        int cherryCenterY = cherryY + cherry.getHeight() / 2;

        if(0 <= cherryCenterX && cherryCenterX <= pacmanSize && pacmanY <= cherryCenterY && cherryCenterY <= pacmanY + pacmanSize) {

            score += 30;
            cherryX = -10;
        }

        //blinky
        int blinkyCenterX = blinkyX + blinky.getWidth() / 2;
        int blinkyCenterY = blinkyY + blinky.getHeight() / 2;

        if(0 <= blinkyCenterX && blinkyCenterX <= pacmanSize && pacmanY <= blinkyCenterY && blinkyCenterY <= pacmanY + pacmanSize) {
            //gameover
            timer.cancel();
            timer = null;

            //show result
            Intent intent = new Intent(getApplicationContext(), result.class);
            intent.putExtra("SCORE", score);
            startActivity(intent);
        }
    }






    public boolean onTouchEvent(MotionEvent me) {

        if(start_flag == false) {

            start_flag = true;

            FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
            frameHeight = frame.getHeight();

            pacmanY = (int) pacman.getY();
            pacmanSize = pacman.getHeight();
            startLabel.setVisibility(View.GONE);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            },0, 20);

        } else {
            if(me.getAction() == MotionEvent.ACTION_DOWN) {
                action_flag = true;
            } else if(me.getAction() == MotionEvent.ACTION_UP) {
                action_flag = false;
            }
        }
        return true;
    }

    //disable back button
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN) {
            switch(event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
