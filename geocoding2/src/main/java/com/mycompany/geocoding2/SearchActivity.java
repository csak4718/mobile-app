package com.mycompany.geocoding2;

import android.content.Intent;
import android.location.Address;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import android.location.Geocoder;

import java.io.IOException;
import java.util.*;


public class SearchActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Geocoder geocoder = new Geocoder(this, Locale.US);
        List<Address> addresses;
        double latitude = 0;
        double longitude = 0;

        // Get the message from the intent
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        try{
            addresses = geocoder.getFromLocationName(message, 1);
            if(addresses.size() > 0) {
                latitude= addresses.get(0).getLatitude();
                longitude= addresses.get(0).getLongitude();
            }

            // Create the text view
            TextView textView =new TextView(this);
            textView.setTextSize(40);
            textView.setText(String.valueOf(latitude)+", "+String.valueOf(longitude));

            // Set the text view as the activity layout
            setContentView(textView);
        }
        catch (IOException e){
            System.out.println("IOException");
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
