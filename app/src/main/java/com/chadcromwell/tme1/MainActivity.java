/*******************************************************************************
 //Author: Chad Cromwell
 //Date: February 10th, 2019
 //Assignment: 1
 //Program: MainActivity.java
 //Description: Main screen
 //Methods:
 //onCreate(Bundle savedInstancedStace) method - When created, sets view and a listener to button
 //onClick(View v) method - Accepts a view, starts new activity when clicked
 ********************************************************************************/
package com.chadcromwell.tme1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//MainActivity Class - Main Screen
public class MainActivity extends Activity
        implements View.OnClickListener{

    //onCreate(Bundle savedInstancedStace) method - When created, sets view and a listener to button
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the view to activity_main
        setContentView(R.layout.activity_main);

        //Create button reference
        final Button buttonPlay =
                (Button)findViewById(R.id.playButton);
        buttonPlay.setOnClickListener(this); //add listener

    }

    //onClick(View v) method - Accepts a view, starts new activity when clicked
    @Override
    public void onClick(View v) {
        Intent i = new Intent(this, GameActivity.class); //Create new intent for GameActivity
        startActivity(i); //Start new GameActivity
        finish(); //Finish this activity
    }
}