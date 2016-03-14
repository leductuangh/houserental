package com.core.core.connection.request;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.core.core.base.Param;
import com.core.core.connection.ParallelServiceRequester;
import com.core.core.connection.volley.ParallelError;
import com.core.core.connection.volley.ParallelResponse;
import com.core.util.Constant;
import com.core.util.Utils;

import java.util.HashMap;
import java.util.Map;

public class ParallelServiceRequest extends Request<ParallelResponse> {

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
    private Response.Listener<ParallelResponse> success;


    public ParallelServiceRequest(String tag, Constant.RequestType type,
                                  Constant.RequestMethod method, String address, Constant.RequestTarget target,
                                  String api, Param content, ParallelServiceRequester requester) {
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
    protected void deliverResponse(ParallelResponse response) {
        success.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError error) {
        return new ParallelError((String) getTag(), target, error);
    }

    @Override
    protected Response<ParallelResponse> parseNetworkResponse(
            NetworkResponse response) {
        ParallelResponse result = new ParallelResponse(response.data,
                response.headers, response.rawHeaders, target, (String) getTag());
        return Response.success(result, getCacheEntry());
    }
}
