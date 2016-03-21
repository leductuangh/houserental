package com.example.houserental.function.payment;

import android.content.Intent;
import android.graphics.Bitmap;
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
            fragment_payment_review_tv_room_type_price,
            fragment_payment_review_tv_room_type_total,
            fragment_payment_review_tv_electric_price,
            fragment_payment_review_tv_electric_total,
            fragment_payment_review_tv_water_price,
            fragment_payment_review_tv_water_total,
            fragment_payment_review_tv_waste_price,
            fragment_payment_review_tv_waste_total,
            fragment_payment_review_tv_device_price,
            fragment_payment_review_tv_device_total,
            fragment_payment_review_tv_total;
    private LinearLayout fragment_payment_review_ll_content;
    private int screen_width, screen_height;

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
        fragment_payment_review_tv_room_type_price = (TextView) findViewById(R.id.fragment_payment_review_tv_room_type_price);
        fragment_payment_review_tv_room_type_total = (TextView) findViewById(R.id.fragment_payment_review_tv_room_type_total);
        fragment_payment_review_tv_electric_price = (TextView) findViewById(R.id.fragment_payment_review_tv_electric_price);
        fragment_payment_review_tv_electric_total = (TextView) findViewById(R.id.fragment_payment_review_tv_electric_total);
        fragment_payment_review_tv_water_price = (TextView) findViewById(R.id.fragment_payment_review_tv_water_price);
        fragment_payment_review_tv_water_total = (TextView) findViewById(R.id.fragment_payment_review_tv_water_total);
        fragment_payment_review_tv_waste_price = (TextView) findViewById(R.id.fragment_payment_review_tv_waste_price);
        fragment_payment_review_tv_waste_total = (TextView) findViewById(R.id.fragment_payment_review_tv_waste_total);
        fragment_payment_review_tv_device_price = (TextView) findViewById(R.id.fragment_payment_review_tv_device_price);
        fragment_payment_review_tv_device_total = (TextView) findViewById(R.id.fragment_payment_review_tv_device_total);
        fragment_payment_review_tv_total = (TextView) findViewById(R.id.fragment_payment_review_tv_total);
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
            int electric_total = electric_different * payment.getElectricPrice();
            int water_difference = payment.getCurrentWaterNumber() - payment.getPreviousWaterNumber();
            int water_total = water_difference * payment.getWaterPrice();
            int waste_price = payment.getUserCount() <= 2 ? 15000 : payment.getWastePrice();
            int user_count = payment.getUserCount() <= 2 ? 1 : payment.getUserCount();
            int waste_total = user_count * waste_price;
            int device_total = payment.getDeviceCount() * payment.getDevicePrice();
            int total = electric_total + water_total + waste_total + device_total + payment.getRoomPrice();


            fragment_payment_review_tv_stay_period.setText(String.format(getString(com.example.houserental.R.string.payment_review_stay_period_text), formatter.format(payment.getStartDate()), formatter.format(payment.getEndDate())));
            fragment_payment_review_tv_room_name.setText(payment.getRoomName());
            fragment_payment_review_tv_owner.setText(payment.getOwner());
            fragment_payment_review_tv_payer.setText(payment.getPayer());
            fragment_payment_review_tv_room_type_price.setText(String.format(UNIT_TIME_PRICE, "1", payment.getRoomPrice()));
            fragment_payment_review_tv_room_type_total.setText(String.format(TOTAL_CURRENCY_UNIT, payment.getRoomPrice() + ""));
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
        ((MainActivity) getActiveActivity()).setScreenHeader(getString(com.example.houserental.R.string.payment_review_header));
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
                if (!isInPrintingProcess)
                    new PrintPayment().execute(captureView(fragment_payment_review_ll_content, screen_width, screen_height));
                break;
        }
    }

    private Bitmap captureView(View v, int width, int height) {
        v.setDrawingCacheEnabled(true);
        Bitmap b = Bitmap.createScaledBitmap(v.getDrawingCache(), width, height, false);
        v.setDrawingCacheEnabled(false);
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
            // print
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
