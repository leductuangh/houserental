package com.example.commonframe.core.base;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

import com.example.commonframe.R;
import com.example.commonframe.core.connection.BackgroundServiceRequester;
import com.example.commonframe.core.connection.Requester;
import com.example.commonframe.core.connection.WebServiceRequester;
import com.example.commonframe.core.connection.WebServiceRequester.WebServiceResultHandler;
import com.example.commonframe.core.connection.queue.QueueElement.Type;
import com.example.commonframe.dialog.GeneralDialog;
import com.example.commonframe.dialog.GeneralDialog.ConfirmListener;
import com.example.commonframe.dialog.GeneralDialog.DecisionListener;
import com.example.commonframe.dialog.LoadingDialog;
import com.example.commonframe.util.ActionTracker;
import com.example.commonframe.util.CentralApplication;
import com.example.commonframe.util.Constant;
import com.example.commonframe.util.Constant.RequestTarget;
import com.example.commonframe.util.DLog;
import com.example.commonframe.util.SingleClick;
import com.example.commonframe.util.SingleClick.SingleClickListener;
import com.example.commonframe.util.SingleTouch;
import com.example.commonframe.util.Utils;

import icepick.Icepick;

/**
 * @author Tyrael
 * @version 1.0 <br>
 *          <br>
 *          <b>Class Overview</b> <br>
 *          <br>
 *          Represents a class for essential activity to be a super class of
 *          activities in project. It includes supportive method of showing,
 *          closing dialogs, making and canceling request. Those methods can be
 *          used in any derived class. <br>
 *          The derived classes must implement <code>onBaseCreate()</code>,
 *          <code>onBindView()</code>, <code>onResumeObject()</code>,
 *          <code>onFreeObject()</code> for the purpose of management.
 * @since January 2014
 */

