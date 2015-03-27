package com.example.commonframe.model;

import java.util.HashMap;

import com.example.commonframe.model.base.BaseParam;
import com.example.commonframe.util.Constant.RequestFormat;

public class ForgotParam extends BaseParam {
	private enum Param {
		EMAIL {
			@Override
			public String toString() {
				return "email";
			}
		}
	}

	private String email;

	public ForgotParam(RequestFormat format, String email) {
		super(format);
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public HashMap<String, String> makeRequestParams() {
		HashMap<String, String> requestParams = super.makeRequestParams();
		requestParams.put(Param.EMAIL.toString(), email);
		return super.makeRequestParams();
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
