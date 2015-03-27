package com.example.commonframe.gcm;

import org.json.JSONObject;

public class GcmMessage {
	private String message;
	private String title;

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

	public static GcmMessage parse(String data) {

		GcmMessage gcm = new GcmMessage();
		JSONObject json = null;
		try {
			json = new JSONObject(data);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		try {
			gcm.message = json.getString("msg");
		} catch (Exception e) {
		}

		try {
			gcm.title = json.getString("title");
		} catch (Exception e) {
		}

		return gcm;
	}

}
