package com.example.houserental.function.room;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.houserental.R;
import com.example.houserental.core.base.BaseApplication;
import com.example.houserental.model.UserDAO;

import java.util.List;

/**
 * Created by leductuan on 3/7/16.
 */
public class RoomDetailUserAdapter extends BaseAdapter {

    private List<UserDAO> data;

    public RoomDetailUserAdapter(List<UserDAO> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public UserDAO getItem(int position) {
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
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(R.layout.fragment_room_detail_user_item, null);
            holder = new Holder();
            holder.fragment_room_detail_user_item_tv_name = (TextView) row.findViewById(R.id.fragment_room_detail_user_item_tv_name);
            row.setTag(holder);
        }
        holder = (Holder) row.getTag();

        if (position == 0) {
            holder.fragment_room_detail_user_item_tv_name.setText(BaseApplication.getContext().getString(R.string.user_item_more));
        } else {
            holder.fragment_room_detail_user_item_tv_name.setText(getItem(position).getName());
        }
        return row;
    }

    private class Holder {
        TextView fragment_room_detail_user_item_tv_name;
    }
}
