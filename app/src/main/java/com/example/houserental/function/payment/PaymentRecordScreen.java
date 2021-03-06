package com.example.houserental.function.payment;

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
import android.widget.Toast;

import com.example.houserental.R;
import com.example.houserental.function.HouseRentalApplication;
import com.example.houserental.function.MainActivity;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.OwnerDAO;
import com.example.houserental.function.model.PaymentDAO;
import com.example.houserental.function.model.ProceedingDAO;
import com.example.houserental.function.model.RoomDAO;
import com.example.houserental.function.model.SettingDAO;
import com.example.houserental.function.model.UserDAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import core.base.BaseMultipleFragment;
import core.util.Utils;

/**
 * Created by leductuan on 3/14/16.
 */
public class PaymentRecordScreen extends BaseMultipleFragment implements AdapterView.OnItemSelectedListener, PaymentPaidDatePickerDialog.OnPaidDatePickerListener {

    public static final String TAG = PaymentRecordScreen.class.getSimpleName();
    private Spinner fragment_payment_record_sn_room, fragment_payment_record_sn_user;
    private RoomDAO room;
    private UserDAO user;
    private OwnerDAO owner;
    private ProceedingDAO proceeding;
    private SettingDAO setting;
    private Calendar paid_date;
    private Calendar start_date;
    private PaymentPayerListAdapter adapter;
    private List<UserDAO> data;
    private EditText fragment_payment_et_payment_end, fragment_payment_record_et_electric, fragment_payment_record_et_water;
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
        formatter = new SimpleDateFormat("dd-MMM-yyyy");
        setting = DAOManager.getSetting();

