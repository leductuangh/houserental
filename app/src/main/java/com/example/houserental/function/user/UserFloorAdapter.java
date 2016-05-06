package com.example.houserental.function.user;

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
public class UserFloorAdapter extends BaseAdapter {

    private List<FloorDAO> data;

    public UserFloorAdapter(List<FloorDAO> data) {
        this.data = data;
        data.add(new FloorDAO(HouseRentalApplication.getContext().getString(R.string.common_room_choose_floor), -1));
    }

    @Override
    public int getCount() {
        int count = data.size();
        if (count > 0)
            count = count - 1;
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
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(com.example.houserental.R.layout.fragment_user_insert_floor_item, null);
            holder = new Holder();
            holder.fragment_user_insert_tv_floor = (TextView) row.findViewById(com.example.houserental.R.id.fragment_user_insert_tv_floor);
            row.setTag(holder);
        }
        holder = (Holder) row.getTag();
        if (floor.getFloorIndex() == -1) {
            holder.fragment_user_insert_tv_floor.setTextColor(HouseRentalApplication.getContext().getResources().getColor(R.color.LightGrey));
        } else {
            holder.fragment_user_insert_tv_floor.setTextColor(HouseRentalApplication.getContext().getResources().getColor(R.color.DarkerGray));
        }
        holder.fragment_user_insert_tv_floor.setText(getItem(position).getName());

        return row;
    }

    private class Holder {
        TextView fragment_user_insert_tv_floor;
    }
}
