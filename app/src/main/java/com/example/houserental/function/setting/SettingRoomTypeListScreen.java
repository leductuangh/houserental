package com.example.houserental.function.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.houserental.R;
import com.example.houserental.function.MainActivity;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.RoomTypeDAO;

import java.util.List;

import core.base.BaseMultipleFragment;

/**
 * Created by Tyrael on 4/28/16.
 */
public class SettingRoomTypeListScreen extends BaseMultipleFragment implements DialogInterface.OnDismissListener {

    public static final String TAG = SettingRoomTypeListScreen.class.getName();
    private ListView fragment_setting_room_type_list_lv_types;
    private SettingRoomTypeAdapter adapter;
    private List<RoomTypeDAO> types;

    public static SettingRoomTypeListScreen getInstance() {
        return new SettingRoomTypeListScreen();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting_room_type_list, container, false);
    }

    @Override
    public void onBaseCreate() {
        types = DAOManager.getAllRoomTypes();
        adapter = new SettingRoomTypeAdapter(types);
    }

    @Override
    public void onDeepLinking(Intent data) {

    }

    @Override
    public void onNotification(Intent data) {

    }

    @Override
    public void onBindView() {
        findViewById(R.id.fragment_setting_room_type_list_bt_add);
        fragment_setting_room_type_list_lv_types = (ListView) findViewById(R.id.fragment_setting_room_type_list_lv_types);
        fragment_setting_room_type_list_lv_types.setAdapter(adapter);
    }

    @Override
    public void onInitializeViewData() {

    }

    @Override
    public void onBaseResume() {
        ((MainActivity) getActiveActivity()).setScreenHeader(getString(R.string.setting_room_type_list_header));
    }

    @Override
    public void onBaseFree() {

    }

    @Override
    public void onSingleClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_setting_room_type_list_bt_add:
                SettingRoomTypeDialog dialog = new SettingRoomTypeDialog(getActiveActivity(), null, null);
                dialog.setOnDismissListener(this);
                dialog.show();
                break;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (types != null)
            types.clear();
        types.addAll(DAOManager.getAllRoomTypes());
        adapter.notifyDataSetChanged();
    }
}
