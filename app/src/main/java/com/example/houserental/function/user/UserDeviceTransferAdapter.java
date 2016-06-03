package com.example.houserental.function.user;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.houserental.R;
import com.example.houserental.function.model.UserDAO;

import java.util.List;

import core.base.BaseApplication;

/**
 * Created by leductuan on 3/7/16.
 */
public class UserDeviceTransferAdapter extends BaseAdapter {

    private List<UserDAO> data;

    public UserDeviceTransferAdapter(List<UserDAO> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public UserDAO getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        View row = convertView;
        if (row == null) {
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(R.layout.fragment_user_device_transfer_item, null);
            holder = new Holder();
            holder.fragment_user_device_transfer_item_tv_name = (TextView) row.findViewById(R.id.fragment_user_device_transfer_item_tv_name);
            row.setTag(holder);
        }
        holder = (Holder) row.getTag();
        holder.fragment_user_device_transfer_item_tv_name.setText(getItem(position).getName());
        return row;
    }

    private class Holder {
        TextView fragment_user_device_transfer_item_tv_name;
    }
}
