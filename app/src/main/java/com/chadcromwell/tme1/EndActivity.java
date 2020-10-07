/*******************************************************************************
 //Author: Chad Cromwell
 //Date: February 10th, 2019
 //Assignment: 1
 //Program: EndActivity.java
 //Description: End screen
 //Methods:
 //onCreate(Bundle savedInstanceState) method - Sets view and adds listener to button
 //onClick(View v) method - When button is clicked, start new activity
 ********************************************************************************/
package com.chadcromwell.tme1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//EndActivity Class - End Screen
public class EndActivity extends Activity
        implements View.OnClickListener{

    //onCreate(Bundle savedInstanceState) method - Sets view and adds listener to button
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the view to activity_end
        setContentView(R.layout.activity_end);

        //Create button reference
        final Button playAgainButton =
                (Button)findViewById(R.id.playAgainButton);
        playAgainButton.setOnClickListener(this); //add listener

    }

    //onClick(View v) method - When button is clicked, start new activity
    @Override
    public void onClick(View v) {
        GamePanel.resetLevel(); //Reset the level
        Intent i = new Intent(this, GameActivity.class); //Create new intent for GameActivity
        startActivity(i); //Start new GameActivity
        finish(); //Finish this activity
    }
}