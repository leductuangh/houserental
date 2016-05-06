package com.example.houserental.function.room;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.houserental.R;
import com.example.houserental.function.HouseRentalApplication;
import com.example.houserental.function.model.RoomTypeDAO;

import java.util.List;

import core.base.BaseApplication;

/**
 * Created by Tyrael on 3/7/16.
 */
public class RoomTypeAdapter extends BaseAdapter {

    private List<RoomTypeDAO> data;
    private boolean isInsert = false;

    public RoomTypeAdapter(List<RoomTypeDAO> data, boolean isInsert) {
        this.data = data;
        this.isInsert = isInsert;
        if (isInsert) {
            this.data.add(new RoomTypeDAO(HouseRentalApplication.getContext().getString(R.string.common_room_choose_type), -1));
        }
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
        RoomTypeDAO type = getItem(position);
        if (row == null) {
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(R.layout.fragment_room_type_item, null);
            holder = new Holder();
            holder.fragment_room_type_item_tv_type = (TextView) row.findViewById(R.id.fragment_room_type_item_tv_type);
            row.setTag(holder);
        }
        holder = (Holder) row.getTag();
        if (isInsert) {
            if (type.getPrice() == -1) {
                holder.fragment_room_type_item_tv_type.setTextColor(HouseRentalApplication.getContext().getResources().getColor(R.color.LightGrey));
                holder.fragment_room_type_item_tv_type.setText(type.getName());
            } else {
                holder.fragment_room_type_item_tv_type.setTextColor(HouseRentalApplication.getContext().getResources().getColor(R.color.DarkerGray));
                holder.fragment_room_type_item_tv_type.setText(String.format("%s %s %s", type.getName(), BaseApplication.getContext().getString(R.string.room_price_title), type.getPrice()));
            }
        } else {
            holder.fragment_room_type_item_tv_type.setText(String.format("%s %s %s", type.getName(), BaseApplication.getContext().getString(R.string.room_price_title), type.getPrice()));
        }

        return row;
    }

    private class Holder {
        TextView fragment_room_type_item_tv_type;
    }
}
