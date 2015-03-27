package com.example.commonframe.base;

import java.util.HashMap;

import com.example.commonframe.connection.BackgroundServiceRequester;
import com.example.commonframe.connection.WebServiceRequester;
import com.example.commonframe.dialog.AlertDialog;
import com.example.commonframe.dialog.DecisionDialog;
import com.example.commonframe.dialog.LoadingDialog;
import com.example.commonframe.dialog.OptionsDialog;
import com.example.commonframe.util.SingleClick;
import com.example.commonframe.util.SingleTouch;

public abstract class BaseProperties {
	/**
	 * Loading dialog reference, this loading dialog will be applied for the
	 * entire application
	 */
	public static LoadingDialog loadingDialog = null;

	/**
	 * Alert dialog reference, this alert dialog will be applied for the entire
	 * application
	 */
	public static AlertDialog alertDialog = null;

	/**
	 * Decision dialog reference, this decision dialog will be applied for the
	 * entire application
	 */
	public static DecisionDialog questionDialog = null;

	/**
	 * Options dialog reference, this options dialog will be applied for the
	 * entire application
	 */
	public static OptionsDialog optionsDialog = null;

	/**
	 * Single touch reference, this single touch will be applied for components
	 * to ensure only one component touched at the same time
	 */
	public static SingleTouch singleTouch = null;

	/**
	 * Single click references, these single clicks will be applied for components
	 * to ensure only the component only clicked once after a short period
	 */
	
	public static HashMap<String, SingleClick> singleClicks = null;

	/**
	 * The web service requester to make the request to server and return the
	 * result to the context
	 */
	public static WebServiceRequester wsRequester = null;

	/**
	 * The background service requester to make the background request with the
	 * low priority and handle the result in the background thread
	 */
	public static BackgroundServiceRequester bgRequester = null;

}
