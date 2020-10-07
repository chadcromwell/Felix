/*******************************************************************************
 //Author: Chad Cromwell
 //Date: February 10th, 2019
 //Assignment: 1
 //Program: Ghost.java
 //Description: Represents a ghost
 //Methods:
 //Ghost(int x, int y, int minX, int maxX, String type, int activationX, boolean activated) Constructor - Accepts x y pos, minX maxX for how far it walks, ghost type, where it activates, whether it is activated or not
 //openEyes() method - Opens sprite eyes
 //squint() method - Squints sprite eyes
 //smile() method - Set sprite mouth to smile
 //frown() method - Set sprite mouth to frown
 //hurt() method - Set sprite to hurt
 //activate(int x) method - Accepts x position and activates ghost if background x passes activation x
 //Update() Method - Handles player positioning, physics, and moving of Destination Rectangle
 //draw(Canvas canvas) method - Accepts a Canvas object and draws bitmaps to the Canvas
 //updateRectangle(String s) method - Accepts a string for the state and resets the Source and Destination Rectangles to first frame. Acceptable input ex. "idle" "walk"
 //flip(Canvas canvas) method - Flips the canvas horizontally at the midpoint of the sprite
 //unFlip(Canvas canvas) method - Restores the canvas back to original orientation
 //loadWalk() method - Loads walk bitmap
 //loadAttack() method - Loads attack bitmap
 //loadHurt() method - Loads hurt bitmap
 //loadWalkLeft() method - Loads flipped walk bitmap
 //loadAttackLeft() method - Loads flipped attack bitmap
 //loadHurtLeft() method - Loads flipped hurt bitmap
 //loadDie() method - Loads die bitmap
 //loadDieLeft() method - Loads flipped die bitmap
 //animate() method - Animates the player
 //animator(DisplayFrame displayFrame, int columns, int height, Rect sr, String part, String s) method - Handles moving and updating frames and SRs
 //incrementSRTop(String part, String st) method - Increments the SR top position down one row
 //resetRow(s) method - Accepts a String for the sprite part and resets the Source Rectangle top position to 0
 //animateState(String s) method - Animates the corresponding state by calling animator, resetting frames, incrementing SR, and handling tickers
 //incrementSR(s) method - Accepts a string for state and moves the Source Rectangle to the right over next frame
 //updateSR(part, state) method - Accepts a sprite part and the state in which to update the source rectangle bottom position
 //resetFrame(String part, String st) method - Accepts a sprite part and the state and resets the source rect to first index frame
 //resetWalk method() - Resets all the walk frames, same logic as resetIdle() method
 //resetAttack method() - Resets all the attack frames, same logic as resetIdle() method
 //resetHurt method() - Resets all the hurt frames, same logic as resetIdle() method
 //resetDie method() - Resets all the die frames, same logic as resetIdle() method
 //setSpriteSize() method - Set the size of the sprite
 //getSpriteSize() method - gets the sprite size x and y as a Point object, not used anymore
 ********************************************************************************/
package com.chadcromwell.tme1;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

//Ghost Class - Represents a ghost
public class Ghost implements GameObject {

    private ArrayList<BitmapTuple> bitmapList = new ArrayList<>();

    //Player variables
    public static int spriteWidth; //Width of sprite
    public static int spriteHeight; //Height of sprite
    public int acceleration = 1; //Player acceleration speed
    private int normalSpeed = 5;
    private int aggressiveSpeed = 15;
    public int maxSpeed; //Max player speed
    public int hp = 2; //Player hp
    private int xSpeed; //Speed in x direction
    private int ySpeed; //Speed in y direction
    public boolean moving = false; //If the player is moving or not
    public boolean attacking = false; //If the player is attacking
    public boolean hurt = false; //If ghost is hurt
    private boolean frown = false; //If ghost should frown
    public int x; //x position, set in GamePanel
    public int y; //y position, set in GamePanel
    public int direction = 1; //Player direction 1 going right, -1 going left
    public String state = "WalkRight"; //Defines player state, Idle, Walk, Hurt, Jump, Attack are options
    public String previousState = "WalkRight"; //Keeps track of the previous state to see if a change has taken place
    public boolean alive = true; //If ghost is alive
    public boolean canDie = true; //If ghost can die
    public String drop; //What item the ghost is carrying to drop ex "attack" "health" "speed" or "none"

    //For animation
    private int currentFrame = 0; //Keep track of current frame
    private int swap = 1; //When to swap

    //Bitmap sizes
    //Walk, self explanatory
    private int walkBodyWidth;
    private int walkBodyHeight;
    private int walkEyesWidth;
    private int walkEyesHeight;
    private int walkMouthWidth;
    private int walkMouthHeight;

    //Hurt, self explanatory
    private int hurtWidth;
    private int hurtHeight;

    //Attack, self explanatory
    private int attackWidth;
    private int attackHeight;

    //Die 1, self explanatory
    private int dieWidth;
    private int dieHeight;

    //Variable part sizes, self explanatory
    private int walkEyesOpenWidth;
    private int walkEyesOpenHeight;
    private int walkEyesSquintWidth;
    private int walkEyesSquintHeight;
    private int walkMouthSmileWidth;
    private int walkMouthSmileHeight;
    private int walkMouthFrownWidth;
    private int walkMouthFrownHeight;

    //Display frames
    private DisplayFrame bodyDisplayFrame; //Frame to display for body
    private DisplayFrame eyesDisplayFrame; //Frame to display for eyes
    private DisplayFrame mouthDisplayFrame; //Frame to display for mouth

    //Walk sprite sheet
    private int walkFrames = 61; //How many frames the sprite sheet has
    private int walkColumns = 7; //How many columns the sprite sheet has
    private int walkRows = 9; //How many rows the sprite sheet has

    //Hurt sprite sheet
    private int hurtFrames = 12; //How many frames the sprite sheet has, same logic as above
    private int hurtColumns = 3;
    private int hurtRows = 4;

    //Attack sprite sheet
    private int attackFrames = 31; //How many frames the sprite sheet has, same logic as above
    private int attackColumns = 5;
    private int attackRows = 7;

    //Die sprite sheet
    private int dieFrames = 27; //Same logic as above
    private int dieColumns = 5;
    private int dieRows = 6;

    //Frame tickers
    public int bodyFrameTicker; //Same logic as above
    private int eyesFrameTicker;
    private int mouthFrameTicker;

    //Primary Source and Destination Rectangles
    private Rect spriteBodySR;
    private Rect spriteBodyDR;
    private Rect spriteEyesSR;
    private Rect spriteEyesDR;
    private Rect spriteMouthSR;
    private Rect spriteMouthDR;

    //Walk SR and DR Rectangles
    private Rect walkBodySRRect;
    private Rect walkBodyDRRect;
    private Rect walkEyesSRRect;
    private Rect walkEyesDRRect;
    private Rect walkEyesOpenSRRect;
    private Rect walkEyesOpenDRRect;
    private Rect walkEyesSquintSRRect;
    private Rect walkEyesSquintDRRect;
    private Rect walkMouthSRRect;
    private Rect walkMouthDRRect;
    private Rect walkMouthSmileSRRect;
    private Rect walkMouthSmileDRRect;
    private Rect walkMouthFrownSRRect;
    private Rect walkMouthFrownDRRect;

    //Hurt SR and DR Rectangles
    private Rect hurtSRRect;
    private Rect hurtDRRect;

