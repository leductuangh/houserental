package com.example.houserental.function.user;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.houserental.R;
import com.example.houserental.core.base.BaseApplication;
import com.example.houserental.model.FloorDAO;

import java.util.List;

/**
 * Created by Tyrael on 3/7/16.
 */
public class UserInsertFloorAdapter extends BaseAdapter {

    private List<FloorDAO> data;

    public UserInsertFloorAdapter(List<FloorDAO> data) {
        this.data = data;
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
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(R.layout.fragment_user_insert_floor_item, null);
            holder = new Holder();
            holder.fragment_user_insert_tv_floor = (TextView) row.findViewById(R.id.fragment_user_insert_tv_floor);
            row.setTag(holder);
        }
        holder = (Holder) row.getTag();
        if (position == 0) {
            // first item
            holder.fragment_user_insert_tv_floor.setText(BaseApplication.getContext().getString(R.string.common_room_choose_floor));
        } else {
            holder.fragment_user_insert_tv_floor.setText(getItem(position).getName());
        }
        return row;
    }

    private class Holder {
        TextView fragment_user_insert_tv_floor;
    }
}