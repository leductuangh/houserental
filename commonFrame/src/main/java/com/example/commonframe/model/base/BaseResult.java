package com.example.commonframe.model.base;

import java.util.HashMap;
import java.util.Map;

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
	 * This method is for creating a tag map of the setting method and its
	 * value, every derived class must implement this method to auto-parsing
	 * function working correctly <br>
	 * <br>
	 * The value and setting method name must match for the auto-parsing to work
	 * correctly. Every recursive parsing must be handled inside the set method
	 * of each value.
	 * 
	 */
	protected abstract HashMap<String, String> createTagMap();

	/**
	 * The set of key-value for methods and values
	 */
	private Map<String, String> methods;

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
	private String status;

	/**
	 * The string value of the return title
	 */
	private String title;

	/**
	 * The string value of the return message
	 */
	private String message;

	/**
	 * private enum <br>
	 * <b>Param</b> <br>
	 * Represents the parameter tags for parsing purpose
	 */
	public enum Tag {
		METHOD {
			@Override
			public String toString() {
				return "method";
			}
		},

		STATUS {
			@Override
			public String toString() {
				return "status";
			}
		},

		TITLE {
			@Override
			public String toString() {
				return "title";
			}
		},

		MESSAGE {
			@Override
			public String toString() {
				return "message";
			}
		}
	}

	/**
	 * private enum <br>
	 * <b>Param</b> <br>
	 * Represents the setting method tags for parsing purpose
	 */
	private enum SetMethod {
		METHOD {
			@Override
			public String toString() {
				return "setMethod";
			}
		},
		STATUS {
			@Override
			public String toString() {
				return "setStatus";
			}
		},
		TITLE {
			@Override
			public String toString() {
				return "setTitle";
			}
		},
		MESSAGE {
			@Override
			public String toString() {
				return "setMessage";
			}
		},
	}

	public BaseResult() {
		methods = new HashMap<String, String>();
		headers = new HashMap<String, String>();
		methods.put(Tag.METHOD.toString(), SetMethod.METHOD.toString());
		methods.put(Tag.STATUS.toString(), SetMethod.STATUS.toString());
		methods.put(Tag.TITLE.toString(), SetMethod.TITLE.toString());
		methods.put(Tag.MESSAGE.toString(), SetMethod.MESSAGE.toString());
		methods.putAll(createTagMap());
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
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
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

	/**
	 * @return the methods
	 */
	public Map<String, String> getMethods() {
		return methods;
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

	// private void testSetValue(String nodeName, String nodeVal) {
	// BaseResult obj = new BaseResult();
	// HashMap<String, String> map = obj.getMethodMap();
	// if (map.get(nodeName) != null) {
	// try {
	// Method method = BaseResult.class.getMethod(map.get(nodeName),
	// new Class[] { String.class });
	// obj.setValue(obj, method, nodeVal);
	// } catch (NoSuchMethodException e) {
	// e.printStackTrace();
	// }
	// }
	// }

	// public void setValue(BaseResult obj, Method method, String value) {
	// String[] params = new String[1];
	// params[0] = value;
	// try {
	// method.invoke(obj, params);
	// } catch (IllegalAccessException e) {
	// e.printStackTrace();
	// } catch (IllegalArgumentException e) {
	// e.printStackTrace();
	// } catch (InvocationTargetException e) {
	// e.printStackTrace();
	// }
	// }
}
