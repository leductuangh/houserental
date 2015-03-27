package com.example.commonframe;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.commonframe.adapter.A007_Adapter_Current;
import com.example.commonframe.base.BaseActivity;
import com.example.commonframe.connection.WebServiceRequester.WebServiceResultHandler;
import com.example.commonframe.dialog.AlertDialog.AlertDialogListener;
import com.example.commonframe.model.CurrentParam;
import com.example.commonframe.model.base.BaseResult;
import com.example.commonframe.model.local.Current;
import com.example.commonframe.util.Constant;
import com.example.commonframe.util.Constant.RequestFormat;
import com.example.commonframe.util.Constant.RequestTarget;
import com.example.commonframe.util.Constant.StatusCode;
import com.example.commonframe.view.list.FetchableInterface.OnLoadMoreListener;
import com.example.commonframe.view.list.FetchableInterface.OnRefreshListener;
import com.example.commonframe.view.list.FetchableListView;
import com.example.commonframe.view.list.OnPositionChangedListener;

public class A007_Activity_Current extends BaseActivity implements
		WebServiceResultHandler, OnRefreshListener, OnLoadMoreListener,
		OnItemClickListener, AlertDialogListener {
	private Button header_bt_back;
	private A007_Adapter_Current a007_adt_current;
	private FetchableListView a007_lv_current;
	private ArrayList<Current> a007_data_current;
	private Thread refreshThread;
	private Thread loadMoreThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a007_activity_current);
	}

	@Override
	public void onCreateObject() {
		a007_data_current = new ArrayList<Current>();
		a007_adt_current = new A007_Adapter_Current(getLayoutInflater(),
				a007_data_current);
	}

	@Override
	public void onDeepLinking() {

	}

	@Override
	public void onNotification() {

	}

	@Override
	public void onFreeObject() {
		a007_lv_current.onRefreshComplete();
		a007_lv_current.onLoadMoreComplete();
		a007_data_current.clear();
	}

	@Override
	public void onBindView() {
		ViewStub stub = (ViewStub) findViewById(R.id.header_bar);
		stub.inflate();
		a007_lv_current = (FetchableListView) findViewById(R.id.a007_lv_current);
		header_bt_back = (Button) findViewById(R.id.header_bt_back);
		header_bt_back.setVisibility(View.VISIBLE);
		a007_lv_current.setLimit(700);
		a007_lv_current.setOnRefreshListener(this);
		a007_lv_current.setOnLoadMoreListener(this);
		a007_lv_current.setOnItemClickListener(this);
		a007_lv_current.setAdapter(a007_adt_current);
		a007_lv_current
				.setOnPositionChangedListener(new OnPositionChangedListener() {

					@Override
					public void onPositionChanged(ListView listView,
							int position, View scrollBarPanel) {

						((TextView) scrollBarPanel
								.findViewById(R.id.scroll_bar_indicator_tv_position))
								.setText("POSITION " + position);
					}
				});
	}

	@Override
	public void onInitializeViewData() {
		makeRequest(TAG, RequestTarget.CURRENT, new CurrentParam(
				RequestFormat.DEFAULT), this);
	}

	@Override
	public void onResumeObject() {

	}

	@Override
	public void onSingleClick(View v) {
		switch (v.getId()) {
		case R.id.header_bt_back:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onResultSuccess(BaseResult result) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				refreshData();

			}
		});
	}

	@Override
	public void onResultFail(final BaseResult result) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				showAlertDialog(getActiveActivity(), -1, result.getTitle(),
						result.getMessage(), -1, A007_Activity_Current.this);
			}
		});
	}

	@Override
	public void onFail(RequestTarget target, final String error, StatusCode code) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				showAlertDialog(getActiveActivity(), -1, null, error, -1,
						A007_Activity_Current.this);
			}
		});
	}

	@Override
	public void onLoadMore() {
		loadMoreThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(5000);
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							loadMoreData();
						}
					});
				} catch (InterruptedException e) {
				}

			}
		});
		loadMoreThread.start();
	}

	@Override
	public boolean shouldOverrideRefresh() {
		// cancel all refresh operations here if return true
		if (refreshThread != null && refreshThread.isAlive())
			refreshThread.interrupt();

		return true;
	}

	@Override
	public void onRefresh() {
		refreshThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {

					Thread.sleep(5000);
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							refreshData();
						}
					});
				} catch (InterruptedException e) {
				}

			}
		});
		refreshThread.start();
	}

	private void loadMoreData() {
		int size = a007_data_current.size();
		for (int i = size; i < (size + 50); ++i) {
			Current c = new Current(i);
			c.setName("Current #" + i);
			c.setUrl("http://lorempixel.com/200/200/sports/1/" + (200000 + i)
					+ "/");
			if (i % 5 == 0)
				c.setType(Current.TYPE_SECTION);
			a007_data_current.add(c);
		}
		a007_lv_current.onLoadMoreComplete();
		a007_adt_current.notifyDataSetChanged();
	}

	private void refreshData() {
		a007_data_current.clear();
		for (int i = 0; i < 50; ++i) {
			Current c = new Current(i);
			c.setName("Current #" + i);
			c.setUrl("http://lorempixel.com/200/200/sports/1/" + (200000 + i)
					+ "/");
			if (i % 5 == 0)
				c.setType(Current.TYPE_SECTION);
			a007_data_current.add(c);
		}
		a007_adt_current.notifyDataSetChanged();
		a007_lv_current.onRefreshComplete();
	}

	@Override
	public boolean shouldOverrideLoadMore() {
		// cancel all load more operations here if return true
		if (loadMoreThread != null && loadMoreThread.isAlive())
			loadMoreThread.interrupt();
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Toast.makeText(this, "click " + arg2, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onAlertConfirmed(int id) {
		if (Constant.DEBUG)
			onResultSuccess(null);
	}

}
