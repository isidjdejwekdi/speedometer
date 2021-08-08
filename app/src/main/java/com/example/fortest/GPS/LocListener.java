package com.example.fortest.GPS;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;

public class LocListener implements LocationListener {
    private LocListenerInterface locListenerInterface;

    @Override
    public void onLocationChanged(@NonNull Location location) {
        locListenerInterface.onLocationChanged(location);
    }

    public void setLocListenerInterface(LocListenerInterface locListenerInterface) {
        this.locListenerInterface = locListenerInterface;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
    }
}

