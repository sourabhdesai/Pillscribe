package com.parse.starter;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import com.parse.ParseGeoPoint;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: neel
 * Date: 3/18/13
 * Time: 4:58 PM
 * To change this template use File | Settings | File Templates.
 */

public class OutbreakMap extends Activity implements LocationListener {
    LocationManager mLocationManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        setContentView(R.layout.main);

        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location != null) {
            // Do something with the recent location fix
            //  otherwise wait for the update below
            //&& location.getTime() > Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000
            ParseGeoPoint point = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
        }
        else {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.v("Location Changed", location.getLatitude() + " and " + location.getLongitude());
            mLocationManager.removeUpdates(this);
        }
    }

    // Required functions
    public void onProviderDisabled(String arg0) {}
    public void onProviderEnabled(String arg0) {}
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}
}