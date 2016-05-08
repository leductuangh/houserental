package com.example.houserental.function.user;

import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.houserental.R;
import com.example.houserental.function.HouseRentalApplication;
import com.example.houserental.function.MainActivity;
import com.example.houserental.function.model.DeviceDAO;

import java.util.List;

import core.base.BaseApplication;
import core.dialog.GeneralDialog;
import core.util.Constant;

/**
 * Created by leductuan on 3/12/16.
 */
public class UserDeviceAdapter extends BaseAdapter implements View.OnClickListener, DialogInterface.OnDismissListener, GeneralDialog.DecisionListener {

    private List<DeviceDAO> data;

    public UserDeviceAdapter(List<DeviceDAO> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public DeviceDAO getItem(int position) {
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
        DeviceDAO device = getItem(position);
        if (row == null) {
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(R.layout.fragment_user_detail_device_item, null);
            holder = new Holder();
            holder.fragment_user_detail_device_item_tv = (TextView) row.findViewById(R.id.fragment_user_detail_device_item_tv);
            holder.fragment_user_device_list_item_tv_description = (TextView) row.findViewById(R.id.fragment_user_device_list_item_tv_description);
            holder.fragment_user_device_list_item_im_edit = (ImageView) row.findViewById(R.id.fragment_user_device_list_item_im_edit);
            holder.fragment_user_device_list_item_im_remove = (ImageView) row.findViewById(R.id.fragment_user_device_list_item_im_remove);
            holder.fragment_user_device_list_item_im_remove.setOnClickListener(this);
            holder.fragment_user_device_list_item_im_edit.setOnClickListener(this);
            row.setTag(holder);
        }
        holder = (Holder) row.getTag();
        holder.fragment_user_device_list_item_im_edit.setTag(position);
        holder.fragment_user_device_list_item_im_remove.setTag(position);
        holder.fragment_user_device_list_item_tv_description.setText(device.getDescription());
        holder.fragment_user_detail_device_item_tv.setText(device.getMAC());
        return row;
    }

    @Override
    public void onClick(View v) {
        Integer position = (Integer) v.getTag();
        DeviceDAO device = getItem(position);
        switch (v.getId()) {
            case R.id.fragment_user_device_list_item_im_edit:
                UserDeviceDialog dialog = new UserDeviceDialog(HouseRentalApplication.getActiveActivity(), device);
                dialog.setOnDismissListener(this);
                dialog.show();
                break;
            case R.id.fragment_user_device_list_item_im_remove:
                ((MainActivity) HouseRentalApplication.getActiveActivity()).showDecisionDialog(
                        HouseRentalApplication.getActiveActivity(),
                        Constant.DELETE_DEVICE_DIALOG, ((MainActivity) HouseRentalApplication.getActiveActivity()).getGeneralDialogLayoutResource(),
                        -1,
                        HouseRentalApplication.getContext().getString(R.string.application_alert_dialog_title),
                        String.format(HouseRentalApplication.getContext().getString(R.string.user_device_delete_message), device.getDescription()),
                        HouseRentalApplication.getContext().getString(R.string.common_ok),
                        HouseRentalApplication.getContext().getString(R.string.common_cancel),
                        null,
                        device,
                        this);
                break;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        notifyDataSetChanged();
    }

    @Override
    public void onAgreed(int id, Object onWhat) {
        switch (id) {
            case Constant.DELETE_DEVICE_DIALOG:
                DeviceDAO device = (DeviceDAO) onWhat;
                device.delete();
                data.remove(device);
                notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onDisAgreed(int id, Object onWhat) {

    }

    @Override
    public void onNeutral(int id, Object onWhat) {

    }

    private class Holder {
        TextView fragment_user_detail_device_item_tv;
        TextView fragment_user_device_list_item_tv_description;
        ImageView fragment_user_device_list_item_im_edit;
        ImageView fragment_user_device_list_item_im_remove;
    }
}
