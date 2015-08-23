package com.example.commonframe.core.connection.volley;

import java.util.Map;

import com.example.commonframe.core.connection.model.BaseResponse;
import com.example.commonframe.util.Constant.RequestTarget;

public class BackgroundResponse extends BaseResponse {

	private RequestTarget target;

	public BackgroundResponse(byte[] content, Map<String, String> headers,
			RequestTarget target) {
		super(content, headers);
		this.target = target;
	}

	/**
	 * @return the target
	 */
	public RequestTarget getRequestTarget() {
		return target;
	}
}
