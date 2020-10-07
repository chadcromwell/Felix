/*******************************************************************************
 //Author: Chad Cromwell
 //Date: February 10th, 2019
 //Assignment: 1
 //Program: Star.java
 //Description: Represents stars, procedurally generates unique stars
 //Methods:
 //rand(int low, int high) method - Random number generator
 ********************************************************************************/
package com.chadcromwell.tme1;

import java.util.Random;

//Star Class - Represents a star
public class Star {
    public int x; //x position
    public int y; //y position
    public int r; //radius
    public int ca; //alpha
    public int cr; //red
    public int cg; //green
    public int cb; //blue
    public int direction; //Direction of flicker, 0 for dimming, 1 for brightening
    Random rand = new Random(); //Random seed

    //Star(int x, int y, int r) method - Accepts an x and y position and a radius
    public Star(int x, int y, int r) {
        int color = rand(1,3); //Assign random number, 1 2 or 3 used to generate random color
        this.x = x; //Capture x pos
        this.y =y; //Capture y pos
        this.r = r; //Capture radius
        this.ca = rand(Background.minFlicker, Background.maxFlicker); //Generate random alpha between min and max flicker amounts
        direction = rand(0, 1); //Randomize if star should begin by brightening or dimming (1 to brighten, 0 to dim)

        //If color 1, pale blue
        if(color == 1) {
            this.cr = 173;
            this.cg = 178;
            this.cb = 218;
        }

        //If color 2, pale red
        if(color == 2) {
            this.cr = 247;
            this.cg = 177;
            this.cb = 173;
        }

        //If colour 3, very pale green
        if(color == 3) {
            this.cr = 251;
            this.cg = 252;
            this.cb = 244;
        }
    }

    //rand(int low, int high) method - Random number generator
    public int rand(int low, int high) {
        int random = rand.nextInt((high+1)-low)+low; //Generate random int
        return random; //Return random int
    }
}