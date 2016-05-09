package com.example.houserental.function.setting;

import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.houserental.R;
import com.example.houserental.function.HouseRentalApplication;
import com.example.houserental.function.HouseRentalUtils;
import com.example.houserental.function.MainActivity;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.RoomTypeDAO;

import java.util.List;

import core.base.BaseApplication;
import core.dialog.GeneralDialog;
import core.util.Constant;

/**
 * Created by Tyrael on 3/7/16.
 */
public class SettingRoomTypeAdapter extends BaseAdapter implements View.OnClickListener, GeneralDialog.DecisionListener, DialogInterface.OnDismissListener {

    private List<RoomTypeDAO> data;

    public SettingRoomTypeAdapter(List<RoomTypeDAO> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public RoomTypeDAO getItem(int position) {
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
        RoomTypeDAO type = getItem(position);
        if (row == null) {
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(com.example.houserental.R.layout.fragment_setting_room_type_item, null);
            holder = new Holder();
            holder.fragment_setting_room_type_item_tv_type = (TextView) row.findViewById(com.example.houserental.R.id.fragment_setting_room_type_item_tv_type);
            holder.fragment_setting_room_type_item_tv_price = (TextView) row.findViewById(R.id.fragment_setting_room_type_item_tv_price);
            holder.fragment_setting_room_type_item_bt_edit = (ImageView) row.findViewById(R.id.fragment_setting_room_type_item_bt_edit);
            holder.fragment_setting_room_type_item_bt_delete = (ImageView) row.findViewById(com.example.houserental.R.id.fragment_setting_room_type_item_bt_delete);
            holder.fragment_setting_room_type_item_tv_room_count = (TextView) row.findViewById(R.id.fragment_setting_room_type_item_tv_room_count);
            holder.fragment_setting_room_type_item_bt_delete.setOnClickListener(this);
            holder.fragment_setting_room_type_item_bt_edit.setOnClickListener(this);
            row.setTag(holder);
        }
        holder = (Holder) row.getTag();
        holder.fragment_setting_room_type_item_bt_edit.setTag(position);
        holder.fragment_setting_room_type_item_bt_delete.setTag(position);
        holder.fragment_setting_room_type_item_tv_type.setText(type.getName());
        holder.fragment_setting_room_type_item_tv_price.setText(String.format(HouseRentalApplication.getContext().getString(R.string.setting_room_type_item_price_title), HouseRentalUtils.toThousandVND(type.getPrice())));
        holder.fragment_setting_room_type_item_tv_room_count.setText(String.format(HouseRentalApplication.getContext().getString(R.string.setting_room_type_item_room_count_title), type.getRoomCount()));
        return row;
    }

    @Override
    public void onClick(View v) {
        Integer position = (Integer) v.getTag();
        RoomTypeDAO type = getItem(position);
        switch (v.getId()) {
            case R.id.fragment_setting_room_type_item_bt_delete:
                if (type.getRoomCount() > 0) {
                    ((MainActivity) HouseRentalApplication.getActiveActivity()).showAlertDialog(HouseRentalApplication.getActiveActivity(), -1, ((MainActivity) HouseRentalApplication.getActiveActivity()).getGeneralDialogLayoutResource(), -1, HouseRentalApplication.getContext().getString(R.string.application_alert_dialog_title), HouseRentalApplication.getContext().getString(R.string.setting_room_type_delete_alert), HouseRentalApplication.getContext().getString(R.string.common_ok), null, null);
                    return;
                }
                ((MainActivity) HouseRentalApplication.getActiveActivity()).showDecisionDialog(HouseRentalApplication.getActiveActivity(),
                        Constant.DELETE_ROOM_TYPE_DIALOG, ((MainActivity) HouseRentalApplication.getActiveActivity()).getGeneralDialogLayoutResource(),
                        -1,
                        String.format(HouseRentalApplication.getContext().getString(R.string.delete_dialog_title), type.getName()),
                        String.format(HouseRentalApplication.getContext().getString(R.string.delete_dialog_message), type.getName())
                                + "\n"
                                + HouseRentalApplication.getContext().getString(R.string.delete_room_type_dialog_message),
                        HouseRentalApplication.getContext().getString(R.string.common_ok),
                        HouseRentalApplication.getContext().getString(R.string.common_cancel), null, type, this);
                break;
            case R.id.fragment_setting_room_type_item_bt_edit:
                SettingRoomTypeDialog dialog = new SettingRoomTypeDialog(HouseRentalApplication.getActiveActivity(), type, data);
                dialog.setOnDismissListener(this);
                dialog.show();
                break;
        }
    }

    @Override
    public void onAgreed(int id, Object onWhat) {
        switch (id) {
            case Constant.DELETE_ROOM_TYPE_DIALOG:
                RoomTypeDAO type = (RoomTypeDAO) onWhat;
                DAOManager.deleteRoomType(type.getId());
                data.remove(type);
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

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (data != null)
            data.clear();
        data.addAll(DAOManager.getAllRoomTypes());
        notifyDataSetChanged();
    }

    private class Holder {
        TextView fragment_setting_room_type_item_tv_type;
        TextView fragment_setting_room_type_item_tv_price;
        TextView fragment_setting_room_type_item_tv_room_count;
        ImageView fragment_setting_room_type_item_bt_delete;
        ImageView fragment_setting_room_type_item_bt_edit;
    }
}
