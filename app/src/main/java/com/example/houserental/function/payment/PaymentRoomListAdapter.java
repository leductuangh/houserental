package com.example.houserental.function.payment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.houserental.R;
import com.example.houserental.function.HouseRentalApplication;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.RoomDAO;

import java.util.Calendar;
import java.util.List;

import core.base.BaseApplication;

/**
 * Created by Tyrael on 5/9/16.
 */
public class PaymentRoomListAdapter extends BaseAdapter {

    private List<RoomDAO> data;
    private String current_month_text;
    private int current_month;


    public PaymentRoomListAdapter() {
        data = DAOManager.getAllRentedRooms();
        RoomDAO room = new RoomDAO();
        room.setName(HouseRentalApplication.getContext().getString(R.string.common_user_choose_room));
        this.data.add(room);
        Calendar calendar = Calendar.getInstance();
        current_month = calendar.get(Calendar.MONTH);
        current_month_text = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, HouseRentalApplication.getContext().getResources().getConfiguration().locale);
    }

    @Override
    public int getCount() {
        int count = data.size();
        if (count > 0)
            count = count - 1;
        return count;
    }

    @Override
    public Object getItem(int position) {
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
        RoomDAO room = (RoomDAO) getItem(position);
        if (row == null) {
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(com.example.houserental.R.layout.fragment_payment_record_room_list_item, null);
            holder = new Holder();
            holder.fragment_payment_record_room_list_item_tv_name = (TextView) row.findViewById(com.example.houserental.R.id.fragment_payment_record_room_list_item_tv_name);
            row.setTag(holder);
        }
        holder = (Holder) row.getTag();
        if (room.getName().equals(HouseRentalApplication.getContext().getString(R.string.common_user_choose_room))) {
            holder.fragment_payment_record_room_list_item_tv_name.setTextColor(HouseRentalApplication.getContext().getResources().getColor(R.color.LightGrey));
        } else {
            holder.fragment_payment_record_room_list_item_tv_name.setTextColor(HouseRentalApplication.getContext().getResources().getColor(R.color.DarkerGray));
        }
        holder.fragment_payment_record_room_list_item_tv_name.setText(room.getName());
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        View row = convertView;
        RoomDAO room = (RoomDAO) getItem(position);
        if (row == null) {
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(com.example.houserental.R.layout.fragment_payment_record_room_list_item, null);
            holder = new Holder();
            holder.fragment_payment_record_room_list_item_tv_name = (TextView) row.findViewById(com.example.houserental.R.id.fragment_payment_record_room_list_item_tv_name);
            holder.fragment_payment_record_room_list_item_tv_name.setTextColor(HouseRentalApplication.getContext().getResources().getColor(R.color.DarkerGray));
            row.setTag(holder);
        }
        holder = (Holder) row.getTag();
        String room_name_text = room.getName();
        int start_month = room.getPaymentStartDate().getMonth();
        if (start_month <= current_month) {
            room_name_text = String.format(HouseRentalApplication.getContext().getString(R.string.payment_record_unpaid_in_month), room_name_text, current_month_text);
        }
        holder.fragment_payment_record_room_list_item_tv_name.setText(room_name_text);
        return row;
    }

    private class Holder {
        TextView fragment_payment_record_room_list_item_tv_name;
    }
}
