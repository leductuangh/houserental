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
import com.example.commonframe.model.base.BaseParam;
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
	public void startActivityForResult(Intent intent, int requestCode) {
		if (getActivity() != null)
			getActivity().startActivityForResult(intent, requestCode);
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
		return ((BaseFragmentActivity) getActivity()).getSingleTouch();
	}

	@Override
	public void showAlertDialog(Context context, int id, String title,
			String message, int icon_id, AlertDialogListener listener) {
		((BaseFragmentActivity) getActivity()).showAlertDialog(context, id,
				title, message, icon_id, listener);
	}

	@Override
	public void showLoadingDialog(Context context) {
		((BaseFragmentActivity) getActivity()).showLoadingDialog(context);
	}

	@Override
	public void showDecisionDialog(Context context, int id, String title,
			String message, String yes, String no,
			DecisionDialogListener listener) {
		((BaseFragmentActivity) getActivity()).showDecisionDialog(context, id,
				title, message, yes, no, listener);
	}

	@Override
	public void showOptionsDialog(Context context, int id, String title,
			String message, int icon, Option[] options,
			OptionsDialogListener listener) {
		((BaseFragmentActivity) getActivity()).showOptionsDialog(context, id,
				title, message, icon, options, listener);
	}

	@Override
	public void closeLoadingDialog() {
		((BaseFragmentActivity) getActivity()).closeLoadingDialog();
	}

	@Override
	public void cancelBackgroundRequest(String tag) {
		((BaseFragmentActivity) getActivity()).cancelBackgroundRequest(tag);
	}

	@Override
	public void cancelWebServiceRequest(String tag) {
		((BaseFragmentActivity) getActivity()).cancelWebServiceRequest(tag);
	}

	@Override
	public void makeBackgroundRequest(String tag, RequestTarget target,
			String[] extras, BaseParam content) {
		((BaseFragmentActivity) getActivity()).makeBackgroundRequest(tag,
				target, extras, content);
	}

	@Override
	public void makeRequest(String tag, RequestTarget target, String[] extras,
			BaseParam content, WebServiceResultHandler handler) {
		((BaseFragmentActivity) getActivity()).makeRequest(tag, target, extras,
				content, handler);
	}

	@Override
	public String getResouceString(int id) {
		return ((BaseFragmentActivity) getActivity()).getResouceString(id);
	}

	@Override
	public Activity getActiveActivity() {
		return ((BaseFragmentActivity) getActivity()).getActiveActivity();
	}

	@Override
	public Context getCentralContext() {
		return ((BaseFragmentActivity) getActivity()).getCentralContext();
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
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDetach() {
		onFreeObject();
		Utils.nullViewDrawablesRecursive(getView());
		Utils.unbindDrawables(getView());
		super.onDetach();
	}

	protected View findViewById(int id) {
		View view = getView().findViewById(id);
		if (view != null && !BaseProperties.isExceptionalView(view)) {
			view.setOnClickListener(getSingleClick());
			view.setOnTouchListener(getSingleTouch());
		}
		return view;
	}

	protected void finish() {
		((BaseFragmentActivity) getActivity()).onBackPressed();
	}

	protected void addFragment(int containerId, BaseFragment fragment,
			String tag) {
		((BaseFragmentActivity) getActivity()).addFragment(containerId,
				fragment, tag);
	}

	protected void replaceFragment(int containerId, BaseFragment fragment,
			String tag, boolean clearStack) {
		((BaseFragmentActivity) getActivity()).replaceFragment(containerId,
				fragment, tag, clearStack);
	}
}
