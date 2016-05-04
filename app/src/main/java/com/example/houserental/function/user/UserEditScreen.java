package com.example.houserental.function.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.example.houserental.R;
import com.example.houserental.function.MainActivity;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.RoomDAO;
import com.example.houserental.function.model.UserDAO;

import java.util.Arrays;
import java.util.Calendar;

import core.base.BaseMultipleFragment;
import core.util.Utils;

/**
 * Created by leductuan on 3/13/16.
 */
public class UserEditScreen extends BaseMultipleFragment {

    public static final String TAG = UserEditScreen.class.getSimpleName();
    private static final String USER_KEY = "user_key";
    private UserDAO user;
    private Spinner fragment_user_edit_sn_room, fragment_user_edit_sn_career;
    private ToggleButton fragment_user_edit_tg_gender;
    private DatePicker fragment_user_edit_dp_dob;
    private EditText fragment_user_edit_et_id, fragment_user_edit_et_name;

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
        fragment_user_edit_sn_room = (Spinner) findViewById(R.id.fragment_user_edit_sn_room);
        fragment_user_edit_sn_career = (Spinner) findViewById(R.id.fragment_user_edit_sn_career);
        fragment_user_edit_tg_gender = (ToggleButton) findViewById(R.id.fragment_user_edit_tg_gender);
        fragment_user_edit_dp_dob = (DatePicker) findViewById(R.id.fragment_user_edit_dp_dob);
        fragment_user_edit_et_id = (EditText) findViewById(R.id.fragment_user_edit_et_id);
        fragment_user_edit_et_name = (EditText) findViewById(R.id.fragment_user_edit_et_name);
        findViewById(R.id.fragment_user_edit_bt_cancel);
        findViewById(R.id.fragment_user_edit_bt_save);
    }

    @Override
    public void onInitializeViewData() {
        if (user != null) {
            fragment_user_edit_et_name.setText(user.getName());
            fragment_user_edit_et_id.setText(user.getIdentification());
            fragment_user_edit_tg_gender.setChecked(user.getGender() == 1);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(user.getDOB());
            fragment_user_edit_dp_dob.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            fragment_user_edit_sn_room.setAdapter(new UserRoomAdapter(DAOManager.getAllRentedRooms(), false));
            fragment_user_edit_sn_career.setAdapter(new UserCareerAdapter(Arrays.asList(UserDAO.Career.values()), false));

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
            case R.id.fragment_user_edit_bt_cancel:
                finish();
                break;
            case R.id.fragment_user_edit_bt_save:
                if (validated()) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(fragment_user_edit_dp_dob.getYear(), fragment_user_edit_dp_dob.getMonth(), fragment_user_edit_dp_dob.getDayOfMonth());
                    DAOManager.updateUser(user.getId(),
                            fragment_user_edit_et_id.getText().toString().trim(),
                            fragment_user_edit_et_name.getText().toString().trim(),
                            fragment_user_edit_tg_gender.isChecked() ? 1 : 0,
                            calendar.getTime(),
                            (UserDAO.Career) fragment_user_edit_sn_career.getSelectedItem(),
                            ((RoomDAO) fragment_user_edit_sn_room.getSelectedItem()).getId());
                    ((MainActivity) getActiveActivity()).setScreenHeader(getString(R.string.user_detail_header) + " " + fragment_user_edit_et_name.getText().toString().trim());
                    showAlertDialog(getActiveActivity(), -1, -1, getString(R.string.application_alert_dialog_title),
                            getString(R.string.room_alert_dialog_update_success),
                            getString((R.string.common_ok)), null, null);
                }
                break;
        }
    }

    private boolean validated() {
        if (Utils.isEmpty(fragment_user_edit_et_id.getText().toString().trim())) {
            showAlertDialog(getActiveActivity(), -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.user_insert_id_error), getString(R.string.common_ok), null, null);
            return false;
        }

        if (Utils.isEmpty(fragment_user_edit_et_name.getText().toString().trim())) {
            showAlertDialog(getActiveActivity(), -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.user_insert_name_error), getString(R.string.common_ok), null, null);
            return false;
        }
        return true;
    }
}
