package com.example.houserental.function.user;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.houserental.R;
import com.example.houserental.function.model.DeviceDAO;

import java.util.List;

import core.base.BaseApplication;

/**
 * Created by leductuan on 3/12/16.
 */
public class UserDeviceAdapter extends BaseAdapter {

    private List<DeviceDAO> data;

    public UserDeviceAdapter(List<DeviceDAO> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public DeviceDAO getItem(int position) {
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
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(R.layout.fragment_user_detail_device_item, null);
            holder = new Holder();
            holder.fragment_user_detail_device_item_tv = (TextView) row.findViewById(R.id.fragment_user_detail_device_item_tv);
            row.setTag(holder);
        }
        holder = (Holder) row.getTag();
        holder.fragment_user_detail_device_item_tv.setText(getItem(position).getMAC());
        return row;
    }

    private class Holder {
        TextView fragment_user_detail_device_item_tv;
    }
}
