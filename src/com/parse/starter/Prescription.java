package com.parse.starter;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
            if(check[i])
                scheduleLocalNotification(drug,hourTT,minuteTT,i);

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

    public void scheduleLocalNotification(String name, int hour,int minute, int day)
    {
      /*Good tutorial on AlarmManager at http://code4reference.com/2012/07/tutorial-on-android-alarmmanager/
        prescription.put("name",drug);
        prescription.put("hourToTake",hourTT);
        prescription.put("minuteToTake",minuteTT);
        prescription.put("DaysToTake",daysOfWeekTT);
        */
       /*
        AlarmManager alarmMgr = (AlarmManager)getSystemService(ALARM_SERVICE);
            SimpleDateFormat dayFormatter = new SimpleDateFormat("yyyy-M-d H:m");
            final Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY,hour);
            calendar.set(Calendar.MINUTE,minute);
            calendar.set(Calendar.DAY_OF_WEEK, day+1);  //day+1 because our format for days of week is one less than the format for days of week in cal.

            Date today = new Date();

            calendar.setTime(today);
            String scheduledStartDate = calendar.get(Calendar.YEAR) + "-" +
                    (calendar.get(Calendar.MONTH)+1) + "-" +
                    calendar.get(Calendar.DAY_OF_MONTH) + " ";
            try
            {
                Date startDate = dayFormatter.parse(scheduledStartDate);
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, startDate.getTime(), AlarmManager.INTERVAL_DAY*7, pendingItent);
            }
            catch(java.text.ParseException pEx)
            {
                pEx.printStackTrace();
            }    */
}

}