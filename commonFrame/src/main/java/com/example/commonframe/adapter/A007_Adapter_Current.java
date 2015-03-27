package com.example.commonframe.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.commonframe.R;
import com.example.commonframe.R.color;
import com.example.commonframe.model.local.Current;
import com.example.commonframe.view.list.PinableSectionInterface;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

@SuppressLint("NewApi")
public class A007_Adapter_Current extends BaseAdapter implements
		PinableSectionInterface {

	private ArrayList<Current> data;
	private LayoutInflater inflater;

	public A007_Adapter_Current(LayoutInflater inflater, ArrayList<Current> data) {
		this.inflater = inflater;
		this.data = data;
	}

	@Override
	public int getCount() {
		if (data != null)
			return data.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;

		if (view == null) {
			view = inflater.inflate(R.layout.a007_item_current, null);
			CurrentViewHolder h = new CurrentViewHolder();
			h.a007_item_current_tv_name = (TextView) view
					.findViewById(R.id.a007_item_current_tv_name);
			h.a007_item_current_img_icon = (ImageView) view
					.findViewById(R.id.a007_item_current_img_icon);
			h.a007_item_current_pb_loading = (ProgressBar) view
					.findViewById(R.id.a007_item_current_pb_loading);
			view.setTag(h);
		}
		final CurrentViewHolder holder = (CurrentViewHolder) view.getTag();
		holder.a007_item_current_tv_name.setText(data.get(position).getName());
		ImageLoader.getInstance().displayImage(data.get(position).getUrl(),
				holder.a007_item_current_img_icon, new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						holder.a007_item_current_pb_loading
								.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(String arg0, View arg1,
							FailReason arg2) {
						holder.a007_item_current_pb_loading
								.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingComplete(String arg0, View arg1,
							Bitmap arg2) {
						holder.a007_item_current_pb_loading
								.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						holder.a007_item_current_pb_loading
								.setVisibility(View.GONE);
					}
				});
		if (data.get(position).getType() == Current.TYPE_SECTION) {
			view.setBackgroundColor(parent.getResources().getColor(color.Aqua));
		}
		return view;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		return data.get(position).getType();
	}

	private class CurrentViewHolder {
		TextView a007_item_current_tv_name;
		ImageView a007_item_current_img_icon;
		ProgressBar a007_item_current_pb_loading;
	}

	@Override
	public boolean isItemViewTypePinned(int viewType) {
		return viewType == Current.TYPE_SECTION;
	}

}
