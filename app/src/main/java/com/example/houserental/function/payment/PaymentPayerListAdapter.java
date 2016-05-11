package com.example.houserental.function.payment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.houserental.R;
import com.example.houserental.function.HouseRentalApplication;
import com.example.houserental.function.model.UserDAO;

import java.util.List;

import core.base.BaseApplication;

/**
 * Created by Tyrael on 5/9/16.
 */
public class PaymentPayerListAdapter extends BaseAdapter {

    private List<UserDAO> data;
    private Long room_id;

    public PaymentPayerListAdapter(List<UserDAO> data, Long room_id) {
        this.data = data;
        this.room_id = room_id;
        UserDAO dummy = new UserDAO();
        dummy.setName(HouseRentalApplication.getContext().getString(R.string.common_setting_choose_payer));
        this.data.add(dummy);
    }

    @Override
    public int getCount() {
        int count = data.size();
        if (count > 0)
            count = count - 1;
        return count;
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
        UserDAO payer = getItem(position);
        if (row == null) {
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(com.example.houserental.R.layout.fragment_payment_record_payer_list_item, null);
            holder = new Holder();
            holder.fragment_payment_record_payer_list_item_tv_name = (TextView) row.findViewById(com.example.houserental.R.id.fragment_payment_record_payer_list_item_tv_name);
            row.setTag(holder);
        }
        holder = (Holder) row.getTag();
        if (payer.getName().equals(HouseRentalApplication.getContext().getString(R.string.common_setting_choose_payer))) {
            holder.fragment_payment_record_payer_list_item_tv_name.setTextColor(HouseRentalApplication.getContext().getResources().getColor(R.color.LightGrey));
        } else {
            holder.fragment_payment_record_payer_list_item_tv_name.setTextColor(HouseRentalApplication.getContext().getResources().getColor(R.color.DarkerGray));
        }
        holder.fragment_payment_record_payer_list_item_tv_name.setText(payer.getName());
        return row;
    }

    private class Holder {
        TextView fragment_payment_record_payer_list_item_tv_name;
    }
}
