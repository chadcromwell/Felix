/*******************************************************************************
 //Author: Chad Cromwell
 //Date: February 10th, 2019
 //Assignment: 1
 //Program: GameActivity.java
 //Description: Game screen, where playing takes place
 //Methods:
 //onCreate(Bundle savedInstanceState) method - Sets view, creates intents for other activities, initializes memory cache, display metrics, vibrator, thumbstick, and buttons
 //vibe(Vibrator vibe, int ms) method - Vibrator method, accepts Vibrator object and time in ms for how long vibration should last, then calls the vibration
 //addBitmapToMemoryCache(String key, Bitmap bitmap) method - Adds a bitmap to the cache with key
 //getBitmapFromCache(String key) method - Accepts key and returns the bitmap from the LRU Cache with corresponding key
 ********************************************************************************/
package com.chadcromwell.tme1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


//GameActivity, the main game activity
public class GameActivity extends Activity implements View.OnClickListener {
    public static Context context; //Declare context
    public static int maxMemory; //Declare max memory
    public static int cacheSize; //Declare cache size
    private static LruCache<String, Bitmap> mMemoryCache; //Declare LRU Cache
    private boolean leftThumbStick = true; //Initialize thumbstick to true (can be touched)
    public int lastDir = 1; //Initialize lastDir to 1 (right)
    float eventX; //Touch event x pos
    float eventY; //Touch event y pos
    float thumbStickX; //Thumb stick x pos
    float thumbStickY; //Thumb stick y pos
    int thumbStickRadius; //Thumb stick radius
    static Button thumbStick; //Thumb stick button
    static Button jumpButton; //Jump button
    static Button attackButton; //Attack button
    static Activity activity; //Declare Activity
    public static Intent intent; //Declare intent
    public static Intent endIntent; //Declare end activity intent
    public static Intent deadIntent; //Declare dead activity intent


