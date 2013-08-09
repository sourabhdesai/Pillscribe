package com.parse.starter;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;

public class OutbreakMap extends Activity implements LocationListener {

    private LocationManager locationManager;
    private String provider;
    MapFragment mMapFragment;
    GoogleMap googleMap;


    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);
        mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mMapFragment);
        fragmentTransaction.commit();
        setUpMapIfNeeded();
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        CameraUpdate toMyLoc= CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude()));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
        googleMap.moveCamera(toMyLoc);
        googleMap.animateCamera(zoom);
        GoogleMapOptions options = new GoogleMapOptions();
        options.rotateGesturesEnabled(false).zoomGesturesEnabled(false);

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {

        }
        moveToPrescription();

    }

    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        int lat = (int) (location.getLatitude());
        int lng = (int) (location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    public void moveToPrescription(){
        Intent intent = new Intent(this,Prescription.class);
        startActivity(intent);
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (googleMap != null) {
                // The Map is verified. It is now safe to manipulate the map.

            }
        }
    }


}