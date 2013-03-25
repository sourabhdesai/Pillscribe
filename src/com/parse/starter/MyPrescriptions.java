package com.parse.starter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.parse.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: neel
 * Date: 3/24/13
 * Time: 10:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class MyPrescriptions extends Activity {

    public ListView prescriptions;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myprescriptions);
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery query = new ParseQuery("Prescription");
        query.whereEqualTo("user",currentUser);

        query.findInBackground(new FindCallback() {
            public void done(List<ParseObject> prescriptionList, ParseException e) {
                // commentList now has the comments for myPost
            }
        });
        ListView prescriptions = (ListView) findViewById(R.id.mylist);
        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2" };

// Define a new Adapter
// First parameter - Context
// Second parameter - Layout for the row
// Third parameter - ID of the TextView to which the data is written
// Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);


// Assign adapter to ListView
       prescriptions.setAdapter(adapter);
    }
}