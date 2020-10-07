/*******************************************************************************
 //Author: Chad Cromwell
 //Date: February 10th, 2019
 //Assignment: 1
 //Program: MainThread.java
 //Description: Threading for the game, keeps FPS limited to 60fps
 //Methods:
 //setRunning(boolean) method - Accepts a boolean and sets running boolean to variable passed to it
 //run() method - Calculates and keeps game running at target FPS, calls update, and calls draw to draw frame to canvas
 ********************************************************************************/
package com.chadcromwell.tme1;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

//MainThread Class - Threading for the game, keeps FPS limited to 60fps
public class MainThread extends Thread {
    private static final int MAX_FPS = 30; //Target maximum FPS
    private double averageFPS; //Average actual FPS
    private static SurfaceHolder surfaceHolder; //Holds display surface
    private GamePanel gamePanel; //Game panel
    private static boolean running; //To determine if game is running or not
    private static Canvas canvas; //Canvas to hold draw calls
    private long startTime; //Holds start time
    private long timeMillis; //Holds time in milliseconds
    private long waitTime; //Holds the amount of time wait until FPS is reached
    private int frameCount; //Holds current frame number between 0 and MAX_FPS
    private long totalTime; //Holds the total amount of time that has passed
    private long targetTime; //Holds the target time

    //MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) - Constructor, accepts a SurfaceHolder and a GamePanel object, handles frame rate, update, and draw
    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super(); //Call super
        this.surfaceHolder = surfaceHolder; //Capture surfaceHolder
        this.gamePanel = gamePanel; //Capture gamePanel
    }

    //setRunning(boolean) method - Accepts a boolean and sets running boolean to variable passed to it
    public void setRunning(boolean running) {
        this.running = running;
    }

    //run() method - Calculates and keeps game running at target FPS, calls update, and calls draw to draw frame to canvas
    @Override
    public void run() {
        frameCount = 0; //Holds current frame number between 0 and MAX_FPS
        totalTime = 0; //Holds the total amount of time that has passed
        targetTime = 1000/MAX_FPS; //Holds the target time

        //While running
        while (running) {
            startTime = System.nanoTime(); //Get current nanoTime
            canvas = null; //null canvas

            try {
                canvas = this.surfaceHolder.lockCanvas(); //Assign the canvas with lockCanvas so you can start editing the pixels in the surface
                synchronized (surfaceHolder) { //Synchronize on surfaceHolder to enable mutually-exclusive access
                    this.gamePanel.update(); //Update
                     this.gamePanel.draw(canvas); //Draw frame
                }
            }
            catch (Exception e) {
                e.printStackTrace(); //Catch error
            }
            finally {
                //If the canvas is not empty
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas); //Stop drawing pixels
                    }
                    catch (Exception e) {
                        e.printStackTrace(); //Catch error
                    }
                }
            }
            timeMillis = (System.nanoTime() - startTime) / 1000000; //Get time passed in milliseconds
            waitTime = targetTime - timeMillis; //Get time until target FPS is reached

            try {
                //If there is time to wait
                if (waitTime > 0) {
                    this.sleep(waitTime); //Sleep the thread until target FPS is reached
                }
            }
            catch (Exception e) {
                e.printStackTrace(); //Catch error
            }
            totalTime += System.nanoTime() - startTime; //Add the frame time to totalTime
            frameCount++; //Increment frameCount

            //If it has drawn MAX_FPS frames
            if (frameCount == MAX_FPS) {
                averageFPS = 1000 / ((totalTime / frameCount) / 1000000); //Calculate the average FPS
                frameCount = 0; //Reset frameCount to 0 for next iteration
                totalTime = 0; //Reset totalTime to 0 for next iteration
            }
        }
    }
}