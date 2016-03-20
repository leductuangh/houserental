package com.example.houserental.function.room;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.core.core.base.BaseMultipleFragment;
import com.core.dialog.GeneralDialog;
import com.core.util.Constant;
import com.example.houserental.function.MainActivity;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.FloorDAO;
import com.example.houserental.function.model.RoomDAO;

import java.util.List;

/**
 * Created by leductuan on 3/5/16.
 */
public class RoomListScreen extends BaseMultipleFragment implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener, GeneralDialog.DecisionListener {

    public static final String TAG = RoomListScreen.class.getSimpleName();
    private static final String FLOOR_KEY = "floor_key";
    private List<RoomDAO> data;
    private RoomListAdapter adapter;
    private ListView fragment_room_list_lv_rooms;
    private FloorDAO floor;
    private String deleted_room;

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
        return inflater.inflate(com.example.houserental.R.layout.fragment_room_list, container, false);
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
        data.add(0, null);
        adapter = new RoomListAdapter(data, true);
    }

    @Override
    public void onDeepLinking(Intent data) {

    }

    @Override
    public void onNotification(Intent data) {

    }

    @Override
    public void onBindView() {
        fragment_room_list_lv_rooms = (ListView) findViewById(com.example.houserental.R.id.fragment_room_list_lv_rooms);
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
        ((MainActivity) getActiveActivity()).setScreenHeader(getString(com.example.houserental.R.string.main_header_room) + " " + floor_name);
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
        if (position == 0)
            return true;
        deleted_room = ((RoomDAO) parent.getItemAtPosition(position)).getRoomId();
        showDecisionDialog(getActiveActivity(), Constant.DELETE_ROOM_DIALOG, -1, getString(com.example.houserental.R.string.application_alert_dialog_title), getString(com.example.houserental.R.string.delete_room_dialog_message), getString(com.example.houserental.R.string.common_ok), getString(com.example.houserental.R.string.common_cancel), null, this);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            addFragment(com.example.houserental.R.id.activity_main_container, RoomInsertScreen.getInstance(floor), RoomInsertScreen.TAG);
        } else {
            addFragment(com.example.houserental.R.id.activity_main_container, RoomDetailScreen.getInstance((RoomDAO) parent.getItemAtPosition(position)), RoomDetailScreen.TAG);
        }
    }

    private void refreshRoomList() {
        if (data != null) {
            data.clear();
            if (floor == null)
                data.addAll(DAOManager.getAllRooms());
            else
                data.addAll(DAOManager.getRoomsOfFloor(floor.getFloorId()));
            data.add(0, null);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAgreed(int id) {
        if (id == Constant.DELETE_ROOM_DIALOG) {
            DAOManager.deleteRoom(deleted_room);
            refreshRoomList();
        }
    }

    @Override
    public void onDisAgreed(int id) {

    }

    @Override
    public void onNeutral(int id) {

    }
}
