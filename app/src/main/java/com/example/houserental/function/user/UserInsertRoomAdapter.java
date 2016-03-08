package com.example.houserental.function.user;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.houserental.R;
import com.example.houserental.core.base.BaseApplication;
import com.example.houserental.model.RoomDAO;

import java.util.List;

/**
 * Created by Tyrael on 3/7/16.
 */
public class UserInsertRoomAdapter extends BaseAdapter {

    private List<RoomDAO> data;

    public UserInsertRoomAdapter(List<RoomDAO> data) {
        this.data = data;
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
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(R.layout.fragment_user_insert_room_item, null);
            holder = new Holder();
            holder.fragment_user_insert_tv_room = (TextView) row.findViewById(R.id.fragment_user_insert_tv_room);
            row.setTag(holder);
        }
        holder = (Holder) row.getTag();
        if (position == 0) {
            // first item
            holder.fragment_user_insert_tv_room.setText(BaseApplication.getContext().getString(R.string.common_user_choose_room));
        } else {
            holder.fragment_user_insert_tv_room.setText(getItem(position).getName());
        }
        return row;
    }

    private class Holder {
        TextView fragment_user_insert_tv_room;
    }
}
