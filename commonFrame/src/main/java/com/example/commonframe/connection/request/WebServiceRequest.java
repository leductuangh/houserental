package com.example.commonframe.connection.request;

import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.example.commonframe.connection.WebServiceRequester;
import com.example.commonframe.connection.WebServiceRequester.WebServiceResultHandler;
import com.example.commonframe.model.base.BaseParam;
import com.example.commonframe.model.volley.WebServiceError;
import com.example.commonframe.model.volley.WebServiceResponse;
import com.example.commonframe.util.Constant;
import com.example.commonframe.util.Constant.RequestMethod;
import com.example.commonframe.util.Constant.RequestTarget;
import com.example.commonframe.util.Constant.RequestType;
import com.example.commonframe.util.Constant.ReturnFormat;

/**
 * @author Tyrael
 * @since April 2014
 * @version 1.0 <br>
 * <br>
 *          <b>Class Overview</b> <br>
 * <br>
 *          - Represents a class for forming a webservice request extends from
 *          Request<?> of Volley framework. This class will handle the type,
 *          method, url, time out, retry, headers, parameters and the return
 *          format of each request <br>
 *          - Every request will be marked with a tag which can be canceled if
 *          it is no longer needed <br>
 *          - The content of the result will be delivered to the
 *          WebServiceHandler as a WebServiceResponse including the data and
 *          headers
 */
public class WebServiceRequest extends Request<WebServiceResponse> {
	/**
	 * The content paramters and headers for this request
	 */
	private BaseParam content;

	/**
	 * The target function of the service for this request, determined by
	 * Constant.RequestTarget enum
	 */
	private RequestTarget target;

	/**
	 * The expected return format from server, determined by
	 * Constant.ReturnFormat enum (XML, JSON and STRING)
	 */
	private ReturnFormat format;

	/**
	 * The request type for this request, either HTTP request or HTTPS request,
	 * determined by Constant.RequestType
	 */
	private RequestType type;

	/**
	 * The target for handling the result from webserivce, the actual end result
	 * will be delivered to this object (either success, unsuccess or failure)
	 */
	private WebServiceResultHandler handler;

	/**
	 * The success result handler to integrate with Volley framework
	 */
	private Listener<WebServiceResponse> success;

	public WebServiceRequest(String tag, RequestType type,
			RequestMethod method, ReturnFormat format, String address,
			RequestTarget target, BaseParam content,
			WebServiceRequester requester, WebServiceResultHandler handler) {
		super(method.getValue(), type.toString() + address + target.toString(),
				requester);
		this.success = (Listener<WebServiceResponse>) requester;
		this.handler = handler;
		this.target = target;
		this.format = format;
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
	public com.android.volley.Request.Priority getPriority() {
		return Priority.IMMEDIATE;
	}

	@Override
	public byte[] getBody() throws AuthFailureError {
		// Override this method for POST and PUT body in a specific format
		// (JSON/XML/STRING)
		return (content.makeRequestBody() == null) ? super.getBody() : content
				.makeRequestBody();
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return content.makeRequestParams();
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return content.makeRequestHeaders();
	}

	/**
	 * @return the target
	 */
	public RequestTarget getRequestTarget() {
		return target;
	}

	/**
	 * @return the format
	 */
	public ReturnFormat getReturnFormat() {
		return format;
	}

	/**
	 * @return the type
	 */
	public RequestType getRequesType() {
		return type;
	}

	/**
	 * @return the handler
	 */
	public WebServiceResultHandler getWebServiceResultHandler() {
		return handler;
	}

	@Override
	protected void deliverResponse(WebServiceResponse response) {
		success.onResponse(response);
	}
	
	@Override
	public void deliverError(VolleyError error) {
		super.deliverError(new WebServiceError(target, error));
	}

	@Override
	protected Response<WebServiceResponse> parseNetworkResponse(
			NetworkResponse response) {
		return Response.success(new WebServiceResponse(response.data,
				response.headers), getCacheEntry());
	}
}
