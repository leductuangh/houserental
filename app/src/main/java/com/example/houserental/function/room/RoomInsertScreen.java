package com.example.houserental.function.room;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.example.houserental.R;
import com.example.houserental.core.base.BaseMultipleFragment;
import com.example.houserental.function.MainActivity;
import com.example.houserental.model.DAOManager;
import com.example.houserental.model.FloorDAO;
import com.example.houserental.model.RoomDAO;
import com.example.houserental.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by leductuan on 3/6/16.
 */
public class RoomInsertScreen extends BaseMultipleFragment implements AdapterView.OnItemSelectedListener {

    public static final String TAG = RoomInsertScreen.class.getSimpleName();
    private static final String FLOOR_KEY = "floor_key";
    private static final String ROOM_ID_FORMAT = "F_%s_R_%s";
    private int room = -1;
    private List<FloorDAO> floors;
    private List<RoomDAO.Type> types;

    private Spinner fragment_room_insert_sn_floor, fragment_room_insert_sn_type;
    private ToggleButton fragment_room_insert_tg_rented;
    private EditText fragment_room_insert_et_area, fragment_room_insert_et_name, fragment_room_insert_et_id;

    private String data_id;
    private String data_name;
    private int data_area;
    private RoomDAO.Type data_type;
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
        if (data_floor != null)
            room = DAOManager.getRoomCountOfFloor(data_floor.getFloorId()) + 1;
        floors = DAOManager.getAllFloors();
        floors.add(0, null);
        types = new ArrayList<>();
        types.addAll(Arrays.asList(RoomDAO.Type.values()));
        types.add(0, RoomDAO.Type.NORMAL);
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

        fragment_room_insert_sn_floor.setAdapter(new RoomInsertFloorAdapter(floors));
        fragment_room_insert_sn_type.setAdapter(new RoomInsertTypeAdapter(types));

        fragment_room_insert_sn_floor.setOnItemSelectedListener(this);

        fragment_room_insert_tg_rented = (ToggleButton) findViewById(R.id.fragment_room_insert_tg_rented);

        fragment_room_insert_et_area = (EditText) findViewById(R.id.fragment_room_insert_et_area);
        fragment_room_insert_et_name = (EditText) findViewById(R.id.fragment_room_insert_et_name);
        fragment_room_insert_et_id = (EditText) findViewById(R.id.fragment_room_insert_et_id);

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
                    if (floorDAO.getFloorId().equals(data_floor.getFloorId())) {
                        fragment_room_insert_sn_floor.setSelection(i);
                        fragment_room_insert_sn_floor.setEnabled(false);
                        break;
                    }
                }
            }
            fragment_room_insert_et_id.setText(String.format(ROOM_ID_FORMAT, data_floor.getFloorIndex() + "", room + ""));
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
                if (validated())
                    DAOManager.addRoom(data_id, data_name, data_area, data_type, data_rented, data_floor.getFloorId());
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
                    getString(R.string.room_insert_choose_floor_error), getString(R.string.common_ok), null);
            return false;
        }

        if (fragment_room_insert_et_name != null && Utils.isEmpty(fragment_room_insert_et_name.getText().toString())) {
            showAlertDialog(getActiveActivity(), -1, -1,
                    getString(R.string.application_alert_dialog_title),
                    getString(R.string.room_insert_insert_name_error), getString(R.string.common_ok), null);
            return false;
        }

        if (fragment_room_insert_et_area != null && Utils.isEmpty(fragment_room_insert_et_area.getText().toString())) {
            showAlertDialog(getActiveActivity(), -1, -1,
                    getString(R.string.application_alert_dialog_title),
                    getString(R.string.room_insert_insert_area_error), getString(R.string.common_ok), null);
            return false;
        }

        if (fragment_room_insert_sn_type != null && fragment_room_insert_sn_type.getSelectedItemPosition() == 0) {
            showAlertDialog(getActiveActivity(), -1, -1,
                    getString(R.string.application_alert_dialog_title),
                    getString(R.string.room_insert_choose_type_error), getString(R.string.common_ok), null);
            return false;
        }

        data_rented = fragment_room_insert_tg_rented.isChecked();
        data_id = fragment_room_insert_et_id.getText().toString();
        data_name = fragment_room_insert_et_name.getText().toString().trim();
        data_area = Integer.parseInt(fragment_room_insert_et_area.getText().toString().trim());
        data_type = (RoomDAO.Type) fragment_room_insert_sn_type.getSelectedItem();
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0)
            return;
        data_floor = (FloorDAO) parent.getAdapter().getItem(position);
        room = DAOManager.getRoomCountOfFloor(data_floor.getFloorId()) + 1;
        fragment_room_insert_et_id.setText(String.format(ROOM_ID_FORMAT, data_floor.getFloorIndex() + "", room + ""));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
