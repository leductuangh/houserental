package com.example.commonframe.model.local;

import java.io.Serializable;

import android.os.Parcelable;

public abstract class BaseModel implements Serializable, Parcelable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7446507223452892349L;
	/**
	 * 
	 */
	private int id;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
}
