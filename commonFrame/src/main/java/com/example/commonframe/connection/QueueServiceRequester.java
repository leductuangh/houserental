package com.example.commonframe.connection;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RequestQueue.RequestFilter;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.commonframe.R;
import com.example.commonframe.connection.queue.WebserviceElement;
import com.example.commonframe.connection.request.QueueServiceRequest;
import com.example.commonframe.connection.ssl.ExtHttpClientStack;
import com.example.commonframe.connection.ssl.SslHttpClient;
import com.example.commonframe.model.base.BaseResult;
import com.example.commonframe.model.volley.QueueError;
import com.example.commonframe.model.volley.QueueResponse;
import com.example.commonframe.parser.BaseParser;
import com.example.commonframe.util.CentralApplication;
import com.example.commonframe.util.Constant;
import com.example.commonframe.util.Constant.RequestTarget;
import com.example.commonframe.util.Constant.RequestType;
import com.example.commonframe.util.Constant.StatusCode;
import com.example.commonframe.util.DLog;
import com.example.commonframe.util.Utils;

import java.util.ArrayList;
import java.util.WeakHashMap;

/**
 * @author Tyrael
 * @version 1.0 <br>
 * @since July 2015
 */
public class QueueServiceRequester implements Listener<QueueResponse>,
        ErrorListener {

    public interface QueueServiceListener {

        /**
         * <b>Specified by:</b> onStartQueue(...) in QueueServiceListener <br>
         * <br>
         * This is called immediately when an element in the queue is started.
         * The request queue only starts when the Internet is available
         *
         * @param element The element is started
         */
        public void onStartQueue(WebserviceElement element);

        /**
         * <b>Specified by:</b> onFinishQueue(...) in QueueServiceListener <br>
         * <br>
         * This is called immediately all the elements in the queue have
         * finished.
         */
        public void onFinishQueue();

        /**
         * <b>Specified by:</b> onBlockQueue(...) in QueueServiceListener <br>
         * <br>
         * This is called immediately when an element in the queue has been
         * failed by network error and the element has BLOCK type. Other
         * elements in the queue are remaining and waiting for user
         * interactions.
         *
         * @param element The element is blocked at
         */
        public void onBlockQueue(WebserviceElement element);

        /**
         * <b>Specified by:</b> onStopQueue(...) in QueueServiceListener <br>
         * <br>
         * This is called immediately when an element in the queue has been
         * failed by network error and the element has STOP type. Other elements
         * in the queue are removed and returned for handling
         *
         * @param remain The elements remaining in the queue include the stopped
         *               element
         */
        public void onStopQueue(ArrayList<WebserviceElement> remain);

        /**
         * <b>Specified by:</b> onResultSuccess(...) in QueueServiceListener <br>
         * <br>
         * This is called immediately after the data is being successfully
         * retrieved.
         *
         * @param result The BaseResult or derived class instance return.
         */
        public void onResultSuccess(BaseResult result);

        /**
         * <b>Specified by:</b> onResultFail(...) in QueueServiceListener <br>
         * <br>
         * This is called immediately after the data is being successfully
         * retrieved as a failure on the server.
         *
         * @param result The BaseResult or derived class instance return.
         */
        public void onResultFail(BaseResult result);

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
        public void onFail(RequestTarget target, String error, StatusCode code);
    }

    private enum Notify {
        START, // notify when an element in queue is requested
        FINISH, // notify when all elements have been sent
        BLOCK, // notify when a block-type element is failed to request, all remaining requests are kept and wait for user actions
        STOP, // notify when a stop-type element is failed to request, all remaining requests are removed and returned to handle
        RESULT_SUCCESS, // notify when a request is returned successfully with status success
        RESULT_FAIL, // notify when a request is returned successfully with status failed
        FAIL // notify when a request is failed to request
    }

    private static final WeakHashMap<Object, QueueServiceListener> listeners = new WeakHashMap<Object, QueueServiceListener>();
    private static final ArrayList<WebserviceElement> queue = new ArrayList<WebserviceElement>();
    private static boolean isRequesting = false;
    private static QueueServiceRequester instance;
    private static RequestQueue httpQueue;
    private static RequestQueue sslQueue;
    private final static String TAG = "QueueServiceRequester";

    public static QueueServiceRequester getInstance(Context context) {
        if (instance == null)
            instance = new QueueServiceRequester(context);
        return instance;
    }

    private QueueServiceRequester(Context context) {
        httpQueue = Volley.newRequestQueue(context);
        sslQueue = Volley.newRequestQueue(context, new ExtHttpClientStack(
                new SslHttpClient(Constant.KEY_STORE_ID,
                        Constant.KEY_STORE_PASSWORD, Constant.KEY_STORE_TYPE)));
    }

    public static void startQueueRequest() {
        if (queue.size() > 0) {
            if (!isRequesting) {
                if (Utils.isInternetAvailable()) {
                    notifyListeners(Notify.START, queue.get(0), null, null,
                            null, null);
                    startRequest(queue.get(0).getRequest());
                }
            }
        } else {
            notifyListeners(Notify.FINISH, null, null, null, null, null);
        }
    }

    public void addQueueRequest(WebserviceElement element) {
        if (!queue.contains(element))
            queue.add(element);
        startQueueRequest();
    }

    public static int getQueueSize() {
        return queue.size();
    }

    public static boolean isRequesting() {
        return isRequesting;
    }

    public static void clearQueueRequest() {
        isRequesting = false;
        cancelAll(null);
        stopRequest();
        queue.clear();
    }

    public static void clearListener() {
        listeners.clear();
    }

    public static void registerListener(QueueServiceListener listener) {
        if (listener != null) {
            listeners.put(listener, listener);
        }
    }

    public static void removeListener(QueueServiceListener listener) {
        listeners.remove(listener);
    }

    private static void startRequest(QueueServiceRequest request) {
        if (request != null) {
            isRequesting = true;
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

    public static void stopRequest() {
        isRequesting = false;
        if (httpQueue != null)
            httpQueue.stop();
        if (sslQueue != null)
            sslQueue.stop();
    }

    public static void cancelAll(Object tag) {
        isRequesting = false;
        if (tag == null) {
            cancelAllWithFilter(new RequestFilter() {

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
    }

    public static void cancelAllWithFilter(RequestFilter filter) {
        isRequesting = false;
        if (httpQueue != null)
            httpQueue.cancelAll(filter);
        if (sslQueue != null)
            sslQueue.cancelAll(filter);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        DLog.d(TAG, "Queue >> onErrorResponse >> " + error.getMessage());
        isRequesting = false;
        Throwable cause = error.getCause();
        String error_message = CentralApplication.getContext().getString(
                R.string.error_unknown);
        StatusCode error_code = StatusCode.ERR_UNKNOWN;
        if (cause != null) {
            if (cause instanceof NoConnectionError) {
                error_message = CentralApplication.getContext().getString(
                        R.string.error_connection_fail);
                error_code = StatusCode.ERR_NO_CONNECTION;
            } else if (cause instanceof NetworkError) {
                error_message = CentralApplication.getContext().getString(
                        R.string.error_connection_fail);
                error_code = StatusCode.ERR_NO_CONNECTION;
            } else if (cause instanceof ServerError) {
                error_message = CentralApplication.getContext().getString(
                        R.string.error_server_fail);
                error_code = StatusCode.ERR_SERVER_FAIL;
            } else if (cause instanceof AuthFailureError) {
                error_message = CentralApplication.getContext().getString(
                        R.string.error_auth_fail);
                error_code = StatusCode.ERR_AUTH_FAIL;
            } else if (cause instanceof ParseError) {
                error_message = CentralApplication.getContext().getString(
                        R.string.error_parsing_fail);
                error_code = StatusCode.ERR_PARSING;
            } else if (cause instanceof TimeoutError) {
                error_message = CentralApplication.getContext().getString(
                        R.string.error_conneciton_time_out);
                error_code = StatusCode.ERR_TIME_OUT;
            }
        }
        if (error instanceof QueueError) {
            QueueError queue_error = (QueueError) error;
            if (Constant.NETWORK_ERROR_DATA_HANDLE) {
                NetworkResponse response = queue_error.getResponse();
                if (response != null && response.headers != null
                        && response.data != null) {
                    onResponse(new QueueResponse(response.data,
                            response.headers, queue_error.getRequestTarget()));
                    return;
                } else {
                    notifyListeners(Notify.FAIL, null, null,
                            queue_error.getRequestTarget(), error_message,
                            error_code);
                }
            } else
                notifyListeners(Notify.FAIL, null, null,
                        queue_error.getRequestTarget(), error_message,
                        error_code);

            handleQueueFail();
        }

    }

    @Override
    public void onResponse(QueueResponse response) {
        DLog.d(TAG,
                "Queue >> onResponse >> " + new String(response.getContent()));
        isRequesting = false;
        BaseResult result = BaseParser.parse(new String(response.getContent()),
                response.getRequestTarget());
        if (result != null) {
            result.setHeaders(response.getHeaders());
            if (result.getStatus() == StatusCode.OK) {
                notifyListeners(Notify.RESULT_SUCCESS, null, result, null,
                        null, null);
                handleQueueSuccess();
            } else {
                notifyListeners(Notify.RESULT_FAIL, null, result, null, null,
                        null);
                handleQueueSuccess();
            }
        } else {
            notifyListeners(Notify.FAIL, null, null,
                    response.getRequestTarget(), CentralApplication
                            .getContext()
                            .getString(R.string.error_parsing_fail),
                    StatusCode.ERR_PARSING);
            handleQueueFail();
        }
    }

    private void handleQueueSuccess() {
        queue.remove(0);
        startQueueRequest();
    }

    private void handleQueueFail() {
        if (queue.size() > 0) {
            WebserviceElement element = queue.get(0);
            switch (element.getType()) {
                case RETRY:
                    startQueueRequest();
                    return;
                case PASS:
                    queue.remove(element);
                    startQueueRequest();
                    return;
                case BLOCK:
                    notifyListeners(Notify.BLOCK, element, null, null, null, null);
                    return;
                case STOP:
                    notifyListeners(Notify.STOP, null, null, null, null, null);
                    queue.clear();
                    return;
                default:
                    break;
            }
        } else {
            notifyListeners(Notify.FINISH, null, null, null, null, null);
        }
    }

    private static void notifyListeners(Notify notify,
                                        WebserviceElement element, BaseResult result, RequestTarget target,
                                        String error, StatusCode code) {
        for (QueueServiceListener listener : listeners.values()) {
            if (listener != null) {
                switch (notify) {
                    case START:
                        listener.onStartQueue(element);
                        break;
                    case FINISH:
                        listener.onFinishQueue();
                        break;
                    case BLOCK:
                        listener.onBlockQueue(element);
                        break;
                    case STOP:
                        listener.onStopQueue(new ArrayList<WebserviceElement>(
                                QueueServiceRequester.queue));
                        break;
                    case RESULT_SUCCESS:
                        listener.onResultSuccess(result);
                        break;
                    case RESULT_FAIL:
                        listener.onResultFail(result);
                        break;
                    case FAIL:
                        listener.onFail(target, error, code);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