    //Attack SR and DR Rectangles
    private Rect attackSRRect;
    private Rect attackDRRect;

    //Die SR and DR Rectangles
    private Rect dieSRRect;
    private Rect dieDRRect;

    //Walk sprite offsets
    private int walkBodyXOffset = 0;
    private int walkBodyYOffset = 0;
    private int walkEyesOpenXOffset = walkBodyXOffset+109;
    private int walkEyesOpenYOffset = walkBodyYOffset+60;
    private int walkEyesSquintXOffset = walkBodyXOffset+112;
    private int walkEyesSquintYOffset = walkBodyYOffset+60;
    private int walkMouthSmileXOffset = walkBodyXOffset+105;
    private int walkMouthSmileYOffset = walkBodyYOffset+115;
    private int walkMouthFrownXOffset = walkBodyXOffset+0;
    private int walkMouthFrownYOffset = walkBodyYOffset-0;

    //Attack sprite offsets
    private int attackXOffset = -85;
    private int attackYOffset = -84;

    //Hurt sprite offsets
    private int hurtXOffset = -70;
    private int hurtYOffset = 0;

    //Die sprite offsets
    private int dieXOffset = -85;
    private int dieYOffset = -84;

    String type; //Type of ghost, dumb, normal, smart

    private boolean goRight = true; //If ghost should go right
    private boolean stateChange = true; //If the ghost's state has changed
    private boolean canAttack = true; //If the ghost can attack
    private boolean proximity = false; //If the ghost is within proximity of the player, used for determining if it should attack
    public boolean canHurt = true; //If the ghost can be hurt
    boolean activated = false; //If the chasing ghost actively chasing

    private int maxX; //Max x position ghost will walk
    private int minX; //Min x position ghost will walk
    private int lastDir = 1; //Last direction represented in int, 1 = right, 0 = left
    public int scytheWidth; //How far the ghost can reach with it's scythe

    Paint paint; //Used for drawing ghost with alpha
    int alpha = 0; //Initialize alpha to 0
    int activationX; //Declare activation player x position where ghost will start chasing once player reaches that point

