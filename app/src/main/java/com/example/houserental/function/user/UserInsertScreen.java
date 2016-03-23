package com.example.houserental.function.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.example.houserental.R;
import com.example.houserental.function.MainActivity;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.FloorDAO;
import com.example.houserental.function.model.RoomDAO;
import com.example.houserental.function.model.UserDAO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import core.base.BaseMultipleFragment;
import core.util.Utils;

/**
 * Created by Tyrael on 3/8/16.
 */
public class UserInsertScreen extends BaseMultipleFragment implements AdapterView.OnItemSelectedListener, DatePicker.OnDateChangedListener {

    public static final String TAG = UserInsertScreen.class.getSimpleName();
    private static final String ROOM_KEY = "room_key";
    private RoomDAO room;
    private List<RoomDAO> rooms;
    private List<FloorDAO> floors;
    private List<UserDAO.Career> careers;
    private Spinner fragment_user_insert_sn_floor, fragment_user_insert_sn_room, fragment_user_insert_sn_career;
    private EditText fragment_user_insert_et_id, fragment_user_insert_et_name;
    private ToggleButton fragment_user_insert_tg_gender;
    private Long room_id;
    private String user_id;
    private String user_name;
    private int gender;
    private Date dob;
    private UserDAO.Career career;
    private DatePicker fragment_user_insert_dp_dob;


