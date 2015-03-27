package com.example.commonframe.model;

import com.example.commonframe.model.base.BaseParam;
import com.example.commonframe.util.Constant.RequestFormat;

public class FileParam extends BaseParam {
	private String url;
	private String name;
	private String extension;

	public FileParam(RequestFormat format, String url, String name,
			String extension) {
		super(format);
		this.url = url;
		this.name = name;
		this.extension = extension;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
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

	@Override
	public byte[] makeRequestBody() {
		return null;
	}

	@Override
	protected byte[] makeJsonBody() {
		return null;
	}

	@Override
	protected byte[] makeXmlBody() {
		return null;
	}

	@Override
	protected byte[] makeStringBody() {
		return null;
	}

}
