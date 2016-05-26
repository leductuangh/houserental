package com.example.houserental.function.payment;

import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.houserental.R;
import com.example.houserental.function.HouseRentalApplication;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import core.base.BaseDialog;
import core.util.SingleClick;

/**
 * Created by Tyrael on 5/6/16.
 */
public class PaymentPaidDatePickerDialog extends BaseDialog implements SingleClick.SingleClickListener {

    private DatePicker fragment_payment_paid_date_dp;
    private OnPaidDatePickerListener listener;
    private Calendar start_date;
    private Calendar endOfMonth;
    private SimpleDateFormat formatter;

    public PaymentPaidDatePickerDialog(Context context, Calendar start_date, OnPaidDatePickerListener listener) {
        super(context);
        this.listener = listener;
        this.start_date = Calendar.getInstance();
        this.start_date.setTimeInMillis(start_date.getTimeInMillis());
        this.start_date.set(Calendar.HOUR_OF_DAY, 0);
        this.start_date.set(Calendar.MINUTE, 0);
        this.start_date.set(Calendar.SECOND, 1);
        this.start_date.set(Calendar.MILLISECOND, 0);
        this.endOfMonth = Calendar.getInstance();
        int endDate = this.start_date.getActualMaximum(Calendar.DAY_OF_MONTH);
        this.endOfMonth.set(start_date.get(Calendar.YEAR), start_date.get(Calendar.MONTH), endDate);
        this.endOfMonth.set(Calendar.HOUR_OF_DAY, 23);
        this.endOfMonth.set(Calendar.MINUTE, 59);
        this.endOfMonth.set(Calendar.SECOND, 59);
        this.endOfMonth.set(Calendar.MILLISECOND, 0);
        setContentView(R.layout.dialog_payment_paid_date_picker);
    }

    @Override
    protected void onBaseCreate() {
        formatter = new SimpleDateFormat("dd-MMM-yyyy");
        getSingleClick().setListener(this);
    }

    @Override
    protected void onBindView() {
        findViewById(R.id.fragment_payment_paid_date_bt_ok);
        fragment_payment_paid_date_dp = (DatePicker) findViewById(R.id.fragment_payment_paid_date_dp);
        fragment_payment_paid_date_dp.setCalendarViewShown(false);
        if (start_date.after(endOfMonth)) {
            endOfMonth.add(Calendar.MONTH, 1);
            fragment_payment_paid_date_dp.setMaxDate(endOfMonth.getTimeInMillis());
            fragment_payment_paid_date_dp.setMinDate(start_date.getTimeInMillis() - 1000);
        } else {
            fragment_payment_paid_date_dp.setMaxDate(endOfMonth.getTimeInMillis());
            fragment_payment_paid_date_dp.setMinDate(start_date.getTimeInMillis() - 1000);
        }
    }

    @Override
    public void onSingleClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_payment_paid_date_bt_ok:
                if (listener != null) {
                    Calendar paid_date = Calendar.getInstance();
                    paid_date.set(Calendar.YEAR, fragment_payment_paid_date_dp.getYear());
                    paid_date.set(Calendar.MONTH, fragment_payment_paid_date_dp.getMonth());
                    paid_date.set(Calendar.DAY_OF_MONTH, fragment_payment_paid_date_dp.getDayOfMonth());
                    if (start_date != null && paid_date.before(start_date)) {
                        Toast.makeText(HouseRentalApplication.getActiveActivity(), String.format(HouseRentalApplication.getContext().getString(R.string.payment_record_wrong_pay_date_error), formatter.format(start_date.getTime())), Toast.LENGTH_SHORT).show();
                        return;
                    } else if (paid_date.after(endOfMonth)) {
                        paid_date = endOfMonth;
                    }
                    listener.onPaidDatePicked(paid_date);
                    dismiss();
                }
                break;
        }
    }

    public interface OnPaidDatePickerListener {
        void onPaidDatePicked(Calendar paid_date);
    }
}
