package com.example.commonframe.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.commonframe.R;
import com.example.commonframe.model.local.Category;
import com.example.commonframe.view.list.FetchableExpandableListView.AnimatedExpandableListAdapter;

public class A006_Adapter_Category extends AnimatedExpandableListAdapter {

	private ArrayList<Category> data;
	private LayoutInflater inflater;

	public A006_Adapter_Category(LayoutInflater inflater,
			ArrayList<Category> data) {
		this.data = data;
		this.inflater = inflater;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return data.get(groupPosition).getSub(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getRealChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		View view = convertView;
		CategorySubViewHolder holder;
		if (view == null) {
			view = inflater.inflate(R.layout.a006_item_category_sub, null);
			holder = new CategorySubViewHolder();
			holder.a006_item_category_sub_tv_description = (TextView) view
					.findViewById(R.id.a006_item_category_sub_tv_description);
			holder.a006_item_category_sub_tv_name = (TextView) view
					.findViewById(R.id.a006_item_category_sub_tv_name);
			view.setTag(holder);
		} else {
			holder = (CategorySubViewHolder) view.getTag();
		}
		holder.a006_item_category_sub_tv_description.setText(data
				.get(groupPosition).getSub(childPosition).getDescription());
		holder.a006_item_category_sub_tv_name.setText(data.get(groupPosition)
				.getSub(childPosition).getName());
		return view;
	}

	@Override
	public int getRealChildrenCount(int groupPosition) {
		return data.get(groupPosition).getSubs().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return data.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return data.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View view = convertView;
		CategoryViewHolder holder;
		if (view == null) {
			view = inflater.inflate(R.layout.a006_item_category, null);
			holder = new CategoryViewHolder();
			holder.a006_item_category_tv_name = (TextView) view
					.findViewById(R.id.a006_item_category_tv_name);
			view.setTag(holder);
		} else {
			holder = (CategoryViewHolder) view.getTag();
		}
		holder.a006_item_category_tv_name.setText(data.get(groupPosition)
				.getName());
		return view;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	private class CategoryViewHolder {
		TextView a006_item_category_tv_name;
	}

	private class CategorySubViewHolder {
		TextView a006_item_category_sub_tv_name,
				a006_item_category_sub_tv_description;
	}

}
