package core.connection;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.WeakHashMap;

import core.base.BaseApplication;
import core.base.BaseParser;
import core.base.BaseResult;
import core.connection.request.ParallelServiceRequest;
import core.connection.ssl.EasySslSocketFactory;
import core.connection.ssl.TrustedSslSocketFactory;
import core.connection.volley.ParallelError;
import core.connection.volley.ParallelResponse;
import core.util.Constant;
import core.util.DLog;

@SuppressWarnings({"WeakerAccess", "unused"})
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
        DLog.d(TAG, "Parallel >> onErrorResponse >> " + error.getMessage());
        Throwable cause = error.getCause();
        String error_message = BaseApplication.getContext().getString(
                com.example.houserental.R.string.error_unknown);
        Constant.StatusCode error_code = Constant.StatusCode.ERR_UNKNOWN;
        if (cause != null) {
            if (cause instanceof NoConnectionError) {
                error_message = BaseApplication.getContext().getString(
                        com.example.houserental.R.string.error_connection_fail);
                error_code = Constant.StatusCode.ERR_NO_CONNECTION;
            } else if (cause instanceof NetworkError) {
                error_message = BaseApplication.getContext().getString(
                        com.example.houserental.R.string.error_connection_fail);
                error_code = Constant.StatusCode.ERR_NO_CONNECTION;
            } else if (cause instanceof ServerError) {
                error_message = BaseApplication.getContext().getString(
                        com.example.houserental.R.string.error_server_fail);
                error_code = Constant.StatusCode.ERR_SERVER_FAIL;
            } else if (cause instanceof AuthFailureError) {
                error_message = BaseApplication.getContext().getString(
                        com.example.houserental.R.string.error_auth_fail);
                error_code = Constant.StatusCode.ERR_AUTH_FAIL;
            } else if (cause instanceof ParseError) {
                error_message = BaseApplication.getContext().getString(
                        com.example.houserental.R.string.error_parsing_fail);
                error_code = Constant.StatusCode.ERR_PARSING;
            } else if (cause instanceof TimeoutError) {
                error_message = BaseApplication.getContext().getString(
                        com.example.houserental.R.string.error_conneciton_time_out);
                error_code = Constant.StatusCode.ERR_TIME_OUT;
            }
        }
        if (error instanceof ParallelError) {
            ParallelError p_error = (ParallelError) error;
            if (Constant.NETWORK_ERROR_DATA_HANDLE) {
                NetworkResponse response = p_error.getResponse();
                if (response != null && response.headers != null
                        && response.rawHeaders != null && response.data != null)
                    onResponse(new ParallelResponse(response.data,
                            response.headers, response.rawHeaders, p_error.getRequestTarget(), p_error.getTag()));
                else
                    notifyListeners(Notify.FAIL, null, p_error.getRequestTarget(), p_error.getTag(), error_message, error_code);
            } else
                notifyListeners(Notify.FAIL, null, p_error.getRequestTarget(), p_error.getTag(), error_message, error_code);
        } else {
            notifyListeners(Notify.FAIL, null, null, null, error_message, error_code);
        }
        handleQueue();
    }

    @Override
    public void onResponse(ParallelResponse response) {
        DLog.d(TAG, "Parallel >> onResponse >> " + new String(response.getContent()));
        BaseResult result = BaseParser.parse(new String(response.getContent()),
                response.getRequestTarget());
        if (result != null) {
            result.setHeaders(response.getHeaders());
            result.setRawHeaders(response.getRawHeaders());
            if (result.getStatus() == Constant.StatusCode.OK)
                notifyListeners(Notify.RESULT_SUCCESS, result, response.getRequestTarget(), response.getTag(), null, null);
            else
                notifyListeners(Notify.RESULT_FAIL, result, response.getRequestTarget(), response.getTag(), null, null);
        } else {
            notifyListeners(Notify.FAIL, null, response.getRequestTarget(), response.getTag(), BaseApplication
                    .getContext().getString(com.example.houserental.R.string.error_parsing_fail), Constant.StatusCode.ERR_PARSING);
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

    private void notifyListeners(Notify status, BaseResult result, Constant.RequestTarget target, String tag, String error, Constant.StatusCode code) {
        for (ParallelServiceListener listener : listeners.values()) {
            switch (status) {
                case RESULT_SUCCESS:
                    listener.onResultSuccess(result);
                    break;
                case RESULT_FAIL:
                    listener.onResultFail(result);
                    break;
                case FAIL:
                    listener.onFail(target, tag, error, code);
                    break;
            }
        }
    }

    private enum Notify {
        RESULT_SUCCESS, // notify when a request is returned successfully with status success
        RESULT_FAIL, // notify when a request is returned successfully with status failed
        FAIL // notify when a request is failed to request
    }

    public interface ParallelServiceListener {

        /**
         * <b>Specified by:</b> onResultSuccess(...) in ParallelServiceListener <br>
         * <br>
         * This is called immediately after the data is being successfully
         * retrieved.
         *
         * @param result The BaseResult or derived class instance return.
         */
        void onResultSuccess(BaseResult result);

        /**
         * <b>Specified by:</b> onResultFail(...) in ParallelServiceListener <br>
         * <br>
         * This is called immediately after the data is being successfully
         * retrieved as a failure on the server.
         *
         * @param result The BaseResult or derived class instance return.
         */
        void onResultFail(BaseResult result);

        /**
         * <b>Specified by:</b> onFail(...) in ParallelServiceListener <br>
         * <br>
         * This is called immediately after request is being fail to process due
         * to the network errors
         *
         * @param target The target request had been called on
         * @param tag    The tag of request has been call from
         * @param error  The string explaining the failure of the request
         * @param code   The code indicating the type of failure
         */
        void onFail(Constant.RequestTarget target, String tag, String error, Constant.StatusCode code);
    }
}
