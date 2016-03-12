package com.example.houserental.function.user;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.houserental.R;
import com.example.houserental.core.base.BaseApplication;
import com.example.houserental.model.UserDAO;

import java.util.List;

/**
 * Created by Tyrael on 3/8/16.
 */
public class UserCareerAdapter extends BaseAdapter {

    private List<UserDAO.Career> data;

    public UserCareerAdapter(List<UserDAO.Career> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public UserDAO.Career getItem(int position) {
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
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(R.layout.fragment_user_insert_career_item, null);
            holder = new Holder();
            holder.fragment_user_insert_tv_career = (TextView) row.findViewById(R.id.fragment_user_insert_tv_career);
            row.setTag(holder);
        }
        holder = (Holder) row.getTag();
        if (position == 0) {
            // first item
            holder.fragment_user_insert_tv_career.setText(BaseApplication.getContext().getString(R.string.common_user_choose_career));
            holder.fragment_user_insert_tv_career.setTextColor(Color.RED);
        } else {
            holder.fragment_user_insert_tv_career.setText(getItem(position).toString());
            holder.fragment_user_insert_tv_career.setTextColor(Color.BLACK);
        }
        return row;
    }

    private class Holder {
        TextView fragment_user_insert_tv_career;
    }
}
