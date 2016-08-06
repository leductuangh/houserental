package com.example.houserental.function.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.houserental.R;
import com.example.houserental.function.HouseRentalApplication;
import com.example.houserental.function.MainActivity;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.RoomDAO;
import com.example.houserental.function.model.UserDAO;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import core.base.BaseMultipleFragment;
import core.util.Utils;

/**
 * Created by leductuan on 3/13/16.
 */
public class UserEditScreen extends BaseMultipleFragment implements UserDOBPickerDialog.OnDOBPickerListener {

    public static final String TAG = UserEditScreen.class.getSimpleName();
    private static final String USER_KEY = "user_key";
    private UserDAO user;
    private Spinner fragment_user_edit_sn_room, fragment_user_edit_sn_career;
    private EditText fragment_user_edit_et_id, fragment_user_edit_et_name, fragment_user_edit_et_dob, fragment_user_edit_et_phone;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    private Button fragment_room_edit_bt_gender, fragment_room_edit_bt_registered;
    private Date dob;
    private int gender;
    private boolean registered;

    public static UserEditScreen getInstance(UserDAO user) {
        UserEditScreen screen = new UserEditScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER_KEY, user);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_edit, container, false);
    }

    @Override
    public void onBaseCreate() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            user = (UserDAO) bundle.getSerializable(USER_KEY);
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
        super.onBindView();
        fragment_user_edit_et_phone = (EditText) findViewById(R.id.fragment_user_edit_et_phone);
        fragment_room_edit_bt_gender = (Button) findViewById(R.id.fragment_room_edit_bt_gender);
        fragment_user_edit_sn_room = (Spinner) findViewById(R.id.fragment_user_edit_sn_room);
        fragment_user_edit_sn_career = (Spinner) findViewById(R.id.fragment_user_edit_sn_career);
        fragment_user_edit_et_id = (EditText) findViewById(R.id.fragment_user_edit_et_id);
        fragment_user_edit_et_name = (EditText) findViewById(R.id.fragment_user_edit_et_name);
        fragment_user_edit_et_dob = (EditText) findViewById(R.id.fragment_user_edit_et_dob);
        fragment_room_edit_bt_registered = (Button) findViewById(R.id.fragment_room_edit_bt_registered);
        fragment_user_edit_et_dob.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dob);
                    UserDOBPickerDialog dialog = new UserDOBPickerDialog(getActiveActivity(), calendar, UserEditScreen.this);
                    dialog.show();
                }
                return false;
            }
        });
        registerSingleAction(R.id.fragment_user_edit_bt_save,
                R.id.fragment_user_edit_bt_cancel,
                R.id.fragment_room_edit_bt_gender,
                R.id.fragment_room_edit_bt_registered);
    }

    @Override
    public void onInitializeViewData() {
        if (user != null) {
            fragment_room_edit_bt_registered.setText((registered = user.isRegistered()) ? getString(R.string.user_registered) : getString(R.string.user_not_registered));
            fragment_room_edit_bt_gender.setText((gender = user.getGender()) == 1 ? getString(R.string.user_gender_male) : getString(R.string.user_gender_female));
            fragment_user_edit_et_name.setText(user.getName());
            fragment_user_edit_et_id.setText(user.getIdentification());
            fragment_user_edit_et_phone.setText(user.getPhone());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dob = user.getDOB());
            fragment_user_edit_sn_room.setAdapter(new UserRoomAdapter(DAOManager.getAllRooms()));
            fragment_user_edit_sn_career.setAdapter(new UserCareerAdapter(Arrays.asList(UserDAO.Career.values())));
            fragment_user_edit_et_dob.setText(formatter.format(dob.getTime()));

            for (int i = 0; i < fragment_user_edit_sn_room.getCount(); ++i) {
                if (((RoomDAO) fragment_user_edit_sn_room.getItemAtPosition(i)).getId() == user.getRoom()) {
                    fragment_user_edit_sn_room.setSelection(i);
                    break;
                }
            }

            for (int i = 0; i < fragment_user_edit_sn_career.getCount(); ++i) {
                if (fragment_user_edit_sn_career.getItemAtPosition(i) == user.getCareer()) {
                    fragment_user_edit_sn_career.setSelection(i);
                    break;
                }
            }
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
        switch (v.getId()) {
            case R.id.fragment_room_edit_bt_registered:
                String register_text = HouseRentalApplication.getContext().getString(R.string.user_not_registered);
                if (!registered) {
                    registered = true;
                    register_text = HouseRentalApplication.getContext().getString(R.string.user_registered);
                } else {
                    registered = false;
                }
                fragment_room_edit_bt_registered.setText(register_text);
                break;
            case R.id.fragment_room_edit_bt_gender:
                gender = gender == 1 ? 0 : 1;
                fragment_room_edit_bt_gender.setText(gender == 1 ? getString(R.string.user_gender_male) : getString(R.string.user_gender_female));
                user.setGender(gender);
                break;
            case R.id.fragment_user_edit_bt_cancel:
                finish();
                break;
            case R.id.fragment_user_edit_bt_save:
                if (validated()) {
                    DAOManager.updateUser(user.getId(),
                            fragment_user_edit_et_id.getText().toString().trim(),
                            fragment_user_edit_et_name.getText().toString().trim(),
                            gender,
                            dob,
                            (UserDAO.Career) fragment_user_edit_sn_career.getSelectedItem(),
                            fragment_user_edit_et_phone.getText().toString().trim(),
                            registered,
                            ((RoomDAO) fragment_user_edit_sn_room.getSelectedItem()).getId());
                    ((MainActivity) getActiveActivity()).setScreenHeader(getString(R.string.user_detail_header) + " " + fragment_user_edit_et_name.getText().toString().trim());
                    showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title),
                            getString(R.string.room_alert_dialog_update_success),
                            getString((R.string.common_ok)), null, null);
                }
                break;
        }
    }

    private boolean validated() {
        if (Utils.isEmpty(fragment_user_edit_et_id.getText().toString().trim())) {
            showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.user_insert_id_error), getString(R.string.common_ok), null, null);
            return false;
        }

        if (Utils.isEmpty(fragment_user_edit_et_name.getText().toString().trim())) {
            showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.user_insert_name_error), getString(R.string.common_ok), null, null);
            return false;
        }
        return true;
    }

    @Override
    public void onDOBPicked(Calendar dob) {
        this.dob = dob.getTime();
        fragment_user_edit_et_dob.setText(formatter.format(dob.getTime()));

    }
}
