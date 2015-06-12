package com.hindu.roof;

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements TabHost.OnTabChangeListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private double latitude = 0.0;
    private double longitude = 0.0;
    private static final String LIST_TAB_TAG = "List";
    private static final String MAP_TAB_TAG = "Map";

    private TabHost tabHost;

    private ListView listView;

    private MapView mapView;

    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        // setup must be called if you are not inflating the tabhost from XML
        tabHost.setup();
        tabHost.setOnTabChangedListener(this);
        setUpMapIfNeeded();
        setUpListView();
        tabHost.addTab(tabHost.newTabSpec(MAP_TAB_TAG).setIndicator("Map").setContent(new TabHost.TabContentFactory() {

            public View createTabContent(String arg0) {

                return mapView;

            }

        }));
        tabHost.addTab(tabHost.newTabSpec(LIST_TAB_TAG).setIndicator("List").setContent(new TabHost.TabContentFactory() {

            public View createTabContent(String arg0) {

                return listView;

            }

        }));

        //HACK to get the list view to show up first,

        // otherwise the mapview would be bleeding through and visibl
        tabHost.setCurrentTab(1);
        tabHost.setCurrentTab(0);


    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();

    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpListView() {

        // setup must be called if you are not inflating the tabhost from XML
        tabHost.setup();
        tabHost.setOnTabChangedListener(this);

        // setup list view
        listView = (ListView) findViewById(R.id.list);
        listView.setEmptyView((TextView) findViewById(R.id.empty));
        List<JSONArray> projectName = new ArrayList<JSONArray>();
        listView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, projectName));
    }
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if(null == mMap) {
                Toast.makeText(getApplicationContext(),
                        "Error creating map", Toast.LENGTH_SHORT).show();
            }

            if (mMap != null) {
                getJson();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    private void getJson(){

        cd = new ConnectionDetector(getApplicationContext());
        String url;
        // Check if Internet present
        isInternetPresent = cd.isConnectingToInternet();
        if (!isInternetPresent) {
            // Internet Connection is not present
            url = Environment.getExternalStorageDirectory() + "/roofandfloor.txt";
        }

       else {
            url = "http://54.254.240.217:8080/app-task/projects";
        }

        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, url, "", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.i("Response ", response.toString());
                String toFile = response.toString();
                FileWriter f;
                try{
                    f = new FileWriter(Environment.getExternalStorageDirectory() + "/roofandfloor.txt", true);
                    f.write(toFile);
                    f.flush();
                    f.close();
                }catch (Exception e) {
                    // TODO: handle exception
                }

                for (int i = 0; i < response.length(); i++){

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(jsonObject.get("lat").toString()), Double.parseDouble(jsonObject.get("lon").toString()))).title(jsonObject.get("projectName").toString()));

                                        } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                //Move the camera instantly to the user's location with a zoom of 15.

                final LatLng usrLoc = new LatLng(latitude, longitude);

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(usrLoc, 15));

             // Zoom in, animating the camera.
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("Response ", error.toString());

            }
        });
        // Adding request to request queue
        VolleyHelper.getInstance(this).addToRequestQueue(jsObjRequest);

    }

    public void onGPSUpdate(Location location) {
        // TODO Auto-generated method stub
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onTabChanged(String tabName) {

        if(tabName.equals(MAP_TAB_TAG)) {

            //do something on the map

        }

        else if(tabName.equals(LIST_TAB_TAG)) {

            //do something on the list

        }


    }


    public class ConnectionDetector {

        private Context _context;

        public ConnectionDetector(Context context){
            this._context = context;
        }

        /**
         * Checking for all possible internet providers
         * **/
        public boolean isConnectingToInternet(){
            ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null)
            {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null)
                    for (int i = 0; i < info.length; i++)
                        if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        {
                            return true;
                        }

            }
            return false;
        }
    }
}
