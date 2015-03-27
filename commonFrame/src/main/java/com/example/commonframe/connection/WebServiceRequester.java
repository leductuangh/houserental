package com.example.commonframe.connection;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
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
import com.example.commonframe.base.BaseInterface;
import com.example.commonframe.connection.request.WebServiceRequest;
import com.example.commonframe.connection.ssl.ExtHttpClientStack;
import com.example.commonframe.connection.ssl.SslHttpClient;
import com.example.commonframe.model.base.BaseResult;
import com.example.commonframe.model.volley.WebServiceResponse;
import com.example.commonframe.parser.BaseParser;
import com.example.commonframe.util.CentralApplication;
import com.example.commonframe.util.Constant;
import com.example.commonframe.util.Constant.RequestTarget;
import com.example.commonframe.util.Constant.RequestType;
import com.example.commonframe.util.Constant.StatusCode;
import com.example.commonframe.util.DLog;

/**
 * @author Tyrael
 * @since January 2014
 * @version 1.0 <br>
 */
public class WebServiceRequester implements Listener<WebServiceResponse>,
		ErrorListener {
	public interface WebServiceResultHandler {
		/**
		 * <b>Specified by:</b> onResultSuccess(...) in WebServiceResultHandler <br>
		 * <br>
		 * This is called immediately after the data is being successfully
		 * retrieved. After returning from this call, you can use data value. If
		 * you want to update the UI, please use <code>Handler</code> or
		 * <code>runOnUI</code> method, otherwise it will throw
		 * <code>CalledFromWrongThreadException</code> exception because this
		 * method is called from a separated thread.
		 * 
		 * @param result
		 *            The BaseResult or derived class instance return.
		 */
		public void onResultSuccess(BaseResult result);

		/**
		 * <b>Specified by:</b> onResultFail(...) in WebServiceResultHandler <br>
		 * <br>
		 * This is called immediately after the data is being successfully
		 * retrieved as a failure on the server. After returning from this call,
		 * you can use data value. If you want to update the UI, please use
		 * <code>Handler</code> or <code>runOnUI</code> method, otherwise it
		 * will throw <code>CalledFromWrongThreadException</code> exception
		 * because this method is called from a separated thread.
		 * 
		 * @param result
		 *            The BaseResult or derived class instance return.
		 */
		public void onResultFail(BaseResult result);

		/**
		 * <b>Specified by:</b> onFail(...) in WebServiceResultHandler <br>
		 * <br>
		 * This is called immediately after request is being fail to process.
		 * After returning from this call, the request will stop. If you want to
		 * update the UI, please use <code>Handler</code> or
		 * <code>runOnUI</code> method, otherwise it will throw
		 * <code>CalledFromWrongThreadException</code> exception because this
		 * method is called from a separated thread.
		 * 
		 * @param target
		 *            The target request had been called
		 * @param error
		 *            The string explaining the failure of the request
		 * @param code
		 *            The code indicating the type of failure
		 */
		public void onFail(RequestTarget target, String error, StatusCode code);
	}

	private static WebServiceRequester instance;
	private WebServiceResultHandler handler;
	private WebServiceRequest request;
	private static RequestQueue httpQueue;
	private static RequestQueue sslQueue;
	private final static String TAG = "WebServiceRequester";

	public static WebServiceRequester getInstance(Context context) {
		if (instance == null)
			instance = new WebServiceRequester(context);
		return instance;
	}

	private WebServiceRequester(Context context) {
		httpQueue = Volley.newRequestQueue(context);
		sslQueue = Volley.newRequestQueue(context, new ExtHttpClientStack(
				new SslHttpClient(Constant.KEY_STORE_ID,
						Constant.KEY_STORE_PASSWORD)));
	}

	public void startRequest(WebServiceRequest request) {
		if (request != null) {
			this.request = request;
			this.handler = request.getWebServiceResultHandler();
			cancelAll(request.getTag());
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
		DLog.d(TAG, "onErrorResponse >> " + error.getMessage());
		if (handler != null) {
			if (handler instanceof BaseInterface)
				((BaseInterface) handler).closeLoadingDialog();

			if (error instanceof NetworkError) {
				handler.onFail(
						request.getRequestTarget(),
						CentralApplication.getContext().getString(
								R.string.error_connection_fail),
						StatusCode.ERR_NO_CONNECTION);
			} else if (error instanceof ServerError) {
				handler.onFail(request.getRequestTarget(), CentralApplication
						.getContext().getString(R.string.error_server_fail),
						StatusCode.ERR_SERVER_FAIL);
			} else if (error instanceof AuthFailureError) {
				handler.onFail(request.getRequestTarget(), CentralApplication
						.getContext().getString(R.string.error_auth_fail),
						StatusCode.ERR_AUTH_FAIL);
			} else if (error instanceof ParseError) {
				handler.onFail(request.getRequestTarget(), CentralApplication
						.getContext().getString(R.string.error_parsing_fail),
						StatusCode.ERR_PARSING);
			} else if (error instanceof NoConnectionError) {
				handler.onFail(
						request.getRequestTarget(),
						CentralApplication.getContext().getString(
								R.string.error_connection_fail),
						StatusCode.ERR_NO_CONNECTION);
			} else if (error instanceof TimeoutError) {
				handler.onFail(
						request.getRequestTarget(),
						CentralApplication.getContext().getString(
								R.string.error_conneciton_time_out),
						StatusCode.ERR_TIME_OUT);
			} else {
				handler.onFail(request.getRequestTarget(), CentralApplication
						.getContext().getString(R.string.error_unknown),
						StatusCode.ERR_UNKNOWN);
			}

		}

	}

	@Override
	public void onResponse(WebServiceResponse response) {
		DLog.d(TAG, "onResponse >> " + new String(response.getContent()));
		BaseResult result = BaseParser.parse(new String(response.getContent()),
				request.getReturnFormat(), request.getRequestTarget());
		if (handler != null) {
			if (handler instanceof BaseInterface)
				((BaseInterface) handler).closeLoadingDialog();

			if (result != null) {
				result.setHeaders(response.getHeaders());
				if (result.getStatus().equals(StatusCode.OK.toString()))
					handler.onResultSuccess(result);
				else
					handler.onResultFail(result);
			} else {
				handler.onFail(request.getRequestTarget(), CentralApplication
						.getContext().getString(R.string.error_parsing_fail),
						StatusCode.ERR_PARSING);
			}
		}
	}
}
