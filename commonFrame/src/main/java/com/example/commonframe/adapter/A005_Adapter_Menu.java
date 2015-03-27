package com.example.commonframe.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.commonframe.R;
import com.example.commonframe.model.local.MenuItem;
import com.example.commonframe.util.CentralApplication;

public class A005_Adapter_Menu extends BaseAdapter {

	private ArrayList<MenuItem> data;
	private LayoutInflater inflater;

	public A005_Adapter_Menu(ArrayList<MenuItem> data) {
		this.inflater = (LayoutInflater) CentralApplication.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		DrawerMenuItemViewHolder holder = null;

		if (view == null) {
			view = inflater.inflate(R.layout.a005_item_menu, parent, false);
			holder = new DrawerMenuItemViewHolder();
			holder.drawer_item_menu_tv_title = (TextView) view
					.findViewById(R.id.drawer_item_menu_tv_title);
			view.setTag(holder);
		} else {
			holder = (DrawerMenuItemViewHolder) view.getTag();
		}
		holder.drawer_item_menu_tv_title.setText(data.get(position).getTitle());

		return view;
	}

	private class DrawerMenuItemViewHolder {
		TextView drawer_item_menu_tv_title;
	}

}
