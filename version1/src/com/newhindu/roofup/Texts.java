package com.newhindu.roofup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;




import android.app.ProgressDialog;
//import com.newhindu.roofup.Maps.ConnectionDetector;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

 
public class Texts extends Fragment {
    protected static final String TAG = null;
    Boolean isInternetPresent = false;
    TextView textView;
    ListView listView ;
    
	@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
		
            View texts = inflater.inflate(R.layout.texts, container, false);
            //listView = (ListView)texts.findViewById(R.id.list);
          textView = (TextView) texts.findViewById(R.id.textView);
            //textView.onCreate(savedInstanceState);
         //   ((TextView)texts.findViewById(R.id.textView)).setText("Text");
      setTxtView();
    return texts;
            
           
}
	
	private void setTxtView() {	
      
        String url = "http://54.254.240.217:8080/app-task/projects";
        final Button detailButton;    
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show(); 
        
	    JsonArrayRequest req = new JsonArrayRequest(url,
	            new Response.Listener<JSONArray>() {
	                @Override
	                public void onResponse(JSONArray response) {
	                    Log.d(TAG, response.toString());
	 
	                    try {
	                        // Parsing json array response
	                        // loop through each json object
	                        String jsonResponse = "";
	                        //ArrayList<String> items = new ArrayList<String>();
	                        
	                        for (int i = 0; i < response.length(); i++) {
	 
	                            JSONObject project = (JSONObject) response
	                                    .get(i);
	 
	                            String pid = project.getString("id");
	                            String pname = project.getString("projectName");	                            
	                           // items.add(pname);
	                            String plat = project.getString("lat");
	                            String plon = project.getString("lon");
	                                
	                            //jsonResponse += "id: " + pid + "\n\n";
	                            jsonResponse += " "+ pname + "\n\n";
	                        }
	 
	                        //TextView textView = null;
	                        textView.setText(jsonResponse);
	                        //ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1,items);
	                        //listView.setAdapter(mArrayAdapter); 
	                        pDialog.hide(); 
	 
	                    } catch (JSONException e) {
	                        e.printStackTrace();
	                        Toast.makeText(getApplicationContext(),
	                                "Error: " + e.getMessage(),
	                                Toast.LENGTH_LONG).show();
	                    }
	 
	                   
	                }

					
	            }, new Response.ErrorListener() {
	                @Override
	                public void onErrorResponse(VolleyError error) {
	                    VolleyLog.d(TAG, "Error: " + error.getMessage());
	                    Toast.makeText(getApplicationContext(),
	                            error.getMessage(), Toast.LENGTH_SHORT).show();
	                   
	                }
	            });
	    pDialog.hide(); 
	 
	    // Adding request to request queue
	    AppController.getInstance().addToRequestQueue(req);
	}	
	private Context getApplicationContext() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
