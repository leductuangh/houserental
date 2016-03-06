package com.example.houserental.function.room;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.houserental.R;
import com.example.houserental.core.base.BaseMultipleFragment;
import com.example.houserental.function.MainActivity;
import com.example.houserental.model.DAOManager;
import com.example.houserental.model.RoomDAO;

/**
 * Created by leductuan on 3/6/16.
 */
public class RoomInsertScreen extends BaseMultipleFragment {

    private static final String FLOOR_KEY = "floor_key";
    private static final String FLOOR_NAME_KEY = "floor_name_key";
    public static final String TAG = RoomInsertScreen.class.getSimpleName();
    private int floor = -1;
    private String floor_name = "";
    private String id;
    private String name;
    private int area;
    private RoomDAO.Type type;
    private boolean isrented;

    public static RoomInsertScreen getInstance(int floor, String floor_name) {
        RoomInsertScreen screen = new RoomInsertScreen();
        Bundle bundle = new Bundle();
        bundle.putInt(FLOOR_KEY, floor);
        bundle.putString(FLOOR_NAME_KEY, floor_name);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_room_insert, container, false);
    }

    @Override
    public void onBaseCreate() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            floor = bundle.getInt(FLOOR_KEY);
            floor_name = bundle.getString(FLOOR_NAME_KEY);
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

    }

    @Override
    public void onInitializeViewData() {

    }

    @Override
    public void onBaseResume() {
        ((MainActivity) getActiveActivity()).setScreenHeader(getString(R.string.main_header_room_insert) + " " + floor_name);
    }

    @Override
    public void onBaseFree() {

    }

    @Override
    public void onSingleClick(View v) {

//        DAOManager.addRoom(id, name, area, type, isrented, floor);
    }
}
