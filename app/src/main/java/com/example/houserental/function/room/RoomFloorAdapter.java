package com.example.houserental.function.room;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.houserental.R;
import com.example.houserental.function.HouseRentalApplication;
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
        if (isInsert)
            data.add(new FloorDAO(HouseRentalApplication.getContext().getString(R.string.common_room_choose_floor), -1));
    }

    @Override
    public int getCount() {
        int count = data.size();
        if (isInsert) {
            if (count > 0)
                count = count - 1;
        }
        return count;
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
        FloorDAO floor = getItem(position);
        if (row == null) {
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(R.layout.fragment_room_floor_item, null);
            holder = new Holder();
            holder.fragment_room_floor_item_tv_floor = (TextView) row.findViewById(R.id.fragment_room_floor_item_tv_floor);
            row.setTag(holder);
        }
        holder = (Holder) row.getTag();
        if (isInsert) {
            if (floor.getFloorIndex() == -1) {
                holder.fragment_room_floor_item_tv_floor.setTextColor(HouseRentalApplication.getContext().getResources().getColor(R.color.LightGrey));
            } else {
                holder.fragment_room_floor_item_tv_floor.setTextColor(HouseRentalApplication.getContext().getResources().getColor(R.color.DarkerGray));
            }
        }
        holder.fragment_room_floor_item_tv_floor.setText(floor.getName());
        return row;
    }

    private class Holder {
        TextView fragment_room_floor_item_tv_floor;
    }
}