    //onCreate(Bundle savedInstanceState) method - Sets view, creates intents for other activities, initializes memory cache, display metrics, vibrator, thumbstick, and buttons
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = getApplicationContext();
        activity = this;
        endIntent = new Intent(context, EndActivity.class);
        endIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        deadIntent = new Intent(context, DeadActivity.class);
        deadIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Set current window to fullscreen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); //Set window to have no bar at top

        //Set up the LRU Cache for caching bitmaps into memory for fast access
        maxMemory = (int)(Runtime.getRuntime().maxMemory()/1024);
        cacheSize = maxMemory;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount()/1024;
            }
            @Override
            protected Bitmap create(String key) {
                return super.create(key);
            }
        };

        //Get and store screen dimensions
        DisplayMetrics dm = new DisplayMetrics(); //Initialize DisplayMetrics object
        getWindowManager().getDefaultDisplay().getRealMetrics(dm); //Assign actual display metrics to dm object
        Constants.SCREEN_WIDTH = dm.widthPixels; //Assign the width of the display in pixels to SCREEN_WIDTH in Constants class
        Constants.SCREEN_HEIGHT = dm.heightPixels; //Assign the height of the display in pixels to SCREEN_HEIGHT in Constants class

        setContentView(R.layout.activity_game);
        final View view = findViewById(R.id.button_layout);

        final Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE); //Create Vibrator object
        thumbStick = view.findViewById(R.id.thumb_stick); //Create Button object with thumb_stick Button from activity_main.xml
        jumpButton = view.findViewById(R.id.jump_button); //Create Button object with jump_button Button from activity_main.xml
        attackButton = view.findViewById(R.id.attack_button); //Create Button object with attack_button Button from activity_main.xml

        //Set up and handle touch events for thumbStick
        thumbStick.setOnTouchListener(new Button.OnTouchListener() { //Create and assign OnTouchListener to thumbStick Button
            //When a touch takes place
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                eventX = v.getLeft() + event.getX(); //Capture and assign the event xPos relative to the left side of the View - gets global coords
                eventY = v.getTop() + event.getY(); //Capture and assign the event yPos relative to the top side of the View - gets global coords
                thumbStickX = thumbStick.getX() + (thumbStick.getWidth() / 2); //Assign the center xPos of the thumbStick Button to thumbStickX
                thumbStickY = thumbStick.getY() + (thumbStick.getHeight() / 2); //Assign the center yPosof the thumbStick Button to thumbStickY
                thumbStickRadius = thumbStick.getWidth() / 2; //Assign the radius of the thumbStick Button to thumbStickRadius

                //Instead of using a switch, let's use if statements
                //If the event action is a tap
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    thumbStick.setPressed(true); //Change button state to pressed. This will light the button up according to @drawable/button.xml
                    leftThumbStick = false; //lefThumbStick cannot be pressed again (set to true upon release). Prevents if(!leftThumbStick) logic below from executing constantly while finger is pressed down

                    //If touch is on the right side of the thumb stick
                    if (eventX > thumbStickX) {
                        GamePanel.player.moving = true;
                        GamePanel.player.direction = 1; //Update player direction to 1 (going right)
                        if (GamePanel.player.state != "WalkRight") { //If player state isn't currently WalkRight
                            GamePanel.player.loadWalk(); //Load walk right sprite
                            GamePanel.player.previousState = "WalkRight"; //Update previous state to WalkRight
                        }
                    }

                    //If touch is on the left side of the thumb stick, follows same logic as above but for left
                    if (eventX < thumbStickX) {
                        GamePanel.player.moving = true;
                        GamePanel.player.direction = -1; //Update player direction to -1 (going left)
                        if (GamePanel.player.state != "WalkLeft") {
                            GamePanel.player.loadWalkLeft();
                            GamePanel.player.previousState = "WalkLeft";
                        }
                    }

                    GamePanel.player.doneDirectionChange = false; //Not finished with changing the direction of the sprite

                    return true;
                }
                //If thumbstick is released
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (eventX > thumbStickX) { //If on the right of the thumbstick
                        GamePanel.player.moving = false; //Player is not being moved
                        GamePanel.player.direction = 1; //Player is going right
                        if (GamePanel.player.state != "IdleRight") { //If not idling right
                            GamePanel.player.loadIdle(); //Load right idle sprite
                            GamePanel.player.previousState = "IdleRight"; //update previousSate
                        }
                    }
                    if (eventX < thumbStickX) { //If on the left of the thumbstick, same logic as above
                        GamePanel.player.moving = false;
                        GamePanel.player.direction = -1;
                        if (GamePanel.player.state != "IdleLeft") {
                            GamePanel.player.loadIdleLeft();
                            GamePanel.player.previousState = "IdleLeft";
                        }
                    }
                    thumbStick.setPressed(false); //Thumbstick is no longer pressed, so color will change
                    GamePanel.player.doneDirectionChange = false; //Not finished with changing the direction of the sprite
                    leftThumbStick = true; //Can touch thumbstick again
                    return true;
                }
                //If finger is moved while on thumbstick
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    lastDir = GamePanel.player.direction; //Update the last direction
                    if (((eventX - thumbStickX) * (eventX - thumbStickX)) + ((eventY - thumbStickY) * (eventY - thumbStickY)) >= (thumbStickRadius * thumbStickRadius)) { //If finger is within thumbstick
                        if (eventX > thumbStickX) { //If on the right side
                            GamePanel.player.direction = 1; //Direction to the right
                            if (GamePanel.player.state != "IdleRight") { //If not idling right
                                GamePanel.player.loadIdle(); //Load right idle sprite
                                GamePanel.player.previousState = "IdleRight"; //Update previous state
                            }
                        }
                        if (eventX < thumbStickX) { //If on left side of thumbstick, same logic as above but for left
                            GamePanel.player.direction = -1;
                            if (GamePanel.player.state != "IdleLeft") {
                                GamePanel.player.loadIdleLeft();
                                GamePanel.player.previousState = "IdleLeft";
                            }
                        }
                        thumbStick.setPressed(false); //Thumbstick is no longer pressed, so color will change
                        //Perform thumb stick release code
                        GamePanel.player.doneDirectionChange = false; //Not finished with changing the direction of the sprite
                        GamePanel.player.moving = false; //Player no longer moving
                        leftThumbStick = true; //Can touch thumbstick again
                        return false;
                    }
                    //If holding thumbstick down
                    if (!leftThumbStick) {
                        if (eventX > thumbStickX) { //If touching on right
                            GamePanel.player.moving = true; //Player is moving
                            GamePanel.player.direction = 1; //Direction is right
                            if (GamePanel.player.state != "WalkRight") { //If state isn't walk right
                                GamePanel.player.captureSpeed = true; //Start capturing speed
                                GamePanel.player.loadWalk(); //Load walk sprite
                                GamePanel.player.previousState = "WalkRight"; //Update previous state
                            }
                        }
                        if (eventX < thumbStickX) { //If touching on left, same logic as above but for left
                            GamePanel.player.moving = true;
                            GamePanel.player.direction = -1;
                            if (GamePanel.player.state != "WalkLeft") {
                                GamePanel.player.captureSpeed = true;
                                GamePanel.player.loadWalkLeft();
                                GamePanel.player.previousState = "WalkLeft";
                            }
                        }
                    }
                }
                return false;
            }
        });

        //Set up and handle touch events for jumpButton
        jumpButton.setOnTouchListener(new Button.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float eventX = v.getLeft() + event.getX(); //Get x in view
                float eventY = v.getTop() + event.getY(); //Get y in view
                float jumpButtonX = jumpButton.getX() + (jumpButton.getWidth() / 2); //Assign centre of button x
                float jumpButtonY = jumpButton.getY() + (jumpButton.getHeight() / 2); //Assign centre of button y
                int jumpButtonRadius = jumpButton.getWidth() / 2; //Assign radius of button

                //If tapping
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    vibe(vibrator, 5); //Vibrate 5ms
                    jumpButton.setPressed(true); //Change button color

                    //Perform jump button touch code
                    if (!GamePanel.player.jumping) {
                        if (GamePanel.player.direction == 1) { //If going right
                            GamePanel.player.loadJump(); //Load jump sprite
                        }
                        if (GamePanel.player.direction == -1) { //If going left
                            GamePanel.player.loadJumpLeft(); //Load left jump sprite
                        }
                        GamePanel.player.jumping = true; //Player is jumping
                    }
                    return true;
                }

                //If released
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    jumpButton.setPressed(false); //Change button color
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) { //If finger is moved on button
                    if (((eventX - jumpButtonX) * (eventX - jumpButtonX)) + ((eventY - jumpButtonY) * (eventY - jumpButtonY)) >= (jumpButtonRadius * jumpButtonRadius)) { //If finger slides off button
                        jumpButton.setPressed(false); //Change button colour
                        return false;
                    }
                }
                return false;
            }
        });

        //Set up and handle touch events for attackButton, same logic as above
        attackButton.setOnTouchListener(new Button.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float eventX = v.getLeft() + event.getX();
                float eventY = v.getTop() + event.getY();
                float attackButtonX = attackButton.getX() + (attackButton.getWidth() / 2);
                float attackButtonY = attackButton.getY() + (attackButton.getHeight() / 2);
                int attackButtonRadiua = attackButton.getWidth() / 2;

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    vibe(vibrator, 5);
                    attackButton.setPressed(true);
                    GamePanel.player.attacking = true;
                    //Perform jump button touch code
                    if (GamePanel.player.direction == 1) {
                        GamePanel.player.loadAttack();
                        return true;
                    }
                    if (GamePanel.player.direction == -1) {
                        GamePanel.player.loadAttackLeft();
                        return true;
                    }
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    attackButton.setPressed(false);
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (((eventX - attackButtonX) * (eventX - attackButtonX)) + ((eventY - attackButtonY) * (eventY - attackButtonY)) >= (attackButtonRadiua * attackButtonRadiua)) {
                        attackButton.setPressed(false);
                        return false;
                    }
                }
                return false;
            }
        });


    }

    //vibe(Vibrator vibe, int ms) method - Vibrator method, accepts Vibrator object and time in ms for how long vibration should last, then calls the vibration
    public void vibe(Vibrator vibe, int ms) {
        vibe.vibrate(ms);
    }

    public void onClick(View v) {
        //Initialized due to implementing listener, only used for debugging on AVD
    }

    //addBitmapToMemoryCache(String key, Bitmap bitmap) method - Adds a bitmap to the cache with key
    public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if(getBitmapFromCache(key) == null) { //If key doesn't already exist
            mMemoryCache.put(key, bitmap); //Add it to the cache
        }
        //Can add else to handle error if needed later
    }

    //getBitmapFromCache(String key) method - Accepts key and returns the bitmap from the LRU Cache with corresponding key
    public static Bitmap getBitmapFromCache(String key) {
        return mMemoryCache.get(key);
    }
}