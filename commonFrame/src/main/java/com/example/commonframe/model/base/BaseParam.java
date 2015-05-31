package com.example.commonframe.model.base;

import java.util.HashMap;

/**
 * @author Tyrael
 * @since April 2014
 * @version 1.0 <br>
 * <br>
 *          <b>Class Overview</b> <br>
 * <br>
 *          Represents an abstract class for forming common parameters content
 *          and headers of a webserivce message, every webservice must use a
 *          derived class from this abstract and override the makeRequestParams
 *          and makeRequestHeaders <br>
 */
public abstract class BaseParam implements Param {

	public abstract byte[] makeRequestBody();

	/**
	 * The set of key-value headers for the webservice message
	 */
	private HashMap<String, String> headers;

	public BaseParam() {
		this.headers = new HashMap<String, String>();
	}

	/**
	 * This method forms a set of key-value headers applied for all the
	 * webservice request, derived class should override this method to append
	 * needed values
	 * 
	 * @return a set of key-value parameters for the request
	 */
	public HashMap<String, String> makeRequestHeaders() {
		return headers;
	}
}
