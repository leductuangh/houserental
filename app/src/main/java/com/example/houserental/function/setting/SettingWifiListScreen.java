package com.example.houserental.function.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.houserental.R;
import com.example.houserental.function.MainActivity;
import com.example.houserental.function.model.WifiHost;

import java.util.ArrayList;

import core.base.BaseMultipleFragment;

public class SettingWifiListScreen extends BaseMultipleFragment {

    private SettingWifiListAdapter adapter;
    private ListView fragment_setting_wifi_host_list_lv_owners;
    private ArrayList<WifiHost> wifis;


    public static SettingWifiListScreen getInstance() {
        SettingWifiListScreen screen = new SettingWifiListScreen();
        Bundle bundle = new Bundle();
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting_wifi_host_list, container, false);
    }

    @Override
    public void onBaseCreate() {
        adapter = new SettingWifiListAdapter(wifis = WifiHost.get());
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
        fragment_setting_wifi_host_list_lv_owners = (ListView) findViewById(R.id.fragment_setting_wifi_host_list_lv_owners);
        fragment_setting_wifi_host_list_lv_owners.setAdapter(adapter);
        registerSingleAction(R.id.fragment_setting_wifi_host_list_bt_add);
    }

    @Override
    public void onInitializeViewData() {

    }

    @Override
    public void onBaseResume() {
        ((MainActivity) getActiveActivity()).setScreenHeader(getString(R.string.setting_wifi_host_list_header));
    }

    @Override
    public void onBaseFree() {

    }

    @Override
    public void onSingleClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_setting_wifi_host_list_bt_add:
                new SettingWifiDialog(getActiveActivity(), wifis, -1).show();
                break;
        }
    }
}
