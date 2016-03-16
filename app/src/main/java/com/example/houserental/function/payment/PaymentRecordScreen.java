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
import com.example.houserental.R;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.RoomDAO;
import com.example.houserental.function.model.UserDAO;
import com.example.houserental.function.room.RoomListAdapter;
import com.example.houserental.function.user.UserListAdapter;

import java.util.ArrayList;
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

    }

    @Override
    public void onInitializeViewData() {
        fragment_payment_record_sn_user.setAdapter(adapter = new UserListAdapter(users, false));
        fragment_payment_record_sn_room.setAdapter(new RoomListAdapter(DAOManager.getAllRentedRooms(), false));
    }

    @Override
    public void onBaseResume() {

    }

    @Override
    public void onBaseFree() {

    }

    @Override
    public void onSingleClick(View v) {
//        if (room != null) {
//            DAOManager.addPayment(room.getRoomId(), room.getName(), );
//        }
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
