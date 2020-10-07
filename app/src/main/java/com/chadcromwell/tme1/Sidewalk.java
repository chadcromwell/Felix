/*******************************************************************************
 //Author: Chad Cromwell
 //Date: February 10th, 2019
 //Assignment: 1
 //Program: Sidewalk.java
 //Description: Represents the sidewalk which is proceduarlly generated
 //Methods:
 //update() method - Calculates and updates second point of line x position
 ********************************************************************************/
package com.chadcromwell.tme1;

//Sidewalk Class - Represents a sidewalk
public class Sidewalk {
    public int x; //x position
    public int y; //y position
    public int xx; //second point xPos
    public int s; //slope
    public int yy; //second point yPos

    //Sidewalk(int x, int y, int s, int t) method - Accepts x and y position, slope and rise
    public Sidewalk(int x, int y, int s, int t) {
        this.s = s; //Capture slope
        this.x = x; //Capture x pos
        this.y = y; //Capture y pos
        this.yy = t; //Capture rise
        xx = x+(t/s); //Calculate second point of line x position
    }

    //update() method - Calculates and updates second point of line x position
    public void update() {
        xx = x+(yy/s);
    }
}
