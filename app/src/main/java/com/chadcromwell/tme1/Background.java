/*******************************************************************************
 //Author: Chad Cromwell
 //Date: February 10th, 2019
 //Assignment: 1
 //Program: Background.java
 //Description: Level of the game, placement of enemies, trash cans, background, houses, etc. Handles parallax scrolling in relation to player position.
 //Methods:
 //housePos(int index) method - Accepts an int which represents the house number and returns an xPos for a trash can so that it is placed in between the houses. Example int = 3, returns xPos that will place a trash can between the 3rd and 4th house
 //trashCan(int index, int type) method - Accepts an index in which to place the trashcan, explained in trashPos() method, and a type to pick which type of trashcan sprite to use
 //aggressiveGhost(int x, int y, int minX, int maxX, int activationX, boolean activated) method - Accepts an x and y int for placement, minX and maxX for max distance it can move, activationX int of player position where it activates the ghost, boolean if it's activated yet or not
 //normalGhost(int x, int y, int minX, int maxX) method - Same as above but for normal type Ghosts
 //update() method - Updates objects, positions, checks collisions, etc.
 //scrollBackLeft() method - Scrolls background back to account for player collision correction towards the left
 //scrollBackRight() method - Scrolls background back to account for player collision correction towards the right, follows same logic as above method
 //scroll() method - Scrolls the background sprites in correlation with player movement, with parallax
 //rand(int low, int high) method - Generates a random int between two ints
 //generateStars() method - Procedurally generates stars
 //genereateSidewalk(int ss, int ey) method - Accepts slope for divide between slabs, and the height of the curb (rise of divide)
 //generateBackgroundTrees(int y) method - Accepts y position for trees, procedurally generates trees in background
 //generateHouses(int x, int y, int s) method - Accepts x for x pos, y for y post, and s for space between houses. Procedurally generates houses.
 //generateTrashCans(int x, int y, int s) method - Same as other procedural generation methods above, but not used because level design needed more accuracy
 //draw(Canvas canvas) method - Draws all sprites
 //sort(ArrayList<SideWalk> sw) method - Custom sort comparator of Sidewalk objects. sorts sidewalks from left to right, used with drawing the lines
 ********************************************************************************/
package com.chadcromwell.tme1;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.ArrayList;

//Background Class - Level of game, handles placement of enemies, trash cans, background objects, houses, etc. Handles parallax scrolling in relation to player position.
public class Background implements GameObject {
    public static int x; //Background x postiion
    public static int backgroundWidth = 20000; //Width of the background
    private int backgroundHeight = 2667; //Height of the background
    private int acceleration = GamePanel.player.acceleration; //Background acceleration
    private int maxSpeed = GamePanel.player.maxSpeed; //Max background speed
    public int xSpeed; //Speed in which background moves in x direction
    private Paint paint; //Paint variable used for drawing graphic objects

    public ArrayList<Ghost> ghosts; //ArrayList of Ghost objects
    public ArrayList<Drop> drops; //ArrayList of Drops

    //Sky stuff
    private int skyColor = Color.argb(128, 11, 13, 23); //Color of sky, dark blue

    //Moon stuff
    private int moonFactor = 100; //The factor in which the moon moves different to other background objects
    private int moonHeight = 25; //Starting height of the moon
    private int moonX = 1200; //Horizontal starting postiion of the moon

    //Star stuff
    Random rand = new Random(); //Random seed
    private Rect sky; //Rectangle for drawing sky
    private ArrayList<Star> stars; //ArrayList of stars
    private int starsSize = 0; //Keep track of number of stars in array, initialized to 0
    private int totalStars = 800; //Total number of stars in sky
    private int maxStarRadius = 3; //Max apparent angular degrees of star in radius (half of overall star size)
    private int minStarRadius = 1; //Minimum apparent angular degrees of star in radius (half of overall star size)
    public static int maxFlicker = 255; //Maximum amount of flicker, 255 is good
    public static int minFlicker = 125; //Minimum amount of flicker, 125 is good
    private int var = 3; //Variance in initialization alpha, 3 is good (Amount of variance between all stars' apparent magnitude)

