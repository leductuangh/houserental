package com.example.houserental.function.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.houserental.R;
import com.example.houserental.function.MainActivity;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.RoomTypeDAO;
import com.example.houserental.function.model.SettingDAO;

import core.base.BaseMultipleFragment;
import core.util.Utils;

/**
 * Created by Tyrael on 8/8/16.
 */
public class CalculatorScreen extends BaseMultipleFragment implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    public static final String TAG = CalculatorScreen.class.getSimpleName();
    private SettingDAO settting;
    private Spinner fragment_calculator_sn_type;
    private RadioButton fragment_calculator_rb_month_base, fragment_calculator_rb_day_base;
    private EditText fragment_calculator_et_water_new, fragment_calculator_et_water_old, fragment_calculator_et_electric_new, fragment_calculator_et_electric_old, fragment_calculator_et_number_of_devices, fragment_calculator_et_day_base, fragment_calculator_et_number_of_people;
    private TextView fragment_calculator_tv_electric_total, fragment_calculator_tv_water_total;
    private TextWatcher[] watchers = {
            // water new
            new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!Utils.isEmpty(s.toString()) && !Utils.isEmpty(fragment_calculator_et_water_old.getText().toString()))
                        fragment_calculator_tv_water_total.setText(String.valueOf(calculateDifference(Integer.parseInt(s.toString()), Integer.parseInt(fragment_calculator_et_water_old.getText().toString()))));
                }
            },
            // water old
            new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!Utils.isEmpty(s.toString()) && !Utils.isEmpty(fragment_calculator_et_water_new.getText().toString()))
                        fragment_calculator_tv_water_total.setText(String.valueOf(calculateDifference(Integer.parseInt(fragment_calculator_et_water_new.getText().toString()), Integer.parseInt(s.toString()))));
                }
            },
            // electric new
            new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!Utils.isEmpty(s.toString()) && !Utils.isEmpty(fragment_calculator_et_electric_old.getText().toString()))
                        fragment_calculator_tv_electric_total.setText(String.valueOf(calculateDifference(Integer.parseInt(s.toString()), Integer.parseInt(fragment_calculator_et_electric_old.getText().toString()))));
                }
            },
            // electric old
            new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!Utils.isEmpty(s.toString()) && !Utils.isEmpty(fragment_calculator_et_electric_new.getText().toString()))
                        fragment_calculator_tv_electric_total.setText(String.valueOf(calculateDifference(Integer.parseInt(fragment_calculator_et_electric_new.getText().toString()), Integer.parseInt(s.toString()))));
                }
            }
    };

    public static CalculatorScreen getInstance() {
        return new CalculatorScreen();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calculator, container, false);
    }

    @Override
    public void onBaseCreate() {

    }

    @Override
    public void onDeepLinking(Intent data) {

    }

    @Override
    public void onNotification(Intent data) {

    }

    @Override
    public void onBindView() {
        fragment_calculator_sn_type = (Spinner) findViewById(R.id.fragment_calculator_sn_type);
        fragment_calculator_rb_month_base = (RadioButton) findViewById(R.id.fragment_calculator_rb_month_base);
        fragment_calculator_rb_day_base = (RadioButton) findViewById(R.id.fragment_calculator_rb_day_base);
        fragment_calculator_et_water_new = (EditText) findViewById(R.id.fragment_calculator_et_water_new);
        fragment_calculator_et_water_old = (EditText) findViewById(R.id.fragment_calculator_et_water_old);
        fragment_calculator_et_electric_new = (EditText) findViewById(R.id.fragment_calculator_et_electric_new);
        fragment_calculator_et_electric_old = (EditText) findViewById(R.id.fragment_calculator_et_electric_old);
        fragment_calculator_et_number_of_devices = (EditText) findViewById(R.id.fragment_calculator_et_number_of_devices);
        fragment_calculator_et_day_base = (EditText) findViewById(R.id.fragment_calculator_et_day_base);
        fragment_calculator_et_number_of_people = (EditText) findViewById(R.id.fragment_calculator_et_number_of_people);
        fragment_calculator_tv_electric_total = (TextView) findViewById(R.id.fragment_calculator_tv_electric_total);
        fragment_calculator_tv_water_total = (TextView) findViewById(R.id.fragment_calculator_tv_water_total);
    }

    @Override
    public void onInitializeViewData() {
        settting = DAOManager.getSetting();
        if (settting == null) {
            finish();
            return;
        }
        fragment_calculator_sn_type.setAdapter(new CalculatorRoomTypeAdapter(DAOManager.getAllRoomTypes()));
        fragment_calculator_sn_type.setOnItemSelectedListener(this);
        fragment_calculator_rb_month_base.setOnCheckedChangeListener(this);
        fragment_calculator_rb_day_base.setOnCheckedChangeListener(this);
        fragment_calculator_et_water_new.addTextChangedListener(watchers[0]);
        fragment_calculator_et_water_old.addTextChangedListener(watchers[1]);
        fragment_calculator_et_electric_new.addTextChangedListener(watchers[2]);
        fragment_calculator_et_electric_old.addTextChangedListener(watchers[3]);
        registerSingleAction(R.id.fragment_calculator_bt_calculate);
    }

    @Override
    public void onBaseResume() {
        ((MainActivity) getActiveActivity()).setScreenHeader(getString(com.example.houserental.R.string.main_header_calculator));
    }

    @Override
    public void onBaseFree() {

    }

    @Override
    public void onSingleClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_calculator_bt_calculate:
                if (validated()) {


                    int water = Integer.parseInt(fragment_calculator_tv_water_total.getText().toString()) * settting.getWaterPrice();
                    int electric = Integer.parseInt(fragment_calculator_tv_electric_total.getText().toString()) * settting.getElectriPrice();
                    int waste = Integer.parseInt(fragment_calculator_et_number_of_people.getText().toString()) * settting.getWastePrice();
                    int wifi = Integer.parseInt(fragment_calculator_et_number_of_devices.getText().toString()) * settting.getDevicePrice();

                    RoomTypeDAO type = (RoomTypeDAO) fragment_calculator_sn_type.getSelectedItem();
                    int room = 0;
                    if (fragment_calculator_rb_month_base.isChecked()) {
                        room = type.getPrice();
                    } else {
                        int day = Integer.parseInt(fragment_calculator_et_day_base.getText().toString());
                        int perDay = type.getPrice() / 30;
                        room = day * perDay;
                    }

                    int total = waste + water + electric + wifi + room;

                    showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.calculator_calculated_message, String.valueOf(total)), getString(R.string.common_ok), null, null);

                }
                break;
        }
    }

    private boolean validated() {
        if (fragment_calculator_sn_type.getSelectedItem() == null) {
            showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.calculator_type_error), getString(R.string.common_ok), null, null);
            return false;
        }

        if (!fragment_calculator_rb_month_base.isChecked()) {
            if (Utils.isEmpty(fragment_calculator_et_day_base.getText().toString())) {
                showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.calculator_day_error), getString(R.string.common_ok), null, null);
                return false;
            } else {
                if (Integer.parseInt(fragment_calculator_et_day_base.getText().toString()) < 1 || Integer.parseInt(fragment_calculator_et_day_base.getText().toString()) > 31) {
                    showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.calculator_day_error), getString(R.string.common_ok), null, null);
                    return false;
                }
            }
        }

        if (Utils.isEmpty(fragment_calculator_et_number_of_people.getText().toString())) {
            showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.calculator_people_error), getString(R.string.common_ok), null, null);
            return false;
        } else {
            if (Integer.parseInt(fragment_calculator_et_number_of_people.getText().toString()) < 1 || Integer.parseInt(fragment_calculator_et_number_of_people.getText().toString()) > 9) {
                showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.calculator_people_error), getString(R.string.common_ok), null, null);
                return false;
            }
        }

        if (Utils.isEmpty(fragment_calculator_et_number_of_devices.getText().toString())) {
            showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.calculator_devices_error), getString(R.string.common_ok), null, null);
            return false;
        } else {
            if (Integer.parseInt(fragment_calculator_et_number_of_devices.getText().toString()) < 0 || Integer.parseInt(fragment_calculator_et_number_of_devices.getText().toString()) > 9) {
                showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.calculator_devices_error), getString(R.string.common_ok), null, null);
                return false;
            }
        }

        return true;
    }

    private int calculateDifference(int newNumber, int oldNumber) {
        int result = newNumber - oldNumber;
        if (result <= 0)
            result = 0;
        return result;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.fragment_calculator_rb_day_base:
                    fragment_calculator_et_day_base.setEnabled(true);
                    fragment_calculator_rb_month_base.setChecked(false);
                    break;
                case R.id.fragment_calculator_rb_month_base:
                    fragment_calculator_et_day_base.setEnabled(false);
                    fragment_calculator_rb_day_base.setChecked(false);
                    break;
            }
        }

    }
}
