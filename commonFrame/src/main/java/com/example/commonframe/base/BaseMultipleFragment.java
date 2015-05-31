package com.example.commonframe.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import com.example.commonframe.connection.WebServiceRequester.WebServiceResultHandler;
import com.example.commonframe.dialog.AlertDialog.AlertDialogListener;
import com.example.commonframe.dialog.DecisionDialog.DecisionDialogListener;
import com.example.commonframe.dialog.Option;
import com.example.commonframe.dialog.OptionsDialog.OptionsDialogListener;
import com.example.commonframe.model.base.Param;
import com.example.commonframe.util.CentralApplication;
import com.example.commonframe.util.Constant.RequestTarget;
import com.example.commonframe.util.SingleClick;
import com.example.commonframe.util.SingleClick.SingleClickListener;
import com.example.commonframe.util.SingleTouch;
import com.example.commonframe.util.Utils;

public abstract class BaseMultipleFragment extends Fragment implements
		BaseInterface, SingleClickListener {

	/**
	 * The flag to indicate all stack of fragments should resume when the host
	 * activity is resuming. If true then all stacks will call resume, false
	 * only the top fragment will call resume. Change true or false depends on
	 * the behavior
	 */
	private static final boolean isAllAttachedToActivityLifeCycle = false;

	/**
	 * The single click to handle click action for this screen
	 */
	private SingleClick singleClick = null;

	/**
	 * Local active activity, in case the getActivity return null;
	 */
	private BaseMultipleFragmentActivity activeActivity;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		onCreateObject();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		activeActivity = (BaseMultipleFragmentActivity) activity;
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
				&& getActivity() instanceof BaseMultipleFragmentActivity)
			return ((BaseMultipleFragmentActivity) getActivity())
					.getSingleTouch();
		else if (getActiveActivity() != null
				&& getActiveActivity() instanceof BaseMultipleFragmentActivity)
			return ((BaseMultipleFragmentActivity) getActiveActivity())
					.getSingleTouch();
		else
			return activeActivity.getSingleTouch();
	}

	@Override
	public void showAlertDialog(Context context, int id, String title,
			String message, int icon_id, AlertDialogListener listener) {
		if (getActivity() != null
				&& getActivity() instanceof BaseMultipleFragmentActivity)
			((BaseMultipleFragmentActivity) getActivity()).showAlertDialog(
					getActivity(), id, title, message, icon_id, listener);
		else if (getActiveActivity() != null
				&& getActiveActivity() instanceof BaseMultipleFragmentActivity)
			((BaseMultipleFragmentActivity) getActiveActivity())
					.showAlertDialog(getActiveActivity(), id, title, message,
							icon_id, listener);
		else
			activeActivity.showAlertDialog(activeActivity, id, title, message,
					icon_id, listener);
	}

	@Override
	public void showLoadingDialog(Context context) {
		if (getActivity() != null
				&& getActivity() instanceof BaseMultipleFragmentActivity)
			((BaseMultipleFragmentActivity) getActivity())
					.showLoadingDialog(getActivity());
		else if (getActiveActivity() != null
				&& getActiveActivity() instanceof BaseMultipleFragmentActivity)
			((BaseMultipleFragmentActivity) getActiveActivity())
					.showLoadingDialog(getActiveActivity());
		else
			activeActivity.showLoadingDialog(activeActivity);
	}

	@Override
	public void showDecisionDialog(Context context, int id, String title,
			String message, String yes, String no,
			DecisionDialogListener listener) {
		if (getActivity() != null
				&& getActivity() instanceof BaseMultipleFragmentActivity)
			((BaseMultipleFragmentActivity) getActivity()).showDecisionDialog(
					getActivity(), id, title, message, yes, no, listener);
		else if (getActiveActivity() != null
				&& getActiveActivity() instanceof BaseMultipleFragmentActivity)
			((BaseMultipleFragmentActivity) getActiveActivity())
					.showDecisionDialog(getActiveActivity(), id, title,
							message, yes, no, listener);
		else
			activeActivity.showDecisionDialog(activeActivity, id, title,
					message, yes, no, listener);
	}

	@Override
	public void showOptionsDialog(Context context, int id, String title,
			String message, int icon, Option[] options,
			OptionsDialogListener listener) {
		if (getActivity() != null
				&& getActivity() instanceof BaseMultipleFragmentActivity)
			((BaseMultipleFragmentActivity) getActivity()).showOptionsDialog(
					getActivity(), id, title, message, icon, options, listener);
		else if (getActiveActivity() != null
				&& getActiveActivity() instanceof BaseMultipleFragmentActivity)
			((BaseMultipleFragmentActivity) getActiveActivity())
					.showOptionsDialog(getActiveActivity(), id, title, message,
							icon, options, listener);
		else
			activeActivity.showOptionsDialog(activeActivity, id, title,
					message, icon, options, listener);
	}

	@Override
	public void closeLoadingDialog() {
		if (getActivity() != null
				&& getActivity() instanceof BaseMultipleFragmentActivity)
			((BaseMultipleFragmentActivity) getActivity()).closeLoadingDialog();
		else if (getActiveActivity() != null
				&& getActiveActivity() instanceof BaseMultipleFragmentActivity)
			((BaseMultipleFragmentActivity) getActiveActivity())
					.closeLoadingDialog();
		else
			activeActivity.closeLoadingDialog();
	}

	@Override
	public void cancelBackgroundRequest(String tag) {
		if (getActivity() != null
				&& getActivity() instanceof BaseMultipleFragmentActivity)
			((BaseMultipleFragmentActivity) getActivity())
					.cancelBackgroundRequest(tag);
		else if (getActiveActivity() != null
				&& getActiveActivity() instanceof BaseMultipleFragmentActivity)
			((BaseMultipleFragmentActivity) getActiveActivity())
					.cancelBackgroundRequest(tag);
		else
			activeActivity.cancelBackgroundRequest(tag);
	}

	@Override
	public void cancelWebServiceRequest(String tag) {
		if (getActivity() != null
				&& getActivity() instanceof BaseMultipleFragmentActivity)
			((BaseMultipleFragmentActivity) getActivity())
					.cancelWebServiceRequest(tag);
		else if (getActiveActivity() != null
				&& getActiveActivity() instanceof BaseMultipleFragmentActivity)
			((BaseMultipleFragmentActivity) getActiveActivity())
					.cancelWebServiceRequest(tag);
		else
			activeActivity.cancelWebServiceRequest(tag);
	}

	@Override
	public void makeBackgroundRequest(String tag, RequestTarget target,
			String[] extras, Param content) {
		if (getActivity() != null
				&& getActivity() instanceof BaseMultipleFragmentActivity)
			((BaseMultipleFragmentActivity) getActivity())
					.makeBackgroundRequest(tag, target, extras, content);
		else if (getActiveActivity() != null
				&& getActiveActivity() instanceof BaseMultipleFragmentActivity)
			((BaseMultipleFragmentActivity) getActiveActivity())
					.makeBackgroundRequest(tag, target, extras, content);
		else
			activeActivity.makeBackgroundRequest(tag, target, extras, content);
	}

	@Override
	public void makeRequest(String tag, RequestTarget target, String[] extras,
			Param content, boolean loading, WebServiceResultHandler handler) {
		if (getActivity() != null
				&& getActivity() instanceof BaseMultipleFragmentActivity)
			((BaseMultipleFragmentActivity) getActivity()).makeRequest(tag,
					target, extras, content, loading, handler);
		else if (getActiveActivity() != null
				&& getActiveActivity() instanceof BaseMultipleFragmentActivity)
			((BaseMultipleFragmentActivity) getActiveActivity()).makeRequest(
					tag, target, extras, content, loading, handler);
		else
			activeActivity.makeRequest(tag, target, extras, content, loading,
					handler);
	}

	@Override
	public String getResourceString(int id) {
		if (getActivity() != null
				&& getActivity() instanceof BaseMultipleFragmentActivity)
			return ((BaseMultipleFragmentActivity) getActivity())
					.getResourceString(id);
		else if (getActiveActivity() != null
				&& getActiveActivity() instanceof BaseMultipleFragmentActivity)
			return ((BaseMultipleFragmentActivity) getActiveActivity())
					.getResourceString(id);
		else
			return activeActivity.getResourceString(id);
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
	public void onPause() {
		super.onPause();
		if (isAllAttachedToActivityLifeCycle) {
			onPauseObject();
		} else {
			pauseCurrentFragment();
		}
	}
	
	protected void onPauseObject() {
		// EventBus.getDefault().unregister(this);
		cancelRequest();
		closeLoadingDialog();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (isAllAttachedToActivityLifeCycle) {
			// EventBus.getDefault().register(this);
			onResumeObject();
		} else {
			resumeCurrentFragment();
		}
	}

	private void pauseCurrentFragment() {
		int containerId = ((ViewGroup) getView().getParent()).getId();
		if (getActivity() != null
				&& getActivity() instanceof BaseMultipleFragmentActivity) {
			BaseMultipleFragment top = ((BaseMultipleFragmentActivity) getActivity())
					.getTopFragment(containerId);
			if (top != null && !Utils.isEmpty(top.getTag())
					&& getTag().equals(top.getTag())) {
				top.onPauseObject();
			}
		} else if (getActiveActivity() != null
				&& getActiveActivity() instanceof BaseMultipleFragmentActivity) {
			BaseMultipleFragment top = ((BaseMultipleFragmentActivity) getActiveActivity())
					.getTopFragment(containerId);
			if (top != null && !Utils.isEmpty(top.getTag())
					&& getTag().equals(top.getTag())) {
				top.onPauseObject();
			}
		} else if (activeActivity != null) {
			BaseMultipleFragment top = activeActivity
					.getTopFragment(containerId);
			if (top != null && !Utils.isEmpty(top.getTag())
					&& getTag().equals(top.getTag())) {
				top.onPauseObject();
			}
		}
	}

	private void resumeCurrentFragment() {
		int containerId = ((ViewGroup) getView().getParent()).getId();
		if (getActivity() != null
				&& getActivity() instanceof BaseMultipleFragmentActivity) {
			BaseMultipleFragment top = ((BaseMultipleFragmentActivity) getActivity())
					.getTopFragment(containerId);
			if (top != null && !Utils.isEmpty(top.getTag())
					&& getTag().equals(top.getTag())) {
				// EventBus.getDefault().register(this);
				onResumeObject();
			}
		} else if (getActiveActivity() != null
				&& getActiveActivity() instanceof BaseMultipleFragmentActivity) {
			BaseMultipleFragment top = ((BaseMultipleFragmentActivity) getActiveActivity())
					.getTopFragment(containerId);
			if (top != null && !Utils.isEmpty(top.getTag())
					&& getTag().equals(top.getTag())) {
				// EventBus.getDefault().register(this);
				onResumeObject();
			}
		} else if (activeActivity != null) {
			BaseMultipleFragment top = activeActivity
					.getTopFragment(containerId);
			if (top != null && !Utils.isEmpty(top.getTag())
					&& getTag().equals(top.getTag())) {
				// EventBus.getDefault().register(this);
				onResumeObject();
			}
		}
	}

	private void cancelRequest() {
		if (getActivity() != null)
			((BaseMultipleFragmentActivity) getActivity()).cancelRequest();
		else if (getActiveActivity() != null)
			((BaseMultipleFragmentActivity) getActiveActivity())
					.cancelRequest();
		else
			activeActivity.cancelRequest();
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
		if (getView() != null) {
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
		int containerId = ((ViewGroup) getView().getParent()).getId();
		if (containerId != View.NO_ID && containerId >= 0) {
			if (getActivity() != null
					&& getActivity() instanceof BaseMultipleFragmentActivity)
				((BaseMultipleFragmentActivity) getActivity()).backStack(
						containerId, null);
			else if (getActiveActivity() != null
					&& getActiveActivity() instanceof BaseMultipleFragmentActivity)
				((BaseMultipleFragmentActivity) getActiveActivity()).backStack(
						containerId, null);
			else
				activeActivity.backStack(containerId, null);
		}

	}

	protected void addFragment(int containerId, BaseMultipleFragment fragment,
			String tag) {
		if (getActivity() != null
				&& getActivity() instanceof BaseMultipleFragmentActivity)
			((BaseMultipleFragmentActivity) getActivity()).addFragment(
					containerId, fragment, tag);
		else if (getActiveActivity() != null
				&& getActiveActivity() instanceof BaseMultipleFragmentActivity)
			((BaseMultipleFragmentActivity) getActiveActivity()).addFragment(
					containerId, fragment, tag);
		else
			activeActivity.addFragment(containerId, fragment, tag);
	}

	protected void replaceFragment(int containerId,
			BaseMultipleFragment fragment, String tag, boolean clearStack) {
		if (getActivity() != null
				&& getActivity() instanceof BaseMultipleFragmentActivity)
			((BaseMultipleFragmentActivity) getActivity()).replaceFragment(
					containerId, fragment, tag, clearStack);
		else if (getActiveActivity() != null
				&& getActiveActivity() instanceof BaseMultipleFragmentActivity)
			((BaseMultipleFragmentActivity) getActiveActivity())
					.replaceFragment(containerId, fragment, tag, clearStack);
		else
			activeActivity.replaceFragment(containerId, fragment, tag,
					clearStack);
	}
}
