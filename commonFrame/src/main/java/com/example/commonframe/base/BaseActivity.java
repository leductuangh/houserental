package com.example.commonframe.base;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;

import com.example.commonframe.R;
import com.example.commonframe.connection.BackgroundServiceRequester;
import com.example.commonframe.connection.WebServiceRequester;
import com.example.commonframe.connection.WebServiceRequester.WebServiceResultHandler;
import com.example.commonframe.connection.request.BackgroundServiceRequest;
import com.example.commonframe.connection.request.WebServiceRequest;
import com.example.commonframe.dialog.AlertDialog;
import com.example.commonframe.dialog.AlertDialog.AlertDialogListener;
import com.example.commonframe.dialog.DecisionDialog;
import com.example.commonframe.dialog.DecisionDialog.DecisionDialogListener;
import com.example.commonframe.dialog.LoadingDialog;
import com.example.commonframe.dialog.Option;
import com.example.commonframe.dialog.OptionsDialog;
import com.example.commonframe.dialog.OptionsDialog.OptionsDialogListener;
import com.example.commonframe.exception.ActivityException;
import com.example.commonframe.gcm.GcmController;
import com.example.commonframe.model.CategoryParam;
import com.example.commonframe.model.CurrentParam;
import com.example.commonframe.model.DocumentParam;
import com.example.commonframe.model.FileParam;
import com.example.commonframe.model.ForgotParam;
import com.example.commonframe.model.LoginParam;
import com.example.commonframe.model.LogoutParam;
import com.example.commonframe.model.NotificationParam;
import com.example.commonframe.model.RegisterParam;
import com.example.commonframe.model.VersionParam;
import com.example.commonframe.model.base.BaseParam;
import com.example.commonframe.util.CentralApplication;
import com.example.commonframe.util.Constant;
import com.example.commonframe.util.Constant.BackgroundRequestTarget;
import com.example.commonframe.util.Constant.RequestMethod;
import com.example.commonframe.util.Constant.RequestTarget;
import com.example.commonframe.util.Constant.RequestType;
import com.example.commonframe.util.Constant.ReturnFormat;
import com.example.commonframe.util.DLog;
import com.example.commonframe.util.SingleClick;
import com.example.commonframe.util.SingleClick.SingleClickListener;
import com.example.commonframe.util.SingleTouch;
import com.example.commonframe.util.Utils;

/**
 * @author Tyrael
 * @since January 2014
 * @version 1.0 <br>
 * <br>
 *          <b>Class Overview</b> <br>
 * <br>
 *          Represents a class for essential activity to be a super class of
 *          activities in project. It includes supportive method of showing,
 *          closing dialogs, making and canceling request. Those methods can be
 *          used in any derived class. <br>
 *          The derived classes must implement <code>onCreateObject()</code>,
 *          <code>onBindView()</code>, <code>onResumeObject()</code>,
 *          <code>onFreeObject()</code> for the purpose of management.
 * 
 */

