package com.example.houserental.function.user;

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
 * Created by Tyrael on 3/8/16.
 */
public class UserCareerAdapter extends BaseAdapter {

    private List<UserDAO.Career> data;
    private boolean isInsert = true;

    public UserCareerAdapter(List<UserDAO.Career> data, boolean isInsert) {
        this.data = data;
        this.isInsert = isInsert;
    }

    @Override
    public int getCount() {
        int count = data.size();
        if (count > 0)
            count = count - 1;
        return count;
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
        UserDAO.Career career = getItem(position);
        if (row == null) {
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(R.layout.fragment_user_insert_career_item, null);
            holder = new Holder();
            holder.fragment_user_insert_tv_career = (TextView) row.findViewById(R.id.fragment_user_insert_tv_career);
            row.setTag(holder);
        }
        holder = (Holder) row.getTag();
        if (career.toString().equals(HouseRentalApplication.getContext().getString(R.string.common_user_choose_career))) {
            holder.fragment_user_insert_tv_career.setTextColor(HouseRentalApplication.getContext().getResources().getColor(R.color.LightGrey));
        } else {
            holder.fragment_user_insert_tv_career.setTextColor(HouseRentalApplication.getContext().getResources().getColor(R.color.DarkerGray));
        }
        holder.fragment_user_insert_tv_career.setText(career.toString());
        return row;
    }

    private class Holder {
        TextView fragment_user_insert_tv_career;
    }
}
