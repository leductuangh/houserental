package com.example.commonframe.core.connection.model;

import java.util.HashMap;

public interface Param {
	public abstract byte[] makeRequestBody();
	
	public abstract HashMap<String, String> makeRequestHeaders();
}
