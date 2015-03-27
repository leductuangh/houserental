package com.example.commonframe.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.commonframe.R;
import com.example.commonframe.R.color;
import com.example.commonframe.util.CentralApplication;

public class A008_Adapter_Pager extends PagerAdapter {
	private LayoutInflater inflater;
	private ArrayList<String> data;

	public A008_Adapter_Pager(ArrayList<String> data) {
		inflater = CentralApplication.getActiveActivity().getLayoutInflater();
		this.data = data;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);
	}
	
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@SuppressLint("InflateParams")
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = inflater.inflate(R.layout.a008_item_pager, null);
		if (position % 2 == 0)
			view.setBackgroundColor(color.AliceBlue);
		((TextView) view.findViewById(R.id.a008_item_pager_tv_page))
				.setText(data.get(position));
		container.addView(view, 0);
		return view;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}

}
