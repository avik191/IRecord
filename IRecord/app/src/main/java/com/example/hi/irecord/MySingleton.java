package com.example.hi.irecord;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by HI on 01-Apr-17.
 */

public class MySingleton {

    private static MySingleton minstance;
    private RequestQueue requestQueue;
    private static Context mctx;


    public MySingleton(Context context)
    {
        mctx=context;
        requestQueue=getRequestQueue();
    }

    public RequestQueue getRequestQueue()
    {
        if(requestQueue==null)
            requestQueue= Volley.newRequestQueue(mctx);
        return requestQueue;
    }

    public static synchronized MySingleton getInstance(Context context)
    {
        if(minstance==null)
            minstance=new MySingleton(context);
        return minstance;
    }

    public<T> void addtoRequestQueue(Request<T> request)
    {
        getRequestQueue().add(request);
    }
}
