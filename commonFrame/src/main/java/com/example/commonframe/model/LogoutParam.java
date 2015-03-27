package com.example.commonframe.model;

import java.util.HashMap;

import com.example.commonframe.model.base.BaseParam;
import com.example.commonframe.util.Constant.RequestFormat;

public class LogoutParam extends BaseParam {
	
	public LogoutParam(RequestFormat format) {
		super(format);
	}

	@Override
	public HashMap<String, String> makeRequestParams() {
		HashMap<String, String> requestParams = super.makeRequestParams();
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
