package com.example.commonframe.parser;

import com.example.commonframe.model.base.BaseResult;
import com.example.commonframe.util.Constant.RequestTarget;

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
	 * This method is required for the derived class to perform the data parsing
	 * of the content <br>
	 * 
	 * The derived class must implemented this method and return
	 * <code>null</code> if there is any error occurs
	 */
	protected abstract BaseResult parseData(String content);

	/**
	 * This method perform parsing data
	 */
	private static BaseResult performParsing(String content,
			RequestTarget target) {
		BaseResult data = null;
		switch (target) {

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
	 * @param target
	 *            The target function of the webserivce
	 * @return
	 */
	public static BaseResult parse(String content, RequestTarget target) {
		return performParsing(content, target);
	}
}
