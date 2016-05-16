package com.example.houserental.function.setting;

import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.houserental.R;
import com.example.houserental.function.HouseRentalApplication;
import com.example.houserental.function.MainActivity;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.OwnerDAO;
import com.example.houserental.function.model.SettingDAO;

import java.util.List;

import core.base.BaseApplication;
import core.dialog.GeneralDialog;
import core.util.Constant;

/**
 * Created by Tyrael on 3/16/16.
 */
public class SettingOwnerListAdapter extends BaseAdapter implements View.OnClickListener, DialogInterface.OnDismissListener, GeneralDialog.DecisionListener {

    private List<OwnerDAO> data;
    private SettingDAO setting;

    public SettingOwnerListAdapter(List<OwnerDAO> data, SettingDAO setting) {
        this.data = data;
        this.setting = setting;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public OwnerDAO getItem(int position) {
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
        OwnerDAO owner = getItem(position);
        if (row == null) {
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(R.layout.fragment_setting_owner_list_item, null);
            holder = new Holder();
            holder.fragment_setting_owner_list_item_tv_name = (TextView) row.findViewById(R.id.fragment_setting_owner_list_item_tv_name);
            holder.fragment_owner_item_bt_delete = (ImageView) row.findViewById(R.id.fragment_owner_item_bt_delete);
            holder.fragment_owner_item_bt_edit = (ImageView) row.findViewById(R.id.fragment_owner_item_bt_edit);
            holder.fragment_setting_owner_list_item_im_main = (ImageView) row.findViewById(R.id.fragment_setting_owner_list_item_im_main);
            holder.fragment_owner_item_bt_edit.setOnClickListener(this);
            holder.fragment_owner_item_bt_delete.setOnClickListener(this);
            row.setTag(holder);
        }
        holder = (Holder) row.getTag();
        holder.fragment_owner_item_bt_edit.setTag(position);
        holder.fragment_owner_item_bt_delete.setTag(position);
        holder.fragment_setting_owner_list_item_tv_name.setText(owner.getName());
        try {
            Long selected = setting.getOwner();
            if (selected != null && selected == owner.getId())
                holder.fragment_setting_owner_list_item_im_main.setVisibility(View.VISIBLE);
            else
                holder.fragment_setting_owner_list_item_im_main.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return row;
    }

    @Override
    public void onClick(View v) {
        Integer position = (Integer) v.getTag();
        OwnerDAO owner = getItem(position);
        switch (v.getId()) {
            case R.id.fragment_owner_item_bt_delete:
                try {
                    Long selected = setting.getOwner();
                    if (selected == owner.getId()) {
                        ((MainActivity) HouseRentalApplication.getActiveActivity()).showAlertDialog(HouseRentalApplication.getActiveActivity(), -1, ((MainActivity) HouseRentalApplication.getActiveActivity()).getGeneralDialogLayoutResource(), -1, HouseRentalApplication.getContext().getString(R.string.application_alert_dialog_title), HouseRentalApplication.getContext().getString(R.string.setting_owner_delete_alert), HouseRentalApplication.getContext().getString(R.string.common_ok), null, null);
                        return;
                    }
                    ((MainActivity) HouseRentalApplication.getActiveActivity()).showDecisionDialog(HouseRentalApplication.getActiveActivity(),
                            Constant.DELETE_OWNER_DIALOG, ((MainActivity) HouseRentalApplication.getActiveActivity()).getGeneralDialogLayoutResource(),
                            -1,
                            String.format(HouseRentalApplication.getContext().getString(R.string.delete_dialog_title), owner.getName()),
                            String.format(HouseRentalApplication.getContext().getString(R.string.delete_dialog_message), owner.getName())
                                    + "\n"
                                    + HouseRentalApplication.getContext().getString(R.string.delete_owner_dialog_message),
                            HouseRentalApplication.getContext().getString(R.string.common_ok),
                            HouseRentalApplication.getContext().getString(R.string.common_cancel), null, owner, this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.fragment_owner_item_bt_edit:
                SettingOwnerDialog dialog = new SettingOwnerDialog(HouseRentalApplication.getActiveActivity(), owner);
                dialog.setOnDismissListener(this);
                dialog.show();
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
            case Constant.DELETE_OWNER_DIALOG:
                OwnerDAO owner = (OwnerDAO) onWhat;
                DAOManager.deleteOwner(owner.getId());
                data.remove(owner);
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
        TextView fragment_setting_owner_list_item_tv_name;
        ImageView fragment_owner_item_bt_delete, fragment_owner_item_bt_edit;
        ImageView fragment_setting_owner_list_item_im_main;
    }
}
