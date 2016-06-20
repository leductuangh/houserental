package com.example.houserental.function.room;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.houserental.R;
import com.example.houserental.function.HouseRentalUtils;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.FloorDAO;
import com.example.houserental.function.model.RoomDAO;
import com.example.houserental.function.model.RoomTypeDAO;
import com.example.houserental.function.model.SettingDAO;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import core.base.BaseMultipleFragment;
import core.dialog.GeneralDialog;
import core.util.Constant;
import core.util.Utils;

/**
 * Created by Tyrael on 3/11/16.
 */
public class RoomEditScreen extends BaseMultipleFragment implements GeneralDialog.DecisionListener {

    public static final String TAG = RoomEditScreen.class.getSimpleName();
    private static final String ROOM_KEY = "room_key";
    private RoomDAO room;
    private SettingDAO setting;
    private Spinner fragment_room_edit_sn_floor, fragment_room_edit_sn_type;
    private EditText fragment_room_edit_et_deposit, fragment_room_edit_et_name, fragment_room_edit_et_area, fragment_room_edit_et_water, fragment_room_edit_et_electric;
    private TextView fragment_room_edit_tv_rented_date;
    private LinearLayout fragment_room_edit_ll_deposit;
    private SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yyyy");
    private Animation slide_down;
    private Animation slide_up;
    private boolean initialRentingStatus = false;
    private boolean currentRentingStatus = false;

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
        setting = DAOManager.getSetting();
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
        fragment_room_edit_ll_deposit = (LinearLayout) findViewById(R.id.fragment_room_edit_ll_deposit);
        fragment_room_edit_et_deposit = (EditText) findViewById(R.id.fragment_room_edit_et_deposit);
        fragment_room_edit_sn_floor = (Spinner) findViewById(R.id.fragment_room_edit_sn_floor);
        fragment_room_edit_sn_type = (Spinner) findViewById(R.id.fragment_room_edit_sn_type);
        fragment_room_edit_et_name = (EditText) findViewById(R.id.fragment_room_edit_et_name);
        fragment_room_edit_et_area = (EditText) findViewById(R.id.fragment_room_edit_et_area);
        fragment_room_edit_et_water = (EditText) findViewById(R.id.fragment_room_edit_et_water);
        fragment_room_edit_et_electric = (EditText) findViewById(R.id.fragment_room_edit_et_electric);
        fragment_room_edit_tv_rented_date = (TextView) findViewById(R.id.fragment_room_edit_tv_rented_date);
        registerSingleAction(R.id.fragment_room_edit_tv_rented_date, R.id.fragment_room_edit_bt_cancel, R.id.fragment_room_edit_bt_save);
    }

    @Override
    public void onInitializeViewData() {
        if (room != null) {
            initialRentingStatus = room.isRented();
            currentRentingStatus = room.isRented();
            fragment_room_edit_et_name.setText(room.getName());
            fragment_room_edit_et_area.setText(String.valueOf(room.getArea()));
            fragment_room_edit_et_electric.setText(String.valueOf(room.getElectricNumber()));
            fragment_room_edit_et_water.setText(String.valueOf(room.getWaterNumber()));
            fragment_room_edit_et_deposit.setText(String.valueOf(room.getDeposit()));
            fragment_room_edit_ll_deposit.setVisibility(room.isRented() ? View.VISIBLE : View.GONE);
            String rent_status = room.isRented() ? getString(R.string.room_rented_text) + "\n" + getString(R.string.room_rented_date_title) + " " + formater.format(room.getRentDate()) : getString(R.string.room_not_rented_text);
            fragment_room_edit_tv_rented_date.setText(rent_status);
            List<FloorDAO> floors = DAOManager.getAllFloors();
            List<RoomTypeDAO> types = DAOManager.getAllRoomTypes();
            fragment_room_edit_sn_floor.setAdapter(new RoomFloorAdapter(floors, false));
            fragment_room_edit_sn_type.setAdapter(new RoomTypeAdapter(types, false));

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
            case R.id.fragment_room_edit_tv_rented_date:
                final boolean beforeChanged = currentRentingStatus;
                currentRentingStatus = !currentRentingStatus;
                if (beforeChanged) {
                    // rented
                    slide_up.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            fragment_room_edit_ll_deposit.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            fragment_room_edit_ll_deposit.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    fragment_room_edit_ll_deposit.startAnimation(slide_up);
                } else {
                    // not rented
                    slide_down.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            fragment_room_edit_ll_deposit.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            fragment_room_edit_ll_deposit.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    fragment_room_edit_ll_deposit.startAnimation(slide_down);
                }
                Calendar renting_date = Calendar.getInstance();
//                int dayCountOfMonth = HouseRentalUtils.dayCountOfMonth(renting_date.get(Calendar.MONTH), renting_date.get(Calendar.YEAR));
//                if (renting_date.get(Calendar.DAY_OF_MONTH) == dayCountOfMonth)
//                    renting_date.add(Calendar.DAY_OF_MONTH, 1);
                String rent_status = currentRentingStatus ? getString(R.string.room_rented_text) + "\n" + getString(R.string.room_rented_date_title) + " " + formater.format(renting_date.getTime()) : getString(R.string.room_not_rented_text);
                fragment_room_edit_tv_rented_date.setText(rent_status);
                break;
            case R.id.fragment_room_edit_bt_cancel:
                finish();
                break;
            case R.id.fragment_room_edit_bt_save:
                if (validated()) {
                    if (initialRentingStatus && !currentRentingStatus) {
                        showDecisionDialog(getActiveActivity(), Constant.REMOVE_RENTAL_DIALOG, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.room_detail_remove_rental_message), getString(R.string.common_ok), getString(R.string.common_cancel), null, null, this);
                    } else {
                        Calendar validated_renting_date = Calendar.getInstance();
                        if (initialRentingStatus && currentRentingStatus) {
                            validated_renting_date.setTime(room.getPaymentStartDate());
                        }
                        initialRentingStatus = currentRentingStatus;
                        room.setRented(initialRentingStatus);
                        DAOManager.updateRoom(room.getId(),
                                fragment_room_edit_et_name.getText().toString().trim(),
                                Integer.parseInt(fragment_room_edit_et_area.getText().toString().trim()),
                                ((RoomTypeDAO) fragment_room_edit_sn_type.getSelectedItem()).getId(),
                                initialRentingStatus, initialRentingStatus ? validated_renting_date.getTime() : null,
                                Integer.parseInt(fragment_room_edit_et_electric.getText().toString().trim()),
                                Integer.parseInt(fragment_room_edit_et_water.getText().toString().trim()),
                                Integer.parseInt(fragment_room_edit_et_deposit.getText().toString().trim()),
                                ((FloorDAO) fragment_room_edit_sn_floor.getSelectedItem()).getId());

                        showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title),
                                getString(R.string.room_alert_dialog_update_success),
                                getString((R.string.common_ok)), null, null);
                    }
                }
                break;
        }
    }

    private boolean validated() {

        if (fragment_room_edit_sn_floor == null) {
            showAlertDialog(getActiveActivity(), -1, -1, -1,
                    getString(R.string.application_alert_dialog_title),
                    getString(R.string.room_choose_floor_error), getString(R.string.common_ok), null, null);
            return false;
        }

        if (fragment_room_edit_et_name != null && Utils.isEmpty(fragment_room_edit_et_name.getText().toString())) {
            showAlertDialog(getActiveActivity(), -1, -1, -1,
                    getString(R.string.application_alert_dialog_title),
                    getString(R.string.room_insert_name_error), getString(R.string.common_ok), null, null);
            return false;
        }

        if (fragment_room_edit_et_area != null && Utils.isEmpty(fragment_room_edit_et_area.getText().toString())) {
            showAlertDialog(getActiveActivity(), -1, -1, -1,
                    getString(R.string.application_alert_dialog_title),
                    getString(R.string.room_insert_area_error), getString(R.string.common_ok), null, null);
            return false;
        }

        if (fragment_room_edit_et_electric != null && Utils.isEmpty(fragment_room_edit_et_electric.getText().toString())) {
            showAlertDialog(getActiveActivity(), -1, -1, -1,
                    getString(R.string.application_alert_dialog_title),
                    getString(R.string.room_insert_electric_error), getString(R.string.common_ok), null, null);
            return false;
        }

        if (fragment_room_edit_et_water != null && Utils.isEmpty(fragment_room_edit_et_water.getText().toString())) {
            showAlertDialog(getActiveActivity(), -1, -1, -1,
                    getString(R.string.application_alert_dialog_title),
                    getString(R.string.room_insert_water_error), getString(R.string.common_ok), null, null);
            return false;
        }

        if (currentRentingStatus) {
            if (fragment_room_edit_et_deposit != null && Utils.isEmpty(fragment_room_edit_et_deposit.getText().toString())) {
                showAlertDialog(getActiveActivity(), -1, -1, -1,
                        getString(R.string.application_alert_dialog_title),
                        getString(R.string.room_insert_deposit_error), getString(R.string.common_ok), null, null);
                return false;
            }
            int data_deposit = Integer.parseInt(fragment_room_edit_et_deposit.getText().toString().trim());
            try {
                int min_deposit = 0;
                if (data_deposit < (min_deposit = setting.getDeposit())) {
                    String deposit_text = String.format(getString(R.string.room_insert_deposit_under_warning), HouseRentalUtils.toThousandVND(min_deposit));
                    Toast.makeText(getActiveActivity(), deposit_text, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (fragment_room_edit_sn_type.getSelectedItem() == null) {
            showAlertDialog(getActiveActivity(), -1, -1, -1,
                    getString(R.string.application_alert_dialog_title),
                    getString(R.string.room_choose_type_error), getString(R.string.common_ok), null, null);
            return false;
        }

        try {
            int data_deposit = Integer.parseInt(fragment_room_edit_et_deposit.getText().toString().trim());
            int min_deposit = 0;
            if (currentRentingStatus && data_deposit < (min_deposit = setting.getDeposit())) {
                String deposit_text = String.format(getString(R.string.room_insert_deposit_under_warning), HouseRentalUtils.toThousandVND(min_deposit));
                Toast.makeText(getActiveActivity(), deposit_text, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void onAgreed(int id, Object onWhat) {
        switch (id) {
            case Constant.REMOVE_RENTAL_DIALOG:
                // remove user of room
                initialRentingStatus = false;
                currentRentingStatus = false;
                room.setRented(false);
                room.setDeposit(0);
                fragment_room_edit_et_deposit.setText("0");
                DAOManager.removeProceedingOfRoom(room.getId());
                DAOManager.removeUsersOfRoom(room.getId());
                DAOManager.updateRoom(room.getId(),
                        fragment_room_edit_et_name.getText().toString().trim(),
                        Integer.parseInt(fragment_room_edit_et_area.getText().toString().trim()),
                        ((RoomTypeDAO) fragment_room_edit_sn_type.getSelectedItem()).getId(),
                        false, null,
                        Integer.parseInt(fragment_room_edit_et_electric.getText().toString().trim()),
                        Integer.parseInt(fragment_room_edit_et_water.getText().toString().trim()),
                        0,
                        ((FloorDAO) fragment_room_edit_sn_floor.getSelectedItem()).getId());
                showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title),
                        getString(R.string.room_alert_dialog_update_success),
                        getString((R.string.common_ok)), null, null);
                break;
        }
    }

    @Override
    public void onDisAgreed(int id, Object onWhat) {
        switch (id) {
            case Constant.REMOVE_RENTAL_DIALOG:
                fragment_room_edit_tv_rented_date.performClick();
                break;
        }
    }

    @Override
    public void onNeutral(int id, Object onWhat) {

    }
}
