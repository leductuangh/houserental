package com.example.commonframe.model.volley;

import java.util.Map;

import com.example.commonframe.util.Constant.BackgroundRequestTarget;
import com.example.commonframe.util.Constant.ReturnFormat;

public class FileResponse extends BackgroundResponse {

	private String name;
	private String extension;

	public FileResponse(byte[] content, Map<String, String> headers,
			BackgroundRequestTarget target, ReturnFormat format, String name,
			String extension) {
		super(content, headers, target, format);
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
