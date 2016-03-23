package com.example.houserental.function.room;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.houserental.R;
import com.example.houserental.function.MainActivity;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.FloorDAO;
import com.example.houserental.function.model.RoomTypeDAO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import core.base.BaseMultipleFragment;
import core.util.Utils;

/**
 * Created by leductuan on 3/6/16.
 */
public class RoomInsertScreen extends BaseMultipleFragment implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    public static final String TAG = RoomInsertScreen.class.getSimpleName();
    private static final String FLOOR_KEY = "floor_key";
    private List<FloorDAO> floors;
    private List<RoomTypeDAO> types;

    private Spinner fragment_room_insert_sn_floor, fragment_room_insert_sn_type;
    private ToggleButton fragment_room_insert_tg_rented;
    private EditText fragment_room_insert_et_area, fragment_room_insert_et_name, fragment_room_insert_et_electric, fragment_room_insert_et_water;
    private TextView fragment_room_insert_tv_rented_date;
    private String data_name;
    private int data_area, data_electric, data_water;
    private Long data_type_id;
    private FloorDAO data_floor;
    private boolean data_rented;

    public static RoomInsertScreen getInstance(FloorDAO floor) {
        RoomInsertScreen screen = new RoomInsertScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(FLOOR_KEY, floor);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_room_insert, container, false);
    }

    @Override
    public void onBaseCreate() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            data_floor = (FloorDAO) bundle.getSerializable(FLOOR_KEY);
        }
        floors = DAOManager.getAllFloors();
        floors.add(0, null);
        types = DAOManager.getAllRoomTypes();
        types.add(0, null);
    }

    @Override
    public void onDeepLinking(Intent data) {

    }

    @Override
    public void onNotification(Intent data) {

    }

    @Override
    public void onBindView() {
        fragment_room_insert_sn_floor = (Spinner) findViewById(R.id.fragment_room_insert_sn_floor);
        fragment_room_insert_sn_type = (Spinner) findViewById(R.id.fragment_room_insert_sn_type);

        fragment_room_insert_sn_floor.setAdapter(new RoomFloorAdapter(floors, true));
        fragment_room_insert_sn_type.setAdapter(new RoomTypeAdapter(types));

        fragment_room_insert_sn_floor.setOnItemSelectedListener(this);

        fragment_room_insert_tg_rented = (ToggleButton) findViewById(R.id.fragment_room_insert_tg_rented);
        fragment_room_insert_tg_rented.setOnCheckedChangeListener(this);
        fragment_room_insert_et_area = (EditText) findViewById(R.id.fragment_room_insert_et_area);
        fragment_room_insert_et_name = (EditText) findViewById(R.id.fragment_room_insert_et_name);
        fragment_room_insert_tv_rented_date = (TextView) findViewById(R.id.fragment_room_insert_tv_rented_date);
        fragment_room_insert_et_electric = (EditText) findViewById(R.id.fragment_room_insert_et_electric);
        fragment_room_insert_et_water = (EditText) findViewById(R.id.fragment_room_insert_et_water);

        findViewById(R.id.fragment_room_insert_bt_save);
        findViewById(R.id.fragment_room_insert_bt_cancel);
    }

    @Override
    public void onInitializeViewData() {
        int floor_size = fragment_room_insert_sn_floor.getAdapter().getCount();
        if (data_floor != null) {
            for (int i = 1; i < floor_size; ++i) {
                if (fragment_room_insert_sn_floor.getItemAtPosition(i) instanceof FloorDAO) {
                    FloorDAO floorDAO = (FloorDAO) fragment_room_insert_sn_floor.getItemAtPosition(i);
                    if (floorDAO.getId() == data_floor.getId()) {
                        fragment_room_insert_sn_floor.setSelection(i);
                        fragment_room_insert_sn_floor.setEnabled(false);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onBaseResume() {
        String floor_name = "";
        if (data_floor != null)
            floor_name = data_floor.getName();
        ((MainActivity) getActiveActivity()).setScreenHeader(getString(R.string.main_header_room_insert) + " " + floor_name);
    }

    @Override
    public void onBaseFree() {

    }

    @Override
    public void onSingleClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_room_insert_bt_save:
                if (validated()) {
                    Date rent_date = data_rented ? new Date() : null;
                    Long data_id = DAOManager.
                            addRoom(data_name, data_area, data_type_id, data_rented, rent_date, data_electric, data_water, data_floor.getId());
                    replaceFragment(R.id.activity_main_container, RoomDetailScreen.getInstance(DAOManager.getRoom(data_id)), RoomDetailScreen.TAG, false);
                }
                break;
            case R.id.fragment_room_insert_bt_cancel:
                finish();
                break;
        }
    }


    private boolean validated() {
        if (fragment_room_insert_sn_floor != null && fragment_room_insert_sn_floor.getSelectedItemPosition() == 0) {
            showAlertDialog(getActiveActivity(), -1, -1,
                    getString(R.string.application_alert_dialog_title),
                    getString(R.string.room_choose_floor_error), getString(R.string.common_ok), null);
            return false;
        }

        if (fragment_room_insert_et_name != null && Utils.isEmpty(fragment_room_insert_et_name.getText().toString())) {
            showAlertDialog(getActiveActivity(), -1, -1,
                    getString(R.string.application_alert_dialog_title),
                    getString(R.string.room_insert_name_error), getString(R.string.common_ok), null);
            return false;
        }

        if (fragment_room_insert_et_area != null && Utils.isEmpty(fragment_room_insert_et_area.getText().toString())) {
            showAlertDialog(getActiveActivity(), -1, -1,
                    getString(R.string.application_alert_dialog_title),
                    getString(R.string.room_insert_area_error), getString(R.string.common_ok), null);
            return false;
        }

        if (fragment_room_insert_et_electric != null && Utils.isEmpty(fragment_room_insert_et_electric.getText().toString())) {
            showAlertDialog(getActiveActivity(), -1, -1,
                    getString(R.string.application_alert_dialog_title),
                    getString(R.string.room_insert_electric_error), getString(R.string.common_ok), null);
            return false;
        }

        if (fragment_room_insert_et_water != null && Utils.isEmpty(fragment_room_insert_et_water.getText().toString())) {
            showAlertDialog(getActiveActivity(), -1, -1,
                    getString(R.string.application_alert_dialog_title),
                    getString(R.string.room_insert_water_error), getString(R.string.common_ok), null);
            return false;
        }

        if (fragment_room_insert_sn_type != null && fragment_room_insert_sn_type.getSelectedItem() == null) {
            showAlertDialog(getActiveActivity(), -1, -1,
                    getString(R.string.application_alert_dialog_title),
                    getString(R.string.room_choose_type_error), getString(R.string.common_ok), null);
            return false;
        }
        data_electric = Integer.parseInt(fragment_room_insert_et_electric.getText().toString().trim());
        data_water = Integer.parseInt(fragment_room_insert_et_water.getText().toString().trim());
        data_rented = fragment_room_insert_tg_rented.isChecked();
        data_name = fragment_room_insert_et_name.getText().toString().trim();
        data_area = Integer.parseInt(fragment_room_insert_et_area.getText().toString().trim());
        data_type_id = ((RoomTypeDAO) fragment_room_insert_sn_type.getSelectedItem()).getId();
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0)
            return;
        data_floor = (FloorDAO) parent.getAdapter().getItem(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yyyy");
            fragment_room_insert_tv_rented_date.setText(getString(R.string.room_rented_date_title) + " " + formater.format(new Date()));
        } else {
            fragment_room_insert_tv_rented_date.setText("");
        }
    }
}
