package com.hindu.roof;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 *
 * Volley Helper for adding new request
 */

public class VolleyHelper {
	private static VolleyHelper instance;
	private RequestQueue requestQueue;
	private Context context;
    private static ImageLoader mImageLoader;
    private static final int MAX_IMAGE_CACHE_ENTIRES  = 100;



    /**
     * Constructor method
     * @param context
     */
	private VolleyHelper(Context context) {
		this.context = context;
		requestQueue = getRequestQueue();
        //mImageLoader = new ImageLoader(requestQueue, new BitmapLruCache(MAX_IMAGE_CACHE_ENTIRES));

    }

    /**
     * Singleton method
     *
     * @param context
     * @return always return same object
     */
	public static synchronized VolleyHelper getInstance(Context context) {
		if (instance == null) {
			instance = new VolleyHelper(context);

        }
		return instance;
	}

	public RequestQueue getRequestQueue() {
		if (requestQueue == null) {
			requestQueue = Volley.newRequestQueue(context
					.getApplicationContext());
		}
		return requestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req) {
		getRequestQueue().add(req);
	}

    public static ImageLoader getImageLoader() {
        if (mImageLoader != null) {
            return mImageLoader;
        } else {
            throw new IllegalStateException("ImageLoader not initialized");
        }
    }
}