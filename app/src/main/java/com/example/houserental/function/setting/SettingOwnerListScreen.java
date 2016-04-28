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
import com.example.houserental.function.model.OwnerDAO;

import java.util.List;

import core.base.BaseMultipleFragment;
import core.data.DataSaver;

/**
 * Created by Tyrael on 4/26/16.
 */
public class SettingOwnerListScreen extends BaseMultipleFragment implements SettingOwnerListAdapter.OnDeleteOwnerListener, DialogInterface.OnDismissListener, AdapterView.OnItemClickListener {

    public static final String TAG = SettingOwnerListScreen.class.getName();
    private ListView fragment_setting_owner_list_lv_owners;
    private List<OwnerDAO> owners;
    private SettingOwnerListAdapter adapter;

    public static SettingOwnerListScreen getInstance() {
        return new SettingOwnerListScreen();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting_owner_list, container, false);
    }

    @Override
    public void onBaseCreate() {
        owners = DAOManager.getAllOwners();
        Long selected_owner = -1L;
        try {
            selected_owner = DataSaver.getInstance().getLong(DataSaver.Key.OWNER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter = new SettingOwnerListAdapter(owners, selected_owner, this);
    }

    @Override
    public void onDeepLinking(Intent data) {

    }

    @Override
    public void onNotification(Intent data) {

    }

    @Override
    public void onBindView() {
        findViewById(R.id.fragment_setting_owner_list_bt_add);
        fragment_setting_owner_list_lv_owners = (ListView) findViewById(R.id.fragment_setting_owner_list_lv_owners);
        fragment_setting_owner_list_lv_owners.setAdapter(adapter);
        fragment_setting_owner_list_lv_owners.setOnItemClickListener(this);
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
            case R.id.fragment_setting_owner_list_bt_add:
                SettingInsertOwnerDialog dialog = new SettingInsertOwnerDialog(getActiveActivity());
                dialog.setOnDismissListener(this);
                dialog.show();
                break;
        }
    }

    @Override
    public void onDeleteOwner(OwnerDAO owner) {
        try {
            Long selected_owner = DataSaver.getInstance().getLong(DataSaver.Key.OWNER);
            DAOManager.deleteOwner(owner.getId());
            owners.remove(owner);
            if (selected_owner == owner.getId()) {
                if (owners != null && owners.size() > 0) {
                    adapter.setSelectedOwner(owners.get(0).getId());
                    DataSaver.getInstance().setLong(DataSaver.Key.OWNER, owners.get(0).getId());
                } else {
                    DataSaver.getInstance().setLong(DataSaver.Key.OWNER, -1L);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (owners != null)
            owners.clear();
        owners.addAll(DAOManager.getAllOwners());
        try {
            DataSaver.getInstance().setLong(DataSaver.Key.OWNER, owners.get(owners.size() - 1).getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter.setSelectedOwner(owners.get(owners.size() - 1).getId());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            DataSaver.getInstance().setLong(DataSaver.Key.OWNER, owners.get(position).getId());
            adapter.setSelectedOwner(owners.get(position).getId());
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
