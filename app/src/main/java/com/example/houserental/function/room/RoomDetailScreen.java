package com.example.houserental.function.room;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.houserental.R;
import com.example.houserental.function.MainActivity;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.RoomDAO;
import com.example.houserental.function.model.RoomTypeDAO;
import com.example.houserental.function.model.UserDAO;
import com.example.houserental.function.user.UserDetailScreen;
import com.example.houserental.function.user.UserInsertScreen;

import java.util.List;

import core.base.BaseMultipleFragment;
import core.dialog.GeneralDialog;
import core.util.Constant;

/**
 * Created by leductuan on 3/6/16.
 */
public class RoomDetailScreen extends BaseMultipleFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, GeneralDialog.DecisionListener {

    public static final String TAG = RoomDetailScreen.class.getSimpleName();
    private static final String ROOM_KEY = "room_key";
    private RoomDAO room;
    private List<UserDAO> users;
    private RoomDetailUserAdapter adapter;
    private ListView fragment_room_detail_lv_user;
    private LinearLayout fragment_room_detail_ll_user;
    private TextView fragment_room_detail_tv_deposit, fragment_room_detail_tv_electric, fragment_room_detail_tv_water, fragment_room_detail_tv_floor, fragment_room_detail_tv_name, fragment_room_detail_tv_type, fragment_room_detail_tv_area, fragment_room_detail_tv_rented, fragment_room_detail_tv_user_count;
    private Long deleted_user;


