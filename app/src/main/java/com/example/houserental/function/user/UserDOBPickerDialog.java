package com.example.houserental.function.user;

import android.content.Context;
import android.view.View;
import android.widget.DatePicker;

import com.example.houserental.R;

import java.util.Calendar;

import core.base.BaseDialog;
import core.util.SingleClick;

/**
 * Created by Tyrael on 5/6/16.
 */
public class UserDOBPickerDialog extends BaseDialog implements SingleClick.SingleClickListener {

    private DatePicker fragment_user_insert_dp_dob;
    private OnDOBPickerListener listener;
    private Calendar dob;

    public UserDOBPickerDialog(Context context, OnDOBPickerListener listener) {
        super(context);
        this.listener = listener;
        setContentView(R.layout.dialog_user_dob_picker);
    }

    public UserDOBPickerDialog(Context context, Calendar dob, OnDOBPickerListener listener) {
        super(context);
        this.listener = listener;
        this.dob = dob;
        setContentView(R.layout.dialog_user_dob_picker);
    }

    @Override
    protected void onBaseCreate() {
        getSingleClick().setListener(this);
    }

    @Override
    protected void onBindView() {
        findViewById(R.id.fragment_user_insert_bt_dob_ok);
        fragment_user_insert_dp_dob = (DatePicker) findViewById(R.id.fragment_user_insert_dp_dob);
        fragment_user_insert_dp_dob.setMaxDate(Calendar.getInstance().getTimeInMillis());
        if (dob != null) {
            fragment_user_insert_dp_dob.updateDate(dob.get(Calendar.YEAR), dob.get(Calendar.MONTH), dob.get(Calendar.DAY_OF_MONTH));
        }
    }

    @Override
    public void onSingleClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_user_insert_bt_dob_ok:
                if (listener != null) {
                    Calendar dob = Calendar.getInstance();
                    dob.set(Calendar.YEAR, fragment_user_insert_dp_dob.getYear());
                    dob.set(Calendar.MONTH, fragment_user_insert_dp_dob.getMonth());
                    dob.set(Calendar.DAY_OF_MONTH, fragment_user_insert_dp_dob.getDayOfMonth());
                    listener.onDOBPicked(dob);
                    dismiss();
                }
                break;
        }
    }

    public interface OnDOBPickerListener {
        void onDOBPicked(Calendar dob);
    }
}
