/*******************************************************************************
 //Author: Chad Cromwell
 //Date: February 10th, 2019
 //Assignment: 1
 //Program: TrashCan.java
 //Description: Represents trash cans in the level
 //Methods:
 //collision(Player player) method - Accepts Player object to check for collision between passed player and trash can
 ********************************************************************************/
package com.chadcromwell.tme1;

import android.graphics.Bitmap;

//TrashCan Class - Represents a trash can object
public class TrashCan {
    int x; //x position
    int y; //y position
    int right; //Right side of sprite
    int spriteHeight; //Height of sprite
    int spriteWidth; //Width of sprite
    Bitmap bitmap; //Bitmap
    boolean canScroll; //Whether background can scroll or not
    boolean collided; //Whether a collision took place

    //TrashCan(int x, int y, Bitmap bitmap) Constructor - Accepts x and y position and a bitmap
    public TrashCan(int x, int y, Bitmap bitmap) {
        this.x = x; //Capture x position
        this.y = y; //Capture y position
        this.bitmap = bitmap; //Capture bitmap
        spriteWidth = bitmap.getWidth(); //Assign width of sprite
        spriteHeight = bitmap.getHeight(); //Assign height of sprite
        collided = false; //Initialize collided as false
        right = x+spriteWidth; //Assign right side of sprite
    }

    //collision(Player player) method - Accepts Player object to check for collision between passed player and trash can
    public void collision(Player player) {
        //If the player collides with the trash can
        if(((player.x+player.spriteWidth > (-GamePanel.background.x)+x && player.x+player.spriteWidth < (-GamePanel.background.x)+x+spriteWidth) || (player.x > (-GamePanel.background.x)+x && player.x < (-GamePanel.background.x)+x+spriteWidth)) && player.y+player.spriteHeight > y) {
            //If the player is to the left of the middle of the trash can
            if(player.x+(player.spriteWidth/2) < (-GamePanel.background.x)+x+(spriteWidth/2)) {
                GamePanel.player.xSpeed = 0; //Stop the player speed
                GamePanel.background.xSpeed = 0; //Stop the background
                GamePanel.background.scrollBackLeft = true; //Scroll background back
            }
            //IF the player is to the right of the middle of the player speed
            else {
                GamePanel.player.xSpeed = 0; //Stop the player speed
                GamePanel.background.xSpeed = 0; //Stop the background
                GamePanel.background.scrollBackRight = true; //Scroll background back
            }
            canScroll = false; //Backround not allowed to scroll
            collided = true; //Player has collided
        }
        //If the player has not collided
        else {
            collided = false; //Player has not collided
            canScroll = true; //Background is free to scroll
        }
    }
}
