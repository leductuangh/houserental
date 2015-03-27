package com.example.commonframe.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.commonframe.R;
import com.example.commonframe.util.SingleClick.SingleClickListener;
import com.example.commonframe.util.Utils;

/**
 * This class is a customized dialog extended from Dialog, allows user to add a
 * number of buttons along with their icons, a customized title, message and the
 * dialog icon
 * 
 * @author Tyrael
 * @since August 2013
 * 
 */
public class OptionsDialog extends BaseDialog implements SingleClickListener {

	/**
	 * This class is the listener for option click event of the OptionsDialog
	 * 
	 * @author Tyrael
	 * @since August 2013
	 */
	public interface OptionsDialogListener {
		/**
		 * Fire an onClick action of the option clicked
		 * 
		 * @param view
		 *            The view of the option clicked
		 * @param id
		 *            The id of the dialog to handle
		 */
		public void onOptionsClick(int id, View view);
	}

	private Option[] options;
	private String title;
	private int title_icon_id;
	private int id;
	private String msg;
	private OptionsDialogListener listener;
	private LinearLayout options_dialog_ll_options;
	private TextView options_dialog_tv_title;
	private TextView options_dialog_tv_msg;
	private ImageView options_dialog_img_icon;

	public OptionsDialog(Context context, int id, String title, String msg,
			int title_icon_id, Option[] options, OptionsDialogListener listener) {
		super(context);
		this.id = id;
		this.options = options;
		this.title = title;
		this.title_icon_id = title_icon_id;
		this.msg = msg;
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options_dialog);
	}

	/**
	 * This function is to add an option of this dialog vertically into the
	 * root, the icon will align on the left, the text on the right, the option
	 * is also clickable
	 * 
	 * @param root
	 *            The LinearLayout root of the dialog
	 * @param id
	 *            The id of each option for onOptionsClick event
	 * @param name
	 *            The name of the option
	 * @param icon_id
	 *            The icon of the option, id to get from resource
	 */
	@SuppressLint("InflateParams")
	private void addButton(LinearLayout root, int id, String name, int icon_id) {

		View button = getLayoutInflater().inflate(
				R.layout.options_dialog_button, null);
		LinearLayout ll_icon = (LinearLayout) button
				.findViewById(R.id.options_dialog_button_ll_icon);
		LinearLayout ll_name = (LinearLayout) button
				.findViewById(R.id.options_dialog_button_ll_name);
		TextView tv_name = (TextView) button
				.findViewById(R.id.options_dialog_button_tv_name);
		ImageView icon = (ImageView) button
				.findViewById(R.id.options_dialog_button_img_icon);

		tv_name.setText(name);

		button.setId(id);
		button.setOnTouchListener(getSingleTouch());
		button.setOnClickListener(getSingleClick());
		root.addView(button);
		if (icon_id != -1)
			icon.setImageResource(icon_id);
		else {
			icon.setVisibility(View.GONE);
			ll_icon.setVisibility(View.GONE);
			((LinearLayout.LayoutParams) ll_name.getLayoutParams()).weight = 10;
		}
	}

	@Override
	protected void onCreateObject() {
		getSingleClick().setListener(this);
	}

	@Override
	protected void onBindView() {
		options_dialog_ll_options = (LinearLayout) findViewById(R.id.options_dialog_ll_options);
		options_dialog_tv_title = (TextView) findViewById(R.id.options_dialog_tv_title);
		options_dialog_tv_msg = (TextView) findViewById(R.id.options_dialog_tv_msg);
		options_dialog_img_icon = (ImageView) findViewById(R.id.options_dialog_img_icon);

		if (options == null || options.length <= 0)
			dismiss();

		if (title_icon_id != -1)
			options_dialog_img_icon.setImageResource(title_icon_id);
		else 
			options_dialog_img_icon.setVisibility(View.GONE);
		
		if (!Utils.isEmpty(title))
			options_dialog_tv_title.setText(title);
		else
			options_dialog_tv_title.setVisibility(View.GONE);

		if (!Utils.isEmpty(msg))
			options_dialog_tv_msg.setText(msg);
		else
			options_dialog_tv_msg.setVisibility(View.GONE);

		options_dialog_tv_title.setTextColor(Color.CYAN);
		options_dialog_tv_msg.setTextColor(Color.WHITE);
		boolean isOptionAdded = false;
		for (int i = 0; i < options.length; ++i) {
			if (options[i] != null) {
				addButton(options_dialog_ll_options, i, options[i].getName(),
						options[i].getIcon_id());
				isOptionAdded = true;
			}
		}

		if (!isOptionAdded)
			dismiss();
	}

	@Override
	public void onSingleClick(View v) {
		dismiss();
		listener.onOptionsClick(id, v);
	}
}
