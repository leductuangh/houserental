package com.example.houserental.function.setting;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.houserental.R;
import com.example.houserental.function.model.RoomTypeDAO;

import java.util.List;

import core.base.BaseApplication;

/**
 * Created by Tyrael on 3/7/16.
 */
public class SettingRoomTypeAdapter extends BaseAdapter implements View.OnClickListener {

    private List<RoomTypeDAO> data;
    private OnDeleteRoomTypeListener listener;
    private Long selected_position = -1L;

    public SettingRoomTypeAdapter(List<RoomTypeDAO> data, Long selected_room_type, OnDeleteRoomTypeListener listener) {
        this.data = data;
        this.listener = listener;
        this.selected_position = selected_room_type;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public RoomTypeDAO getItem(int position) {
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
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(com.example.houserental.R.layout.fragment_setting_room_type_item, null);
            holder = new Holder();
            holder.fragment_setting_room_type_item_tv_type = (TextView) row.findViewById(com.example.houserental.R.id.fragment_setting_room_type_item_tv_type);
            holder.fragment_setting_room_type_item_bt_delete = (Button) row.findViewById(com.example.houserental.R.id.fragment_setting_room_type_item_bt_delete);
            holder.fragment_setting_room_type_item_bt_delete.setOnClickListener(this);
            row.setTag(holder);
        }
        holder = (Holder) row.getTag();
        holder.fragment_setting_room_type_item_bt_delete.setTag(getItem(position));
        holder.fragment_setting_room_type_item_tv_type.setText(String.format("%s %s %s", getItem(position).getName(), BaseApplication.getContext().getString(com.example.houserental.R.string.room_price_title), getItem(position).getPrice()));
        if (getItem(position).getId() == selected_position) {
            row.setBackgroundResource(R.color.Aquamarine);
        } else {
            row.setBackgroundResource(android.R.color.white);
        }
        return row;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == com.example.houserental.R.id.fragment_setting_room_type_item_bt_delete) {
            if (listener != null) {
                listener.onDeleteRoomType((RoomTypeDAO) v.getTag());
            }
        }
    }

    public void setSelectedRoomType(Long id) {
        selected_position = id;
    }

    public interface OnDeleteRoomTypeListener {
        void onDeleteRoomType(RoomTypeDAO roomType);
    }

    private class Holder {
        TextView fragment_setting_room_type_item_tv_type;
        Button fragment_setting_room_type_item_bt_delete;
    }
}
