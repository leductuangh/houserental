package com.example.houserental.function.setting;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.core.core.base.BaseApplication;
import com.example.houserental.R;
import com.example.houserental.function.model.RoomTypeDAO;

import java.util.List;

/**
 * Created by Tyrael on 3/7/16.
 */
public class SettingRoomTypeAdapter extends BaseAdapter implements View.OnClickListener {

    private List<RoomTypeDAO> data;
    private OnDeleteRoomTypeListener listener;

    public SettingRoomTypeAdapter(List<RoomTypeDAO> data, OnDeleteRoomTypeListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return data.size();
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
        if (row == null) {
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(R.layout.fragment_setting_room_type_item, null);
            holder = new Holder();
            holder.fragment_setting_room_type_item_tv_type = (TextView) row.findViewById(R.id.fragment_setting_room_type_item_tv_type);
            holder.fragment_setting_room_type_item_bt_delete = (Button) row.findViewById(R.id.fragment_setting_room_type_item_bt_delete);
            holder.fragment_setting_room_type_item_bt_delete.setOnClickListener(this);
            row.setTag(holder);
        }
        holder = (Holder) row.getTag();

        if (position == 0) {
            // first item
            holder.fragment_setting_room_type_item_tv_type.setText(BaseApplication.getContext().getString(R.string.common_room_insert_type));
            holder.fragment_setting_room_type_item_tv_type.setTextColor(Color.RED);
            holder.fragment_setting_room_type_item_bt_delete.setVisibility(View.GONE);
        } else {
            holder.fragment_setting_room_type_item_bt_delete.setVisibility(View.VISIBLE);
            holder.fragment_setting_room_type_item_bt_delete.setTag(position);
            holder.fragment_setting_room_type_item_tv_type.setText(String.format("%s %s %s", getItem(position).getName(), BaseApplication.getContext().getString(R.string.room_price_title), getItem(position).getPrice()));
            holder.fragment_setting_room_type_item_tv_type.setTextColor(Color.BLACK);
        }

        return row;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment_setting_room_type_item_bt_delete) {
            if (listener != null) {
                listener.onDeleteRoomType((Integer) v.getTag());
            }
        }
    }

    public interface OnDeleteRoomTypeListener {
        void onDeleteRoomType(int position);
    }

    private class Holder {
        TextView fragment_setting_room_type_item_tv_type;
        Button fragment_setting_room_type_item_bt_delete;
    }
}
