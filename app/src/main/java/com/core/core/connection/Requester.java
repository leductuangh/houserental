package com.core.core.connection;

import android.annotation.SuppressLint;

import com.core.core.base.BaseApplication;
import com.core.core.base.BaseProperties;
import com.core.core.base.Param;
import com.core.core.connection.WebServiceRequester.WebServiceResultHandler;
import com.core.core.connection.queue.QueueElement;
import com.core.core.connection.queue.QueueElement.Type;
import com.core.core.connection.request.BackgroundServiceRequest;
import com.core.core.connection.request.ParallelServiceRequest;
import com.core.core.connection.request.QueueServiceRequest;
import com.core.core.connection.request.WebServiceRequest;
import com.core.util.Constant;
import com.core.util.Constant.RequestMethod;
import com.core.util.Constant.RequestTarget;
import com.core.util.Constant.RequestType;
import com.core.util.DLog;

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
                        .getInstance(BaseApplication.getContext());
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
                        .getInstance(BaseApplication.getContext());
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
                        .getInstance(BaseApplication.getContext());
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

    public static boolean startParallelRequest(String tag, RequestTarget target,
                                               String[] extras, Param content) {
        try {
            ParallelServiceRequest request;
            if (BaseProperties.parallelRequester == null)
                BaseProperties.parallelRequester = ParallelServiceRequester
                        .getInstance(BaseApplication.getContext());
            switch (target) {
                case WEBSERVICE_REQUEST:
                    request = new ParallelServiceRequest(tag, RequestType.HTTP,
                            RequestMethod.POST, Constant.SERVER_URL, target,
                            RequestTarget.build(target, extras), content,
                            BaseProperties.parallelRequester);
                    break;
                default:
                    throw new Exception(
                            "Requester: No request target found");
            }
            ParallelServiceRequester.addRequest(request);
            DLog.d(TAG, request.getRequestMethod().name().toUpperCase()
                    + " >> " + request.toString());
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            DLog.d(TAG, "Parallel request canceled!");
            return false;
        }
    }
}