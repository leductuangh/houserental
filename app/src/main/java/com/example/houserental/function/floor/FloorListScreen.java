package com.example.houserental.function.floor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.core.core.base.BaseApplication;
import com.core.core.base.BaseMultipleFragment;
import com.core.dialog.GeneralDialog;
import com.core.util.Constant;
import com.example.houserental.function.MainActivity;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.FloorDAO;
import com.example.houserental.function.room.RoomListScreen;

import java.util.List;

/**
 * Created by leductuan on 3/6/16.
 */
public class FloorListScreen extends BaseMultipleFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, GeneralDialog.DecisionListener, GeneralDialog.ConfirmListener {

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
        data.add(0, null);
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
        fragment_floor_list_lv_floors = (ListView) findViewById(com.example.houserental.R.id.fragment_floor_list_lv_floors);
        fragment_floor_list_lv_floors.setAdapter(adapter);
        fragment_floor_list_lv_floors.setOnItemClickListener(this);
        fragment_floor_list_lv_floors.setOnItemLongClickListener(this);
    }

    @Override
    public void onInitializeViewData() {

    }

    @Override
    public void onBaseResume() {
        ((MainActivity) getActiveActivity()).setScreenHeader(getString(com.example.houserental.R.string.main_header_floor));
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

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            DAOManager.addFloor("floor_" + parent.getCount(), BaseApplication.getContext().getString(com.example.houserental.R.string.common_floor) + " " + parent.getCount(), parent.getCount());
            refreshFloorList();
        } else {
            addFragment(com.example.houserental.R.id.activity_main_container, RoomListScreen.getInstance((FloorDAO) parent.getItemAtPosition(position)), RoomListScreen.TAG);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            return true;
        }
        if (position < data.size() - 1) {
            showAlertDialog(getActiveActivity(),
                    Constant.DELETE_FLOOR_ERROR_DIALOG,
                    -1,
                    String.format(getString(com.example.houserental.R.string.delete_dialog_title),
                            ((FloorDAO) parent.getItemAtPosition(position)).getName()),
                    getString(com.example.houserental.R.string.delete_floor_error_dialog_message),
                    getString(com.example.houserental.R.string.common_ok), this);
            return true;
        }
        showDecisionDialog(getActiveActivity(),
                Constant.DELETE_FLOOR_DIALOG,
                -1,
                String.format(getString(com.example.houserental.R.string.delete_dialog_title), ((FloorDAO) parent.getItemAtPosition(position)).getName()),
                String.format(getString(com.example.houserental.R.string.delete_dialog_message), ((FloorDAO) parent.getItemAtPosition(position)).getName())
                        + "\n"
                        + getString(com.example.houserental.R.string.delete_floor_dialog_message),
                getString(com.example.houserental.R.string.common_ok),
                getString(com.example.houserental.R.string.common_cancel), null, this);
        return true;
    }

    private void refreshFloorList() {
        if (data != null) {
            data.clear();
            data.addAll(DAOManager.getAllFloors());
            data.add(0, null);
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onAgreed(int id) {
        switch (id) {
            case Constant.DELETE_FLOOR_DIALOG:
                DAOManager.deleteFloor(data.get(data.size() - 1).getFloorId());
                refreshFloorList();
                break;
        }
    }

    @Override
    public void onDisAgreed(int id) {
        switch (id) {
            case Constant.DELETE_FLOOR_DIALOG:
                break;
        }
    }

    @Override
    public void onNeutral(int id) {
        switch (id) {
            case Constant.DELETE_FLOOR_DIALOG:
                break;
        }
    }

    @Override
    public void onConfirmed(int id) {
        switch (id) {
            case Constant.DELETE_FLOOR_ERROR_DIALOG:
                break;
        }
    }
}
