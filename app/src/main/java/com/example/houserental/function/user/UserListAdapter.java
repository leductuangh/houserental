package com.example.houserental.function.user;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.houserental.R;
import com.example.houserental.function.HouseRentalApplication;
import com.example.houserental.function.MainActivity;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.UserDAO;

import java.util.List;

import core.base.BaseApplication;
import core.dialog.GeneralDialog;
import core.util.Constant;

/**
 * Created by leductuan on 3/7/16.
 */
public class UserListAdapter extends BaseAdapter implements View.OnClickListener, GeneralDialog.DecisionListener {

    private List<UserDAO> data;
    private boolean isSimplified = true;

    public UserListAdapter(List<UserDAO> data, boolean isSimplified) {
        this.data = data;
        this.isSimplified = isSimplified;
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
        UserDAO user = getItem(position);
        if (row == null) {
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(com.example.houserental.R.layout.fragment_user_list_item, null);
            holder = new Holder();
            holder.fragment_user_list_item_tv_age = (TextView) row.findViewById(R.id.fragment_user_list_item_tv_age);
            holder.fragment_user_list_item_tv_device_count = (TextView) row.findViewById(R.id.fragment_user_list_item_tv_device_count);
            holder.fragment_user_list_item_tv_phone = (TextView) row.findViewById(R.id.fragment_user_list_item_tv_phone);
            holder.fragment_user_list_item_tv_name = (TextView) row.findViewById(com.example.houserental.R.id.fragment_user_list_item_tv_name);
            holder.fragment_user_list_item_tv_gender = (TextView) row.findViewById(R.id.fragment_user_list_item_tv_gender);
            holder.fragment_user_list_item_im_remove = (ImageView) row.findViewById(R.id.fragment_user_list_item_im_remove);
            holder.fragment_user_list_item_im_remove.setOnClickListener(this);
            row.setTag(holder);
        }
        holder = (Holder) row.getTag();
        if (isSimplified) {
            holder.fragment_user_list_item_im_remove.setVisibility(View.GONE);
            holder.fragment_user_list_item_tv_phone.setVisibility(View.GONE);
            holder.fragment_user_list_item_tv_device_count.setVisibility(View.GONE);
            holder.fragment_user_list_item_tv_age.setVisibility(View.GONE);
            holder.fragment_user_list_item_tv_gender.setVisibility(View.GONE);
        } else {
            holder.fragment_user_list_item_im_remove.setVisibility(View.VISIBLE);
            holder.fragment_user_list_item_tv_phone.setVisibility(View.VISIBLE);
            holder.fragment_user_list_item_tv_device_count.setVisibility(View.VISIBLE);
            holder.fragment_user_list_item_tv_age.setVisibility(View.VISIBLE);
            holder.fragment_user_list_item_tv_gender.setVisibility(View.VISIBLE);
            holder.fragment_user_list_item_im_remove.setTag(position);
            holder.fragment_user_list_item_tv_phone.setText(String.format(HouseRentalApplication.getContext().getString(R.string.user_phone_title), user.getPhone()));
            holder.fragment_user_list_item_tv_device_count.setText(String.format(HouseRentalApplication.getContext().getString(R.string.user_device_count), user.getDeviceCount()));
            holder.fragment_user_list_item_tv_age.setText(String.format(HouseRentalApplication.getContext().getString(R.string.user_age_title), user.getAge()));
            holder.fragment_user_list_item_tv_gender.setText(String.format(HouseRentalApplication.getContext().getString(R.string.user_gender), user.getGender() == 1 ? HouseRentalApplication.getContext().getString(R.string.user_gender_male) : HouseRentalApplication.getContext().getString(R.string.user_gender_female)));
        }

        holder.fragment_user_list_item_tv_name.setText(user.getName());
        return row;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_user_list_item_im_remove:
                UserDAO user = getItem((Integer) v.getTag());
                ((MainActivity) HouseRentalApplication.getActiveActivity()).showDecisionDialog(HouseRentalApplication.getActiveActivity(), Constant.DELETE_USER_DIALOG, ((MainActivity) HouseRentalApplication.getActiveActivity()).getGeneralDialogLayoutResource(), -1, HouseRentalApplication.getContext().getString(R.string.application_alert_dialog_title), HouseRentalApplication.getContext().getString(R.string.delete_user_dialog_message), HouseRentalApplication.getContext().getString(R.string.common_ok), HouseRentalApplication.getContext().getString(R.string.common_cancel), null, user, this);
                break;
        }
    }

    @Override
    public void onAgreed(int id, Object onWhat) {
        if (id == Constant.DELETE_USER_DIALOG) {
            UserDAO user = (UserDAO) onWhat;
            DAOManager.deleteUser(user.getId());
            data.remove(user);
            notifyDataSetChanged();
        }
    }

    @Override
    public void onDisAgreed(int id, Object onWhat) {

    }

    @Override
    public void onNeutral(int id, Object onWhat) {

    }

    private class Holder {
        TextView fragment_user_list_item_tv_age;
        TextView fragment_user_list_item_tv_device_count;
        TextView fragment_user_list_item_tv_phone;
        TextView fragment_user_list_item_tv_name;
        TextView fragment_user_list_item_tv_gender;
        ImageView fragment_user_list_item_im_remove;
    }
}