    public static UserInsertScreen getInstance(RoomDAO room) {
        UserInsertScreen screen = new UserInsertScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ROOM_KEY, room);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_insert, container, false);
    }

    @Override
    public void onBaseCreate() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            room = (RoomDAO) bundle.getSerializable(ROOM_KEY);
            if (room != null)
                room_id = room.getId();
        }
        rooms = DAOManager.getAllRooms();
        rooms.add(0, null);
        floors = DAOManager.getAllFloors();
        floors.add(0, null);
        careers = new ArrayList<>(Arrays.asList(UserDAO.Career.values()));
        careers.add(0, null);
    }

    @Override
    public void onDeepLinking(Intent data) {

    }

    @Override
    public void onNotification(Intent data) {

    }

    @Override
    public void onBindView() {
        Calendar today = Calendar.getInstance();
        fragment_user_insert_dp_dob = (DatePicker) findViewById(R.id.fragment_user_insert_dp_dob);
        fragment_user_insert_sn_floor = (Spinner) findViewById(R.id.fragment_user_insert_sn_floor);
        fragment_user_insert_sn_room = (Spinner) findViewById(R.id.fragment_user_insert_sn_room);
        fragment_user_insert_sn_career = (Spinner) findViewById(R.id.fragment_user_insert_sn_career);
        fragment_user_insert_et_id = (EditText) findViewById(R.id.fragment_user_insert_et_id);
        fragment_user_insert_et_name = (EditText) findViewById(R.id.fragment_user_insert_et_name);
        fragment_user_insert_tg_gender = (ToggleButton) findViewById(R.id.fragment_user_insert_tg_gender);
        fragment_user_insert_sn_floor.setOnItemSelectedListener(this);
        fragment_user_insert_dp_dob.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), this);

        findViewById(R.id.fragment_user_insert_bt_save);
        findViewById(R.id.fragment_user_insert_bt_cancel);
    }

    @Override
    public void onInitializeViewData() {
        fragment_user_insert_sn_floor.setAdapter(new UserFloorAdapter(floors));
        fragment_user_insert_sn_room.setAdapter(new UserRoomAdapter(rooms, true));
        fragment_user_insert_sn_career.setAdapter(new UserCareerAdapter(careers, true));

        if (room != null) {
            for (int i = 1; i < fragment_user_insert_sn_floor.getCount(); ++i) {
                if (room.getFloor() == ((FloorDAO) fragment_user_insert_sn_floor.getItemAtPosition(i)).getId()) {
                    final int j = i;
                    fragment_user_insert_sn_floor.post(new Runnable() {
                        @Override
                        public void run() {
                            fragment_user_insert_sn_floor.setSelection(j);
                            fragment_user_insert_sn_floor.setEnabled(false);
                        }
                    });
                    break;
                }
            }

            for (int i = 1; i < fragment_user_insert_sn_room.getCount(); ++i) {
                if (room.getId() == (((RoomDAO) fragment_user_insert_sn_room.getItemAtPosition(i)).getId())) {
                    final int j = i;
                    fragment_user_insert_sn_room.post(new Runnable() {
                        @Override
                        public void run() {
                            fragment_user_insert_sn_room.setSelection(j);
                            fragment_user_insert_sn_room.setEnabled(false);
                        }
                    });
                    break;
                }
            }
        } else {
            fragment_user_insert_sn_room.setEnabled(false);
        }
        fragment_user_insert_sn_room.setOnItemSelectedListener(this);
        fragment_user_insert_sn_career.setOnItemSelectedListener(this);
    }

    @Override
    public void onBaseResume() {
        String room_name = "";
        if (room != null)
            room_name = room.getName();
        ((MainActivity) getActiveActivity()).setScreenHeader(getString(R.string.main_header_user_insert) + " " + room_name);
    }

    @Override
    public void onBaseFree() {

    }

    @Override
    public void onSingleClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_user_insert_bt_cancel:
                finish();
                break;
            case R.id.fragment_user_insert_bt_save:
                if (validated()) {
                    Long id = DAOManager.addUser(user_id, user_name, gender, dob, career, room_id);
                    replaceFragment(R.id.activity_main_container, UserDetailScreen.getInstance(DAOManager.getUser(id)), UserDetailScreen.TAG, false);
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.fragment_user_insert_sn_floor:
                if (position == 0) {
                    fragment_user_insert_sn_room.setSelection(0);
                    fragment_user_insert_sn_room.setEnabled(false);
                } else {
                    if (room == null) {
                        FloorDAO floor = (FloorDAO) parent.getSelectedItem();
                        refreshRoomList(floor.getId());
                        fragment_user_insert_sn_room.setSelection(0);
                        fragment_user_insert_sn_room.setEnabled(true);
                    }
                }
                break;
            case R.id.fragment_user_insert_sn_room:
                if (position != 0) {
                    room_id = ((RoomDAO) parent.getSelectedItem()).getId();
                } else {
                    room_id = Long.valueOf(-1);
                }
                break;
            case R.id.fragment_user_insert_sn_career:
                if (position == 0) {
                    career = null;
                } else {
                    career = (UserDAO.Career) parent.getSelectedItem();
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private boolean validated() {
        if (fragment_user_insert_sn_room.getSelectedItemPosition() == 0 || room_id == -1) {
            showAlertDialog(getActiveActivity(), -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.user_choose_room_error), getString(R.string.common_ok), null);
            return false;
        }

        if (Utils.isEmpty(fragment_user_insert_et_id.getText().toString().trim())) {
            showAlertDialog(getActiveActivity(), -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.user_insert_id_error), getString(R.string.common_ok), null);
            return false;
        }

        if (Utils.isEmpty(fragment_user_insert_et_name.getText().toString().trim())) {
            showAlertDialog(getActiveActivity(), -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.user_insert_name_error), getString(R.string.common_ok), null);
            return false;
        }

        if (dob == null) {
            showAlertDialog(getActiveActivity(), -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.user_insert_dob_error), getString(R.string.common_ok), null);
            return false;
        }

        if (fragment_user_insert_sn_career.getSelectedItemPosition() == 0 || career == null) {
            showAlertDialog(getActiveActivity(), -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.user_insert_career_error), getString(R.string.common_ok), null);
            return false;
        }
        user_name = fragment_user_insert_et_name.getText().toString().trim();
        user_id = fragment_user_insert_et_id.getText().toString().trim();
        gender = fragment_user_insert_tg_gender.isChecked() ? 1 : 0;

        return true;
    }

    private void refreshRoomList(Long floor) {
        if (rooms != null)
            rooms.clear();
        if (floor == -1)
            rooms.addAll(DAOManager.getAllRooms());
        else
            rooms.addAll(DAOManager.getRoomsOfFloor(floor));
        rooms.add(0, null);
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        dob = calendar.getTime();
    }
}
