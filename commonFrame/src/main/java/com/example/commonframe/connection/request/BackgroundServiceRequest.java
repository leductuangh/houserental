package com.example.commonframe.connection.request;

import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.example.commonframe.connection.BackgroundServiceRequester;
import com.example.commonframe.model.FileParam;
import com.example.commonframe.model.base.BaseParam;
import com.example.commonframe.model.volley.BackgroundError;
import com.example.commonframe.model.volley.BackgroundResponse;
import com.example.commonframe.model.volley.FileResponse;
import com.example.commonframe.util.Constant;
import com.example.commonframe.util.Constant.BackgroundRequestTarget;
import com.example.commonframe.util.Constant.RequestMethod;
import com.example.commonframe.util.Constant.RequestType;
import com.example.commonframe.util.Constant.ReturnFormat;

/**
 * @author Tyrael
 * @since April 2014
 * @version 1.0 <br>
 * <br>
 *          <b>Class Overview</b> <br>
 * <br>
 *          - Represents a class for forming a background service request
 *          extends from Request<?> of Volley framework. This class will handle
 *          the type, method, url, time out, retry, headers, parameters and the
 *          return format of each request <br>
 *          - Every request will be marked with a tag which can be canceled if
 *          it is no longer needed <br>
 *          - The content of the result will be delivered to the
 *          BackgroundServiceRequester as a BackgroundResponse including the
 *          data, request target and headers
 */
public class BackgroundServiceRequest extends Request<BackgroundResponse> {

	/**
	 * The content paramters and headers for this request
	 */
	private BaseParam content;

	/**
	 * The target function of the service for this request, determined by
	 * Constant.RequestTarget enum
	 */
	private BackgroundRequestTarget target;

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
	 * The success result handler to integrate with Volley framework
	 */
	private Listener<BackgroundResponse> success;

	public BackgroundServiceRequest(String tag, RequestType type,
			RequestMethod method, ReturnFormat format, String address,
			BackgroundRequestTarget target, BaseParam content,
			BackgroundServiceRequester requester) {
		super(method.getValue(), type.toString() + address + target.toString(),
				requester);
		this.success = (Listener<BackgroundResponse>) requester;
		this.target = target;
		this.content = content;
		this.type = type;
		this.format = format;
		setTag(tag);
	}

	@Override
	public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {
		return super.setRetryPolicy(new DefaultRetryPolicy(
				Constant.TIMEOUT_BACKGROUND_CONNECT,
				Constant.RETRY_BACKGROUND_CONNECT,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
	}

	@Override
	public com.android.volley.Request.Priority getPriority() {
		return Priority.LOW;
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
	 * @return the type
	 */
	public RequestType getRequesType() {
		return type;
	}

	@Override
	protected void deliverResponse(BackgroundResponse response) {
		success.onResponse(response);
	}

	@Override
	public void deliverError(VolleyError error) {
		super.deliverError(new BackgroundError(target, error));
	}

	@Override
	protected Response<BackgroundResponse> parseNetworkResponse(
			NetworkResponse response) {
		BackgroundResponse result = null;
		if (format == ReturnFormat.FILE
				&& target == BackgroundRequestTarget.FILE) {
			result = new FileResponse(response.data, response.headers, target,
					format, ((FileParam) content).getName(),
					((FileParam) content).getExtension());
		} else {
			result = new BackgroundResponse(response.data, response.headers,
					target, format);
		}

		return Response.success(result, getCacheEntry());
	}
}
