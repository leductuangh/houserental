package com.example.houserental.function.room;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.houserental.R;
import com.example.houserental.function.HouseRentalUtils;
import com.example.houserental.function.MainActivity;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.FloorDAO;
import com.example.houserental.function.model.RoomTypeDAO;
import com.example.houserental.function.model.SettingDAO;

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
    private SettingDAO setting;
    private Spinner fragment_room_insert_sn_floor, fragment_room_insert_sn_type;
    private EditText fragment_room_insert_et_deposit, fragment_room_insert_et_area, fragment_room_insert_et_name, fragment_room_insert_et_electric, fragment_room_insert_et_water;
    private TextView fragment_room_insert_tv_rented_date;
    private LinearLayout fragment_room_insert_ll_deposit;
    private String data_name;
    private int data_area, data_electric, data_water, data_deposit;
    private Long data_type_id;
    private SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yyyy");
    private FloorDAO data_floor;
    private boolean data_rented = false;
    private RoomFloorAdapter floor_adapter;
    private RoomTypeAdapter room_type_adapter;
    private Animation slide_down;
    private Animation slide_up;

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
        setting = DAOManager.getSetting();
        floors = DAOManager.getAllFloors();
        types = DAOManager.getAllRoomTypes();
        floor_adapter = new RoomFloorAdapter(floors, true);
        room_type_adapter = new RoomTypeAdapter(types, true);
        slide_down = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_top);
        slide_up = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_top);
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

        fragment_room_insert_sn_floor.setAdapter(floor_adapter);
        fragment_room_insert_sn_type.setAdapter(room_type_adapter);

        fragment_room_insert_sn_floor.setOnItemSelectedListener(this);
        fragment_room_insert_sn_type.setOnItemSelectedListener(this);
        fragment_room_insert_ll_deposit = (LinearLayout) findViewById(R.id.fragment_room_insert_ll_deposit);
        fragment_room_insert_et_deposit = (EditText) findViewById(R.id.fragment_room_insert_et_deposit);
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
            fragment_room_insert_sn_floor.setEnabled(false);
        } else {
            fragment_room_insert_sn_floor.setSelection(floor_adapter.getCount());
        }
        fragment_room_insert_sn_type.setSelection(room_type_adapter.getCount());
        fragment_room_insert_tv_rented_date.setText(getString(R.string.room_not_rented_text));
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
            case R.id.fragment_room_insert_tv_rented_date:
                if (data_rented) {
                    // rented
                    slide_up.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            fragment_room_insert_ll_deposit.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            fragment_room_insert_ll_deposit.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    fragment_room_insert_ll_deposit.startAnimation(slide_up);
                } else {
                    // not rented
                    slide_down.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            fragment_room_insert_ll_deposit.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            fragment_room_insert_ll_deposit.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    fragment_room_insert_ll_deposit.startAnimation(slide_down);
                }
                data_rented = !data_rented;
                String rent_status = data_rented ? getString(R.string.room_rented_text) + "\n" + getString(R.string.room_rented_date_title) + " " + formater.format(new Date()) : getString(R.string.room_not_rented_text);
                fragment_room_insert_tv_rented_date.setText(rent_status);
                break;
            case R.id.fragment_room_insert_bt_save:
                if (validated()) {
                    Date rent_date = data_rented ? new Date() : null;
                    Long data_id = DAOManager.
                            addRoom(data_name, data_area, data_type_id, data_rented, rent_date, data_electric, data_water, data_deposit, data_floor.getId());
                    replaceFragment(R.id.activity_main_container, RoomDetailScreen.getInstance(DAOManager.getRoom(data_id)), RoomDetailScreen.TAG, false);
                }
                break;
            case R.id.fragment_room_insert_bt_cancel:
                finish();
                break;
        }
    }


    private boolean validated() {
        if (data_floor == null || data_floor.getId() == null) {
            showAlertDialog(getActiveActivity(), -1, -1, -1,
                    getString(R.string.application_alert_dialog_title),
                    getString(R.string.room_choose_floor_error), getString(R.string.common_ok), null, null);
            return false;
        }

        if (fragment_room_insert_et_name != null && Utils.isEmpty(fragment_room_insert_et_name.getText().toString())) {
            showAlertDialog(getActiveActivity(), -1, -1, -1,
                    getString(R.string.application_alert_dialog_title),
                    getString(R.string.room_insert_name_error), getString(R.string.common_ok), null, null);
            return false;
        }

        if (fragment_room_insert_et_area != null && Utils.isEmpty(fragment_room_insert_et_area.getText().toString())) {
            showAlertDialog(getActiveActivity(), -1, -1, -1,
                    getString(R.string.application_alert_dialog_title),
                    getString(R.string.room_insert_area_error), getString(R.string.common_ok), null, null);
            return false;
        }

        if (fragment_room_insert_et_electric != null && Utils.isEmpty(fragment_room_insert_et_electric.getText().toString())) {
            showAlertDialog(getActiveActivity(), -1, -1, -1,
                    getString(R.string.application_alert_dialog_title),
                    getString(R.string.room_insert_electric_error), getString(R.string.common_ok), null, null);
            return false;
        }

        if (fragment_room_insert_et_water != null && Utils.isEmpty(fragment_room_insert_et_water.getText().toString())) {
            showAlertDialog(getActiveActivity(), -1, -1, -1,
                    getString(R.string.application_alert_dialog_title),
                    getString(R.string.room_insert_water_error), getString(R.string.common_ok), null, null);
            return false;
        }

        if (data_type_id == null) {
            showAlertDialog(getActiveActivity(), -1, -1, -1,
                    getString(R.string.application_alert_dialog_title),
                    getString(R.string.room_choose_type_error), getString(R.string.common_ok), null, null);
            return false;
        }

        if (data_rented) {
            if (fragment_room_insert_et_deposit != null && Utils.isEmpty(fragment_room_insert_et_deposit.getText().toString())) {
                showAlertDialog(getActiveActivity(), -1, -1, -1,
                        getString(R.string.application_alert_dialog_title),
                        getString(R.string.room_insert_deposit_error), getString(R.string.common_ok), null, null);
                return false;
            }
            data_deposit = Integer.parseInt(fragment_room_insert_et_deposit.getText().toString().trim());
            try {
                int min_deposit = 0;
                if (data_rented && data_deposit < (min_deposit = setting.getDeposit())) {
                    String deposit_text = String.format(getString(R.string.room_insert_deposit_under_warning), HouseRentalUtils.toThousandVND(min_deposit));
                    Toast.makeText(getActiveActivity(), deposit_text, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            data_deposit = 0;
        }

        data_electric = Integer.parseInt(fragment_room_insert_et_electric.getText().toString().trim());
        data_water = Integer.parseInt(fragment_room_insert_et_water.getText().toString().trim());
        data_name = fragment_room_insert_et_name.getText().toString().trim();
        data_area = Integer.parseInt(fragment_room_insert_et_area.getText().toString().trim());
        data_type_id = ((RoomTypeDAO) fragment_room_insert_sn_type.getSelectedItem()).getId();
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getAdapter() instanceof RoomFloorAdapter) {
            data_floor = (FloorDAO) parent.getAdapter().getItem(position);
        } else if (parent.getAdapter() instanceof RoomTypeAdapter) {
            data_type_id = ((RoomTypeDAO) parent.getAdapter().getItem(position)).getId();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        fragment_room_insert_et_deposit.setEnabled(isChecked);
        fragment_room_insert_ll_deposit.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        if (isChecked) {
            SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yyyy");
            fragment_room_insert_tv_rented_date.setText(getString(R.string.room_rented_date_title) + " " + formater.format(new Date()));
        } else {
            fragment_room_insert_tv_rented_date.setText("");
        }
    }
}
