package core.connection.request;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import core.base.Param;
import core.connection.WebServiceRequester;
import core.connection.volley.WebServiceError;
import core.connection.volley.WebServiceResponse;
import core.util.Constant;
import core.util.Utils;

/**
 * @author Tyrael
 * @version 1.0 <br>
 *          <br>
 *          <b>Class Overview</b> <br>
 *          <br>
 *          - Represents a class for forming a webservice request extends from
 *          Request<?> of Volley framework. This class will handle the type,
 *          method, url, time out, retry, headers, parameters and the return
 *          format of each request <br>
 *          - Every request will be marked with a tag which can be canceled if
 *          it is no longer needed <br>
 *          - The content of the result will be delivered to the
 *          WebServiceHandler as a WebServiceResponse including the data and
 *          headers
 * @since April 2014
 */
@SuppressWarnings("ALL")
public class WebServiceRequest extends Request<WebServiceResponse> {
    /**
     * The content parameters and headers for this request
     */
    private final Param content;

    /**
     * The target function of the service for this request, determined by
     * Constant.RequestTarget enum
     */
    private final Constant.RequestTarget target;

    /**
     * The request type for this request, either HTTP request or HTTPS request,
     * determined by Constant.RequestType
     */
    private final Constant.RequestType type;

    /**
     * The request method for this request, determined by Constant.RequestMethod
     */
    private final Constant.RequestMethod method;

    /**
     * The request url for this request, built by request type, server url and
     * target
     */
    private final String url;

    /**
     * The target for handling the result from webserivce, the actual end result
     * will be delivered to this object (either success, unsuccessful or
     * failure)
     */
    private final WebServiceRequester.WebServiceResultHandler handler;

    /**
     * The success result handler to integrate with Volley framework
     */
    private Listener<WebServiceResponse> success;

    public WebServiceRequest(String tag, Constant.RequestType type,
                             Constant.RequestMethod method, String address, Constant.RequestTarget target,
                             String api, Param content, WebServiceRequester requester,
                             WebServiceRequester.WebServiceResultHandler handler) {
        super(method.getValue(), type.toString() + address + api, requester);
        this.url = type.toString() + address + api;
        this.success = requester;
        this.method = method;
        this.handler = handler;
        this.target = target;
        this.content = content;
        this.type = type;
        setTag(tag);
    }

    @Override
    public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {
        return super.setRetryPolicy(new DefaultRetryPolicy(
                Constant.TIMEOUT_CONNECT, Constant.RETRY_CONNECT,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public Request.Priority getPriority() {
        return Priority.IMMEDIATE;
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
     * @return the target
     */
    public Constant.RequestTarget getRequestTarget() {
        return target;
    }

    /**
     * @return the type
     */
    public Constant.RequestType getRequestType() {
        return type;
    }

    /**
     * @return the handler
     */
    public WebServiceRequester.WebServiceResultHandler getWebServiceResultHandler() {
        return handler;
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
    protected void deliverResponse(WebServiceResponse response) {
        success.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError error) {
        return new WebServiceError(target, error);
    }

    @Override
    protected Response<WebServiceResponse> parseNetworkResponse(
            NetworkResponse response) {
        return Response.success(new WebServiceResponse(response.data,
                response.headers, response.rawHeaders), getCacheEntry());
    }
}
