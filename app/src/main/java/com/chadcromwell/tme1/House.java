/*******************************************************************************
 //Author: Chad Cromwell
 //Date: February 10th, 2019
 //Assignment: 1
 //Program: House.java
 //Description: Represents a house object, which is procedurally generated
 //Methods:
 //update() method - Accepts int for new xPos and updates x variable
 ********************************************************************************/
package com.chadcromwell.tme1;

import android.graphics.Bitmap;

//House Class - Represents a house object
public class House {
    int x; //x position
    int y; //y position
    int spriteHeight; //height of sprite
    int spriteWidth; //width of sprite
    Bitmap bitmap; //Bitmap object

    //House(int x, int y, Bitmap bitmap) Constructor - Accepts x and y position and bitmap
    public House(int x, int y, Bitmap bitmap) {
        this.x = x; //Capture x
        this.y = y; //Capture y
        this.bitmap = bitmap; //Capture bitmap
        spriteWidth = bitmap.getWidth(); //Set sprite width
        spriteHeight = bitmap.getHeight(); //Set sprite height
    }

    //update() method - Accepts int for new xPos and updates x variable
    public void update (int x) {
        this.x = x;
    }
}
