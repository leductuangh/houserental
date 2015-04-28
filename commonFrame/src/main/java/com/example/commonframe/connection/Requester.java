package com.example.commonframe.connection;

import com.example.commonframe.base.BaseProperties;
import com.example.commonframe.connection.WebServiceRequester.WebServiceResultHandler;
import com.example.commonframe.connection.request.BackgroundServiceRequest;
import com.example.commonframe.connection.request.WebServiceRequest;
import com.example.commonframe.exception.ActivityException;
import com.example.commonframe.model.base.BaseParam;
import com.example.commonframe.util.CentralApplication;
import com.example.commonframe.util.Constant;
import com.example.commonframe.util.Constant.RequestMethod;
import com.example.commonframe.util.Constant.RequestTarget;
import com.example.commonframe.util.Constant.RequestType;
import com.example.commonframe.util.DLog;

public class Requester {
	private static final String TAG = "Requester";

	public static boolean startWSRequest(String tag, RequestTarget target,
			String[] extras, BaseParam content, WebServiceResultHandler handler) {

		try {
			WebServiceRequest request = null;
			if (BaseProperties.wsRequester == null)
				BaseProperties.wsRequester = WebServiceRequester
						.getInstance(CentralApplication.getContext());
			switch (target) {
			case WEBSERVICE_REQUEST:
				request = new WebServiceRequest(tag, RequestType.HTTP,
						RequestMethod.POST, Constant.SERVER_URL, target,
						RequestTarget.build(target, extras), content,
						BaseProperties.wsRequester, handler);
				break;
			default:
				throw new ActivityException(
						"Requester: No request target found");
			}
			BaseProperties.wsRequester.startRequest(request);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			DLog.d(TAG, "Request canceled!");
			return false;
		}

	}

	public static boolean startBackgroundRequest(String tag,
			RequestTarget target, String[] extras, BaseParam content) {
		try {
			BackgroundServiceRequest request = null;
			if (BaseProperties.bgRequester == null)
				BaseProperties.bgRequester = BackgroundServiceRequester
						.getInstance(CentralApplication.getContext());
			switch (target) {
			case BACKGROUND_REQUEST:
				request = new BackgroundServiceRequest(tag, RequestType.HTTP,
						RequestMethod.GET, Constant.SERVER_URL, target,
						RequestTarget.build(target, extras), content,
						BaseProperties.bgRequester);
				break;
			default:
				throw new ActivityException(
						"Requester: No request target found");
			}

			BaseProperties.bgRequester.startRequest(request);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			DLog.d(TAG, "Background request canceled!");
			return false;
		}
	}
}
