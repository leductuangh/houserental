package com.example.houserental.function.room;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.houserental.R;
import com.example.houserental.core.base.BaseMultipleFragment;
import com.example.houserental.function.MainActivity;
import com.example.houserental.function.user.UserDetailScreen;
import com.example.houserental.function.user.UserInsertScreen;
import com.example.houserental.model.DAOManager;
import com.example.houserental.model.RoomDAO;
import com.example.houserental.model.UserDAO;

import java.util.List;

/**
 * Created by leductuan on 3/6/16.
 */
public class RoomDetailScreen extends BaseMultipleFragment implements AdapterView.OnItemClickListener {

    public static final String TAG = RoomDetailScreen.class.getSimpleName();
    private static final String ROOM_KEY = "room_key";
    private RoomDAO room;
    private List<UserDAO> users;
    private RoomDetailUserAdapter adapter;
    private ListView fragment_room_detail_lv_user;
    private TextView fragment_room_detail_tv_floor, fragment_room_detail_tv_name, fragment_room_detail_tv_type, fragment_room_detail_tv_area, fragment_room_detail_tv_rented, fragment_room_detail_tv_user_count;


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
        return inflater.inflate(R.layout.fragment_room_detail, container, false);
    }

    @Override
    public void onBaseCreate() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            room = (RoomDAO) bundle.getSerializable(ROOM_KEY);
        }

        if (room != null) {
            users = DAOManager.getUsersOfRoom(room.getRoomId());
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
        findViewById(R.id.fragment_room_detail_bt_edit);
        fragment_room_detail_tv_name = (TextView) findViewById(R.id.fragment_room_detail_tv_name);
        fragment_room_detail_tv_floor = (TextView) findViewById(R.id.fragment_room_detail_tv_floor);
        fragment_room_detail_tv_area = (TextView) findViewById(R.id.fragment_room_detail_tv_area);
        fragment_room_detail_tv_type = (TextView) findViewById(R.id.fragment_room_detail_tv_type);
        fragment_room_detail_tv_rented = (TextView) findViewById(R.id.fragment_room_detail_tv_rented);
        fragment_room_detail_tv_user_count = (TextView) findViewById(R.id.fragment_room_detail_tv_user_count);
        fragment_room_detail_lv_user = (ListView) findViewById(R.id.fragment_room_detail_lv_user);
        fragment_room_detail_lv_user.setAdapter(adapter);
        fragment_room_detail_lv_user.setOnItemClickListener(this);
    }

    @Override
    public void onInitializeViewData() {
        // will be initialized on Base resume (page will be refreshed after update information or add more user)
    }

    @Override
    public void onBaseResume() {
        if (room != null) {
            ((MainActivity) getActiveActivity()).setScreenHeader(getString(R.string.common_detail) + " " + room.getName());
            fragment_room_detail_tv_type.setText(room.getType().toString());
            fragment_room_detail_tv_floor.setText(DAOManager.getFloor(room.getFloor()).getName());
            fragment_room_detail_tv_name.setText(room.getName());
            fragment_room_detail_tv_area.setText(String.format("%s %s", room.getArea(), getString(R.string.room_insert_area_unit)));
            fragment_room_detail_tv_rented.setText(room.isRented() ? getString(R.string.room_rented_text) : getString(R.string.room_not_rented_text));
            fragment_room_detail_tv_user_count.setText(users.size() - 1 + "");
        }
    }

    @Override
    public void onBaseFree() {

    }

    @Override
    public void onSingleClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_room_detail_bt_edit:
                addFragment(R.id.activity_main_container, RoomEditScreen.getInstance(room), RoomEditScreen.TAG);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            addFragment(R.id.activity_main_container, UserInsertScreen.getInstance(room), UserInsertScreen.TAG);
        } else {
            addFragment(R.id.activity_main_container, UserDetailScreen.getInstance((UserDAO) parent.getItemAtPosition(position)), UserDetailScreen.TAG);
        }
    }
}
