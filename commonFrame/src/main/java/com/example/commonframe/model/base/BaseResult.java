package com.example.commonframe.model.base;

import java.util.HashMap;
import java.util.Map;

import com.example.commonframe.util.Constant.StatusCode;
import com.example.commonframe.util.Utils;

/**
 * @author Tyrael
 * @since April 2014
 * @version 1.0 <br>
 * <br>
 *          <b>Class Overview</b> <br>
 * <br>
 *          Represents a class for result from network response after parsing,
 *          including the data as an object or a failure object in general<br>
 */
public abstract class BaseResult {

	/**
	 * The set of key-value for header and values
	 */
	private Map<String, String> headers;

	/**
	 * The string value of the method requested from server
	 */
	private String method;

	/**
	 * The string value of the return status
	 */
	private StatusCode status;

	/**
	 * The string value of the return title
	 */
	private String title;

	/**
	 * The string value of the return message
	 */
	private String message;

	public BaseResult() {
		headers = new HashMap<String, String>();
	}

	/**
	 * @return the headers
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}

	/**
	 * @param headers
	 *            the headers to set
	 */
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method
	 *            the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @return the status
	 */
	public StatusCode getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(StatusCode status) {
		this.status = status;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "[Status] = "
				+ status
				+ " - [Method] = "
				+ method
				+ ((!Utils.isEmpty(title)) ? " - [Title] = " + title : "")
				+ ((!Utils.isEmpty(message)) ? " - [Message] = " + message : "");
	}
}