public abstract class BaseActivity extends Activity implements BaseInterface,
		SingleClickListener {

	// Test merge base trunk
	
	/**
	 * Tag of BaseActivity class for Log usage
	 */
	protected static String TAG = "BaseActivity";

	/**
	 * The flag indicating that the activity is finished and should free all of
	 * resources at <code>onStop()</code> method
	 */
	private boolean isFinished = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		TAG = getClass().getName();
		super.onCreate(savedInstanceState);
		onCreateObject();
		if (getIntent() != null) {
			if (getIntent().getData() != null
					&& !Utils.isEmpty(getIntent().getData().getHost())
					&& (getIntent().getData().getHost()
							.equals(getString(R.string.deep_linking_app_host)) || getIntent()
							.getData().getHost()
							.equals(getString(R.string.deep_linking_http_host)))) {
				onDeepLinking();
			} else if (getIntent().getExtras() != null
					&& getIntent().getBooleanExtra(
							Constant.NOTIFICATION_DEFINED, false)) {
				onNotification();
			}
		}

	}

	@Override
	protected void onResume() {
		TAG = getClass().getName();
		GcmController.getInstance(this).subscribeNotification();
		BaseProperties.wsRequester = WebServiceRequester
				.getInstance(CentralApplication.getContext());
		BaseProperties.bgRequester = BackgroundServiceRequester
				.getInstance(CentralApplication.getContext());
		CentralApplication.setActiveActivity(this);
		getSingleClick().setListener(this);
		// EventBus.getDefault().register(this);
		onResumeObject();
		super.onResume();
		Utils.logHeap(TAG);
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
			onFreeObject();
			Utils.nullViewDrawablesRecursive((ViewGroup) findViewById(
					android.R.id.content).getRootView());
			Utils.unbindDrawables((ViewGroup) findViewById(android.R.id.content)
					.getRootView());
		}
		super.onStop();
	}

	@Override
	public void finish() {
		isFinished = true;
		super.finish();
		if (BaseProperties.singleClicks != null)
			BaseProperties.singleClicks.remove(TAG);
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
		if (view != null && !(view instanceof AdapterView)
				&& !(view instanceof EditText)
				&& !(view instanceof SwipeRefreshLayout)) {
			view.setOnClickListener(getSingleClick());
			view.setOnTouchListener(getSingleTouch());
		}
		return view;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			Utils.closeSoftKeyboard(this, findViewById(android.R.id.content)
					.getRootView());
			return true;
		}
		return super.onTouchEvent(event);
	}

	public void makeBackgroundRequest(String tag,
			BackgroundRequestTarget target, BaseParam content) {
		if (!Utils.isInternetAvailable()) {
			return;
		}
		try {
			BackgroundServiceRequest request = null;
			if (BaseProperties.bgRequester == null)
				BaseProperties.bgRequester = BackgroundServiceRequester
						.getInstance(CentralApplication.getContext());
			switch (target) {
			case DOCUMENT:
				if (content instanceof DocumentParam)
					request = new BackgroundServiceRequest(tag,
							RequestType.HTTP, RequestMethod.GET,
							ReturnFormat.STRING, Constant.SERVER_URL,
							BackgroundRequestTarget.DOCUMENT, content,
							BaseProperties.bgRequester);
				else
					throw new ActivityException(
							"BaseActivity:makeBackgroundRequest: Background request target and param are not matching!");
				break;
			case NOTIFICATION:
				if (content instanceof NotificationParam)
					request = new BackgroundServiceRequest(tag,
							RequestType.HTTP, RequestMethod.GET,
							ReturnFormat.STRING, Constant.SERVER_URL,
							BackgroundRequestTarget.NOTIFICATION, content,
							BaseProperties.bgRequester);
				else
					throw new ActivityException(
							"BaseActivity:makeBackgroundRequest: Background request target and param are not matching!");
				break;
			case FILE:
				if (content instanceof FileParam)
					request = new BackgroundServiceRequest(tag,
							RequestType.HTTP, RequestMethod.GET,
							ReturnFormat.FILE, ((FileParam) content).getUrl(),
							BackgroundRequestTarget.FILE, content,
							BaseProperties.bgRequester);
				else
					throw new ActivityException(
							"BaseActivity:makeBackgroundRequest: Background request target and param are not matching!");
				break;
			default:
				break;
			}

			BaseProperties.bgRequester.startRequest(request);
		} catch (Exception ex) {
			ex.printStackTrace();
			DLog.d(TAG, "Background request canceled!");
		}
	}

	public void makeRequest(String tag, RequestTarget target,
			BaseParam content, WebServiceResultHandler handler) {
		if (!Utils.isInternetAvailable()) {
			closeLoadingDialog();
			showAlertDialog(
					this,
					-1,
					getResouceString(R.string.error_internet_unavailable_title),
					getResouceString(R.string.error_internet_unavailable_message),
					-1, null);
			return;
		}
		showLoadingDialog(this);
		try {
			WebServiceRequest request = null;
			if (BaseProperties.wsRequester == null)
				BaseProperties.wsRequester = WebServiceRequester
						.getInstance(CentralApplication.getContext());
			switch (target) {
			case LOGIN:
				if (content instanceof LoginParam)
					request = new WebServiceRequest(tag, RequestType.HTTP,
							RequestMethod.POST, ReturnFormat.XML,
							Constant.SERVER_URL, target, content,
							BaseProperties.wsRequester, handler);
				else
					throw new ActivityException(
							"BaseActivity:makeRequest: Request target and param are not matching!");
				break;
			case REGISTER:
				if (content instanceof RegisterParam)
					request = new WebServiceRequest(tag, RequestType.HTTPS,
							RequestMethod.POST, ReturnFormat.JSON,
							Constant.SERVER_URL, target, content,
							BaseProperties.wsRequester, handler);
				else
					throw new ActivityException(
							"BaseActivity:makeRequest: Request target and param are not matching!");
				break;
			case LOGOUT:
				if (content instanceof LogoutParam)
					request = new WebServiceRequest(tag, RequestType.HTTPS,
							RequestMethod.POST, ReturnFormat.XML,
							Constant.SERVER_URL, target, content,
							BaseProperties.wsRequester, handler);
				else
					throw new ActivityException(
							"BaseActivity:makeRequest: Request target and param are not matching!");
				break;
			case FORGOT:
				if (content instanceof ForgotParam)
					request = new WebServiceRequest(tag, RequestType.HTTPS,
							RequestMethod.POST, ReturnFormat.XML,
							Constant.SERVER_URL, target, content,
							BaseProperties.wsRequester, handler);
				else
					throw new ActivityException(
							"BaseActivity:makeRequest: Request target and param are not matching!");
				break;
			case CATEGORY:
				if (content instanceof CategoryParam)
					request = new WebServiceRequest(tag, RequestType.HTTP,
							RequestMethod.GET, ReturnFormat.JSON,
							Constant.SERVER_URL, target, content,
							BaseProperties.wsRequester, handler);
				else
					throw new ActivityException(
							"BaseActivity:makeRequest: Request target and param are not matching!");
				break;
			case CURRENT:
				if (content instanceof CurrentParam)
					request = new WebServiceRequest(tag, RequestType.HTTP,
							RequestMethod.GET, ReturnFormat.XML,
							Constant.SERVER_URL, target, content,
							BaseProperties.wsRequester, handler);
				else
					throw new ActivityException(
							"BaseActivity:makeRequest: Request target and param are not matching!");
				break;

			case VERSION:
				if (content instanceof VersionParam)
					request = new WebServiceRequest(tag, RequestType.HTTP,
							RequestMethod.GET, ReturnFormat.XML,
							Constant.SERVER_URL, target, content,
							BaseProperties.wsRequester, handler);
				else
					throw new ActivityException(
							"BaseActivity:makeRequest: Request target and param are not matching!");
				break;
			default:
				closeLoadingDialog();
				break;
			}

			BaseProperties.wsRequester.startRequest(request);
		} catch (Exception ex) {
			ex.printStackTrace();
			DLog.d(TAG, "Request canceled!");
		}

	}

	@Override
	public SingleClick getSingleClick() {
		if (BaseProperties.singleClicks == null) {
			BaseProperties.singleClicks = new HashMap<String, SingleClick>();
		}

		if (BaseProperties.singleClicks.get(TAG) == null) {
			BaseProperties.singleClicks.put(TAG, new SingleClick());
		}

		return BaseProperties.singleClicks.get(TAG);
	}

	@Override
	public SingleTouch getSingleTouch() {
		if (BaseProperties.singleTouch == null)
			BaseProperties.singleTouch = new SingleTouch();
		return BaseProperties.singleTouch;
	}

	@Override
	public void showDecisionDialog(Context context, int id, String title,
			String message, String yes, String no,
			DecisionDialogListener listener) {
		if (BaseProperties.questionDialog != null)
			BaseProperties.questionDialog.dismiss();
		BaseProperties.questionDialog = null;
		if (BaseProperties.questionDialog == null)
			BaseProperties.questionDialog = new DecisionDialog(context, id,
					title, message, yes, no, listener);

		if (BaseProperties.questionDialog != null)
			BaseProperties.questionDialog.show();
	}

	@Override
	public void showOptionsDialog(Context context, int id, String title,
			String message, int icon, Option[] options,
			OptionsDialogListener listener) {
		if (BaseProperties.optionsDialog != null)
			BaseProperties.optionsDialog.dismiss();
		BaseProperties.optionsDialog = null;

		if (BaseProperties.optionsDialog == null)
			BaseProperties.optionsDialog = new OptionsDialog(context, id,
					title, message, icon, options, listener);

		if (BaseProperties.optionsDialog != null)
			BaseProperties.optionsDialog.show();
	}

	@Override
	public void showAlertDialog(Context context, int id, String title,
			String message, int icon_id, AlertDialogListener listener) {
		if (BaseProperties.alertDialog != null)
			BaseProperties.alertDialog.dismiss();
		BaseProperties.alertDialog = null;
		if (BaseProperties.alertDialog == null)
			BaseProperties.alertDialog = new AlertDialog(context, id, title,
					message, icon_id, listener);

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
	public void closeLoadingDialog() {
		if (BaseProperties.loadingDialog != null)
			if (BaseProperties.loadingDialog.isShowing())
				BaseProperties.loadingDialog.dismiss();
	}

	@Override
	public String getResouceString(int id) {
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
