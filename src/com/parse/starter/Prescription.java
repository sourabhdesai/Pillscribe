package com.parse.starter;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: neel
 * Date: 3/18/13
 * Time: 5:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class Prescription extends Activity implements LocationListener {
    EditText name,reason;
    Button add;
    LocationManager mLocationManager;
    private String prescription_name,prescription_sickness;
    private Time timeToTake;  //The time of the day they have to take it
    private int frequencyOfIntake;  //The frequency (in days they have to take it).
                                    // Example: a value of 0 indicates they will take it once at the specified time at that day.
                                    //Example: a value of 2 indicates they will take it once every two days (every other day).

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addprescription);
        add = (Button)findViewById(R.id.pAdd);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        add.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                name = (EditText)findViewById(R.id.pName);
                reason = (EditText)findViewById(R.id.pFor);
                NumberPicker freqOfInWidget= (NumberPicker)  findViewById(R.id.numberPicker);
                TimePicker timeTTWidget= (TimePicker) findViewById(R.id.timePicker);
                frequencyOfIntake=freqOfInWidget.getValue();
                timeToTake.hour=timeTTWidget.getCurrentHour();
                timeToTake.minute=timeTTWidget.getCurrentMinute();
                prescription_name = name.getText().toString();
                prescription_sickness = reason.getText().toString();
                parsePrescription(timeToTake, frequencyOfIntake, prescription_name,prescription_sickness);
                getLocation();
            }
        });

    }


    public void parsePrescription(Time timeTT, int freq, String drug, String sickness){
        ParseObject prescription = new ParseObject("Prescription");
        prescription.put("name",name);
        prescription.put("sickness",sickness);
        ParseUser current = ParseUser.getCurrentUser();
        prescription.put("user",current);
        prescription.put("timeToTake",timeTT);
        prescription.put("freqOfIntake",freq);
        prescription.put("dateAdded",new Date());  //The date the user added the drug they have to take as a reminder
        prescription.saveInBackground();
    }

    public void parseOutbreak(String sickness, ParseGeoPoint point){
        ParseObject outbreak = new ParseObject("Outbreak");
        outbreak.put("sickness", sickness);
        outbreak.put("location",point);
        outbreak.saveInBackground();

    }
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.v("Location Changed", location.getLatitude() + " and " + location.getLongitude());
            mLocationManager.removeUpdates(this);
        }
    }

    public void getLocation(){
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location == null){
            location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if(location != null) {
            // Do something with the recent location fix
            //  otherwise wait for the update below
            //&& location.getTime() > Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000
            ParseGeoPoint point = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
            parseOutbreak(prescription_sickness,point);
        }
        else {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }

    }



    // Required functions
    public void onProviderDisabled(String arg0) {}
    public void onProviderEnabled(String arg0) {}
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}

}