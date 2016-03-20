package com.example.houserental.function.user;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.core.core.base.BaseApplication;
import com.example.houserental.function.model.RoomDAO;

import java.util.List;

/**
 * Created by Tyrael on 3/7/16.
 */
public class UserRoomAdapter extends BaseAdapter {

    private List<RoomDAO> data;
    private boolean isInsert = true;

    public UserRoomAdapter(List<RoomDAO> data, boolean isInsert) {
        this.data = data;
        this.isInsert = isInsert;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public RoomDAO getItem(int position) {
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
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(com.example.houserental.R.layout.fragment_user_insert_room_item, null);
            holder = new Holder();
            holder.fragment_user_insert_tv_room = (TextView) row.findViewById(com.example.houserental.R.id.fragment_user_insert_tv_room);
            row.setTag(holder);
        }
        holder = (Holder) row.getTag();
        if (isInsert) {
            if (position == 0) {
                // first item
                holder.fragment_user_insert_tv_room.setText(BaseApplication.getContext().getString(com.example.houserental.R.string.common_user_choose_room));
                holder.fragment_user_insert_tv_room.setTextColor(Color.RED);
            } else {
                holder.fragment_user_insert_tv_room.setText(getItem(position).getName());
                holder.fragment_user_insert_tv_room.setTextColor(Color.BLACK);
            }
        } else {
            holder.fragment_user_insert_tv_room.setText(getItem(position).getName());
            holder.fragment_user_insert_tv_room.setTextColor(Color.BLACK);
        }

        return row;
    }

    private class Holder {
        TextView fragment_user_insert_tv_room;
    }
}
