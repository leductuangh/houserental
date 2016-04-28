package com.example.houserental.function.setting;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.houserental.R;
import com.example.houserental.function.model.OwnerDAO;

import java.util.List;

import core.base.BaseApplication;

/**
 * Created by Tyrael on 3/16/16.
 */
public class SettingOwnerListAdapter extends BaseAdapter implements View.OnClickListener {

    private List<OwnerDAO> data;
    private OnDeleteOwnerListener listener;
    private Long selected_position = -1L;

    public SettingOwnerListAdapter(List<OwnerDAO> data, Long selected_position, OnDeleteOwnerListener listener) {
        this.data = data;
        this.listener = listener;
        this.selected_position = selected_position;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public OwnerDAO getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        View row = convertView;
        if (row == null) {
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(R.layout.fragment_setting_owner_list_item, null);
            holder = new Holder();
            holder.fragment_setting_owner_list_item_tv_name = (TextView) row.findViewById(R.id.fragment_setting_owner_list_item_tv_name);
            holder.fragment_setting_owner_list_item_bt_delete = (Button) row.findViewById(R.id.fragment_setting_owner_list_item_bt_delete);
            holder.fragment_setting_owner_list_item_bt_delete.setOnClickListener(this);
            row.setTag(holder);
        }
        holder = (Holder) row.getTag();
        holder.fragment_setting_owner_list_item_bt_delete.setTag(getItem(position));
        holder.fragment_setting_owner_list_item_tv_name.setText(getItem(position).getName());
        if (getItem(position).getId() == selected_position) {
            row.setBackgroundResource(R.color.Aquamarine);
        } else {
            row.setBackgroundResource(android.R.color.white);
        }
        return row;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onDeleteOwner((OwnerDAO) v.getTag());
        }
    }

    public void setSelectedOwner(Long id) {
        selected_position = id;
    }

    interface OnDeleteOwnerListener {
        void onDeleteOwner(OwnerDAO owner);
    }

    private class Holder {
        TextView fragment_setting_owner_list_item_tv_name;
        Button fragment_setting_owner_list_item_bt_delete;
    }
}