    //Background Trees stuff
    private ArrayList<BackgroundTrees> backgroundTrees; //ArrayList of Bitmaps to hold the background tree Bitmaps
    private int backgroundTreesFactor = 15; //The factor in which the trees move different to other background objects
    private int backgroundTreesHeight = 0; //Height of trees, initialized to 0
    private int backgroundTreesWidth = 0; //Width of trees, initialized to 0
    private int backgroundTreesSize = 0; //Number of trees in array, initialized to 0

    //Road Stuff
    private Rect road; //Rectangle for drawing road
    private int roadHeight = 20;  //Height of road
    private int roadColor = Color.argb(255, 50, 50, 50); //Color of road, dark gray

    //Sidewalk stuff
    private Rect sidewalk; //Rectangle for drawing sidewalk
    private ArrayList<Sidewalk> sidewalks; //ArrayList of sidewalks
    private int sidewalksSize = 0; //size of sidewalks ArrayList
    private static int sidewalkFactor = 10; //How much to divide sidewalk
    private int sidewalkWidth = Constants.SCREEN_WIDTH/sidewalkFactor; //Width of each sidewalk slab
    private int sidewalkHeight = 60; //Height (width between road and grass) of the sidewalk
    private int slope = 10; //The x width of the slope of the line between each slab of concrete
    private int sidewalkColor = Color.argb(255, 70, 70, 70); //Color of the sidewalk, light gray

    //Grass stuff
    private Rect grass; //Rectangle for drawing grass, attempted gradient however there is a known graphics issue in Android that prevented this, so flat green it is
    private int grassHeight = (Constants.SCREEN_HEIGHT/4); //Height of the grass (how tall it is/width)
    private int lightGrass = Color.argb(255, 72, 84, 51); //Light green color, not used because of gradient graphics bug in Android
    private int darkGrass = Color.argb(255, 52, 64, 31); //Color of grass, dark green

    //House stuff
    private ArrayList<House> houses; //ArrayList of houses
    private int housesSize = 0; //size of houses ArrayList
    private int houseSpacing = 200; //Space between each house
    private int house1YAdjustment = -60; //Vertical adjustment for house type 1
    private int house2YAdjustment = house1YAdjustment+20; //Vertical adjustment for house type 2

    //Trash Can stuff
    private ArrayList<TrashCan> trashCans; //ArrayList of trash cans
    private int trashCansSize = 0; //Size of trashCans ArrayList
    private int trashCanYAdjustment = 50; //Verticle placement adjustment for the trash cans

    private int startX = Constants.SCREEN_WIDTH/4; //Player starting x position, 1/4 in from left side
    public int startY = Constants.SCREEN_HEIGHT-GamePanel.player.spriteHeight-roadHeight-((sidewalkHeight/2)-10); //Player starting y position, placed on sidewalk

    private boolean canScroll = true; //Whether or not the background is allowed to scroll
    private int collisionCount = 0; //Int to hold number of collisions

    private int trashCanIndex; //Int to hold index of trash can object in array
    public boolean scrollBackLeft; //Whether or not to adjust scroll back to the left (due to collisions)
    public boolean scrollBackRight; //Whether or not to adjust scroll back to the right (due to collisions)

