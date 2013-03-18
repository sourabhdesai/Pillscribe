package com.parse.starter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    Button register;
    EditText username_string;
    EditText password_string;


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
                        username=username_string.getText().toString();
                        password=password_string.getText().toString();
                        register(username,password);
                    }
                });
    }

    public void register(String set_username, String set_password){
        ParseUser user = new ParseUser();
        user.setUsername(set_username);
        user.setPassword(set_password);
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        });
    }
}
