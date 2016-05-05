package com.example.houserental.function.floor;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.houserental.R;
import com.example.houserental.function.MainActivity;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.FloorDAO;
import com.example.houserental.function.room.RoomListScreen;

import java.util.List;

import core.base.BaseMultipleFragment;

/**
 * Created by leductuan on 3/6/16.
 */
public class FloorListScreen extends BaseMultipleFragment implements AdapterView.OnItemClickListener, DialogInterface.OnDismissListener {

    public static final String TAG = FloorListScreen.class.getSimpleName();
    private FloorListAdapter adapter;
    private List<FloorDAO> data;
    private ListView fragment_floor_list_lv_floors;


    public static FloorListScreen getInstance() {
        return new FloorListScreen();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(com.example.houserental.R.layout.fragment_floor_list, container, false);
    }

    @Override
    public void onBaseCreate() {
        data = DAOManager.getAllFloors();
        adapter = new FloorListAdapter(data);
    }

    @Override
    public void onDeepLinking(Intent data) {

    }

    @Override
    public void onNotification(Intent data) {

    }

    @Override
    public void onBindView() {
        findViewById(R.id.fragment_floor_list_fab_add);
        fragment_floor_list_lv_floors = (ListView) findViewById(R.id.fragment_floor_list_lv_floors);
        fragment_floor_list_lv_floors.setAdapter(adapter);
        fragment_floor_list_lv_floors.setOnItemClickListener(this);
    }

    @Override
    public void onInitializeViewData() {

    }

    @Override
    public void onBaseResume() {
        ((MainActivity) getActiveActivity()).setScreenHeader(getString(R.string.main_header_floor));
        refreshFloorList();
    }

    @Override
    public void onBaseFree() {
        if (adapter != null)
            adapter.clearData();
        if (data != null)
            data.clear();
    }

    @Override
    public void onSingleClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_floor_list_fab_add:
                FloorInsertDialog dialog = new FloorInsertDialog(getActiveActivity());
                dialog.setOnDismissListener(this);
                dialog.show();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        addFragment(R.id.activity_main_container, RoomListScreen.getInstance((FloorDAO) parent.getItemAtPosition(position)), RoomListScreen.TAG);
    }

    private void refreshFloorList() {
        if (data != null) {
            data.clear();
            data.addAll(DAOManager.getAllFloors());
        }

        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        refreshFloorList();
    }
}
