package com.example.commonframe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.commonframe.base.BaseActivity;
import com.example.commonframe.connection.WebServiceRequester.WebServiceResultHandler;
import com.example.commonframe.dialog.AlertDialog.AlertDialogListener;
import com.example.commonframe.model.LoginParam;
import com.example.commonframe.model.base.BaseResult;
import com.example.commonframe.service.S001_Service_LocationTracking;
import com.example.commonframe.util.Constant.RequestFormat;
import com.example.commonframe.util.Constant.RequestTarget;
import com.example.commonframe.util.Constant.StatusCode;
import com.example.commonframe.util.Constant;
import com.example.commonframe.util.SoftKeyBoardTracker;
import com.example.commonframe.util.SoftKeyBoardTracker.OnKeyBoardListener;
import com.example.commonframe.view.drawer.Drawer;

@SuppressWarnings("unused")
public class A001_Activity_Login extends BaseActivity implements
		WebServiceResultHandler, OnKeyBoardListener, AlertDialogListener {

	// Test merge
	
	private EditText a001_et_user_name, a001_et_password;
	private Button a001_bt_login, a001_bt_register, a001_bt_forgot;
	private SoftKeyBoardTracker a001_soft_keyboard_tracker;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a001_activity_login);
	}

	@Override
	public void onCreateObject() {
		a001_soft_keyboard_tracker = new SoftKeyBoardTracker(findViewById(
				android.R.id.content).getRootView(), this);
		startService(new Intent(getApplicationContext(),
				S001_Service_LocationTracking.class));
	}

	@Override
	public void onDeepLinking() {

	}

	@Override
	public void onNotification() {

	}

	@Override
	public void onFreeObject() {
		a001_soft_keyboard_tracker.remove();
	}

	@Override
	public void onBindView() {
		a001_et_user_name = (EditText) findViewById(R.id.a001_et_user_name);
		a001_et_password = (EditText) findViewById(R.id.a001_et_password);
		a001_bt_login = (Button) findViewById(R.id.a001_bt_login);
		a001_bt_register = (Button) findViewById(R.id.a001_bt_register);
		a001_bt_forgot = (Button) findViewById(R.id.a001_bt_forgot);
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
		case R.id.a001_bt_login:
			makeRequest(TAG, RequestTarget.LOGIN,
					new LoginParam(RequestFormat.DEFAULT, a001_et_user_name
							.getText().toString(), a001_et_password.getText()
							.toString()), this);
			break;
		case R.id.a001_bt_register:
			Intent register = new Intent(this, A002_Activity_Register.class);
			startActivity(register);
			break;
		case R.id.a001_bt_forgot:
			Intent forgot = new Intent(this, A004_Activity_Forgot.class);
			startActivity(forgot);

		default:
			break;
		}
	}

	@Override
	public void onResultSuccess(BaseResult data) {
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
						result.getMessage(), -1, A001_Activity_Login.this);
			}
		});

	}

	@Override
	public void onFail(RequestTarget target, final String error, StatusCode code) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				showAlertDialog(getActiveActivity(), -1, null, error, -1, A001_Activity_Login.this);
			}
		});
	}

	@Override
	public void onKeyBoardShown() {
	}

	@Override
	public void onKeyBoardHidden() {
	}

	@Override
	public void onAlertConfirmed(int id) {
		if(Constant.DEBUG)
			onResultSuccess(null);
	}

}
