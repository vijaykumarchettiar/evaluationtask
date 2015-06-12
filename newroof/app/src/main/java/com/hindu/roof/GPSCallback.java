package com.hindu.roof;

import android.location.Location;

/**
 * Created by gowrishi on 12-06-2015.
 */
public interface GPSCallback {
    public abstract void onGPSUpdate(Location location);
}
