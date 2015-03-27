package com.example.commonframe.model.base;

import java.util.HashMap;

import com.example.commonframe.util.Constant.RequestFormat;
import com.example.commonframe.util.Utils;

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
public abstract class BaseParam {
	
	/**
	 * private enum <br>
	 * <b>Param</b> <br>
	 * Represents the parameter keys for <code>BaseParam</code>
	 */
	private enum Param {
		UID {
			@Override
			public String toString() {
				return "uid";
			}
		},
	}
	
	public abstract byte[] makeRequestBody();
	
	protected abstract byte[] makeJsonBody();
	
	protected abstract byte[] makeXmlBody();
	
	protected abstract byte[] makeStringBody();
	
	/**
	 * The format of the request body required
	 */
	protected RequestFormat format;

	/**
	 * The string value of key UID
	 */
	private String UID;

	/**
	 * The set of key-value parameters for the webservice message
	 */
	private HashMap<String, String> params;

	/**
	 * The set of key-value headers for the webservice message
	 */
	private HashMap<String, String> headers;

	public BaseParam(RequestFormat format) {
		this.params = new HashMap<String, String>();
		this.headers = new HashMap<String, String>();
		this.format = format;
		this.UID = Utils.getUID();
	}

	/**
	 * This method forms a set of key-value parameters applied for all the
	 * webservice request, derived class should override this method to append
	 * needed values
	 * 
	 * @return a set of key-value parameters for the request
	 */
	public HashMap<String, String> makeRequestParams() {
		params.put(Param.UID.toString(), UID);
		return params;
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
