package com.example.houserental.function.room;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.core.core.base.BaseApplication;
import com.example.houserental.R;
import com.example.houserental.function.model.RoomDAO;

import java.util.List;

/**
 * Created by leductuan on 3/6/16.
 */
public class RoomListAdapter extends BaseAdapter {

    private List<RoomDAO> data;
    private boolean hasInsert = true;

    public RoomListAdapter(List<RoomDAO> data, boolean hasInsert) {
        this.data = data;
        this.hasInsert = hasInsert;
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
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(R.layout.fragment_room_list_item, null);
            holder = new Holder();
            holder.fragment_room_list_item_tv_name = (TextView) row.findViewById(R.id.fragment_room_list_item_tv_name);
            row.setTag(holder);
        }
        holder = (Holder) row.getTag();
        if (hasInsert) {
            if (position == 0) {
                // fist item
                holder.fragment_room_list_item_tv_name.setText(BaseApplication.getContext().getString(R.string.room_item_more));
            } else {
                holder.fragment_room_list_item_tv_name.setText(getItem(position).getName());
            }
        } else {
            holder.fragment_room_list_item_tv_name.setText(getItem(position).getName());
        }


        return row;
    }

    public void clearData() {
        data.clear();
    }

    private class Holder {
        TextView fragment_room_list_item_tv_name;
    }
}
