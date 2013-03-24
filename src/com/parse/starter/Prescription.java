package com.parse.starter;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
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

import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: neel
 * Date: 3/18/13
 * Time: 5:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class Prescription extends Activity {
    EditText name,reason;
    Button add;
    LocationManager mLocationManager;
    private static String prescription_name,prescription_sickness;
    TimePicker timeTTWidget;
    int hour;
    int minute;
    int notificationId = 0;
    double latitude, longitude;
    String provider;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addprescription);
        add = (Button)findViewById(R.id.pAdd);
        timeTTWidget= (TimePicker) findViewById(R.id.timePicker);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = mLocationManager.getBestProvider(criteria, false);
        Location location = mLocationManager.getLastKnownLocation(provider);
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        add.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                name = (EditText)findViewById(R.id.pName);
                hour=timeTTWidget.getCurrentHour();  //returns int from 0-23 (24 hour clock), no need to worry about am or pm
                minute=timeTTWidget.getCurrentMinute();

                reason = (EditText)findViewById(R.id.pFor);
                boolean[] daysOfWeekToTake= {((CheckBox) findViewById(R.id.sunday)).isChecked(),
                        ((CheckBox) findViewById(R.id.monday)).isChecked(),
                        ((CheckBox) findViewById(R.id.tuesday)).isChecked(),
                        ((CheckBox) findViewById(R.id.wednesday)).isChecked(),
                        ((CheckBox) findViewById(R.id.thursday)).isChecked(),
                        ((CheckBox) findViewById(R.id.friday)).isChecked(),
                        ((CheckBox) findViewById(R.id.saturday)).isChecked(),};

                prescription_name = name.getText().toString();
                prescription_sickness = reason.getText().toString();
                EditText notesWidget= (EditText) findViewById(R.id.notes);
                String notes= notesWidget.getText().toString();
                String daysString= booleanArrToStr(daysOfWeekToTake);   //For some reason, cant store a boolean[] in a ParseObject, so I created a method to convert boolean[] to a string of 0s and 1s
                parsePrescription(daysString, prescription_name,prescription_sickness, notes,hour,minute);
                parseOutbreak(prescription_sickness,latitude, longitude);


                finish();

            }
        });

    }


    public void parsePrescription(String daysOfWeekTT, String drug, String sickness, String notes,int hourTT,int minuteTT){
        ParseObject prescription = new ParseObject("Prescription");
        prescription.put("name",drug);
        prescription.put("sickness",sickness);
        ParseUser current = ParseUser.getCurrentUser();
        prescription.put("user",current);
        prescription.put("hourToTake",hourTT);
        prescription.put("minuteToTake",minuteTT);
        prescription.put("DaysToTake",daysOfWeekTT);  //A String of a boolean[] of size 7. An array {0,1,1,1,0,0,0} means the drug will be taken every mon., tues., wed. Use Boolean.parseBoolean(String s)
        prescription.put("Notes",notes);
        prescription.put("dateAdded",new Date());  //The date the user added the drug they have to take as a reminder
        prescription.saveInBackground();
        boolean [] check = StrtoBoolArr(daysOfWeekTT);
        for(int i = 0; i < check.length; i++){
            if(check[i] == true)
                scheduleLocalNotification("Time to take: "+drug,12000);

        }

    }

    public void parseOutbreak(String sickness, double latitude, double longitude){
        ParseObject outbreak = new ParseObject("Outbreak");
        ParseGeoPoint point = new ParseGeoPoint(latitude, longitude);
        outbreak.put("sickness", sickness);
        outbreak.put("location",point);
        outbreak.saveInBackground();

    }


    public String booleanArrToStr(boolean[] b)	{ //takes a boolean[] and returns a string of corresponding 0's and 1's
        String str="";
        for (int i=0; i<b.length; i++)	{
            if (b[i])	{
                str+=1;
            } else	{
                str+=0;
            }
        }
        return str;
    }

    public boolean[] StrtoBoolArr (String s)	{ //takes a string of 0's and 1's and returns a corresponding boolean[]
        boolean[] boolArr=new boolean[s.length()];
        for(int i=0; i<s.length(); i++)	{
            if(s.charAt(i)=='1')	{
                boolArr[i]=true;
            } else	{
                boolArr[i]=false;
            }
        }
        return boolArr;
    }

    public void scheduleLocalNotification(String text, int seconds)
    {
        Context context = getApplicationContext();
        long when = System.currentTimeMillis() + seconds * 10;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, Home.class);

        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, 0,notificationIntent, 0);
        Notification notification = new Notification(R.drawable.ic_launcher,text,when);
        notification.setLatestEventInfo(context, "PillScribe", text, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.when = when;
        notificationManager.notify(notificationId, notification);

        notificationId++;
    }





}