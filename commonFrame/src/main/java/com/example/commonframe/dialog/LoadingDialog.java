package com.example.commonframe.dialog;

import android.content.Context;
import android.os.Bundle;

import com.example.commonframe.R;

public class LoadingDialog extends BaseDialog {

	public LoadingDialog(Context context) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_dialog);
	}

	@Override
	protected void onCreateObject() {

	}

	@Override
	protected void onBindView() {
		
	}

}
