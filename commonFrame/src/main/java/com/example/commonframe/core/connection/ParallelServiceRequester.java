package com.example.commonframe.core.connection;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.example.commonframe.core.base.BaseResult;
import com.example.commonframe.core.connection.request.ParallelServiceRequest;
import com.example.commonframe.core.connection.ssl.EasySslSocketFactory;
import com.example.commonframe.core.connection.ssl.TrustedSslSocketFactory;
import com.example.commonframe.core.connection.volley.ParallelResponse;
import com.example.commonframe.util.Constant;
import com.example.commonframe.util.DLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.WeakHashMap;

public final class ParallelServiceRequester implements Response.Listener<ParallelResponse>, Response.ErrorListener {

    public static final String TAG = ParallelServiceRequester.class.getSimpleName();
    private static final WeakHashMap<Object, ParallelServiceListener> listeners = new WeakHashMap<>();
    private static final ArrayList<ParallelServiceRequest> queue = new ArrayList<>();
    private static final int CONNECTIONS_LIMIT = 4;
    private static int currentRequestingConnection = 0;
    private static ParallelServiceRequester instance;
    private static RequestQueue httpQueue;
    private static RequestQueue sslQueue;

    private ParallelServiceRequester(Context context) {
        httpQueue = Volley.newRequestQueue(context);
        sslQueue = Volley.newRequestQueue(context, new HurlStack(null, Constant.SSL_ENABLED ?
                TrustedSslSocketFactory.getTrustedSslSocketFactory(context,
                        Constant.KEY_STORE_TYPE,
                        Constant.KEY_STORE_ID,
                        Constant.KEY_STORE_PASSWORD)
                : EasySslSocketFactory.getEasySslSocketFactory()));
    }

    public static ParallelServiceRequester getInstance(Context context) {
        if (instance == null)
            instance = new ParallelServiceRequester(context);
        return instance;
    }

    public static void addRequest(ParallelServiceRequest request) {
        if (request != null) {
            if (currentRequestingConnection >= CONNECTIONS_LIMIT) {
                queue.add(request);
            } else {
                startRequest(request);
            }
        }
    }

    public static void registerListener(ParallelServiceListener listener) {
        if (listener != null) {
            listeners.put(listener, listener);
        }
    }

    public static void removeListener(ParallelServiceListener listener) {
        listeners.remove(listener);
    }

    private static void startRequest(ParallelServiceRequest request) {
        if (request != null) {
            if (currentRequestingConnection < CONNECTIONS_LIMIT) {
                if (httpQueue != null
                        && request.getRequestType() == Constant.RequestType.HTTP) {
                    currentRequestingConnection++;
                    httpQueue.add(request);
                }
                if (sslQueue != null
                        && request.getRequestType() == Constant.RequestType.HTTPS) {
                    currentRequestingConnection++;
                    sslQueue.add(request);
                }
            }
        }
    }

    public static void cancelAll(Object tag) {
        if (tag == null) {
            cancelAllWithFilter(new RequestQueue.RequestFilter() {

                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });
        } else {
            if (httpQueue != null)
                httpQueue.cancelAll(tag);
            if (sslQueue != null)
                sslQueue.cancelAll(tag);
        }
        queue.clear();
        currentRequestingConnection = 0;
    }

    public static void cancelAllWithFilter(RequestQueue.RequestFilter filter) {
        if (httpQueue != null)
            httpQueue.cancelAll(filter);
        if (sslQueue != null)
            sslQueue.cancelAll(filter);
        queue.clear();
        currentRequestingConnection = 0;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        handleQueue();
    }

    @Override
    public void onResponse(ParallelResponse response) {
        String res = new String(response.getContent());

        try {
            JSONObject object = new JSONObject(res);
            String message = (String) object.get("message");
            String time = (String) object.get("time");
            DLog.d(TAG, message + " " + time + " seconds");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        handleQueue();
    }

    private void handleQueue() {
        if (currentRequestingConnection > 0) {
            currentRequestingConnection--;
            if (queue.size() > 0) {
                startRequest(queue.get(queue.size() - 1));
            }
        }
    }

    public interface ParallelServiceListener {

        /**
         * <b>Specified by:</b> onResultSuccess(...) in QueueServiceListener <br>
         * <br>
         * This is called immediately after the data is being successfully
         * retrieved.
         *
         * @param result The BaseResult or derived class instance return.
         */
        void onResultSuccess(BaseResult result);

        /**
         * <b>Specified by:</b> onResultFail(...) in QueueServiceListener <br>
         * <br>
         * This is called immediately after the data is being successfully
         * retrieved as a failure on the server.
         *
         * @param result The BaseResult or derived class instance return.
         */
        void onResultFail(BaseResult result);

        /**
         * <b>Specified by:</b> onFail(...) in QueueServiceListener <br>
         * <br>
         * This is called immediately after request is being fail to process due
         * to the network errors
         *
         * @param target The target request had been called on
         * @param error  The string explaining the failure of the request
         * @param code   The code indicating the type of failure
         */
        void onFail(Constant.RequestTarget target, String error, Constant.StatusCode code);
    }
}
