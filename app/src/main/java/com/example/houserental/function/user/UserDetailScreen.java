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
import android.widget.TextView;

import com.example.houserental.R;
import com.example.houserental.core.base.BaseMultipleFragment;
import com.example.houserental.model.DAOManager;
import com.example.houserental.model.DeviceDAO;
import com.example.houserental.model.UserDAO;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by leductuan on 3/6/16.
 */
public class UserDetailScreen extends BaseMultipleFragment implements AdapterView.OnItemClickListener, DialogInterface.OnDismissListener {

    public static final String TAG = UserDetailScreen.class.getSimpleName();
    private static final String USER_KEY = "user_key";
    private UserDAO user;
    private List<DeviceDAO> devices;
    private TextView fragment_user_detail_tv_device_count, fragment_user_detail_tv_id, fragment_user_detail_tv_name, fragment_user_detail_tv_dob, fragment_user_detail_tv_career, fragment_user_detail_tv_gender;
    private ListView fragment_user_detail_lv_device;
    private UserDeviceAdapter adapter;
    private UserDetailInsertDeviceDialog dialog;


    public static UserDetailScreen getInstance(UserDAO user) {
        UserDetailScreen screen = new UserDetailScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER_KEY, user);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_detail, container, false);
    }

    @Override
    public void onBaseCreate() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            user = (UserDAO) bundle.getSerializable(USER_KEY);
        }


        if (user != null) {
            devices = DAOManager.getDevicesOfUser(user.getUserId());
        }
        devices.add(0, null);
        adapter = new UserDeviceAdapter(devices);
    }

    @Override
    public void onDeepLinking(Intent data) {

    }

    @Override
    public void onNotification(Intent data) {

    }

    @Override
    public void onBindView() {
        fragment_user_detail_tv_device_count = (TextView) findViewById(R.id.fragment_user_detail_tv_device_count);
        fragment_user_detail_tv_id = (TextView) findViewById(R.id.fragment_user_detail_tv_id);
        fragment_user_detail_tv_name = (TextView) findViewById(R.id.fragment_user_detail_tv_name);
        fragment_user_detail_tv_gender = (TextView) findViewById(R.id.fragment_user_detail_tv_gender);
        fragment_user_detail_tv_dob = (TextView) findViewById(R.id.fragment_user_detail_tv_dob);
        fragment_user_detail_tv_career = (TextView) findViewById(R.id.fragment_user_detail_tv_career);
        fragment_user_detail_lv_device = (ListView) findViewById(R.id.fragment_user_detail_lv_device);
        fragment_user_detail_lv_device.setOnItemClickListener(this);
    }

    @Override
    public void onInitializeViewData() {
        fragment_user_detail_lv_device.setAdapter(adapter);
    }

    @Override
    public void onBaseResume() {
        if (user != null) {
            SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yyyy");
            fragment_user_detail_tv_id.setText(user.getUserId());
            fragment_user_detail_tv_name.setText(user.getName());
            fragment_user_detail_tv_career.setText(user.getCareer().toString());
            fragment_user_detail_tv_dob.setText(formater.format(user.getDOB()));
            fragment_user_detail_tv_gender.setText(user.getGender() == 1 ? getString(R.string.user_gender_male) : getString(R.string.user_gender_female));
            fragment_user_detail_tv_device_count.setText((devices.size() - 1) + "");
        }
    }

    @Override
    public void onBaseFree() {

    }

    @Override
    public void onSingleClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            // show add device dialog
            dialog = new UserDetailInsertDeviceDialog(getActiveActivity());
            dialog.setOnDismissListener(this);
            dialog.show();
        } else {
            // copy code to clip-board
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (this.dialog == dialog) {
            if (user != null) {
                devices.clear();
                devices.addAll(DAOManager.getDevicesOfUser(user.getUserId()));
            }
            devices.add(0, null);
            adapter.notifyDataSetChanged();
        }
    }
}