    //Ghost(int x, int y, int minX, int maxX, String type, int activationX, boolean activated) Constructor - Accepts x y pos, minX maxX for how far it walks, ghost type, where it activates, whether it is activated or not
    public Ghost(int x, int y, int minX, int maxX, String type, int activationX, boolean activated) {
        paint = new Paint(); //Initialize paint
        this.x = x; //Capture x
        this.y = y; //Capture y
        this.type = type; //Capture type
        scytheWidth = x+170; //Set scytheWidth
        this.activationX = GamePanel.background.x+activationX; //Capture activation x location
        this.maxX = x+maxX; //Capture/set maxX (in map coords)
        this.minX = x-minX; //Capture/set minX (in map coords)

        //If ghost is normal, set it's speed to normalSpeed
        if(type.equals("normal")) {
            maxSpeed = normalSpeed;
        }

        //If ghost is aggressive, set it's speed to aggressiveSpeed
        if(type.equals("aggressive")) {
            maxSpeed = aggressiveSpeed;
        }

        bodyDisplayFrame = new DisplayFrame(0); //Create a new DisplayFrame object and initialize the frame to 0
        eyesDisplayFrame = new DisplayFrame(0); //Create a new DisplayFrame object and initialize the frame to 0
        mouthDisplayFrame = new DisplayFrame(0); //Create a new DisplayFrame object and initialize the frame to 0

        //Load sprite sheets into memory
        //Load walk bitmaps
        GameActivity.addBitmapToMemoryCache("ghostWalkBody", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Ghost/Walk/Body.png"));
        GameActivity.addBitmapToMemoryCache("ghostWalkMouthSmile", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Ghost/Walk/Mouth/Smile.png"));
        GameActivity.addBitmapToMemoryCache("ghostWalkMouthFrown", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Ghost/Walk/Mouth/Frown.png"));
        GameActivity.addBitmapToMemoryCache("ghostWalkEyesOpen", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Ghost/Walk/Eyes/Open.png"));
        GameActivity.addBitmapToMemoryCache("ghostWalkEyesSquint", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Ghost/Walk/Eyes/Squint.png"));

        //Load hurt bitmaps
        GameActivity.addBitmapToMemoryCache("ghostHurt", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Ghost/Hurt.png"));

        //Load attack bitmaps
        GameActivity.addBitmapToMemoryCache("ghostAttack", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Ghost/Attack.png"));

        //Load death bitmaps
        GameActivity.addBitmapToMemoryCache("ghostDie1", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Ghost/Die1.png"));
        GameActivity.addBitmapToMemoryCache("ghostDie2", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Ghost/Die2.png"));
        GameActivity.addBitmapToMemoryCache("ghostDie3", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Ghost/Die3.png"));


        //Get sizes (width and height) of the bitmaps
        walkBodyWidth = GameActivity.getBitmapFromCache("ghostWalkBody").getWidth();
        walkBodyHeight = GameActivity.getBitmapFromCache("ghostWalkBody").getHeight();
        walkEyesOpenWidth = GameActivity.getBitmapFromCache("ghostWalkEyesOpen").getWidth();
        walkEyesOpenHeight = GameActivity.getBitmapFromCache("ghostWalkEyesOpen").getHeight();
        walkEyesSquintWidth = GameActivity.getBitmapFromCache("ghostWalkEyesSquint").getWidth();
        walkEyesSquintHeight = GameActivity.getBitmapFromCache("ghostWalkEyesSquint").getHeight();
        walkMouthSmileWidth = GameActivity.getBitmapFromCache("ghostWalkMouthSmile").getWidth();
        walkMouthSmileHeight = GameActivity.getBitmapFromCache("ghostWalkMouthSmile").getHeight();
        walkMouthFrownWidth = GameActivity.getBitmapFromCache("ghostWalkMouthFrown").getWidth();
        walkMouthFrownHeight = GameActivity.getBitmapFromCache("ghostWalkMouthFrown").getHeight();

        hurtWidth = GameActivity.getBitmapFromCache("ghostHurt").getWidth();
        hurtHeight = GameActivity.getBitmapFromCache("ghostHurt").getHeight();

        attackWidth = GameActivity.getBitmapFromCache("ghostAttack").getWidth();
        attackHeight = GameActivity.getBitmapFromCache("ghostAttack").getHeight();

        dieWidth = GameActivity.getBitmapFromCache("ghostDie1").getWidth();
        dieHeight = GameActivity.getBitmapFromCache("ghostDie1").getHeight();

        //Initialise source and destination Rects for each state
        //Walk state
        walkBodySRRect = new Rect(0, 0, walkBodyWidth/walkColumns, walkBodyHeight/walkRows); //Set source rectangle
        walkBodyDRRect = new Rect(-Background.x+x+walkBodyXOffset, y+walkBodyYOffset, -Background.x+x+walkBodyXOffset+(walkBodyWidth/walkColumns), y+walkBodyYOffset+(walkBodyHeight/walkRows)); //Set destination rectangle

        walkEyesOpenSRRect = new Rect(0, 0, walkEyesOpenWidth/walkColumns, walkEyesOpenHeight/walkRows); //Code below follows logic as above
        walkEyesOpenDRRect = new Rect(-Background.x+x+walkEyesOpenXOffset, y+walkEyesOpenYOffset, -Background.x+x+walkEyesOpenXOffset+(walkEyesOpenWidth/walkColumns), y+walkEyesOpenYOffset+(walkEyesOpenHeight/walkRows));

        walkEyesSquintSRRect = new Rect(0, 0, walkEyesSquintWidth/walkColumns, walkEyesSquintHeight/walkRows);
        walkEyesSquintDRRect = new Rect(-Background.x+x+walkEyesSquintXOffset, y+walkEyesSquintYOffset, -Background.x+x+walkEyesSquintXOffset+(walkEyesSquintWidth/walkColumns), y+walkEyesSquintYOffset+(walkEyesSquintHeight/walkRows));

        walkMouthSmileSRRect = new Rect(0, 0, walkMouthSmileWidth/walkColumns, walkMouthSmileHeight/walkRows);
        walkMouthSmileDRRect = new Rect(-Background.x+x+walkMouthSmileXOffset, y+walkMouthSmileYOffset, -Background.x+x+walkMouthSmileXOffset+(walkMouthSmileWidth/walkColumns), y+walkMouthSmileYOffset+(walkMouthSmileHeight/walkRows));

        walkMouthFrownSRRect = new Rect(0, 0, walkMouthFrownWidth/walkColumns, walkMouthFrownHeight/walkRows);
        walkMouthFrownDRRect = new Rect(-Background.x+x+walkMouthFrownXOffset, y+walkMouthFrownYOffset, -Background.x+x+walkMouthFrownXOffset+(walkMouthFrownWidth/walkColumns), y+walkMouthFrownYOffset+(walkMouthFrownHeight/walkRows));

        //Hurt state, same logic as above
        hurtSRRect = new Rect(0, 0, hurtWidth/hurtColumns, hurtHeight/hurtRows);
        hurtDRRect = new Rect(-Background.x+x+hurtXOffset, y+hurtYOffset, -Background.x+x+hurtXOffset+(hurtWidth/hurtColumns), y+hurtYOffset+(hurtHeight/hurtRows));

        //Attack state, same logic as above
        attackSRRect = new Rect(0, 0, attackWidth/attackColumns, attackHeight/attackRows);
        attackDRRect = new Rect(-Background.x+x+attackXOffset, y+attackYOffset, -Background.x+x+attackXOffset+(attackWidth/attackColumns), y+attackYOffset+(attackHeight/attackRows));

        //Die state, same logic as above
        dieSRRect = new Rect(0, 0, dieWidth/dieColumns, dieHeight/dieRows);
        dieDRRect = new Rect(-Background.x+x+dieXOffset, y+dieYOffset, -Background.x+x+dieXOffset+(dieWidth/dieColumns), y+dieYOffset+(dieHeight/dieRows));

        smile(); //Set to smiling
        openEyes(); //Set to open eyes

        //Current state, initialize as walk;
        spriteBodySR = walkBodySRRect;
        spriteBodyDR = walkBodyDRRect;
        spriteEyesSR = walkEyesSRRect;
        spriteEyesDR = walkEyesDRRect;
        spriteMouthSR = walkMouthSRRect;
        spriteMouthDR = walkMouthDRRect;

        //Set the size of the sprite
        setSpriteSize();
    }

    //openEyes() method - Opens sprite eyes
    public void openEyes() {
        walkEyesSRRect = walkEyesOpenSRRect;
        walkEyesDRRect = walkEyesOpenDRRect;
        walkEyesWidth = walkEyesOpenWidth;
        walkEyesHeight = walkEyesOpenHeight;
    }

    //squint() method - Squints sprite eyes
    public void squint() {
        walkEyesSRRect = walkEyesSquintSRRect;
        walkEyesDRRect = walkEyesSquintDRRect;
        walkEyesWidth = walkEyesSquintWidth;
        walkEyesHeight = walkEyesSquintHeight;
    }

    //smile() method - Set sprite mouth to smile
    public void smile() {
        walkMouthSRRect = walkMouthSmileSRRect;
        walkMouthDRRect = walkMouthSmileDRRect;
        walkMouthWidth = walkMouthSmileWidth;
        walkMouthHeight = walkMouthSmileHeight;
    }

    //frown() method - Set sprite mouth to frown
    public void frown() {
        walkMouthSRRect = walkMouthFrownSRRect;
        walkMouthDRRect = walkMouthFrownDRRect;
        walkMouthWidth = walkMouthFrownWidth;
        walkMouthHeight = walkMouthFrownHeight;
    }

    //hurt() method - Set sprite to hurt
    public void hurt(int damage) {
        if(hp > 0) { //If the ghost has health left to lose
            if (direction == 1) { //If ghost is going right
                hp = hp-damage; //Reduce hp by 1
                loadHurt(); //Load hurt state
            }
            if (direction == -1) { //If ghost is going left
                hp = hp-damage; //Reduce hp by 1
                loadHurtLeft(); //Load hurt left state
            }
        }
    }

    //activate(int x) method - Accepts x position and activates ghost if background x passes activation x
    public void activate(int x) {
        if(GamePanel.background.x+x > activationX) {
            activated = true;
        }
    }

    //Update() Method - Handles player positioning, physics, and moving of Destination Rectangle
    @Override
    public void update() {

        //If ghost is activated
        if(activated) {

            //If ghost can die and has no hp left
            if (canDie && hp <= 0) {
                if (direction == 1) { //If going right
                    loadDie1(); //Load die animation
                } else { //Else, going left
                    loadDie1Left(); //Load die animation
                }
                canDie = false; //Can no longer die
            }

            //If the ghost is alive, on frame 25, attack, within player range, hit the player
            if (alive && bodyFrameTicker == 25 && attacking && ((GamePanel.player.x + GamePanel.player.spriteWidth > (-GamePanel.background.x) + x && GamePanel.player.x + GamePanel.player.spriteWidth < (-GamePanel.background.x) + x + spriteWidth) || (GamePanel.player.x > (-GamePanel.background.x) + x && GamePanel.player.x < (-GamePanel.background.x) + x + spriteWidth))) {
                GamePanel.player.hurt();
            }

            //If normal ghost and can die
            if (type.equals("normal") && canDie) {

                //If outside of proximity of player, set proximity to false
                if(x+spriteWidth < GamePanel.background.x+GamePanel.player.x || x > GamePanel.background.x+GamePanel.player.x+GamePanel.player.spriteWidth) {
                    proximity = false;
                }
                //If set to go right
                if (goRight) {
                    direction = 1; //Update direction
                    //If the state has changed
                    if (stateChange) {
                        loadWalk(); //Load walk sprite
                        canAttack = true; //Can attack
                        stateChange = false; //State has changed
                    }
                    //If the player is not at maxSpeed
                    if (xSpeed < maxSpeed) {
                        xSpeed += acceleration; //Accelerate to the right
                    }

                    //If the player is at or above maxSpeed
                    if (xSpeed >= maxSpeed) {
                        xSpeed = maxSpeed; //Set xSpeed to maxSpeed
                    }

                    //If the ghost reaches maxX
                    if (x + spriteWidth > this.maxX) {
                        goRight = false; //Don't go right anymore
                        stateChange = true; //stateChange must take place
                    }
                } else { //Ghost needs to go left
                    direction = -1; //Below follow same logic as above but for left
                    if (stateChange) {
                        loadWalkLeft();
                        canAttack = true;
                        stateChange = false;
                    }
                    //If the player is not at maxSpeed
                    if (xSpeed > -maxSpeed) {
                        xSpeed -= acceleration; //Accelerate to the right
                    }

                    //If the player is at or above maxSpeed
                    if (xSpeed <= -maxSpeed) {
                        xSpeed = -maxSpeed; //Set xSpeed to maxSpeed
                    }
                    if (x < this.minX) {
                        goRight = true;
                        stateChange = true;
                    }
                }
            }

            //If aggressive type, like above, follows same logic
            if (type.equals("aggressive") && canDie) {
                if (x + spriteWidth < GamePanel.background.x + GamePanel.player.x) {
                    proximity = false;
                    direction = 1;
                    if (lastDir != direction) {
                        stateChange = true;
                    }
                    if (stateChange) {
                        loadWalk();
                        canAttack = true;
                        stateChange = false;
                    }
                    //If the player is not at maxSpeed
                    if (xSpeed < maxSpeed) {
                        xSpeed += acceleration; //Accelerate to the right
                    }

                    //If the player is at or above maxSpeed
                    if (xSpeed >= maxSpeed) {
                        xSpeed = maxSpeed; //Set xSpeed to maxSpeed
                    }
                }

                if (x > GamePanel.background.x + GamePanel.player.x + 50) {
                    proximity = false;
                    direction = -1;
                    if (lastDir != direction) {
                        stateChange = true;
                    }
                    if (stateChange) {
                        loadWalkLeft();
                        canAttack = true;
                        stateChange = false;
                    }

                    //If the ghost is not at maxSpeed
                    if (xSpeed > -maxSpeed) {
                        xSpeed -= acceleration; //Accelerate to the left
                    }

                    //If the ghost is at or above maxSpeed
                    if (xSpeed <= -maxSpeed) {
                        xSpeed = -maxSpeed; //Set xSpeed to -maxSpeed
                    }
                }

                lastDir = direction;
            }

            //If ghost is within proximity
            if (x + spriteWidth > GamePanel.background.x + GamePanel.player.x - 50 && x < GamePanel.background.x + GamePanel.player.x + 50) {
                proximity = true; //In proximity
                if (xSpeed != 0) { //If moving
                    if (direction == 1) { //If going right
                        xSpeed -= acceleration; //Slow down
                        if (xSpeed <= 0) { //If stopped
                            xSpeed = 0; //Keep speed at 0
                            stateChange = true; //State needs to change
                        }
                    }
                    if (direction == -1) { //If going left, follow logic as above
                        xSpeed += acceleration;
                        if (xSpeed >= 0) {
                            xSpeed = 0;
                            stateChange = true;
                        }
                    }
                }
                if (direction == 1) { //If going right
                    if (canAttack && !attacking) { //If can attack and is not yet attacking
                        loadAttack(); //Load attack sprite
                        canAttack = false; //Cannot attack again
                    }
                }
                if (direction == -1) { //For left, same logic as above
                    if (canAttack && !attacking) {
                        loadAttackLeft();
                        canAttack = false;
                    }
                }
            }

            x += xSpeed; //Update player x position

            //If the player state is WalkRight or WalkLeft, update the Destination Rectangles for the new player position with offsets
            if (state.equals("WalkRight") || state.equals("WalkLeft")) {
                spriteBodyDR.left = -Background.x + x + walkBodyXOffset;
                spriteBodyDR.top = y + walkBodyYOffset;
                spriteBodyDR.right = -Background.x + x + (walkBodyWidth / walkColumns) + walkBodyXOffset;
                spriteBodyDR.bottom = y + (walkBodyHeight / walkRows) + walkBodyYOffset;

                spriteEyesDR.left = -Background.x + x + walkEyesOpenXOffset;
                spriteEyesDR.top = y + walkEyesOpenYOffset;
                spriteEyesDR.right = -Background.x + x + (walkEyesOpenWidth / walkColumns) + walkEyesOpenXOffset;
                spriteEyesDR.bottom = y + (walkEyesOpenHeight / walkRows) + walkEyesOpenYOffset;

                spriteMouthDR.left = -Background.x + x + walkMouthSmileXOffset;
                spriteMouthDR.top = y + walkMouthSmileYOffset;
                spriteMouthDR.right = -Background.x + x + (walkMouthSmileWidth / walkColumns) + walkMouthSmileXOffset;
                spriteMouthDR.bottom = y + (walkMouthSmileHeight / walkRows) + walkMouthSmileYOffset;
            }

            //If the player state is AttackRight or AttackLeft, update the Destination Rectangles for the new player position with offsets
            if (state.equals("AttackRight") || state.equals("AttackLeft")) {
                spriteBodyDR.left = -Background.x + x + attackXOffset;
                spriteBodyDR.top = y + attackYOffset;
                spriteBodyDR.right = -Background.x + x + (attackWidth / attackColumns) + attackXOffset;
                spriteBodyDR.bottom = y + (attackHeight / attackRows) + attackYOffset;
            }

            //If the player state is HurtRight or HurtLeft, update the Destination Rectangles for the new player position with offsets
            if (state.equals("HurtRight") || state.equals("HurtLeft")) {
                spriteBodyDR.left = -Background.x + x + hurtXOffset;
                spriteBodyDR.top = y + hurtYOffset;
                spriteBodyDR.right = -Background.x + x + (hurtWidth / hurtColumns) + hurtXOffset;
                spriteBodyDR.bottom = y + (hurtHeight / hurtRows) + hurtYOffset;
            }
            //If the player state is HurtRight or HurtLeft, update the Destination Rectangles for the new player position with offsets
            if (state.equals("DieRight") || state.equals("DieLeft")) {
                spriteBodyDR.left = -Background.x + x + dieXOffset;
                spriteBodyDR.top = y + dieYOffset;
                spriteBodyDR.right = -Background.x + x + (dieWidth / dieColumns) + dieXOffset;
                spriteBodyDR.bottom = y + (dieHeight / dieRows) + dieYOffset;
            }
            animate(); //Animate
        }
    }

    //draw(Canvas canvas) method - Accepts a Canvas object and draws bitmaps to the Canvas
    @Override
    public void draw(Canvas canvas) {
        Log.i("state", state); //Print erroneous String to Logcat
        //If aggressive ghost
        if(type.equals("aggressive")) {
            if(x+(spriteWidth/2) > GamePanel.background.x+GamePanel.player.x-600 && x+(spriteWidth/2) < GamePanel.background.x+GamePanel.player.x+600) { //If within range of player to materialize
                if(alpha < 90) { //If alpha is not 90
                    alpha += 1; //Increase alphs
                }
                else { //Otherwise, keep alpha at 90
                    alpha = 90;
                }
            }
            else { //If not within range to materialize
                if(alpha > 0) { //If alpha is above 0
                    alpha -= 1; //Decrease alpha (go invisible)
                }
                else { //Otherwise, keep alpha at 0
                    alpha = 0;
                }
            }
            paint.setAlpha(alpha); //Set the ghost alpha

            //If current state is WalkRight, draw the parts (body, eyes, mouth)
            if(state.equals("WalkRight")) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("ghostWalkBody"), spriteBodySR, spriteBodyDR, paint);
                canvas.drawBitmap(GameActivity.getBitmapFromCache("ghostWalkEyesOpen"), spriteEyesSR, spriteEyesDR, paint);
                canvas.drawBitmap(GameActivity.getBitmapFromCache("ghostWalkMouthSmile"), spriteMouthSR, spriteMouthDR, paint);
            }

            //If current state is HurtRight, draw parts
            if(state.equals("HurtRight")) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("ghostHurt"), spriteBodySR, spriteBodyDR, paint);
            }

            //If current state is AttackRight, draw parts
            if(state.equals("AttackRight")) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("ghostAttack"), spriteBodySR, spriteBodyDR, paint);

                //If the player goes left during drawing
                if(direction == -1) {
                    state = "AttackLeft";
                }
            }

            //If current state is DieRight
            if(state.equals("DieRight")) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("ghostDie1"), spriteBodySR, spriteBodyDR, paint);
            }

            //If current state is WalkLeft
            if(state.equals("WalkLeft")) {
                flip(canvas);
                canvas.drawBitmap(GameActivity.getBitmapFromCache("ghostWalkBody"), spriteBodySR, spriteBodyDR, paint);
                canvas.drawBitmap(GameActivity.getBitmapFromCache("ghostWalkEyesOpen"), spriteEyesSR, spriteEyesDR, paint);
                canvas.drawBitmap(GameActivity.getBitmapFromCache("ghostWalkMouthSmile"), spriteMouthSR, spriteMouthDR, paint);
                unFlip(canvas);
            }

            //If current state is HurtLeft
            if(state.equals("HurtLeft")) {
                flip(canvas);
                canvas.drawBitmap(GameActivity.getBitmapFromCache("ghostHurt"), spriteBodySR, spriteBodyDR, paint);
                unFlip(canvas);
            }

            //If current state is AttackLeft
            if(state.equals("AttackLeft")) {
                flip(canvas);
                canvas.drawBitmap(GameActivity.getBitmapFromCache("ghostAttack"), spriteBodySR, spriteBodyDR, paint);
                unFlip(canvas);

                //If the player goes right during drawing
                if(direction == 1) {
                    state = "AttackRight";
                }
            }

            //If current state is DieLeft
            if(state.equals("DieLeft")) {
                flip(canvas);
                canvas.drawBitmap(GameActivity.getBitmapFromCache("ghostDie1"), spriteBodySR, spriteBodyDR, null);
                unFlip(canvas);
            }
        }

        //If ghost is normal, follows same logic as above
        if(type.equals("normal")) {
            paint.setAlpha(255);
            //If current state is WalkRight
            if (state.equals("WalkRight")) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("ghostWalkBody"), spriteBodySR, spriteBodyDR, paint);
                canvas.drawBitmap(GameActivity.getBitmapFromCache("ghostWalkEyesOpen"), spriteEyesSR, spriteEyesDR, paint);
                canvas.drawBitmap(GameActivity.getBitmapFromCache("ghostWalkMouthSmile"), spriteMouthSR, spriteMouthDR, paint);
            }

            //If current state is HurtRight
            if (state.equals("HurtRight")) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("ghostHurt"), spriteBodySR, spriteBodyDR, paint);
            }

