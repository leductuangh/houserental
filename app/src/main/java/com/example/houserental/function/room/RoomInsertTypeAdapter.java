package com.example.houserental.function.room;

import android.graphics.Color;
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
public class RoomInsertTypeAdapter extends BaseAdapter {

    private List<RoomDAO.Type> data;

    public RoomInsertTypeAdapter(List<RoomDAO.Type> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public RoomDAO.Type getItem(int position) {
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
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(R.layout.fragment_room_insert_type_item, null);
            holder = new Holder();
            holder.fragment_room_insert_tv_type = (TextView) row.findViewById(R.id.fragment_room_insert_tv_type);
            row.setTag(holder);
        }
        holder = (Holder) row.getTag();
        if (position == 0) {
            // first item
            holder.fragment_room_insert_tv_type.setText(BaseApplication.getContext().getString(R.string.common_room_choose_type));
            holder.fragment_room_insert_tv_type.setTextColor(Color.RED);

        } else {
            holder.fragment_room_insert_tv_type.setText(getItem(position).toString());
            holder.fragment_room_insert_tv_type.setTextColor(Color.BLACK);
        }
        return row;
    }

    private class Holder {
        TextView fragment_room_insert_tv_type;
    }
}
