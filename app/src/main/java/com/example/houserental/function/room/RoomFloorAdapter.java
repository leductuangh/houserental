package com.example.houserental.function.room;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.houserental.R;
import com.example.houserental.function.model.FloorDAO;

import java.util.List;

import core.base.BaseApplication;

/**
 * Created by Tyrael on 3/7/16.
 */
public class RoomFloorAdapter extends BaseAdapter {

    private List<FloorDAO> data;
    private boolean isInsert = true;

    public RoomFloorAdapter(List<FloorDAO> data, boolean isInsert) {
        this.data = data;
        this.isInsert = isInsert;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public FloorDAO getItem(int position) {
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
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(R.layout.fragment_room_floor_item, null);
            holder = new Holder();
            holder.fragment_room_floor_item_tv_floor = (TextView) row.findViewById(R.id.fragment_room_floor_item_tv_floor);
            row.setTag(holder);
        }
        holder = (Holder) row.getTag();

//        if (isInsert) {
//            if (position == 0) {
//                // first item
//                holder.fragment_room_floor_item_tv_floor.setText(BaseApplication.getContext().getString(R.string.common_room_choose_floor));
//            } else {
//                holder.fragment_room_floor_item_tv_floor.setText(getItem(position).getName());
//            }
//        } else {
//
//        }

        holder.fragment_room_floor_item_tv_floor.setText(getItem(position).getName());

        return row;
    }

    private class Holder {
        TextView fragment_room_floor_item_tv_floor;
    }
}
