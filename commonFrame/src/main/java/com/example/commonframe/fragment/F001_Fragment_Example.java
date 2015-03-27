package com.example.commonframe.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.commonframe.R;
import com.example.commonframe.base.BaseFragment;
import com.example.commonframe.util.CentralApplication;
import com.example.commonframe.util.DLog;
import com.example.commonframe.view.spinner.AbstractWheel;
import com.example.commonframe.view.spinner.AbstractWheel.OnWheelListener;
import com.example.commonframe.view.spinner.WheelHorizontalView;
import com.example.commonframe.view.spinner.WheelVerticalView;
import com.example.commonframe.view.spinner.adapter.AbstractWheelAdapter;

@SuppressLint("InflateParams")
public class F001_Fragment_Example extends BaseFragment {
	public static final String TAG = F001_Fragment_Example.class.getName();
	private WheelVerticalView f001_vertical_wheel;
	private WheelHorizontalView f001_horizontal_wheel;
	private WheelAdapter f001_wheel_adapter;
	private ArrayList<String> f001_wheel_data;
	private TextView f001_tv_vertical, f001_tv_horizontal;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.f001_fragment_example, null);
	}

	@Override
	public void onCreateObject() {
		initWheelsData();

	}

	private void initWheelsData() {
		f001_wheel_data = new ArrayList<String>();

		for (int i = 0; i < 20; ++i) {
			f001_wheel_data.add("Data " + i);
		}
		f001_wheel_adapter = new WheelAdapter(f001_wheel_data);
	}

	@Override
	public void onDeepLinking() {
	}

	@Override
	public void onNotification() {
	}

	@Override
	public void onBindView() {
		f001_horizontal_wheel = (WheelHorizontalView) findViewById(R.id.f001_horizontal_wheel);
		f001_horizontal_wheel.setViewAdapter(f001_wheel_adapter);
		f001_horizontal_wheel.setCyclic(false);
		
		f001_horizontal_wheel.setOnWheelListener(new OnWheelListener() {
			
			@Override
			public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
				f001_tv_horizontal.setText((CharSequence) f001_wheel_adapter.getItem(newValue));
			}

			@Override
			public void onItemClicked(AbstractWheel wheel, int itemIndex) {
				Toast.makeText(CentralApplication.getActiveActivity(), "Clicked on HORIZONTAL " + f001_wheel_adapter.getItem(itemIndex), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onScrollingStarted(AbstractWheel wheel) {
				
			}

			@Override
			public void onScrollingFinished(AbstractWheel wheel) {
				
			}
		});
		
		f001_vertical_wheel = (WheelVerticalView) findViewById(R.id.f001_vertical_wheel);
		f001_vertical_wheel.setViewAdapter(f001_wheel_adapter);
		f001_vertical_wheel.setCyclic(true);
		
		f001_vertical_wheel.setOnWheelListener(new OnWheelListener() {
			
			@Override
			public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
				f001_tv_vertical.setText((String) f001_wheel_adapter.getItem(newValue));
			}

			@Override
			public void onItemClicked(AbstractWheel wheel, int itemIndex) {
				Toast.makeText(CentralApplication.getActiveActivity(), "Clicked on VERTICAL " + f001_wheel_adapter.getItem(itemIndex), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onScrollingStarted(AbstractWheel wheel) {
				
			}

			@Override
			public void onScrollingFinished(AbstractWheel wheel) {
				
			}
		});
		f001_tv_vertical = (TextView) findViewById(R.id.f001_tv_vertical);
		f001_tv_vertical.setText((String) f001_wheel_adapter.getItem(f001_vertical_wheel.getCurrentItemIndex()));
		
		f001_tv_horizontal = (TextView) findViewById(R.id.f001_tv_horizontal);
		f001_tv_horizontal.setText((String) f001_wheel_adapter.getItem(f001_horizontal_wheel.getCurrentItemIndex()));
	}

	@Override
	public void onInitializeViewData() {
	}

	@Override
	public void onResumeObject() {
	}

	@Override
	public void onFreeObject() {
	}

	@Override
	public void onSingleClick(View v) {
		DLog.d(TAG, "View clicked!");
	}

	public void onEvent(Object event) {

	}

	private class WheelAdapter extends AbstractWheelAdapter {

		private ArrayList<String> data;

		public WheelAdapter(ArrayList<String> data) {
			this.data = data;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public View getView(int index, View convertView, ViewGroup parent) {
			View view = convertView;
			Holder holder;
			if (view == null) {
				holder = new Holder();
				view = CentralApplication.getActiveActivity()
						.getLayoutInflater()
						.inflate(R.layout.a005_item_menu, null);
				holder.textView = (TextView) view
						.findViewById(R.id.drawer_item_menu_tv_title);
				view.setTag(holder);
			}
			holder = (Holder) view.getTag();
			holder.textView.setText(data.get(index));
			return view;
		}

		@Override
		public View getEmptyView(View convertView, ViewGroup parent) {
			return null;
		}

		@Override
		public Object getItem(int index) {
			return data.get(index);
		}

		private class Holder {
			TextView textView;
		}
	}
}
