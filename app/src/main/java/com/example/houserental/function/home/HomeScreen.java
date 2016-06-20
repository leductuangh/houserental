package com.example.houserental.function.home;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.houserental.R;
import com.example.houserental.function.HouseRentalUtils;
import com.example.houserental.function.MainActivity;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.payment.PaymentRecordScreen;

import java.util.Calendar;

import core.base.BaseMultipleFragment;

/**
 * Created by leductuan on 3/5/16.
 */
public class HomeScreen extends BaseMultipleFragment {

    public static final String TAG = HomeScreen.class.getSimpleName();
    private TextView fragment_room_detail_tv_total_water_electric, fragment_room_detail_tv_registered_user_count, fragment_home_tv_unpaid_room_counter, fragment_room_detail_tv_floor_count, fragment_room_detail_tv_room_count, fragment_room_detail_tv_user_count, fragment_room_detail_tv_device_count, fragment_home_tv_day_counter;

    public static HomeScreen getInstance() {
        return new HomeScreen();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(com.example.houserental.R.layout.fragment_home, container, false);
    }

    @Override
    public void onBaseCreate() {

    }

    @Override
    public void onDeepLinking(Intent data) {

    }

    @Override
    public void onNotification(Intent data) {

    }

    @Override
    public void onBindView() {
        super.onBindView();
        fragment_room_detail_tv_total_water_electric = (TextView) findViewById(R.id.fragment_room_detail_tv_total_water_electric);
        fragment_room_detail_tv_registered_user_count = (TextView) findViewById(R.id.fragment_room_detail_tv_registered_user_count);
        fragment_home_tv_unpaid_room_counter = (TextView) findViewById(R.id.fragment_home_tv_unpaid_room_counter);
        fragment_home_tv_day_counter = (TextView) findViewById(R.id.fragment_home_tv_day_counter);
        fragment_room_detail_tv_floor_count = (TextView) findViewById(R.id.fragment_room_detail_tv_floor_count);
        fragment_room_detail_tv_room_count = (TextView) findViewById(R.id.fragment_room_detail_tv_room_count);
        fragment_room_detail_tv_user_count = (TextView) findViewById(R.id.fragment_room_detail_tv_user_count);
        fragment_room_detail_tv_device_count = (TextView) findViewById(R.id.fragment_room_detail_tv_device_count);
        registerSingleAction(R.id.fragment_home_bt_create_payment);
    }

    @Override
    public void onInitializeViewData() {
        fragment_room_detail_tv_floor_count.setText(String.format(getString(R.string.home_floor_info), DAOManager.getFloorCount()));
        fragment_room_detail_tv_room_count.setText(String.format(getString(R.string.home_room_info), DAOManager.getRoomCount(), DAOManager.getRentedRoomCount()));
        fragment_room_detail_tv_user_count.setText(String.format(getString(R.string.home_user_info), DAOManager.getUserCount(), DAOManager.getMaleCount(), DAOManager.getFemaleCount()));
        fragment_room_detail_tv_device_count.setText(String.format(getString(R.string.home_device_info), DAOManager.getDeviceCount()));
        fragment_room_detail_tv_registered_user_count.setText(String.format(getString(R.string.home_registered_user_info), DAOManager.getRegisteredUserCount()));
        fragment_room_detail_tv_total_water_electric.setText(getString(R.string.home_water_electric_total_info, HouseRentalUtils.toThousandVND(DAOManager.totalWaterAndElectricRevenueInMonth(Calendar.getInstance()))));
    }

    private Spannable getUnPaidRoomText() {
        int unPaidRoom = DAOManager.getUnPaidRoomInMonth(Calendar.getInstance());
        String unPaidRoomText = String.valueOf(unPaidRoom);
        if (unPaidRoom < 10) {
            unPaidRoomText = "0" + unPaidRoomText;
        }

        if (unPaidRoom > 0) {
            String un_paid_room_counter = String.format(getString(R.string.home_unpaid_room_counter), unPaidRoomText);
            Spannable wordtoSpan = new SpannableString(un_paid_room_counter);
            wordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.Level_Four_Color)), 4, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            wordtoSpan.setSpan(new AbsoluteSizeSpan(56, true), 4, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            wordtoSpan.setSpan(new StyleSpan(Typeface.BOLD), 4, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return wordtoSpan;
        }
        return new SpannableString(getString(R.string.home_none_unpaid_room_counter));

    }

    private Spannable getDayCounterText() {
        Calendar now = Calendar.getInstance();
        Calendar endOfMonth = Calendar.getInstance();
        endOfMonth.set(Calendar.DAY_OF_MONTH, endOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
        int dayBetweenNumber = (int) HouseRentalUtils.daysBetween(now.getTime(), endOfMonth.getTime());
        String dayBetweenString = String.valueOf(dayBetweenNumber);
        if (dayBetweenNumber < 10) {
            dayBetweenString = "0" + dayBetweenString;
        }
        String day_counter = String.format(getString(R.string.home_day_counter), dayBetweenString, now.get(Calendar.MONTH) + 1);
        Spannable wordtoSpan = new SpannableString(day_counter);
        wordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.Level_Four_Color)), 4, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new AbsoluteSizeSpan(56, true), 4, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new StyleSpan(Typeface.BOLD), 4, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        wordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.Level_Four_Color)), 19, day_counter.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new AbsoluteSizeSpan(56, true), 19, day_counter.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new StyleSpan(Typeface.BOLD), 19, day_counter.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return wordtoSpan;
    }

    @Override
    public void onBaseResume() {
        ((MainActivity) getActiveActivity()).setScreenHeader(getString(com.example.houserental.R.string.main_header_home));
        fragment_home_tv_day_counter.setText(getDayCounterText());
        fragment_home_tv_unpaid_room_counter.setText(getUnPaidRoomText());

    }

    @Override
    public void onBaseFree() {

    }

    @Override
    public void onSingleClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_home_bt_create_payment:
                addFragment(R.id.activity_main_container, PaymentRecordScreen.getInstance(), PaymentRecordScreen.TAG);
                break;
        }
    }
}