@SuppressWarnings("ALL")
public abstract class BaseActivity extends Activity implements BaseInterface,
        SingleClickListener {

    /**
     * Tag of BaseActivity class for Log usage
     */
    protected static String TAG = "BaseActivity";

    /**
     * The single click to handle click action for this screen
     */
    private SingleClick singleClick = null;

    /**
     * The flag indicating that the activity is finished and should free all of
     * resources at <code>onStop()</code> method
     */
    private boolean isFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // This is to prevent multiple instances on release build (bug from
        // Android)
        if (!isTaskRoot()) {
            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action != null
                    && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        } else
            ActionTracker.openActionLog();

        TAG = getClass().getName();
        overridePendingTransition(Constant.DEFAULT_ADD_ANIMATION[0],
                Constant.DEFAULT_ADD_ANIMATION[1]);
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        // ConnectivityReceiver.registerListener(this);
        onBaseCreate();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    protected void onResume() {
        TAG = getClass().getName();
        BaseProperties.wsRequester = WebServiceRequester
                .getInstance(CentralApplication.getContext());
        BaseProperties.bgRequester = BackgroundServiceRequester
                .getInstance(CentralApplication.getContext());
        CentralApplication.setActiveActivity(this);
        // EventBus.getDefault().register(this);
        ActionTracker.enterScreen(getClass().getSimpleName(), ActionTracker.Screen.ACTIVITY);
        onBaseResume();
        super.onResume();
        onOutsideActionReceived();
        Utils.logHeap(TAG);
    }

    private void onOutsideActionReceived() {
        if (getIntent() != null) {
            if (getIntent().getData() != null
                    && !Utils.isEmpty(getIntent().getData().getHost())
                    && (getIntent().getData().getHost()
                    .equals(getString(R.string.deep_linking_app_host)) || getIntent()
                    .getData().getHost()
                    .equals(getString(R.string.deep_linking_http_host)))) {
                onDeepLinking(new Intent(getIntent()));
                Intent resetDeepLinkIntent = new Intent(getIntent());
                resetDeepLinkIntent.setData(Uri.EMPTY);
                setIntent(resetDeepLinkIntent);
            } else if (getIntent().getExtras() != null
                    && getIntent().getBooleanExtra(
                    Constant.NOTIFICATION_DEFINED, false)) {
                int id = getIntent().getIntExtra(Constant.NOTIFICATION_ID, -1);
                if (id != -1) {
                    NotificationManager manager = (NotificationManager) getCentralContext()
                            .getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.cancel(id);
                    onNotification(new Intent(getIntent()));
                    Intent resetNotificationIntent = new Intent(getIntent());
                    resetNotificationIntent.putExtra(
                            Constant.NOTIFICATION_DEFINED, false);
                    setIntent(resetNotificationIntent);
                }
            }
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        onBindView();
        onInitializeViewData();
    }

    @Override
    protected void onStop() {
        if (isFinished) {
            // ConnectivityReceiver.removeListener(this);
            ActionTracker.exitScreen(getClass().getSimpleName());
            if (isTaskRoot())
                ActionTracker.closeActionLog();
            onBaseFree();
            Utils.nullViewDrawablesRecursive(findViewById(android.R.id.content)
                    .getRootView());
            Utils.unbindDrawables(findViewById(android.R.id.content)
                    .getRootView());
        }
        super.onStop();
    }

    @Override
    public void finish() {
        isFinished = true;
        super.finish();
        overridePendingTransition(Constant.DEFAULT_BACK_ANIMATION[0],
                Constant.DEFAULT_BACK_ANIMATION[1]);
    }

    @Override
    protected void onPause() {
        // EventBus.getDefault().unregister(this);
        cancelRequest();
        closeLoadingDialog();
        super.onPause();
    }

    @Override
    public View findViewById(int id) {
        View view = super.findViewById(id);
        if (view != null && !BaseProperties.isExceptionalView(view)) {
            view.setOnClickListener(getSingleClick());
            view.setOnTouchListener(getSingleTouch());
        }
        return view;
    }

    @Override
    public void onBackPressed() {
        if (BaseProperties.getSingleBackPress().onBackPressAllowed()) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Utils.closeSoftKeyboard(this, findViewById(android.R.id.content)
                    .getRootView());
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Utils.closeSoftKeyboard(this, findViewById(android.R.id.content)
                    .getRootView());
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void makeBackgroundRequest(String tag, RequestTarget target,
                                      String[] extras, Param content) {
        if (!Utils.isNetworkConnectionAvailable()) {
            return;
        }
        if (!Requester.startBackgroundRequest(tag, target, extras, content))
            DLog.d(TAG, "makeBackgroundRequest failed with " + tag);
    }

    public void makeRequest(String tag, boolean loading, Param content,
                            WebServiceResultHandler handler, RequestTarget target,
                            String... extras) {
        if (!Utils.isNetworkConnectionAvailable()) {
            closeLoadingDialog();
            showAlertDialog(
                    this,
                    -1,
                    -1,
                    getResourceString(R.string.error_internet_unavailable_title),
                    getResourceString(R.string.error_internet_unavailable_message),
                    getResourceString(android.R.string.ok), null);
            return;
        }
        if (loading)
            showLoadingDialog(this);
        if (!Requester.startWSRequest(tag, target, extras, content, handler)) {
            DLog.d(TAG, "makeRequest failed with " + tag);
            closeLoadingDialog();
        }
    }

    @Override
    public void makeQueueRequest(String tag, Type type, Param content,
                                 RequestTarget target, String... extras) {
        if (!Requester.startQueueRequest(tag, target, extras, type, content))
            DLog.d(TAG, "makeQueueRequest failed with " + tag);
    }

    @Override
    public SingleClick getSingleClick() {
        if (singleClick == null) {
            singleClick = new SingleClick();
            singleClick.setListener(this);
        }
        return singleClick;
    }

    @Override
    public SingleTouch getSingleTouch() {
        return BaseProperties.getSingleTouch();
    }

    @Override
    public void showDecisionDialog(Context context, int id, int icon,
                                   String title, String message, String yes, String no, String cancel,
                                   DecisionListener listener) {
        if (BaseProperties.decisionDialog != null)
            BaseProperties.decisionDialog.dismiss();
        BaseProperties.decisionDialog = null;
        if (BaseProperties.decisionDialog == null)
            BaseProperties.decisionDialog = new GeneralDialog(context, id,
                    icon, title, message, yes, no, cancel, listener);

        if (BaseProperties.decisionDialog != null)
            BaseProperties.decisionDialog.show();
    }

    @Override
    public void showAlertDialog(Context context, int id, int icon,
                                String title, String message, String confirm,
                                ConfirmListener listener) {
        if (BaseProperties.alertDialog != null)
            BaseProperties.alertDialog.dismiss();
        BaseProperties.alertDialog = null;
        if (BaseProperties.alertDialog == null)
            BaseProperties.alertDialog = new GeneralDialog(context, id, icon,
                    title, message, confirm, listener);

        if (BaseProperties.alertDialog != null)
            BaseProperties.alertDialog.show();
    }

    @Override
    public void showLoadingDialog(Context context) {
        if (BaseProperties.loadingDialog != null)
            BaseProperties.loadingDialog.dismiss();
        BaseProperties.loadingDialog = null;
        if (BaseProperties.loadingDialog == null)
            BaseProperties.loadingDialog = new LoadingDialog(context);

        if (BaseProperties.loadingDialog != null)
            BaseProperties.loadingDialog.show();
    }

    @Override
    public void showLoadingDialog(Context context, String loading) {
        if (BaseProperties.loadingDialog != null)
            BaseProperties.loadingDialog.dismiss();
        BaseProperties.loadingDialog = null;
        if (BaseProperties.loadingDialog == null)
            BaseProperties.loadingDialog = new LoadingDialog(context, loading);

        if (BaseProperties.loadingDialog != null)
            BaseProperties.loadingDialog.show();
    }

    @Override
    public void closeLoadingDialog() {
        if (BaseProperties.loadingDialog != null)
            if (BaseProperties.loadingDialog.isShowing())
                BaseProperties.loadingDialog.dismiss();
    }

    @Override
    public String getResourceString(int id) {
        try {
            return getResources().getString(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Activity getActiveActivity() {
        return CentralApplication.getActiveActivity();
    }

    @Override
    public Context getCentralContext() {
        return CentralApplication.getContext();
    }

    private void cancelRequest() {
        if (BaseProperties.wsRequester != null)
            BaseProperties.wsRequester.cancelAll(null);
        BaseProperties.wsRequester = null;
    }

    @Override
    public void cancelWebServiceRequest(String tag) {
        if (BaseProperties.wsRequester != null)
            BaseProperties.wsRequester.cancelAll(tag);
    }

    @Override
    public void cancelBackgroundRequest(String tag) {
        if (BaseProperties.bgRequester != null)
            BaseProperties.bgRequester.cancelAll(tag);
    }
}