    public static RoomDetailScreen getInstance(RoomDAO room) {
        RoomDetailScreen screen = new RoomDetailScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ROOM_KEY, room);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(com.example.houserental.R.layout.fragment_room_detail, container, false);
    }

    @Override
    public void onBaseCreate() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            room = (RoomDAO) bundle.getSerializable(ROOM_KEY);
        }

        if (room != null) {
            users = DAOManager.getUsersOfRoom(room.getId());
        }
        users.add(0, null);
        adapter = new RoomDetailUserAdapter(users);
    }

    @Override
    public void onDeepLinking(Intent data) {

    }

    @Override
    public void onNotification(Intent data) {

    }

    @Override
    public void onBindView() {
        findViewById(com.example.houserental.R.id.fragment_room_detail_bt_edit);
        findViewById(com.example.houserental.R.id.fragment_room_detail_bt_delete);
        fragment_room_detail_ll_user = (LinearLayout) findViewById(R.id.fragment_room_detail_ll_user);
        fragment_room_detail_tv_deposit = (TextView) findViewById(R.id.fragment_room_detail_tv_deposit);
        fragment_room_detail_tv_electric = (TextView) findViewById(com.example.houserental.R.id.fragment_room_detail_tv_electric);
        fragment_room_detail_tv_water = (TextView) findViewById(com.example.houserental.R.id.fragment_room_detail_tv_water);
        fragment_room_detail_tv_name = (TextView) findViewById(com.example.houserental.R.id.fragment_room_detail_tv_name);
        fragment_room_detail_tv_floor = (TextView) findViewById(com.example.houserental.R.id.fragment_room_detail_tv_floor);
        fragment_room_detail_tv_area = (TextView) findViewById(com.example.houserental.R.id.fragment_room_detail_tv_area);
        fragment_room_detail_tv_type = (TextView) findViewById(com.example.houserental.R.id.fragment_room_detail_tv_type);
        fragment_room_detail_tv_rented = (TextView) findViewById(com.example.houserental.R.id.fragment_room_detail_tv_rented);
        fragment_room_detail_tv_user_count = (TextView) findViewById(com.example.houserental.R.id.fragment_room_detail_tv_user_count);
        fragment_room_detail_lv_user = (ListView) findViewById(com.example.houserental.R.id.fragment_room_detail_lv_user);
        fragment_room_detail_lv_user.setAdapter(adapter);
        fragment_room_detail_lv_user.setOnItemClickListener(this);
        fragment_room_detail_lv_user.setOnItemLongClickListener(this);
    }

    @Override
    public void onInitializeViewData() {
        // will be initialized on Base resume (page will be refreshed after update information or add more user)
    }

    @Override
    public void onBaseResume() {
        if (room != null) {
            RoomTypeDAO typeDAO = room.getType();
            String roomType = (typeDAO == null) ? getString(com.example.houserental.R.string.common_unknown) : String.format("%s %s %s", typeDAO.getName(), getString(com.example.houserental.R.string.room_price_title), typeDAO.getPrice());
            ((MainActivity) getActiveActivity()).setScreenHeader(getString(com.example.houserental.R.string.common_detail) + " " + room.getName());
            fragment_room_detail_tv_type.setText(roomType);
            fragment_room_detail_tv_floor.setText(DAOManager.getFloor(room.getFloor()).getName());
            fragment_room_detail_tv_name.setText(room.getName());
            fragment_room_detail_tv_area.setText(String.format("%s %s", room.getArea(), getString(com.example.houserental.R.string.common_area_unit)));
            fragment_room_detail_tv_water.setText(room.getWaterNumber() + "");
            fragment_room_detail_tv_electric.setText(room.getElectricNumber() + "");
            fragment_room_detail_tv_deposit.setText(room.getDeposit() + "");
            fragment_room_detail_tv_rented.setText(room.isRented() ? getString(com.example.houserental.R.string.room_rented_text) : getString(com.example.houserental.R.string.room_not_rented_text));
            fragment_room_detail_ll_user.setVisibility(room.isRented() ? View.VISIBLE : View.GONE);
            refreshUserList();
        }
    }

    @Override
    public void onBaseFree() {

    }

    @Override
    public void onSingleClick(View v) {
        switch (v.getId()) {
            case com.example.houserental.R.id.fragment_room_detail_bt_edit:
                addFragment(com.example.houserental.R.id.activity_main_container, RoomEditScreen.getInstance(room), RoomEditScreen.TAG);
                break;
            case com.example.houserental.R.id.fragment_room_detail_bt_delete:
                showDecisionDialog(getActiveActivity(), Constant.DELETE_ROOM_DIALOG, -1, getString(com.example.houserental.R.string.application_alert_dialog_title), getString(com.example.houserental.R.string.delete_room_dialog_message), getString(com.example.houserental.R.string.common_ok), getString(com.example.houserental.R.string.common_cancel), null, null, this);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            addFragment(com.example.houserental.R.id.activity_main_container, UserInsertScreen.getInstance(room), UserInsertScreen.TAG);
        } else {
            addFragment(com.example.houserental.R.id.activity_main_container, UserDetailScreen.getInstance((UserDAO) parent.getItemAtPosition(position)), UserDetailScreen.TAG);
        }
    }

    private void refreshUserList() {
        if (room != null) {
            if (users != null)
                users.clear();
            users.addAll(DAOManager.getUsersOfRoom(room.getId()));
            users.add(0, null);
            adapter.notifyDataSetChanged();
            fragment_room_detail_tv_user_count.setText(users.size() - 1 + "");
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0)
            return true;
        deleted_user = ((UserDAO) parent.getItemAtPosition(position)).getId();
        showDecisionDialog(getActiveActivity(), Constant.DELETE_USER_DIALOG, -1, getString(com.example.houserental.R.string.application_alert_dialog_title), getString(com.example.houserental.R.string.delete_user_dialog_message), getString(com.example.houserental.R.string.common_ok), getString(com.example.houserental.R.string.common_cancel), null, null, this);
        return true;
    }

    @Override
    public void onAgreed(int id, Object onWhat) {
        if (id == Constant.DELETE_USER_DIALOG) {
            DAOManager.deleteUser(deleted_user);
            refreshUserList();
        } else if (id == Constant.DELETE_ROOM_DIALOG) {
            if (room != null)
                DAOManager.deleteRoom(room.getId());
            finish();
        }
    }

    @Override
    public void onDisAgreed(int id, Object onWhat) {

    }

    @Override
    public void onNeutral(int id, Object onWhat) {

    }
}
