package com.example.fortest.GPS;

import com.example.fortest.DisplayParameters;

public class LocationData {

    public static float distance; //met
    public static float speed; //met/sec
    public static float avrSpeed=-1; //met/sec
    public static long temp;
    public static String description;

    public static float getDistance() {
        if (DisplayParameters.displayMiles)
            return distance * 0.00062137f;//miles;
        return distance / 1000f;           //km
    }

    public static void resetDistance(){
        distance = 0;
    }
    public static void resetAvrSpeed(){ avrSpeed = 0; }

    public static int getSpeed() {
        if (DisplayParameters.displayMiles)
            return Math.round(speed * 2.24f); //MLH
        return Math.round(speed * 3.6f); //KMH

    }

    public static int getAvrSpeed() {
        if (DisplayParameters.displayMiles)
            return Math.round(avrSpeed * 2.24f);
        return Math.round(avrSpeed * 3.6f);
    }
}

