package com.example.houserental.function.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.houserental.R;
import com.example.houserental.function.MainActivity;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.DeviceDAO;
import com.example.houserental.function.model.UserDAO;

import java.util.ArrayList;
import java.util.List;

import core.base.BaseMultipleFragment;
import core.util.ClipboarbWrapper;

/**
 * Created by Tyrael on 5/7/16.
 */
public class UserDeviceListScreen extends BaseMultipleFragment implements AdapterView.OnItemClickListener, DialogInterface.OnDismissListener {

    public static final String TAG = UserDeviceListScreen.class.getName();
    private static final String USER_KEY = "user_key";
    private UserDAO user;
    private List<DeviceDAO> devices;
    private UserDeviceAdapter adapter;
    private ListView fragment_user_device_list_lv_devices;

    public static UserDeviceListScreen getInstance(UserDAO user) {
        UserDeviceListScreen screen = new UserDeviceListScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER_KEY, user);
        screen.setArguments(bundle);
        return screen;
    }

    @Override
    public void onBaseCreate() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.user = (UserDAO) bundle.getSerializable(USER_KEY);
        }

        if (user != null) {
            devices = DAOManager.getDevicesOfUser(user.getId());
        } else {
            devices = new ArrayList<>();
        }
        adapter = new UserDeviceAdapter(devices);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_device_list, container, false);
    }

    @Override
    public void onDeepLinking(Intent data) {

    }

    @Override
    public void onNotification(Intent data) {

    }

    @Override
    public void onBindView() {
        fragment_user_device_list_lv_devices = (ListView) findViewById(R.id.fragment_user_device_list_lv_devices);
        fragment_user_device_list_lv_devices.setOnItemClickListener(this);
        findViewById(R.id.fragment_user_device_list_fab_add);
    }

    @Override
    public void onInitializeViewData() {
        fragment_user_device_list_lv_devices.setAdapter(adapter);
    }

    @Override
    public void onBaseResume() {
        ((MainActivity) getActiveActivity()).setScreenHeader(String.format(getString(R.string.user_device_list_header), user.getName()));
    }

    @Override
    public void onBaseFree() {

    }

    @Override
    public void onSingleClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_user_device_list_fab_add:
                UserDeviceDialog dialog = new UserDeviceDialog(getActiveActivity(), user.getId());
                dialog.setOnDismissListener(this);
                dialog.show();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ClipboarbWrapper.copyToClipboard(getContext(), adapter.getItem(position).getMAC());
        Toast.makeText(getActiveActivity(), String.format(getString(R.string.application_clipboard_message), adapter.getItem(position).getMAC()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (devices != null)
            devices.clear();
        devices.addAll(DAOManager.getDevicesOfUser(user.getId()));
        adapter.notifyDataSetChanged();
    }
}
