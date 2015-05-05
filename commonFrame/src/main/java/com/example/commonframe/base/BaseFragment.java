package com.example.commonframe.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.example.commonframe.R;
import com.example.commonframe.connection.WebServiceRequester.WebServiceResultHandler;
import com.example.commonframe.dialog.AlertDialog.AlertDialogListener;
import com.example.commonframe.dialog.DecisionDialog.DecisionDialogListener;
import com.example.commonframe.dialog.Option;
import com.example.commonframe.dialog.OptionsDialog.OptionsDialogListener;
import com.example.commonframe.model.base.Param;
import com.example.commonframe.util.CentralApplication;
import com.example.commonframe.util.Constant;
import com.example.commonframe.util.Constant.RequestTarget;
import com.example.commonframe.util.SingleClick;
import com.example.commonframe.util.SingleClick.SingleClickListener;
import com.example.commonframe.util.SingleTouch;
import com.example.commonframe.util.Utils;

public abstract class BaseFragment extends Fragment implements BaseInterface,
		SingleClickListener {

	/**
	 * The single click to handle click action for this screen
	 */
	private SingleClick singleClick = null;

	/**
	 * Local active activity, in case the getActivity return null;
	 */
	private BaseFragmentActivity activeActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		onCreateObject();
		if (getActivity().getIntent() != null) {
			if (getActivity().getIntent().getData() != null
					&& !Utils.isEmpty(getActivity().getIntent().getData()
							.getHost())
					&& (getActivity().getIntent().getData().getHost()
							.equals(getString(R.string.deep_linking_app_host)) || getActivity()
							.getIntent().getData().getHost()
							.equals(getString(R.string.deep_linking_http_host)))) {
				onDeepLinking();
			} else if (getActivity().getIntent().getExtras() != null
					&& getActivity().getIntent().getBooleanExtra(
							Constant.NOTIFICATION_DEFINED, false)) {
				onNotification();
			}
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		activeActivity = (BaseFragmentActivity) activity;
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		if (getActivity() != null)
			getActivity().startActivityForResult(intent, requestCode);
		else if (getActiveActivity() != null)
			getActiveActivity().startActivityForResult(intent, requestCode);
		else
			activeActivity.startActivityForResult(intent, requestCode);
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
		if (getActivity() != null
				&& getActivity() instanceof BaseFragmentActivity)
			return ((BaseFragmentActivity) getActivity()).getSingleTouch();
		else if (getActiveActivity() != null
				&& getActiveActivity() instanceof BaseFragmentActivity)
			return ((BaseFragmentActivity) getActiveActivity())
					.getSingleTouch();
		else
			return activeActivity.getSingleTouch();
	}

	@Override
	public void showAlertDialog(Context context, int id, String title,
			String message, int icon_id, AlertDialogListener listener) {
		if (getActivity() != null
				&& getActivity() instanceof BaseFragmentActivity)
			((BaseFragmentActivity) getActivity()).showAlertDialog(
					getActivity(), id, title, message, icon_id, listener);
		else if (getActiveActivity() != null
				&& getActiveActivity() instanceof BaseFragmentActivity)
			((BaseFragmentActivity) getActiveActivity()).showAlertDialog(
					getActiveActivity(), id, title, message, icon_id, listener);
		else
			activeActivity.showAlertDialog(activeActivity, id, title, message,
					icon_id, listener);
	}

	@Override
	public void showLoadingDialog(Context context) {
		if (getActivity() != null
				&& getActivity() instanceof BaseFragmentActivity)
			((BaseFragmentActivity) getActivity())
					.showLoadingDialog(getActivity());
		else if (getActiveActivity() != null
				&& getActiveActivity() instanceof BaseFragmentActivity)
			((BaseFragmentActivity) getActiveActivity())
					.showLoadingDialog(getActiveActivity());
		else
			activeActivity.showLoadingDialog(activeActivity);
	}

	@Override
	public void showDecisionDialog(Context context, int id, String title,
			String message, String yes, String no,
			DecisionDialogListener listener) {
		if (getActivity() != null
				&& getActivity() instanceof BaseFragmentActivity)
			((BaseFragmentActivity) getActivity()).showDecisionDialog(
					getActivity(), id, title, message, yes, no, listener);
		else if (getActiveActivity() != null
				&& getActiveActivity() instanceof BaseFragmentActivity)
			((BaseFragmentActivity) getActiveActivity()).showDecisionDialog(
					getActiveActivity(), id, title, message, yes, no, listener);
		else
			activeActivity.showDecisionDialog(activeActivity, id, title,
					message, yes, no, listener);
	}

	@Override
	public void showOptionsDialog(Context context, int id, String title,
			String message, int icon, Option[] options,
			OptionsDialogListener listener) {
		if (getActivity() != null
				&& getActivity() instanceof BaseFragmentActivity)
			((BaseFragmentActivity) getActivity()).showOptionsDialog(
					getActivity(), id, title, message, icon, options, listener);
		else if (getActiveActivity() != null
				&& getActiveActivity() instanceof BaseFragmentActivity)
			((BaseFragmentActivity) getActiveActivity()).showOptionsDialog(
					getActiveActivity(), id, title, message, icon, options,
					listener);
		else
			activeActivity.showOptionsDialog(activeActivity, id, title,
					message, icon, options, listener);
	}

	@Override
	public void closeLoadingDialog() {
		if (getActivity() != null
				&& getActivity() instanceof BaseFragmentActivity)
			((BaseFragmentActivity) getActivity()).closeLoadingDialog();
		else if (getActiveActivity() != null
				&& getActiveActivity() instanceof BaseFragmentActivity)
			((BaseFragmentActivity) getActiveActivity()).closeLoadingDialog();
		else
			activeActivity.closeLoadingDialog();
	}

	@Override
	public void cancelBackgroundRequest(String tag) {
		if (getActivity() != null
				&& getActivity() instanceof BaseFragmentActivity)
			((BaseFragmentActivity) getActivity()).cancelBackgroundRequest(tag);
		else if (getActiveActivity() != null
				&& getActiveActivity() instanceof BaseFragmentActivity)
			((BaseFragmentActivity) getActiveActivity())
					.cancelBackgroundRequest(tag);
		else
			activeActivity.cancelBackgroundRequest(tag);
	}

	@Override
	public void cancelWebServiceRequest(String tag) {
		if (getActivity() != null
				&& getActivity() instanceof BaseFragmentActivity)
			((BaseFragmentActivity) getActivity()).cancelWebServiceRequest(tag);
		else if (getActiveActivity() != null
				&& getActiveActivity() instanceof BaseFragmentActivity)
			((BaseFragmentActivity) getActiveActivity())
					.cancelWebServiceRequest(tag);
		else
			activeActivity.cancelWebServiceRequest(tag);
	}

	@Override
	public void makeBackgroundRequest(String tag, RequestTarget target,
			String[] extras, Param content) {
		if (getActivity() != null
				&& getActivity() instanceof BaseFragmentActivity)
			((BaseFragmentActivity) getActivity()).makeBackgroundRequest(tag,
					target, extras, content);
		else if (getActiveActivity() != null
				&& getActiveActivity() instanceof BaseFragmentActivity)
			((BaseFragmentActivity) getActiveActivity()).makeBackgroundRequest(
					tag, target, extras, content);
		else
			activeActivity.makeBackgroundRequest(tag, target, extras, content);
	}

	@Override
	public void makeRequest(String tag, RequestTarget target, String[] extras,
			Param content, WebServiceResultHandler handler) {
		if (getActivity() != null
				&& getActivity() instanceof BaseFragmentActivity)
			((BaseFragmentActivity) getActivity()).makeRequest(tag, target,
					extras, content, handler);
		else if (getActiveActivity() != null
				&& getActiveActivity() instanceof BaseFragmentActivity)
			((BaseFragmentActivity) getActiveActivity()).makeRequest(tag,
					target, extras, content, handler);
		else
			activeActivity.makeRequest(tag, target, extras, content, handler);
	}

	@Override
	public String getResouceString(int id) {
		if (getActivity() != null
				&& getActivity() instanceof BaseFragmentActivity)
			return ((BaseFragmentActivity) getActivity()).getResouceString(id);
		else if (getActiveActivity() != null
				&& getActiveActivity() instanceof BaseFragmentActivity)
			return ((BaseFragmentActivity) getActiveActivity())
					.getResouceString(id);
		else
			return activeActivity.getResouceString(id);
	}

	@Override
	public Activity getActiveActivity() {
		return CentralApplication.getActiveActivity();
	}

	@Override
	public Context getCentralContext() {
		return CentralApplication.getContext();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (view != null)
			view.setClickable(true);
		onBindView();
		onInitializeViewData();
	}

	@Override
	public void onResume() {
		// EventBus.getDefault().register(this);
		onResumeObject();
		super.onResume();
		Utils.logHeap(getTag());
	}

	@Override
	public void onDetach() {
		onFreeObject();
		Utils.nullViewDrawablesRecursive(getView());
		Utils.unbindDrawables(getView());
		super.onDetach();
		activeActivity = null;
	}

	protected View findViewById(int id) {
		if(getView() != null) {
			View view = getView().findViewById(id);
			if (view != null && !BaseProperties.isExceptionalView(view)) {
				view.setOnClickListener(getSingleClick());
				view.setOnTouchListener(getSingleTouch());
			}
			return view;
		}
		return null;
	}

	protected void finish() {
		if (getActivity() != null
				&& getActivity() instanceof BaseFragmentActivity)
			getActivity().onBackPressed();
		else if (getActiveActivity() != null
				&& getActiveActivity() instanceof BaseFragmentActivity)
			getActiveActivity().onBackPressed();
		else
			activeActivity.onBackPressed();

	}

	protected void addFragment(int containerId, BaseFragment fragment,
			String tag) {
		if (getActivity() != null
				&& getActivity() instanceof BaseFragmentActivity)
			((BaseFragmentActivity) getActivity()).addFragment(containerId,
					fragment, tag);
		else if (getActiveActivity() != null
				&& getActiveActivity() instanceof BaseFragmentActivity)
			((BaseFragmentActivity) getActiveActivity()).addFragment(
					containerId, fragment, tag);
		else
			activeActivity.addFragment(containerId, fragment, tag);
	}

	protected void replaceFragment(int containerId, BaseFragment fragment,
			String tag, boolean clearStack) {
		if (getActivity() != null
				&& getActivity() instanceof BaseFragmentActivity)
			((BaseFragmentActivity) getActivity()).replaceFragment(containerId,
					fragment, tag, clearStack);
		else if (getActiveActivity() != null
				&& getActiveActivity() instanceof BaseFragmentActivity)
			((BaseFragmentActivity) getActiveActivity()).replaceFragment(
					containerId, fragment, tag, clearStack);
		else
			activeActivity.replaceFragment(containerId, fragment, tag,
					clearStack);
	}
}
