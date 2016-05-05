package com.example.houserental.function.floor;

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
import com.example.houserental.function.model.FloorDAO;

import java.util.List;

import core.base.BaseApplication;
import core.dialog.GeneralDialog;
import core.util.Constant;

/**
 * Created by leductuan on 3/6/16.
 */
public class FloorListAdapter extends BaseAdapter implements View.OnClickListener, GeneralDialog.DecisionListener, DialogInterface.OnDismissListener {

    private List<FloorDAO> data;

    public FloorListAdapter(List<FloorDAO> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public FloorDAO getItem(int position) {
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
        FloorDAO floor = getItem(position);
        if (row == null) {
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(R.layout.fragment_floor_list_item, null);
            holder = new Holder();
            holder.fragment_floor_list_item_tv_name = (TextView) row.findViewById(R.id.fragment_floor_list_item_tv_name);
            holder.fragment_floor_list_item_tv_device_count = (TextView) row.findViewById(R.id.fragment_floor_list_item_tv_device_count);
            holder.fragment_floor_list_item_tv_room_count = (TextView) row.findViewById(R.id.fragment_floor_list_item_tv_room_count);
            holder.fragment_floor_list_item_tv_user_count = (TextView) row.findViewById(R.id.fragment_floor_list_item_tv_user_count);
            holder.fragment_floor_list_item_im_remove = (ImageView) row.findViewById(R.id.fragment_floor_list_item_im_remove);
            holder.fragment_floor_list_item_im_edit = (ImageView) row.findViewById(R.id.fragment_floor_list_item_im_edit);
            holder.fragment_floor_list_item_im_remove.setOnClickListener(this);
            holder.fragment_floor_list_item_im_edit.setOnClickListener(this);
            row.setTag(holder);
        }
        holder = (Holder) row.getTag();
        holder.fragment_floor_list_item_im_remove.setTag(position);
        holder.fragment_floor_list_item_im_edit.setTag(position);
        holder.fragment_floor_list_item_tv_name.setText(floor.getName());
        holder.fragment_floor_list_item_tv_device_count.setText(String.format(HouseRentalApplication.getContext().getString(R.string.floor_device_count), floor.getDeviceCount()));
        holder.fragment_floor_list_item_tv_user_count.setText(String.format(HouseRentalApplication.getContext().getString(R.string.floor_user_count), floor.getUserCount()));
        holder.fragment_floor_list_item_tv_room_count.setText(String.format(HouseRentalApplication.getContext().getString(R.string.floor_room_count), floor.getRentedRoomCount(), floor.getRoomCount()));
        return row;
    }

    public void clearData() {
        data.clear();
    }

    @Override
    public void onClick(View v) {
        Integer position = (Integer) v.getTag();
        FloorDAO floor = getItem(position);
        switch (v.getId()) {
            case R.id.fragment_floor_list_item_im_edit:
                FloorUpdateDialog dialog = new FloorUpdateDialog(HouseRentalApplication.getActiveActivity(), floor);
                dialog.setOnDismissListener(this);
                dialog.show();
                break;
            case R.id.fragment_floor_list_item_im_remove:
//                if (position < data.size() - 1) {
//                    ((MainActivity) HouseRentalApplication.getActiveActivity()).showAlertDialog(HouseRentalApplication.getActiveActivity(),
//                            Constant.DELETE_FLOOR_ERROR_DIALOG,
//                            -1,
//                            String.format(HouseRentalApplication.getContext().getString(R.string.delete_dialog_title),
//                                    floor.getName()),
//                            HouseRentalApplication.getContext().getString(R.string.delete_floor_error_dialog_message),
//                            HouseRentalApplication.getContext().getString(R.string.common_ok), null);
//                    return;
//                }
                ((MainActivity) HouseRentalApplication.getActiveActivity()).showDecisionDialog(HouseRentalApplication.getActiveActivity(),
                        Constant.DELETE_FLOOR_DIALOG,
                        -1,
                        String.format(HouseRentalApplication.getContext().getString(R.string.delete_dialog_title), floor.getName()),
                        String.format(HouseRentalApplication.getContext().getString(R.string.delete_dialog_message), floor.getName())
                                + "\n"
                                + HouseRentalApplication.getContext().getString(R.string.delete_floor_dialog_message),
                        HouseRentalApplication.getContext().getString(R.string.common_ok),
                        HouseRentalApplication.getContext().getString(R.string.common_cancel), null, floor, this);
                break;
        }
    }

    @Override
    public void onAgreed(int id, Object onWhat) {
        switch (id) {
            case Constant.DELETE_FLOOR_DIALOG:
                FloorDAO floor = (FloorDAO) onWhat;
                DAOManager.deleteFloor(floor.getId());
                data.remove(floor);
                notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onDisAgreed(int id, Object onWhat) {
        switch (id) {
            case Constant.DELETE_FLOOR_DIALOG:
                break;
        }
    }

    @Override
    public void onNeutral(int id, Object onWhat) {
        switch (id) {
            case Constant.DELETE_FLOOR_DIALOG:
                break;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        notifyDataSetChanged();
    }

    private class Holder {
        TextView fragment_floor_list_item_tv_room_count;
        TextView fragment_floor_list_item_tv_user_count;
        TextView fragment_floor_list_item_tv_device_count;
        TextView fragment_floor_list_item_tv_name;
        ImageView fragment_floor_list_item_im_remove;
        ImageView fragment_floor_list_item_im_edit;
    }
}
