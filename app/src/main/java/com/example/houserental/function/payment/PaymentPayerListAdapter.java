package com.example.houserental.function.payment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.UserDAO;

import java.util.List;

/**
 * Created by Tyrael on 5/9/16.
 */
public class PaymentPayerListAdapter extends BaseAdapter {

    private List<UserDAO> data;
    private Long room_id;

    public PaymentPayerListAdapter(List<UserDAO> data, Long room_id) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }


    @Override
    public void notifyDataSetChanged() {
        data.clear();
        data.addAll(DAOManager.getUsersOfRoom(room_id));
        super.notifyDataSetChanged();
    }
}
