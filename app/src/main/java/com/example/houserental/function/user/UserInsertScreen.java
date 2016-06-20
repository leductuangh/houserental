package com.example.houserental.function.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.houserental.R;
import com.example.houserental.function.HouseRentalApplication;
import com.example.houserental.function.MainActivity;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.FloorDAO;
import com.example.houserental.function.model.RoomDAO;
import com.example.houserental.function.model.UserDAO;

import java.text.SimpleDateFormat;
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
public class UserInsertScreen extends BaseMultipleFragment implements UserDOBPickerDialog.OnDOBPickerListener, AdapterView.OnItemSelectedListener {

    public static final String TAG = UserInsertScreen.class.getSimpleName();
    private static final String ROOM_KEY = "room_key";
    private RoomDAO room;
    private RoomDAO predefined_room;
    private List<RoomDAO> rooms;
    private List<FloorDAO> floors;
    private List<UserDAO.Career> careers;
    private Spinner fragment_user_insert_sn_floor, fragment_user_insert_sn_room, fragment_user_insert_sn_career;
    private EditText fragment_user_insert_et_id, fragment_user_insert_et_name, fragment_user_insert_et_phone, fragment_user_insert_et_dob;
    private TextView fragment_room_insert_tv_gender, fragment_room_insert_tv_registered;
    private String user_id;
    private String user_name;
    private boolean registered;
    private int gender = 0;
    private Date dob;
    private UserDAO.Career career;
    private String phone;
    private UserRoomAdapter room_adapter;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    private boolean isInitialized = false;


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
            room = predefined_room = (RoomDAO) bundle.getSerializable(ROOM_KEY);
        }
        if (room != null) {
            rooms = DAOManager.getRoomsOfFloor(room.getFloor());
        } else {
            rooms = new ArrayList<>();
        }
        floors = DAOManager.getAllFloors();
        careers = new ArrayList<>(Arrays.asList(UserDAO.Career.values()));
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
        fragment_room_insert_tv_registered = (TextView) findViewById(R.id.fragment_room_insert_tv_registered);
        fragment_user_insert_et_dob = (EditText) findViewById(R.id.fragment_user_insert_et_dob);
        fragment_user_insert_sn_floor = (Spinner) findViewById(R.id.fragment_user_insert_sn_floor);
        fragment_user_insert_sn_room = (Spinner) findViewById(R.id.fragment_user_insert_sn_room);
        fragment_user_insert_sn_career = (Spinner) findViewById(R.id.fragment_user_insert_sn_career);
        fragment_user_insert_et_id = (EditText) findViewById(R.id.fragment_user_insert_et_id);
        fragment_user_insert_et_name = (EditText) findViewById(R.id.fragment_user_insert_et_name);
        fragment_room_insert_tv_gender = (TextView) findViewById(R.id.fragment_room_insert_tv_gender);
        fragment_user_insert_et_phone = (EditText) findViewById(R.id.fragment_user_insert_et_phone);
        fragment_user_insert_et_dob.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    showDOBPickerDialog();
                }
                return false;
            }
        });
        registerSingleAction(R.id.fragment_user_insert_bt_save,
                R.id.fragment_user_insert_bt_cancel,
                R.id.fragment_room_insert_tv_gender,
                R.id.fragment_room_insert_tv_registered);
    }

    @Override
    public void onInitializeViewData() {
        fragment_room_insert_tv_registered.setText(getString(R.string.user_not_registered));
        fragment_room_insert_tv_gender.setText(HouseRentalApplication.getContext().getString(R.string.user_gender_female));
        fragment_user_insert_sn_floor.setAdapter(new UserFloorAdapter(floors));
        fragment_user_insert_sn_room.setAdapter(room_adapter = new UserRoomAdapter(rooms));
        fragment_user_insert_sn_career.setAdapter(new UserCareerAdapter(careers));
        fragment_user_insert_sn_career.setSelection(fragment_user_insert_sn_career.getAdapter().getCount());
        if (room != null) {
            for (int i = 0; i < fragment_user_insert_sn_floor.getCount(); ++i) {
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

            for (int i = 0; i < fragment_user_insert_sn_room.getCount(); ++i) {
                if (room.getId() == (((RoomDAO) fragment_user_insert_sn_room.getItemAtPosition(i)).getId())) {
                    final int j = i;
                    fragment_user_insert_sn_room.post(new Runnable() {
                        @Override
                        public void run() {
                            fragment_user_insert_sn_room.setSelection(j, false);
                            fragment_user_insert_sn_room.setEnabled(false);
                        }
                    });
                    break;
                }
            }
        } else {
            fragment_user_insert_sn_floor.setSelection(fragment_user_insert_sn_floor.getAdapter().getCount());
            fragment_user_insert_sn_room.setSelection(fragment_user_insert_sn_room.getAdapter().getCount());
            fragment_user_insert_sn_room.setEnabled(false);
        }
        fragment_user_insert_sn_floor.setOnItemSelectedListener(this);
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
            case R.id.fragment_room_insert_tv_registered:
                String register_text = HouseRentalApplication.getContext().getString(R.string.user_not_registered);
                if (!registered) {
                    registered = true;
                    register_text = HouseRentalApplication.getContext().getString(R.string.user_registered);
                } else {
                    registered = false;
                }
                fragment_room_insert_tv_registered.setText(register_text);
                break;
            case R.id.fragment_room_insert_tv_gender:
                String gender_text = HouseRentalApplication.getContext().getString(R.string.user_gender_female);
                if (gender == 0) {
                    gender = 1;
                    gender_text = HouseRentalApplication.getContext().getString(R.string.user_gender_male);
                } else {
                    gender = 0;
                }
                fragment_room_insert_tv_gender.setText(gender_text);
                break;
            case R.id.fragment_user_insert_bt_cancel:
                finish();
                break;
            case R.id.fragment_user_insert_bt_save:
                if (validated()) {
                    Long id = DAOManager.addUser(user_id, user_name, gender, dob, career, phone, room.getId(), registered);
                    replaceFragment(R.id.activity_main_container, UserDetailScreen.getInstance(DAOManager.getUser(id)), UserDetailScreen.TAG, false);
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.fragment_user_insert_sn_floor:
                if (!isInitialized) {
                    isInitialized = true;
                    return;
                }
                FloorDAO floor = (FloorDAO) parent.getSelectedItem();
                if (rooms != null)
                    rooms.clear();
                rooms.addAll(DAOManager.getRoomsOfFloor(floor.getId()));
                room_adapter.notifyDataSetChanged();
                if (predefined_room == null) {
                    fragment_user_insert_sn_room.setEnabled(true);
                    fragment_user_insert_sn_room.post(new Runnable() {
                        @Override
                        public void run() {
                            fragment_user_insert_sn_room.setSelection(room_adapter.getCount(), false);
                        }
                    });
                }

                break;
            case R.id.fragment_user_insert_sn_room:
                room = (RoomDAO) parent.getSelectedItem();
                break;
            case R.id.fragment_user_insert_sn_career:
                career = (UserDAO.Career) parent.getSelectedItem();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private boolean validated() {
        if (room == null) {
            showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.user_choose_room_error), getString(R.string.common_ok), null, null);
            return false;
        } else {
            if (room.getId() == null) {
                showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.user_choose_room_error), getString(R.string.common_ok), null, null);
                return false;
            }
        }

        if (Utils.isEmpty(fragment_user_insert_et_id.getText().toString().trim())) {
            showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.user_insert_id_error), getString(R.string.common_ok), null, null);
            return false;
        }

        if (Utils.isEmpty(fragment_user_insert_et_name.getText().toString().trim())) {
            showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.user_insert_name_error), getString(R.string.common_ok), null, null);
            return false;
        }

        if (dob == null) {
            showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.user_insert_dob_error), getString(R.string.common_ok), null, null);
            return false;
        }

        if (career == null || career == UserDAO.Career.CHOOSE) {
            showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.user_insert_career_error), getString(R.string.common_ok), null, null);
            return false;
        }
        phone = fragment_user_insert_et_phone.getText().toString().trim();
        user_name = fragment_user_insert_et_name.getText().toString().trim();
        user_id = fragment_user_insert_et_id.getText().toString().trim();
        return true;
    }

    @Override
    public void onDOBPicked(Calendar dob) {
        this.dob = dob.getTime();
        fragment_user_insert_et_dob.setText(formatter.format(dob.getTime()));
    }

    private void showDOBPickerDialog() {
        UserDOBPickerDialog dialog = new UserDOBPickerDialog(getActiveActivity(), this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
