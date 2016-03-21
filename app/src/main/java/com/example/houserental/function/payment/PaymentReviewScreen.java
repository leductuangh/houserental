package com.example.houserental.function.payment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.houserental.R;
import com.example.houserental.function.MainActivity;
import com.example.houserental.function.model.PaymentDAO;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import core.base.BaseMultipleFragment;

/**
 * Created by leductuan on 3/14/16.
 */
public class PaymentReviewScreen extends BaseMultipleFragment {

    public static final String TAG = PaymentReviewScreen.class.getSimpleName();
    private static final String PAYMENT_KEY = "payment_key";
    private final String UNIT_TIME_PRICE = "%s X %s";
    private final String TOTAL_CURRENCY_UNIT = "%s VNĐ";
    private PaymentDAO payment;
    private SimpleDateFormat formatter;
    private boolean isInPrintingProcess = false;
    private TextView
            fragment_payment_review_tv_room_name,
            fragment_payment_review_tv_stay_period,
            fragment_payment_review_tv_owner,
            fragment_payment_review_tv_payer,
            fragment_payment_review_tv_room_price,
            fragment_payment_review_tv_room_price_total,
            fragment_payment_review_tv_electric_price,
            fragment_payment_review_tv_electric_total,
            fragment_payment_review_tv_water_price,
            fragment_payment_review_tv_water_total,
            fragment_payment_review_tv_waste_price,
            fragment_payment_review_tv_waste_total,
            fragment_payment_review_tv_device_price,
            fragment_payment_review_tv_device_total,
            fragment_payment_review_tv_total,
            fragment_payment_review_tv_room_price_day,
            fragment_payment_review_tv_room_price_day_total;
    private LinearLayout fragment_payment_review_ll_content;
    private int screen_width, screen_height, device_total, total, waste_total, electric_total;