            //If current state is AttackRight
            if (state.equals("AttackRight")) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("ghostAttack"), spriteBodySR, spriteBodyDR, paint);

                //If the player goes left during drawing
                if (direction == -1) {
                    state = "AttackLeft";
                }
            }

            //If current state is DieRight
            if (state.equals("DieRight")) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("ghostDie1"), spriteBodySR, spriteBodyDR, paint);
            }

            //If current state is WalkLeft
            if (state.equals("WalkLeft")) {
                flip(canvas);
                canvas.drawBitmap(GameActivity.getBitmapFromCache("ghostWalkBody"), spriteBodySR, spriteBodyDR, paint);
                canvas.drawBitmap(GameActivity.getBitmapFromCache("ghostWalkEyesOpen"), spriteEyesSR, spriteEyesDR, paint);
                canvas.drawBitmap(GameActivity.getBitmapFromCache("ghostWalkMouthSmile"), spriteMouthSR, spriteMouthDR, paint);
                unFlip(canvas);
            }

            //If current state is HurtLeft
            if (state.equals("HurtLeft")) {
                flip(canvas);
                canvas.drawBitmap(GameActivity.getBitmapFromCache("ghostHurt"), spriteBodySR, spriteBodyDR, paint);
                unFlip(canvas);
            }

            //If current state is AttackLeft
            if (state.equals("AttackLeft")) {
                flip(canvas);
                canvas.drawBitmap(GameActivity.getBitmapFromCache("ghostAttack"), spriteBodySR, spriteBodyDR, paint);
                unFlip(canvas);

                //If the player goes right during drawing
                if (direction == 1) {
                    state = "AttackRight";
                }
            }

            //If current state is DieLeft
            if (state.equals("DieLeft")) {
                flip(canvas);
                canvas.drawBitmap(GameActivity.getBitmapFromCache("ghostDie1"), spriteBodySR, spriteBodyDR, null);
                unFlip(canvas);
            }
        }
    }

    //updateRectangle(String s) method - Accepts a string for the state and resets the Source and Destination Rectangles to first frame. Acceptable input ex. "idle" "walk"
    private void resetRectangles(String s) {
        //If walk, set source and destination positions for body, eyes, mouth, etc
        if(s.equals("walk")) {
            walkBodySRRect.left = 0;
            walkBodySRRect.top = 0;
            walkBodySRRect.right = (walkBodyWidth/walkColumns);
            walkBodySRRect.bottom = (walkBodyHeight/walkRows);
            walkBodyDRRect.left = -Background.x+x+walkBodyXOffset;
            walkBodyDRRect.top = y+walkBodyYOffset;
            walkBodyDRRect.right = -Background.x+x+walkBodyXOffset+(walkBodyWidth/walkColumns);
            walkBodyDRRect.bottom = y+walkBodyYOffset+(walkBodyHeight/walkRows);

            walkEyesSRRect.left = 0;
            walkEyesSRRect.top = 0;
            walkEyesSRRect.right = (walkEyesOpenWidth / walkColumns);
            walkEyesSRRect.bottom = (walkEyesOpenHeight / walkRows);
            walkEyesDRRect.left = -Background.x+x + walkEyesOpenXOffset;
            walkEyesDRRect.top = y + walkEyesOpenYOffset;
            walkEyesDRRect.right = -Background.x+x + walkEyesOpenXOffset + (walkEyesOpenWidth / walkColumns);
            walkEyesDRRect.bottom = y + walkEyesOpenYOffset + (walkEyesOpenHeight / walkRows);

            walkEyesSquintSRRect.left = 0;
            walkEyesSquintSRRect.top = 0;
            walkEyesSquintSRRect.right = (walkEyesSquintWidth/walkColumns);
            walkEyesSquintSRRect.bottom = (walkEyesHeight/walkRows);
            walkEyesSquintDRRect.left = -Background.x+x+walkEyesSquintXOffset;
            walkEyesSquintDRRect.top = y+walkEyesSquintYOffset;
            walkEyesSquintDRRect.right = -Background.x+x+walkEyesSquintXOffset+(walkEyesSquintWidth/walkColumns);
            walkEyesSquintDRRect.bottom = y+walkEyesSquintYOffset+(walkEyesHeight/walkRows);

            walkMouthSRRect.left = 0;
            walkMouthSRRect.top = 0;
            walkMouthSRRect.right = (walkMouthSmileWidth/walkColumns);
            walkMouthSRRect.bottom = (walkMouthHeight/walkRows);
            walkMouthDRRect.left = -Background.x+x+walkMouthSmileXOffset;
            walkMouthDRRect.top = y+walkMouthSmileYOffset;
            walkMouthDRRect.right = -Background.x+x+walkMouthSmileXOffset+(walkMouthSmileWidth/walkColumns);
            walkMouthDRRect.bottom = y+walkMouthSmileYOffset+(walkMouthHeight/walkRows);

            walkMouthFrownSRRect.left = 0;
            walkMouthFrownSRRect.top = 0;
            walkMouthFrownSRRect.right = (walkMouthFrownWidth/walkColumns);
            walkMouthFrownSRRect.bottom = (walkMouthHeight/walkRows);
            walkMouthFrownDRRect.left = -Background.x+x+walkMouthFrownXOffset;
            walkMouthFrownDRRect.top = y+walkMouthFrownYOffset;
            walkMouthFrownDRRect.right = -Background.x+x+walkMouthFrownXOffset+(walkMouthFrownWidth/walkColumns);
            walkMouthFrownDRRect.bottom = y+walkMouthFrownYOffset+(walkMouthHeight/walkRows);
        }

        //If attack
        if(s.equals("attack")) {
            attackSRRect.left = 0;
            attackSRRect.top = 0;
            attackSRRect.right = (attackWidth/attackColumns);
            attackSRRect.bottom = (attackHeight/attackRows);
            attackDRRect.left = -Background.x+x+attackXOffset;
            attackDRRect.top = y+attackYOffset;
            attackDRRect.right = -Background.x+x+attackXOffset+(attackWidth/attackColumns);
            attackDRRect.bottom = y+attackYOffset+(attackHeight/attackRows);
        }

        //If hurt
        if(s.equals("hurt")) {
            hurtSRRect.left = 0;
            hurtSRRect.top = 0;
            hurtSRRect.right = (hurtWidth/hurtColumns);
            hurtSRRect.bottom = (hurtHeight/hurtRows);
            hurtDRRect.left = -Background.x+x+hurtXOffset;
            hurtDRRect.top = y+hurtYOffset;
            hurtDRRect.right = -Background.x+x+hurtXOffset+(hurtWidth/hurtColumns);
            hurtDRRect.bottom = y+hurtYOffset+(hurtHeight/hurtRows);
        }

        //If hurt
        if(s.equals("die")) {
            hurtSRRect.left = 0;
            hurtSRRect.top = 0;
            hurtSRRect.right = (dieWidth/dieColumns);
            hurtSRRect.bottom = (dieHeight/dieRows);
            hurtDRRect.left = -Background.x+x+dieXOffset;
            hurtDRRect.top = y+dieYOffset;
            hurtDRRect.right = -Background.x+x+dieXOffset+(dieWidth/dieColumns);
            hurtDRRect.bottom = y+dieYOffset+(dieHeight/dieRows);
        }
    }

    //stateChanged(String s) method - Accepts a String variable and changes the Source and Destination Rectangle to the corresponding state. Ex. "IdleRight" resets the idle SR and DRs and sets them as the current SR and DRs
    private void stateChanged(String s) {
        //If WalkRight or WalkLeft
        if(hp > 0 && s.equals("WalkRight") || hp > 0 &&  s.equals("WalkLeft")) {

            //If WalkRight
            if(s.equals("WalkRight")) {
                state = "WalkRight"; //Update state
            }

            //If WalkLeft
            if(s.equals("WalkLeft")) {
                state = "WalkLeft"; //Update state
            }
            resetRectangles("walk"); //Reset the SR and DR

            //Assign reset SR and DRs to current SR and DR
            spriteBodySR = walkBodySRRect;
            spriteBodyDR = walkBodyDRRect;
            spriteEyesSR = walkEyesSRRect;
            spriteEyesDR = walkEyesDRRect;
            spriteMouthSR = walkMouthSRRect;
            spriteMouthDR = walkMouthDRRect;
            resetWalk(); //Reset walk frame
        }

        //If HurtRight or HurtLeft
        else if(hp > 0 &&  s.equals("HurtRight") || hp > 0 &&  s.equals("HurtLeft")) {

            //If HurtRight
            if(s.equals("HurtRight")) {
                state = "HurtRight"; //Update state
            }

            //If HurtLeft
            if(s.equals("HurtLeft")) {
                state = "HurtLeft"; //Update state
            }
            resetRectangles("hurt"); //Reset the SR and DR

            //Assign reset SR and Drs to current SR and DR
            spriteBodySR = hurtSRRect;
            spriteBodyDR = hurtDRRect;
            resetHurt(); //Reset hurt frame
        }

        //If AttackRight or AttackLeft
        else if(hp > 0 &&  s.equals("AttackRight") || hp > 0 &&  s.equals("AttackLeft")) {
            attacking = true;

            //If AttackRight
            if(s.equals("AttackRight")) {
                state = "AttackRight"; //Update state
            }

            //If AttackLeft
            if(s.equals("AttackLeft")) {
                state = "AttackLeft"; //Update state
            }
            resetRectangles("attack"); //Reset the SR and DR

            //Assign reset SR and Drs to current SR and DR
            spriteBodySR = attackSRRect;
            spriteBodyDR = attackDRRect;
            resetAttack(); //Reset attack frame
        }

        //If DieRight or DieLeft
        else if(s.equals("DieRight") || s.equals("DieLeft")) {
            canHurt = false;
            //If AttackRight
            if(s.equals("DieRight")) {
                state = "DieRight"; //Update state
            }

            //If AttackLeft
            if(s.equals("DieLeft")) {
                state = "DieLeft"; //Update state
            }
            resetRectangles("die"); //Reset the SR and DR

            //Assign reset SR and Drs to current SR and DR
            spriteBodySR = dieSRRect;
            spriteBodyDR = dieDRRect;
            resetDie(); //Reset attack frame
        }
        else {
            Log.i("stateChanged()", s); //Print erroneous String to Logcat
        }
    }

    //flip(Canvas canvas) method - Flips the canvas horizontally at the midpoint of the sprite
    private void flip(Canvas canvas) {
        canvas.save();
        canvas.scale(-1.0f, 1.0f, -Background.x+x+spriteWidth/2, y+spriteHeight/2);
    }

    //unFlip(Canvas canvas) method - Restores the canvas back to original orientation
    private void unFlip(Canvas canvas) {
        canvas.restore();
    }

    //loadWalk() method - Loads walk bitmap
    private void loadWalk() {
        if(!attacking && !hurt) {
            stateChanged("WalkRight"); //Call stateChange
        }
    }

    //loadAttack() method - Loads attack bitmap
    public void loadAttack() {
        //If the player is not hurt
        if(!hurt) {
            stateChanged("AttackRight"); //Call stateChange
        }
    }

    //loadHurt() method - Loads hurt bitmap
    public void loadHurt() {
        stateChanged("HurtRight"); //Call stateChange
    }

    //loadWalkLeft() method - Loads flipped walk bitmap
    private void loadWalkLeft() {
        //If the player is not jumping, attacking, or hurt
        if(!attacking && !hurt) {
            stateChanged("WalkLeft"); //Call stateChange
        }
    }

    //loadAttackLeft() method - Loads flipped attack bitmap
    public void loadAttackLeft() {
        if(!hurt) {
            stateChanged("AttackLeft"); //Call stateChange
        }
    }

    //loadHurtLeft() method - Loads flipped hurt bitmap
    public void loadHurtLeft() {
        stateChanged("HurtLeft"); //Call stateChange
    }

    //loadDie() method - Loads die bitmap
    public void loadDie1() {
        stateChanged("DieRight");
    }

    //loadDieLeft() method - Loads flipped die bitmap
    public void loadDie1Left() {
        stateChanged("DieLeft");
    }

    //animate() method - Animates the player
    private void animate() {
        //If we need to swap frames
        if (currentFrame == swap) {
            currentFrame = 0; //Set currentFrame to 0
            animateState("walk"); //Animate walk state
            animateState("attack"); //Animate attack state
            animateState("hurt"); //Animate hurt state
            animateState("die"); //Animate die state
        }
        currentFrame++; //Increment current frame
    }

    //animator(DisplayFrame displayFrame, int columns, int height, Rect sr, String part, String s) method - Handles moving and updating frames and SRs
    private void animator(DisplayFrame displayFrame, int columns, int height, Rect sr, String part, String s) {
        //If at the end of the row of the body sprite sheet
        if (displayFrame.get() == columns) {
            displayFrame.set(0); //Set display frame to 0
            //If not at the last row
            if (sr.bottom < height) {
                incrementSRTop(part, s); //Increment the SR top down one row
            }
            //If at the last row
            if (sr.bottom == height) {
                resetRow(part); //Reset the row so it is at the top
            }
            incrementSRBottom(part, s); //Increment the SR bottom down one row
        }
    }

    //incrementSRTop(String part, String st) method - Increments the SR top position down one row
    private void incrementSRTop(String part, String st) {
        //Follows the logic above
        if (st.equals("walk")) {
            if (part.equals("body")) {
                this.spriteBodySR.top += (walkBodyHeight / walkRows); //Increase the row by 1
            }
            if (part.equals("eyes")) {
                    this.spriteEyesSR.top += (walkEyesHeight / walkRows); //Increase the row by 1
            }
            if (part.equals("mouth")) {
                if (frown) {
                    this.spriteMouthSR.top += (walkMouthHeight / walkRows); //Increase the row by 1
                } else {
                    this.spriteMouthSR.top += (walkMouthHeight / walkRows); //Increase the row by 1
                }
            }
        }

        //Follows the logic above
        if (st.equals("attack")) {
            if (part.equals("body")) {
                this.spriteBodySR.top += (attackHeight / attackRows); //Increase the row by 1
            }
        }

        //Follows the logic above
        if (st.equals("hurt")) {
            if (part.equals("body")) {
                this.spriteBodySR.top += (hurtHeight/hurtRows); //Increase the row by 1
            }
        }

        //Follows the logic above
        if (st.equals("die")) {
            if (part.equals("body")) {
                this.spriteBodySR.top += (dieHeight/dieRows); //Increase the row by 1
            }
        }
    }

    //resetRow(s) method - Accepts a String for the sprite part and resets the Source Rectangle top position to 0
    private void resetRow(String s) {

        //If body part
        if(s.equals("body")) {
            this.spriteBodySR.top = 0; //Reset the SR top to 0, the rest below follow the same logic
        }
        if(s.equals("eyes")) {
            this.spriteEyesSR.top = 0;
        }
        if(s.equals("mouth")) {
            this.spriteMouthSR.top = 0;
        }
    }

    //animateState(String s) method - Animates the corresponding state by calling animator, resetting frames, incrementing SR, and handling tickers
    private void animateState(String s) {

        //Follows the same logic as above
        if (s.equals("walk")) {
            if(hp > 0 && state.equals("WalkRight") || hp > 0 &&  state.equals("WalkLeft")) {
                animator(bodyDisplayFrame, walkColumns, walkBodyHeight, spriteBodySR, "body", s);
                animator(eyesDisplayFrame, walkColumns, walkEyesHeight, spriteEyesSR, "eyes", s);
                animator(mouthDisplayFrame, walkColumns, walkMouthHeight, spriteMouthSR, "mouth", s);

                //If at the last frame of sprite sheet go back to first frame
                if (bodyFrameTicker == walkFrames) {
                    resetFrame("body", s); //Reset the body frame
                }
                if (eyesFrameTicker == walkFrames) {
                    resetFrame("eyes", s); //Reset the eyes frame
                }
                if (mouthFrameTicker == walkFrames) {
                    resetFrame("mouth", s); //Reset the mouth frame
                }
                    incrementSR(s); //Increment the SR

                    //Increment tickers
                    bodyFrameTicker++;
                    bodyDisplayFrame.frame++;
                    eyesFrameTicker++;
                    eyesDisplayFrame.frame++;
                    mouthFrameTicker++;
                    mouthDisplayFrame.frame++;
            }
        }

        //Follows the same logic as above
        if (s.equals("attack")) {
            if(hp > 0 && state.equals("AttackRight") || hp > 0 &&  state.equals("AttackLeft")) {
                animator(bodyDisplayFrame, attackColumns, attackHeight, spriteBodySR, "body", s);

                //If at the last frame of sprite sheet go back to first frame
                if (bodyFrameTicker >= attackFrames) {
                    resetAttack(); //Reset the attack frame
                    attacking = false; //No longer attacking
                    canAttack = true;

                    //If the player is facing right
                    if(direction == 1) {
                        if(!proximity) {
                            loadWalk(); //Load walk sprite
                        }
                    }

                    //If the player is facing left
                    else {
                        if(!proximity) {
                            loadWalkLeft(); //Load walk sprite
                        }
                    }
                }

                //If not at the last frame
                else {
                    incrementSR(s); //Increment the SR

                    //Increment tickers
                    bodyFrameTicker++;
                    bodyDisplayFrame.frame++;
                }
            }
        }

        //Follows the same logic as above
        if (s.equals("hurt")) {
            if(state.equals("HurtRight") || state.equals("HurtLeft")) {
                animator(bodyDisplayFrame, hurtColumns, hurtHeight, spriteBodySR, "body", s);

                //If at the last frame of sprite sheet go back to first frame
                if (bodyFrameTicker >= hurtFrames) {
                    resetHurt(); //Reset the attack frame
                    hurt = false; //No longer attacking
                    attacking = false;
                    canAttack = true;

                    //If the player is facing right
                    if(direction == 1) {
                        loadWalk(); //Load walk sprite
                    }

                    //If the player is facing left
                    else {
                        loadWalkLeft(); //Load walk sprite
                    }
                }

                //If not at the last frame
                else {
                    incrementSR(s); //Increment the SR

                    //Increment tickers
                    bodyFrameTicker++;
                    bodyDisplayFrame.frame++;
                }
            }
        }

        //Follows the same logic as above
        if (s.equals("die")) {
            if(state.equals("DieRight") || state.equals("DieLeft")) {
                animator(bodyDisplayFrame, dieColumns, dieHeight, spriteBodySR, "body", s);
                attacking = false;

                //If at the last frame of sprite sheet go back to first frame
                if (bodyFrameTicker >= dieFrames) {
                    Log.i("alive", "false");
                    alive = false;
                }

                //If not at the last frame
                else {
                    incrementSR(s); //Increment the SR

                    //Increment tickers
                    bodyFrameTicker++;
                    bodyDisplayFrame.frame++;
                }
            }
        }
    }

    //incrementSR(s) method - Accepts a string for state and moves the Source Rectangle to the right over next frame
    private void incrementSR(String s){

        //Logic is the same as above
        if (s.equals("walk")) {
            this.spriteBodySR.left = bodyDisplayFrame.get() * ((walkBodyWidth/walkColumns));
            this.spriteBodySR.right = this.spriteBodySR.left+(walkBodyWidth/walkColumns);
            this.spriteEyesSR.left = eyesDisplayFrame.get() * ((walkEyesWidth/walkColumns));
            this.spriteEyesSR.right = this.spriteEyesSR.left+(walkEyesWidth/walkColumns);
            this.spriteMouthSR.left = mouthDisplayFrame.get() * ((walkMouthWidth/walkColumns));
            this.spriteMouthSR.right = this.spriteMouthSR.left+(walkMouthWidth/walkColumns);
        }

        //Logic is the same as above
        else if (s.equals("attack")) {
            this.spriteBodySR.left = bodyDisplayFrame.get() * ((attackWidth/attackColumns));
            this.spriteBodySR.right = this.spriteBodySR.left+(attackWidth/attackColumns);
        }

        //Logic is the same as above
        else if (s.equals("hurt")) {
            this.spriteBodySR.left = bodyDisplayFrame.get() * ((hurtWidth/hurtColumns));
            this.spriteBodySR.right = this.spriteBodySR.left+(hurtWidth/hurtColumns);
        }

        //Logic is the same as above
        else if (s.equals("die")) {
            this.spriteBodySR.left = bodyDisplayFrame.get() * ((dieWidth/dieColumns));
            this.spriteBodySR.right = this.spriteBodySR.left+(dieWidth/dieColumns);
        }

        else {
            Log.i("incrementSR()", s);
        }
    }

    //updateSR(part, state) method - Accepts a sprite part and the state in which to update the source rectangle bottom position
    private void incrementSRBottom(String part, String st) {

        if(st.equals("walk")) {
            if(part.equals("body")) {
                this.spriteBodySR.bottom = this.spriteBodySR.top+((walkBodyHeight/walkRows)); //Update sourceRect bottom to new index position
            }
            if(part.equals("eyes")) {
                this.spriteEyesSR.bottom = this.spriteEyesSR.top+((walkEyesHeight/walkRows)); //Update sourceRect bottom to new index position
            }
            if(part.equals("mouth")) {
                this.spriteMouthSR.bottom = this.spriteMouthSR.top+((walkMouthHeight/walkRows)); //Update sourceRect bottom to new index position
            }
        }

        if(st.equals("attack"))  {
            if (part.equals("body")) {
                this.spriteBodySR.bottom = this.spriteBodySR.top+((attackHeight/attackRows)); //Update sourceRect bottom to new index position
            }
        }
        if(st.equals("hurt"))  {
            if (part.equals("body")) {
                this.spriteBodySR.bottom = this.spriteBodySR.top+((hurtHeight/hurtRows)); //Update sourceRect bottom to new index position
            }
        }

        if(st.equals("die"))  {
            if (part.equals("body")) {
                this.spriteBodySR.bottom = this.spriteBodySR.top+((dieHeight/dieRows)); //Update sourceRect bottom to new index position
            }
        }
    }

    //resetFrame(String part, String st) method - Accepts a sprite part and the state and resets the source rect to first index frame
    private void resetFrame(String part, String st) {
        if(st.equals("walk")) {
            if(part.equals("body")) {
                bodyDisplayFrame.set(0); //Set displayFrame back to 0
                this.spriteBodySR.top = 0; //Set sourceRect top back to 0
                this.spriteBodySR.bottom = this.spriteBodySR.top+((walkBodyHeight/walkRows)); //Update sourceRect bottom to new index position
                bodyFrameTicker = 0; //Reset frameTicker
            }
            if(part.equals("eyes")) {
                eyesDisplayFrame.set(0); //Set displayFrame back to 0
                this.spriteEyesSR.top = 0; //Increase the row by 1
                this.spriteEyesSR.bottom = this.spriteEyesSR.top+((walkEyesHeight/walkRows)); //Increase the row by 1
                eyesFrameTicker = 0; //Reset frameTicker
            }
            if(part.equals("mouth")) {
                mouthDisplayFrame.set(0); //Set displayFrame back to 0
                this.spriteMouthSR.top = 0; //Increase the row by 1
                this.spriteMouthSR.bottom = this.spriteMouthSR.top+((walkMouthHeight/walkRows)); //Increase the row by 1
                mouthFrameTicker = 0; //Reset frameTicker
            }
        }
        if(st.equals("attack")) {
            if(part.equals("body")) {
                bodyDisplayFrame.set(0); //Set displayFrame back to 0
                this.spriteBodySR.top = 0; //Set sourceRect top back to 0
                this.spriteBodySR.bottom = this.spriteBodySR.top+((attackHeight/attackRows)); //Update sourceRect bottom to new index position
                bodyFrameTicker = 0; //Reset frameTicker
            }
        }
        if(st.equals("hurt")) {
            if(part.equals("body")) {
                bodyDisplayFrame.set(0); //Set displayFrame back to 0
                this.spriteBodySR.top = 0; //Set sourceRect top back to 0
                this.spriteBodySR.bottom = this.spriteBodySR.top+((hurtHeight/hurtRows)); //Update sourceRect bottom to new index position
                bodyFrameTicker = 0; //Reset frameTicker
            }
        }
        if(st.equals("die")) {
            if(part.equals("body")) {
                bodyDisplayFrame.set(0); //Set displayFrame back to 0
                this.spriteBodySR.top = 0; //Set sourceRect top back to 0
                this.spriteBodySR.bottom = this.spriteBodySR.top+((dieHeight/dieRows)); //Update sourceRect bottom to new index position
                bodyFrameTicker = 0; //Reset frameTicker
            }
        }
    }

    //resetWalk method() - Resets all the walk frames, same logic as resetIdle() method
    private void resetWalk() {
        currentFrame = 0; //Set currentFrame to 0
        resetFrame("body", "walk"); //Reset body frame
        resetFrame("eyes", "walk"); //Reset eyes frame
        resetFrame("mouth", "walk"); //Reset mouth frame
    }

    //resetAttack method() - Resets all the attack frames, same logic as resetIdle() method
    private void resetAttack() {
        currentFrame = 0; //Set currentFrame to 0
        resetFrame("body", "attack"); //Reset body frame\
    }

    //resetHurt method() - Resets all the hurt frames, same logic as resetIdle() method
    private void resetHurt() {
        currentFrame = 0; //Set currentFrame to 0
        resetFrame("body", "hurt"); //Reset body frame
    }

    //resetDie method() - Resets all the die frames, same logic as resetIdle() method
    private void resetDie() {
        currentFrame = 0; //Set currentFrame to 0
        resetFrame("body", "die"); //Reset body frame
    }

    //setSpriteSize() method - Set the size of the sprite
    private void setSpriteSize() {
        spriteHeight = spriteBodyDR.bottom-spriteBodyDR.top; //Highest SR yPos - lowest SR yPos, gives current height
        spriteWidth = spriteBodyDR.right-spriteBodyDR.left; //Highest SR xPos - lowest SR xPos, gives current width
    }

    //getSpriteSize() method - gets the sprite size x and y as a Point object, not used anymore
    public Point getSpriteSize() {
        Point point = new Point(spriteBodyDR.right-spriteBodyDR.left, spriteBodyDR.right-spriteBodyDR.left);
        return point;
    }
}
