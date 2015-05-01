package com.example.commonframe.base;

import java.util.NoSuchElementException;
import java.util.Stack;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

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
 *          Represents a class for essential fragment activity to be a super
 *          class of activities in project. It includes supportive method of
 *          showing, closing dialogs, making and canceling request. Those
 *          methods can be used in any derived class. <br>
 *          The derived classes must implement <code>onCreateObject()</code>,
 *          <code>onBindView()</code>, <code>onResumeObject()</code>,
 *          <code>onFreeObject()</code> for the purpose of management.
 * 
 */
public abstract class BaseFragmentActivity extends FragmentActivity implements
		BaseInterface, SingleClickListener, OnBackStackChangedListener {
	/**
	 * Tag of BaseFragmentActivity class for Log usage
	 */
	protected static String TAG = "BaseFragmentActivity";

	/**
	 * The single click to handle click action for this screen
	 */
	private SingleClick singleClick = null;

	/**
	 * The flag indicating that the activity is finished and should free all of
	 * resources at <code>onStop()</code> method
	 */
	private boolean isFinished = false;

	/**
	 * The flag indicating that the fragments are first initialized after the
	 * activity created, this variable is only invoked once.
	 */
	private boolean isFragmentInitialized = false;

	/**
	 * The flag indicating the process of popping all fragments
	 */
	private boolean isPopAllFragments = false;

	/**
	 * The flag indicating the fragment is replaced without clearing stack
	 */
	private boolean isRelace = false;

	/**
	 * The flag indicating the fragment is being removed from stack by back
	 * press
	 */
	private boolean isBackStack = false;

	private final Stack<BaseFragment> fragments = new Stack<BaseFragment>();

	protected abstract void onInitializeFragments();

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
				if (getTopFragment() != null)
					getTopFragment().onDeepLinking();
			} else if (getIntent().getExtras() != null
					&& getIntent().getBooleanExtra(
							Constant.NOTIFICATION_DEFINED, false)) {

				int id = getIntent().getIntExtra(Constant.NOTIFICATION_ID, -1);
				if (id != -1) {
					NotificationManager manager = (NotificationManager) getCentralContext()
							.getSystemService(Context.NOTIFICATION_SERVICE);
					manager.cancel(id);
					onNotification();
					if (getTopFragment() != null)
						getTopFragment().onNotification();
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
		if (getSupportFragmentManager() != null) {
			getSupportFragmentManager().removeOnBackStackChangedListener(this);
			getSupportFragmentManager().addOnBackStackChangedListener(this);
		}
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
	protected void onResumeFragments() {
		super.onResumeFragments();
		// this code is to prevent using commitAllowStateLoss
		if (!isFragmentInitialized) {
			isFragmentInitialized = true;
			onInitializeFragments();
		}
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
		overridePendingTransition(Constant.DEFAULT_EXIT_ANIMATION[0],
				Constant.DEFAULT_EXIT_ANIMATION[1]);
	}

	@Override
	protected void onPause() {
		// EventBus.getDefault().unregister(this);
		cancelRequest();
		closeLoadingDialog();
		if (isFinished) {
			clearStack();
			clearFragments();
		}
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		if (BaseProperties.getSingleBackPress().onBackPressAllowed()) {
			// super.onBackPressed();
			backStack(null);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		BaseFragment fragment = getTopFragment();
		if (fragment != null)
			fragment.onActivityResult(requestCode, resultCode, data);

		super.onActivityResult(requestCode, resultCode, data);
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
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			Utils.closeSoftKeyboard(this, findViewById(android.R.id.content)
					.getRootView());
			return true;
		}
		return super.onTouchEvent(event);
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
		BaseProperties.wsRequester = (null);
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
					getResouceString(R.string.error_internet_unavailable_title),
					getResouceString(R.string.error_internet_unavailable_message),
					-1, null);
			return;
		}
		showLoadingDialog(this);
		if (!Requester.startWSRequest(tag, target, extras, content, handler))
			closeLoadingDialog();
	}

	@Override
	public SingleTouch getSingleTouch() {
		return BaseProperties.getSingleTouch();
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
	public void onBackStackChanged() {
		FragmentManager manager = getSupportFragmentManager();
		if (manager != null) {
			BaseFragment fragment = getTopFragment();
			if (fragment != null && isBackStack && !isPopAllFragments
					&& !isRelace) {
				isBackStack = false;
				if (fragment.getView() != null)
					fragment.getView().setVisibility(View.VISIBLE);
				fragment.onResume();
			}
		}
	}

	public BaseFragment getTopFragment() {
		try {
			return fragments.lastElement();
		} catch (NoSuchElementException e) {
			// e.printStackTrace();
		}
		return null;
	}

	private void clearStack() {
		fragments.removeAllElements();
	}

	private void clearFragments() {
		if (getSupportFragmentManager() != null
				&& getSupportFragmentManager().getFragments() != null) {
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			for (int i = 0; i < getSupportFragmentManager().getFragments()
					.size(); ++i) {
				Fragment fragment = getSupportFragmentManager().getFragments()
						.get(i);
				if (fragment != null) {
					transaction.remove(fragment);
					fragment = null;
				}
			}
			transaction.commit();
			getSupportFragmentManager().executePendingTransactions();
		}
	}

	private void backStack(String toTag) {
		if (getSupportFragmentManager() != null) {
			if (fragments.size() <= 1 && Utils.isEmpty(toTag)) {
				popAllBackStacks();
				finish();
			} else {
				FragmentTransaction transaction = getSupportFragmentManager()
						.beginTransaction();
				for (int i = fragments.size() - 1; i > 0; --i) {
					BaseFragment entry = fragments.get(i);
					if (entry != null) {
						if (Utils.isEmpty(toTag)) {
							isBackStack = true;
							fragments.remove(i);
							getSupportFragmentManager().popBackStackImmediate(
									entry.getTag(),
									FragmentManager.POP_BACK_STACK_INCLUSIVE);
							transaction.remove(entry);
							break;
						} else {
							if (toTag.equals(entry.getTag()))
								break;
							isBackStack = true;
							fragments.remove(i);
							getSupportFragmentManager().popBackStackImmediate(
									entry.getTag(),
									FragmentManager.POP_BACK_STACK_INCLUSIVE);
							transaction.remove(entry);
						}
					}
				}
				transaction.commit();
				getSupportFragmentManager().executePendingTransactions();
				animateFragmentExit();
			}
		}
	}

	protected void popAllBackStacks() {
		if (getSupportFragmentManager() != null) {
			try {
				isPopAllFragments = true;
				clearStack();
				clearFragments();
				getSupportFragmentManager().popBackStackImmediate(null,
						FragmentManager.POP_BACK_STACK_INCLUSIVE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		isPopAllFragments = false;
	}

	protected void addFragment(int containerId, BaseFragment fragment,
			String tag) {
		animateFragmentEnter();
		if (getSupportFragmentManager() != null) {
			boolean isExist = false;
			for (BaseFragment bf : fragments) {
				if (bf != null && bf.getTag().equals(tag)) {
					isExist = true;
					break;
				}
			}
			if (!isExist) {
				FragmentTransaction transaction = getSupportFragmentManager()
						.beginTransaction();
				transaction
						.setCustomAnimations(
								Constant.DEFAULT_ENTER_ANIMATION[0],
								R.anim.slide_out_bottom, R.anim.slide_in_top,
								Constant.DEFAULT_EXIT_ANIMATION[1])
						.add(containerId, fragment, tag).addToBackStack(tag)
						.commit();
				getSupportFragmentManager().executePendingTransactions();
				fragments.add(fragment);
			} else {
				backStack(tag);
			}
		}

	}

	protected void replaceFragment(int containerId, BaseFragment fragment,
			String tag, boolean clearStack) {
		if (getSupportFragmentManager() != null) {
			if (clearStack) {
				animateFragmentEnter();
				popAllBackStacks();
				addFragment(containerId, fragment, tag);
			} else {
				boolean isExist = false;
				for (int i = 0; i < fragments.size(); ++i) {
					BaseFragment entry = fragments.get(i);
					if (entry != null && entry.getTag().equals(tag)) {
						isExist = true;
						break;
					}
				}
				if (isExist) {
					if (fragments.size() > 1)
						backStack(tag);
				} else {
					isRelace = true;
					if (fragments.size() > 1) {
						backStack(null);
						addFragment(containerId, fragment, tag);
					} else {
						popAllBackStacks();
						addFragment(containerId, fragment, tag);
					}
					isRelace = false;
				}
			}
		}
	}

	protected void removeFragment(String tag) {
		BaseFragment removed = getTopFragment();
		if (removed != null && removed.getTag().equals(tag)) {
			backStack(null);
		} else {
			for (int i = 0; i < fragments.size(); ++i) {
				removed = fragments.get(i);
				if (removed.getTag().equals(tag)) {
					getSupportFragmentManager().beginTransaction()
							.remove(removed).commit();
					getSupportFragmentManager().executePendingTransactions();
					fragments.remove(i);
					break;
				}
			}
		}
	}

	private void animateFragmentEnter() {
		BaseFragment previous = getTopFragment();
		if (previous != null) {
			final View view = previous.getView();
			Animation gone = AnimationUtils.loadAnimation(this,
					Constant.DEFAULT_ENTER_ANIMATION[1]);
			gone.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					view.setVisibility(View.GONE);
				}
			});
			previous.getView().startAnimation(gone);

		}
	}

	private void animateFragmentExit() {
		BaseFragment current = getTopFragment();
		if (current != null) {
			current.getView().startAnimation(
					AnimationUtils.loadAnimation(this,
							Constant.DEFAULT_EXIT_ANIMATION[0]));
		}
	}
}
