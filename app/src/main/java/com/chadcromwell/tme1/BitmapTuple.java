/*******************************************************************************
 //Author: Chad Cromwell
 //Date: February 10th, 2019
 //Assignment: 1
 //Program: BitmapTuple.java
 //Description: Represents a bitmap with a key for easier getting
 //Methods:
 //getBitmapTuple() method - Returns bitmapTuple Object
 ********************************************************************************/
package com.chadcromwell.tme1;

import android.graphics.Bitmap;

//BitmapTuple Class - Represents a bitmap with a key for easier getting
public class BitmapTuple {
    public Bitmap bitmap; //Bitmap object
    public String key; //String key

    //Constructor, accepts Bitmap and Key
    public BitmapTuple(Bitmap bitmap, String key) {
        this.bitmap = bitmap; //Capture bitmap
        this.key = key; //Capture key
    }

    //getBitmapTuple() method - Returns bitmapTuple Object
    public Bitmap getBitmapTuple() {
        return this.bitmap;
    }
}
