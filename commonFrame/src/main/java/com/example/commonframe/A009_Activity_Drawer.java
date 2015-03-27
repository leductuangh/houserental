package com.example.commonframe;

import android.os.Bundle;
import android.view.View;

import com.example.commonframe.base.BaseActivity;
import com.example.commonframe.view.drawer.Drawer;

public class A009_Activity_Drawer extends BaseActivity{

	private Drawer a009_drawer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a009_activity_drawer);
	}
	
	@Override
	public void onCreateObject() {
		a009_drawer = new Drawer(this);
	}

	@Override
	public void onDeepLinking() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNotification() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBindView() {
		a009_drawer.setLeftBehindContentView(R.layout.a001_activity_login);
		findViewById(R.id.a009_ll_root);
	}

	@Override
	public void onInitializeViewData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResumeObject() {
		getSingleClick().setListener(this);
	}

	@Override
	public void onFreeObject() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSingleClick(View v) {
		a009_drawer.toggleLeftDrawer();
	}

}
