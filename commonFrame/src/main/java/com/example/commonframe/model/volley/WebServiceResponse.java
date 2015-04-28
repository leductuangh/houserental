package com.example.commonframe.model.volley;

import java.util.Map;

import com.example.commonframe.model.base.BaseResponse;

public class WebServiceResponse extends BaseResponse{

	public WebServiceResponse(byte[] content, Map<String, String> headers) {
		super(content, headers);
	}

}
