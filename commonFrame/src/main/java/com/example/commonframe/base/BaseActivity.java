package com.example.commonframe.base;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.example.commonframe.R;
import com.example.commonframe.connection.BackgroundServiceRequester;
import com.example.commonframe.connection.Requester;
import com.example.commonframe.connection.WebServiceRequester;
import com.example.commonframe.connection.WebServiceRequester.WebServiceResultHandler;
import com.example.commonframe.dialog.AlertDialog;
import com.example.commonframe.dialog.AlertDialog.AlertDialogListener;
import com.example.commonframe.dialog.DecisionDialog;
import com.example.commonframe.dialog.DecisionDialog.DecisionDialogListener;
import com.example.commonframe.dialog.LoadingDialog;
import com.example.commonframe.dialog.Option;
import com.example.commonframe.dialog.OptionsDialog;
import com.example.commonframe.dialog.OptionsDialog.OptionsDialogListener;
import com.example.commonframe.model.base.Param;
import com.example.commonframe.util.CentralApplication;
import com.example.commonframe.util.Constant;
import com.example.commonframe.util.Constant.RequestTarget;
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
		TAG = getClass().getName();
		overridePendingTransition(Constant.DEFAULT_ENTER_ANIMATION[0],
				Constant.DEFAULT_ENTER_ANIMATION[1]);
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
				int id = getIntent().getIntExtra(Constant.NOTIFICATION_ID, -1);
				if (id != -1) {
					NotificationManager manager = (NotificationManager) getCentralContext()
							.getSystemService(Context.NOTIFICATION_SERVICE);
					manager.cancel(id);
					onNotification();
				}
			}
		}

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
			Utils.nullViewDrawablesRecursive(findViewById(
					android.R.id.content).getRootView());
			Utils.unbindDrawables(findViewById(android.R.id.content)
					.getRootView());
		}
		super.onStop();
	}

	@Override
	public void finish() {
		isFinished = true;
		super.finish();
		overridePendingTransition(Constant.DEFAULT_EXIT_ANIMATION[0],
				Constant.DEFAULT_EXIT_ANIMATION[1]);
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
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			Utils.closeSoftKeyboard(this, findViewById(android.R.id.content)
					.getRootView());
			return true;
		}
		return super.onTouchEvent(event);
	}

	public void makeBackgroundRequest(String tag, RequestTarget target,
			String[] extras, Param content) {
		if (!Utils.isInternetAvailable()) {
			return;
		}
		if (!Requester.startBackgroundRequest(tag, target, extras, content))
			;
	}

	public void makeRequest(String tag, RequestTarget target, String[] extras,
			Param content, WebServiceResultHandler handler) {
		if (!Utils.isInternetAvailable()) {
			closeLoadingDialog();
			showAlertDialog(
					this,
					-1,
					getResourceString(R.string.error_internet_unavailable_title),
					getResourceString(R.string.error_internet_unavailable_message),
					-1, null);
			return;
		}
		showLoadingDialog(this);
		if (!Requester.startWSRequest(tag, target, extras, content, handler))
			closeLoadingDialog();
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
