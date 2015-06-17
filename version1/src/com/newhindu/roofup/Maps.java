package com.newhindu.roofup;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;








//import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
//import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
//import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
//import java.util.Collection;
public class Maps extends Fragment {
protected static final String TAG = null;
MapView mapView;
GoogleMap map;
LatLng CENTER = null;

public LocationManager locationManager;

double longitudeDouble;
double latitudeDouble;

String snippet;
String title;
Location location;
String myAddress;

String LocationId;
String CityName;
String imageURL;

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    View view = inflater
                .inflate(R.layout.maps, container, false);

    mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
  setMapView();
return view;


 }

 private void setMapView() {
    try {
        MapsInitializer.initialize(getActivity());

        switch (GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getActivity())) {
        case ConnectionResult.SUCCESS:
            // Toast.makeText(getActivity(), "SUCCESS", Toast.LENGTH_SHORT)
            // .show();

            // Gets to GoogleMap from the MapView and does initialization
            // stuff
            if (mapView != null) {
            	

                locationManager = ((LocationManager) getActivity()
                        .getSystemService(Context.LOCATION_SERVICE));

                Boolean localBoolean = Boolean.valueOf(locationManager
                        .isProviderEnabled("network"));

                if (localBoolean.booleanValue()) {

                    double latitude = 0;
					double longitude = 0;
					CENTER = new LatLng(latitude, longitude);

                } 
                
                map = mapView.getMap();
                
                if (map == null) {

                    Log.d("", "Map Fragment Not Found or no Map in it!!");

                }

                //map.clear();
                /*
                try {
                    map.addMarker(new MarkerOptions().position(CENTER)
                            .title(CityName).snippet(""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
*/
               

                	// Connection detector class
                
                String url;
                url = "http://54.254.240.217:8080/app-task/projects";
                
                final ProgressDialog pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Loading...");
                pDialog.show(); 
                
                JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.d(TAG, response.toString());
                                String toFile = response.toString();                                
                                FileWriter f;
                                try{
                                    f = new FileWriter(Environment.getExternalStorageDirectory() + "/roofandfloor.txt", 

        false);
                                    f.write(toFile);
                                    f.flush();
                                    f.close();
                                }catch (Exception e) {
                                    // TODO: handle exception
                                }
                                /*String filePath = Environment.getExternalStorageDirectory() + "/roofandfloor.txt";
                                try {                               	

                                    JSONArray obj = new JSONArray(filePath);
                                        for (int i = 0; i < obj.length(); i++) {
                                        JSONObject project = obj.getJSONObject(i);
                                        map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(project.get("lat").toString()), Double.parseDouble(project.get("lon").toString()))).title(project.get("projectName").toString()).icon(BitmapDescriptorFactory
                                                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
                                        if(i==obj.length()){
                                            double latitude = Double.parseDouble(project.get("lat").toString());
                                            double longitude = Double.parseDouble(project.get("lon").toString());
                                        
                                            final LatLng usrLoc = new LatLng(latitude, longitude);
                                            //map.moveCamera(CameraUpdateFactory.newLatLng(usrLoc));
                                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(usrLoc, 17);
                                            map.animateCamera(cameraUpdate);
                                           // CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitude,longitude)).zoom(12).build();
                                           // map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                            pDialog.hide();
                                    }
                                }} catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                */
                                
             
                                try {
                                    // Parsing json array response
                                    // loop through each json object
                                                                        for (int i = 0; i < response.length(); i++) {
             
                                        JSONObject project = (JSONObject) response.get(i);
                                        
                                        map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(project.get("lat").toString()), Double.parseDouble(project.get("lon").toString()))).title(project.get("projectName").toString()).icon(BitmapDescriptorFactory
                                                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
                                        if(i==response.length()-1){
                                        	double latval = Double.parseDouble(project.get("lat").toString());
                                        	double  lonval = Double.parseDouble(project.get("lon").toString());
                                           final LatLng usrLoc = new LatLng(latval, lonval);
                                           CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(usrLoc, 12);
                                           map.animateCamera(cameraUpdate);
                                           map.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(latval, lonval) , 12.0f) );
             
                                    }
             
                                  //  txtResponse.setText(jsonResponse); g
                               pDialog.hide();
                      
							
							
                               
/*.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() { 
                            	   @Override 
                            	   public void onMapLoaded() { 
                            	       map.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 15));
                            	    } 
                            	   });*/
                                }} catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(),
                                            "Error: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
             
                            
                            }

							private Context getApplicationContext() {
								// TODO Auto-generated method stub
								return null;
							}
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d(TAG, "Error: " + error.getMessage());
                                Toast.makeText(getApplicationContext(),
                                        error.getMessage(), Toast.LENGTH_SHORT).show();
                               
                              
                            }

							private Context getApplicationContext() {
								// TODO Auto-generated method stub
								return null;
							}
                        });
                
                AppController.getInstance().addToRequestQueue(req);
                
                
            }
            

        }
    } catch (Exception e) {

    }

}

private Object getApplicationContext() {
	// TODO Auto-generated method stub
	return null;
}
 
}
