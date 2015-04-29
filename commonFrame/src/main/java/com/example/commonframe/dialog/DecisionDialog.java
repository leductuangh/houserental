package com.example.commonframe.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.commonframe.R;
import com.example.commonframe.util.SingleClick.SingleClickListener;
import com.example.commonframe.util.Utils;

public class DecisionDialog extends BaseDialog implements SingleClickListener {
	private String title;
	private String message;
	private String yes;
	private String no;
	private int id;
	private DecisionDialogListener listener;
	private TextView decision_dialog_tv_title, decision_dialog_tv_message;
	private Button decision_dialog_bt_no, decision_dialog_bt_yes;

	public interface DecisionDialogListener {
		public void onAgree(int id);

		public void onDisAgree(int id);
	}

	public DecisionDialog(Context context, int id, String title,
			String message, String yes, String no,
			DecisionDialogListener listener) {
		super(context);
		this.id = id;
		this.title = title;
		this.message = message;
		this.yes = yes;
		this.no = no;
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.decision_dialog);
	}

	@Override
	protected void onCreateObject() {
		getSingleClick().setListener(this);
	}

	@Override
	protected void onBindView() {
		decision_dialog_bt_yes = (Button) findViewById(R.id.decision_dialog_bt_yes);
		decision_dialog_bt_no = (Button) findViewById(R.id.decision_dialog_bt_no);
		decision_dialog_tv_title = (TextView) findViewById(R.id.decision_dialog_tv_title);
		decision_dialog_tv_message = (TextView) findViewById(R.id.decision_dialog_tv_message);

		decision_dialog_bt_no.setOnTouchListener(getSingleTouch());
		decision_dialog_bt_yes.setOnTouchListener(getSingleTouch());
		decision_dialog_bt_yes.setOnClickListener(getSingleClick());
		decision_dialog_bt_no.setOnClickListener(getSingleClick());

		if (!Utils.isEmpty(title))
			decision_dialog_tv_title.setText(title);
		if (!Utils.isEmpty(message))
			decision_dialog_tv_message.setText(message);
		if (!Utils.isEmpty(yes))
			decision_dialog_bt_yes.setText(yes);
		if (!Utils.isEmpty(no))
			decision_dialog_bt_no.setText(no);
	}

	@Override
	public void onSingleClick(View v) {
		dismiss();
		switch (v.getId()) {
		case R.id.decision_dialog_bt_no:
			listener.onDisAgree(id);
			break;
		case R.id.decision_dialog_bt_yes:
			listener.onAgree(id);
			break;
		default:
			break;
		}
	}
}
