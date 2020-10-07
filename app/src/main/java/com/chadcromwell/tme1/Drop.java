/*******************************************************************************
 //Author: Chad Cromwell
 //Date: February 10th, 2019
 //Assignment: 1
 //Program: Drop.java
 //Description: Represents drop object in the level
 //Methods:
 //collision(Player player) method - Accepts Player object to check for collision between passed player and drop
 ********************************************************************************/
package com.chadcromwell.tme1;

import android.graphics.Bitmap;
import android.graphics.Canvas;

//Drop Class - Represents a trash can object
public class Drop {
    int x; //x position
    int y; //y position
    int right; //Right side of sprite
    int spriteHeight; //Height of sprite
    int spriteWidth; //Width of sprite
    Bitmap bitmap; //Bitmap
    String drop; //Type of drop, "attack" "health" "speed"

    //Drop(int x, int y, Bitmap bitmap) Constructor - Accepts x and y position and a bitmap
    public Drop(int x, int y, String drop) {
        this.x = x; //Capture x position
        this.y = y; //Capture y position
        GameActivity.addBitmapToMemoryCache("attackDrop", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Drops/Attack.png"));

        //GameActivity.addBitmapToMemoryCache("healthDrop", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Drops/Health.png"));
        //GameActivity.addBitmapToMemoryCache("speedDrop", GetBitmapFromAsset.getBitmapFromAsset(GameActivity.context, "Drops/Speed.png"));
        this.drop = drop;

        if(drop.equals("attack")){
            bitmap = GameActivity.getBitmapFromCache("attackDrop");
        }
        if(drop.equals("health")){
            //bitmap = GameActivity.getBitmapFromCache("healthDrop"); //Not created yet
        }
        if(drop.equals("speed")){
            //bitmap = GameActivity.getBitmapFromCache("speedDrop"); //Not created yet
        }

        spriteWidth = bitmap.getWidth(); //Assign width of sprite
        spriteHeight = bitmap.getHeight(); //Assign height of sprite
        right = x+spriteWidth; //Assign right side of sprite
    }

    //collision(Player player) method - Accepts Player object to check for collision between passed player and trash can
    public void collision(Player player) {
        //If the player collides with the trash can
        if(((player.x+player.spriteWidth > (-GamePanel.background.x)+x && player.x+player.spriteWidth < (-GamePanel.background.x)+x+spriteWidth) || (player.x > (-GamePanel.background.x)+x && player.x < (-GamePanel.background.x)+x+spriteWidth)) && player.y+player.spriteHeight > y) {
            if(drop.equals("attack")) {
                player.attackDrops++; //Increase player's damage by 1
            }
            if(drop.equals("health")) {
                player.hp++; //Increase player's hp by 1
            }
            if(drop.equals("speed")) {
                player.maxSpeed += 5; //Increase player's speed by 5
            }
        }
    }

    public boolean collisionTest(Player player) {
        boolean b = false;
        //If the player collides with the trash can
        if (((player.x + player.spriteWidth > (-GamePanel.background.x) + x && player.x + player.spriteWidth < (-GamePanel.background.x) + x + spriteWidth) || (player.x > (-GamePanel.background.x) + x && player.x < (-GamePanel.background.x) + x + spriteWidth)) && player.y + player.spriteHeight > y) {
            b = true;
        }
        return b;
    }
}