package com.example.commonframe;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.commonframe.adapter.A008_Adapter_Pager;
import com.example.commonframe.base.BaseActivity;
import com.example.commonframe.util.CentralApplication;
import com.example.commonframe.view.pager.InfinitePagerAdapter;
import com.example.commonframe.view.pager.InfiniteViewPager;
import com.example.commonframe.view.pager.transform.AccordionTransformer;
import com.example.commonframe.view.pager.transform.BackgroundToForegroundTransformer;
import com.example.commonframe.view.pager.transform.CubeInTransformer;
import com.example.commonframe.view.pager.transform.CubeOutTransformer;
import com.example.commonframe.view.pager.transform.DefaultTransformer;
import com.example.commonframe.view.pager.transform.DepthPageTransformer;
import com.example.commonframe.view.pager.transform.FlipHorizontalTransformer;
import com.example.commonframe.view.pager.transform.FlipVerticalTransformer;
import com.example.commonframe.view.pager.transform.ForegroundToBackgroundTransformer;
import com.example.commonframe.view.pager.transform.RotateDownTransformer;
import com.example.commonframe.view.pager.transform.RotateUpTransformer;
import com.example.commonframe.view.pager.transform.StackTransformer;
import com.example.commonframe.view.pager.transform.TabletTransformer;
import com.example.commonframe.view.pager.transform.ZoomInTransformer;
import com.example.commonframe.view.pager.transform.ZoomOutSlideTransformer;
import com.example.commonframe.view.pager.transform.ZoomOutTranformer;

public class A008_Activity_Pager extends BaseActivity {
	private InfiniteViewPager a008_vp;
	private InfinitePagerAdapter adapter;
	private ArrayList<TransformerItem> data;
	private Spinner a008_sp_transform;
	private TextView a008_tv_transform_title;
	private ArrayList<String> content_data;

	private static final class TransformerItem {

		final String title;
		final Class<? extends PageTransformer> clazz;

		public TransformerItem(Class<? extends PageTransformer> clazz) {
			this.clazz = clazz;
			title = clazz.getSimpleName();
		}

		@Override
		public String toString() {
			return title;
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a008_activity_pager);
	}

	@Override
	public void onCreateObject() {
		content_data = new ArrayList<String>();
		for (int i = 0; i < 10; ++i)
			content_data.add("Content data " + i);
		data = new ArrayList<TransformerItem>();
		data.add(new TransformerItem(DefaultTransformer.class));
		data.add(new TransformerItem(AccordionTransformer.class));
		data.add(new TransformerItem(BackgroundToForegroundTransformer.class));
		data.add(new TransformerItem(CubeInTransformer.class));
		data.add(new TransformerItem(CubeOutTransformer.class));
		data.add(new TransformerItem(DepthPageTransformer.class));
		data.add(new TransformerItem(FlipHorizontalTransformer.class));
		data.add(new TransformerItem(FlipVerticalTransformer.class));
		data.add(new TransformerItem(ForegroundToBackgroundTransformer.class));
		data.add(new TransformerItem(RotateDownTransformer.class));
		data.add(new TransformerItem(RotateUpTransformer.class));
		data.add(new TransformerItem(StackTransformer.class));
		data.add(new TransformerItem(TabletTransformer.class));
		data.add(new TransformerItem(ZoomInTransformer.class));
		data.add(new TransformerItem(ZoomOutSlideTransformer.class));
		data.add(new TransformerItem(ZoomOutTranformer.class));
		adapter = new InfinitePagerAdapter(new A008_Adapter_Pager(content_data));
	}

	@Override
	public void onDeepLinking() {

	}

	@Override
	public void onNotification() {

	}

	@Override
	public void onBindView() {
		a008_tv_transform_title = (TextView) findViewById(R.id.a008_tv_transform_title);
		a008_sp_transform = (Spinner) findViewById(R.id.a008_sp_transform);
		a008_sp_transform.setAdapter(new ArrayAdapter<TransformerItem>(
				CentralApplication.getActiveActivity(),
				android.R.layout.simple_list_item_1, data));
		a008_sp_transform
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						try {
							a008_vp.setPageTransformer(true,
									data.get(position).clazz.newInstance());
							a008_tv_transform_title.setText(data.get(position).title);
							content_data.clear();
							for (int i = 0; i < 10; ++i)
								content_data.add(data.get(position).title + " "
										+ "Content data " + i);
							adapter.notifyDataSetChanged();
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						a008_vp.setCurrentItem(0);
					}
				});

		a008_vp = (InfiniteViewPager) findViewById(R.id.a008_vp);
		a008_vp.setAdapter(adapter);
	}

	@Override
	public void onInitializeViewData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResumeObject() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFreeObject() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSingleClick(View v) {
		// TODO Auto-generated method stub

	}

}