        if (setting == null) {
            Toast.makeText(getActiveActivity(), getString(R.string.application_alert_dialog_error_general), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Long owner_id = setting.getOwner();
            if (owner_id != null)
                owner = DAOManager.getOwner(owner_id);
            else {
                Toast.makeText(getActiveActivity(), getString(R.string.application_alert_dialog_error_general), Toast.LENGTH_SHORT).show();
                finish();
            }
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
        fragment_payment_et_payment_end = (EditText) findViewById(R.id.fragment_payment_et_payment_end);
        fragment_payment_et_payment_end.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    start_date = Calendar.getInstance();
                    start_date.setTime(room.getPaymentStartDate());
                    PaymentPaidDatePickerDialog dialog = new PaymentPaidDatePickerDialog(getActiveActivity(), start_date, PaymentRecordScreen.this);
                    dialog.show();
                }
                return true;
            }
        });
        registerSingleAction(R.id.fragment_payment_record_bt_save, R.id.fragment_payment_record_bt_create);
        fragment_payment_record_et_electric = (EditText) findViewById(R.id.fragment_payment_record_et_electric);
        fragment_payment_record_et_water = (EditText) findViewById(R.id.fragment_payment_record_et_water);
        fragment_payment_record_sn_user = (Spinner) findViewById(R.id.fragment_payment_record_sn_user);
        fragment_payment_record_sn_room = (Spinner) findViewById(R.id.fragment_payment_record_sn_room);
        fragment_payment_record_sn_room.setOnItemSelectedListener(this);
        fragment_payment_record_sn_user.setEnabled(false);
        fragment_payment_record_sn_user.setOnItemSelectedListener(this);

    }

    @Override
    public void onInitializeViewData() {
        fragment_payment_record_sn_user.setAdapter(adapter = new PaymentPayerListAdapter(data = new ArrayList<UserDAO>(), null));
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
            case R.id.fragment_payment_record_bt_save:
                if (validated()) {
                    proceeding.setElectricNumber(Integer.parseInt(fragment_payment_record_et_electric.getText().toString().trim()));
                    proceeding.setWaterNumber(Integer.parseInt(fragment_payment_record_et_water.getText().toString().trim()));
                    proceeding.save();
                    showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.payment_record_save_successful_message), getString(R.string.common_ok), null, null);
                }
                break;
            case R.id.fragment_payment_record_bt_create:
                if (validated()) {
                    try {
                        long daysBetween = Utils.daysBetween(start_date.getTime(), paid_date.getTime()) + 1;
                        proceeding.setElectricNumber(Integer.parseInt(fragment_payment_record_et_electric.getText().toString().trim()));
                        proceeding.setWaterNumber(Integer.parseInt(fragment_payment_record_et_water.getText().toString().trim()));
                        proceeding.save();
                        // including the start date
                        PaymentDAO payment = new PaymentDAO(
                                room.getId(), // room id
                                room.getName(), // room name
                                owner.getName(), // owner
                                user.getName(), // payer
                                room.getType().getPrice(), // room price
                                room.getElectricNumber(), // previous electric number
                                room.getWaterNumber(), // previous water number
                                Integer.parseInt(fragment_payment_record_et_electric.getText().toString().trim()), // current electric number
                                Integer.parseInt(fragment_payment_record_et_water.getText().toString().trim()), // current water number
                                DAOManager.getDeviceCountOfRoom(room.getId()), // number of wifi device
                                setting.getElectriPrice(), // electric price
                                setting.getWaterPrice(), // water price
                                setting.getDevicePrice(), // device price
                                setting.getWastePrice(), // waste price
                                DAOManager.getUserCountOfRoom(room.getId()), // user count
                                start_date.getTime(), // start payment date
                                paid_date.getTime(),  // end payment date
                                (int) daysBetween, 0, room.getDeposit()); // stay days
                        addFragment(R.id.activity_main_container, PaymentReviewScreen.getInstance(payment, proceeding));
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

            if (user == null) {
                showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.payment_record_no_payer_error), getString(R.string.common_ok), null, null);
                return false;
            }

            if (start_date != null && paid_date != null) {
                if (start_date.after(paid_date)) {
                    showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), String.format(getString(R.string.payment_record_start_date_after_error), formatter.format(start_date.getTime())), getString(R.string.common_ok), null, null);
                    return false;
                }
            } else {
                showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.payment_record_no_date_error), getString(R.string.common_ok), null, null);
                return false;
            }

            if (owner == null) {
                showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.payment_record_no_owner_error), getString(R.string.common_ok), null, null);
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
        Object item = parent.getSelectedItem();
        if (item != null && item instanceof RoomDAO) {
            RoomDAO room = (RoomDAO) item;
            if (room != null && room.getId() != null) {
                proceeding = DAOManager.getProceeding(room.getId());
                if (proceeding == null)
                    proceeding = new ProceedingDAO();
                proceeding.setRoom(room.getId());
                this.room = room;
                data.clear();
                data.addAll(DAOManager.getUsersOfRoom(room.getId()));
                UserDAO dummy = new UserDAO();
                dummy.setName(HouseRentalApplication.getContext().getString(R.string.common_setting_choose_payer));
                data.add(dummy);
                adapter.notifyDataSetChanged();
                fragment_payment_record_sn_user.post(new Runnable() {
                    @Override
                    public void run() {
                        String water_text = "";
                        String electric_text = "";
                        String payment_end = "";
                        paid_date = null;
                        boolean isSaved = false;
                        if (proceeding != null) {
                            if (proceeding.getElectricNumber() >= 0) {
                                isSaved = true;
                                electric_text = String.valueOf(proceeding.getElectricNumber());
                            }

                            if (proceeding.getWaterNumber() >= 0) {
                                isSaved = true;
                                water_text = String.valueOf(proceeding.getWaterNumber());
                            }

                            if (proceeding.getPaidDate() != null) {
                                isSaved = true;
                                start_date = Calendar.getInstance();
                                start_date.setTime(PaymentRecordScreen.this.room.getPaymentStartDate());
                                paid_date = Calendar.getInstance();
                                paid_date.setTime(proceeding.getPaidDate());
                                payment_end = formatter.format(paid_date.getTime());
                            }
                        }
                        fragment_payment_et_payment_end.setEnabled(isSaved);
                        fragment_payment_et_payment_end.setText(payment_end);
                        fragment_payment_record_et_water.setText(water_text);
                        fragment_payment_record_et_electric.setText(electric_text);
                        fragment_payment_record_sn_user.setEnabled(true);

                        if (isSaved) {
                            if (data != null) {
                                for (int i = 0; i < data.size(); ++i) {
                                    UserDAO u = data.get(i);
                                    if (u != null && u.getId() == proceeding.getPayer()) {
                                        fragment_payment_record_sn_user.setSelection(i, false);
                                    }
                                }
                            }
                        } else {
                            fragment_payment_record_sn_user.setSelection(adapter.getCount(), false);
                        }

                    }
                });
            } else {
                this.room = null;
                if (proceeding != null)
                    proceeding.setRoom(null);
            }
        } else if (item instanceof UserDAO) {
            UserDAO user = (UserDAO) item;
            if (user != null && user.getId() != null && this.data.size() > 1) {
                if (proceeding != null)
                    proceeding.setPayer(user.getId());
                this.user = user;
                fragment_payment_et_payment_end.setEnabled(true);
            } else {
                this.user = null;
                if (proceeding != null)
                    proceeding.setPayer(null);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onPaidDatePicked(Calendar paid_date) {
        if (proceeding != null)
            proceeding.setPaidDate(paid_date.getTime());
        this.paid_date = paid_date;
        fragment_payment_et_payment_end.setText(formatter.format(paid_date.getTime()));
    }
}
