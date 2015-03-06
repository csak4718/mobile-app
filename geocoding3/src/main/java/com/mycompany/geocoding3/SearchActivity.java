package com.mycompany.geocoding3;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import android.location.Geocoder;

import com.beust.jcommander.JCommander;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import org.w3c.dom.Attr;

import java.io.IOException;
import java.util.*;


public class SearchActivity extends Activity implements OnMapReadyCallback {
    double latitude;
    double longitude;
    ArrayList<Attraction> attractions;
    Weather weather;
    public final static String EXTRA_MESSAGE = "com.mycompany.geocoding3.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Geocoder geocoder = new Geocoder(this, Locale.US);
        List<Address> addresses;


        // Get the message from the intent
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        try {
            addresses = geocoder.getFromLocationName(message, 1); // return 1 address to user

            if (addresses.size() > 0) {
                latitude = addresses.get(0).getLatitude();
                longitude = addresses.get(0).getLongitude();
            }

            YelpQuery yq = new YelpQuery();
            attractions = yq.getAttractions(latitude, longitude);


            WeatherQuery wq = new WeatherQuery();

//            System.out.println(addresses.get(0).getAdminArea()+ "/" +addresses.get(0).getLocality().replaceAll(" ","_"));
            weather = wq.getWeather(addresses.get(0).getAdminArea()+ "/" +addresses.get(0).getLocality().replaceAll(" ","_"));


            TextView textView = (TextView) findViewById(R.id.txv);
            textView.setTextSize(25);
            textView.setText("Latitude: " + String.valueOf(latitude) + "\n"
                    + "Longitude: " + String.valueOf(longitude)); // Show Latitude & Longitude of searching location

            if (attractions.size()==0) textView.setText("Back to previous page to enter correct address!");

            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);


        }
        catch (Exception e){
            System.out.println("Exception in SearchActivity");
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


    @Override
    public void onMapReady(GoogleMap map) {
        LatLng addr = new LatLng(latitude, longitude);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(addr, 13));

        map.addMarker(new MarkerOptions()
                .title("Location Name") // TO DO
                .snippet("URL") // TO DO
                .position(addr)); // Marker of searching location


        // Add markers to locations retrieved from Yelp
        for (int i=0; i< attractions.size(); i++){

            map.addMarker(new MarkerOptions()
                    .title(attractions.get(i).getName()) // TO DO
                    .snippet("URL") // TO DO
                    .position( new LatLng(attractions.get(i).getLatitude(), attractions.get(i).getLongitude()) )
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }

    }

    /** Called when the user clicks the Navigate button */
    public void startNavigate(View view){

        Uri gmmIntentUri = Uri.parse("google.navigation:q="+String.valueOf(latitude)+","+String.valueOf(longitude));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    /** Called when the user clicks the Send button */
    public void showWeather(View view){
        // Do something in response to button
        Intent intent =new Intent(this,DisplayWeatherActivity.class);
        intent.putExtra(EXTRA_MESSAGE, weather.getSummary());
        startActivity(intent);
    }
}
