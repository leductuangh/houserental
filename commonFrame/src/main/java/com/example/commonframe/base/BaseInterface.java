package com.example.commonframe.base;

import android.app.Activity;
import android.content.Context;

import com.example.commonframe.connection.WebServiceRequester.WebServiceResultHandler;
import com.example.commonframe.dialog.AlertDialog.AlertDialogListener;
import com.example.commonframe.dialog.DecisionDialog.DecisionDialogListener;
import com.example.commonframe.dialog.Option;
import com.example.commonframe.dialog.OptionsDialog.OptionsDialogListener;
import com.example.commonframe.model.base.Param;
import com.example.commonframe.util.Constant.RequestTarget;
import com.example.commonframe.util.SingleClick;
import com.example.commonframe.util.SingleTouch;

public interface BaseInterface {

	/**
	 * This method is for initializing objects used in the activity. This method
	 * is called immediately after the <code>onCreate()</code> method of the
	 * activity and only called once when the activity is created. Any global
	 * objects that used inside the activity should be initialized here for the
	 * purpose of management.
	 */
	abstract void onCreateObject();

	/**
	 * This method is for handling the actions when user enter the application
	 * from a deep link. This method is called immediately after
	 * <code>onCreateObject()</code> method and only called once when the
	 * activity is created. Any actions and data from deep link sent to this
	 * activity must be handle in this method. <br>
	 * <b>Note</b>: The views have <b>NOT</b> been binded yet, so all
	 * interactions with the views <b>MUST BE AVOIDED</b> at this point.
	 */
	abstract void onDeepLinking();

	/**
	 * This method is for handling the actions when user enter the application
	 * from a notification. This method is called immediately after
	 * <code>onDeepLinking()</code> method and only called once when the
	 * activity is created. Any actions and data from notification sent to this
	 * activity must be handle in this method. <br>
	 * <b>Note</b>: The views have <b>NOT</b> been binded yet, so all
	 * interactions with the views <b>MUST BE AVOIDED</b> at this point.
	 */
	abstract void onNotification();

	/**
	 * This method is for attaching views to the object references used in the
	 * activity. This method is called immediately after the
	 * <code>setContentView()</code> method of the activity and only called once
	 * when the activity is created. Any views defined in xml file should be
	 * attached to object references in the activity here.
	 */
	abstract void onBindView();

	/**
	 * This method is for initialization of data into views after binding from
	 * layout. This method is called immediately after the
	 * <code>onBindView()</code> method of the activity and only called once
	 * when the activity is created. Any views had been binding in onBindView
	 * file can set the data here.
	 */
	abstract void onInitializeViewData();

	/**
	 * This method is for re-initiating objects after return from another
	 * activity. This method is called immediately after the
	 * <code>onResume()</code> method of the activity and called every time the
	 * activity become visible on the screen (back from another activity or
	 * return to the application from another application). Any object that
	 * needs to be re-asigned should be here such as connection, observer,
	 * listener.
	 */
	abstract void onResumeObject();

	/**
	 * This method is for releasing objects after the activity is finished. This
	 * method is called immediately after the <code>onStop()</code> method of
	 * the activity and maybe called or not depending on the memory situation.
	 * Any object that used in this activity should be released here.
	 */
	abstract void onFreeObject();

	/**
	 * This method is to show a decision dialog with defined values, only once
	 * instance of this dialog will be allowed at a time. If there are more than
	 * one, the previous will be dismissed. This dialog also auto close whenever
	 * a decision made.
	 * 
	 * @param context
	 *            The context which the dialog show on
	 * @param title
	 *            The title of this dialog if present, null if not
	 * @param message
	 *            The message of this dialog if present, null if not
	 * @param yes
	 *            The text for accepting the decision
	 * @param no
	 *            The text for rejecting the decision
	 * @param listener
	 *            The listener to handle the action from this dialog
	 */
	void showDecisionDialog(Context context, int id, String title,
			String message, String yes, String no,
			DecisionDialogListener listener);

	/**
	 * This method is to show an option dialog with defined values, only once
	 * instance of this dialog will be allowed at a time. If there are more than
	 * one, the previous will be dismissed. This dialog also auto close whenever
	 * an option chosen.
	 * 
	 * @param context
	 *            The context which the dialog show on
	 * @param title
	 *            The title of this dialog if present, null if not
	 * @param message
	 *            The message of this dialog if present, null if not
	 * @param icon
	 *            The icon id from resource of this dialog if present, -1 if not
	 * @param options
	 *            The array of instances of Option class including text and
	 *            icon, the dialog will show options base on this array
	 * @param listener
	 *            The listener to handle the action from this dialog
	 */
	void showOptionsDialog(Context context, int id, String title,
			String message, int icon, Option[] options,
			OptionsDialogListener listener);

