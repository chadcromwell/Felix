/*******************************************************************************
 //Author: Chad Cromwell
 //Date: February 10th, 2019
 //Assignment: 1
 //Program: GetBitmapFromAsset.java
 //Description: Custom class to load bitmap from asset folder
 //Methods:
 //calculateInSampleSize method, previously used with inJustDecodeBounds to scale, and calculate the size, but not used anymore, keeping for debugging purposes in future
 ********************************************************************************/
package com.chadcromwell.tme1;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

//GetBitmapFromAsset Class - Custom class to load bitmap from asset folder
public class GetBitmapFromAsset {

    //Default Constructor
    public GetBitmapFromAsset() {

    }

    //getBitmapFromAsset(Context context, String fn) method - Loads a bitmap from asset folder and returns the bitmap
    public static Bitmap getBitmapFromAsset(Context context, String fn) {
        AssetManager am = context.getAssets(); //Use assetmanager
        InputStream is; //Input stream
        Bitmap bitmap = null; //Empty bitmap
        BitmapFactory.Options options = new BitmapFactory.Options(); //Create options for bitmap
        options.inPreferredConfig = Bitmap.Config.ARGB_4444; //Set to ARGB_4444
        options.inJustDecodeBounds = true; //Allows querying of bitmap without allocating memory for pixels, code below
        try {
            is = am.open(fn); //open input stream with filename
            bitmap = BitmapFactory.decodeStream(is, null, options); //using inputstream and options, load bitmap, this was used previously for scaling however not used anymore
        }
        catch (IOException e) {
        }

        options.inJustDecodeBounds = false; //Turn off ijdb
        try {// Same as above, left over from query
            is = am.open(fn);
            bitmap = BitmapFactory.decodeStream(is, null, options);
        }
        catch (IOException e) {
        }
        return bitmap; //Return the bitmap
    }

    //calculateInSampleSize method, previously used with inJustDecodeBounds to scale, and calculate the size, but not used anymore, keeping for debugging purposes in future
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}