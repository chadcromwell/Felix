/*******************************************************************************
 //Author: Chad Cromwell
 //Date: February 10th, 2019
 //Assignment: 1
 //Program: GamePanel.java
 //Description: Drawing surface for the game info such as life left and kill count, it is the foremost panel
 //Methods:
 //surfaceChanged(SurfaceHolder, int format, int width, int height) method - When surface is changed
 //surfaceCreated(SurfaceHolder) method - When surface is created
 //surfaceDestroyed(SurfaceHolder) method - When surface is destroyed
 //onTouchEvent(MotionEvent) method - When display is touched
 //update() method - Updates the game
 //draw(Canvas) method - Accepts a canvas object, calls draw to canvas
 //resetLevel() method - Sets playing to true and initializes new level
 ********************************************************************************/
package com.chadcromwell.tme1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

//GamePanel - Drawing surface
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    public static MainThread thread; //Declare new MainThread object
    public static Player player; //Declare a new Player object
    private Point playerPoint; //Declare a new Point object for player position
    public static Background background; //Declare new Background object
    private static int playerSize = Player.spriteHeight;
    private int playerStartX = (Constants.SCREEN_WIDTH/4);
    public static int playerStartY = (Constants.SCREEN_HEIGHT)-playerSize;
    private Paint textPaint;
    public static boolean startScreenB = false;
    public static boolean playing = true; //TODO this enables whether or not playing
    public static boolean winScreenB = false;
    public static boolean deadScreenB = false;

    //GamePanel(Context context) constructor - Accepts a Context object
    public GamePanel(Context context) {
        super(context); //Call parent class, passing Context context to it

        getHolder().addCallback(this); //add callback interface to this class to detect changes to the surface
        thread = new MainThread(getHolder(), this); //Initialize new MainThread object, passing GamePanel object to it
        player = new Player(); //Initialize Player object
        playerPoint = new Point(player.x, player.y); //Initialize Point object for player position 150x150 halfway between 100 and 200 for Player object
        setFocusable(true); //This view can take focus
    }

    //GamePanel(Context context, AttributeSet attrs) Constructor - Used constructor
    public GamePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this); //add callback interface to this class to detect changes to the surface
        thread = new MainThread(getHolder(), this); //Initialize new MainThread object, passing GamePanel object to it
        player = new Player(); //Initialize Player object
        background = new Background();
        playerPoint = new Point(player.x, player.y); //Initialize Point object for player position 150x150 halfway between 100 and 200 for Player object
        setFocusable(true); //This view can take focus
        GameActivity.addBitmapToMemoryCache("heart", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Heart.png")); //Add heart sprite to LRU Cache
        textPaint = new Paint(); //Paint object for text
        textPaint.setColor(Color.WHITE); //Set text paint to white
        textPaint.setStyle(Paint.Style.FILL); //Set text paint to fill
        textPaint.setTextSize(48); //Set text paint size to 48
        textPaint.setTypeface(Typeface.create("Arial", Typeface.BOLD)); //Set text paint typeface to arial
    }

    //GamePanel Constructor - default
    public GamePanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //surfaceChanged(SurfaceHolder, int format, int width, int height) method - When surface is changed
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //Override needed, no change to implementation
    }

    //surfaceCreated(SurfaceHolder) method - When surface is created
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(getHolder(), this); //Initialize new MainThread object, passing GamePanel object to it
        thread.setRunning(true); //Set the thread to running
        thread.start(); //Start the MainThread object
    }

    //surfaceDestroyed(SurfaceHolder) method - When surface is destroyed
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true; //To determine if it should retry or not
        while(retry) { //While it hasn't joined the thread
            try {
                thread.setRunning(false); //setRunning to false because the game should no longer be running
                thread.join(); //Join the thread
                retry = false; //Set retry to false because it is done
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    //onTouchEvent(MotionEvent) method - When display is touched
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true; //Return true on any touch event
    }

    //update() method - Updates the game
    public void update() {
        playerPoint.set(player.x, player.y); //Update the playerPoint position with touch event typecast to int because there's no such thing as half a pixel
        if (playing) {
            player.update(); //Update player with current point position
            background.update();
        }
        if (winScreenB) {

        }
    }

    //draw(Canvas) method - Accepts a canvas object, calls draw to canvas
    public void draw(Canvas canvas) {
        if (canvas != null) {
            super.draw(canvas); //Call draw from parent class, pass Canvas canvas to it
            if(playing) {
                background.draw(canvas); //Draw the background
                player.draw(canvas); //Draw player to canvas

                //For each HP the player has, draw a heart at the top left of the display
                for (int i = 0; i < player.hp; i++) {
                    canvas.drawBitmap(GameActivity.getBitmapFromCache("heart"), 20 + (i * 80), 20, null);
                }

                canvas.drawText("Ghosts Left: " + Integer.toString(background.ghosts.size()), Constants.SCREEN_WIDTH - 550, 58, textPaint);
                //if(player.attackDrops != 0) {
                    canvas.drawText("Weapon Charge: " + Integer.toString(player.attackDrops/3), Constants.SCREEN_WIDTH - 640, 120, textPaint);
                //}
                //else {
                //    canvas.drawText("Weapon Charge: " + Integer.toString(0), Constants.SCREEN_WIDTH - 640, 120, textPaint);
                //}
            }
        }
    }

    //resetLevel() method - Sets playing to true and initializes new level
    public static void resetLevel() {
        playing = true;
        background = new Background();
    }
}