    //Background() Constructor
    public Background() {

        x = 0; //Assign start x to 0 so that when level is reset the background starts in proper position.
        GamePanel.player.x = startX; //Assign startX to player x
        GamePanel.player.y = startY; //Assign startY to player y
        Matrix matrix = new Matrix(); //Create new matrix for scaling
        matrix.postScale(1.0f, 1.0f); //Scaled to 1x1
        GameActivity.addBitmapToMemoryCache("moon", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Level 1/Moon1.png")); //Get moon asset at add it to the bitmap memory cache
        GameActivity.addBitmapToMemoryCache("trees", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Level 1/Trees.png")); //Get trees asset at add it to the bitmap memory cache
        backgroundTreesHeight = GameActivity.getBitmapFromCache("trees").getHeight(); //Get the height of the trees bitmap and assign
        backgroundTreesWidth = GameActivity.getBitmapFromCache("trees").getWidth(); //Get the width of the trees bitmap and assign
        GameActivity.addBitmapToMemoryCache("house1", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Level 1/House1.png")); //Get house1 asset at add it to the bitmap memory cache
        GameActivity.addBitmapToMemoryCache("house2", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Level 1/House2.png")); //Get house2 asset at add it to the bitmap memory cache
        GameActivity.addBitmapToMemoryCache("trashCan1", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Level 1/TrashCan1.png")); //Get trashcan1 asset at add it to the bitmap memory cache
        GameActivity.addBitmapToMemoryCache("trashCan2", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Level 1/TrashCan2.png")); //Get trashcan2 asset at add it to the bitmap memory cache
        GameActivity.addBitmapToMemoryCache("trashCan3", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Level 1/TrashCan3.png")); //Get trashcan3 asset at add it to the bitmap memory cache
        paint = new Paint(); //New paint for coloring rects
        sky = new Rect(0, 0, backgroundWidth, backgroundHeight); //Initialize sky rect
        road = new Rect(0, Constants.SCREEN_HEIGHT-roadHeight, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT); //Initialize road rect
        sidewalk = new Rect(0, road.top-sidewalkHeight, Constants.SCREEN_WIDTH, road.top); //Initialize sidewalk rect
        grass = new Rect(0, sidewalk.top-grassHeight, Constants.SCREEN_WIDTH, sidewalk.top); //Initialize grass rect

        stars = new ArrayList<>(); //Initlialize array to hold stars
        generateStars(); //Call generateStars() method to generate the stars procedurally
        starsSize = stars.size(); //Assign the size of the stars array to starsSize

        sidewalks = new ArrayList<>(); //Initialize sidewalk array
        generateSidewalk(slope, sidewalk.top); //Call generateSidewalk() method to generate the sidewalk procedurally
        sidewalksSize = sidewalks.size(); //Assign the size of the sidewalk array to sidewalksSize

        houses = new ArrayList<>(); //Initialize houses array
        generateHouses(0, sidewalk.top, houseSpacing); //Call generateHouses() method to generate the houses procedurally
        housesSize = houses.size(); //Assign the size of the houses array to housesSize

        backgroundTrees = new ArrayList<>(); // Initialize trees array
        generateBackgroundTrees(grass.top); //Call generateBackgroundTrees() method to generate the trees procedurally
        backgroundTreesSize = backgroundTrees.size(); //Assign the size of the trees array to backgroundTreesSize

        ghosts = new ArrayList<>(); //Initialize ghosts array
        drops = new ArrayList<>(); //Initialize drops array

        trashCans = new ArrayList<>(); //Initialize trashCans array
        trashCans.add(trashCan(4, 1)); //Add a trashcan to the array
        trashCans.add(trashCan(5, 3)); //Add a trashcan to the array
        trashCans.add(trashCan(8, 1)); //Add a trashcan to the array
        trashCans.add(trashCan(10, 2)); //Add a trashcan to the array
        trashCans.add(trashCan(11, 1)); //Add a trashcan to the array
        trashCans.add(trashCan(13, 3)); //Add a trashcan to the array
        trashCansSize = trashCans.size(); //Assign the size of the trashCan array to trashCansSize

        ghosts.add(aggressiveGhost(11000, sidewalk.top-280, 250, 250, 0, true)); //Add an aggressive ghost to ghost array
        ghosts.get(0).drop = "attack"; //Add attack drop to the first aggressive
        ghosts.add(aggressiveGhost(backgroundWidth, sidewalk.top-280, 250, 250, backgroundWidth/2, false)); //Add an aggressive ghost to ghost array
        ghosts.add(normalGhost(trashCans.get(0).right, sidewalk.top-280, 0, trashCans.get(1).x-(trashCans.get(0).x)-256)); //Add a normal ghost to ghost array
        ghosts.get(2).drop = "attack"; //Add attack drop to the first normal ghost
        ghosts.add(normalGhost(trashCans.get(3).right, sidewalk.top-280, 0, trashCans.get(4).x-(trashCans.get(3).x)-256)); //Add a normal ghost to ghost array
        ghosts.get(3).drop = "attack"; //Add attack drop to the second normal ghost
        ghosts.add(normalGhost(trashCans.get(4).right, sidewalk.top-280, 0, trashCans.get(5).x-(trashCans.get(4).x)-256)); //Add a normal ghost to ghost array

    }

    //housePos(int index) method - Accepts an int which represents the house number and returns an xPos for a trash can so that it is placed in between the houses. Example int = 3, returns xPos that will place a trash can between the 3rd and 4th house
    public int trashPos(int index) {
        int pos = houses.get(index-1).x+((houses.get(index).x-houses.get(index-1).x+houses.get(index-1).spriteWidth)/2)-120; //Described above
        return pos; //Return the int
    }

    //trashCan(int index, int type) method - Accepts an index in which to place the trashcan, explained in trashPos() method, and a type to pick which type of trashcan sprite to use
    public TrashCan trashCan(int index, int type) {
        TrashCan can = new TrashCan(trashPos(index), sidewalk.top - GameActivity.getBitmapFromCache("trashCan1").getHeight() + trashCanYAdjustment, GameActivity.getBitmapFromCache("trashCan1")); //Initialize trash can object, then assign bitmap later, better on memory

        //If type 1 get trashCan1 sprite and assign it to can object
        if(type == 1) {
            can.bitmap =  GameActivity.getBitmapFromCache("trashCan1");
        }
        //If type 2 get trashCan2 sprite and assign it to can object
        if(type == 2) {
            can.bitmap = GameActivity.getBitmapFromCache("trashCan2");
        }
        //If type 3 get trashCan3 sprite and assign it to can object
        if(type == 3) {
            can.bitmap = GameActivity.getBitmapFromCache("trashCan3");
        }
        return can; //Return the can
    }

    //aggressiveGhost(int x, int y, int minX, int maxX, int activationX, boolean activated) method - Accepts an x and y int for placement, minX and maxX for max distance it can move, activationX int of player position where it activates the ghost, boolean if it's activated yet or not
    public Ghost aggressiveGhost(int x, int y, int minX, int maxX, int activationX, boolean activated) {
        Ghost ghost = new Ghost(x, y, minX, maxX, "aggressive", activationX, activated); //create new Ghost object with params described above
        return ghost; //Return the ghost
    }

    //normalGhost(int x, int y, int minX, int maxX) method - Same as above but for normal type Ghosts
    public Ghost normalGhost(int x, int y, int minX, int maxX) {
        Ghost ghost = new Ghost(x, y, minX, maxX, "normal", 0, true);
        return ghost;
    }

    public void addDrop(int x, int y, String s) {
        drops.add(new Drop(x, y, s)); //Add new drop item in location where enemy dies
    }

    //update() method - Updates objects, positions, checks collisions, etc.
    @Override
    public void update() {

        //If the player is dead
        if(GamePanel.player.hp == 0) {
            GamePanel.playing = false; //No longer playing
            GameActivity.context.startActivity(GameActivity.deadIntent); //Start DeadActivity
            GameActivity.activity.finish(); //Finish the GameActivity
        }

        //If the ghosts are all dead
        if(ghosts.size() == 0) {
            GamePanel.playing = false; //No longer playing
            GameActivity.context.startActivity(GameActivity.endIntent); //Start EndActivity
            GameActivity.activity.finish(); //Finish the GameActivity
        }

        //Iterate through ghosts
        for(int i = 0; i < ghosts.size(); i++) {
            if(!ghosts.get(i).alive) { //If the ghost is dead
                if(ghosts.get(i).drop != null && ghosts.get(i).drop.equals("attack")) { //If the ghost is carrying an item
                    addDrop(ghosts.get(i).x+(ghosts.get(i).spriteWidth/2), ghosts.get(i).y+(ghosts.get(i).spriteHeight/2),"attack"); //Add the item to drop array, placed in middle of ghost sprite
                }
                GamePanel.player.kills++; //Increase number of kills the player has
                ghosts.remove(ghosts.get(i)); //Remove the ghost from the array
            }
        }

        //Iterate through trash cans
        for(int i = 0; i < trashCansSize; i++) {
            trashCans.get(i).collision(GamePanel.player); //Check collisions between player and trashcans
            if(trashCans.get(i).collided) { //If the player collided
                trashCanIndex = i; //Assign current i to index
            }
            if(!trashCans.get(i).canScroll) { //If the current trashcan cannot scroll
                collisionCount++; //Increase number of collisions
            }
        }

        //If there are no collisions
        if(collisionCount == 0) {
            canScroll = true; //The background can scroll
        }

        //If there is collisions
        if(collisionCount != 0) {
            canScroll = false; //The background cannot scroll
            collisionCount = 0; //Reset collisionCount to 0
        }

        //If can scroll back to the left
        if(scrollBackLeft) {
            scrollBackLeft(); //Call method to scroll the background back to account for player being moved back by trash can collision
        }

        //If can scroll back to the right
        if(scrollBackRight) {
            scrollBackRight(); //Same as above but scrolling right
        }

        //Iterate through ghosts
        for (int i = 0; i < ghosts.size(); i++) {
            ghosts.get(i).update(); //Update each ghost
            //If the ghost is not activated
            if(!ghosts.get(i).activated) {
                ghosts.get(i).activate(GamePanel.player.x); //Pass player position to activate method to see if player is close enough for chasing ghost to activate
            }
        }

        //Iterate through drops
        for(int i = 0; i < drops.size(); i++) {
            if(drops.get(i).collisionTest(GamePanel.player)) { //If the player collides with the drop
                drops.get(i).collision(GamePanel.player); //Perform collision
                drops.remove(drops.get(i)); //Remove the drop
            }
        }
    }

    //scrollBackLeft() method - Scrolls background back to account for player collision correction towards the left
    public void scrollBackLeft() {
        int pos = (-x)+trashCans.get(trashCanIndex).x; //Get the current position of the trash can
        if(GamePanel.player.x+GamePanel.player.spriteWidth > pos && GamePanel.player.x < (-x)+trashCans.get(trashCanIndex).x+trashCans.get(trashCanIndex).spriteWidth) { //If the player is still over the trash can
            canScroll = false; //Cannot scroll
            x -= 30; //scroll back the background to account for collision correction
        }
        else {
            canScroll = true; //Can scroll
            scrollBackLeft = false; //Do not correct
        }
    }

    //scrollBackRight() method - Scrolls background back to account for player collision correction towards the right, follows same logic as above method
    public void scrollBackRight() {
        int pos = (-x)+trashCans.get(trashCanIndex).x+trashCans.get(trashCanIndex).spriteWidth;
        if(GamePanel.player.x < pos && GamePanel.player.x > (-x)+trashCans.get(trashCanIndex).x) {
            canScroll = false;
            x += 30;
        }
        else {
            canScroll = true;
            scrollBackRight = false;
        }
    }

    //scroll() method - Scrolls the background sprites in correlation with player movement, with parallax
    public void scroll() {
        if(canScroll) { //If the background can scroll
            //If the player is moving
            if (GamePanel.player.moving) {
                //If player is going right and they reach the middle of the screen
                if (GamePanel.player.direction == 1/* && captureOffset*/) {
                    xSpeed = maxSpeed;
                }

                //If the player is going left and they reach the middle of the screen
                if (GamePanel.player.direction == -1/* && captureOffset*/) {
                    xSpeed = -maxSpeed;
                }
            }

            //If the player is not moving
            if (!GamePanel.player.moving) {
                if (xSpeed > 0) { //If the player has any velocity going to the right
                    xSpeed -= acceleration; //Slow down the background
                }
                if (xSpeed < 0) { //If the player has any velocity going to the left
                    xSpeed += acceleration; //Slow down the background
                }
                if (xSpeed == 0) { //If the player has no velocity
                    xSpeed = 0; //Kill velocity
                }
            }

            x += xSpeed; //Increase background x pos by player speed

            //If the background scrolls past the left side of the screen, stop it at 0
            if (x <= 0) {
                x = 0;
            }

            //If the background scrolls past the right side of the screen, stop it at the end
            if (x >= backgroundWidth - Constants.SCREEN_WIDTH) {
                x = backgroundWidth - Constants.SCREEN_WIDTH;
            }

            //If the player is going right and the first sidewalk is starting to go off the screen to the left, move the 0 index sidewalk to the far right. This minimizes memory use as there are only enough sidewalk parts to cover the screen
            if (GamePanel.player.direction == 1 && sidewalks.get(1).x < x) {
                sidewalks.get(0).x = x + Constants.SCREEN_WIDTH; //Move the first index sidewalk to the right end
                sidewalks.get(0).update(); //Update the sidewalk pieces
                sort(sidewalks); //Sort them via position
            }

            //Same as above but for going left
            if (GamePanel.player.direction == -1 && sidewalks.get(sidewalksSize - 1).x > x + Constants.SCREEN_WIDTH) {
                sort(sidewalks);
                sidewalks.get(sidewalksSize - 1).x = x - sidewalkWidth;
                sidewalks.get(sidewalksSize - 1).update();
                sort(sidewalks);
            }

            //Iterate through all stars
            for (int i = 0; i < starsSize; i++) {
                if (stars.get(i).direction == 1) { //If star is to be getting brighter
                    if (stars.get(i).ca < maxFlicker) { //If star alpha is not at maxFlicker yet
                        stars.get(i).ca += var; //Increase apparent magnitude by var amount
                        if (stars.get(i).ca >= maxFlicker) { //If star reaches maxFlicker
                            stars.get(i).ca = maxFlicker; //Stop alpha at maxFlicker
                        }
                    }
                    else {
                        stars.get(i).direction = 0; //Set direction to 0 so it now starts dimming
                    }
                }
                if (stars.get(i).direction == 0) { //If star is to be getting dimmer, follows same logic as above
                    if (stars.get(i).ca > minFlicker) {
                        stars.get(i).ca -= var;
                        if (stars.get(i).ca <= minFlicker) {
                            stars.get(i).ca = minFlicker;
                        }
                    } else {
                        stars.get(i).direction = 1;
                    }
                }
            }
        }
    }

    //rand(int low, int high) method - Generates a random int between two ints
    public int rand(int low, int high) {
        int random = rand.nextInt((high+1)-low)+low; //Generate random int
        return random; //Return int
    }

    //generateStars() method - Procedurally generates stars
    public void generateStars() {
        for(int i = 0; i < totalStars; i++) {
            stars.add(new Star(rand(0, backgroundWidth), rand(0, backgroundHeight), rand(minStarRadius, maxStarRadius)));
        }
    }

    //genereateSidewalk(int ss, int ey) method - Accepts slope for divide between slabs, and the height of the curb (rise of divide)
    public void generateSidewalk(int ss, int ey) {
        int sx = -sidewalkWidth; //Starting x for slab
        int sy = sidewalk.bottom; //Bottom of slab
        //Iterate for sidewalkFactor (how many sidewalk divisions/slabs there should be on the screen)
        for(int i = 0; i < sidewalkFactor+1; i++){
            sidewalks.add(new Sidewalk(sx, sy, ss, ey)); //Add a sidewalk with params
            if(i != 0) { //If not on the first slab
                sidewalks.get(i).x += sidewalkWidth*i; //Increment starting x position of the divide
                sidewalks.get(i).xx += sidewalkWidth*i; //Increment ending x position of the divide
            }
        }
        sort(sidewalks); //Sort the sidewalks by x position
    }

    //generateBackgroundTrees(int y) method - Accepts y position for trees, procedurally generates trees in background
    public void generateBackgroundTrees(int y) {
        int startX = 0; //Start at the left side of screen
        int startY = y-backgroundTreesHeight; //Set y position
        int totalBackgroundTrees = backgroundWidth/backgroundTreesWidth; //How many tree sprites are needed to fill total level length
        //Iterate through all background trees
        for(int i = 0; i < totalBackgroundTrees; i++) {
            backgroundTrees.add(new BackgroundTrees(startX, startY, GameActivity.getBitmapFromCache("trees"))); //Add a tree sprite
            startX += backgroundTreesWidth; //Increment the startX position for next tree sprite that will be added
        }
    }

    //generateHouses(int x, int y, int s) method - Accepts x for x pos, y for y post, and s for space between houses. Procedurally generates houses.
    public void generateHouses(int x, int y, int s) {
        int startX = x; //Capture starting x
        int startY = y-GameActivity.getBitmapFromCache("house1").getHeight(); //Set the y pos with offset
        int houseSpacing = s; //Capture spacing
        int totalHouses = backgroundWidth/houseSpacing; //Calculate number of houses needed to populate the level based on spacing

        //Iterate through all houses
        for(int i = 0; i < totalHouses; i++) {
            int random = rand(0, 1); //Generate random seed
            if(random == 0) { //If rand is 0, add a house1 to the array
                houses.add(new House(startX, startY+house1YAdjustment, GameActivity.getBitmapFromCache("house1")));
                startX += houseSpacing+GameActivity.getBitmapFromCache("house1").getWidth(); //Increment the x pos for next house
            }
            if(random == 1) { //If rand is 1, add a house2 to the array
                houses.add(new House(startX, startY+house2YAdjustment, GameActivity.getBitmapFromCache("house2")));
                startX += houseSpacing+GameActivity.getBitmapFromCache("house2").getWidth(); //Increment the x pos for next house
            }
        }
    }

    //generateTrashCans(int x, int y, int s) method - Same as other procedural generation methods above, but not used because level design needed more accuracy
    public void generateTrashCans(int x, int y, int s) {
        int startX = x;
        int startY = y-GameActivity.getBitmapFromCache("trashCan1").getHeight();
        int trashCanSpacing = s;
        int totalTrashCans = backgroundWidth/trashCanSpacing;
        for(int i = 0; i < totalTrashCans; i++) {
            int random = rand(0, 1);
            if(random == 0) {
                trashCans.add(new TrashCan(startX, startY+trashCanYAdjustment, GameActivity.getBitmapFromCache("trashCan1")));
                startX += trashCanSpacing+GameActivity.getBitmapFromCache("trashCan1").getWidth();
            }
            if(random == 1) {
                trashCans.add(new TrashCan(startX, startY+trashCanYAdjustment, GameActivity.getBitmapFromCache("trashCan2")));
                startX += trashCanSpacing+GameActivity.getBitmapFromCache("trashCan2").getWidth();
            }
            if(random == 3) {
                trashCans.add(new TrashCan(startX, startY+trashCanYAdjustment, GameActivity.getBitmapFromCache("trashCan3")));
                startX += trashCanSpacing+GameActivity.getBitmapFromCache("trashCan3").getWidth();
            }
        }
    }

    //draw(Canvas canvas) method - Draws all sprites
    @Override
    public void draw(Canvas canvas){
        paint.setColor(skyColor); //Set paint to skyColor
        canvas.drawRect(sky, paint); //Draw the sky with the color

        //Iterate through stars
        for (int i = 0; i < starsSize; i++) {
            paint.setColor(Color.argb(stars.get(i).ca, stars.get(i).cr, stars.get(i).cg, stars.get(i).cb)); //Get argb from the star object and assign it to paint
            canvas.drawCircle(stars.get(i).x, stars.get(i).y, stars.get(i).r, paint); //Draw the star
        }

        canvas.drawBitmap(GameActivity.getBitmapFromCache("moon"), -(x / moonFactor) + moonX, moonHeight, null); //Draw the moon

        //Iterate through all background trees
        for(int i = 0; i < backgroundTreesSize; i++) {

            //If the trees are within the screen
            if(backgroundTrees.get(i).x > (x/backgroundTreesFactor)-backgroundTrees.get(i).spriteWidth && backgroundTrees.get(i).x < x+Constants.SCREEN_WIDTH) {
                canvas.drawBitmap(backgroundTrees.get(i).bitmap, -(x / backgroundTreesFactor) + backgroundTrees.get(i).x, backgroundTrees.get(i).y, null); //Draw the trees
            }
        }

        paint.setColor(roadColor); //Set paint colour to dark grey for the road
        canvas.drawRect(road, paint); //Draw the road
        paint.setColor(sidewalkColor); //Set paint colour to light grey for the sidewalk
        canvas.drawRect(sidewalk, paint); //Draw the sidewalk
        paint.setColor(roadColor); //Set paint colour to dark grey for the sidewalk lines
        paint.setStrokeWidth(4); //Set the stroke width for the sidewalk lines

        //Iterate through all sidewalks
        for(int i = 0; i < sidewalksSize; i++) {
            canvas.drawLine(-(x)+sidewalks.get(i).x, sidewalk.bottom, -(x)+sidewalks.get(i).xx, sidewalk.top, paint); //Draw each sidewalk line
        }

        paint.setColor(darkGrass); //Set paint colour to lightGrass
        canvas.drawRect(grass, paint); //Draw the grass

        //Iterate through all houses
        for(int i = 0; i < housesSize; i++) {

            //If the house is within the screen
            if(houses.get(i).x > x-houses.get(i).spriteWidth && houses.get(i).x < x+Constants.SCREEN_WIDTH) {
                canvas.drawBitmap(houses.get(i).bitmap, -(x) + houses.get(i).x, houses.get(i).y, null); //Draw the house
            }
        }

        //Iterate through all trash cans
        for(int i = 0; i < trashCansSize; i++) {

            //If the trash can is within the screen
            if(trashCans.get(i).x > x-trashCans.get(i).spriteWidth && trashCans.get(i).x < x+Constants.SCREEN_WIDTH) {
                canvas.drawBitmap(trashCans.get(i).bitmap, -(x) + trashCans.get(i).x, trashCans.get(i).y, null); //Draw the trash can
            }
        }

        //Iterate through all ghosts
        for(int i = 0; i < ghosts.size(); i++) {

            //If the ghost is within the screen
            if(ghosts.get(i).x > x-ghosts.get(i).spriteWidth && ghosts.get(i).x < x+Constants.SCREEN_WIDTH) {
                ghosts.get(i).draw(canvas); //Draw the ghost
            }
        }

        //Iterate through all drops
        for(int i = 0; i < drops.size(); i++) {
            canvas.drawBitmap(drops.get(i).bitmap, -(x) + drops.get(i).x, drops.get(i).y, null); //Draw the drop
        }
    }

    //sort(ArrayList<SideWalk> sw) method - Custom sort comparator of Sidewalk objects. sorts sidewalks from left to right, used with drawing the lines
    public void sort(ArrayList<Sidewalk> sw) {
        Collections.sort(sw, new Comparator<Sidewalk>() {
            @Override
            public int compare(Sidewalk s1, Sidewalk s2) { //Compare Sidewalk objects
                if (s1.x > s2.x) { //If the first Sidewalk x is farther right than the one comparing it to
                    return 1; //Shift it right
                }
                if (s1.x < s2.x) { //If the first sidewalk is to the left of the one comparing it to
                    return -1; //Shift it left
                }
                return 0; //Done
            }
        });
    }
}