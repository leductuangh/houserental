package com.example.commonframe.model;

import java.util.HashMap;

import com.example.commonframe.model.base.BaseParam;
import com.example.commonframe.util.Constant.RequestFormat;

public class VersionParam extends BaseParam {
	private enum Param {
		VERSION {
			@Override
			public String toString() {
				return "version";
			}
		}
	}

	private String version;

	public VersionParam(RequestFormat format, String version) {
		super(format);
		this.version = version;
	}

	@Override
	public HashMap<String, String> makeRequestParams() {
		HashMap<String, String> requestParams = super.makeRequestParams();
		requestParams.put(Param.VERSION.toString(), version);
		return requestParams;
	}

	@Override
	public HashMap<String, String> makeRequestHeaders() {
		HashMap<String, String> headers = super.makeRequestHeaders();

		return headers;
	}

	@Override
	public byte[] makeRequestBody() {
		switch (format) {
		case JSON:
			return makeJsonBody();
		case XML:
			return makeXmlBody();
		case STRING:
			return makeStringBody();
		default:
			break;
		}
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
