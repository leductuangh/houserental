package com.example.houserental.function.floor;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.houserental.R;
import com.example.houserental.core.base.BaseApplication;
import com.example.houserental.model.FloorDAO;

import java.util.List;

/**
 * Created by leductuan on 3/6/16.
 */
public class FloorListAdapter extends BaseAdapter {

    private List<FloorDAO> data;

    public FloorListAdapter(List<FloorDAO> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size() + 1;
    }

    @Override
    public String getItem(int position) {
        return data.get(position).getName();
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
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(R.layout.fragment_floor_list_item, null);
            holder = new Holder();
            holder.fragment_floor_list_item_tv_name = (TextView) row.findViewById(R.id.fragment_floor_list_item_tv_name);
            row.setTag(holder);
        }
        holder = (Holder) row.getTag();
        if (position == data.size()) {
            // last item
            holder.fragment_floor_list_item_tv_name.setText(BaseApplication.getContext().getString(R.string.floor_item_more));
        } else {
            holder.fragment_floor_list_item_tv_name.setText(getItem(position));
        }
        return row;
    }

    public void clearData() {
        data.clear();
    }

    private class Holder {
        TextView fragment_floor_list_item_tv_name;
    }
}
