package core.base;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.commonframe.R;

import java.util.NoSuchElementException;
import java.util.Stack;

import core.connection.BackgroundServiceRequester;
import core.connection.Requester;
import core.connection.WebServiceRequester;
import core.connection.WebServiceRequester.WebServiceResultHandler;
import core.connection.queue.QueueElement.Type;
import core.dialog.GeneralDialog;
import core.dialog.GeneralDialog.ConfirmListener;
import core.dialog.GeneralDialog.DecisionListener;
import core.dialog.LoadingDialog;
import core.util.ActionTracker;
import core.util.Constant;
import core.util.Constant.RequestTarget;
import core.util.DLog;
import core.util.SingleClick;
import core.util.SingleClick.SingleClickListener;
import core.util.SingleTouch;
import core.util.Utils;
import icepick.Icepick;

/**
 * @author Tyrael
 * @version 1.0 <br>
 *          <br>
 *          <b>Class Overview</b> <br>
 *          <br>
 *          Represents a class for essential fragment activity to be a super
 *          class of activities in project. It includes supportive method of
 *          showing, closing dialogs, making and canceling request. Those
 *          methods can be used in any derived class. <br>
 *          This class also supports multiple fragment containers for both
 *          tablet and phone with methods of add, remove, replace, back and
 *          clear fragments on a specific container. <br>
 *          The derived classes must implement <code>onBaseCreate()</code>,
 *          <code>onBindView()</code>, <code>onResumeObject()</code>,
 *          <code>onFreeObject()</code> for the purpose of management.
 * @since May 2015
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class BaseMultipleFragmentActivity extends FragmentActivity
        implements BaseInterface, SingleClickListener {
    /**
     * Tag of BaseFragmentActivity class for Log usage
     */
    private static String TAG = BaseMultipleFragmentActivity.class.getSimpleName();
    /**
     * The array of fragment containers and all of its stacks. Each entry is
     * defined by the id of the container.
     */
    private final SparseArray<Stack<BaseMultipleFragment>> containers = new SparseArray<>();
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
    private boolean isFragmentsInitialized = false;
    /**
     * The identification of the main fragment container, the default is the
     * first container added. Or it can be set by
     * <code>setMainContainerId()</code>. The id is used for default
     * <code>onBackPress()</code>, <code>onDeepLinking()</code>,
     * <code>onNotification()</code>, <code>onActivityResult()</code>
     */
    private int mainContainerId = -1;

    /**
     * This method is for initializing fragments used in the activity. This
     * method is called immediately after the <code>onResumeFragments()</code>
     * method of the activity and only called once when the activity is created,
     * it depends on the <code>isFragmentsInitialized</code>. Any first
     * fragments that used inside the activity should be initialized here for
     * the purpose of management.
     */
    protected abstract void onInitializeFragments();

    /**
     * This method is for handling the back stack event of the last fragment of
     * one container.
     *
     * @param containerId The container id of the last fragment when back stack event
     *                    called
     */
    protected abstract void onLastFragmentBack(int containerId);

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
                .getInstance(BaseApplication.getContext());
        BaseProperties.bgRequester = BackgroundServiceRequester
                .getInstance(BaseApplication.getContext());
        BaseApplication.setActiveActivity(this);
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
                if (getTopFragment(mainContainerId) != null)
                    getTopFragment(mainContainerId).onDeepLinking(
                            new Intent(getIntent()));

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
                    if (getTopFragment(mainContainerId) != null)
                        getTopFragment(mainContainerId).onNotification(
                                new Intent(getIntent()));
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
    protected void onResumeFragments() {
        super.onResumeFragments();
        // this code is to prevent using commitAllowStateLoss
        if (!isFragmentsInitialized) {
            isFragmentsInitialized = true;
            onInitializeFragments();
        }
    }

    @Override
    protected void onStop() {
        if (isFinished) {
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
        if (isFinished) {
            clearAllStacks();
        }
    }

    @Override
    public void onBackPressed() {
        if (BaseProperties.getSingleBackPress().onBackPressAllowed()) {
            // super.onBackPressed();
            backStack(mainContainerId, null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        BaseMultipleFragment fragment = getTopFragment(mainContainerId);
        if (fragment != null)
            fragment.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View findViewById(int id) {
        View view = super.findViewById(id);
        if (view != null && !isExceptionalView(view)) {
            view.setOnClickListener(getSingleClick());
            view.setOnTouchListener(getSingleTouch());
        }
        return view;
    }

    @Override
    public boolean isExceptionalView(View view) {
        return BaseProperties.isExceptionalView(view);
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
        return BaseApplication.getActiveActivity();
    }

    @Override
    public Context getCentralContext() {
        return BaseApplication.getContext();
    }

    public void cancelRequest() {
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

    public int getMainContainerId() {
        return this.mainContainerId;
    }

    protected void setMainContainerId(int mainContainerId) {
        this.mainContainerId = mainContainerId;
    }

    public BaseMultipleFragment getTopFragment(int containerId) {
        try {
            Stack<BaseMultipleFragment> fragments = containers.get(containerId);
            if (fragments != null)
                return fragments.lastElement();
        } catch (NoSuchElementException e) {
            // ignore this exception
        }
        return null;
    }

    private void clearStack(int containerId) {
        Stack<BaseMultipleFragment> fragments = containers.get(containerId);
        if (fragments != null)
            fragments.removeAllElements();
    }

    private void clearAllStacks() {
        for (int i = 0; i < containers.size(); ++i) {
            Stack<BaseMultipleFragment> stack = containers.get(i);
            if (stack != null)
                stack.removeAllElements();
        }
        containers.clear();
    }

    public void backStack(int containerId, String toTag) {
        if (getSupportFragmentManager() != null) {
            Stack<BaseMultipleFragment> fragments = containers.get(containerId);
            if (fragments != null) {
                if (fragments.size() <= 1 && Utils.isEmpty(toTag)) {
                    onLastFragmentBack(containerId);
                } else {
                    FragmentTransaction transaction = getSupportFragmentManager()
                            .beginTransaction();
                    for (int i = fragments.size() - 1; i > 0; --i) {
                        BaseMultipleFragment entry = fragments.get(i);
                        if (entry != null) {
                            View view = entry.getView();
                            if (Utils.isEmpty(toTag)) {
                                animateBackOut(view, entry.getBackOutAnimation());
                                entry.onBasePause();
                                fragments.remove(i);
                                transaction.remove(entry);
                                ActionTracker.exitScreen(entry.getTag());
                                break;
                            } else {
                                if (toTag.equals(entry.getTag()))
                                    break;
                                animateBackOut(view, entry.getBackOutAnimation());
                                entry.onBasePause();
                                fragments.remove(i);
                                transaction.remove(entry);
                                ActionTracker.exitScreen(entry.getTag());
                            }
                        }
                    }
                    transaction.commit();
                    getSupportFragmentManager().executePendingTransactions();
                    BaseMultipleFragment fragment = getTopFragment(containerId);
                    if (fragment != null) {
                        if (fragment.getView() != null) {
                            View view = fragment.getView();
                            animateBackIn(view, fragment.getBackInAnimation());
                        }
                        fragment.onResume();
                    }
                }
            }
        }
    }

    protected void popAllBackStack(int containerId) {
        if (getSupportFragmentManager() != null) {
            try {
                Stack<BaseMultipleFragment> fragments = containers
                        .get(containerId);
                if (fragments != null) {
                    BaseMultipleFragment last = getTopFragment(containerId);
                    if (last != null) {
                        animateAddOut(containerId);
                    }
                    FragmentTransaction transaction = getSupportFragmentManager()
                            .beginTransaction();
                    for (BaseMultipleFragment fragment : fragments) {
                        transaction.remove(fragment);
                    }
                    if (transaction != null) {
                        transaction.commit();
                        getSupportFragmentManager()
                                .executePendingTransactions();
                    }
                    clearStack(containerId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void addFragment(int containerId, BaseMultipleFragment fragment,
                               String tag) {
        BaseMultipleFragment top = getTopFragment(containerId);
        if (top != null) {
            if (tag.equals(top.getTag()))
                return;
            top.onPause();
        }
        animateAddOut(containerId);
        if (getSupportFragmentManager() != null) {
            Stack<BaseMultipleFragment> fragments = containers.get(containerId);
            if (fragments == null) {
                if (mainContainerId == -1)
                    mainContainerId = containerId;
                containers.append(containerId,
                        fragments = new Stack<>());
                fragments.add(fragment);
                FragmentTransaction transaction = getSupportFragmentManager()
                        .beginTransaction();
                int anim = fragment.getEnterInAnimation();
                if (anim == -1) {
                    anim = Constant.DEFAULT_ADD_ANIMATION[0];
                }
                transaction
                        .setCustomAnimations(anim,
                                0, 0, 0) // add in animation
                        .add(containerId, fragment, tag).commit();
                getSupportFragmentManager().executePendingTransactions();
            } else {
                boolean isExist = false;
                for (BaseMultipleFragment bf : fragments) {
                    if (bf != null && !Utils.isEmpty(bf.getTag())
                            && bf.getTag().equals(tag)) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    fragments.add(fragment);
                    FragmentTransaction transaction = getSupportFragmentManager()
                            .beginTransaction();
                    int anim = fragment.getEnterInAnimation();
                    if (anim == -1) {
                        anim = Constant.DEFAULT_ADD_ANIMATION[0];
                    }
                    transaction
                            .setCustomAnimations(
                                    anim, 0, 0, 0) // add
                                    // in
                                    // animation
                            .add(containerId, fragment, tag).commit();
                    getSupportFragmentManager().executePendingTransactions();
                } else {
                    backStack(containerId, tag);
                }
            }
        }
    }

    protected void replaceFragment(int containerId,
                                   BaseMultipleFragment fragment, String tag, boolean clearStack) {
        if (getSupportFragmentManager() != null) {
            Stack<BaseMultipleFragment> fragments = containers.get(containerId);
            if (fragments != null) {
                if (clearStack) {
                    popAllBackStack(containerId);
                    addFragment(containerId, fragment, tag);
                } else {
                    boolean isExist = false;
                    for (int i = 0; i < fragments.size(); ++i) {
                        BaseMultipleFragment entry = fragments.get(i);
                        if (entry != null && entry.getTag().equals(tag)) {
                            isExist = true;
                            break;
                        }
                    }
                    if (isExist) {
                        if (fragments.size() > 1) {
                            BaseMultipleFragment top = getTopFragment(containerId);
                            if (!(top != null && top.getTag().equals(tag))) {
                                backStack(containerId, tag);
                            }
                        }
                    } else {
                        if (fragments.size() > 1) {
                            BaseMultipleFragment top = getTopFragment(containerId);
                            addFragment(containerId, fragment, tag);
                            if (top != null && !Utils.isEmpty(top.getTag()))
                                removeFragment(containerId, top.getTag());
                        } else {
                            popAllBackStack(containerId);
                            addFragment(containerId, fragment, tag);
                        }
                    }
                }
            } else {
                addFragment(containerId, fragment, tag);
            }
        }
    }

    protected void removeFragment(int containerId, String tag) {
        Stack<BaseMultipleFragment> fragments = containers.get(containerId);
        if (fragments != null) {
            BaseMultipleFragment removed = getTopFragment(containerId);
            if (removed != null && removed.getTag().equals(tag)) {
                backStack(containerId, null);
            } else {
                for (int i = 0; i < fragments.size(); ++i) {
                    removed = fragments.get(i);
                    if (removed.getTag().equals(tag)) {
                        getSupportFragmentManager().beginTransaction()
                                .remove(removed).commit();
                        getSupportFragmentManager()
                                .executePendingTransactions();
                        fragments.remove(i);
                        break;
                    }
                }
            }
        }
    }

    private void animateAddOut(int containerId) {
        BaseMultipleFragment previous = getTopFragment(containerId);
        if (previous != null) {
            final View view = previous.getView();
            if (view != null) {
                int anim = previous.getEnterOutAnimation();
                if (anim == -1) {
                    anim = Constant.DEFAULT_ADD_ANIMATION[1];
                }
                Animation animation = AnimationUtils.loadAnimation(this,
                        anim);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        view.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                view.startAnimation(animation);
            }
        }
    }

    private void animateBackIn(final View view, int anim) {
        if (view != null) {
            if (anim == -1) {
                anim = Constant.DEFAULT_BACK_ANIMATION[0];
            }
            Animation animation = AnimationUtils.loadAnimation(this,
                    anim);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    view.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            view.startAnimation(animation);
        }
    }

    private void animateBackOut(View view, int anim) {
        if (view != null) {
            if (anim == -1) {
                anim = Constant.DEFAULT_BACK_ANIMATION[1];
            }
            view.startAnimation(AnimationUtils.loadAnimation(this,
                    anim));
        }
    }
}
