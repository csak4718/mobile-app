package com.mycompany.geocoding3;

/**
 * Created by de-weikung on 3/4/15.
 */

public class Attraction {
    private String name;
    private String cityName;
    private double rating;
    private double distanceFromStart;
    private String picURL;
    private double latitude;
    private double longitude;
//    Weather weather;

    //possibly adding addresses, directions, descriptions, review list...


    public Attraction(){}
    public Attraction(String n, String cn, double r, double dfs, String pu, double lat, double lng){
        name = n;
        cityName = cn; //city, STATE
        rating = r;
        distanceFromStart = dfs;
        picURL = pu;
//        weather = null;
        latitude = lat;
        longitude = lng;
    }

    public String getName(){
        return name;
    }

    public String getCity(){
        return cityName;
    }

    public double getRating(){
        return rating;
    }

    public double getDistanceFromStart(){
        return distanceFromStart;
    }

    public double getLatitude(){ return latitude; }

    public double getLongitude(){ return longitude; }

//    public void setWeather(Weather w){
//        weather = w;
//    }
//
//    public Weather getWeather(){
//        return weather;
//    }

    public String getPicURL(){
        return picURL;
    }
}
