package com.example.houserental.function.payment;

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

import com.core.core.base.BaseMultipleFragment;
import com.core.data.DataSaver;
import com.core.util.Utils;
import com.example.houserental.R;
import com.example.houserental.function.MainActivity;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.PaymentDAO;
import com.example.houserental.function.model.RoomDAO;
import com.example.houserental.function.model.UserDAO;
import com.example.houserental.function.room.RoomListAdapter;
import com.example.houserental.function.user.UserListAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by leductuan on 3/14/16.
 */
public class PaymentRecordScreen extends BaseMultipleFragment implements AdapterView.OnItemSelectedListener {

    public static final String TAG = PaymentRecordScreen.class.getSimpleName();
    private Spinner fragment_payment_record_sn_room, fragment_payment_record_sn_user;
    private List<UserDAO> users;
    private RoomDAO room;
    private UserListAdapter adapter;
    private EditText fragment_payment_record_et_electric, fragment_payment_record_et_water;
    private DatePicker fragment_payment_dp_payment_end;

    public static PaymentRecordScreen getInstance() {
        PaymentRecordScreen screen = new PaymentRecordScreen();
        Bundle bundle = new Bundle();
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment_record, container, false);
    }

    @Override
    public void onBaseCreate() {
        users = new ArrayList<>();
    }

    @Override
    public void onDeepLinking(Intent data) {

    }

    @Override
    public void onNotification(Intent data) {

    }

    @Override
    public void onBindView() {
        fragment_payment_record_et_electric = (EditText) findViewById(R.id.fragment_payment_record_et_electric);
        fragment_payment_record_et_water = (EditText) findViewById(R.id.fragment_payment_record_et_water);
        fragment_payment_record_sn_user = (Spinner) findViewById(R.id.fragment_payment_record_sn_user);
        fragment_payment_dp_payment_end = (DatePicker) findViewById(R.id.fragment_payment_dp_payment_end);
        fragment_payment_record_sn_room = (Spinner) findViewById(R.id.fragment_payment_record_sn_room);
        fragment_payment_record_sn_room.setOnItemSelectedListener(this);
        findViewById(R.id.fragment_payment_record_bt_create);

    }

    @Override
    public void onInitializeViewData() {
        fragment_payment_record_sn_user.setAdapter(adapter = new UserListAdapter(users, false));
        fragment_payment_record_sn_room.setAdapter(new RoomListAdapter(DAOManager.getAllRentedRooms(), false));
    }

    @Override
    public void onBaseResume() {
        ((MainActivity) getActiveActivity()).setScreenHeader(getString(R.string.payment_record_header));
    }

    @Override
    public void onBaseFree() {

    }

    @Override
    public void onSingleClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_payment_record_bt_create:
                if (validated()) {
                    try {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(fragment_payment_dp_payment_end.getYear(), fragment_payment_dp_payment_end.getMonth(), fragment_payment_dp_payment_end.getDayOfMonth());
                        PaymentDAO payment = new PaymentDAO(room.getRoomId(), // room id
                                room.getName(), // room name
                                DataSaver.getInstance().getString(DataSaver.Key.OWNER), // owner
                                ((UserDAO) fragment_payment_record_sn_user.getSelectedItem()).getName(), // payer
                                room.getType().getPrice(), // room price
                                room.getElectricNumber(), // previous electric number
                                room.getWaterNumber(), // previous water number
                                Integer.parseInt(fragment_payment_record_et_electric.getText().toString().trim()), // current electric number
                                Integer.parseInt(fragment_payment_record_et_water.getText().toString().trim()), // current water number
                                DAOManager.getDeviceCountOfRoom(room.getRoomId()), // number of wifi device
                                DataSaver.getInstance().getInt(DataSaver.Key.ELECTRIC_PRICE), // electric price
                                DataSaver.getInstance().getInt(DataSaver.Key.WATER_PRICE), // water price
                                DataSaver.getInstance().getInt(DataSaver.Key.DEVICE_PRICE), // device price
                                DAOManager.getUserCountOfRoom(room.getRoomId()), // user count
                                DataSaver.getInstance().getInt(DataSaver.Key.WASTE_PRICE), // previous payment date
                                room.getPaymentStartDate(), calendar.getTime()); // current payment date
                        addFragment(R.id.activity_main_container, PaymentReviewScreen.getInstance(payment), PaymentReviewScreen.TAG);
                    } catch (Exception e) {
                        showAlertDialog(getActiveActivity(), -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.payment_record_no_owner_error), getString(R.string.common_ok), null);
                    }
                }
                break;
        }
    }

    private boolean validated() {
        try {
            if (room == null) {
                showAlertDialog(getActiveActivity(), -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.application_alert_dialog_error_general), getString(R.string.common_ok), null);
                finish();
                return false;
            }
            if (Utils.isEmpty((DataSaver.getInstance().getString(DataSaver.Key.OWNER)))) {
                showAlertDialog(getActiveActivity(), -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.payment_record_no_owner_error), getString(R.string.common_ok), null);
                return false;
            }

            if (fragment_payment_record_sn_user.getSelectedItem() == null) {
                showAlertDialog(getActiveActivity(), -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.payment_record_no_payer_error), getString(R.string.common_ok), null);
                return false;
            }

            if (room.getType() == null) {
                showAlertDialog(getActiveActivity(), -1, -1, getString(R.string.application_alert_dialog_title), String.format(getString(R.string.payment_record_no_room_type_error), room.getName()), getString(R.string.common_ok), null);
                return false;
            }

            if (Utils.isEmpty(fragment_payment_record_et_electric.getText().toString().trim())) {
                showAlertDialog(getActiveActivity(), -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.payment_record_no_current_electric_error), getString(R.string.common_ok), null);
                return false;
            } else {
                if (Integer.parseInt(fragment_payment_record_et_electric.getText().toString().trim()) - room.getElectricNumber() < 0) {
                    showAlertDialog(getActiveActivity(), -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.payment_record_negative_electric_error), getString(R.string.common_ok), null);
                    return false;
                }
            }

            if (Utils.isEmpty(fragment_payment_record_et_water.getText().toString().trim())) {
                showAlertDialog(getActiveActivity(), -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.payment_record_no_current_water_error), getString(R.string.common_ok), null);
                return false;
            } else {
                if (Integer.parseInt(fragment_payment_record_et_water.getText().toString().trim()) - room.getWaterNumber() < 0) {
                    showAlertDialog(getActiveActivity(), -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.payment_record_negative_water_error), getString(R.string.common_ok), null);
                    return false;
                }
            }
        } catch (Exception e) {
            showAlertDialog(getActiveActivity(), -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.application_alert_dialog_error_general), getString(R.string.common_ok), null);
            finish();
            return false;
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Object room = parent.getSelectedItem();
        if (room != null && room instanceof RoomDAO) {
            this.room = (RoomDAO) room;
            if (users != null)
                users.clear();
            users.addAll(DAOManager.getUsersOfRoom(((RoomDAO) room).getRoomId()));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
