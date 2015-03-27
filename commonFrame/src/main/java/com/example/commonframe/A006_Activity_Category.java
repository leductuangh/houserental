package com.example.commonframe;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.example.commonframe.adapter.A006_Adapter_Category;
import com.example.commonframe.base.BaseActivity;
import com.example.commonframe.connection.WebServiceRequester.WebServiceResultHandler;
import com.example.commonframe.model.CategoryParam;
import com.example.commonframe.model.base.BaseResult;
import com.example.commonframe.model.local.Category;
import com.example.commonframe.model.local.CategorySub;
import com.example.commonframe.util.Constant.RequestFormat;
import com.example.commonframe.util.Constant.RequestTarget;
import com.example.commonframe.util.Constant.StatusCode;
import com.example.commonframe.view.list.FetchableExpandableListView;
import com.example.commonframe.view.list.FetchableInterface.OnLoadMoreListener;
import com.example.commonframe.view.list.FetchableInterface.OnRefreshListener;

public class A006_Activity_Category extends BaseActivity implements
		WebServiceResultHandler, OnChildClickListener, OnLoadMoreListener,
		OnRefreshListener, OnGroupClickListener {
	private FetchableExpandableListView a006_exlv_category;
	private A006_Adapter_Category a006_adt_category;
	private ArrayList<Category> a006_data;
	private Button header_bt_back;
	private Thread refreshThread;
	private Thread loadMoreThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a006_activity_category);
	}

	@Override
	public void onCreateObject() {
		refreshData();
		a006_adt_category = new A006_Adapter_Category(getLayoutInflater(),
				a006_data);
	}

	@Override
	public void onDeepLinking() {

	}

	@Override
	public void onNotification() {

	}

	@Override
	public void onFreeObject() {

	}

	@Override
	public void onBindView() {
		ViewStub stub = (ViewStub) findViewById(R.id.header_bar);
		stub.inflate();
		header_bt_back = (Button) findViewById(R.id.header_bt_back);
		header_bt_back.setVisibility(View.VISIBLE);
		a006_exlv_category = (FetchableExpandableListView) findViewById(R.id.a006_exlv_category);
		a006_exlv_category.setOnChildClickListener(this);
		a006_exlv_category.setOnGroupClickListener(this);
		a006_exlv_category.setLimit(100);
		a006_exlv_category.setOnLoadMoreListener(this);
		a006_exlv_category.setOnRefreshListener(this);
		a006_exlv_category.setAdapter(a006_adt_category);
	}

	@Override
	public void onInitializeViewData() {
	}

	@Override
	public void onResumeObject() {
		makeRequest(TAG, RequestTarget.CATEGORY, new CategoryParam(
				RequestFormat.DEFAULT), this);
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
			}
		});
	}

	@Override
	public void onResultFail(final BaseResult result) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				showAlertDialog(getActiveActivity(), -1, result.getTitle(),
						result.getMessage(), -1, null);
			}
		});
	}

	@Override
	public void onFail(RequestTarget target, final String error, StatusCode code) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				showAlertDialog(getActiveActivity(), -1, null, error, -1, null);
			}
		});
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		return false;
	}

	@Override
	public void onRefresh() {
		refreshThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {

					Thread.sleep(3000);
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							refreshData();
							a006_exlv_category.onRefreshComplete();
							a006_adt_category.notifyDataSetChanged();
						}
					});
				} catch (InterruptedException e) {
				}

			}
		});
		refreshThread.start();
	}

	@Override
	public boolean shouldOverrideLoadMore() {
		// cancel all load more operations here if return true
		if (loadMoreThread != null && loadMoreThread.isAlive())
			loadMoreThread.interrupt();
		return true;
	}

	@Override
	public void onLoadMore() {
		loadMoreThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(3000);
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							loadMoreData();
							a006_exlv_category.onLoadMoreComplete();
							a006_adt_category.notifyDataSetChanged();
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

	private void loadMoreData() {
		int size = a006_data.size();
		for (int i = size; i < (size + 5); ++i) {
			Category category = new Category(i);
			category.setName("Category " + i);
			if (i % 2 == 0) {
				for (int j = 0; j < 2; ++j) {
					CategorySub sub = new CategorySub();
					sub.setName("Sub " + j);
					category.addSub(sub);
				}
			}
			a006_data.add(category);
		}
	}

	private void refreshData() {
		if (a006_data == null)
			a006_data = new ArrayList<Category>();
		a006_data.clear();

		for (int i = 0; i < 20; ++i) {
			Category category = new Category(i);
			category.setName("Category " + i);
			if (i % 2 == 0) {
				for (int j = 0; j < 2; ++j) {
					CategorySub sub = new CategorySub();
					sub.setName("Sub " + j);
					category.addSub(sub);
				}
			}
			a006_data.add(category);
		}
	}

	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
		// We call collapseGroupWithAnimation(int) and
		// expandGroupWithAnimation(int) to animate group
		// expansion/collapse.
		if (a006_exlv_category.isGroupExpanded(groupPosition)) {
			a006_exlv_category.collapseGroupWithAnimation(groupPosition);
		} else {
			a006_exlv_category.expandGroupWithAnimation(groupPosition);
		}
		return true;
	}

}
