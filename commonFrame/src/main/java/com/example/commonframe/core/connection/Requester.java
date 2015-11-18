package com.example.commonframe.core.connection;

import android.annotation.SuppressLint;

import com.example.commonframe.core.base.BaseProperties;
import com.example.commonframe.core.base.Param;
import com.example.commonframe.core.connection.WebServiceRequester.WebServiceResultHandler;
import com.example.commonframe.core.connection.queue.QueueElement;
import com.example.commonframe.core.connection.queue.QueueElement.Type;
import com.example.commonframe.core.connection.request.BackgroundServiceRequest;
import com.example.commonframe.core.connection.request.QueueServiceRequest;
import com.example.commonframe.core.connection.request.WebServiceRequest;
import com.example.commonframe.util.CentralApplication;
import com.example.commonframe.util.Constant;
import com.example.commonframe.util.Constant.RequestMethod;
import com.example.commonframe.util.Constant.RequestTarget;
import com.example.commonframe.util.Constant.RequestType;
import com.example.commonframe.util.DLog;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
@SuppressLint("DefaultLocale")
public class Requester {
    private static final String TAG = "Requester";

    public static boolean startWSRequest(String tag, RequestTarget target,
                                         String[] extras, Param content, WebServiceResultHandler handler) {

        try {
            WebServiceRequest request;
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
                    throw new Exception(
                            "Requester: No request target found");
            }
            BaseProperties.wsRequester.startRequest(request);
            DLog.d(TAG, request.getRequestMethod().name().toUpperCase()
                    + " >> " + request.getUrl());
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            DLog.d(TAG, "Request canceled!");
            return false;
        }
    }

    public static boolean startBackgroundRequest(String tag,
                                                 RequestTarget target, String[] extras, Param content) {
        try {
            BackgroundServiceRequest request;
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
                    throw new Exception(
                            "Requester: No request target found");
            }

            BaseProperties.bgRequester.startRequest(request);
            DLog.d(TAG, request.getRequestMethod().name().toUpperCase()
                    + " >> " + request.getUrl());
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            DLog.d(TAG, "Background request canceled!");
            return false;
        }
    }

    public static boolean startQueueRequest(String tag, RequestTarget target,
                                            String[] extras, Type type, Param content) {
        try {
            QueueServiceRequest request;
            if (BaseProperties.queueRequester == null)
                BaseProperties.queueRequester = QueueServiceRequester
                        .getInstance(CentralApplication.getContext());
            switch (target) {
                case WEBSERVICE_REQUEST:
                    request = new QueueServiceRequest(tag, RequestType.HTTP,
                            RequestMethod.POST, Constant.SERVER_URL, target,
                            RequestTarget.build(target, extras), content,
                            BaseProperties.queueRequester);
                    break;
                default:
                    throw new Exception(
                            "Requester: No request target found");
            }
            BaseProperties.queueRequester.addQueueRequest(new QueueElement(
                    request, type));
            DLog.d(TAG, request.getRequestMethod().name().toUpperCase()
                    + " >> " + request.toString());
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            DLog.d(TAG, "Queue request canceled!");
            return false;
        }
    }
}
