package com.example.houserental.function.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.houserental.R;
import com.example.houserental.function.HouseRentalUtils;
import com.example.houserental.function.model.PaymentDAO;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import core.base.BaseMultipleFragment;
import core.util.Utils;

/**
 * Created by Tyrael on 5/23/16.
 */
public class PaymentHistoryReviewScreen extends BaseMultipleFragment {

    public static final String TAG = PaymentHistoryReviewScreen.class.getName();
    private static final String PAYMENT_KEY = "payment_key";
    private PaymentDAO payment;
    private TextView fragment_payment_history_review_tv_room_name,
            fragment_payment_history_review_tv_stay_period,
            fragment_payment_history_review_tv_owner,
            fragment_payment_history_review_tv_payer,
            fragment_payment_history_review_tv_room_unit,
            fragment_payment_history_review_tv_room_price,
            fragment_payment_history_review_tv_room_price_total,
            fragment_payment_history_review_tv_electric_unit,
            fragment_payment_history_review_tv_electric_price,
            fragment_payment_history_review_tv_electric_total,
            fragment_payment_history_review_tv_water_unit,
            fragment_payment_history_review_tv_water_price,
            fragment_payment_history_review_tv_water_total,
            fragment_payment_history_review_tv_waste_unit,
            fragment_payment_history_review_tv_waste_price,
            fragment_payment_history_review_tv_waste_total,
            fragment_payment_history_review_tv_device_unit,
            fragment_payment_history_review_tv_device_price,
            fragment_payment_history_review_tv_device_total,
            fragment_payment_history_review_tv_total;
    private SimpleDateFormat formatter;

