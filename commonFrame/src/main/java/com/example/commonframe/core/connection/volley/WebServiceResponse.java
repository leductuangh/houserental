package com.example.commonframe.core.connection.volley;

import java.util.Map;

import com.example.commonframe.core.base.BaseResponse;

public class WebServiceResponse extends BaseResponse{

	public WebServiceResponse(byte[] content, Map<String, String> headers) {
		super(content, headers);
	}

}
