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
import com.example.houserental.core.base.BaseMultipleFragment;
import com.example.houserental.function.MainActivity;
import com.example.houserental.model.DAOManager;
import com.example.houserental.model.FloorDAO;
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
    private FloorDAO floor;

    public static RoomListScreen getInstance(FloorDAO floor) {
        RoomListScreen screen = new RoomListScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(FLOOR_KEY, floor);
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
            floor = (FloorDAO) bundle.getSerializable(FLOOR_KEY);
        }

        if (floor == null) {
            data = DAOManager.getAllRooms();
        } else {
            data = DAOManager.getRoomsOfFloor(floor.getFloorId());
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
        String floor_name = "";
        if (floor != null)
            floor_name = floor.getName();
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
        return position == parent.getCount() - 1;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == parent.getCount() - 1) {
            addFragment(R.id.activity_main_container, RoomInsertScreen.getInstance(floor), RoomInsertScreen.TAG);
        } else {

        }
    }

    private void refreshRoomList() {
        if (data != null) {
            data.clear();
            if (floor == null)
                data.addAll(DAOManager.getAllRooms());
            else
                data.addAll(DAOManager.getRoomsOfFloor(floor.getFloorId()));
            adapter.notifyDataSetChanged();
        }
    }
}
