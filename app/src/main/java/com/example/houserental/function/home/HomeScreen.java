package com.example.houserental.function.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.houserental.R;
import com.example.houserental.function.MainActivity;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.payment.PaymentRecordScreen;

import core.base.BaseMultipleFragment;

/**
 * Created by leductuan on 3/5/16.
 */
public class HomeScreen extends BaseMultipleFragment {

    public static final String TAG = HomeScreen.class.getSimpleName();
    private TextView fragment_room_detail_tv_floor_count, fragment_room_detail_tv_room_count, fragment_room_detail_tv_user_count, fragment_room_detail_tv_device_count;

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
        findViewById(R.id.fragment_home_bt_create_payment);
        fragment_room_detail_tv_floor_count = (TextView) findViewById(R.id.fragment_room_detail_tv_floor_count);
        fragment_room_detail_tv_room_count = (TextView) findViewById(R.id.fragment_room_detail_tv_room_count);
        fragment_room_detail_tv_user_count = (TextView) findViewById(R.id.fragment_room_detail_tv_user_count);
        fragment_room_detail_tv_device_count = (TextView) findViewById(R.id.fragment_room_detail_tv_device_count);
    }

    @Override
    public void onInitializeViewData() {
        fragment_room_detail_tv_floor_count.setText(String.format(getString(R.string.home_floor_info), DAOManager.getFloorCount()));
        fragment_room_detail_tv_room_count.setText(String.format(getString(R.string.home_room_info), DAOManager.getRoomCount(), DAOManager.getRentedRoomCount()));
        fragment_room_detail_tv_user_count.setText(String.format(getString(R.string.home_user_info), DAOManager.getUserCount(), DAOManager.getMaleCount(), DAOManager.getFemaleCount()));
        fragment_room_detail_tv_device_count.setText(String.format(getString(R.string.home_device_info), DAOManager.getDeviceCount()));
    }

    @Override
    public void onBaseResume() {
        ((MainActivity) getActiveActivity()).setScreenHeader(getString(com.example.houserental.R.string.main_header_home));

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
