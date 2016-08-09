package com.example.houserental.function.calculator;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.houserental.R;
import com.example.houserental.function.HouseRentalApplication;
import com.example.houserental.function.HouseRentalUtils;
import com.example.houserental.function.model.RoomTypeDAO;

import java.util.List;

import core.base.BaseApplication;

/**
 * Created by Tyrael on 3/7/16.
 */
public class CalculatorRoomTypeAdapter extends BaseAdapter {

    private List<RoomTypeDAO> data;

    public CalculatorRoomTypeAdapter(List<RoomTypeDAO> data) {
        this.data = data;
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
        RoomTypeDAO type = getItem(position);
        if (row == null) {
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(R.layout.fragment_calculator_room_type_item, null);
            holder = new Holder();
            holder.fragment_calculator_room_type_item_tv_type = (TextView) row.findViewById(R.id.fragment_calculator_room_type_item_tv_type);
            holder.fragment_calculator_room_type_item_tv_price = (TextView) row.findViewById(R.id.fragment_calculator_room_type_item_tv_price);
            row.setTag(holder);
        }
        holder = (Holder) row.getTag();
        holder.fragment_calculator_room_type_item_tv_type.setText(type.getName());
        holder.fragment_calculator_room_type_item_tv_price.setText(String.format(HouseRentalApplication.getContext().getString(R.string.calculator_room_type_item_price_title), HouseRentalUtils.toThousandVND(type.getPrice())));
        return row;
    }


    private class Holder {
        TextView fragment_calculator_room_type_item_tv_type;
        TextView fragment_calculator_room_type_item_tv_price;
    }
}
