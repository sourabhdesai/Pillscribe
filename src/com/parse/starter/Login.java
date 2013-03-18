package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created with IntelliJ IDEA.
 * User: neel
 * Date: 3/18/13
 * Time: 8:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class Login extends Activity {
    private String username;
    private String password;
    Button login,register;
    EditText username_string;
    EditText password_string;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        login = (Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                username_string = (EditText)findViewById(R.id.username);
                password_string = (EditText)findViewById(R.id.password);
                username=username_string.getText().toString();
                password=password_string.getText().toString();
                loginParse(username, password);
            }
        });
        register=(Button)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                moveToRegister();
            }
        });

    }


    public void loginParse(String username, String password){
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    moveToHome();
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                }
            }
        });

    }

    public void moveToHome(){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    public void moveToRegister(){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }


}
