package com.example.commonframe.model.volley;

import java.util.Map;

import com.example.commonframe.model.base.BaseResponse;
import com.example.commonframe.util.Constant.BackgroundRequestTarget;
import com.example.commonframe.util.Constant.ReturnFormat;

public class BackgroundResponse extends BaseResponse {

	private BackgroundRequestTarget target;
	private ReturnFormat format;

	public BackgroundResponse(byte[] content, Map<String, String> headers,
			BackgroundRequestTarget target, ReturnFormat format) {
		super(content, headers);
		this.target = target;
		this.format = format;
	}

	/**
	 * @return the target
	 */
	public BackgroundRequestTarget getRequestTarget() {
		return target;
	}

	/**
	 * @return the format
	 */
	public ReturnFormat getReturnFormat() {
		return format;
	}
}
