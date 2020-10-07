/*******************************************************************************
 //Author: Chad Cromwell
 //Date: February 10th, 2019
 //Assignment: 1
 //Program: Player.java
 //Description: Represents a player
 //Methods:
 //hurt() method - Lowers player HP and loads hurt animation
 //Update() Method - Handles player positioning, physics, and moving of Destination Rectangle
 //draw(Canvas canvas) method - Accepts a Canvas object and draws bitmaps to the Canvas
 //updateRectangle(String s) method - Accepts a string for the state and resets the Source and Destination Rectangles to first frame. Acceptable input ex. "idle" "walk"
 //stateChanged(String s) method - Accepts a String variable and changes the Source and Destination Rectangle to the corresponding state. Ex. "IdleRight" resets the idle SR and DRs and sets them as the current SR and DRs
 //flip(Canvas canvas) method - Flips the canvas horizontally at the midpoint of the sprite
 //unFlip(Canvas canvas) method - Restores the canvas back to original orientation
 //loadWalk() method - Loads walk bitmap
 //loadJump() method - Load jump bitmap
 //loadAttack() method - Loads attack bitmap
 //loadHurt() method - Loads hurt bitmap
 //loadIdleLeft() method - Loads flipped idle bitmap
 //loadWalkLeft() method - Loads flipped walk bitmap
 //loadJumpLeft() method - Loads flipped jump bitmap
 //loadAttackLeft() method - Loads flipped attack bitmap
 //loadHurtLeft() method - Loads flipped hurt bitmap
 //animate() method - Animates the player
 //animator(DisplayFrame displayFrame, int columns, int height, Rect sr, String part, String s) method - Handles moving and updating frames and SRs
 //incrementSRTop(String part, String st) method - Increments the SR top position down one row
 //resetRow(s) method - Accepts a String for the sprite part and resets the Source Rectangle top position to 0
 //animateState(String s) method - Animates the corresponding state by calling animator, resetting frames, incrementing SR, and handling tickers
 //incrementSR(s) method - Accepts a string for state and moves the Source Rectangle to the right over next frame
 //updateSR(part, state) method - Accepts a sprite part and the state in which to update the source rectangle bottom position
 //resetFrame(String part, String st) method - Accepts a body part and state, resets source rect to first index on the sprite sheet according to state and part
 //resetIdle() method - Resets all the idle frames
 //resetWalk method() - Resets all the walk frames, same logic as resetIdle() method
 //resetJump method() - Resets all the jump frames, same logic as resetIdle() method
 //resetAttack method() - Resets all the attack frames, same logic as resetIdle() method
 //resetHurt method() - Resets all the hurt frames, same logic as resetIdle() method
 //setSpriteSize() method - Set the size of the sprite
 ********************************************************************************/
package com.chadcromwell.tme1;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

//Player Class - Represents a player
public class Player implements GameObject {

    //Player variables
    public static int spriteWidth; //Width of sprite
    public static int spriteHeight; //Height of sprite
    private static int factor = 1; //Factor used for scaling bitmap in and sprite out
    public int acceleration = 2; //Player acceleration speed
    public int maxSpeed = 30; //Max player speed
    public int hp = 4; //Player hp
    public int damage = 1; //Damage player deals
    public int attackDrops = 0; //Count the number of attack drops the player picks up (charges for weapon power), when 3 is reached, the next attack does 3 damage then resets to 0
    public int xSpeed; //Speed in x direction
    private int ySpeed; //Speed in y direction
    private int jumpAmount = 200; //How high the player can jump Keep at 100
    private int jumpAcceleration = 8; //How fast the player gains altitude Keep at 5
    public int gravity = 8; //Gravity Keep at 5
    public boolean chargedAttack = false;
    public boolean moving = false; //If the player is moving or not
    public boolean jumping = false; //If the player is jumping or not
    public boolean attacking = false; //If the player is attacking
    public boolean hurt = false;
    public int x; //x position, set in GamePanel
    public int y; //y position, set in GamePanel
    public int direction = 1; //Player direction 1 going right, -1 going left
    public String state = "IdleRight"; //Defines player state, Idle, Walk, Hurt, Jump, Attack are options
    public String previousState = "IdleRight"; //Keeps track of the previous state to see if a change has taken place
    public int hitX = 0; //Position where player sword can hit
    public int swordWidth = 170; //Width of sword in x coords (distance it reaches out from player sprite)
    public int kills = 0; //Number of kills player has

    //For animation
    private int currentFrame = 0; //Keep track of current frame
    private int swap = 1; //When to swap

    //Bitmap sizes, statePartDimension, self explanatory
    private int idleBodyWidth;
    private int idleBodyHeight;
    private int idleHeadWidth;
    private int idleHeadHeight;
    private int idleEyesWidth;
    private int idleEyesHeight;
    private int idleMouthWidth;
    private int idleMouthHeight;
    private int idleWeaponWidth;
    private int idleWeaponHeight;
    private int idleParticleWidth;
    private int idleParticleHeight;

    private int walkBodyWidth;
    private int walkBodyHeight;
    private int walkHeadWidth;
    private int walkHeadHeight;
    private int walkEyesWidth;
    private int walkEyesHeight;
    private int walkMouthWidth;
    private int walkMouthHeight;
    private int walkWeaponWidth;
    private int walkWeaponHeight;
    private int walkParticleWidth;
    private int walkParticleHeight;

    private int hurtBodyWidth;
    private int hurtBodyHeight;
    private int hurtHeadWidth;
    private int hurtHeadHeight;
    private int hurtWeaponWidth;
    private int hurtWeaponHeight;
    private int hurtParticleWidth;
    private int hurtParticleHeight;

    private int jumpBodyWidth;
    private int jumpBodyHeight;
    private int jumpHeadWidth;
    private int jumpHeadHeight;
    private int jumpArmWidth;
    private int jumpArmHeight;
    private int jumpWeaponWidth;
    private int jumpWeaponHeight;
    private int jumpParticleWidth;
    private int jumpParticleHeight;

    private int attackBodyWidth;
    private int attackBodyHeight;
    private int attackHeadWidth;
    private int attackHeadHeight;
    private int attackArmWidth;
    private int attackArmHeight;
    private int attackWeaponWidth;
    private int attackWeaponHeight;
    private int attackParticleWidth;
    private int attackParticleHeight;

    public boolean doneDirectionChange = true; //If the player sprite has finished changing direction
    private boolean goDown = false; //If the player is falling or not
    private boolean captureState = true; //If need to capture player state
    public boolean captureSpeed = true; //If need to capture player speed

    //DisplayFrames - Frames to display, self explanatory
    private DisplayFrame bodyDisplayFrame;
    private DisplayFrame headDisplayFrame;
    private DisplayFrame eyesDisplayFrame;
    private DisplayFrame mouthDisplayFrame;
    private DisplayFrame armDisplayFrame;
    private DisplayFrame weaponDisplayFrame;
    private DisplayFrame particleDisplayFrame;

    //Amount of idle frames
    private int idleFrames = 30; //How many frames the sprite sheet has
    private int idleColumns = 5; //How many columns the sprite sheet has
    private int idleRows = 6; //How many rows the sprite sheet has

    //Amount of walk frames, same logic as above
    private int walkFrames = 28; //How many frames the sprite sheet has
    private int walkColumns = 5;
    private int walkRows = 6;

    //Amount of jump frames, same logic as above
    private int jumpFrames = 24; //How many frames the sprite sheet has
    private int jumpColumns = 4;
    private int jumpRows = 6;

    //Amount of hurt frames, same logic as above
    private int hurtFrames = 16; //How many frames the sprite sheet has
    private int hurtColumns = 4;
    private int hurtRows = 4;
    private boolean canHurt = true;

    //Amount of attack frames, same logic as above
    private int attackFrames = 23; //How many frames the sprite sheet has
    private int attackColumns = 4;
    private int attackRows = 6;

    //Tickers to keep track of current frame for each part
    public int bodyFrameTicker;
    private int headFrameTicker;
    private int eyesFrameTicker;
    private int mouthFrameTicker;
    private int armFrameTicker;
    private int weaponFrameTicker;
    private int particleFrameTicker;

    //Rect objects to hold bitmap, source (SR) holds full sprite sheet, destination (DR) holds specific sprite frame.
    //For current state sprite, other Rects get copied to this set of Rects
    private Rect spriteBodySR;
    private Rect spriteBodyDR;
    private Rect spriteHeadSR;
    private Rect spriteHeadDR;
    private Rect spriteEyesSR;
    private Rect spriteEyesDR;
    private Rect spriteMouthSR;
    private Rect spriteMouthDR;
    private Rect spriteArmDR;
    private Rect spriteArmSR;
    private Rect spriteWeaponSR;
    private Rect spriteWeaponDR;
    private Rect spriteParticleSR;
    private Rect spriteParticleDR;

    //For idle state (animation)
    private Rect idleBodySRRect;
    private Rect idleBodyDRRect;
    private Rect idleHeadSRRect;
    private Rect idleHeadDRRect;
    private Rect idleEyesSRRect;
    private Rect idleEyesDRRect;
    private Rect idleMouthSRRect;
    private Rect idleMouthDRRect;
    private Rect idleWeaponSRRect;
    private Rect idleWeaponDRRect;
    private Rect idleParticleSRRect;
    private Rect idleParticleDRRect;

    //For walk state (animation)
    private Rect walkBodySRRect;
    private Rect walkBodyDRRect;
    private Rect walkHeadSRRect;
    private Rect walkHeadDRRect;
    private Rect walkEyesSRRect;
    private Rect walkEyesDRRect;
    private Rect walkMouthSRRect;
    private Rect walkMouthDRRect;
    private Rect walkWeaponSRRect;
    private Rect walkWeaponDRRect;
    private Rect walkParticleSRRect;
    private Rect walkParticleDRRect;

    //For jump state (animation)
    private Rect jumpBodySRRect;
    private Rect jumpBodyDRRect;
    private Rect jumpHeadSRRect;
    private Rect jumpHeadDRRect;
    private Rect jumpArmSRRect;
    private Rect jumpArmDRRect;
    private Rect jumpWeaponSRRect;
    private Rect jumpWeaponDRRect;
    private Rect jumpParticleSRRect;
    private Rect jumpParticleDRRect;

    //For hurt state (animation)
    private Rect hurtBodySRRect;
    private Rect hurtBodyDRRect;
    private Rect hurtHeadSRRect;
    private Rect hurtHeadDRRect;
    private Rect hurtWeaponSRRect;
    private Rect hurtWeaponDRRect;
    private Rect hurtParticleSRRect;
    private Rect hurtParticleDRRect;

    //For attack state (animation)
    private Rect attackBodySRRect;
    private Rect attackBodyDRRect;
    private Rect attackHeadSRRect;
    private Rect attackHeadDRRect;
    private Rect attackArmSRRect;
    private Rect attackArmDRRect;
    private Rect attackWeaponSRRect;
    private Rect attackWeaponDRRect;
    private Rect attackParticleSRRect;
    private Rect attackParticleDRRect;

    //Idle offsets
    private int idleBodyXOffset = 0;
    private int idleBodyYOffset = 0;
    private int idleHeadXOffset = idleBodyXOffset-40;
    private int idleHeadYOffset = idleBodyYOffset-155;
    private int idleEyesXOffset = idleBodyXOffset+0;
    private int idleEyesYOffset = idleBodyYOffset+0;
    private int idleMouthXOffset = idleBodyXOffset+60;
    private int idleMouthYOffset = idleBodyYOffset-13;
    private int idleWeaponXOffset = idleBodyXOffset-25;
    private int idleWeaponYOffset = idleBodyYOffset+36;
    private int idleParticleXOffset = idleBodyXOffset+41;
    private int idleParticleYOffset = idleBodyYOffset+83;

    //Walk offsets
    private int walkBodyXOffset = -35;
    private int walkBodyYOffset = 0;
    private int walkHeadXOffset = walkBodyXOffset-18;
    private int walkHeadYOffset = walkBodyYOffset-157;
    private int walkEyesXOffset = walkBodyXOffset+0;
    private int walkEyesYOffset = walkBodyYOffset+0;
    private int walkMouthXOffset = walkBodyXOffset+88;
    private int walkMouthYOffset = walkBodyYOffset-12;
    private int walkWeaponXOffset = walkBodyXOffset-36;
    private int walkWeaponYOffset = walkBodyYOffset-143;
    private int walkParticleXOffset = walkBodyXOffset+30;
    private int walkParticleYOffset = walkBodyYOffset+42;

    //Jump offsets
    private int jumpBodyXOffset = -15;
    private int jumpBodyYOffset = -45;
    private int jumpHeadXOffset = jumpBodyXOffset-45;
    private int jumpHeadYOffset = jumpBodyYOffset-110;
    private int jumpArmXOffset = jumpBodyXOffset-6;
    private int jumpArmYOffset = jumpBodyYOffset-2;
    private int jumpWeaponXOffset = jumpBodyXOffset-164;
    private int jumpWeaponYOffset = jumpBodyYOffset-164;
    private int jumpParticleXOffset = jumpBodyXOffset+0;
    private int jumpParticleYOffset = jumpBodyYOffset+0;

    //Hurt offsets
    private int hurtBodyXOffset = 2;
    private int hurtBodyYOffset = 2;
    private int hurtHeadXOffset = hurtBodyXOffset-69;
    private int hurtHeadYOffset = hurtBodyYOffset-156;
    private int hurtWeaponXOffset = hurtBodyXOffset-34; //-29
    private int hurtWeaponYOffset = hurtBodyYOffset+35; //36
    private int hurtParticleXOffset = hurtBodyXOffset+0;
    private int hurtParticleYOffset = hurtBodyYOffset+0;

    //Attack offsets
    private int attackBodyXOffset = 0;
    private int attackBodyYOffset = +1;
    private int attackHeadXOffset = attackBodyXOffset-62;
    private int attackHeadYOffset = attackBodyYOffset-158;
    private int attackArmXOffset = attackBodyXOffset-0;
    private int attackArmYOffset = attackBodyYOffset-5;
    private int attackWeaponXOffset = attackBodyXOffset-161; //161
    private int attackWeaponYOffset = attackBodyYOffset-199;
    private int attackParticleXOffset = attackBodyXOffset+0;
    private int attackParticleYOffset = attackBodyYOffset-10;

    public boolean scroll = false; //If the background can scroll

