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
import android.widget.Toast;

import com.example.houserental.R;
import com.example.houserental.function.MainActivity;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.DeviceDAO;
import com.example.houserental.function.model.UserDAO;

import java.text.SimpleDateFormat;
import java.util.List;

import core.base.BaseMultipleFragment;
import core.dialog.GeneralDialog;
import core.util.ClipboarbWrapper;
import core.util.Constant;

/**
 * Created by leductuan on 3/6/16.
 */
public class UserDetailScreen extends BaseMultipleFragment implements AdapterView.OnItemClickListener, DialogInterface.OnDismissListener, AdapterView.OnItemLongClickListener, GeneralDialog.DecisionListener {

    public static final String TAG = UserDetailScreen.class.getSimpleName();
    private static final String USER_KEY = "user_key";
    private UserDAO user;
    private List<DeviceDAO> devices;
    private TextView fragment_user_detail_tv_device_count, fragment_user_detail_tv_id, fragment_user_detail_tv_name, fragment_user_detail_tv_dob, fragment_user_detail_tv_career, fragment_user_detail_tv_gender, fragment_user_detail_tv_phone;
    private ListView fragment_user_detail_lv_device;
    private UserDeviceAdapter adapter;
    private String deleted_device;
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
        return inflater.inflate(com.example.houserental.R.layout.fragment_user_detail, container, false);
    }

    @Override
    public void onBaseCreate() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            user = (UserDAO) bundle.getSerializable(USER_KEY);
            if (user == null) {
                showAlertDialog(getActiveActivity(), -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.application_alert_dialog_error_general), getString(R.string.common_ok), null, null);
                return;
            }
        }
        devices = DAOManager.getDevicesOfUser(user.getId());
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
        fragment_user_detail_tv_phone = (TextView) findViewById(R.id.fragment_user_detail_tv_phone);
        fragment_user_detail_tv_device_count = (TextView) findViewById(com.example.houserental.R.id.fragment_user_detail_tv_device_count);
        fragment_user_detail_tv_id = (TextView) findViewById(com.example.houserental.R.id.fragment_user_detail_tv_id);
        fragment_user_detail_tv_name = (TextView) findViewById(com.example.houserental.R.id.fragment_user_detail_tv_name);
        fragment_user_detail_tv_gender = (TextView) findViewById(com.example.houserental.R.id.fragment_user_detail_tv_gender);
        fragment_user_detail_tv_dob = (TextView) findViewById(com.example.houserental.R.id.fragment_user_detail_tv_dob);
        fragment_user_detail_tv_career = (TextView) findViewById(com.example.houserental.R.id.fragment_user_detail_tv_career);
        fragment_user_detail_lv_device = (ListView) findViewById(com.example.houserental.R.id.fragment_user_detail_lv_device);
        fragment_user_detail_lv_device.setOnItemClickListener(this);
        fragment_user_detail_lv_device.setOnItemLongClickListener(this);

        findViewById(com.example.houserental.R.id.fragment_user_detail_bt_edit);
        findViewById(com.example.houserental.R.id.fragment_user_detail_bt_delete);
    }

    @Override
    public void onInitializeViewData() {
        fragment_user_detail_lv_device.setAdapter(adapter);
    }

    @Override
    public void onBaseResume() {
        if (user != null) {
            SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yyyy");
            fragment_user_detail_tv_id.setText(user.getIdentification());
            fragment_user_detail_tv_name.setText(user.getName());
            fragment_user_detail_tv_career.setText(user.getCareer().toString());
            fragment_user_detail_tv_dob.setText(formater.format(user.getDOB()));
            fragment_user_detail_tv_gender.setText(user.getGender() == 1 ? getString(com.example.houserental.R.string.user_gender_male) : getString(com.example.houserental.R.string.user_gender_female));
            fragment_user_detail_tv_device_count.setText((devices.size() - 1) + "");
            fragment_user_detail_tv_phone.setText(user.getPhone());
            ((MainActivity) getActiveActivity()).setScreenHeader(getString(com.example.houserental.R.string.user_detail_header) + " " + user.getName());
        }
    }

    @Override
    public void onBaseFree() {

    }

    @Override
    public void onSingleClick(View v) {
        switch (v.getId()) {
            case com.example.houserental.R.id.fragment_user_detail_bt_edit:
                addFragment(com.example.houserental.R.id.activity_main_container, UserEditScreen.getInstance(user), UserEditScreen.TAG);
                break;
            case com.example.houserental.R.id.fragment_user_detail_bt_delete:
                showDecisionDialog(getActiveActivity(), Constant.DELETE_USER_DIALOG, -1, getString(com.example.houserental.R.string.application_alert_dialog_title), getString(com.example.houserental.R.string.delete_user_dialog_message), getString(com.example.houserental.R.string.common_ok), getString(com.example.houserental.R.string.common_cancel), null, null, this);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            // show add device dialog
            dialog = new UserDetailInsertDeviceDialog(getActiveActivity(), user.getId());
            dialog.setOnDismissListener(this);
            dialog.show();
        } else {
            ClipboarbWrapper.copyToClipboard(getActiveActivity(), ((DeviceDAO) parent.getItemAtPosition(position)).getMAC());
            Toast.makeText(getActiveActivity(), ClipboarbWrapper.readFromClipboard(getActiveActivity()), Toast.LENGTH_SHORT).show();
            // copy code to clip-board
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (this.dialog == dialog) {
            refreshDeviceList();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0)
            return true;
        deleted_device = ((DeviceDAO) parent.getItemAtPosition(position)).getMAC();
        showDecisionDialog(getActiveActivity(), Constant.DELETE_DEVICE_DIALOG, -1, getString(com.example.houserental.R.string.application_alert_dialog_title), String.format(getString(com.example.houserental.R.string.user_device_delete_message), deleted_device), getString(com.example.houserental.R.string.common_ok), getString(com.example.houserental.R.string.common_cancel), null, null, this);
        return true;
    }

    private void refreshDeviceList() {
        if (user != null) {
            devices.clear();
            devices.addAll(DAOManager.getDevicesOfUser(user.getId()));
        }
        devices.add(0, null);
        adapter.notifyDataSetChanged();
        fragment_user_detail_tv_device_count.setText((devices.size() - 1) + "");
    }

    @Override
    public void onAgreed(int id, Object onWhat) {
        if (id == Constant.DELETE_DEVICE_DIALOG) {
            DAOManager.deleteDevice(deleted_device);
            refreshDeviceList();
        } else if (id == Constant.DELETE_USER_DIALOG) {
            if (user != null) {
                DAOManager.deleteUser(user.getId());
            }
            finish();
        }
    }

    @Override
    public void onDisAgreed(int id, Object onWhat) {

    }

    @Override
    public void onNeutral(int id, Object onWhat) {

    }
}
