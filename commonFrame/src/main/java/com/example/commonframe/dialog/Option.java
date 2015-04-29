package com.example.commonframe.dialog;

/**
 * @author Tyrael
 * @since April 2014
 * @version 1.0 <br>
 * <br>
 *          <b>Class Overview</b> <br>
 * <br>
 *          Represents a class for an option used in <code>OptionDialog</code>,
 *          including the name and the icon id <br>
 */
public class Option {
	private String name;
	private int icon_id;

	public Option(String name, int icon_id) {
		this.name = name;
		this.icon_id = icon_id;
	}

	public Option(String name) {
		this.name = name;
		icon_id = -1;
	}

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
	 * @return the icon_id
	 */
	public int getIcon_id() {
		return icon_id;
	}

	/**
	 * @param icon_id
	 *            the icon_id to set
	 */
	public void setIcon_id(int icon_id) {
		this.icon_id = icon_id;
	}

}
