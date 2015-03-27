package com.example.commonframe;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;

import com.example.commonframe.base.BaseActivity;
import com.example.commonframe.connection.WebServiceRequester.WebServiceResultHandler;
import com.example.commonframe.dialog.DecisionDialog.DecisionDialogListener;
import com.example.commonframe.dialog.Option;
import com.example.commonframe.dialog.OptionsDialog.OptionsDialogListener;
import com.example.commonframe.model.LogoutParam;
import com.example.commonframe.model.LogoutResult;
import com.example.commonframe.model.base.BaseResult;
import com.example.commonframe.util.Constant.RequestFormat;
import com.example.commonframe.util.Constant.RequestTarget;
import com.example.commonframe.util.Constant.StatusCode;

@SuppressLint("InlinedApi")
@SuppressWarnings("unused")
public class A003_Activity_Home extends BaseActivity implements
		WebServiceResultHandler, DecisionDialogListener, OptionsDialogListener {

	private Button a003_bt_category, a003_bt_current, a003_bt_howto,
			a003_bt_out, a003_bt_share, a003_bt_pager, a003_bt_drawer;
	private static final int LOGOUT_DIALOG_ID = 1;
	private static final int SHARE_DIALOG_ID = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a003_activity_home);
	}

	@Override
	public void onCreateObject() {

	}

	@Override
	public void onDeepLinking() {

	}

	@Override
	public void onNotification() {
		if (getIntent() != null
				&& getIntent().getStringExtra("Notification") != null) {
			String message = "ID " + getIntent().getStringExtra("Notification");

			if (getIntent().getStringExtra("Action") != null) {
				message += " with action "
						+ getIntent().getStringExtra("Action");
				NotificationManager manager = (NotificationManager) getCentralContext()
						.getSystemService(Context.NOTIFICATION_SERVICE);
				if (manager != null) {
					manager.cancel(Integer.parseInt(getIntent().getStringExtra(
							"Notification")));
				}
			}
			showAlertDialog(this, 0, "Notification", message, -1, null);
		}
	}

	@Override
	public void onFreeObject() {

	}

	@Override
	public void onBindView() {
		ViewStub stub = (ViewStub) findViewById(R.id.header_bar);
		stub.inflate();

		a003_bt_category = (Button) findViewById(R.id.a003_bt_category);
		a003_bt_current = (Button) findViewById(R.id.a003_bt_current);
		a003_bt_howto = (Button) findViewById(R.id.a003_bt_howto);
		a003_bt_out = (Button) findViewById(R.id.a003_bt_out);
		a003_bt_share = (Button) findViewById(R.id.a003_bt_share);
		a003_bt_pager = (Button) findViewById(R.id.a003_bt_pager);
		a003_bt_drawer = (Button) findViewById(R.id.a003_bt_drawer);

		// a003_social_adapter.enable(a003_bt_share);

	}

	public void onEvent(Location event) {

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
		case R.id.a003_bt_drawer:
			startActivity(new Intent(this, A009_Activity_Drawer.class));
			break;
		case R.id.a003_bt_pager:
			startActivity(new Intent(this, A008_Activity_Pager.class));
			break;
		case R.id.a003_bt_category:
			startActivity(new Intent(this, A006_Activity_Category.class));
			break;
		case R.id.a003_bt_current:
			startActivity(new Intent(this, A007_Activity_Current.class));
			break;
		case R.id.a003_bt_howto:
			startActivity(new Intent(this, A005_Activity_Howto.class));
			break;
		case R.id.a003_bt_out:
			showDecisionDialog(this, LOGOUT_DIALOG_ID, "LOGOUT",
					"Do you want to logout?", "YES", "NO", this);
			break;
		case R.id.a003_bt_share:
			showOptionsDialog(this, SHARE_DIALOG_ID, "Share",
					"Where do you want to share?", R.drawable.share_icon,
					new Option[] { new Option("Mail", R.drawable.gmail),
							new Option("Facebook", R.drawable.facebook),
							new Option("Twitter", R.drawable.twitter) }, this);
			break;
		default:
			break;
		}

	}

	@Override
	public void onResultSuccess(final BaseResult result) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (result instanceof LogoutResult) {
					finish();
					Intent login = new Intent(getActiveActivity(),
							A001_Activity_Login.class);
					login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(login);
				}
			}
		});
	}

	@Override
	public void onResultFail(final BaseResult result) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (result instanceof LogoutResult) {
					finish();
					Intent login = new Intent(getActiveActivity(),
							A001_Activity_Login.class);
					login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(login);
				} else {
					showAlertDialog(getActiveActivity(), -1, result.getTitle(),
							result.getMessage(), -1, null);
				}
			}
		});
	}

	@Override
	public void onFail(final RequestTarget target, final String error,
			StatusCode code) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (target == RequestTarget.LOGOUT) {
					finish();
					Intent login = new Intent(getActiveActivity(),
							A001_Activity_Login.class);
					login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(login);
				} else {
					showAlertDialog(getActiveActivity(), -1, null, error, -1,
							null);
				}

			}
		});
	}

	@Override
	public void onAgree(int dialogId) {
		if (dialogId == LOGOUT_DIALOG_ID)
			makeRequest(TAG, RequestTarget.LOGOUT, new LogoutParam(
					RequestFormat.DEFAULT), this);
	}

	@Override
	public void onDisAgree(int dialogId) {
		if (dialogId == LOGOUT_DIALOG_ID) {

		}

	}

	@Override
	public void onOptionsClick(int id, View view) {
		if (id == SHARE_DIALOG_ID) {
			switch (view.getId()) {
			case 0:
				// first option
				break;
			case 1:
				// second option
				break;
			case 2:
				// third option
				break;
			default:
				break;
			}
		}
	}

}
