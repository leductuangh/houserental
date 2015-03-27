package com.example.commonframe.model.local;

import java.util.ArrayList;

public class Category extends BaseModel{
	private String name;
	private ArrayList<CategorySub> subs;

	public Category(int id) {
		super(id);
		subs = new ArrayList<CategorySub>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<CategorySub> getSubs() {
		return subs;
	}

	public void addSub(CategorySub sub) {
		if (sub != null)
			subs.add(sub);
	}

	public CategorySub getSub(int index) {
		return subs.get(index);
	}

}
