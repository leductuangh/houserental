package com.example.commonframe.model;

import java.util.HashMap;

import com.example.commonframe.model.base.BaseParam;
import com.example.commonframe.util.Constant.RequestFormat;

public class RegisterParam extends BaseParam {

	private enum Param {
		USERNAME {
			@Override
			public String toString() {
				return "username";
			}
		},
		PASSWORD {
			@Override
			public String toString() {
				return "password";
			}
		},
		EMAIL {
			@Override
			public String toString() {
				return "email";
			}
		}
	}

	private String email;
	private String username;
	private String password;

	public RegisterParam(RequestFormat format, String email, String username, String password) {
		super(format);
		this.email = email;
		this.username = username;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public HashMap<String, String> makeRequestParams() {
		HashMap<String, String> requestParams = super.makeRequestParams();
		requestParams.put(Param.EMAIL.toString(), email);
		requestParams.put(Param.USERNAME.toString(), username);
		requestParams.put(Param.PASSWORD.toString(), password);
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
