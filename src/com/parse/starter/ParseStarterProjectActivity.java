package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.Timer;
import java.util.TimerTask;

public class ParseStarterProjectActivity extends Activity {

    /** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
       	setContentView(R.layout.register);
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            moveToHome();
        } else {
            moveToLogin();
        }

    }

    public void moveToLogin(){
        Intent intent  = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void moveToHome(){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }




}
