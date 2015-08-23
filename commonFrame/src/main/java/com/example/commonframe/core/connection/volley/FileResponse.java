package com.example.commonframe.core.connection.volley;

import java.util.Map;

import com.example.commonframe.util.Constant.RequestTarget;

public class FileResponse extends BackgroundResponse {

	private String name;
	private String extension;

	public FileResponse(byte[] content, Map<String, String> headers,
			RequestTarget target, String name,
			String extension) {
		super(content, headers, target);
		this.name = name;
		this.extension = extension;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the extension
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * @param extension
	 *            the extension to set
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

}
