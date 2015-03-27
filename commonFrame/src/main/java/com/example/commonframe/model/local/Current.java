package com.example.commonframe.model.local;

import java.util.ArrayList;

public class Current extends BaseModel{
	
	public static final int TYPE_ITEM = 0;
	public static final int TYPE_SECTION = 1;
	
	private String name;
	private String url;
	private int type;

	private ArrayList<CurrentSub> subs;

	public Current(int id) {
		super(id);
		subs = new ArrayList<CurrentSub>();
		type = TYPE_ITEM;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<CurrentSub> getSubs() {
		return subs;
	}

	public void setSubs(ArrayList<CurrentSub> subs) {
		this.subs = subs;
	}

	public void addSub(CurrentSub sub) {
		if (sub != null)
			subs.add(sub);
	}

	public CurrentSub getSub(int index) {
		return subs.get(index);
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	

}
