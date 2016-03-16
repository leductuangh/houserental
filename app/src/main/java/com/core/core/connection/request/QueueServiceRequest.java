package com.core.core.connection.request;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.core.core.base.Param;
import com.core.core.connection.QueueServiceRequester;
import com.core.core.connection.volley.QueueError;
import com.core.core.connection.volley.QueueResponse;
import com.core.util.Constant;
import com.core.util.Utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tyrael
 * @version 1.0 <br>
 *          <br>
 *          <b>Class Overview</b> <br>
 *          <br>
 *          - Represents a class for forming a queue service request extends
 *          from Request<?> of Volley framework. This class will handle the
 *          type, method, url, time out, retry, headers, parameters and the
 *          return format of each request <br>
 *          - Every request will be marked with a tag which can be canceled if
 *          it is no longer needed <br>
 *          - The content of the result will be delivered to the
 *          QueueServiceRequester as a QueueResponse including the data, request
 *          target and headers
 * @since April 2014
 */
@SuppressWarnings("ALL")
public class QueueServiceRequest extends Request<QueueResponse> {

    /**
     * The content parameters and headers for this request
     */
    private Param content;

    /**
     * The target function of the service for this request, determined by
     * Constant.RequestTarget enum
     */
    private Constant.RequestTarget target;

    /**
     * The request type for this request, either HTTP request or HTTPS request,
     * determined by Constant.RequestType
     */
    private Constant.RequestType type;

    /**
     * The request method for this request, determined by Constant.RequestMethod
     */
    private Constant.RequestMethod method;

    /**
     * The request url for this request, built by request type, server url and
     * target
     */
    private String url;

    /**
     * The success result handler to integrate with Volley framework
     */
    private Listener<QueueResponse> success;

    public QueueServiceRequest(String tag, Constant.RequestType type,
                               Constant.RequestMethod method, String address, Constant.RequestTarget target,
                               String api, Param content, QueueServiceRequester requester) {
        super(method.getValue(), type.toString() + address + api, requester);
        this.method = method;
        this.url = type.toString() + address + api;
        this.success = requester;
        this.target = target;
        this.content = content;
        this.type = type;
        setTag(tag);
    }

    @Override
    public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {
        return super.setRetryPolicy(new DefaultRetryPolicy(
                Constant.TIMEOUT_QUEUE_CONNECT, Constant.RETRY_QUEUE_CONNECT,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public com.android.volley.Request.Priority getPriority() {
        return Priority.LOW;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        byte[] body = content.makeRequestBody();
        return (body == null || body.length == 0) ? super.getBody() : body;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return content.makeRequestHeaders();
    }

    /**
     * @return the type
     */
    public Constant.RequestType getRequestType() {
        return type;
    }

    /**
     * @return the request method
     */
    public Constant.RequestMethod getRequestMethod() {
        return method;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    @Override
    public String getBodyContentType() {
        if (content != null) {
            HashMap<String, String> headers = content.makeRequestHeaders();
            if (headers != null) {
                String contentType = headers.get(Constant.Header.CONTENT_TYPE.toString());
                if (!Utils.isEmpty(contentType)) {
                    headers.remove(Constant.Header.CONTENT_TYPE.toString());
                    return contentType;
                }
            }
        }
        return super.getBodyContentType();
    }

    @Override
    protected void deliverResponse(QueueResponse response) {
        success.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError error) {
        return new QueueError(target, error);
    }

    @Override
    protected Response<QueueResponse> parseNetworkResponse(
            NetworkResponse response) {
        QueueResponse result = new QueueResponse(response.data,
                response.headers, response.rawHeaders, target);
        return Response.success(result, getCacheEntry());
    }

    @Override
    public boolean equals(Object object) {
        if (object != null && object instanceof QueueServiceRequest) {
            QueueServiceRequest request = (QueueServiceRequest) object;
            return request.target == target
                    && request.method == method
                    && request.type == type
                    && request.url.equals(url)
                    && request.content.makeRequestHeaders().equals(
                    content.makeRequestHeaders())
                    && Arrays.equals(request.content.makeRequestBody(),
                    content.makeRequestBody());
        }
        return false;
    }
}