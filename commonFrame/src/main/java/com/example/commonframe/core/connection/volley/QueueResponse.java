package com.example.commonframe.core.connection.volley;

import java.util.Map;

import com.example.commonframe.core.connection.model.BaseResponse;
import com.example.commonframe.util.Constant.RequestTarget;

public class QueueResponse extends BaseResponse {

	private RequestTarget target;

	public QueueResponse(byte[] content, Map<String, String> headers,
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
