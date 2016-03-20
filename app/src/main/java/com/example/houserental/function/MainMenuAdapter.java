package com.example.houserental.function;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.core.core.base.BaseApplication;

import java.util.ArrayList;

/**
 * Created by leductuan on 3/5/16.
 */
public class MainMenuAdapter extends BaseAdapter {

    private ArrayList<String> data;

    public MainMenuAdapter(ArrayList<String> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = BaseApplication.getActiveActivity().getLayoutInflater().inflate(com.example.houserental.R.layout.activity_main_menu_item, null);
        TextView text = (TextView) view.findViewById(com.example.houserental.R.id.activity_main_menu_item_tv);
        text.setText(getItem(position));
        return view;
    }
}
