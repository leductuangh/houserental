package com.example.houserental.function.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.example.houserental.R;
import com.example.houserental.function.MainActivity;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.PaymentDAO;
import com.example.houserental.function.model.RoomDAO;
import com.example.houserental.function.model.UserDAO;
import com.example.houserental.function.user.UserListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import core.base.BaseMultipleFragment;
import core.data.DataSaver;
import core.util.Utils;

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
    private CheckBox fragment_payment_record_cb_exceed_date;
    private ToggleButton fragment_payment_record_tg_continue;
    private SimpleDateFormat formatter;

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
        formatter = new SimpleDateFormat("dd-MMM-yyyy");
    }

    @Override
    public void onDeepLinking(Intent data) {

    }

    @Override
    public void onNotification(Intent data) {

    }

    @Override
    public void onBindView() {
        fragment_payment_record_tg_continue = (ToggleButton) findViewById(R.id.fragment_payment_record_tg_continue);
        fragment_payment_record_et_electric = (EditText) findViewById(R.id.fragment_payment_record_et_electric);
        fragment_payment_record_et_water = (EditText) findViewById(R.id.fragment_payment_record_et_water);
        fragment_payment_record_sn_user = (Spinner) findViewById(R.id.fragment_payment_record_sn_user);
        fragment_payment_dp_payment_end = (DatePicker) findViewById(R.id.fragment_payment_dp_payment_end);
        fragment_payment_dp_payment_end.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        fragment_payment_record_sn_room = (Spinner) findViewById(R.id.fragment_payment_record_sn_room);
        fragment_payment_record_cb_exceed_date = (CheckBox) findViewById(R.id.fragment_payment_record_cb_exceed_date);
        fragment_payment_record_sn_room.setOnItemSelectedListener(this);
        findViewById(R.id.fragment_payment_record_bt_create);

    }

    @Override
    public void onInitializeViewData() {
        fragment_payment_record_sn_user.setAdapter(adapter = new UserListAdapter(users, true));
        fragment_payment_record_sn_room.setAdapter(new PaymentRoomListAdapter());
        fragment_payment_record_sn_room.setSelection(fragment_payment_record_sn_room.getAdapter().getCount(), false);
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
                        Calendar start = Calendar.getInstance();
                        start.setTime(room.getPaymentStartDate());
                        long daysBetween = Utils.daysBetween(room.getPaymentStartDate(), calendar.getTime());
                        int startMonth = start.get(Calendar.MONTH) + 1;
                        int startYear = start.get(Calendar.YEAR);
                        int dayOfMonthCount = Utils.dayCountOfMonth(startMonth, startYear);
                        boolean isFullMonth = daysBetween >= dayOfMonthCount;
                        int exceed_date = 0;
                        if (isFullMonth) {
                            // count exceed days too
                            if (fragment_payment_record_cb_exceed_date.isChecked())
                                exceed_date = (int) (daysBetween - dayOfMonthCount);
                        } else {
                            // charge by day
                            exceed_date = (int) daysBetween;
                        }

                        PaymentDAO payment = new PaymentDAO(room.getId(), // room id
                                room.getName(), // room name
                                DataSaver.getInstance().getString(DataSaver.Key.OWNER), // owner
                                ((UserDAO) fragment_payment_record_sn_user.getSelectedItem()).getName(), // payer
                                room.getType().getPrice(), // room price
                                room.getElectricNumber(), // previous electric number
                                room.getWaterNumber(), // previous water number
                                Integer.parseInt(fragment_payment_record_et_electric.getText().toString().trim()), // current electric number
                                Integer.parseInt(fragment_payment_record_et_water.getText().toString().trim()), // current water number
                                DAOManager.getDeviceCountOfRoom(room.getId()), // number of wifi device
                                DataSaver.getInstance().getInt(DataSaver.Key.ELECTRIC_PRICE), // electric price
                                DataSaver.getInstance().getInt(DataSaver.Key.WATER_PRICE), // water price
                                DataSaver.getInstance().getInt(DataSaver.Key.DEVICE_PRICE), // device price
                                DAOManager.getUserCountOfRoom(room.getId()), // user count
                                DataSaver.getInstance().getInt(DataSaver.Key.WASTE_PRICE), // previous payment date
                                room.getPaymentStartDate(), calendar.getTime(),  // current payment date
                                isFullMonth, // full month rental
                                exceed_date); // count exceed date charge
                        payment.setContinueRental(fragment_payment_record_tg_continue.isChecked());
                        addFragment(R.id.activity_main_container, PaymentReviewScreen.getInstance(payment), PaymentReviewScreen.TAG);
                    } catch (Exception e) {
                        e.printStackTrace();
                        showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.application_alert_dialog_error_general), getString(R.string.common_ok), null, null);
                    }
                }
                break;
        }
    }

    private boolean validated() {
        try {
            if (room == null) {
                showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.payment_record_no_room_error), getString(R.string.common_ok), null, null);
                return false;
            }
            if (Utils.isEmpty((DataSaver.getInstance().getString(DataSaver.Key.OWNER)))) {
                showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.payment_record_no_owner_error), getString(R.string.common_ok), null, null);
                return false;
            }

            if (fragment_payment_record_sn_user.getSelectedItem() == null) {
                showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.payment_record_no_payer_error), getString(R.string.common_ok), null, null);
                return false;
            }

            Calendar start = Calendar.getInstance();
            start.setTime(room.getPaymentStartDate());
            Calendar end = Calendar.getInstance();
            end.set(fragment_payment_dp_payment_end.getYear(), fragment_payment_dp_payment_end.getMonth(), fragment_payment_dp_payment_end.getDayOfMonth());

            if (end.before(start)) {
                showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), String.format(getString(R.string.payment_record_wrong_pay_date_error), formatter.format(start.getTime())), getString(R.string.common_ok), null, null);
                return false;
            }

            if (room.getType() == null) {
                showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), String.format(getString(R.string.payment_record_no_room_type_error), room.getName()), getString(R.string.common_ok), null, null);
                return false;
            }

            if (Utils.isEmpty(fragment_payment_record_et_electric.getText().toString().trim())) {
                showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.payment_record_no_current_electric_error), getString(R.string.common_ok), null, null);
                return false;
            } else {
                if (Integer.parseInt(fragment_payment_record_et_electric.getText().toString().trim()) - room.getElectricNumber() < 0) {
                    showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), String.format(getString(R.string.payment_record_negative_electric_error), room.getElectricNumber() + ""), getString(R.string.common_ok), null, null);
                    return false;
                }
            }

            if (Utils.isEmpty(fragment_payment_record_et_water.getText().toString().trim())) {
                showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.payment_record_no_current_water_error), getString(R.string.common_ok), null, null);
                return false;
            } else {
                if (Integer.parseInt(fragment_payment_record_et_water.getText().toString().trim()) - room.getWaterNumber() < 0) {
                    showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), String.format(getString(R.string.payment_record_negative_water_error), room.getWaterNumber() + ""), getString(R.string.common_ok), null, null);
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.application_alert_dialog_error_general), getString(R.string.common_ok), null, null);
            return false;
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Object room = parent.getSelectedItem();
        if (room != null && room instanceof RoomDAO) {
            this.room = (RoomDAO) room;
//            if (users != null)
//                users.clear();
//            users.addAll(DAOManager.getUsersOfRoom(((RoomDAO) room).getId()));
//            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
