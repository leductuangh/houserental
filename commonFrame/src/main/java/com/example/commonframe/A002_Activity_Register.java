package com.example.commonframe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;

import com.example.commonframe.base.BaseActivity;
import com.example.commonframe.connection.WebServiceRequester.WebServiceResultHandler;
import com.example.commonframe.model.RegisterParam;
import com.example.commonframe.model.base.BaseResult;
import com.example.commonframe.util.Constant.RequestFormat;
import com.example.commonframe.util.Constant.RequestTarget;
import com.example.commonframe.util.Constant.StatusCode;

@SuppressWarnings("unused")
public class A002_Activity_Register extends BaseActivity implements
		WebServiceResultHandler {
	private Button a002_bt_register, header_bt_back;

	private EditText a002_et_email, a002_et_username, a002_et_password,
			a002_et_confirm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a002_activity_register);
	}

	@Override
	public void onCreateObject() {

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
		a002_bt_register = (Button) findViewById(R.id.a002_bt_register);
		a002_et_email = (EditText) findViewById(R.id.a002_et_email);
		a002_et_username = (EditText) findViewById(R.id.a002_et_username);
		a002_et_password = (EditText) findViewById(R.id.a002_et_password);
		a002_et_confirm = (EditText) findViewById(R.id.a002_et_confirm);

		header_bt_back.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void onInitializeViewData() {	
	}

	@Override
	public void onResumeObject() {
	}

	@Override
	public void onSingleClick(View v) {
		switch (v.getId()) {
		case R.id.a002_bt_register:
			showLoadingDialog(this);
			a002_et_confirm.getText().toString();
			makeRequest(TAG, RequestTarget.REGISTER, new RegisterParam(
					RequestFormat.DEFAULT, "", "", ""), this);
			break;
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
				finish();
				Intent home = new Intent(getActiveActivity(),
						A003_Activity_Home.class);
				home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(home);
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

}
