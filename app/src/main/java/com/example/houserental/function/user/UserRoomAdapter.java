package com.example.houserental.function.user;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.houserental.R;
import com.example.houserental.function.HouseRentalApplication;
import com.example.houserental.function.model.RoomDAO;

import java.util.List;

import core.base.BaseApplication;

/**
 * Created by Tyrael on 3/7/16.
 */
public class UserRoomAdapter extends BaseAdapter {

    private List<RoomDAO> data;

    public UserRoomAdapter(List<RoomDAO> data) {
        this.data = data;
        RoomDAO room = new RoomDAO();
        room.setName(HouseRentalApplication.getContext().getString(R.string.common_user_choose_room));
        this.data.add(room);
    }

    @Override
    public int getCount() {
        int count = data.size();
        if (count > 0)
            count = count - 1;
        return count;
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
        RoomDAO room = getItem(position);
        if (row == null) {
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(com.example.houserental.R.layout.fragment_user_insert_room_item, null);
            holder = new Holder();
            holder.fragment_user_insert_tv_room = (TextView) row.findViewById(com.example.houserental.R.id.fragment_user_insert_tv_room);
            row.setTag(holder);
        }
        holder = (Holder) row.getTag();
        if (room.getName().equals(HouseRentalApplication.getContext().getString(R.string.common_user_choose_room))) {
            holder.fragment_user_insert_tv_room.setTextColor(HouseRentalApplication.getContext().getResources().getColor(R.color.LightGrey));
        } else {
            holder.fragment_user_insert_tv_room.setTextColor(HouseRentalApplication.getContext().getResources().getColor(R.color.DarkerGray));
        }
        holder.fragment_user_insert_tv_room.setText(room.getName());

        return row;
    }

    @Override
    public void notifyDataSetChanged() {
        RoomDAO room = new RoomDAO();
        room.setName(HouseRentalApplication.getContext().getString(R.string.common_user_choose_room));
        this.data.add(room);
        super.notifyDataSetChanged();
    }

    private class Holder {
        TextView fragment_user_insert_tv_room;
    }
}
