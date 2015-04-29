package com.example.commonframe.model.volley;

import com.android.volley.VolleyError;
import com.example.commonframe.util.Constant.RequestTarget;

public class BackgroundError extends VolleyError {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RequestTarget target;

	public BackgroundError(RequestTarget target, VolleyError error) {
		super(error);
		this.target = target;
	}

	/**
	 * @return the target
	 */
	public RequestTarget getRequestTarget() {
		return target;
	}

}
