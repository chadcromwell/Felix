/*******************************************************************************
 //Author: Chad Cromwell
 //Date: February 10th, 2019
 //Assignment: 1
 //Program: DisplayFrame.java
 //Description: Represents the display frame, used for animation
 //Methods:
 //get() method - returns the frame
 //set(int frame) method - sets the frame
 ********************************************************************************/
package com.chadcromwell.tme1;

//DisplayFrame Class - Represents the display frame, used for animation
public class DisplayFrame {
    int frame; //Holds current frame

    //Constructor, accepts frame and captures frame
    public DisplayFrame(int frame) {
        this.frame = frame;
    }

    //get() method - returns the frame
    public int get() {
        return this.frame;
    }

    //set(int frame) method - sets the frame
    public void set(int frame) {
        this.frame = frame;
    }
}
