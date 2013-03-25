package com.parse.starter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.parse.*;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created with IntelliJ IDEA.
 * User: neel
 * Date: 3/24/13
 * Time: 10:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class MyPrescriptions extends Activity {

    public ListView prescriptions;
    public String [] names;
    public List<ParseObject> pr;
    public ArrayList<String> pList;
    public ArrayAdapter<String> pListAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myprescriptions);

        //Set up the listview
        pList = new ArrayList<String>();
        //Create and populate an ArrayList of objects from parse
        pListAdapter =  new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        ListView prescriptions = (ListView) findViewById(R.id.mylist);
        prescriptions.setAdapter(pListAdapter);
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery query = new ParseQuery("Prescription");
        query.whereEqualTo("user",currentUser);
        query.include("user");
        query.findInBackground(new FindCallback() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    //Toast.makeText(getApplicationContext(), objects.toString(), Toast.LENGTH_LONG).show();
                    for (int i = 0; i < objects.size(); i++) {
                        Object object = objects.get(i);
                        String name = ((ParseObject) object).getString("name").toString();
                        pListAdapter.add(name);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }



                 }

        });




// Define a new Adapter
// First parameter - Context
// Second parameter - Layout for the row
// Third parameter - ID of the TextView to which the data is written
// Forth - the Array of data

    }
}