package com.example.commonframe.model.volley;

import com.android.volley.VolleyError;
import com.example.commonframe.util.Constant.BackgroundRequestTarget;

public class BackgroundError extends VolleyError {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BackgroundRequestTarget target;

	public BackgroundError(BackgroundRequestTarget target, VolleyError error) {
		super(error);
		this.target = target;
	}

	/**
	 * @return the target
	 */
	public BackgroundRequestTarget getRequestTarget() {
		return target;
	}

}
