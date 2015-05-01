package com.example.commonframe.model.base;

import java.util.HashMap;

public interface Param {
	public abstract byte[] makeRequestBody();
	
	public abstract HashMap<String, String> makeRequestParams();
	
	public abstract HashMap<String, String> makeRequestHeaders();
}
