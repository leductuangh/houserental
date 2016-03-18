package com.example.houserental.function.setting;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.core.core.base.BaseApplication;
import com.example.houserental.R;
import com.example.houserental.function.model.OwnerDAO;

import java.util.List;

/**
 * Created by Tyrael on 3/16/16.
 */
public class OwnerListAdapter extends BaseAdapter implements View.OnClickListener {

    private List<OwnerDAO> data;
    private OnDeleteOwnerListener listener;

    public OwnerListAdapter(List<OwnerDAO> data, OnDeleteOwnerListener listener) {
        this.data = data;
        this.listener = listener;
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
            holder.fragment_setting_owner_list_item_bt_delete.setVisibility(View.GONE);
            row.setTag(holder);
        }
        holder = (Holder) row.getTag();
        holder.fragment_setting_owner_list_item_tv_name.setText(getItem(position).getName());
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
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
        holder.fragment_setting_owner_list_item_bt_delete.setTag(position);
        holder.fragment_setting_owner_list_item_tv_name.setText(getItem(position).getName());
        return row;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onDeleteOwner((Integer) v.getTag());

        }
    }

    interface OnDeleteOwnerListener {
        void onDeleteOwner(int position);
    }

    private class Holder {
        TextView fragment_setting_owner_list_item_tv_name;
        Button fragment_setting_owner_list_item_bt_delete;
    }
}
