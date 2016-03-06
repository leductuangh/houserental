package com.example.houserental.function.room;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.houserental.R;
import com.example.houserental.core.base.BaseApplication;
import com.example.houserental.core.base.BaseMultipleFragment;
import com.example.houserental.function.MainActivity;
import com.example.houserental.model.DAOManager;
import com.example.houserental.model.RoomDAO;

import java.util.List;

/**
 * Created by leductuan on 3/5/16.
 */
public class RoomListScreen extends BaseMultipleFragment implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    public static final String TAG = RoomListScreen.class.getSimpleName();
    private static final String FLOOR_KEY = "floor_key";
    private static final String FLOOR_NAME_KEY = "floor_name_key";
    private List<RoomDAO> data;
    private RoomListAdapter adapter;
    private ListView fragment_room_list_lv_rooms;
    private int floor = -1;
    private String floor_name = "";

    public static RoomListScreen getInstance(int floor, String floor_name) {
        RoomListScreen screen = new RoomListScreen();
        Bundle bundle = new Bundle();
        bundle.putInt(FLOOR_KEY, floor);
        bundle.putString(FLOOR_NAME_KEY, floor_name);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_room_list, container, false);
    }

    @Override
    public void onBaseCreate() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            floor = bundle.getInt(FLOOR_KEY);
            floor_name = bundle.getString(FLOOR_NAME_KEY);
        }

        if (floor == -1) {
            data = DAOManager.getAllRooms();
        } else {
            data = DAOManager.getRoomsOfFloor("floor_" + floor);
        }
        adapter = new RoomListAdapter(data);
    }

    @Override
    public void onDeepLinking(Intent data) {

    }

    @Override
    public void onNotification(Intent data) {

    }

    @Override
    public void onBindView() {
        fragment_room_list_lv_rooms = (ListView) findViewById(R.id.fragment_room_list_lv_rooms);
        fragment_room_list_lv_rooms.setAdapter(adapter);
        fragment_room_list_lv_rooms.setOnItemClickListener(this);
        fragment_room_list_lv_rooms.setOnItemLongClickListener(this);
    }

    @Override
    public void onInitializeViewData() {

    }

    @Override
    public void onBaseResume() {
        ((MainActivity) getActiveActivity()).setScreenHeader(getString(R.string.main_header_room) + " " + floor_name);
        refreshRoomList();
    }

    @Override
    public void onBaseFree() {

    }

    @Override
    public void onSingleClick(View v) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == parent.getCount() - 1) {
            return true;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == parent.getCount() - 1) {
            addFragment(R.id.activity_main_container, RoomInsertScreen.getInstance(floor, floor_name), RoomInsertScreen.TAG);
        } else {

        }
    }

    private void refreshRoomList() {
        if (data != null) {
            data.clear();
            if (floor == -1)
                data.addAll(DAOManager.getAllRooms());
            else
                data.addAll(DAOManager.getRoomsOfFloor("floor_" + floor));
            adapter.notifyDataSetChanged();
        }
    }
}
