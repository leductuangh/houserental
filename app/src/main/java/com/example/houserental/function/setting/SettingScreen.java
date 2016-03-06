package com.example.houserental.function.setting;

import android.content.Intent;
import android.view.View;

import com.example.houserental.R;
import com.example.houserental.core.base.BaseMultipleFragment;
import com.example.houserental.function.MainActivity;

/**
 * Created by leductuan on 3/6/16.
 */
public class SettingScreen extends BaseMultipleFragment {

    public static final String TAG = SettingScreen.class.getSimpleName();

    public static SettingScreen getInstance() {
        return new SettingScreen();
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

    }

    @Override
    public void onInitializeViewData() {

    }

    @Override
    public void onBaseResume() {
        ((MainActivity) getActiveActivity()).setScreenHeader(getString(R.string.main_header_setting));
    }

    @Override
    public void onBaseFree() {

    }

    @Override
    public void onSingleClick(View v) {

    }
}
