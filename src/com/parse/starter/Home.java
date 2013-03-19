package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.parse.ParseUser;

/**
 * Created with IntelliJ IDEA.
 * User: neel
 * Date: 3/18/13
 * Time: 9:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class Home extends Activity {

    Button logout,addPrescription;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        logout=(Button)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ParseUser.logOut();
                moveToLogin();
            }
        });
        addPrescription = (Button)findViewById(R.id.prescription);
        addPrescription.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                moveToPrescription();
            }
        });

    }



    public void moveToLogin(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);

    }

    public void moveToPrescription(){
        Intent intent = new Intent(this,Prescription.class);
        startActivity(intent);

    }



}



