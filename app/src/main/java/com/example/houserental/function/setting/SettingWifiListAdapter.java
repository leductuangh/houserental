
package com.example.houserental.function.setting;

import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.houserental.R;
import com.example.houserental.function.model.WifiHost;

import java.util.List;

import core.base.BaseApplication;
import core.dialog.GeneralDialog;

public class SettingWifiListAdapter extends BaseAdapter implements View.OnClickListener, DialogInterface.OnDismissListener, GeneralDialog.DecisionListener {

    private List<WifiHost> data;

    public SettingWifiListAdapter(List<WifiHost> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public WifiHost getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        View row = convertView;
        WifiHost wifi = getItem(position);
        if (row == null) {
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(R.layout.fragment_setting_wifi_host_item, null);
            holder = new Holder();
            holder.fragment_setting_wifi_host_item_tv_name = (TextView) row.findViewById(R.id.fragment_setting_wifi_host_item_tv_name);
            holder.fragment_wifi_host_item_bt_edit = (ImageView) row.findViewById(R.id.fragment_wifi_host_item_bt_edit);
            holder.fragment_wifi_host_item_bt_delete = (ImageView) row.findViewById(R.id.fragment_wifi_host_item_bt_delete);
            holder.fragment_setting_wifi_host_item_im_main = (ImageView) row.findViewById(R.id.fragment_setting_wifi_host_item_im_main);
            row.setTag(holder);
        }
        holder = (Holder) row.getTag();
        holder.fragment_setting_wifi_host_item_tv_name.setText(wifi.getHost());
        holder.fragment_wifi_host_item_bt_edit.setTag(position);
        holder.fragment_wifi_host_item_bt_delete.setTag(position);
        holder.fragment_wifi_host_item_bt_edit.setOnClickListener(this);
        holder.fragment_wifi_host_item_bt_delete.setOnClickListener(this);
        try {
            if (wifi.isMain())
                holder.fragment_setting_wifi_host_item_im_main.setVisibility(View.VISIBLE);
            else
                holder.fragment_setting_wifi_host_item_im_main.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return row;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_wifi_host_item_bt_delete:
                break;
            case R.id.fragment_wifi_host_item_bt_edit:
                break;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        notifyDataSetChanged();
    }

    @Override
    public void onAgreed(int id, Object onWhat) {

    }

    @Override
    public void onDisAgreed(int id, Object onWhat) {

    }

    @Override
    public void onNeutral(int id, Object onWhat) {

    }

    private class Holder {
        TextView fragment_setting_wifi_host_item_tv_name;
        ImageView fragment_wifi_host_item_bt_edit;
        ImageView fragment_wifi_host_item_bt_delete;
        ImageView fragment_setting_wifi_host_item_im_main;
    }
}