	/**
	 * This method is to show an alert dialog with defined values, only once
	 * instance of this dialog will be allowed at a time. If there are more than
	 * one, the previous will be dismissed. This dialog also auto close whenever
	 * user clicks OK button.
	 * 
	 * @param context
	 *            The context which the dialog show on
	 * @param id
	 *            The id of this dialog in case many dialogs are shown
	 * @param title
	 *            The title of this dialog if present, null if not
	 * @param message
	 *            The message of this dialog if present, null if not
	 * @param icon_id
	 *            The id of the icon for this dialog if present, -1 if not
	 * @param listener
	 *            The listener to listen the confirmed action, the dialog will
	 *            dismiss and the action will be fired immediately after confirm
	 *            button clicked
	 */
	void showAlertDialog(Context context, int id, String title, String message,
			int icon_id, AlertDialogListener listener);

	/**
	 * This method is to show a loading dialog and stops user from interacting
	 * with other views. Only once instance of this dialog will be allowed at a
	 * time. If there are more than one, the previous will be dismissed. This
	 * dialog also auto close whenever the application move out of the screen
	 * (back to home).
	 * 
	 * @param context
	 *            The context which the dialog show on
	 */
	void showLoadingDialog(Context context);

	/**
	 * This method is to dismiss the loading dialog if present when finish the
	 * task and allow user to interact with other views.
	 */
	void closeLoadingDialog();

	/**
	 * This method is to find a string in the resource with a defined id.
	 * 
	 * @param id
	 *            The id of the string in resource
	 * @return String from resource, null if the id not found
	 */
	String getResourceString(int id);

	/**
	 * This method is to retrieve the application context as general.
	 * 
	 * @return The application context in CentralApplication
	 */
	Context getCentralContext();

	/**
	 * This method is to retrieve the active activity.
	 * 
	 * @return The active activity
	 */
	Activity getActiveActivity();

	/**
	 * This method is to cancel all webservice requests with a specific tag if
	 * any
	 * 
	 * @param tag
	 *            The web service requests will be canceled
	 */
	void cancelWebServiceRequest(String tag);

	/**
	 * This method is to cancel all background requests with a specific tag if
	 * any
	 * 
	 * @param tag
	 *            The background requests will be canceled
	 */
	void cancelBackgroundRequest(String tag);

	/**
	 * This method is for making a connection to server base on the target and
	 * parameters defined in request. <br>
	 * Every target must be defined in enum RequestTarget in Constant and
	 * override the <code>toString()</code> to return the actual function of the
	 * web-service. <br>
	 * Every request content must be derived from Param and implements
	 * <code>makeRequestParams()</code> for the parameters and
	 * <code>makeRequestHeaders()</code> for the headers of the web-service.
	 * This method can be used at any class implements BaseInterface. <br>
	 * To receive the result from the request, the activity must implement
	 * WebServiceResultHandler and re-asigned the requester with the activity as
	 * the WebServiceResultHandler.
	 * 
	 * @param tag
	 *            The activity starts this request
	 * @param target
	 *            The function requested to the server
	 * @param extras
	 *            The extra parameters to build api
	 * @param content
	 *            The content of the request including parameters and headers
	 * @param handler
	 *            The handler for the result returned from the request
	 */
	void makeRequest(String tag, RequestTarget target, String[] extras,
			Param content, WebServiceResultHandler handler);

	/**
	 * This method is for making a background connection to server base on the
	 * target and parameters defined in request. <br>
	 * Every target must be defined in enum RequestTarget in Constant and
	 * override the <code>toString()</code> to return the actual function of the
	 * web-service. <br>
	 * Every request content must be derived from Param and implements
	 * <code>makeRequestParams()</code> for the parameters and
	 * <code>makeRequestHeaders()</code> for the headers of the web-service.
	 * This method can be used at any class implements BaseInterface. <br>
	 * This method will return the result in background and it has the lowest
	 * priority <br>
	 * To handle the result of these request, implement the
	 * <code>onResponse</code> and <code>onErrorResponse</code> in
	 * <code>BackgroundServiceRequester</code>.
	 * 
	 * @param tag
	 *            The string indicate the id of this request
	 * @param target
	 *            The function requested to the server
	 * @param extras
	 *            The extra parameters to build api
	 * @param content
	 *            The content of the request including parameters and headers
	 */
	void makeBackgroundRequest(String tag, RequestTarget target,
			String[] extras, Param content);

	/**
	 * This method is to return the single instance of SingleTouch applying for
	 * entire application.
	 * 
	 * @return The SingleTouch instance
	 */
	SingleTouch getSingleTouch();

	/**
	 * This method is to return the single instance of SingleClick applying for
	 * entire application.
	 * 
	 * @return The SingleClick instance for each screen or fragment
	 */
	SingleClick getSingleClick();
}
