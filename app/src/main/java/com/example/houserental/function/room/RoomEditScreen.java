package com.example.houserental.function.room;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.houserental.R;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.FloorDAO;
import com.example.houserental.function.model.RoomDAO;
import com.example.houserental.function.model.RoomTypeDAO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import core.base.BaseMultipleFragment;
import core.util.Utils;

/**
 * Created by Tyrael on 3/11/16.
 */
public class RoomEditScreen extends BaseMultipleFragment implements CompoundButton.OnCheckedChangeListener {

    public static final String TAG = RoomEditScreen.class.getSimpleName();
    private static final String ROOM_KEY = "room_key";
    private RoomDAO room;
    private Spinner fragment_room_edit_sn_floor, fragment_room_edit_sn_type;
    private EditText fragment_room_edit_et_name, fragment_room_edit_et_area, fragment_room_edit_et_water, fragment_room_edit_et_electric;
    private ToggleButton fragment_room_edit_tg_rented;
    private TextView fragment_room_edit_tv_rented_date;

    public static RoomEditScreen getInstance(RoomDAO room) {
        RoomEditScreen screen = new RoomEditScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ROOM_KEY, room);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_room_edit, container, false);
    }

    @Override
    public void onBaseCreate() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            room = (RoomDAO) bundle.getSerializable(ROOM_KEY);
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
        fragment_room_edit_sn_floor = (Spinner) findViewById(R.id.fragment_room_edit_sn_floor);
        fragment_room_edit_sn_type = (Spinner) findViewById(R.id.fragment_room_edit_sn_type);
        fragment_room_edit_et_name = (EditText) findViewById(R.id.fragment_room_edit_et_name);
        fragment_room_edit_et_area = (EditText) findViewById(R.id.fragment_room_edit_et_area);
        fragment_room_edit_et_water = (EditText) findViewById(R.id.fragment_room_edit_et_water);
        fragment_room_edit_et_electric = (EditText) findViewById(R.id.fragment_room_edit_et_electric);
        fragment_room_edit_tg_rented = (ToggleButton) findViewById(R.id.fragment_room_edit_tg_rented);
        fragment_room_edit_tv_rented_date = (TextView) findViewById(R.id.fragment_room_edit_tv_rented_date);
        fragment_room_edit_tg_rented.setOnCheckedChangeListener(this);
        findViewById(R.id.fragment_room_edit_bt_save);
        findViewById(R.id.fragment_room_edit_bt_cancel);
    }

    @Override
    public void onInitializeViewData() {
        if (room != null) {

            fragment_room_edit_et_name.setText(room.getName());
            fragment_room_edit_et_area.setText(room.getArea() + "");
            fragment_room_edit_et_electric.setText(room.getElectricNumber() + "");
            fragment_room_edit_et_water.setText(room.getWaterNumber() + "");
            fragment_room_edit_tg_rented.setChecked(room.isRented());

            List<FloorDAO> floors;
            List<RoomTypeDAO> types = DAOManager.getAllRoomTypes();
            types.add(0, null);
            fragment_room_edit_sn_floor.setAdapter(new RoomFloorAdapter(floors = DAOManager.getAllFloors(), false));
            fragment_room_edit_sn_type.setAdapter(new RoomTypeAdapter(types));

            for (int i = 0; i < floors.size(); ++i) {
                if (floors.get(i).getId() == room.getFloor()) {
                    fragment_room_edit_sn_floor.setSelection(i);
                    break;
                }
            }

            for (int i = 1; i < types.size(); ++i) {
                if (types.get(i).equals(room.getType())) {
                    fragment_room_edit_sn_type.setSelection(i);
                    break;
                }
            }

        } else {
            finish();
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
            case R.id.fragment_room_edit_bt_cancel:
                finish();
                break;
            case R.id.fragment_room_edit_bt_save:
                if (validated()) {
                    boolean isRented = fragment_room_edit_tg_rented.isChecked();
                    DAOManager.updateRoom(room.getId(),
                            fragment_room_edit_et_name.getText().toString().trim(),
                            Integer.parseInt(fragment_room_edit_et_area.getText().toString().trim()),
                            ((RoomTypeDAO) fragment_room_edit_sn_type.getSelectedItem()).getId(),
                            isRented, isRented ? new Date() : null,
                            Integer.parseInt(fragment_room_edit_et_electric.getText().toString().trim()),
                            Integer.parseInt(fragment_room_edit_et_water.getText().toString().trim()),
                            ((FloorDAO) fragment_room_edit_sn_floor.getSelectedItem()).getId());
                    showAlertDialog(getActiveActivity(), -1, -1, getString(R.string.application_alert_dialog_title),
                            getString(R.string.room_alert_dialog_update_success),
                            getString((R.string.common_ok)), null);
                }
                break;
        }
    }

    private boolean validated() {

        if (fragment_room_edit_sn_floor == null) {
            showAlertDialog(getActiveActivity(), -1, -1,
                    getString(R.string.application_alert_dialog_title),
                    getString(R.string.room_choose_floor_error), getString(R.string.common_ok), null);
            return false;
        }

        if (fragment_room_edit_et_name != null && Utils.isEmpty(fragment_room_edit_et_name.getText().toString())) {
            showAlertDialog(getActiveActivity(), -1, -1,
                    getString(R.string.application_alert_dialog_title),
                    getString(R.string.room_insert_name_error), getString(R.string.common_ok), null);
            return false;
        }

        if (fragment_room_edit_et_area != null && Utils.isEmpty(fragment_room_edit_et_area.getText().toString())) {
            showAlertDialog(getActiveActivity(), -1, -1,
                    getString(R.string.application_alert_dialog_title),
                    getString(R.string.room_insert_area_error), getString(R.string.common_ok), null);
            return false;
        }

        if (fragment_room_edit_et_electric != null && Utils.isEmpty(fragment_room_edit_et_electric.getText().toString())) {
            showAlertDialog(getActiveActivity(), -1, -1,
                    getString(R.string.application_alert_dialog_title),
                    getString(R.string.room_insert_electric_error), getString(R.string.common_ok), null);
            return false;
        }

        if (fragment_room_edit_et_water != null && Utils.isEmpty(fragment_room_edit_et_water.getText().toString())) {
            showAlertDialog(getActiveActivity(), -1, -1,
                    getString(R.string.application_alert_dialog_title),
                    getString(R.string.room_insert_water_error), getString(R.string.common_ok), null);
            return false;
        }

        if (fragment_room_edit_sn_type.getSelectedItem() == null) {
            showAlertDialog(getActiveActivity(), -1, -1,
                    getString(R.string.application_alert_dialog_title),
                    getString(R.string.room_choose_type_error), getString(R.string.common_ok), null);
            return false;
        }

        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yyyy");
            fragment_room_edit_tv_rented_date.setText(getString(R.string.room_rented_date_title) + " " + formater.format(new Date()));
        } else {
            fragment_room_edit_tv_rented_date.setText("");
        }
    }
}
