package com.example.houserental.function.room;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.houserental.R;
import com.example.houserental.function.HouseRentalApplication;
import com.example.houserental.function.MainActivity;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.RoomDAO;

import java.util.List;

import core.base.BaseApplication;
import core.dialog.GeneralDialog;
import core.util.Constant;

/**
 * Created by leductuan on 3/6/16.
 */
public class RoomListAdapter extends BaseAdapter implements View.OnClickListener, GeneralDialog.DecisionListener {

    private List<RoomDAO> data;
    private boolean isSimplified = false;

    public RoomListAdapter(List<RoomDAO> data, boolean isSimplified) {
        this.data = data;
        this.isSimplified = isSimplified;
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
        RoomDAO room = getItem(position);
        if (row == null) {
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(R.layout.fragment_room_list_item, null);
            holder = new Holder();
            holder.fragment_room_list_item_tv_name = (TextView) row.findViewById(R.id.fragment_room_list_item_tv_name);
            holder.fragment_room_list_item_im_delete = (ImageView) row.findViewById(R.id.fragment_room_list_item_im_delete);
            holder.fragment_room_list_item_tv_user_count = (TextView) row.findViewById(R.id.fragment_room_list_item_tv_user_count);
            holder.fragment_room_list_item_tv_device_count = (TextView) row.findViewById(R.id.fragment_room_list_item_tv_device_count);
            holder.fragment_room_list_item_im_delete.setOnClickListener(this);
            row.setTag(holder);
        }
        holder = (Holder) row.getTag();
        String room_name = isSimplified ? room.getName() : room.isRented()
                ? String.format(HouseRentalApplication.getContext().getString(R.string.room_status_line_rented), room.getName())
                : room.getName();

        if (isSimplified) {
            holder.fragment_room_list_item_im_delete.setVisibility(View.GONE);
            holder.fragment_room_list_item_tv_user_count.setVisibility(View.GONE);
            holder.fragment_room_list_item_tv_device_count.setVisibility(View.GONE);
            holder.fragment_room_list_item_tv_user_count.setVisibility(View.GONE);
            holder.fragment_room_list_item_tv_device_count.setVisibility(View.GONE);
        } else {
            holder.fragment_room_list_item_im_delete.setVisibility(View.VISIBLE);
            holder.fragment_room_list_item_tv_user_count.setVisibility(View.VISIBLE);
            holder.fragment_room_list_item_tv_device_count.setVisibility(View.VISIBLE);
            holder.fragment_room_list_item_tv_user_count.setVisibility(View.VISIBLE);
            holder.fragment_room_list_item_tv_device_count.setVisibility(View.VISIBLE);
            holder.fragment_room_list_item_im_delete.setTag(position);
            holder.fragment_room_list_item_tv_user_count.setText(String.format(HouseRentalApplication.getContext().getString(R.string.room_user_count), room.getUserCount()));
            holder.fragment_room_list_item_tv_device_count.setText(String.format(HouseRentalApplication.getContext().getString(R.string.room_device_count), room.getDeviceCount()));
            holder.fragment_room_list_item_tv_user_count.setVisibility(room.isRented() ? View.VISIBLE : View.GONE);
            holder.fragment_room_list_item_tv_device_count.setVisibility(room.isRented() ? View.VISIBLE : View.GONE);
        }
        holder.fragment_room_list_item_tv_name.setText(room_name);
        return row;
    }

    public void clearData() {
        data.clear();
    }

    @Override
    public void onClick(View v) {
        Integer position = (Integer) v.getTag();
        RoomDAO room = getItem(position);
        switch (v.getId()) {
            case R.id.fragment_room_list_item_im_delete:
                ((MainActivity) HouseRentalApplication.getActiveActivity()).showDecisionDialog(HouseRentalApplication.getActiveActivity(), Constant.DELETE_ROOM_DIALOG, -1, HouseRentalApplication.getContext().getString(com.example.houserental.R.string.application_alert_dialog_title), HouseRentalApplication.getContext().getString(com.example.houserental.R.string.delete_room_dialog_message), HouseRentalApplication.getContext().getString(com.example.houserental.R.string.common_ok), HouseRentalApplication.getContext().getString(com.example.houserental.R.string.common_cancel), null, room, this);
                break;
        }
    }

    @Override
    public void onAgreed(int id, Object onWhat) {
        switch (id) {
            case Constant.DELETE_ROOM_DIALOG:
                RoomDAO room = (RoomDAO) onWhat;
                DAOManager.deleteRoom(room.getId());
                data.remove(room);
                notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onDisAgreed(int id, Object onWhat) {

    }

    @Override
    public void onNeutral(int id, Object onWhat) {

    }

    private class Holder {
        TextView fragment_room_list_item_tv_user_count;
        TextView fragment_room_list_item_tv_device_count;
        TextView fragment_room_list_item_tv_name;
        ImageView fragment_room_list_item_im_delete;
    }
}
