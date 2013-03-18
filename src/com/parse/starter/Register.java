package com.parse.starter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created with IntelliJ IDEA.
 * User: neel
 * Date: 3/17/13
 * Time: 7:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class Register extends Activity {

    private String username;
    private String password;
    private String email;
    Button register,login;
    EditText username_string;
    EditText password_string;
    EditText email_string;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        register = (Button)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        username_string = (EditText)findViewById(R.id.username);
                        password_string = (EditText)findViewById(R.id.password);
                        email_string = (EditText)findViewById(R.id.email);
                        username=username_string.getText().toString();
                        password=password_string.getText().toString();
                        email=email_string.getText().toString();
                        registerParse(username,password,email);
                    }
                });
        login = (Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                               moveToLogin();
                        }
                });

    }

    public void registerParse(String set_username, String set_password, String set_email){
        ParseUser user = new ParseUser();
        user.setUsername(set_username);
        user.setPassword(set_password);
        user.setEmail(set_email);
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    //added a little notification at the bottom
                    Context context = getApplicationContext();
                    CharSequence text = "Registration Successful";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    moveToHome();


                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        });
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
