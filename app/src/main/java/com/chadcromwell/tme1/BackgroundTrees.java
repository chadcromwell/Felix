/*******************************************************************************
 //Author: Chad Cromwell
 //Date: February 10th, 2019
 //Assignment: 1
 //Program: BackgroundTrees.java
 //Description: Represents trees in background
 //Methods:
 //update() method - Accepts int for new xPos and updates x variable
 ********************************************************************************/
package com.chadcromwell.tme1;

import android.graphics.Bitmap;

//BackgroundTrees Class - Represents trees in background
public class BackgroundTrees {
    int x; //x position
    int y; //y position
    int spriteWidth; //width of sprite
    int spriteHeight; //height of sprite
    Bitmap bitmap; //Bitmap object

    //Constructor, accepts x and y pos and Bitmap
    public BackgroundTrees(int x, int y, Bitmap bitmap) {
        this.x = x; //Capture x pos
        this.y = y; //Capture y pos
        this.bitmap = bitmap; //Capture bitmap
        spriteWidth = bitmap.getWidth(); //Capture width of bitmap
        spriteHeight = bitmap.getHeight(); //Capture height of bitmap
    }

    //update() method - Accepts int for new xPos and updates x variable
    public void update (int x) {
        this.x = x;
    }
}