    //Player() Constructor
    public Player() {
        bodyDisplayFrame = new DisplayFrame(0); //Create a new DisplayFrame object and initialize the frame to 0
        headDisplayFrame = new DisplayFrame(0); //Create a new DisplayFrame object and initialize the frame to 0
        eyesDisplayFrame = new DisplayFrame(0); //Create a new DisplayFrame object and initialize the frame to 0
        mouthDisplayFrame = new DisplayFrame(0); //Create a new DisplayFrame object and initialize the frame to 0
        armDisplayFrame = new DisplayFrame(0); //Create a new DisplayFrame object and initialize the frame to 0
        weaponDisplayFrame = new DisplayFrame(0); //Create a new DisplayFrame object and initialize the frame to 0
        particleDisplayFrame = new DisplayFrame(0); //Create a new DisplayFrame object and initialize the frame to 0

        //Load sprite sheets into memory
        //Load idle bitmaps
        GameActivity.addBitmapToMemoryCache("heroIdleBody", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Idle/Body.png"));
        GameActivity.addBitmapToMemoryCache("heroIdleHead", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Idle/Head.png"));
        GameActivity.addBitmapToMemoryCache("heroIdleMouthSmile", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Idle/Mouth/Smile.png"));
        GameActivity.addBitmapToMemoryCache("heroIdleMouthFrown", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Idle/Mouth/Frown.png"));
        GameActivity.addBitmapToMemoryCache("heroIdleMouthSmall", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Idle/Mouth/Small.png"));
        GameActivity.addBitmapToMemoryCache("heroIdleMouthOh", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Idle/Mouth/Oh.png"));
        GameActivity.addBitmapToMemoryCache("heroIdleEyesAngry", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Idle/Eyes/Angry.png"));
        GameActivity.addBitmapToMemoryCache("heroIdleEyesHurt", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Idle/Eyes/Hurt.png"));
        GameActivity.addBitmapToMemoryCache("heroIdleEyesClosed", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Idle/Eyes/Closed.png"));
        GameActivity.addBitmapToMemoryCache("heroIdleSword", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Idle/Weapons/Sword.png"));
        GameActivity.addBitmapToMemoryCache("heroIdleSwordParticle1", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Idle/Weapons/SwordParticle1.png"));
        GameActivity.addBitmapToMemoryCache("heroIdleSwordParticle2", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Idle/Weapons/SwordParticle2.png"));
        GameActivity.addBitmapToMemoryCache("heroIdleSwordParticle3", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Idle/Weapons/SwordParticle3.png"));
        GameActivity.addBitmapToMemoryCache("heroIdleSwordParticle4", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Idle/Weapons/SwordParticle4.png"));

        //Load walk bitmaps
        GameActivity.addBitmapToMemoryCache("heroWalkBody", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Walk/Body.png"));
        GameActivity.addBitmapToMemoryCache("heroWalkHead", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Walk/Head.png"));
        GameActivity.addBitmapToMemoryCache("heroWalkMouthSmile", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Walk/Mouth/Smile.png"));
        GameActivity.addBitmapToMemoryCache("heroWalkMouthFrown", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Walk/Mouth/Frown.png"));
        GameActivity.addBitmapToMemoryCache("heroWalkMouthSmall", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Walk/Mouth/Small.png"));
        GameActivity.addBitmapToMemoryCache("heroWalkMouthOh", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Walk/Mouth/Oh.png"));
        GameActivity.addBitmapToMemoryCache("heroWalkEyesAngry", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Walk/Eyes/Angry.png"));
        GameActivity.addBitmapToMemoryCache("heroWalkEyesHurt", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Walk/Eyes/Hurt.png"));
        GameActivity.addBitmapToMemoryCache("heroWalkEyesClosed", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Walk/Eyes/Closed.png"));
        GameActivity.addBitmapToMemoryCache("heroWalkSword", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Walk/Weapons/Sword.png"));
        GameActivity.addBitmapToMemoryCache("heroWalkSwordParticle1", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Walk/Weapons/SwordParticle1.png"));
        GameActivity.addBitmapToMemoryCache("heroWalkSwordParticle2", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Walk/Weapons/SwordParticle2.png"));
        GameActivity.addBitmapToMemoryCache("heroWalkSwordParticle3", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Walk/Weapons/SwordParticle3.png"));
        GameActivity.addBitmapToMemoryCache("heroWalkSwordParticle4", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Walk/Weapons/SwordParticle4.png"));

        //Load jump bitmaps
        GameActivity.addBitmapToMemoryCache("heroJumpBody", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Jump/Body.png"));
        GameActivity.addBitmapToMemoryCache("heroJumpHead", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Jump/Head.png"));
        GameActivity.addBitmapToMemoryCache("heroJumpArm", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Jump/Arm.png"));
        GameActivity.addBitmapToMemoryCache("heroJumpSword", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Jump/Weapons/Sword.png"));
        GameActivity.addBitmapToMemoryCache("heroJumpSwordParticle1", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Jump/Weapons/SwordParticle1.png"));
        GameActivity.addBitmapToMemoryCache("heroJumpSwordParticle2", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Jump/Weapons/SwordParticle2.png"));
        GameActivity.addBitmapToMemoryCache("heroJumpSwordParticle3", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Jump/Weapons/SwordParticle3.png"));
        GameActivity.addBitmapToMemoryCache("heroJumpSwordParticle4", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Jump/Weapons/SwordParticle4.png"));

        //Load hurt bitmaps
        GameActivity.addBitmapToMemoryCache("heroHurtBody", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Hurt/Body.png"));
        GameActivity.addBitmapToMemoryCache("heroHurtHead", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Hurt/Head.png"));
        GameActivity.addBitmapToMemoryCache("heroHurtSword", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Hurt/Weapons/Sword.png"));
        GameActivity.addBitmapToMemoryCache("heroHurtSwordParticle1", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Hurt/Weapons/SwordParticle1.png"));
        GameActivity.addBitmapToMemoryCache("heroHurtSwordParticle2", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Hurt/Weapons/SwordParticle2.png"));
        GameActivity.addBitmapToMemoryCache("heroHurtSwordParticle3", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Hurt/Weapons/SwordParticle3.png"));
        GameActivity.addBitmapToMemoryCache("heroHurtSwordParticle4", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Hurt/Weapons/SwordParticle4.png"));

        //Load attack bitmaps
        GameActivity.addBitmapToMemoryCache("heroAttackBody", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Attack/Body.png"));
        GameActivity.addBitmapToMemoryCache("heroAttackHead", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Attack/Head.png"));
        GameActivity.addBitmapToMemoryCache("heroAttackArm", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Attack/Arm.png"));
        GameActivity.addBitmapToMemoryCache("heroAttackSword", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Attack/Weapons/Sword.png"));
        GameActivity.addBitmapToMemoryCache("heroAttackSwordParticle1", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Attack/Weapons/SwordParticle1.png"));
        GameActivity.addBitmapToMemoryCache("heroAttackSwordParticle2", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Attack/Weapons/SwordParticle2.png"));
        GameActivity.addBitmapToMemoryCache("heroAttackSwordParticle3", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Attack/Weapons/SwordParticle3.png"));
        GameActivity.addBitmapToMemoryCache("heroAttackSwordParticle4", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Hero/Attack/Weapons/SwordParticle4.png"));


        //Get sizes (width and height) of the bitmaps
        idleBodyWidth = GameActivity.getBitmapFromCache("heroIdleBody").getWidth();
        idleBodyHeight = GameActivity.getBitmapFromCache("heroIdleBody").getHeight();
        idleHeadWidth = GameActivity.getBitmapFromCache("heroIdleHead").getWidth();
        idleHeadHeight = GameActivity.getBitmapFromCache("heroIdleHead").getHeight();
        idleEyesWidth = GameActivity.getBitmapFromCache("heroIdleEyesClosed").getWidth();
        idleEyesHeight = GameActivity.getBitmapFromCache("heroIdleEyesClosed").getHeight();
        idleMouthWidth = GameActivity.getBitmapFromCache("heroIdleMouthSmile").getWidth();
        idleMouthHeight = GameActivity.getBitmapFromCache("heroIdleMouthSmile").getHeight();
        idleWeaponWidth = GameActivity.getBitmapFromCache("heroIdleSword").getWidth();
        idleWeaponHeight = GameActivity.getBitmapFromCache("heroIdleSword").getHeight();
        idleParticleWidth = GameActivity.getBitmapFromCache("heroIdleSwordParticle1").getWidth();
        idleParticleHeight = GameActivity.getBitmapFromCache("heroIdleSwordParticle1").getHeight();

        walkBodyWidth = GameActivity.getBitmapFromCache("heroWalkBody").getWidth();
        walkBodyHeight = GameActivity.getBitmapFromCache("heroWalkBody").getHeight();
        walkHeadWidth = GameActivity.getBitmapFromCache("heroWalkHead").getWidth();
        walkHeadHeight = GameActivity.getBitmapFromCache("heroWalkHead").getHeight();
        walkEyesWidth = GameActivity.getBitmapFromCache("heroWalkEyesClosed").getWidth();
        walkEyesHeight = GameActivity.getBitmapFromCache("heroWalkEyesClosed").getHeight();
        walkMouthWidth = GameActivity.getBitmapFromCache("heroWalkMouthSmile").getWidth();
        walkMouthHeight = GameActivity.getBitmapFromCache("heroWalkMouthSmile").getHeight();
        walkWeaponWidth = GameActivity.getBitmapFromCache("heroWalkSword").getWidth();
        walkWeaponHeight = GameActivity.getBitmapFromCache("heroWalkSword").getHeight();
        walkParticleWidth = GameActivity.getBitmapFromCache("heroWalkSwordParticle1").getWidth();
        walkParticleHeight = GameActivity.getBitmapFromCache("heroWalkSwordParticle1").getHeight();

        hurtBodyWidth = GameActivity.getBitmapFromCache("heroHurtBody").getWidth();
        hurtBodyHeight = GameActivity.getBitmapFromCache("heroHurtBody").getHeight();
        hurtHeadWidth = GameActivity.getBitmapFromCache("heroHurtHead").getWidth();
        hurtHeadHeight = GameActivity.getBitmapFromCache("heroHurtHead").getHeight();
        hurtWeaponWidth = GameActivity.getBitmapFromCache("heroHurtSword").getWidth();
        hurtWeaponHeight = GameActivity.getBitmapFromCache("heroHurtSword").getHeight();
        hurtParticleWidth = GameActivity.getBitmapFromCache("heroHurtSwordParticle1").getWidth();
        hurtParticleHeight = GameActivity.getBitmapFromCache("heroHurtSwordParticle1").getHeight();

        jumpBodyWidth = GameActivity.getBitmapFromCache("heroJumpBody").getWidth();
        jumpBodyHeight = GameActivity.getBitmapFromCache("heroJumpBody").getHeight();
        jumpHeadWidth = GameActivity.getBitmapFromCache("heroJumpHead").getWidth();
        jumpHeadHeight = GameActivity.getBitmapFromCache("heroJumpHead").getHeight();
        jumpArmWidth = GameActivity.getBitmapFromCache("heroJumpArm").getWidth();
        jumpArmHeight = GameActivity.getBitmapFromCache("heroJumpArm").getHeight();
        jumpWeaponWidth = GameActivity.getBitmapFromCache("heroJumpSword").getWidth();
        jumpWeaponHeight = GameActivity.getBitmapFromCache("heroJumpSword").getHeight();
        jumpParticleWidth = GameActivity.getBitmapFromCache("heroJumpSwordParticle1").getWidth();
        jumpParticleHeight = GameActivity.getBitmapFromCache("heroJumpSwordParticle1").getHeight();

        attackBodyWidth = GameActivity.getBitmapFromCache("heroAttackBody").getWidth();
        attackBodyHeight = GameActivity.getBitmapFromCache("heroAttackBody").getHeight();
        attackHeadWidth = GameActivity.getBitmapFromCache("heroAttackHead").getWidth();
        attackHeadHeight = GameActivity.getBitmapFromCache("heroAttackHead").getHeight();
        attackArmWidth = GameActivity.getBitmapFromCache("heroAttackArm").getWidth();
        attackArmHeight = GameActivity.getBitmapFromCache("heroAttackArm").getHeight();
        attackWeaponWidth = GameActivity.getBitmapFromCache("heroAttackSword").getWidth();
        attackWeaponHeight = GameActivity.getBitmapFromCache("heroAttackSword").getHeight();
        attackParticleWidth = GameActivity.getBitmapFromCache("heroAttackSwordParticle1").getWidth();
        attackParticleHeight = GameActivity.getBitmapFromCache("heroAttackSwordParticle1").getHeight();

        //Initialise source and destination Rects for each state
        //Idle state
        idleBodySRRect = new Rect(0, 0, idleBodyWidth / idleColumns, idleBodyHeight / idleRows); //Body SR Rect
        idleBodyDRRect = new Rect(x + idleBodyXOffset, y + idleBodyYOffset, x + idleBodyXOffset + (idleBodyWidth / idleColumns), y + idleBodyYOffset + (idleBodyHeight / idleRows)); //Body DR Rect
        idleHeadSRRect = new Rect(0, 0, idleHeadWidth / idleColumns, idleHeadHeight / idleRows); //Head SR Rect
        idleHeadDRRect = new Rect(x + idleHeadXOffset, y + idleHeadYOffset, x + idleHeadXOffset + (idleHeadWidth / idleColumns), y + idleHeadYOffset + (idleHeadHeight / idleRows)); //Head DR Rect
        idleEyesSRRect = new Rect(0, 0, idleEyesWidth / idleColumns, idleEyesHeight / idleRows); //Eyes SR Rect
        idleEyesDRRect = new Rect(x + idleEyesXOffset, y + idleEyesYOffset, x + idleEyesXOffset + (idleEyesWidth / idleColumns), y + idleEyesYOffset + (idleEyesHeight / idleRows)); //Eyes DR Rect
        idleMouthSRRect = new Rect(0, 0, idleMouthWidth / idleColumns, idleMouthHeight / idleRows); //Mouth SR Rect
        idleMouthDRRect = new Rect(x + idleMouthXOffset, y + idleMouthYOffset, x + idleMouthXOffset + (idleMouthWidth / idleColumns), y + idleMouthYOffset + (idleMouthHeight / idleRows)); //Mouth DR Rect
        idleWeaponSRRect = new Rect(0, 0, idleWeaponWidth / idleColumns, idleWeaponHeight / idleRows); //Idle SR Rect
        idleWeaponDRRect = new Rect(x + idleWeaponXOffset, y + idleWeaponYOffset, x + idleWeaponXOffset + (idleWeaponWidth / idleColumns), y + idleWeaponYOffset + (idleWeaponHeight / idleRows)); //Weapon DR Rect
        idleParticleSRRect = new Rect(0, 0, idleParticleWidth / idleColumns, idleParticleHeight / idleRows); //Particle SR Rect
        idleParticleDRRect = new Rect(x + idleParticleXOffset, y + idleParticleYOffset, x + idleParticleXOffset + (idleParticleWidth / idleColumns), y + idleParticleYOffset + (idleParticleHeight / idleRows)); //Particle DR Rect

        //Walk state - this and the code below follow the same logic as that above
        walkBodySRRect = new Rect(0, 0, walkBodyWidth / walkColumns, walkBodyHeight / walkRows);
        walkBodyDRRect = new Rect(x + walkBodyXOffset, y + walkBodyYOffset, x + walkBodyXOffset + (walkBodyWidth / walkColumns), y + walkBodyYOffset + (walkBodyHeight / walkRows));
        walkHeadSRRect = new Rect(0, 0, walkHeadWidth / walkColumns, walkHeadHeight / walkRows);
        walkHeadDRRect = new Rect(x + walkHeadXOffset, y + walkHeadYOffset, x + walkHeadXOffset + (walkHeadWidth / walkColumns), y + walkHeadYOffset + (walkHeadHeight / walkRows));
        walkEyesSRRect = new Rect(0, 0, walkEyesWidth / walkColumns, walkEyesHeight / walkRows);
        walkEyesDRRect = new Rect(x + walkEyesXOffset, y + walkEyesYOffset, x + walkEyesXOffset + (walkEyesWidth / walkColumns), y + walkEyesYOffset + (walkEyesHeight / walkRows));
        walkMouthSRRect = new Rect(0, 0, walkMouthWidth / walkColumns, walkMouthHeight / walkRows);
        walkMouthDRRect = new Rect(x + walkMouthXOffset, y + walkMouthYOffset, x + walkMouthXOffset + (walkMouthWidth / walkColumns), y + walkMouthYOffset + (walkMouthHeight / walkRows));
        walkWeaponSRRect = new Rect(0, 0, walkWeaponWidth / walkColumns, walkWeaponHeight / walkRows);
        walkWeaponDRRect = new Rect(x + walkWeaponXOffset, y + walkWeaponYOffset, x + walkWeaponXOffset + (walkWeaponWidth / walkColumns), y + walkWeaponYOffset + (walkWeaponHeight / walkRows));
        walkParticleSRRect = new Rect(0, 0, walkParticleWidth / walkColumns, walkParticleHeight / walkRows);
        walkParticleDRRect = new Rect(x + walkParticleXOffset, y + walkParticleYOffset, x + walkParticleXOffset + (walkParticleWidth / walkColumns), y + walkParticleYOffset + (walkParticleHeight / walkRows));

        //Jump state
        jumpBodySRRect = new Rect(0, 0, jumpBodyWidth / jumpColumns, jumpBodyHeight / jumpRows);
        jumpBodyDRRect = new Rect(x + jumpBodyXOffset, y + jumpBodyYOffset, x + jumpBodyXOffset + (jumpBodyWidth / jumpColumns), y + jumpBodyYOffset + (jumpBodyHeight / jumpRows));
        jumpHeadSRRect = new Rect(0, 0, jumpHeadWidth / jumpColumns, jumpHeadHeight / jumpRows);
        jumpHeadDRRect = new Rect(x + jumpHeadXOffset, y + jumpHeadYOffset, x + jumpHeadXOffset + (jumpHeadWidth / jumpColumns), y + jumpHeadYOffset + (jumpHeadHeight / jumpRows));
        jumpArmSRRect = new Rect(0, 0, jumpArmWidth / jumpColumns, jumpArmHeight / jumpRows);
        jumpArmDRRect = new Rect(x + jumpArmXOffset, y + jumpArmYOffset, x + jumpArmXOffset + (jumpArmWidth / jumpColumns), y + jumpArmYOffset + (jumpArmHeight / jumpRows));
        jumpWeaponSRRect = new Rect(0, 0, jumpWeaponWidth / jumpColumns, jumpWeaponHeight / jumpRows);
        jumpWeaponDRRect = new Rect(x + jumpWeaponXOffset, y + jumpWeaponYOffset, x + jumpWeaponXOffset + (jumpWeaponWidth / jumpColumns), y + jumpWeaponYOffset + (jumpWeaponHeight / jumpRows));
        jumpParticleSRRect = new Rect(0, 0, jumpParticleWidth / jumpColumns, jumpParticleHeight / jumpRows);
        jumpParticleDRRect = new Rect(x + jumpParticleXOffset, y + jumpParticleYOffset, x + jumpParticleXOffset + (jumpParticleWidth / jumpColumns), y + jumpParticleYOffset + (jumpParticleHeight / jumpRows));

        //Hurt state
        hurtBodySRRect = new Rect(0, 0, hurtBodyWidth / hurtColumns, hurtBodyHeight / hurtRows);
        hurtBodyDRRect = new Rect(x + hurtBodyXOffset, y + hurtBodyYOffset, x + hurtBodyXOffset + (hurtBodyWidth / hurtColumns), y + hurtBodyYOffset + (hurtBodyHeight / hurtRows));
        hurtHeadSRRect = new Rect(0, 0, hurtHeadWidth / hurtColumns, hurtHeadHeight / hurtRows);
        hurtHeadDRRect = new Rect(x + hurtHeadXOffset, y + hurtHeadYOffset, x + hurtHeadXOffset + (hurtHeadWidth / hurtColumns), y + hurtHeadYOffset + (hurtHeadHeight / hurtRows));
        hurtWeaponSRRect = new Rect(0, 0, hurtWeaponWidth / hurtColumns, hurtWeaponHeight / hurtRows);
        hurtWeaponDRRect = new Rect(x + hurtWeaponXOffset, y + hurtWeaponYOffset, x + hurtWeaponXOffset + (hurtWeaponWidth / hurtColumns), y + hurtWeaponYOffset + (hurtWeaponHeight / hurtRows));
        hurtParticleSRRect = new Rect(0, 0, hurtParticleWidth / hurtColumns, hurtParticleHeight / hurtRows);
        hurtParticleDRRect = new Rect(x + hurtParticleXOffset, y + hurtParticleYOffset, x + hurtParticleXOffset + (hurtParticleWidth / hurtColumns), y + hurtParticleYOffset + (hurtParticleHeight / hurtRows));

        //Attack state
        attackBodySRRect = new Rect(0, 0, attackBodyWidth / attackColumns, attackBodyHeight / attackRows);
        attackBodyDRRect = new Rect(x + attackBodyXOffset, y + attackBodyYOffset, x + attackBodyXOffset + (attackBodyWidth / attackColumns), y + attackBodyYOffset + (attackBodyHeight / attackRows));
        attackHeadSRRect = new Rect(0, 0, attackHeadWidth / attackColumns, attackHeadHeight / attackRows);
        attackHeadDRRect = new Rect(x + attackHeadXOffset, y + attackHeadYOffset, x + attackHeadXOffset + (attackHeadWidth / attackColumns), y + attackHeadYOffset + (attackHeadHeight / attackRows));
        attackArmSRRect = new Rect(0, 0, attackArmWidth / attackColumns, attackArmHeight / attackRows);
        attackArmDRRect = new Rect(x + attackArmXOffset, y + attackArmYOffset, x + attackArmXOffset + (attackArmWidth / attackColumns), y + attackArmYOffset + (attackArmHeight / attackRows));
        attackWeaponSRRect = new Rect(0, 0, attackWeaponWidth / attackColumns, attackWeaponHeight / attackRows);
        attackWeaponDRRect = new Rect(x + attackWeaponXOffset, y + attackWeaponYOffset, x + attackWeaponXOffset + (attackWeaponWidth / attackColumns), y + attackWeaponYOffset + (attackWeaponHeight / attackRows));
        attackParticleSRRect = new Rect(0, 0, attackParticleWidth / attackColumns, attackParticleHeight / attackRows);
        attackParticleDRRect = new Rect(x + attackParticleXOffset, y + attackParticleYOffset, x + attackParticleXOffset + (attackParticleWidth / attackColumns), y + attackParticleYOffset + (attackParticleHeight / attackRows));

        //Current state, initialize as idle;
        spriteBodySR = idleBodySRRect;
        spriteBodyDR = idleBodyDRRect;
        spriteHeadSR = idleHeadSRRect;
        spriteHeadDR = idleHeadDRRect;
        spriteEyesSR = idleEyesSRRect;
        spriteEyesDR = idleEyesDRRect;
        spriteMouthSR = idleMouthSRRect;
        spriteMouthDR = idleMouthDRRect;
        spriteWeaponSR = idleWeaponSRRect;
        spriteWeaponDR = idleWeaponDRRect;
        spriteParticleSR = idleParticleSRRect;
        spriteParticleDR = idleParticleDRRect;

        //Set the size of the sprite
        setSpriteSize();
    }

    //hurt() method - Lowers player HP and loads hurt animation
    public void hurt() {
            if(direction == 1) { //If going right
                hp--; //Lower hp
                loadHurt(); //Load hurt animation
            }
            if(direction == -1) { //If going left
                hp--; //Lower hp (nested for symmetry in code)
                loadHurtLeft(); //Load left hurt animation
            }
    }

    //Update() Method - Handles player positioning, physics, and moving of Destination Rectangle
    @Override
    public void update() {

        //If the player has picked up more than 3 attack drops, their weapon is charged
        if(attackDrops >= 3) {
            chargedAttack = true;
        }

        //If the player has less than 3 attack drops, their weapon is not charged
        if(attackDrops < 3) {
            chargedAttack = false;
        }

        //If the player's weapon is charged
        if(chargedAttack) {
            damage = 3; //Their damage is 3
        }

        //If the player's weapon is not charged
        if(!chargedAttack) {
            damage = 1; //Their damage is 1
        }

        //Log.i("xSpeed", Integer.toString(xSpeed));
        //If the player is moving
        if(moving && !scroll) {
            //If the player is going right
            if(direction == 1) {// || direction == -1) {
                //If the player is not at maxSpeed
                if (xSpeed < maxSpeed) {
                    xSpeed += acceleration; //Accelerate to the right
                }
                //If the player is at or above maxSpeed
                if (xSpeed >= maxSpeed) {
                    xSpeed = maxSpeed; //Set xSpeed to maxSpeed
                }
            }
            //If the player is going left
            if(direction == -1) {
                //If the player is not at maxSpeed
                if (xSpeed > -maxSpeed) {
                    xSpeed -= acceleration; //Accelerate to the left
                }
                //If the player is at or above maxSpeed
                if (xSpeed <= -maxSpeed) {
                    xSpeed = -maxSpeed; //Set xSpeed to -maxSpeed
                }
            }
        }

        //If the player is not moving
        if(!moving && !scroll) {
            //If the player is still moving right
            if (xSpeed > 0) {
                xSpeed -= acceleration; //Decelerate
            }
            //If the player is still moving left
            if (xSpeed < 0) {
                xSpeed += acceleration; //Decelerate
            }
            //If the player speed is 0
            if (xSpeed == 0) {
                xSpeed = 0; //Set to 0
            }
        }

        //If the player is past the center of the screen and the background has some scroll left
        if(direction == 1 && x+(spriteWidth/2) >= (Constants.SCREEN_WIDTH/2) && Background.x < (Background.backgroundWidth-Constants.SCREEN_WIDTH)){
            //If speed needs to be captured
            if(captureSpeed) {
                xSpeed = 0; //Stop moving the sprite horizontally, background will now scroll
                x = Constants.SCREEN_WIDTH/2-(spriteWidth/2); //Set player to middle of screen so background just scrolls
                captureSpeed = false; //Don't capture player speed
            }
                scroll = true; //Background can be scrolled
                GamePanel.background.scroll(); //Scroll the background
        }

        //If the player is going right and has reached the end of the map
        if(direction == 1 && Background.x >= Background.backgroundWidth-Constants.SCREEN_WIDTH) {
            //If speed does not need to be captured
            if(!captureSpeed) {
                xSpeed = maxSpeed; //Assign xSpeed to maxSpeed
                captureSpeed = true; //Capture player speed
            }
            scroll = false; //Background cannot be scrolled
        }


        //If the player is past the center of the screen and the background has some scroll left, follows same logic as above
        if(direction == -1 && x+(spriteWidth/2) <= (Constants.SCREEN_WIDTH/2) && Background.x > 0){
            //If speed needs to be captured
            if(captureSpeed) {
                xSpeed = 0; //Stop moving sprite horizontally, background will now scroll
                x = Constants.SCREEN_WIDTH/2-(spriteWidth/2); //Set player to middle of screen so background can scroll
                captureSpeed = false; //Don't capture player speed
            }
                GamePanel.background.scroll(); //Scroll background
                scroll = true; //Background can be scrolled
        }

        //If the player is going left and has reached the end of the map
        if(direction == -1 && Background.x == 0) {
            //If speed does not need to be captured
            if(!captureSpeed) {
                xSpeed = -maxSpeed; //Assign -maxSpeed to xSpeed
                captureSpeed = true; //Capture player speed
            }
            scroll = false; //Background cannot be scrolled
        }

        //If player goes outside left screen
        if(x < 0) {
            x = 0; //Set x to 0
            xSpeed = 0; //Kill velocity
        }

        //If player goes outside right screen
        if(x > Constants.SCREEN_WIDTH-spriteWidth) {
            x = Constants.SCREEN_WIDTH-(spriteWidth); //Set x to right side-spriteWidth
            xSpeed = 0; //Kill velocity
        }

        x += xSpeed; //Update player x position
        hitX = x+spriteWidth+swordWidth; //Update hitX position

        //If the player is jumping
        if(jumping) {

            //If player has reached jump altitude
            if (y <= (GamePanel.background.startY-jumpAmount)) {
                goDown = true; //Player can go down now
            }

            //If player is going up and has not reached jump altitude
            if(!goDown && y >= (GamePanel.background.startY-jumpAmount)) {
                ySpeed -= jumpAcceleration; //Accelerate ySpeed
            }

            //If player is going down and they have not yet reached the ground
            if(goDown && y < GamePanel.background.startY) {
                ySpeed += gravity; //Apply gravity
            }

            //If player is going down and has reached the ground
            if(goDown && y >= GamePanel.background.startY) {
                goDown = false; //Player no longer needs to go down
                y = GamePanel.background.startY; //Set y to match ground
                ySpeed = 0; //Set ySpeed to 0
                jumping = false; //Player is no longer jumping
                captureState = true; //Can capture the state again

                if(!attacking) {
                    //If the player was walking right before jumping
                    if (previousState.equals("WalkRight")) {
                        loadWalk(); //Resuming walk right
                    }

                    //If the player was idle right before jumping
                    if (previousState.equals("IdleRight")) {
                        loadIdle(); //Resume idle right
                    }

                    //If the player was walking left before jumping
                    if (previousState.equals("WalkLeft")) {
                        loadWalkLeft(); //Resume walk left
                    }

                    //If the player was idle left before jumping
                    if (previousState.equals("IdleLeft")) {
                        loadIdleLeft(); //Resume idle left
                    }
                }
            }
            y += ySpeed; //Update player y position
        }

        //If the player state is IdleRight or IdleLeft, update the Destination Rectangles for the new player position with offsets
        if(state.equals("IdleRight") || state.equals("IdleLeft")) {
            spriteBodyDR.left = x+idleBodyXOffset;
            spriteBodyDR.top = y+idleBodyYOffset;
            spriteHeadDR.left = x+idleHeadXOffset;
            spriteHeadDR.top = y+idleHeadYOffset;
            spriteEyesDR.left = x+idleEyesXOffset;
            spriteEyesDR.top = y+idleEyesYOffset;
            spriteMouthDR.left = x+idleMouthXOffset;
            spriteMouthDR.top = y+idleMouthYOffset;
            spriteWeaponDR.left = x+idleWeaponXOffset;
            spriteWeaponDR.top = y+idleWeaponYOffset;
            spriteParticleDR.left = x+idleParticleXOffset;
            spriteParticleDR.top = y+idleParticleYOffset;
            spriteBodyDR.right = x + (idleBodyWidth/idleColumns)+idleBodyXOffset;
            spriteBodyDR.bottom = y + (idleBodyHeight/idleRows)+idleBodyYOffset;
            spriteHeadDR.right = x + (idleHeadWidth/idleColumns)+idleHeadXOffset;
            spriteHeadDR.bottom = y + (idleHeadHeight/idleRows)+idleHeadYOffset;
            spriteEyesDR.right = x + (idleEyesWidth/idleColumns)+idleEyesXOffset;
            spriteEyesDR.bottom = y + (idleEyesHeight/idleRows)+idleEyesYOffset;
            spriteMouthDR.right = x + (idleMouthWidth/idleColumns)+idleMouthXOffset;
            spriteMouthDR.bottom = y + (idleMouthHeight/idleRows)+idleMouthYOffset;
            spriteWeaponDR.right = x + (idleWeaponWidth/idleColumns)+idleWeaponXOffset;
            spriteWeaponDR.bottom = y + (idleWeaponHeight/idleRows)+idleWeaponYOffset;
            spriteParticleDR.right = x + (idleParticleWidth/idleColumns)+idleParticleXOffset;
            spriteParticleDR.bottom = y + (idleParticleHeight/idleRows)+idleParticleYOffset;
        }

        //If the player state is WalkRight or WalkLeft, update the Destination Rectangles for the new player position with offsets
        if(state.equals("WalkRight") || state.equals("WalkLeft")) {
            spriteBodyDR.left = x+walkBodyXOffset;
            spriteBodyDR.top = y+walkBodyYOffset;
            spriteHeadDR.left = x+walkHeadXOffset;
            spriteHeadDR.top = y+walkHeadYOffset;
            spriteEyesDR.left = x+walkEyesXOffset;
            spriteEyesDR.top = y+walkEyesYOffset;
            spriteMouthDR.left = x+walkMouthXOffset;
            spriteMouthDR.top = y+walkMouthYOffset;
            spriteWeaponDR.left = x+walkWeaponXOffset;
            spriteWeaponDR.top = y+walkWeaponYOffset;
            spriteParticleDR.left = x+walkParticleXOffset;
            spriteParticleDR.top = y+walkParticleYOffset;
            spriteBodyDR.right = x + (walkBodyWidth/walkColumns)+walkBodyXOffset;
            spriteBodyDR.bottom = y + (walkBodyHeight/walkRows)+walkBodyYOffset;
            spriteHeadDR.right = x + (walkHeadWidth/walkColumns)+walkHeadXOffset;
            spriteHeadDR.bottom = y + (walkHeadHeight/walkRows)+walkHeadYOffset;
            spriteEyesDR.right = x + (walkEyesWidth/walkColumns)+walkEyesXOffset;
            spriteEyesDR.bottom = y + (walkEyesHeight/walkRows)+walkEyesYOffset;
            spriteMouthDR.right = x + (walkMouthWidth/walkColumns)+walkMouthXOffset;
            spriteMouthDR.bottom = y + (walkMouthHeight/walkRows)+walkMouthYOffset;
            spriteWeaponDR.right = x + (walkWeaponWidth/walkColumns)+walkWeaponXOffset;
            spriteWeaponDR.bottom = y + (walkWeaponHeight/walkRows)+walkWeaponYOffset;
            spriteParticleDR.right = x + (walkParticleWidth/walkColumns)+walkParticleXOffset;
            spriteParticleDR.bottom = y + (walkParticleHeight/walkRows)+walkParticleYOffset;
        }

        //If the player state is AttackRight or AttackLeft, update the Destination Rectangles for the new player position with offsets
        if(state.equals("AttackRight") || state.equals("AttackLeft")) {
            spriteBodyDR.left = x+attackBodyXOffset;
            spriteBodyDR.top = y+attackBodyYOffset;
            spriteHeadDR.left = x+attackHeadXOffset;
            spriteHeadDR.top = y+attackHeadYOffset;
            spriteArmDR.left = x+attackArmXOffset;
            spriteArmDR.top = y+attackArmYOffset;
            spriteWeaponDR.left = x+attackWeaponXOffset;
            spriteWeaponDR.top = y+attackWeaponYOffset;
            spriteParticleDR.left = x+attackParticleXOffset;
            spriteParticleDR.top = y+attackParticleYOffset;
            spriteBodyDR.right = x + (attackBodyWidth/attackColumns)+attackBodyXOffset;
            spriteBodyDR.bottom = y + (attackBodyHeight/attackRows)+attackBodyYOffset;
            spriteHeadDR.right = x + (attackHeadWidth/attackColumns)+attackHeadXOffset;
            spriteHeadDR.bottom = y + (attackHeadHeight/attackRows)+attackHeadYOffset;
            spriteArmDR.right = x + (attackArmWidth/attackColumns)+attackArmXOffset;
            spriteArmDR.bottom = y + (attackArmHeight/attackRows)+attackArmYOffset;
            spriteWeaponDR.right = x + (attackWeaponWidth/attackColumns)+attackWeaponXOffset;
            spriteWeaponDR.bottom = y + (attackWeaponHeight/attackRows)+attackWeaponYOffset;
            spriteParticleDR.right = x + (attackParticleWidth/attackColumns)+attackParticleXOffset;
            spriteParticleDR.bottom = y + (attackParticleHeight/attackRows)+attackParticleYOffset;
        }

        //If the player state is JumpRight or JumpLeft, update the Destination Rectangles for the new player position with offsets
        if(state.equals("JumpRight") || state.equals(("JumpLeft"))) {
            spriteBodyDR.left = x+jumpBodyXOffset;
            spriteBodyDR.top = y+jumpBodyYOffset;
            spriteHeadDR.left = x+jumpHeadXOffset;
            spriteHeadDR.top = y+jumpHeadYOffset;
            spriteArmDR.left = x+jumpArmXOffset;
            spriteArmDR.top = y+jumpArmYOffset;
            spriteWeaponDR.left = x+jumpWeaponXOffset;
            spriteWeaponDR.top = y+jumpWeaponYOffset;
            spriteParticleDR.left = x+jumpParticleXOffset;
            spriteParticleDR.top = y+jumpParticleYOffset;
            spriteBodyDR.right = x + (jumpBodyWidth/jumpColumns)+jumpBodyXOffset;
            spriteBodyDR.bottom = y + (jumpBodyHeight/jumpRows)+jumpBodyYOffset;
            spriteHeadDR.right = x + (jumpHeadWidth/jumpColumns)+jumpHeadXOffset;
            spriteHeadDR.bottom = y + (jumpHeadHeight/jumpRows)+jumpHeadYOffset;
            spriteArmDR.right = x + (jumpArmWidth/jumpColumns)+jumpArmXOffset;
            spriteArmDR.bottom = y + (jumpArmHeight/jumpRows)+jumpArmYOffset;
            spriteWeaponDR.right = x + (jumpWeaponWidth/jumpColumns)+jumpWeaponXOffset;
            spriteWeaponDR.bottom = y + (jumpWeaponHeight/jumpRows)+jumpWeaponYOffset;
            spriteParticleDR.right = x + (jumpParticleWidth/jumpColumns)+jumpParticleXOffset;
            spriteParticleDR.bottom = y + (jumpParticleHeight/jumpRows)+jumpParticleYOffset;
        }

        //If the player state is HurtRight or HurtLeft, update the Destination Rectangles for the new player position with offsets
        if(state.equals("HurtRight") || state.equals("HurtLeft")) {
            spriteBodyDR.left = x+hurtBodyXOffset;
            spriteBodyDR.top = y+hurtBodyYOffset;
            spriteHeadDR.left = x+hurtHeadXOffset;
            spriteHeadDR.top = y+hurtHeadYOffset;
            spriteWeaponDR.left = x+hurtWeaponXOffset;
            spriteWeaponDR.top = y+hurtWeaponYOffset;
            spriteParticleDR.left = x+hurtParticleXOffset;
            spriteParticleDR.top = y+hurtParticleYOffset;
            spriteBodyDR.right = x + (hurtBodyWidth/hurtColumns)+hurtBodyXOffset;
            spriteBodyDR.bottom = y + (hurtBodyHeight/hurtRows)+hurtBodyYOffset;
            spriteHeadDR.right = x + (hurtHeadWidth/hurtColumns)+hurtHeadXOffset;
            spriteHeadDR.bottom = y + (hurtHeadHeight/hurtRows)+hurtHeadYOffset;
            spriteWeaponDR.right = x + (hurtWeaponWidth/hurtColumns)+hurtWeaponXOffset;
            spriteWeaponDR.bottom = y + (hurtWeaponHeight/hurtRows)+hurtWeaponYOffset;
            spriteParticleDR.right = x + (hurtParticleWidth/hurtColumns)+hurtParticleXOffset;
            spriteParticleDR.bottom = y + (hurtParticleHeight/hurtRows)+hurtParticleYOffset;
        }
        animate(); //Animate
    }

    //draw(Canvas canvas) method - Accepts a Canvas object and draws bitmaps to the Canvas
    @Override
    public void draw(Canvas canvas) {

        //If current state is IdleRight
        if(state.equals("IdleRight")) {
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroIdleBody"), spriteBodySR, spriteBodyDR, null);
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroIdleHead"), spriteHeadSR, spriteHeadDR, null);
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroIdleMouthSmile"), spriteMouthSR, spriteMouthDR, null);
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroIdleSword"), spriteWeaponSR, spriteWeaponDR, null);
            //Depending on number of kills, update particle effect on sword
            if(kills <= 1) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroIdleSwordParticle1"), spriteParticleSR, spriteParticleDR, null);
            }
            if(kills > 1 && kills <= 2) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroIdleSwordParticle2"), spriteParticleSR, spriteParticleDR, null);
            }
            if(kills > 2 && kills <= 3) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroIdleSwordParticle3"), spriteParticleSR, spriteParticleDR, null);
            }
            if(kills > 3 && kills <= 4) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroIdleSwordParticle4"), spriteParticleSR, spriteParticleDR, null);
            }
        }

        //If current state is WalkRight, follows same logic as above
        if(state.equals("WalkRight")) {
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroWalkBody"), spriteBodySR, spriteBodyDR, null);
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroWalkHead"), spriteHeadSR, spriteHeadDR, null);
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroWalkMouthSmile"), spriteMouthSR, spriteMouthDR, null);
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroWalkSword"), spriteWeaponSR, spriteWeaponDR, null);
            if(kills <= 1) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroWalkSwordParticle1"), spriteParticleSR, spriteParticleDR, null);
            }
            if(kills > 1 && kills <= 2) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroWalkSwordParticle2"), spriteParticleSR, spriteParticleDR, null);
            }
            if(kills > 2 && kills <= 3) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroWalkSwordParticle3"), spriteParticleSR, spriteParticleDR, null);
            }
            if(kills > 3 && kills <= 4) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroWalkSwordParticle4"), spriteParticleSR, spriteParticleDR, null);
            }
        }

        //If current state is HurtRight, follows same logic as above
        if(state.equals("HurtRight")) {
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroHurtBody"), spriteBodySR, spriteBodyDR, null);
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroHurtHead"), spriteHeadSR, spriteHeadDR, null);
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroHurtSword"), spriteWeaponSR, spriteWeaponDR, null);
            if(kills <= 1) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroHurtSwordParticle1"), spriteParticleSR, spriteParticleDR, null);
            }
            if(kills > 1 && kills <= 2) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroHurtSwordParticle2"), spriteParticleSR, spriteParticleDR, null);
            }
            if(kills > 2 && kills <= 3) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroHurtSwordParticle3"), spriteParticleSR, spriteParticleDR, null);
            }
            if(kills > 3 && kills <= 4) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroHurtSwordParticle4"), spriteParticleSR, spriteParticleDR, null);
            }
        }

        //If current state is AttackRight, follows same logic as above
        if(state.equals("AttackRight")) {
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroAttackBody"), spriteBodySR, spriteBodyDR, null);
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroAttackHead"), spriteHeadSR, spriteHeadDR, null);
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroAttackArm"), spriteArmSR, spriteArmDR, null);
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroAttackSword"), spriteWeaponSR, spriteWeaponDR, null);
            if(kills <= 1) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroAttackSwordParticle1"), spriteParticleSR, spriteParticleDR, null);
            }
            if(kills > 1 && kills <= 2) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroAttackSwordParticle2"), spriteParticleSR, spriteParticleDR, null);
            }
            if(kills > 2 && kills <= 3) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroAttackSwordParticle3"), spriteParticleSR, spriteParticleDR, null);
            }
            if(kills > 3 && kills <= 4) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroAttackSwordParticle4"), spriteParticleSR, spriteParticleDR, null);
            }

            //If the player goes left during drawing
            if(direction == -1) {
                state = "AttackLeft"; //Update state
            }
        }

        //If current state is JumpRight, follows same logic as above
        if(state.equals("JumpRight")) {
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroJumpBody"), spriteBodySR, spriteBodyDR, null);
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroJumpHead"), spriteHeadSR, spriteHeadDR, null);
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroJumpArm"), spriteArmSR, spriteArmDR, null);
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroJumpSword"), spriteWeaponSR, spriteWeaponDR, null);
            if(kills <= 1) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroJumpSwordParticle1"), spriteParticleSR, spriteParticleDR, null);
            }
            if(kills > 1 && kills <= 2) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroJumpSwordParticle2"), spriteParticleSR, spriteParticleDR, null);
            }
            if(kills > 2 && kills <= 3) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroJumpSwordParticle3"), spriteParticleSR, spriteParticleDR, null);
            }
            if(kills > 3 && kills <= 4) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroJumpSwordParticle4"), spriteParticleSR, spriteParticleDR, null);
            }

            //If the player goes left during drawing
            if(direction == -1) {
                state = "JumpLeft"; //Update state so player flips mid jump
            }
        }


        //If current state is IdleLeft, follows same logic as above
        if(state.equals("IdleLeft")) {
            flip(canvas); //Flip the canvas so it is going the other direction
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroIdleBody"), spriteBodySR, spriteBodyDR, null);
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroIdleHead"), spriteHeadSR, spriteHeadDR, null);
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroIdleMouthSmile"), spriteMouthSR, spriteMouthDR, null);
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroIdleSword"), spriteWeaponSR, spriteWeaponDR, null);
            if(kills <= 1) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroIdleSwordParticle1"), spriteParticleSR, spriteParticleDR, null);
            }
            if(kills > 1 && kills <= 2) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroIdleSwordParticle2"), spriteParticleSR, spriteParticleDR, null);
            }
            if(kills > 2 && kills <= 3) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroIdleSwordParticle3"), spriteParticleSR, spriteParticleDR, null);
            }
            if(kills > 3 && kills <= 4) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroIdleSwordParticle4"), spriteParticleSR, spriteParticleDR, null);
            }
            unFlip(canvas);
        }

        //If current state is WalkLeft
        if(state.equals("WalkLeft")) {
            flip(canvas); //Flip the canvas so it is going the other direction
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroWalkBody"), spriteBodySR, spriteBodyDR, null);
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroWalkHead"), spriteHeadSR, spriteHeadDR, null);
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroWalkMouthSmile"), spriteMouthSR, spriteMouthDR, null);
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroWalkSword"), spriteWeaponSR, spriteWeaponDR, null);
            if(kills <= 1) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroWalkSwordParticle1"), spriteParticleSR, spriteParticleDR, null);
            }
            if(kills > 1 && kills <= 2) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroWalkSwordParticle2"), spriteParticleSR, spriteParticleDR, null);
            }
            if(kills > 2 && kills <= 3) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroWalkSwordParticle3"), spriteParticleSR, spriteParticleDR, null);
            }
            if(kills > 3 && kills <= 4) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroWalkSwordParticle4"), spriteParticleSR, spriteParticleDR, null);
            }
            unFlip(canvas);
        }

        //If current state is HurtLeft
        if(state.equals("HurtLeft")) {
            flip(canvas); //Flip the canvas so it is going the other direction
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroHurtBody"), spriteBodySR, spriteBodyDR, null);
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroHurtHead"), spriteHeadSR, spriteHeadDR, null);
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroHurtSword"), spriteWeaponSR, spriteWeaponDR, null);
            if(kills <= 1) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroHurtSwordParticle1"), spriteParticleSR, spriteParticleDR, null);
            }
            if(kills > 1 && kills <= 2) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroHurtSwordParticle2"), spriteParticleSR, spriteParticleDR, null);
            }
            if(kills > 2 && kills <= 3) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroHurtSwordParticle3"), spriteParticleSR, spriteParticleDR, null);
            }
            if(kills > 3 && kills <= 4) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroHurtSwordParticle4"), spriteParticleSR, spriteParticleDR, null);
            }
            unFlip(canvas);
        }

        //If current state is AttackLeft
        if(state.equals("AttackLeft")) {
            flip(canvas); //Flip the canvas so it is going the other direction
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroAttackBody"), spriteBodySR, spriteBodyDR, null);
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroAttackHead"), spriteHeadSR, spriteHeadDR, null);
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroAttackArm"), spriteArmSR, spriteArmDR, null);
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroAttackSword"), spriteWeaponSR, spriteWeaponDR, null);
            if(kills <= 1) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroAttackSwordParticle1"), spriteParticleSR, spriteParticleDR, null);
            }
            if(kills > 1 && kills <= 2) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroAttackSwordParticle2"), spriteParticleSR, spriteParticleDR, null);
            }
            if(kills > 2 && kills <= 3) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroAttackSwordParticle3"), spriteParticleSR, spriteParticleDR, null);
            }
            if(kills > 3 && kills <= 4) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroAttackSwordParticle4"), spriteParticleSR, spriteParticleDR, null);
            }
            unFlip(canvas);

            //If the player goes right during drawing
            if(direction == 1) {
                state = "AttackRight";
            }
        }

        //If current state is JumpLeft
        if(state.equals("JumpLeft")) {
            flip(canvas); //Flip the canvas so it is going the other direction
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroJumpBody"), spriteBodySR, spriteBodyDR, null);
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroJumpHead"), spriteHeadSR, spriteHeadDR, null);
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroJumpArm"), spriteArmSR, spriteArmDR, null);
            canvas.drawBitmap( GameActivity.getBitmapFromCache("heroJumpSword"), spriteWeaponSR, spriteWeaponDR, null);
            if(kills <= 1) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroJumpSwordParticle1"), spriteParticleSR, spriteParticleDR, null);
            }
            if(kills > 1 && kills <= 2) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroJumpSwordParticle2"), spriteParticleSR, spriteParticleDR, null);
            }
            if(kills > 2 && kills <= 3) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroJumpSwordParticle3"), spriteParticleSR, spriteParticleDR, null);
            }
            if(kills > 3 && kills <= 4) {
                canvas.drawBitmap(GameActivity.getBitmapFromCache("heroJumpSwordParticle4"), spriteParticleSR, spriteParticleDR, null);
            }
            unFlip(canvas);

            //If the player goes right during drawing
            if(direction == 1){
                state = "JumpRight"; //Update state so player flips mid jump
            }
        }
    }

    //updateRectangle(String s) method - Accepts a string for the state and resets the Source and Destination Rectangles to first frame. Acceptable input ex. "idle" "walk"
    private void resetRectangles(String s) {
        //If idle
        if(s.equals("idle")) {
            //Update body SR and DR
            idleBodySRRect.left = 0;
            idleBodySRRect.top = 0;
            idleBodySRRect.right = (idleBodyWidth/idleColumns)/factor;
            idleBodySRRect.bottom = (idleBodyHeight/idleRows)/factor;
            idleBodyDRRect.left = x+idleBodyXOffset;
            idleBodyDRRect.top = y+idleBodyYOffset;
            idleBodyDRRect.right = x+idleBodyXOffset+(idleBodyWidth/idleColumns);
            idleBodyDRRect.bottom = y+idleBodyYOffset+(idleBodyHeight/idleRows);

            //Update idle SR and DR
            idleHeadSRRect.left = 0;
            idleHeadSRRect.top = 0;
            idleHeadSRRect.right = (idleHeadWidth/idleColumns)/factor;
            idleHeadSRRect.bottom = (idleHeadHeight/idleRows)/factor;
            idleHeadDRRect.left = x+idleHeadXOffset;
            idleHeadDRRect.top = y+idleHeadYOffset;
            idleHeadDRRect.right = x+idleHeadXOffset+(idleHeadWidth/idleColumns);
            idleHeadDRRect.bottom = y+idleHeadYOffset+(idleHeadHeight/idleRows);

            //Update eyes SR and DR
            idleEyesSRRect.left = 0;
            idleEyesSRRect.top = 0;
            idleEyesSRRect.right = (idleEyesWidth/idleColumns)/factor;
            idleEyesSRRect.bottom = (idleEyesHeight/idleRows)/factor;
            idleEyesDRRect.left = x+idleEyesXOffset;
            idleEyesDRRect.top = y+idleEyesYOffset;
            idleEyesDRRect.right = x+idleEyesXOffset+(idleEyesWidth/idleColumns);
            idleEyesDRRect.bottom = y+idleEyesYOffset+(idleEyesHeight/idleRows);

            //Update mouth SR and DR
            idleMouthSRRect.left = 0;
            idleMouthSRRect.top = 0;
            idleMouthSRRect.right = (idleMouthWidth/idleColumns)/factor;
            idleMouthSRRect.bottom = (idleMouthHeight/idleRows)/factor;
            idleMouthDRRect.left = x+idleMouthXOffset;
            idleMouthDRRect.top = y+idleMouthYOffset;
            idleMouthDRRect.right = x+idleMouthXOffset+(idleMouthWidth/idleColumns);
            idleMouthDRRect.bottom = y+idleMouthYOffset+(idleMouthHeight/idleRows);

            //Update weapon SR and DR
            idleWeaponSRRect.left = 0;
            idleWeaponSRRect.top = 0;
            idleWeaponSRRect.right = (idleWeaponWidth/idleColumns)/factor;
            idleWeaponSRRect.bottom = (idleWeaponHeight/idleRows)/factor;
            idleWeaponDRRect.left = x+idleWeaponXOffset;
            idleWeaponDRRect.top = y+idleWeaponYOffset;
            idleWeaponDRRect.right = x+idleWeaponXOffset+(idleWeaponWidth/idleColumns);
            idleWeaponDRRect.bottom = y+idleWeaponYOffset+(idleWeaponHeight/idleRows);

            //Update particle SR and DR
            idleParticleSRRect.left = 0;
            idleParticleSRRect.top = 0;
            idleParticleSRRect.right = (idleParticleWidth/idleColumns)/factor;
            idleParticleSRRect.bottom = (idleParticleHeight/idleRows)/factor;
            idleParticleDRRect.left = x+idleParticleXOffset;
            idleParticleDRRect.top = y+idleParticleYOffset;
            idleParticleDRRect.right = x+idleParticleXOffset+(idleParticleWidth/idleColumns);
            idleParticleDRRect.bottom = y+idleParticleYOffset+(idleParticleHeight/idleRows);
        }
        //If walk
        if(s.equals("walk")) {
            walkBodySRRect.left = 0;
            walkBodySRRect.top = 0;
            walkBodySRRect.right = (walkBodyWidth/walkColumns)/factor;
            walkBodySRRect.bottom = (walkBodyHeight/walkRows)/factor;
            walkBodyDRRect.left = x+walkBodyXOffset;
            walkBodyDRRect.top = y+walkBodyYOffset;
            walkBodyDRRect.right = x+walkBodyXOffset+(walkBodyWidth/walkColumns);
            walkBodyDRRect.bottom = y+walkBodyYOffset+(walkBodyHeight/walkRows);

            walkHeadSRRect.left = 0;
            walkHeadSRRect.top = 0;
            walkHeadSRRect.right = (walkHeadWidth/walkColumns)/factor;
            walkHeadSRRect.bottom = (walkHeadHeight/walkRows)/factor;
            walkHeadDRRect.left = x+walkHeadXOffset;
            walkHeadDRRect.top = y+walkHeadYOffset;
            walkHeadDRRect.right = x+walkHeadXOffset+(walkHeadWidth/walkColumns);
            walkHeadDRRect.bottom = y+walkHeadYOffset+(walkHeadHeight/walkRows);

            walkEyesSRRect.left = 0;
            walkEyesSRRect.top = 0;
            walkEyesSRRect.right = (walkEyesWidth/walkColumns)/factor;
            walkEyesSRRect.bottom = (walkEyesHeight/walkRows)/factor;
            walkEyesDRRect.left = x+walkEyesXOffset;
            walkEyesDRRect.top = y+walkEyesYOffset;
            walkEyesDRRect.right = x+walkEyesXOffset+(walkEyesWidth/walkColumns);
            walkEyesDRRect.bottom = y+walkEyesYOffset+(walkEyesHeight/walkRows);

            walkMouthSRRect.left = 0;
            walkMouthSRRect.top = 0;
            walkMouthSRRect.right = (walkMouthWidth/walkColumns)/factor;
            walkMouthSRRect.bottom = (walkMouthHeight/walkRows)/factor;
            walkMouthDRRect.left = x+walkMouthXOffset;
            walkMouthDRRect.top = y+walkMouthYOffset;
            walkMouthDRRect.right = x+walkMouthXOffset+(walkMouthWidth/walkColumns);
            walkMouthDRRect.bottom = y+walkMouthYOffset+(walkMouthHeight/walkRows);

            walkWeaponSRRect.left = 0;
            walkWeaponSRRect.top = 0;
            walkWeaponSRRect.right = (walkWeaponWidth/walkColumns)/factor;
            walkWeaponSRRect.bottom = (walkWeaponHeight/walkRows)/factor;
            walkWeaponDRRect.left = x+walkWeaponXOffset;
            walkWeaponDRRect.top = y+walkWeaponYOffset;
            walkWeaponDRRect.right = x+walkWeaponXOffset+(walkWeaponWidth/walkColumns);
            walkWeaponDRRect.bottom = y+walkWeaponYOffset+(walkWeaponHeight/walkRows);

            walkParticleSRRect.left = 0;
            walkParticleSRRect.top = 0;
            walkParticleSRRect.right = (walkParticleWidth/walkColumns)/factor;
            walkParticleSRRect.bottom = (walkParticleHeight/walkRows)/factor;
            walkParticleDRRect.left = x+walkParticleXOffset;
            walkParticleDRRect.top = y+walkParticleYOffset;
            walkParticleDRRect.right = x+walkParticleXOffset+(walkParticleWidth/walkColumns);
            walkParticleDRRect.bottom = y+walkParticleYOffset+(walkParticleHeight/walkRows);
        }

        //If jump
        if(s.equals("jump")) {
            jumpBodySRRect.left = 0;
            jumpBodySRRect.top = 0;
            jumpBodySRRect.right = (jumpBodyWidth/jumpColumns)/factor;
            jumpBodySRRect.bottom = (jumpBodyHeight/jumpRows)/factor;
            jumpBodyDRRect.left = x+jumpBodyXOffset;
            jumpBodyDRRect.top = y+jumpBodyYOffset;
            jumpBodyDRRect.right = x+jumpBodyXOffset+(jumpBodyWidth/jumpColumns);
            jumpBodyDRRect.bottom = y+jumpBodyYOffset+(jumpBodyHeight/jumpRows);

            jumpHeadSRRect.left = 0;
            jumpHeadSRRect.top = 0;
            jumpHeadSRRect.right = (jumpHeadWidth/jumpColumns)/factor;
            jumpHeadSRRect.bottom = (jumpHeadHeight/jumpRows)/factor;
            jumpHeadDRRect.left = x+jumpHeadXOffset;
            jumpHeadDRRect.top = y+jumpHeadYOffset;
            jumpHeadDRRect.right = x+jumpHeadXOffset+(jumpHeadWidth/jumpColumns);
            jumpHeadDRRect.bottom = y+jumpHeadYOffset+(jumpHeadHeight/jumpRows);

            jumpArmSRRect.left = 0;
            jumpArmSRRect.top = 0;
            jumpArmSRRect.right = (jumpArmWidth/jumpColumns)/factor;
            jumpArmSRRect.bottom = (jumpArmHeight/jumpRows)/factor;
            jumpArmDRRect.left = x+jumpArmXOffset;
            jumpArmDRRect.top = y+jumpArmYOffset;
            jumpArmDRRect.right = x+jumpArmXOffset+(jumpArmWidth/jumpColumns);
            jumpArmDRRect.bottom = y+jumpArmYOffset+(jumpArmHeight/jumpRows);

            jumpWeaponSRRect.left = 0;
            jumpWeaponSRRect.top = 0;
            jumpWeaponSRRect.right = (jumpWeaponWidth/jumpColumns)/factor;
            jumpWeaponSRRect.bottom = (jumpWeaponHeight/jumpRows)/factor;
            jumpWeaponDRRect.left = x+jumpWeaponXOffset;
            jumpWeaponDRRect.top = y+jumpWeaponYOffset;
            jumpWeaponDRRect.right = x+jumpWeaponXOffset+(jumpWeaponWidth/jumpColumns);
            jumpWeaponDRRect.bottom = y+jumpWeaponYOffset+(jumpWeaponHeight/jumpRows);

            jumpParticleSRRect.left = 0;
            jumpParticleSRRect.top = 0;
            jumpParticleSRRect.right = (jumpParticleWidth/jumpColumns)/factor;
            jumpParticleSRRect.bottom = (jumpParticleHeight/jumpRows)/factor;
            jumpParticleDRRect.left = x+jumpParticleXOffset;
            jumpParticleDRRect.top = y+jumpParticleYOffset;
            jumpParticleDRRect.right = x+jumpParticleXOffset+(jumpParticleWidth/jumpColumns);
            jumpParticleDRRect.bottom = y+jumpParticleYOffset+(jumpParticleHeight/jumpRows);
        }

        //If attack
        if(s.equals("attack")) {
            attackBodySRRect.left = 0;
            attackBodySRRect.top = 0;
            attackBodySRRect.right = (attackBodyWidth/attackColumns)/factor;
            attackBodySRRect.bottom = (attackBodyHeight/attackRows)/factor;
            attackBodyDRRect.left = x+attackBodyXOffset;
            attackBodyDRRect.top = y+attackBodyYOffset;
            attackBodyDRRect.right = x+attackBodyXOffset+(attackBodyWidth/attackColumns);
            attackBodyDRRect.bottom = y+attackBodyYOffset+(attackBodyHeight/attackRows);

            attackHeadSRRect.left = 0;
            attackHeadSRRect.top = 0;
            attackHeadSRRect.right = (attackHeadWidth/attackColumns)/factor;
            attackHeadSRRect.bottom = (attackHeadHeight/attackRows)/factor;
            attackHeadDRRect.left = x+attackHeadXOffset;
            attackHeadDRRect.top = y+attackHeadYOffset;
            attackHeadDRRect.right = x+attackHeadXOffset+(attackHeadWidth/attackColumns);
            attackHeadDRRect.bottom = y+attackHeadYOffset+(attackHeadHeight/attackRows);

            attackArmSRRect.left = 0;
            attackArmSRRect.top = 0;
            attackArmSRRect.right = (attackArmWidth/attackColumns)/factor;
            attackArmSRRect.bottom = (attackArmHeight/attackRows)/factor;
            attackArmDRRect.left = x+attackArmXOffset;
            attackArmDRRect.top = y+attackArmYOffset;
            attackArmDRRect.right = x+attackArmXOffset+(attackArmWidth/attackColumns);
            attackArmDRRect.bottom = y+attackArmYOffset+(attackArmHeight/attackRows);

            attackWeaponSRRect.left = 0;
            attackWeaponSRRect.top = 0;
            attackWeaponSRRect.right = (attackWeaponWidth/attackColumns)/factor;
            attackWeaponSRRect.bottom = (attackWeaponHeight/attackRows)/factor;
            attackWeaponDRRect.left = x+attackWeaponXOffset;
            attackWeaponDRRect.top = y+attackWeaponYOffset;
            attackWeaponDRRect.right = x+attackWeaponXOffset+(attackWeaponWidth/attackColumns);
            attackWeaponDRRect.bottom = y+attackWeaponYOffset+(attackWeaponHeight/attackRows);

            attackParticleSRRect.left = 0;
            attackParticleSRRect.top = 0;
            attackParticleSRRect.right = (attackParticleWidth/attackColumns)/factor;
            attackParticleSRRect.bottom = (attackParticleHeight/attackRows)/factor;
            attackParticleDRRect.left = x+attackParticleXOffset;
            attackParticleDRRect.top = y+attackParticleYOffset;
            attackParticleDRRect.right = x+attackParticleXOffset+(attackParticleWidth/attackColumns);
            attackParticleDRRect.bottom = y+attackParticleYOffset+(attackParticleHeight/attackRows);
        }

        //If hurt
        if(s.equals("hurt")) {
            hurtBodySRRect.left = 0;
            hurtBodySRRect.top = 0;
            hurtBodySRRect.right = (hurtBodyWidth/hurtColumns)/factor;
            hurtBodySRRect.bottom = (hurtBodyHeight/hurtRows)/factor;
            hurtBodyDRRect.left = x+hurtBodyXOffset;
            hurtBodyDRRect.top = y+hurtBodyYOffset;
            hurtBodyDRRect.right = x+hurtBodyXOffset+(hurtBodyWidth/hurtColumns);
            hurtBodyDRRect.bottom = y+hurtBodyYOffset+(hurtBodyHeight/hurtRows);

            hurtHeadSRRect.left = 0;
            hurtHeadSRRect.top = 0;
            hurtHeadSRRect.right = (hurtHeadWidth/hurtColumns)/factor;
            hurtHeadSRRect.bottom = (hurtHeadHeight/hurtRows)/factor;
            hurtHeadDRRect.left = x+hurtHeadXOffset;
            hurtHeadDRRect.top = y+hurtHeadYOffset;
            hurtHeadDRRect.right = x+hurtHeadXOffset+(hurtHeadWidth/hurtColumns);
            hurtHeadDRRect.bottom = y+hurtHeadYOffset+(hurtHeadHeight/hurtRows);

            hurtWeaponSRRect.left = 0;
            hurtWeaponSRRect.top = 0;
            hurtWeaponSRRect.right = (hurtWeaponWidth/hurtColumns)/factor;
            hurtWeaponSRRect.bottom = (hurtWeaponHeight/hurtRows)/factor;
            hurtWeaponDRRect.left = x+hurtWeaponXOffset;
            hurtWeaponDRRect.top = y+hurtWeaponYOffset;
            hurtWeaponDRRect.right = x+hurtWeaponXOffset+(hurtWeaponWidth/hurtColumns);
            hurtWeaponDRRect.bottom = y+hurtWeaponYOffset+(hurtWeaponHeight/hurtRows);

            hurtParticleSRRect.left = 0;
            hurtParticleSRRect.top = 0;
            hurtParticleSRRect.right = (hurtParticleWidth/hurtColumns)/factor;
            hurtParticleSRRect.bottom = (hurtParticleHeight/hurtRows)/factor;
            hurtParticleDRRect.left = x+hurtParticleXOffset;
            hurtParticleDRRect.top = y+hurtParticleYOffset;
            hurtParticleDRRect.right = x+hurtParticleXOffset+(hurtParticleWidth/hurtColumns);
            hurtParticleDRRect.bottom = y+hurtParticleYOffset+(hurtParticleHeight/hurtRows);
        }
    }

    //stateChanged(String s) method - Accepts a String variable and changes the Source and Destination Rectangle to the corresponding state. Ex. "IdleRight" resets the idle SR and DRs and sets them as the current SR and DRs
    private void stateChanged(String s) {

        //If IdleRight or IdleLeft
        if(s.equals("IdleRight") || s.equals("IdleLeft")) {

            //If IdleRight
            if(s.equals("IdleRight")) {
                state = "IdleRight"; //Update state
            }

            //If IdleLeft
            if(s.equals("IdleLeft")) {
                state = "IdleLeft"; //Update state
            }
            resetRectangles("idle"); //Reset the SR and DR

            //Assign reset SR and Drs to current SR and DR
            spriteBodySR = idleBodySRRect;
            spriteBodyDR = idleBodyDRRect;
            spriteHeadSR = idleHeadSRRect;
            spriteHeadDR = idleHeadDRRect;
            spriteEyesSR = idleEyesSRRect;
            spriteEyesDR = idleEyesDRRect;
            spriteMouthSR = idleMouthSRRect;
            spriteMouthDR = idleMouthDRRect;
            spriteWeaponSR = idleWeaponSRRect;
            spriteWeaponDR = idleWeaponDRRect;
            spriteParticleSR = idleParticleSRRect;
            spriteParticleDR = idleParticleDRRect;
            resetIdle(); //Reset idle frame
        }

        //If WalkRight or WalkLeft
        else if(s.equals("WalkRight") || s.equals("WalkLeft")) {

            //If WalkRight
            if(s.equals("WalkRight")) {
                state = "WalkRight"; //Update state
            }

            //If WalkLeft
            if(s.equals("WalkLeft")) {
                state = "WalkLeft"; //Update state
            }
            resetRectangles("walk"); //Reset the SR and DR

            //Assign reset SR and Drs to current SR and DR
            spriteBodySR = walkBodySRRect;
            spriteBodyDR = walkBodyDRRect;
            spriteHeadSR = walkHeadSRRect;
            spriteHeadDR = walkHeadDRRect;
            spriteEyesSR = walkEyesSRRect;
            spriteEyesDR = walkEyesDRRect;
            spriteMouthSR = walkMouthSRRect;
            spriteMouthDR = walkMouthDRRect;
            spriteWeaponSR = walkWeaponSRRect;
            spriteWeaponDR = walkWeaponDRRect;
            spriteParticleSR = walkParticleSRRect;
            spriteParticleDR = walkParticleDRRect;
            resetWalk(); //Reset walk frame
        }

        //If JumpRight or JumpLeft
        else if(s.equals("JumpRight") || s.equals("JumpLeft")) {

            //If JumpRight
            if(s.equals("JumpRight")) {
                state = "JumpRight"; //Update state
            }

            //If JumpLeft
            if(s.equals("JumpLeft")) {
                state = "JumpLeft"; //Update state
            }
            resetRectangles("jump"); //Reset the SR and DR

            //Assign reset SR and Drs to current SR and DR
            spriteBodySR = jumpBodySRRect;
            spriteBodyDR = jumpBodyDRRect;
            spriteHeadSR = jumpHeadSRRect;
            spriteHeadDR = jumpHeadDRRect;
            spriteArmSR = jumpArmSRRect;
            spriteArmDR = jumpArmDRRect;
            spriteWeaponSR = jumpWeaponSRRect;
            spriteWeaponDR = jumpWeaponDRRect;
            spriteParticleSR = jumpParticleSRRect;
            spriteParticleDR = jumpParticleDRRect;
            resetJump(); //Reset jump frame
        }

        //If HurtRight or HurtLeft
        else if(s.equals("HurtRight") || s.equals("HurtLeft")) {

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
            spriteBodySR = hurtBodySRRect;
            spriteBodyDR = hurtBodyDRRect;
            spriteHeadSR = hurtHeadSRRect;
            spriteHeadDR = hurtHeadDRRect;
            spriteWeaponSR = hurtWeaponSRRect;
            spriteWeaponDR = hurtWeaponDRRect;
            spriteParticleSR = hurtParticleSRRect;
            spriteParticleDR = hurtParticleDRRect;
            resetHurt(); //Reset hurt frame
        }

        //If AttackRight or AttackLeft
        else if(s.equals("AttackRight") || s.equals("AttackLeft")) {

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
            spriteBodySR = attackBodySRRect;
            spriteBodyDR = attackBodyDRRect;
            spriteHeadSR = attackHeadSRRect;
            spriteHeadDR = attackHeadDRRect;
            spriteArmSR = attackArmSRRect;
            spriteArmDR = attackArmDRRect;
            spriteWeaponSR = attackWeaponSRRect;
            spriteWeaponDR = attackWeaponDRRect;
            spriteParticleSR = attackParticleSRRect;
            spriteParticleDR = attackParticleDRRect;
            resetAttack(); //Reset attack frame
        }
        else {
            Log.i("stateChanged()", s); //Print erroneous String to Logcat
        }
    }

    //flip(Canvas canvas) method - Flips the canvas horizontally at the midpoint of the sprite
    private void flip(Canvas canvas) {
        canvas.save(); //Save the current canvas so it can be loaded back again later (like matrix operations)
        canvas.scale(-1.0f, 1.0f, x+spriteWidth/2, y+spriteHeight/2);
    }

    //unFlip(Canvas canvas) method - Restores the canvas back to original orientation
    private void unFlip(Canvas canvas) {
        canvas.restore();
    }

    //loadIdle() method - Loads idle bitmap
    public void loadIdle() {
        //If the player is not jumping
        if(!jumping && !attacking && !hurt) {
            stateChanged("IdleRight"); //Call stateChange
        }
    }

    //loadWalk() method - Loads walk bitmap
    public void loadWalk() {
        if(!jumping && !attacking && !hurt) {
            stateChanged("WalkRight"); //Call stateChange
        }
    }

    //loadJump() method - Load jump bitmap
    public void loadJump() {
        //If the player is not attacking or hurt
        if(!attacking && !hurt) {
            //If captureState
            if (captureState) {
                previousState = state; //Assign previousState
                captureState = false; //No need to capture state anymore
            }
            stateChanged("JumpRight"); //Call stateChange
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
        canHurt = false;
        hurt = true;
        attacking = false;
        stateChanged("HurtRight"); //Call stateChange
    }

    //loadIdleLeft() method - Loads flipped idle bitmap
    public void loadIdleLeft() {
        //If the player is not jumping, attacking, or hurt
        if(!jumping && !attacking && !hurt) {
            stateChanged("IdleLeft"); //Call stateChange
        }
    }

    //loadWalkLeft() method - Loads flipped walk bitmap
    public void loadWalkLeft() {
        //If the player is not jumping, attacking, or hurt
        if(!jumping && !attacking && !hurt) {
            stateChanged("WalkLeft"); //Call stateChange
        }
    }

    //loadJumpLeft() method - Loads flipped jump bitmap
    public void loadJumpLeft() {
        //If the player is not attacking or hurt
        if(!attacking && !hurt) {
            //If captureState
            if (captureState) {
                previousState = state; //Assign previousState
                captureState = false; //No need to capture state anymore
            }
            stateChanged("JumpLeft"); //Call stateChange
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
        canHurt = false;
        hurt = true;
        attacking = false;
        stateChanged("HurtLeft"); //Call stateChange
    }

    //animate() method - Animates the player
    private void animate() {
            //If we need to swap frames
            if (currentFrame == swap) {
                currentFrame = 0; //Set currentFrame to 0
                animateState("idle"); //Animate idle state
                animateState("walk"); //Animate walk state
                animateState("jump"); //Animate jump state
                animateState("attack"); //Animate attack state
                animateState("hurt"); //Animate hurt state
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
        //If the state is idle or idleLeft
        if(st.equals("idle")) {

            //If the part is body
            if(part.equals("body")) {
                this.spriteBodySR.top += (idleBodyHeight/idleRows)/factor; //Increase the row by 1
            }

            //If the part is head
            if(part.equals("head")) {
                this.spriteHeadSR.top += (idleHeadHeight/idleRows)/factor; //Increase the row by 1
            }

            //If the part is eyes
            if(part.equals("eyes")) {
                this.spriteEyesSR.top += (idleEyesHeight/idleRows)/factor; //Increase the row by 1
            }

            //If the part is mouth
            if(part.equals("mouth")) {
                this.spriteMouthSR.top += (idleMouthHeight/idleRows)/factor; //Increase the row by 1
            }

            //If the part is weapon
            if(part.equals("weapon")) {
                this.spriteWeaponSR.top += (idleWeaponHeight/idleRows)/factor; //Increase the row by 1
            }

            //If the part is particle
            if(part.equals("particle")) {
                this.spriteParticleSR.top += (idleParticleHeight/idleRows)/factor; //Increase the row by 1
            }
        }

        //Follows the logic above
        if(st.equals("walk")) {
            if(part.equals("body")) {
                this.spriteBodySR.top += (walkBodyHeight/walkRows)/factor; //Increase the row by 1
            }
            if(part.equals("head")) {
                this.spriteHeadSR.top += (walkHeadHeight/walkRows)/factor; //Increase the row by 1
            }
            if(part.equals("eyes")) {
                this.spriteEyesSR.top += (walkEyesHeight/walkRows)/factor; //Increase the row by 1
            }
            if(part.equals("mouth")) {
                this.spriteMouthSR.top += (walkMouthHeight/walkRows)/factor; //Increase the row by 1
            }
            if(part.equals("weapon")) {
                this.spriteWeaponSR.top += (walkWeaponHeight/walkRows)/factor; //Increase the row by 1
            }
            if(part.equals("particle")) {
                this.spriteParticleSR.top += (walkParticleHeight/walkRows)/factor; //Increase the row by 1
            }
        }

        //Follows the logic above
        if(st.equals("jump")) {
            if(part.equals("body")) {
                this.spriteBodySR.top += (jumpBodyHeight/jumpRows)/factor; //Increase the row by 1
            }
            if(part.equals("head")) {
                this.spriteHeadSR.top += (jumpHeadHeight/jumpRows)/factor; //Increase the row by 1
            }
            if(part.equals("arm")) {
                this.spriteArmSR.top += (jumpArmHeight/jumpRows)/factor; //Increase the row by 1
            }
            if(part.equals("weapon")) {
                this.spriteWeaponSR.top += (jumpWeaponHeight/jumpRows)/factor; //Increase the row by 1
            }
            if(part.equals("particle")) {
                this.spriteParticleSR.top += (jumpParticleHeight/jumpRows)/factor; //Increase the row by 1
            }
        }

        //Follows the logic above
        if(st.equals("attack")) {
            if(part.equals("body")) {
                this.spriteBodySR.top += (attackBodyHeight/attackRows)/factor; //Increase the row by 1
            }
            if(part.equals("head")) {
                this.spriteHeadSR.top += (attackHeadHeight/attackRows)/factor; //Increase the row by 1
            }
            if(part.equals("arm")) {
                this.spriteArmSR.top += (attackArmHeight/attackRows)/factor; //Increase the row by 1
            }
            if(part.equals("weapon")) {
                this.spriteWeaponSR.top += (attackWeaponHeight/attackRows)/factor; //Increase the row by 1
            }
            if(part.equals("particle")) {
                this.spriteParticleSR.top += (attackParticleHeight/attackRows)/factor; //Increase the row by 1
            }
        }

        //Follows the logic above
        if(st.equals("hurt")) {
            if(part.equals("body")) {
                this.spriteBodySR.top += (hurtBodyHeight/hurtRows)/factor; //Increase the row by 1
            }
            if(part.equals("head")) {
                this.spriteHeadSR.top += (hurtHeadHeight/hurtRows)/factor; //Increase the row by 1
            }
            if(part.equals("weapon")) {
                this.spriteWeaponSR.top += (hurtWeaponHeight/hurtRows)/factor; //Increase the row by 1
            }
            if(part.equals("particle")) {
                this.spriteParticleSR.top += (hurtParticleHeight/hurtRows)/factor; //Increase the row by 1
            }
        }

    }

    //resetRow(s) method - Accepts a String for the sprite part and resets the Source Rectangle top position to 0
    private void resetRow(String s) {

        //If body part
        if(s.equals("body")) {
            this.spriteBodySR.top = 0; //Reset the SR top to 0, the rest below follow the same logic
        }
        if(s.equals("head")) {
            this.spriteHeadSR.top = 0;
        }
        if(s.equals("eyes")) {
            this.spriteEyesSR.top = 0;
        }
        if(s.equals("mouth")) {
            this.spriteMouthSR.top = 0;
        }
        if(s.equals("arm")) {
            this.spriteArmSR.top = 0;
        }
        if(s.equals("weapon")) {
            this.spriteWeaponSR.top = 0;
        }
        if(s.equals("particle")) {
            this.spriteParticleSR.top = 0;
        }
    }

    //animateState(String s) method - Animates the corresponding state by calling animator, resetting frames, incrementing SR, and handling tickers
    private void animateState(String s) {

        //If the state is idle
        if (s.equals("idle")) {

            //If the state is IdleRight - This is needed because animateState is always called for all passed states. However, only the relevant animation is called.
            if(state.equals("IdleRight") || state.equals("IdleLeft")) {
                animator(bodyDisplayFrame, idleColumns, idleBodyHeight, spriteBodySR, "body", s); //Call animator on idle body
                animator(headDisplayFrame, idleColumns, idleHeadHeight, spriteHeadSR, "head", s); //Call animator on idle head
                animator(eyesDisplayFrame, idleColumns, idleEyesHeight, spriteEyesSR, "eyes", s); //Call animator on idle eyes
                animator(mouthDisplayFrame, idleColumns, idleMouthHeight, spriteMouthSR, "mouth", s); //Call animator on idle mouth
                animator(weaponDisplayFrame, idleColumns, idleWeaponHeight, spriteWeaponSR, "weapon", s); //Call animator on idle weapon
                animator(particleDisplayFrame, idleColumns, idleParticleHeight, spriteParticleSR, "particle", s); //Call animator on idle particle

                //If at the last frame of sprite sheet go back to first frame
                if (bodyFrameTicker >= idleFrames) {
                    resetFrame("body", s); //Reset the body frame
                }
                if (headFrameTicker >= idleFrames) {
                    resetFrame("head", s); //Reset the head frame
                }
                if (eyesFrameTicker >= idleFrames) {
                    resetFrame("eyes", s); //Reset the eyes frame
                }
                if (mouthFrameTicker >= idleFrames) {
                    resetFrame("mouth", s); //Reset the mouth frame
                }
                if (weaponFrameTicker >= idleFrames) {
                    resetFrame("weapon", s); //Reset the weapon frame
                }
                if (particleFrameTicker >= idleFrames) {
                    resetFrame("particle", s); //Reset the particle frame
                }

                incrementSR(s); //Increment the SR

                //Increment tickers
                bodyFrameTicker++;
                bodyDisplayFrame.frame++;
                headFrameTicker++;
                headDisplayFrame.frame++;
                eyesFrameTicker++;
                eyesDisplayFrame.frame++;
                mouthFrameTicker++;
                mouthDisplayFrame.frame++;
                weaponFrameTicker++;
                weaponDisplayFrame.frame++;
                particleFrameTicker++;
                particleDisplayFrame.frame++;
            }
        }

        //Follows the same logic as above
        if (s.equals("walk")) {
            if(state.equals("WalkRight") || state.equals("WalkLeft")) {
                animator(bodyDisplayFrame, walkColumns, walkBodyHeight, spriteBodySR, "body", s);
                animator(headDisplayFrame, walkColumns, walkHeadHeight, spriteHeadSR, "head", s);
                animator(eyesDisplayFrame, walkColumns, walkEyesHeight, spriteEyesSR, "eyes", s);
                animator(mouthDisplayFrame, walkColumns, walkMouthHeight, spriteMouthSR, "mouth", s);
                animator(weaponDisplayFrame, walkColumns, walkWeaponHeight, spriteWeaponSR, "weapon", s);
                animator(particleDisplayFrame, walkColumns, walkParticleHeight, spriteParticleSR, "particle", s);

                //If at the last frame of sprite sheet go back to first frame
                if (bodyFrameTicker >= walkFrames) {
                    resetFrame("body", s); //Reset the body frame
                }
                if (headFrameTicker >= walkFrames) {
                    resetFrame("head", s); //Reset the head frame
                }
                if (eyesFrameTicker >= walkFrames) {
                    resetFrame("eyes", s); //Reset the eyes frame
                }
                if (mouthFrameTicker >= walkFrames) {
                    resetFrame("mouth", s); //Reset the mouth frame
                }
                if (weaponFrameTicker >= walkFrames) {
                    resetFrame("weapon", s); //Reset the weapon frame
                }
                if (particleFrameTicker >= walkFrames) {
                    resetFrame("particle", s); //Reset the particle frame
                }

                incrementSR(s); //Increment the SR

                //Increment tickers
                bodyFrameTicker++;
                bodyDisplayFrame.frame++;
                headFrameTicker++;
                headDisplayFrame.frame++;
                eyesFrameTicker++;
                eyesDisplayFrame.frame++;
                mouthFrameTicker++;
                mouthDisplayFrame.frame++;
                weaponFrameTicker++;
                weaponDisplayFrame.frame++;
                particleFrameTicker++;
                particleDisplayFrame.frame++;
            }
        }

        //Follows the same logic as above
        if (s.equals("jump")) {
            if(state.equals("JumpRight") || state.equals("JumpLeft")) {
                animator(bodyDisplayFrame, jumpColumns, jumpBodyHeight, spriteBodySR, "body", s);
                animator(headDisplayFrame, jumpColumns, jumpHeadHeight, spriteHeadSR, "head", s);
                animator(armDisplayFrame, jumpColumns, jumpArmHeight, spriteArmSR, "arm", s);
                animator(weaponDisplayFrame, jumpColumns, jumpWeaponHeight, spriteWeaponSR, "weapon", s);
                animator(particleDisplayFrame, jumpColumns, jumpParticleHeight, spriteParticleSR, "particle", s);

                //If at the last frame of sprite sheet go back to first frame
                if (bodyFrameTicker >= jumpFrames) {
                    resetFrame("body", s); //Reset the body frame
                }
                if (headFrameTicker >= jumpFrames) {
                    resetFrame("head", s); //Reset the head frame
                }
                if (armFrameTicker >= jumpFrames) {
                    resetFrame("arm", s); //Reset the arm frame
                }
                if (weaponFrameTicker >= jumpFrames) {
                    resetFrame("weapon", s); //Reset the weapon frame
                }
                if (particleFrameTicker >= jumpFrames) {
                    resetFrame("particle", s); //Reset the particle frame
                }

                incrementSR(s); //Increment the SR

                //Increment tickers
                bodyFrameTicker++;
                bodyDisplayFrame.frame++;
                headFrameTicker++;
                headDisplayFrame.frame++;
                armFrameTicker++;
                armDisplayFrame.frame++;
                weaponFrameTicker++;
                weaponDisplayFrame.frame++;
                particleFrameTicker++;
                particleDisplayFrame.frame++;
            }
        }

        //Follows the same logic as above
        if (s.equals("attack")) {
            if(state.equals("AttackRight") || state.equals("AttackLeft")) {
                animator(bodyDisplayFrame, attackColumns, attackBodyHeight, spriteBodySR, "body", s);
                animator(headDisplayFrame, attackColumns, attackHeadHeight, spriteHeadSR, "head", s);
                animator(armDisplayFrame, attackColumns, attackArmHeight, spriteArmSR, "arm", s);
                animator(weaponDisplayFrame, attackColumns, attackWeaponHeight, spriteWeaponSR, "weapon", s);
                animator(particleDisplayFrame, attackColumns, attackParticleHeight, spriteParticleSR, "particle", s);

                //Collision detection for attacks
                for (int i = 0; i < GamePanel.background.ghosts.size(); i++) {
                    //If the player hits the ghost
                    if (bodyFrameTicker == 15 && ((direction == 1 && GamePanel.background.x+x+spriteWidth < GamePanel.background.ghosts.get(i).x+GamePanel.background.ghosts.get(i).spriteWidth && GamePanel.background.x+hitX > GamePanel.background.ghosts.get(i).x) || (direction == -1 && GamePanel.background.x+x > GamePanel.background.ghosts.get(i).x && GamePanel.background.x+x-swordWidth < GamePanel.background.ghosts.get(i).x+GamePanel.background.ghosts.get(i).spriteWidth))) {
                        GamePanel.background.ghosts.get(i).hurt(damage);
                        if(chargedAttack) { //If player has a charged weapon
                            attackDrops -= 3; //They just spent 3 attack drops to use their charged attack so lower total attackDrops the player is carrying by 3
                        }
                    }
                }

                //If at the last frame of sprite sheet go back to first frame
                if (bodyFrameTicker >= attackFrames) {
                    resetAttack(); //Reset the attack frame
                    attacking = false; //No longer attacking

                    //If the player is facing right
                    if(direction == 1) {

                        //If the player is moving
                        if(moving) {
                            loadWalk(); //Load walk sprite
                        }

                        //If the player is not moving
                        else {
                            loadIdle(); //Load idle sprite
                        }
                    }

                    //If the player is facing left
                    else {

                        //If the player is moving
                        if(moving) {
                            loadWalkLeft(); //Load walk sprite
                        }

                        //If the player is not moving
                        else {
                            loadIdleLeft(); //Load idle sprite
                        }
                    }
                }

                //If not at the last frame
                else {
                    incrementSR(s); //Increment the SR

                    //Increment tickers
                    bodyFrameTicker++;
                    bodyDisplayFrame.frame++;
                    headFrameTicker++;
                    headDisplayFrame.frame++;
                    armFrameTicker++;
                    armDisplayFrame.frame++;
                    weaponFrameTicker++;
                    weaponDisplayFrame.frame++;
                    particleFrameTicker++;
                    particleDisplayFrame.frame++;
                }
            }
        }

        //Follows the same logic as above
        if (s.equals("hurt")) {
            if(state.equals("HurtRight") || state.equals("HurtLeft")) {
                animator(bodyDisplayFrame, hurtColumns, hurtBodyHeight, spriteBodySR, "body", s);
                animator(headDisplayFrame, hurtColumns, hurtHeadHeight, spriteHeadSR, "head", s);
                animator(weaponDisplayFrame, hurtColumns, hurtWeaponHeight, spriteWeaponSR, "weapon", s);
                animator(particleDisplayFrame, hurtColumns, hurtParticleHeight, spriteParticleSR, "particle", s);

                //If at the last frame of sprite sheet go back to first frame
                if (bodyFrameTicker >= hurtFrames) {
                    hurt = false; //No longer hurt
                    canHurt = true;
                    resetHurt(); //Reset the hurt frame

                    //If the player is facing right
                    if(direction == 1) {
                        //If the player is moving
                        if(moving) {
                            loadWalk(); //Load walk sprite
                        }

                        //If the player is not moving
                        else {
                            loadIdle(); //Load idle sprite
                        }
                    }

                    //If the player is facing left
                    else {

                        //If the player is moving
                        if(moving) {
                            loadWalkLeft(); //Load walk sprite
                        }

                        //If the player is not moving
                        else {
                            loadIdleLeft(); //Load idle sprite
                        }
                    }
                }

                //If not at the last frame
                else {
                    incrementSR(s); //Increment the SR

                    //Increment tickers
                    bodyFrameTicker++;
                    bodyDisplayFrame.frame++;
                    headFrameTicker++;
                    headDisplayFrame.frame++;
                    weaponFrameTicker++;
                    weaponDisplayFrame.frame++;
                    particleFrameTicker++;
                    particleDisplayFrame.frame++;
                }
            }
        }
    }

    //incrementSR(s) method - Accepts a string for state and moves the Source Rectangle to the right over next frame
    private void incrementSR(String s){

        //If the state is idle
        if (s.equals("idle")) {
            this.spriteBodySR.left = bodyDisplayFrame.get() * ((idleBodyWidth / idleColumns) / factor); //Assign SR left to new frame index for body
            this.spriteBodySR.right = this.spriteBodySR.left + (idleBodyWidth / idleColumns) / factor; //Assign RR right to new frame index for body
            this.spriteHeadSR.left = headDisplayFrame.get() * ((idleHeadWidth / idleColumns) / factor); //Logic is the same below for each body part
            this.spriteHeadSR.right = this.spriteHeadSR.left + (idleHeadWidth / idleColumns) / factor;
            this.spriteEyesSR.left = eyesDisplayFrame.get() * ((idleEyesWidth / idleColumns) / factor);
            this.spriteEyesSR.right = this.spriteEyesSR.left + (idleEyesWidth / idleColumns) / factor;
            this.spriteMouthSR.left = mouthDisplayFrame.get() * ((idleMouthWidth / idleColumns) / factor);
            this.spriteMouthSR.right = this.spriteMouthSR.left + (idleMouthWidth / idleColumns) / factor;
            this.spriteWeaponSR.left = weaponDisplayFrame.get() * ((idleWeaponWidth / idleColumns) / factor);
            this.spriteWeaponSR.right = this.spriteWeaponSR.left + (idleWeaponWidth / idleColumns) / factor;
            this.spriteParticleSR.left = particleDisplayFrame.get() * ((idleParticleWidth / idleColumns) / factor);
            this.spriteParticleSR.right = this.spriteParticleSR.left + (idleParticleWidth / idleColumns) / factor;
        }

        //Logic is the same as above
        else if (s.equals("walk")) {
            this.spriteBodySR.left = bodyDisplayFrame.get() * ((walkBodyWidth / walkColumns) / factor);
            this.spriteBodySR.right = this.spriteBodySR.left + (walkBodyWidth / walkColumns) / factor;
            this.spriteHeadSR.left = headDisplayFrame.get() * ((walkHeadWidth / walkColumns) / factor);
            this.spriteHeadSR.right = this.spriteHeadSR.left + (walkHeadWidth / walkColumns) / factor;
            this.spriteEyesSR.left = eyesDisplayFrame.get() * ((walkEyesWidth / walkColumns) / factor);
            this.spriteEyesSR.right = this.spriteEyesSR.left + (walkEyesWidth / walkColumns) / factor;
            this.spriteMouthSR.left = mouthDisplayFrame.get() * ((walkMouthWidth / walkColumns) / factor);
            this.spriteMouthSR.right = this.spriteMouthSR.left + (walkMouthWidth / walkColumns) / factor;
            this.spriteWeaponSR.left = weaponDisplayFrame.get() * ((walkWeaponWidth / walkColumns) / factor);
            this.spriteWeaponSR.right = this.spriteWeaponSR.left + (walkWeaponWidth / walkColumns) / factor;
            this.spriteParticleSR.left = particleDisplayFrame.get() * ((walkParticleWidth / walkColumns) / factor);
            this.spriteParticleSR.right = this.spriteParticleSR.left + (walkParticleWidth / walkColumns) / factor;
        }

        //Logic is the same as above
        else if (s.equals("jump")) {
            this.spriteBodySR.left = bodyDisplayFrame.get() * ((jumpBodyWidth / jumpColumns) / factor);
            this.spriteBodySR.right = this.spriteBodySR.left + (jumpBodyWidth / jumpColumns) / factor;
            this.spriteHeadSR.left = headDisplayFrame.get() * ((jumpHeadWidth / jumpColumns) / factor);
            this.spriteHeadSR.right = this.spriteHeadSR.left + (jumpHeadWidth / jumpColumns) / factor;
            this.spriteArmSR.left = armDisplayFrame.get() * ((jumpArmWidth / jumpColumns) / factor);
            this.spriteArmSR.right = this.spriteArmSR.left + (jumpArmWidth / jumpColumns) / factor;
            this.spriteWeaponSR.left = weaponDisplayFrame.get() * ((jumpWeaponWidth / jumpColumns) / factor);
            this.spriteWeaponSR.right = this.spriteWeaponSR.left + (jumpWeaponWidth / jumpColumns) / factor;
            this.spriteParticleSR.left = particleDisplayFrame.get() * ((jumpParticleWidth / jumpColumns) / factor);
            this.spriteParticleSR.right = this.spriteParticleSR.left + (jumpParticleWidth / jumpColumns) / factor;
        }

        //Logic is the same as above
        else if (s.equals("attack")) {
            this.spriteBodySR.left = bodyDisplayFrame.get() * ((attackBodyWidth / attackColumns) / factor);
            this.spriteBodySR.right = this.spriteBodySR.left + (attackBodyWidth / attackColumns) / factor;
            this.spriteHeadSR.left = headDisplayFrame.get() * ((attackHeadWidth / attackColumns) / factor);
            this.spriteHeadSR.right = this.spriteHeadSR.left + (attackHeadWidth / attackColumns) / factor;
            this.spriteArmSR.left = armDisplayFrame.get() * ((attackArmWidth / attackColumns) / factor);
            this.spriteArmSR.right = this.spriteArmSR.left + (attackArmWidth / attackColumns) / factor;
            this.spriteWeaponSR.left = weaponDisplayFrame.get() * ((attackWeaponWidth / attackColumns) / factor);
            this.spriteWeaponSR.right = this.spriteWeaponSR.left + (attackWeaponWidth / attackColumns) / factor;
            this.spriteParticleSR.left = particleDisplayFrame.get() * ((attackParticleWidth / attackColumns) / factor);
            this.spriteParticleSR.right = this.spriteParticleSR.left + (attackParticleWidth / attackColumns) / factor;
        }

        //Logic is the same as above
        else if (s.equals("hurt")) {
            this.spriteBodySR.left = bodyDisplayFrame.get() * ((hurtBodyWidth / hurtColumns) / factor);
            this.spriteBodySR.right = this.spriteBodySR.left + (hurtBodyWidth / hurtColumns) / factor;
            this.spriteHeadSR.left = headDisplayFrame.get() * ((hurtHeadWidth / hurtColumns) / factor);
            this.spriteHeadSR.right = this.spriteHeadSR.left + (hurtHeadWidth / hurtColumns) / factor;
            this.spriteWeaponSR.left = weaponDisplayFrame.get() * ((hurtWeaponWidth / hurtColumns) / factor);
            this.spriteWeaponSR.right = this.spriteWeaponSR.left + (hurtWeaponWidth / hurtColumns) / factor;
            this.spriteParticleSR.left = particleDisplayFrame.get() * ((hurtParticleWidth / hurtColumns) / factor);
            this.spriteParticleSR.right = this.spriteParticleSR.left + (hurtParticleWidth / hurtColumns) / factor;
        }

        else {
            Log.i("incrementSR()", s);
        }
    }

    //updateSR(part, state) method - Accepts a sprite part and the state in which to update the source rectangle bottom position
    private void incrementSRBottom(String part, String st) {

        if(st.equals("idle")) {
            if(part.equals("body")) {
                this.spriteBodySR.bottom = this.spriteBodySR.top + ((idleBodyHeight/idleRows)/factor); //Update sourceRect bottom to new index position
            }
            if(part.equals("head")) {
                this.spriteHeadSR.bottom = this.spriteHeadSR.top + ((idleHeadHeight/idleRows)/factor); //Update sourceRect bottom to new index position
            }
            if(part.equals("eyes")) {
                this.spriteEyesSR.bottom = this.spriteEyesSR.top + ((idleEyesHeight/idleRows)/factor); //Update sourceRect bottom to new index position
            }
            if(part.equals("mouth")) {
                this.spriteMouthSR.bottom = this.spriteMouthSR.top + ((idleMouthHeight/idleRows)/factor); //Update sourceRect bottom to new index position
            }
            if(part.equals("weapon")) {
                this.spriteWeaponSR.bottom = this.spriteWeaponSR.top + ((idleWeaponHeight/idleRows)/factor); //Update sourceRect bottom to new index position
            }
            if(part.equals("particle")) {
                this.spriteParticleSR.bottom = this.spriteParticleSR.top + ((idleParticleHeight/idleRows)/factor); //Update sourceRect bottom to new index position
            }
        }
        if(st.equals("walk")) {
            if(part.equals("body")) {
                this.spriteBodySR.bottom = this.spriteBodySR.top + ((walkBodyHeight/walkRows)/factor); //Update sourceRect bottom to new index position
            }
            if(part.equals("head")) {
                this.spriteHeadSR.bottom = this.spriteHeadSR.top + ((walkHeadHeight/walkRows)/factor); //Update sourceRect bottom to new index position
            }
            if(part.equals("eyes")) {
                this.spriteEyesSR.bottom = this.spriteEyesSR.top + ((walkEyesHeight/walkRows)/factor); //Update sourceRect bottom to new index position
            }
            if(part.equals("mouth")) {
                this.spriteMouthSR.bottom = this.spriteMouthSR.top + ((walkMouthHeight/walkRows)/factor); //Update sourceRect bottom to new index position
            }
            if(part.equals("weapon")) {
                this.spriteWeaponSR.bottom = this.spriteWeaponSR.top + ((walkWeaponHeight/walkRows)/factor); //Update sourceRect bottom to new index position
            }
            if(part.equals("particle")) {
                this.spriteParticleSR.bottom = this.spriteParticleSR.top + ((walkParticleHeight/walkRows)/factor); //Update sourceRect bottom to new index position
            }
        }
        if(st.equals("jump")) {
            if (part.equals("body")) {
                this.spriteBodySR.bottom = this.spriteBodySR.top + ((jumpBodyHeight / jumpRows) / factor); //Update sourceRect bottom to new index position
            }
            if (part.equals("head")) {
                this.spriteHeadSR.bottom = this.spriteHeadSR.top + ((jumpHeadHeight / jumpRows) / factor); //Update sourceRect bottom to new index position
            }
            if (part.equals("arm")) {
                this.spriteArmSR.bottom = this.spriteArmSR.top + ((jumpArmHeight / jumpRows) / factor); //Update sourceRect bottom to new index position
            }
            if (part.equals("weapon")) {
                this.spriteWeaponSR.bottom = this.spriteWeaponSR.top + ((jumpWeaponHeight / jumpRows) / factor); //Update sourceRect bottom to new index position
            }
            if (part.equals("particle")) {
                this.spriteParticleSR.bottom = this.spriteParticleSR.top + ((jumpParticleHeight / jumpRows) / factor); //Update sourceRect bottom to new index position
            }
        }
        if(st.equals("attack"))  {
            if (part.equals("body")) {
                this.spriteBodySR.bottom = this.spriteBodySR.top + ((attackBodyHeight / attackRows) / factor); //Update sourceRect bottom to new index position
            }
            if (part.equals("head")) {
                this.spriteHeadSR.bottom = this.spriteHeadSR.top + ((attackHeadHeight / attackRows) / factor); //Update sourceRect bottom to new index position
            }
            if (part.equals("arm")) {
                this.spriteArmSR.bottom = this.spriteArmSR.top + ((attackArmHeight / attackRows) / factor); //Update sourceRect bottom to new index position
            }
            if (part.equals("weapon")) {
                this.spriteWeaponSR.bottom = this.spriteWeaponSR.top + ((attackWeaponHeight / attackRows) / factor); //Update sourceRect bottom to new index position
            }
            if (part.equals("particle")) {
                this.spriteParticleSR.bottom = this.spriteParticleSR.top + ((attackParticleHeight / attackRows) / factor); //Update sourceRect bottom to new index position
            }
        }
        if(st.equals("hurt"))  {
            if (part.equals("body")) {
                this.spriteBodySR.bottom = this.spriteBodySR.top + ((hurtBodyHeight / hurtRows) / factor); //Update sourceRect bottom to new index position
            }
            if (part.equals("head")) {
                this.spriteHeadSR.bottom = this.spriteHeadSR.top + ((hurtHeadHeight / hurtRows) / factor); //Update sourceRect bottom to new index position
            }
            if (part.equals("weapon")) {
                this.spriteWeaponSR.bottom = this.spriteWeaponSR.top + ((hurtWeaponHeight / hurtRows) / factor); //Update sourceRect bottom to new index position
            }
            if (part.equals("particle")) {
                this.spriteParticleSR.bottom = this.spriteParticleSR.top + ((hurtParticleHeight / hurtRows) / factor); //Update sourceRect bottom to new index position
            }
        }
    }

    //resetFrame(String part, String st) method - Accepts a body part and state, resets source rect to first index on the sprite sheet according to state and part
    private void resetFrame(String part, String st) {
        //If idle state
        if(st.equals("idle")) {
            //If body part
            if(part.equals("body")) {
                bodyDisplayFrame.set(0); //Set displayFrame back to 0
                this.spriteBodySR.top = 0; //Set sourceRect top back to 0
                this.spriteBodySR.bottom = this.spriteBodySR.top+((idleBodyHeight/idleRows)/factor); //Update sourceRect bottom to new index position
                bodyFrameTicker = 0; //Reset frameTicker
            }
            //If head part
            if(part.equals("head")) {
                headDisplayFrame.set(0); //Set displayFrame back to 0
                this.spriteHeadSR.top = 0; //Increase the row by 1
                this.spriteHeadSR.bottom = this.spriteHeadSR.top+((idleHeadHeight/idleRows)/factor); //Increase the row by 1
                headFrameTicker = 0; //Reset frameTicker
            }
            //If eyes part
            if(part.equals("eyes")) {
                eyesDisplayFrame.set(0); //Set displayFrame back to 0
                this.spriteEyesSR.top = 0; //Increase the row by 1
                this.spriteEyesSR.bottom = this.spriteEyesSR.top+((idleEyesHeight/idleRows)/factor); //Increase the row by 1
                eyesFrameTicker = 0; //Reset frameTicker
            }
            //If mouth part
            if(part.equals("mouth")) {
                mouthDisplayFrame.set(0); //Set displayFrame back to 0
                this.spriteMouthSR.top = 0; //Increase the row by 1
                this.spriteMouthSR.bottom = this.spriteMouthSR.top+((idleMouthHeight/idleRows)/factor); //Increase the row by 1
                mouthFrameTicker = 0; //Reset frameTicker
            }
            //If weapon part
            if(part.equals("weapon")) {
                weaponDisplayFrame.set(0); //Set displayFrame back to 0
                this.spriteWeaponSR.top = 0; //Increase the row by 1
                this.spriteWeaponSR.bottom = this.spriteWeaponSR.top+((idleWeaponHeight/idleRows)/factor); //Increase the row by 1
                weaponFrameTicker = 0; //Reset frameTicker
            }
            //If particle part
            if(part.equals("particle")) {
                particleDisplayFrame .set(0); //Set displayFrame back to 0
                this.spriteParticleSR.top = 0; //Increase the row by 1
                this.spriteParticleSR.bottom = this.spriteParticleSR.top+((idleParticleHeight/idleRows)/factor); //Increase the row by 1
                particleFrameTicker = 0; //Reset frameTicker
            }
        }
        //If walk state, same logic as above
        if(st.equals("walk")) {
            if(part.equals("body")) {
                bodyDisplayFrame.set(0); //Set displayFrame back to 0
                this.spriteBodySR.top = 0; //Set sourceRect top back to 0
                this.spriteBodySR.bottom = this.spriteBodySR.top+((walkBodyHeight/walkRows)/factor); //Update sourceRect bottom to new index position
                bodyFrameTicker = 0; //Reset frameTicker
            }
            if(part.equals("head")) {
                headDisplayFrame.set(0); //Set displayFrame back to 0
                this.spriteHeadSR.top = 0; //Increase the row by 1
                this.spriteHeadSR.bottom = this.spriteHeadSR.top+((walkHeadHeight/walkRows)/factor); //Increase the row by 1
                headFrameTicker = 0; //Reset frameTicker
            }
            if(part.equals("eyes")) {
                eyesDisplayFrame.set(0); //Set displayFrame back to 0
                this.spriteEyesSR.top = 0; //Increase the row by 1
                this.spriteEyesSR.bottom = this.spriteEyesSR.top+((walkEyesHeight/walkRows)/factor); //Increase the row by 1
                eyesFrameTicker = 0; //Reset frameTicker
            }
            if(part.equals("mouth")) {
                mouthDisplayFrame.set(0); //Set displayFrame back to 0
                this.spriteMouthSR.top = 0; //Increase the row by 1
                this.spriteMouthSR.bottom = this.spriteMouthSR.top+((walkMouthHeight/walkRows)/factor); //Increase the row by 1
                mouthFrameTicker = 0; //Reset frameTicker
            }
            if(part.equals("weapon")) {
                weaponDisplayFrame.set(0); //Set displayFrame back to 0
                this.spriteWeaponSR.top = 0; //Increase the row by 1
                this.spriteWeaponSR.bottom = this.spriteWeaponSR.top+((walkWeaponHeight/walkRows)/factor); //Increase the row by 1
                weaponFrameTicker = 0; //Reset frameTicker
            }
            if(part.equals("particle")) {
                particleDisplayFrame.set(0); //Set displayFrame back to 0
                this.spriteParticleSR.top = 0; //Increase the row by 1
                this.spriteParticleSR.bottom = this.spriteParticleSR.top+((walkParticleHeight/walkRows)/factor); //Increase the row by 1
                particleFrameTicker = 0; //Reset frameTicker
            }
        }
        //If jump state, same logic as above
        if(st.equals("jump")) {
            if(part.equals("body")) {
                bodyDisplayFrame.set(0); //Set displayFrame back to 0
                this.spriteBodySR.top = 0; //Set sourceRect top back to 0
                this.spriteBodySR.bottom = this.spriteBodySR.top+((jumpBodyHeight/jumpRows)/factor); //Update sourceRect bottom to new index position
                bodyFrameTicker = 0; //Reset frameTicker
            }
            if(part.equals("head")) {
                headDisplayFrame.set(0); //Set displayFrame back to 0
                this.spriteHeadSR.top = 0; //Increase the row by 1
                this.spriteHeadSR.bottom = this.spriteHeadSR.top+((jumpHeadHeight/jumpRows)/factor); //Increase the row by 1
                headFrameTicker = 0; //Reset frameTicker
            }
            if(part.equals("arm")) {
                armDisplayFrame.set(0); //Set displayFrame back to 0
                this.spriteArmSR.top = 0; //Increase the row by 1
                this.spriteArmSR.bottom = this.spriteArmSR.top+((jumpArmHeight/jumpRows)/factor); //Increase the row by 1
                armFrameTicker = 0; //Reset frameTicker
            }
            if(part.equals("weapon")) {
                weaponDisplayFrame.set(0); //Set displayFrame back to 0
                this.spriteWeaponSR.top = 0; //Increase the row by 1
                this.spriteWeaponSR.bottom = this.spriteWeaponSR.top+((jumpWeaponHeight/jumpRows)/factor); //Increase the row by 1
                weaponFrameTicker = 0; //Reset frameTicker
            }
            if(part.equals("particle")) {
                particleDisplayFrame.set(0); //Set displayFrame back to 0
                this.spriteParticleSR.top = 0; //Increase the row by 1
                this.spriteParticleSR.bottom = this.spriteParticleSR.top+((jumpParticleHeight/jumpRows)/factor); //Increase the row by 1
                particleFrameTicker = 0; //Reset frameTicker
            }
        }
        //If attack state, same logic as above
        if(st.equals("attack")) {
            if(part.equals("body")) {
                bodyDisplayFrame.set(0); //Set displayFrame back to 0
                this.spriteBodySR.top = 0; //Set sourceRect top back to 0
                this.spriteBodySR.bottom = this.spriteBodySR.top+((attackBodyHeight/attackRows)/factor); //Update sourceRect bottom to new index position
                bodyFrameTicker = 0; //Reset frameTicker
            }
            if(part.equals("head")) {
                headDisplayFrame.set(0); //Set displayFrame back to 0
                this.spriteHeadSR.top = 0; //Increase the row by 1
                this.spriteHeadSR.bottom = this.spriteHeadSR.top+((attackHeadHeight/attackRows)/factor); //Increase the row by 1
                headFrameTicker = 0; //Reset frameTicker
            }
            if(part.equals("arm")) {
                armDisplayFrame.set(0); //Set displayFrame back to 0
                this.spriteArmSR.top = 0; //Increase the row by 1
                this.spriteArmSR.bottom = this.spriteArmSR.top+((attackArmHeight/attackRows)/factor); //Increase the row by 1
                armFrameTicker = 0; //Reset frameTicker
            }
            if(part.equals("weapon")) {
                weaponDisplayFrame.set(0); //Set displayFrame back to 0
                this.spriteWeaponSR.top = 0; //Increase the row by 1
                this.spriteWeaponSR.bottom = this.spriteWeaponSR.top+((attackWeaponHeight/attackRows)/factor); //Increase the row by 1
                weaponFrameTicker = 0; //Reset frameTicker
            }
            if(part.equals("particle")) {
                particleDisplayFrame.set(0); //Set displayFrame back to 0
                this.spriteParticleSR.top = 0; //Increase the row by 1
                this.spriteParticleSR.bottom = this.spriteParticleSR.top+((attackParticleHeight/attackRows)/factor); //Increase the row by 1
                particleFrameTicker = 0; //Reset frameTicker
            }
        }
        //If hurt state, same logic as above
        if(st.equals("hurt")) {
            if(part.equals("body")) {
                bodyDisplayFrame.set(0); //Set displayFrame back to 0
                this.spriteBodySR.top = 0; //Set sourceRect top back to 0
                this.spriteBodySR.bottom = this.spriteBodySR.top+((hurtBodyHeight/hurtRows)/factor); //Update sourceRect bottom to new index position
                bodyFrameTicker = 0; //Reset frameTicker
            }
            if(part.equals("head")) {
                headDisplayFrame.set(0); //Set displayFrame back to 0
                this.spriteHeadSR.top = 0; //Increase the row by 1
                this.spriteHeadSR.bottom = this.spriteHeadSR.top+((hurtHeadHeight/hurtRows)/factor); //Increase the row by 1
                headFrameTicker = 0; //Reset frameTicker
            }
            if(part.equals("weapon")) {
                weaponDisplayFrame.set(0); //Set displayFrame back to 0
                this.spriteWeaponSR.top = 0; //Increase the row by 1
                this.spriteWeaponSR.bottom = this.spriteWeaponSR.top+((hurtWeaponHeight/hurtRows)/factor); //Increase the row by 1
                weaponFrameTicker = 0; //Reset frameTicker
            }
            if(part.equals("particle")) {
                particleDisplayFrame.set(0); //Set displayFrame back to 0
                this.spriteParticleSR.top = 0; //Increase the row by 1
                this.spriteParticleSR.bottom = this.spriteParticleSR.top+((hurtParticleHeight/hurtRows)/factor); //Increase the row by 1
                particleFrameTicker = 0; //Reset frameTicker
            }
        }
    }

    //resetIdle() method - Resets all the idle frames
    private void resetIdle() {
        currentFrame = 0; //Set currentFrame to 0
        resetFrame("body", "idle"); //Reset idle body frames
        resetFrame("head", "idle"); //Reset idle head frames
        resetFrame("eyes", "idle"); //Reset idle eye frames
        resetFrame("mouth", "idle"); //Reset idle mouth frames
        resetFrame("weapon", "idle"); //Reset idle weapon frames
        resetFrame("particle", "idle"); //Reset idle particle frames
    }

    //resetWalk method() - Resets all the walk frames, same logic as resetIdle() method
    private void resetWalk() {
        currentFrame = 0; //Set currentFrame to 0
        resetFrame("body", "walk"); //Reset body frame
        resetFrame("head", "walk"); //Reset head frame
        resetFrame("eyes", "walk"); //Reset eyes frame
        resetFrame("mouth", "walk"); //Reset mouth frame
        resetFrame("weapon", "walk"); //Reset weapon frame
        resetFrame("particle", "walk"); //Reset particle frame
    }

    //resetJump method() - Resets all the jump frames, same logic as resetIdle() method
    private void resetJump() {
        currentFrame = 0; //Set currentFrame to 0
        resetFrame("body", "jump"); //Reset body frame
        resetFrame("head", "jump"); //Reset head frame
        resetFrame("arm", "jump"); //Reset arm frame
        resetFrame("weapon", "jump"); //Reset weapon frame
        resetFrame("particle", "jump"); //Reset particle frame
    }

    //resetAttack method() - Resets all the attack frames, same logic as resetIdle() method
    private void resetAttack() {
        currentFrame = 0; //Set currentFrame to 0
        resetFrame("body", "attack"); //Reset body frame
        resetFrame("head", "attack"); //Reset head frame
        resetFrame("arm", "attack"); //Reset arm frame
        resetFrame("weapon", "attack"); //Reset weapon frame
        resetFrame("particle", "attack"); //Reset particle frame
    }

    //resetHurt method() - Resets all the hurt frames, same logic as resetIdle() method
    private void resetHurt() {
        currentFrame = 0; //Set currentFrame to 0
        resetFrame("body", "hurt"); //Reset body frame
        resetFrame("head", "hurt"); //Reset head frame
        resetFrame("arm", "hurt"); //Reset arm frame
        resetFrame("weapon", "hurt"); //Reset weapon frame
        resetFrame("particle", "hurt"); //Reset particle frame
    }

    //setSpriteSize() method - Set the size of the sprite
    private void setSpriteSize() {
        spriteHeight = spriteBodyDR.bottom-spriteBodyDR.top; //Highest SR yPos - lowest SR yPos, gives current height
        spriteWidth = spriteBodyDR.right-spriteBodyDR.left; //Highest SR xPos - lowest SR xPos, gives current width
    }
}