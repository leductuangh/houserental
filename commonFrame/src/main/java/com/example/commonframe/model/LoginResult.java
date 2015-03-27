package com.example.commonframe.model;

import java.util.HashMap;

import com.example.commonframe.model.base.BaseResult;

public class LoginResult extends BaseResult {

	public enum Tag {
		NAME {
			@Override
			public String toString() {
				return "name";
			}
		},

		DOB {
			@Override
			public String toString() {
				return "dob";
			}
		}
	}

	private enum SetMethod {
		NAME {
			@Override
			public String toString() {
				return "setName";
			}
		},
		DOB {
			@Override
			public String toString() {
				return "setDob";
			}
		},
	}

	private String name;
	private String dob;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the dob
	 */
	public String getDob() {
		return dob;
	}

	/**
	 * @param dob
	 *            the dob to set
	 */
	public void setDob(String dob) {
		this.dob = dob;
	}

	@Override
	protected HashMap<String, String> createTagMap() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Tag.NAME.toString(), SetMethod.NAME.toString());
		map.put(Tag.DOB.toString(), SetMethod.DOB.toString());
		return map;
	}

	@Override
	public String toString() {
		return super.toString() + " - [Name] = " + name + " - [DOB] = " + dob;
	}
}
