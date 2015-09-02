package com.example.commonframe.core.connection;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RequestQueue.RequestFilter;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.example.commonframe.core.connection.request.BackgroundServiceRequest;
import com.example.commonframe.core.connection.ssl.EasySslSocketFactory;
import com.example.commonframe.core.connection.volley.BackgroundResponse;
import com.example.commonframe.util.Constant.RequestType;
import com.example.commonframe.util.DLog;

/**
 * @author Tyrael
 * @version 1.0 <br>
 * @since January 2014
 */
public class BackgroundServiceRequester implements
        Listener<BackgroundResponse>, ErrorListener {

    private static BackgroundServiceRequester instance;
    private static RequestQueue httpQueue;
    private static RequestQueue sslQueue;
    private final static String TAG = "BackgroundServiceRequester";

    public static BackgroundServiceRequester getInstance(Context context) {
        if (instance == null)
            instance = new BackgroundServiceRequester(context);
        return instance;
    }

    private BackgroundServiceRequester(Context context) {
        httpQueue = Volley.newRequestQueue(context);
        sslQueue = Volley.newRequestQueue(context, new HurlStack(null, EasySslSocketFactory.getEasySslSocketFactory()));
    }

    public void startRequest(BackgroundServiceRequest request) {
        if (request != null) {
            if (httpQueue != null
                    && request.getRequesType() == RequestType.HTTP) {
                httpQueue.add(request);
            }
            if (sslQueue != null
                    && request.getRequesType() == RequestType.HTTPS) {
                sslQueue.add(request);
            }
        }
    }

    public void stopRequest() {
        if (httpQueue != null)
            httpQueue.stop();
        if (sslQueue != null)
            sslQueue.stop();
    }

    public void cancelAll(Object tag) {
        if (tag == null) {
            cancelAllWithFilter(new RequestFilter() {

                @Override
                public boolean apply(Request<?> req) {
                    return true;
                }
            });
        } else {
            if (httpQueue != null)
                httpQueue.cancelAll(tag);
            if (sslQueue != null)
                sslQueue.cancelAll(tag);
        }
    }

    public void cancelAllWithFilter(RequestFilter filter) {
        if (httpQueue != null)
            httpQueue.cancelAll(filter);
        if (sslQueue != null)
            sslQueue.cancelAll(filter);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        DLog.d(TAG, "Background >> onErrorResponse >> " + error.getMessage());
    }

    @Override
    public void onResponse(BackgroundResponse response) {
        DLog.d(TAG, "Background >> onResponse >> "
                + response.getContent().length);
    }

}