    public static PaymentReviewScreen getInstance(PaymentDAO payment) {
        PaymentReviewScreen screen = new PaymentReviewScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PAYMENT_KEY, payment);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(com.example.houserental.R.layout.fragment_payment_review, container, false);
    }

    @Override
    public void onBaseCreate() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            payment = (PaymentDAO) bundle.getSerializable(PAYMENT_KEY);
            if (payment == null) {
                showAlertDialog(getActiveActivity(), -1, -1, getString(com.example.houserental.R.string.application_alert_dialog_title), getString(com.example.houserental.R.string.payment_record_no_owner_error), getString(com.example.houserental.R.string.common_ok), null);
                finish();
            }
        }
        formatter = new SimpleDateFormat("dd-MMM-yyyy");
    }

    @Override
    public void onDeepLinking(Intent data) {

    }

    @Override
    public void onNotification(Intent data) {

    }

    @Override
    public void onBindView() {
        fragment_payment_review_tv_room_name = (TextView) findViewById(R.id.fragment_payment_review_tv_room_name);
        fragment_payment_review_tv_stay_period = (TextView) findViewById(R.id.fragment_payment_review_tv_stay_period);
        fragment_payment_review_tv_owner = (TextView) findViewById(R.id.fragment_payment_review_tv_owner);
        fragment_payment_review_tv_payer = (TextView) findViewById(R.id.fragment_payment_review_tv_payer);
        fragment_payment_review_tv_room_price = (TextView) findViewById(R.id.fragment_payment_review_tv_room_price);
        fragment_payment_review_tv_room_price_total = (TextView) findViewById(R.id.fragment_payment_review_tv_room_price_total);
        fragment_payment_review_tv_electric_price = (TextView) findViewById(R.id.fragment_payment_review_tv_electric_price);
        fragment_payment_review_tv_electric_total = (TextView) findViewById(R.id.fragment_payment_review_tv_electric_total);
        fragment_payment_review_tv_water_price = (TextView) findViewById(R.id.fragment_payment_review_tv_water_price);
        fragment_payment_review_tv_water_total = (TextView) findViewById(R.id.fragment_payment_review_tv_water_total);
        fragment_payment_review_tv_waste_price = (TextView) findViewById(R.id.fragment_payment_review_tv_waste_price);
        fragment_payment_review_tv_waste_total = (TextView) findViewById(R.id.fragment_payment_review_tv_waste_total);
        fragment_payment_review_tv_device_price = (TextView) findViewById(R.id.fragment_payment_review_tv_device_price);
        fragment_payment_review_tv_device_total = (TextView) findViewById(R.id.fragment_payment_review_tv_device_total);
        fragment_payment_review_tv_total = (TextView) findViewById(R.id.fragment_payment_review_tv_total);
        fragment_payment_review_tv_room_price_day = (TextView) findViewById(R.id.fragment_payment_review_tv_room_price_day);
        fragment_payment_review_tv_room_price_day_total = (TextView) findViewById(R.id.fragment_payment_review_tv_room_price_day_total);
        fragment_payment_review_ll_content = (LinearLayout) findViewById(R.id.fragment_payment_review_ll_content);
        findViewById(R.id.fragment_payment_review_correct);
        findViewById(R.id.fragment_payment_review_print);
        screen_width = getActiveActivity().getWindow().getDecorView().getWidth();
        screen_height = getActiveActivity().getWindow().getDecorView().getHeight();
    }

    @Override
    public void onInitializeViewData() {
        if (payment != null) {
            int electric_different = payment.getCurrentElectricNumber() - payment.getPreviousElectricNumber();
            electric_total = electric_different * payment.getElectricPrice();
            int water_difference = payment.getCurrentWaterNumber() - payment.getPreviousWaterNumber();
            int water_total = water_difference * payment.getWaterPrice();
            int waste_price = payment.getUserCount() <= 2 ? 15000 : payment.getWastePrice();
            int user_count = payment.getUserCount() <= 2 ? 1 : payment.getUserCount();
            waste_total = user_count * waste_price;
            device_total = payment.getDeviceCount() * payment.getDevicePrice();
            total = electric_total + water_total + waste_total + device_total + payment.getRoomPrice();
            int month_count = payment.isFullMonth() ? 1 : 0;
            int month_pay = month_count * payment.getRoomPrice();
            int day_count = payment.getExceedDate();
            int price_per_day = payment.getRoomPrice() / 30;
            int day_pay = price_per_day * day_count;
            Calendar end_date = Calendar.getInstance();
            if (payment.isFullMonth() && payment.getExceedDate() <= 0) {
                end_date.setTime(payment.getStartDate());
                end_date.add(Calendar.MONTH, 1);
            } else {
                end_date.setTime(payment.getEndDate());
            }

            fragment_payment_review_tv_stay_period.setText(String.format(getString(com.example.houserental.R.string.payment_review_stay_period_text), formatter.format(payment.getStartDate()), formatter.format(end_date.getTime())));
            fragment_payment_review_tv_room_name.setText(payment.getRoomName());
            fragment_payment_review_tv_owner.setText(payment.getOwner());
            fragment_payment_review_tv_payer.setText(payment.getPayer());
            fragment_payment_review_tv_room_price.setText(String.format(UNIT_TIME_PRICE, month_count + "", payment.getRoomPrice()));
            fragment_payment_review_tv_room_price_total.setText(String.format(TOTAL_CURRENCY_UNIT, month_pay + ""));
            fragment_payment_review_tv_room_price_day.setText(String.format(UNIT_TIME_PRICE, day_count + "", price_per_day));
            fragment_payment_review_tv_room_price_day_total.setText(String.format(TOTAL_CURRENCY_UNIT, day_pay + ""));
            fragment_payment_review_tv_electric_price.setText(String.format(UNIT_TIME_PRICE, "" + electric_different, "" + payment.getElectricPrice()));
            fragment_payment_review_tv_electric_total.setText(String.format(TOTAL_CURRENCY_UNIT, electric_total + ""));
            fragment_payment_review_tv_water_price.setText(String.format(UNIT_TIME_PRICE, "" + water_difference, payment.getWaterPrice() + ""));
            fragment_payment_review_tv_water_total.setText(String.format(TOTAL_CURRENCY_UNIT, water_total + ""));
            fragment_payment_review_tv_waste_price.setText(String.format(UNIT_TIME_PRICE, user_count + "", waste_price));
            fragment_payment_review_tv_waste_total.setText(String.format(TOTAL_CURRENCY_UNIT, waste_total + ""));
            fragment_payment_review_tv_device_price.setText(String.format(UNIT_TIME_PRICE, payment.getDeviceCount() + "", payment.getDevicePrice()));
            fragment_payment_review_tv_device_total.setText(String.format(TOTAL_CURRENCY_UNIT, device_total + ""));
            fragment_payment_review_tv_total.setText(String.format(TOTAL_CURRENCY_UNIT, total + ""));


        }
    }

    @Override
    public void onBaseResume() {
        ((MainActivity) getActiveActivity()).setScreenHeader(getString(R.string.payment_review_header));
    }

    @Override
    public void onBaseFree() {

    }

    @Override
    public void onSingleClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_payment_review_correct:
                finish();
                break;
            case R.id.fragment_payment_review_print:
                if (!isInPrintingProcess) {
                    try {
                        boolean result = new PrintPayment().execute(captureView(fragment_payment_review_ll_content, screen_width, screen_height)).get();
                        if (result) {
                            payment.setDeviceTotal(device_total);
                            payment.setElectricTotal(electric_total);
                            payment.setWaterTotal(waste_total);
                            payment.setTotal(total);
                            payment.save();
                            replaceFragment(R.id.activity_main_container, PaymentHistoryScreen.getInstance(), PaymentHistoryScreen.TAG, true);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private Bitmap captureView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }

    private class PrintPayment extends AsyncTask<Bitmap, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            isInPrintingProcess = true;
            showLoadingDialog(getActiveActivity(), getString(com.example.houserental.R.string.payment_review_print_in_process));
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Bitmap... params) {
            try {
                MediaStore.Images.Media.insertImage(getActiveActivity().getContentResolver(), params[0], payment.getRoomId() + "_" + payment.getEndDate().getYear() + "_" + payment.getEndDate().getMonth() + "_" + payment.getEndDate().getDate(), "");
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            closeLoadingDialog();
            if (result) {
                Toast.makeText(getActiveActivity(), getString(com.example.houserental.R.string.payment_review_print_success), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActiveActivity(), getString(R.string.application_alert_dialog_error_general), Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
            isInPrintingProcess = false;
        }
    }
}
