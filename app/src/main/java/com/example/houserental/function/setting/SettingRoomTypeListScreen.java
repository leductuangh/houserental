package com.example.houserental.function.setting;

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
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.RoomTypeDAO;

import java.util.List;

import core.base.BaseMultipleFragment;
import core.data.DataSaver;

/**
 * Created by Tyrael on 4/28/16.
 */
public class SettingRoomTypeListScreen extends BaseMultipleFragment implements SettingRoomTypeAdapter.OnDeleteRoomTypeListener, AdapterView.OnItemClickListener, DialogInterface.OnDismissListener {

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
        Long selected_room_type = -1L;
        try {
            selected_room_type = DataSaver.getInstance().getLong(DataSaver.Key.ROOM_TYPE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter = new SettingRoomTypeAdapter(types, selected_room_type, this);
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
        fragment_setting_room_type_list_lv_types.setOnItemClickListener(this);
    }

    @Override
    public void onInitializeViewData() {

    }

    @Override
    public void onBaseResume() {

    }

    @Override
    public void onBaseFree() {

    }

    @Override
    public void onSingleClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_setting_room_type_list_bt_add:
                SettingInsertRoomTypeDialog dialog = new SettingInsertRoomTypeDialog(getActiveActivity());
                dialog.setOnDismissListener(this);
                dialog.show();
                break;
        }
    }

    @Override
    public void onDeleteRoomType(RoomTypeDAO roomType) {
        try {
            Long selected_room_type = DataSaver.getInstance().getLong(DataSaver.Key.ROOM_TYPE);
            DAOManager.deleteRoomType(roomType.getId());
            types.remove(roomType);
            if (selected_room_type == roomType.getId()) {
                if (types != null && types.size() > 0) {
                    adapter.setSelectedRoomType(types.get(0).getId());
                    DataSaver.getInstance().setLong(DataSaver.Key.ROOM_TYPE, types.get(0).getId());
                } else {
                    DataSaver.getInstance().setLong(DataSaver.Key.ROOM_TYPE, -1L);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            DataSaver.getInstance().setLong(DataSaver.Key.ROOM_TYPE, types.get(position).getId());
            adapter.setSelectedRoomType(types.get(position).getId());
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (types != null)
            types.clear();
        types.addAll(DAOManager.getAllRoomTypes());
        try {
            DataSaver.getInstance().setLong(DataSaver.Key.ROOM_TYPE, types.get(types.size() - 1).getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter.setSelectedRoomType(types.get(types.size() - 1).getId());
        adapter.notifyDataSetChanged();
    }
}
