package com.mycompany.geocoding3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

/**
 * Created by de-weikung on 3/4/15.
 */
public class YelpQuery {
    private static String CONSUMER_KEY = "euOioTVcEZW-KD8Ou6jHFQ";
    private static String CONSUMER_SECRET = "_VobQj0q2Yrauxnb0DiyT0n0h_k";
    private static String TOKEN = "6BeXTbYV3_SHoU6WgAQloHg2TVnEl6Q9";
    private static String TOKEN_SECRET = "GInA4dQcdXj39J22Lw3Wzp9yt30";

    private YelpAPI yelpAPI;

    public YelpQuery(){
        this.yelpAPI = new YelpAPI(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);
    }

    public class FetchAPI extends Thread{
        private double latitude;
        private double longitude;

        private ArrayList<Attraction> attractions;

        public FetchAPI(double lat, double lng){
            latitude = lat;
            longitude = lng;
        }

        public void run() {
            attractions = getAPIResponse(latitude, longitude); // try this
        }

        public ArrayList<Attraction> getResults(){
            return attractions;
        }
    }

    public ArrayList<Attraction> getAttractions(double latitude, double longitude){
        FetchAPI fapi = new FetchAPI(latitude, longitude);
        fapi.start();
        try {
            fapi.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        return fapi.getResults();
    }

    public ArrayList<Attraction> getAPIResponse(double latitude, double longitude){

        String output_query;
        JSONParser parser;
        JSONObject response = null;

        /*//if true get longitude and latitude
        if (first_letter <= 'z' && first_letter >= 'a'){
            output_query = this.yelpAPI.search(start_location, distance, true);
            parser = new JSONParser();
            try {
                response = (JSONObject) parser.parse(output_query);
            } catch (ParseException pe) {
                System.out.println("Error: could not parse JSON response:");
                System.out.println(output_query);
                System.exit(1);
            }
            JSONObject region_info = (JSONObject)response.get("region");
            JSONObject center      = (JSONObject)region_info.get("center");
            double latitude  = (double)center.get("latitude");
            double longitude = (double)center.get("longitude");
            start_location = Double.toString(latitude) + ", " + Double.toString(longitude);
        }*/
        String start_location = Double.toString(latitude) + ", " + Double.toString(longitude);

        ArrayList<Attraction>list = new ArrayList<Attraction>();
        output_query = this.yelpAPI.search(latitude, longitude);
//        System.out.println(output_query);

        parser = new JSONParser();
        try {
            response = (JSONObject) parser.parse(output_query);


            JSONArray businesses = (JSONArray) response.get("businesses");
            for (int i = 0; i < 10; i++) {
                JSONObject cur_business;
                try {
                    cur_business = (JSONObject) businesses.get(i);
                } catch (RuntimeException pe) {
                    return list;
                }
                JSONObject loc = (JSONObject) cur_business.get("location");
                double attr_distance = (double) cur_business.get("distance");
                String attr_name = (String) cur_business.get("name");
                String picURL = (String) cur_business.get("snippet_image_url");
                double rating = (double) cur_business.get("rating");
                JSONArray display = (JSONArray) loc.get("display_address");
                JSONObject coordinate = (JSONObject) loc.get("coordinate");


                String city_name = ((String) display.get(display.size() - 1)).replaceAll("[0-9]", "").trim();

                Attraction a =
                        new Attraction(attr_name, city_name, rating, attr_distance, picURL,
                                (double) coordinate.get("latitude"), (double) coordinate.get("longitude"));

                list.add(a);
            }
        }
        catch (Exception e){
            System.out.println("Exception in YelpQuery");


        }
//        catch (ParseException pe) {
//            System.out.println("Error: could not parse JSON response:");
//            System.out.println(output_query);
//            System.exit(1);
//        }

        return list;
    }
}
