/*******************************************************************************
 //Author: Chad Cromwell
 //Date: February 10th, 2019
 //Assignment: 1
 //Program: GameObject.java
 //Description: Interface for GameObject, each GameObject can draw and update
 //Methods:
 //draw(Canvas canvas) method - Draw to canvas
 //update() method - Update objects
 ********************************************************************************/
package com.chadcromwell.tme1;

import android.graphics.Canvas;

//GameObject interface - All game objects have a draw and update method
public interface GameObject {
    void draw(Canvas canvas);
    void update();
}