    public static PaymentHistoryReviewScreen getInstance(PaymentDAO payment) {
        Bundle bundle = new Bundle();
        PaymentHistoryReviewScreen screen = new PaymentHistoryReviewScreen();
        bundle.putSerializable(PAYMENT_KEY, payment);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment_history_review, container, false);
    }

    @Override
    public void onBaseCreate() {
        Bundle bundle = getArguments();
        formatter = new SimpleDateFormat("dd-MMM-yyyy");
        if (bundle != null) {
            payment = (PaymentDAO) bundle.getSerializable(PAYMENT_KEY);
            if (payment == null) {
                showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.application_alert_dialog_error_general), getString(R.string.common_ok), null, null);
                return;
            }
        } else {
            showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.application_alert_dialog_error_general), getString(R.string.common_ok), null, null);
            return;
        }
    }

    @Override
    public void onDeepLinking(Intent data) {

    }

    @Override
    public void onNotification(Intent data) {

    }

    @Override
    public void onBindView() {
        fragment_payment_history_review_tv_room_name = (TextView) findViewById(R.id.fragment_payment_history_review_tv_room_name);
        fragment_payment_history_review_tv_stay_period = (TextView) findViewById(R.id.fragment_payment_history_review_tv_stay_period);
        fragment_payment_history_review_tv_owner = (TextView) findViewById(R.id.fragment_payment_history_review_tv_owner);
        fragment_payment_history_review_tv_payer = (TextView) findViewById(R.id.fragment_payment_history_review_tv_payer);
        fragment_payment_history_review_tv_room_unit = (TextView) findViewById(R.id.fragment_payment_history_review_tv_room_unit);
        fragment_payment_history_review_tv_room_price = (TextView) findViewById(R.id.fragment_payment_history_review_tv_room_price);
        fragment_payment_history_review_tv_room_price_total = (TextView) findViewById(R.id.fragment_payment_history_review_tv_room_price_total);
        fragment_payment_history_review_tv_electric_unit = (TextView) findViewById(R.id.fragment_payment_history_review_tv_electric_unit);
        fragment_payment_history_review_tv_electric_price = (TextView) findViewById(R.id.fragment_payment_history_review_tv_electric_price);
        fragment_payment_history_review_tv_electric_total = (TextView) findViewById(R.id.fragment_payment_history_review_tv_electric_total);
        fragment_payment_history_review_tv_water_unit = (TextView) findViewById(R.id.fragment_payment_history_review_tv_water_unit);
        fragment_payment_history_review_tv_water_price = (TextView) findViewById(R.id.fragment_payment_history_review_tv_water_price);
        fragment_payment_history_review_tv_water_total = (TextView) findViewById(R.id.fragment_payment_history_review_tv_water_total);
        fragment_payment_history_review_tv_waste_unit = (TextView) findViewById(R.id.fragment_payment_history_review_tv_waste_unit);
        fragment_payment_history_review_tv_waste_price = (TextView) findViewById(R.id.fragment_payment_history_review_tv_waste_price);
        fragment_payment_history_review_tv_waste_total = (TextView) findViewById(R.id.fragment_payment_history_review_tv_waste_total);
        fragment_payment_history_review_tv_device_unit = (TextView) findViewById(R.id.fragment_payment_history_review_tv_device_unit);
        fragment_payment_history_review_tv_device_price = (TextView) findViewById(R.id.fragment_payment_history_review_tv_device_price);
        fragment_payment_history_review_tv_device_total = (TextView) findViewById(R.id.fragment_payment_history_review_tv_device_total);
        fragment_payment_history_review_tv_total = (TextView) findViewById(R.id.fragment_payment_history_review_tv_total);
    }

    @Override
    public void onInitializeViewData() {
        if (payment != null) {
            Calendar start = Calendar.getInstance();
            start.setTime(payment.getStartDate());
            int dayCountOfMonth = Utils.dayCountOfMonth(start.get(Calendar.MONTH), start.get(Calendar.YEAR));

            // quantity
            int stay_days = payment.getStayDays();
            int electric_different = payment.getCurrentElectricNumber() - payment.getPreviousElectricNumber();
            int water_difference = payment.getCurrentWaterNumber() - payment.getPreviousWaterNumber();
            int device_count = payment.getDeviceCount();
            int user_count = payment.getUserCount() <= 2 ? 1 : payment.getUserCount();

            // price
            int electric_price = payment.getElectricPrice();
            int water_price = payment.getWaterPrice();
            int device_price = payment.getDevicePrice();
            int waste_price = user_count <= 2 ? payment.getWastePrice() * 3 : payment.getWastePrice();
            int per_day_room_price = payment.getRoomPrice() / dayCountOfMonth;

            // total
            int water_total = payment.getWaterTotal();
            int electric_total = payment.getElectricTotal();
            int waste_total = payment.getWasteTotal();
            int device_total = payment.getDeviceTotal();
            int room_total = stay_days * per_day_room_price;

            String room_unit_text = "";
            if (stay_days >= dayCountOfMonth) {
                room_unit_text = String.format(getString(R.string.payment_review_room_unit_month_text), 1);
                room_total = payment.getRoomPrice();
                fragment_payment_history_review_tv_room_price.setText(HouseRentalUtils.toThousandVND(payment.getRoomPrice()));
            } else {
                room_unit_text = String.format(getString(R.string.payment_review_room_unit_day_text), stay_days);
                fragment_payment_history_review_tv_room_price.setText(HouseRentalUtils.toThousandVND(per_day_room_price));
            }

            String waste_unit_text = "";
            if (user_count == 1) {
                waste_unit_text = String.format(getString(R.string.payment_review_waste_unit_room_text), user_count);
            } else {
                waste_unit_text = String.format(getString(R.string.payment_review_waste_unit_people_text), user_count);
            }

            String electric_unit_text = String.format(getString(R.string.payment_review_electric_unit_text), electric_different);
            String water_unit_text = String.format(getString(R.string.payment_review_water_unit_text), water_difference);
            String device_unit_text = String.format(getString(R.string.payment_review_device_unit_text), device_count);

            int total = (water_total + electric_total + waste_total + device_total + room_total);
            fragment_payment_history_review_tv_stay_period.setText(String.format(getString(com.example.houserental.R.string.payment_review_stay_period_text), formatter.format(payment.getStartDate()), formatter.format(payment.getEndDate().getTime())));
            fragment_payment_history_review_tv_room_name.setText(payment.getRoomName());
            fragment_payment_history_review_tv_owner.setText(payment.getOwner());
            fragment_payment_history_review_tv_payer.setText(payment.getPayer());
            fragment_payment_history_review_tv_room_unit.setText(room_unit_text);
            fragment_payment_history_review_tv_room_price_total.setText(HouseRentalUtils.toThousandVND(room_total));
            fragment_payment_history_review_tv_electric_unit.setText(electric_unit_text);
            fragment_payment_history_review_tv_electric_price.setText(HouseRentalUtils.toThousandVND(electric_price));
            fragment_payment_history_review_tv_electric_total.setText(HouseRentalUtils.toThousandVND(electric_total));
            fragment_payment_history_review_tv_water_unit.setText(water_unit_text);
            fragment_payment_history_review_tv_water_price.setText(HouseRentalUtils.toThousandVND(water_price));
            fragment_payment_history_review_tv_water_total.setText(HouseRentalUtils.toThousandVND(water_total));
            fragment_payment_history_review_tv_waste_unit.setText(waste_unit_text);
            fragment_payment_history_review_tv_waste_price.setText(HouseRentalUtils.toThousandVND(waste_price));
            fragment_payment_history_review_tv_waste_total.setText(HouseRentalUtils.toThousandVND(waste_total));
            fragment_payment_history_review_tv_device_unit.setText(device_unit_text);
            fragment_payment_history_review_tv_device_price.setText(HouseRentalUtils.toThousandVND(device_price));
            fragment_payment_history_review_tv_device_total.setText(HouseRentalUtils.toThousandVND(device_total));
            fragment_payment_history_review_tv_total.setText(HouseRentalUtils.toThousandVND(total));
        }
    }

    @Override
    public void onBaseResume() {

    }

    @Override
    public void onBaseFree() {

    }

    @Override
    public void onSingleClick(View v) {

    }
}
