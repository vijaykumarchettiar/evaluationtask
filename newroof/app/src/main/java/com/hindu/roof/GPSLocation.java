package com.hindu.roof;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.List;

/**
 * Created by gowrishi on 9-06-2015.
 */
public class GPSLocation {
    
        private static final int gpsMinTime = 500;
        private static final int gpsMinDistance = 0;

        private static LocationManager locationManager = null;
        private static LocationListener locationListener = null;
        private static GPSCallback gpsCallback = null;

        public GPSLocation()
        {
            GPSLocation.locationListener = new LocationListener()
            {
                public void onLocationChanged(final Location location)
                {
                    if (GPSLocation.gpsCallback != null)
                    {
                        GPSLocation.gpsCallback.onGPSUpdate(location);
                    }
                }

                public void onProviderDisabled(final String provider)
                {
                }

                public void onProviderEnabled(final String provider)
                {
                }

                public void onStatusChanged(final String provider, final int status, final Bundle extras)
                {
                }
            };
        }

        public GPSCallback getGPSCallback()
        {
            return GPSLocation.gpsCallback;
        }

        public void setGPSCallback(final GPSCallback gpsCallback)
        {
            GPSLocation.gpsCallback = gpsCallback;
        }

        public void startListening(final Context context)
        {
            if (GPSLocation.locationManager == null)
            {
                GPSLocation.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            }

            final Criteria criteria = new Criteria();

            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setSpeedRequired(true);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);
            criteria.setPowerRequirement(Criteria.POWER_LOW);

            final String bestProvider = GPSLocation.locationManager.getBestProvider(criteria, true);

            if (bestProvider != null && bestProvider.length() > 0)
            {
                GPSLocation.locationManager.requestLocationUpdates(bestProvider, GPSLocation.gpsMinTime,
                        GPSLocation.gpsMinDistance, GPSLocation.locationListener);
            }
            else
            {
                final List<String> providers = GPSLocation.locationManager.getProviders(true);

                for (final String provider : providers)
                {
                    GPSLocation.locationManager.requestLocationUpdates(provider, GPSLocation.gpsMinTime,
                            GPSLocation.gpsMinDistance, GPSLocation.locationListener);
                }
            }
        }

        public void stopListening()
        {
            try
            {
                if (GPSLocation.locationManager != null && GPSLocation.locationListener != null)
                {
                    GPSLocation.locationManager.removeUpdates(GPSLocation.locationListener);
                }

                GPSLocation.locationManager = null;
            }
            catch (final Exception ex)
            {

            }
        }
    }
