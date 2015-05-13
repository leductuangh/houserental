package com.example.commonframe.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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

import java.io.Serializable;

public abstract class BaseFragment extends Fragment implements BaseInterface,
		SingleClickListener {

	private final String FRAGMENT_SERIALIZABLE_ARGUMENTS = "fragment_serializable_arguments_"
			+ this.getClass().getSimpleName();

	private final String FRAGMENT_PARCELABLE_ARGUMENTS = "fragment_parcelable_arguments_"
			+ this.getClass().getSimpleName();

	/**
	 * The flag to indicate all stack of fragments should resume when the host
	 * activity is resuming. If true then all stack will call resume, false only
	 * the top fragment will call resume. Change true or false depends on the
	 * behavior
	 */
	private static final boolean isAllFragmentsResume = false;

	/**
	 * The single click to handle click action for this screen
	 */
	private SingleClick singleClick = null;

	/**
	 * Local active activity, in case the getActivity return null;
	 */
	private BaseFragmentActivity activeActivity;

	/**
	 * This method is for restoring serializable objects which were stored in
	 * <code>getRestorableInstance</code>. This method is called immediately
	 * after the <code>onCreate()</code> method of the fragment, before
	 * <code>onCreateObject()</code> and only called once when the fragment is
	 * created. Any global objects that used inside the fragment should be
	 * restored here.
	 * 
	 * @param serializables
	 *            The array of parameters re-assigned to global variables
	 */
	protected abstract void onRestore(Serializable... serializables);

	/**
	 * This method is for restoring parcelable objects which were stored in
	 * <code>getRestorableInstance()</code>. This method is called immediately
	 * after the <code>onCreate()</code> method of the fragment, before
	 * <code>onCreateObject()</code> and only called once when the fragment is
	 * created. Any global objects that used inside the fragment should be
	 * restored here.
	 * 
	 * @param parcelables
	 *            The array of parameters re-assigned to global variables
	 */
	protected abstract void onRestore(Parcelable... parcelables);

	/**
	 * This method is for initiating an instance of this class with serializable
	 * parameters. This is to avoid using the arguments constructor (only use
	 * empty constructor for fragment). The serializables after passing in will
	 * be stored in <code>setArguments()</code>. These parameters will be
	 * re-used (re-assigned to the global variables of this class) once the
	 * fragment is killed and re-initiated by android system. The
	 * <code>onRestore()</code> method must be implemented to re-assigned the
	 * arguments to the global variables. <br/>
	 * <b>NOTE</b>: The parcelables is more preferable in term of performance
	 * (compare to serializables). Parcelable are twice faster than Serializable.
	 * 
	 * @param serializables
	 *            The array of parameters assigned to this fragment
	 */
	public BaseFragment getRestorableInstance(Serializable... serializables) {
		Bundle arguments = new Bundle();
		arguments.putSerializable(FRAGMENT_SERIALIZABLE_ARGUMENTS,
				serializables);
		setArguments(arguments);
		return this;
	}

	/**
	 * This method is for initiating an instance of this class with parcelable
	 * parameters. This is to avoid using the arguments constructor (only use
	 * empty constructor for fragment). The parcelable after passing in will be
	 * stored in <code>setArguments()</code>. These parameters will be re-used
	 * (re-assigned to the global variables of this class) once the fragment is
	 * killed and re-initiated by android system. The <code>onRestore()</code>
	 * method must be implemented to re-assigned the arguments to the global
	 * variables.
	 * 
	 * @param parcelables
	 *            The array of parameters assigned to this fragment
	 */
	public BaseFragment getRestorableInstance(Parcelable... parcelables) {
		Bundle arguments = new Bundle();
		arguments
				.putParcelableArray(FRAGMENT_PARCELABLE_ARGUMENTS, parcelables);
		setArguments(arguments);
		return this;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle arguments = getArguments();
		if (arguments != null
				&& arguments.getSerializable(FRAGMENT_SERIALIZABLE_ARGUMENTS) != null)
			onRestore(arguments
					.getSerializable(FRAGMENT_SERIALIZABLE_ARGUMENTS));
		if (arguments != null
				&& arguments.getParcelableArray(FRAGMENT_PARCELABLE_ARGUMENTS) != null)
			onRestore(arguments
					.getParcelableArray(FRAGMENT_PARCELABLE_ARGUMENTS));

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
	public String getResourceString(int id) {
		if (getActivity() != null
				&& getActivity() instanceof BaseFragmentActivity)
			return ((BaseFragmentActivity) getActivity()).getResourceString(id);
		else if (getActiveActivity() != null
				&& getActiveActivity() instanceof BaseFragmentActivity)
			return ((BaseFragmentActivity) getActiveActivity())
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
	public void onResume() {
		super.onResume();
		if (isAllFragmentsResume) {
			// EventBus.getDefault().register(this);
			onResumeObject();
		} else {
			if (getActivity() != null
					&& getActivity() instanceof BaseFragmentActivity) {
				BaseFragment top = ((BaseFragmentActivity) getActivity())
						.getTopFragment();
				if (top != null && top.getTag().equals(getTag())) {
					// EventBus.getDefault().register(this);
					top.onResumeObject();
				}
			} else if (getActiveActivity() != null
					&& getActiveActivity() instanceof BaseFragmentActivity) {
				BaseFragment top = ((BaseFragmentActivity) getActiveActivity())
						.getTopFragment();
				if (top != null && top.getTag().equals(getTag())) {
					// EventBus.getDefault().register(this);
					top.onResumeObject();
				}
			} else if (activeActivity != null) {
				BaseFragment top = activeActivity.getTopFragment();
				if (top != null && top.getTag().equals(getTag())) {
					// EventBus.getDefault().register(this);
					top.onResumeObject();
				}
			}
		}

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
