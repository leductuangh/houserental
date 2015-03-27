package com.example.commonframe.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.commonframe.R;
import com.example.commonframe.util.SingleClick.SingleClickListener;
import com.example.commonframe.util.Utils;

public class AlertDialog extends BaseDialog implements SingleClickListener {

	public interface AlertDialogListener {
		public void onAlertConfirmed(int id);
	}

	private Button alert_dialog_bt_ok;
	private TextView alert_dialog_tv_title, alert_dialog_tv_message;
	private String title;
	private String message;
	private int icon_id;
	private int id;
	private ImageView alert_dialog_img_icon;
	private AlertDialogListener listener;

	public AlertDialog(Context context, int id, String title, String message,
			int icon_id, AlertDialogListener listener) {
		super(context);
		this.title = title;
		this.message = message;
		this.icon_id = icon_id;
		this.id = id;
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_dialog);
	}

	@Override
	public void onSingleClick(View v) {
		switch (v.getId()) {
		case R.id.alert_dialog_bt_ok:
			dismiss();
			if (listener != null)
				listener.onAlertConfirmed(id);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onCreateObject() {
		getSingleClick().setListener(this);
	}

	@Override
	protected void onBindView() {
		alert_dialog_tv_message = (TextView) findViewById(R.id.alert_dialog_tv_message);
		alert_dialog_tv_title = (TextView) findViewById(R.id.alert_dialog_tv_title);
		alert_dialog_img_icon = (ImageView) findViewById(R.id.alert_dialog_img_icon);

		alert_dialog_bt_ok = (Button) findViewById(R.id.alert_dialog_bt_ok);
		alert_dialog_bt_ok.setOnClickListener(getSingleClick());

		if (icon_id != -1)
			alert_dialog_img_icon.setImageResource(icon_id);
		else
			alert_dialog_img_icon.setVisibility(View.GONE);

		if (!Utils.isEmpty(title))
			alert_dialog_tv_title.setText(title);
		else
			alert_dialog_tv_title.setVisibility(View.GONE);

		if (!Utils.isEmpty(message))
			alert_dialog_tv_message.setText(message);
		else
			alert_dialog_tv_message.setVisibility(View.GONE);
	}

}
