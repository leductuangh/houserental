package com.example.houserental.function.user;

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
import com.example.houserental.function.model.UserDAO;

import java.text.SimpleDateFormat;

import core.base.BaseMultipleFragment;
import core.dialog.GeneralDialog;
import core.util.Constant;

/**
 * Created by leductuan on 3/6/16.
 */
public class UserDetailScreen extends BaseMultipleFragment implements GeneralDialog.DecisionListener {

    public static final String TAG = UserDetailScreen.class.getSimpleName();
    private static final String USER_KEY = "user_key";
    private UserDAO user;
    private TextView fragment_user_detail_tv_registered, fragment_user_detail_tv_room, fragment_user_detail_tv_id, fragment_user_detail_tv_name, fragment_user_detail_tv_dob, fragment_user_detail_tv_career, fragment_user_detail_tv_gender, fragment_user_detail_tv_phone;


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
                showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.application_alert_dialog_error_general), getString(R.string.common_ok), null, null);
                return;
            }
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
        fragment_user_detail_tv_registered = (TextView) findViewById(R.id.fragment_user_detail_tv_registered);
        fragment_user_detail_tv_room = (TextView) findViewById(R.id.fragment_user_detail_tv_room);
        fragment_user_detail_tv_phone = (TextView) findViewById(R.id.fragment_user_detail_tv_phone);
        fragment_user_detail_tv_id = (TextView) findViewById(R.id.fragment_user_detail_tv_id);
        fragment_user_detail_tv_name = (TextView) findViewById(R.id.fragment_user_detail_tv_name);
        fragment_user_detail_tv_gender = (TextView) findViewById(R.id.fragment_user_detail_tv_gender);
        fragment_user_detail_tv_dob = (TextView) findViewById(R.id.fragment_user_detail_tv_dob);
        fragment_user_detail_tv_career = (TextView) findViewById(R.id.fragment_user_detail_tv_career);
        findViewById(R.id.fragment_user_detail_bt_manage_device);
        findViewById(R.id.fragment_user_detail_bt_edit);
        findViewById(R.id.fragment_user_detail_bt_delete);
    }

    @Override
    public void onInitializeViewData() {

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
            fragment_user_detail_tv_registered.setText(user.isRegistered() ? getString(R.string.user_registered) : getString(R.string.user_not_registered));
            fragment_user_detail_tv_phone.setText(user.getPhone());
            fragment_user_detail_tv_room.setText(DAOManager.getRoomName(user.getRoom()));
            ((MainActivity) getActiveActivity()).setScreenHeader(getString(com.example.houserental.R.string.user_detail_header) + " " + user.getName());
        }
    }

    @Override
    public void onBaseFree() {

    }

    @Override
    public void onSingleClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_user_detail_bt_manage_device:
                addFragment(R.id.activity_main_container, UserDeviceListScreen.getInstance(user), UserDeviceListScreen.TAG);
                break;
            case R.id.fragment_user_detail_bt_edit:
                addFragment(R.id.activity_main_container, UserEditScreen.getInstance(user), UserEditScreen.TAG);
                break;
            case R.id.fragment_user_detail_bt_delete:
                showDecisionDialog(getActiveActivity(), Constant.DELETE_USER_DIALOG, getGeneralDialogLayoutResource(), -1, getString(com.example.houserental.R.string.application_alert_dialog_title), getString(com.example.houserental.R.string.delete_user_dialog_message), getString(com.example.houserental.R.string.common_ok), getString(com.example.houserental.R.string.common_cancel), null, null, this);
                break;
        }
    }

    @Override
    public void onAgreed(int id, Object onWhat) {
        if (id == Constant.DELETE_USER_DIALOG) {
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
