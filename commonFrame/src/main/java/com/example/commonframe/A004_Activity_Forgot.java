package com.example.commonframe;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;

import com.example.commonframe.base.BaseActivity;
import com.example.commonframe.connection.WebServiceRequester.WebServiceResultHandler;
import com.example.commonframe.model.ForgotParam;
import com.example.commonframe.model.base.BaseResult;
import com.example.commonframe.util.Constant.RequestFormat;
import com.example.commonframe.util.Constant.RequestTarget;
import com.example.commonframe.util.Constant.StatusCode;

@SuppressWarnings("unused")
public class A004_Activity_Forgot extends BaseActivity implements
		WebServiceResultHandler {
	private Button a004_bt_send, header_bt_back;
	private EditText a004_et_email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a004_activity_forgot);
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
		a004_bt_send = (Button) findViewById(R.id.a004_bt_send);
		a004_et_email = (EditText) findViewById(R.id.a004_et_email);

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
		case R.id.a004_bt_send:
			makeRequest(TAG, RequestTarget.FORGOT, new ForgotParam(
					RequestFormat.DEFAULT, ""), this);
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
