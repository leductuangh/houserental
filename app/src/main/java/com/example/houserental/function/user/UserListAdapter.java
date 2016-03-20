package com.example.houserental.function.user;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.core.core.base.BaseApplication;
import com.example.houserental.function.model.UserDAO;

import java.util.List;

/**
 * Created by leductuan on 3/7/16.
 */
public class UserListAdapter extends BaseAdapter {

    private List<UserDAO> data;
    private boolean hasInsert = true;

    public UserListAdapter(List<UserDAO> data, boolean hasInsert) {
        this.data = data;
        this.hasInsert = hasInsert;
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
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(com.example.houserental.R.layout.fragment_user_list_item, null);
            holder = new Holder();
            holder.fragment_user_list_item_tv_name = (TextView) row.findViewById(com.example.houserental.R.id.fragment_user_list_item_tv_name);
            row.setTag(holder);
        }
        holder = (Holder) row.getTag();
        if (hasInsert) {
            if (position == 0) {
                holder.fragment_user_list_item_tv_name.setText(BaseApplication.getContext().getString(com.example.houserental.R.string.user_item_more));
            } else {
                holder.fragment_user_list_item_tv_name.setText(getItem(position).getName());
            }
        } else {
            holder.fragment_user_list_item_tv_name.setText(getItem(position).getName());
        }

        return row;
    }

    private class Holder {
        TextView fragment_user_list_item_tv_name;
    }
}
