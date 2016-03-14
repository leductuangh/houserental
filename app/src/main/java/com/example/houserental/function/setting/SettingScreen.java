package com.example.houserental.function.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.core.core.base.BaseMultipleFragment;
import com.core.data.DataSaver;
import com.core.util.Utils;
import com.example.houserental.R;
import com.example.houserental.function.MainActivity;

/**
 * Created by leductuan on 3/6/16.
 */
public class SettingScreen extends BaseMultipleFragment {

    public static final String TAG = SettingScreen.class.getSimpleName();
    private EditText fragment_setting_et_water, fragment_setting_et_electric, fragment_setting_et_device;

    public static SettingScreen getInstance() {
        return new SettingScreen();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
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
        findViewById(R.id.fragment_setting_bt_save);
        fragment_setting_et_water = (EditText) findViewById(R.id.fragment_setting_et_water);
        fragment_setting_et_electric = (EditText) findViewById(R.id.fragment_setting_et_electric);
        fragment_setting_et_device = (EditText) findViewById(R.id.fragment_setting_et_device);
    }

    @Override
    public void onInitializeViewData() {
        try {
            fragment_setting_et_water.setText(DataSaver.getInstance().getInt(DataSaver.Key.WATER_PRICE) + "");
            fragment_setting_et_electric.setText(DataSaver.getInstance().getInt(DataSaver.Key.ELECTRIC_PRICE) + "");
            fragment_setting_et_device.setText(DataSaver.getInstance().getInt(DataSaver.Key.DEVICE_PRICE) + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        switch (v.getId()) {
            case R.id.fragment_setting_bt_save:

                if (validated()) {
                    try {
                        DataSaver.getInstance().setInt(DataSaver.Key.ELECTRIC_PRICE, Integer.parseInt(fragment_setting_et_electric.getText().toString().trim()));
                        DataSaver.getInstance().setInt(DataSaver.Key.WATER_PRICE, Integer.parseInt(fragment_setting_et_water.getText().toString().trim()));
                        DataSaver.getInstance().setInt(DataSaver.Key.DEVICE_PRICE, Integer.parseInt(fragment_setting_et_device.getText().toString().trim()));
                        showAlertDialog(getActiveActivity(), -1, -1, getString(R.string.application_alert_dialog_title),
                                getString(R.string.room_alert_dialog_update_success),
                                getString((R.string.common_ok)), null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
        }
    }

    private boolean validated() {
        if (Utils.isEmpty(fragment_setting_et_electric.getText().toString().trim())) {
            showAlertDialog(getActiveActivity(), -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.setting_insert_electric_error), getString(R.string.common_ok), null);
            return false;
        }

        if (Utils.isEmpty(fragment_setting_et_water.getText().toString().trim())) {
            showAlertDialog(getActiveActivity(), -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.setting_insert_water_error), getString(R.string.common_ok), null);
            return false;
        }

        if (Utils.isEmpty(fragment_setting_et_device.getText().toString().trim())) {
            showAlertDialog(getActiveActivity(), -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.setting_insert_device_error), getString(R.string.common_ok), null);
            return false;
        }
        return true;
    }
}
