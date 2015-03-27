package com.example.commonframe.parser;

import com.example.commonframe.model.base.BaseResult;
import com.example.commonframe.util.Constant.RequestTarget;
import com.example.commonframe.util.Constant.ReturnFormat;

/**
 * @author Tyrael
 * @since April 2014
 * @version 1.0 <br>
 * <br>
 *          <b>Class Overview</b> <br>
 * <br>
 *          - Represents an abstract class for performing parsing the content of
 *          a response. The parsing action based on the return format and the
 *          request target that requested<br>
 *          - Every request must have its own parser and extended from this base
 *          parser<br>
 *          - Every derived parser must implement 3 methods of parsing for XML,
 *          JSON and STRING
 */
public abstract class BaseParser {
	/**
	 * This method is required for the derived class to perform the XML parsing
	 * of the content <br>
	 * 
	 * The derived class must implemented this method and return
	 * <code>null</code> if there is any error occurs
	 */
	protected abstract BaseResult parseXML(String content);

	/**
	 * This method is required for the derived class to perform the JSON parsing
	 * of the content <br>
	 * 
	 * The derived class must implemented this method and return
	 * <code>null</code> if there is any error occurs
	 */
	protected abstract BaseResult parseJSON(String content);

	/**
	 * This method is required for the derived class to perform the STRING
	 * parsing of the content <br>
	 * 
	 * The derived class must implemented this method and return
	 * <code>null</code> if there is any error occurs
	 */
	protected abstract BaseResult parseSTRING(String content);

	/**
	 * This method perform the switching to the correct XML parser based on the
	 * request target
	 */
	private static BaseResult performXMLParsing(String content,
			RequestTarget target) {
		BaseResult data = null;
		switch (target) {
		case CATEGORY:
			data = new PrsCategory().parseXML(content);
			break;
		case CURRENT:
			data = new PrsCurrent().parseXML(content);
			break;
		case FORGOT:
			data = new PrsForgot().parseXML(content);
			break;
		case LOGIN:
			data = new PrsLogin().parseXML(content);
			break;
		case LOGOUT:
			data = new PrsLogout().parseXML(content);
			break;
		case REGISTER:
			data = new PrsRegister().parseXML(content);
			break;
		case VERSION:
			data = new PrsVersion().parseXML(content);
			break;
		default:
			break;
		}

		return data;
	}

	/**
	 * This method perform the switching to the correct JSON parser based on the
	 * request target
	 */
	private static BaseResult performJSONParsing(String content,
			RequestTarget target) {
		BaseResult data = null;
		switch (target) {
		case CATEGORY:
			data = new PrsCategory().parseJSON(content);
			break;
		case CURRENT:
			data = new PrsCurrent().parseJSON(content);
			break;
		case FORGOT:
			data = new PrsForgot().parseJSON(content);
			break;
		case LOGIN:
			data = new PrsLogin().parseJSON(content);
			break;
		case LOGOUT:
			data = new PrsLogout().parseJSON(content);
			break;
		case REGISTER:
			data = new PrsRegister().parseJSON(content);
			break;
		case VERSION:
			data = new PrsVersion().parseJSON(content);
			break;
		default:
			break;
		}
		return data;
	}

	/**
	 * This method perform the switching to the correct STRING parser based on
	 * the request target
	 */
	private static BaseResult performSTRINGParsing(String content,
			RequestTarget target) {
		BaseResult data = null;
		switch (target) {
		case CATEGORY:
			data = new PrsCategory().parseSTRING(content);
			break;
		case CURRENT:
			data = new PrsCurrent().parseSTRING(content);
			break;
		case FORGOT:
			data = new PrsForgot().parseSTRING(content);
			break;
		case LOGIN:
			data = new PrsLogin().parseSTRING(content);
			break;
		case LOGOUT:
			data = new PrsLogout().parseSTRING(content);
			break;
		case REGISTER:
			data = new PrsRegister().parseSTRING(content);
			break;
		case VERSION:
			data = new PrsVersion().parseSTRING(content);
			break;
		default:
			break;
		}
		return data;
	}

	/**
	 * This method perform parsing the response from the webservice based on its
	 * return format and request target
	 * 
	 * @param content
	 *            The content to parse
	 * @param format
	 *            The return format of the content either XML, JSON or STRING
	 * @param target
	 *            The target function of the webserivce
	 * @return
	 */
	public static BaseResult parse(String content, ReturnFormat format,
			RequestTarget target) {
		BaseResult data = null;
		switch (format) {
		case XML:
			data = performXMLParsing(content, target);
			break;
		case JSON:
			data = performJSONParsing(content, target);
			break;
		case STRING:
			data = performSTRINGParsing(content, target);
			break;
		default:
			break;
		}
		return data;
	}
}
