package core.connection;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.WeakHashMap;

import core.connection.request.FileRequest;
import core.connection.ssl.EasySslSocketFactory;
import core.connection.ssl.TrustedSslSocketFactory;
import core.connection.volley.FileError;
import core.connection.volley.FileResponse;
import core.connection.volley.ParallelError;
import core.util.Constant;
import core.util.DLog;

/**
 * Created by Tyrael on 8/9/16.
 */
public class FileRequester implements Response.Listener<FileResponse>, Response.ErrorListener {

    private static final String TAG = FileRequester.class.getSimpleName();
    private static final WeakHashMap<Object, FileResultHandler> listeners = new WeakHashMap<>();
    private static final ArrayList<FileRequest> queue = new ArrayList<>();
    private static final int CONNECTIONS_LIMIT = 4;
    private static int currentRequestingConnection = 0;
    private static FileRequester instance;
    private static RequestQueue httpQueue;
    private static RequestQueue sslQueue;

    private FileRequester(Context context) {
        httpQueue = Volley.newRequestQueue(context);
        sslQueue = Volley.newRequestQueue(context, new HurlStack(null, Constant.SSL_ENABLED ?
                TrustedSslSocketFactory.getTrustedSslSocketFactory(context,
                        Constant.KEY_STORE_TYPE,
                        Constant.KEY_STORE_ID,
                        Constant.KEY_STORE_PASSWORD)
                : EasySslSocketFactory.getEasySslSocketFactory()));
    }

    public static FileRequester getInstance(Context context) {
        if (instance == null)
            instance = new FileRequester(context);
        return instance;
    }

    public static void addRequest(FileRequest request) {
        if (request != null) {
            if (currentRequestingConnection >= CONNECTIONS_LIMIT) {
                queue.add(request);
            }
            startRequest(request);
        }
    }

    private static void startRequest(FileRequest request) {
        if (request != null) {
            if (currentRequestingConnection < CONNECTIONS_LIMIT) {
                if (httpQueue != null
                        && request.getRequestType() == Constant.RequestType.HTTP) {
                    currentRequestingConnection++;
                    httpQueue.add(request);
                    queue.remove(request);
                }
                if (sslQueue != null
                        && request.getRequestType() == Constant.RequestType.HTTPS) {
                    currentRequestingConnection++;
                    sslQueue.add(request);
                    queue.remove(request);
                }
            }
        }
    }

    public static void registerListener(FileResultHandler listener) {
        if (listener != null) {
            listeners.put(listener, listener);
        }
    }

    public static void removeListener(FileResultHandler listener) {
        listeners.remove(listener);
    }

    public void stopRequest() {
        if (httpQueue != null)
            httpQueue.stop();
        if (sslQueue != null)
            sslQueue.stop();
    }

    public void cancelAll(Object tag) {
        if (tag == null) {
            cancelAllWithFilter(new RequestQueue.RequestFilter() {

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
            queue.clear();
            currentRequestingConnection = 0;
        }
    }

    public void cancelAllWithFilter(RequestQueue.RequestFilter filter) {
        if (httpQueue != null)
            httpQueue.cancelAll(filter);
        if (sslQueue != null)
            sslQueue.cancelAll(filter);
        queue.clear();
        currentRequestingConnection = 0;
    }

    private void handleQueue() {
        if (currentRequestingConnection > 0) {
            currentRequestingConnection--;
            if (queue.size() > 0) {
                startRequest(queue.get(0));
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        DLog.d(TAG, "File >> onErrorResponse >> " + error.getMessage());
        Throwable cause = error.getCause();
        Constant.StatusCode error_code = Constant.StatusCode.ERR_UNKNOWN;
        if (cause != null) {
            if (cause instanceof NoConnectionError) {
                error_code = Constant.StatusCode.ERR_NO_CONNECTION;
            } else if (cause instanceof NetworkError) {
                error_code = Constant.StatusCode.ERR_NO_CONNECTION;
            } else if (cause instanceof ServerError) {
                error_code = Constant.StatusCode.ERR_SERVER_FAIL;
            } else if (cause instanceof AuthFailureError) {
                error_code = Constant.StatusCode.ERR_AUTH_FAIL;
            } else if (cause instanceof ParseError) {
                error_code = Constant.StatusCode.ERR_PARSING;
            } else if (cause instanceof TimeoutError) {
                error_code = Constant.StatusCode.ERR_TIME_OUT;
            }
        }
        if (error instanceof ParallelError) {
            FileError file_error = (FileError) error;
            notifyListeners(file_error.getRequestTarget(), error_code, file_error.getUrl(), file_error.getFile());
        }
        handleQueue();
    }

    @Override
    public void onResponse(FileResponse response) {
        DLog.d(TAG, "File >> onResponse >> " + response.getUrl() + " >> " + response.getFile());
        try {
            File file = new File(response.getFile());
            if (file.exists())
                file.delete();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(response.getFile()));
            bos.write(response.getContent());
            bos.flush();
            bos.close();
            notifyListeners(response.getRequestTarget(), Constant.StatusCode.OK, response.getUrl(), response.getFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            notifyListeners(response.getRequestTarget(), Constant.StatusCode.ERR_STORE_FILE, response.getUrl(), response.getFile());
        } catch (IOException e) {
            e.printStackTrace();
            notifyListeners(response.getRequestTarget(), Constant.StatusCode.ERR_STORE_FILE, response.getUrl(), response.getFile());
        } catch (Exception e) {
            e.printStackTrace();
            notifyListeners(response.getRequestTarget(), Constant.StatusCode.ERR_STORE_FILE, response.getUrl(), response.getFile());
        }
        handleQueue();
    }

    private void notifyListeners(Constant.RequestTarget target, Constant.StatusCode status, String url, String file) {
        for (FileResultHandler listener : listeners.values()) {
            if (status == Constant.StatusCode.OK)
                listener.onSuccess(url, file, target);
            else
                listener.onFail(url, file, target, status);
        }
    }

    public interface FileResultHandler {
        /**
         * <b>Specified by:</b> onSuccess(...) in FileResultHandler <br>
         * <br>
         * This is called immediately after the file is being successfully
         * downloaded and stored into a pre-defined path.
         *
         * @param url    The download url
         * @param file   The path where the downloaded file is stored
         * @param target The target request had been called
         */
        void onSuccess(String url, String file, Constant.RequestTarget target);

        /**
         * <b>Specified by:</b> onFail(...) in FileResultHandler <br>
         * <br>
         * This is called immediately after request is being fail to process downloading
         * or storing the file into a pre-defined path.
         *
         * @param url    The download url
         * @param file   The path where the downloaded file is stored
         * @param target The target request had been called
         * @param code   The code indicating the type of failure
         */
        void onFail(String url, String file, Constant.RequestTarget target, Constant.StatusCode code);
    }
}
