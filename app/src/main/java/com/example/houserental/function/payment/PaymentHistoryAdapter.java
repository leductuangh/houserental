package com.example.houserental.function.payment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.houserental.R;
import com.example.houserental.function.HouseRentalApplication;
import com.example.houserental.function.HouseRentalUtils;
import com.example.houserental.function.model.Payment;
import com.example.houserental.function.model.PaymentDAO;
import com.example.houserental.function.view.FetchableExpandableListView;

import java.util.List;

import core.base.BaseApplication;

/**
 * Created by Tyrael on 3/15/16.
 */
public class PaymentHistoryAdapter extends FetchableExpandableListView.AnimatedExpandableListAdapter {

    private List<Payment> data;

    public PaymentHistoryAdapter(List<Payment> data) {
        this.data = data;
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

//    @Override
//    public int getChildrenCount(int groupPosition) {
//        return data.get(groupPosition).getPayments().size();
//    }

    @Override
    public Payment getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public PaymentDAO getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).getPayments().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder holder = null;
        View row = convertView;
        Payment group = getGroup(groupPosition);
        if (row == null) {
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(com.example.houserental.R.layout.fragment_payment_history_parent_item, null);
            holder = new GroupHolder();
            holder.fragment_payment_history_parent_item_tv_name = (TextView) row.findViewById(com.example.houserental.R.id.fragment_payment_history_parent_item_tv_name);
            holder.fragment_payment_history_parent_item_tv_count = (TextView) row.findViewById(R.id.fragment_payment_history_parent_item_tv_count);
            row.setTag(holder);
        }
        holder = (GroupHolder) row.getTag();
        holder.fragment_payment_history_parent_item_tv_count.setText(String.format(HouseRentalApplication.getContext().getString(R.string.payment_history_transaction_count_text), group.getPayments().size()));
        holder.fragment_payment_history_parent_item_tv_name.setText(group.getName());
        return row;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder holder = null;
        View row = convertView;
        PaymentDAO payment = getGroup(groupPosition).getPayments().get(childPosition);
        if (row == null) {
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(com.example.houserental.R.layout.fragment_payment_history_child_item, null);
            holder = new ChildHolder();
            holder.fragment_payment_history_child_item_tv_name = (TextView) row.findViewById(com.example.houserental.R.id.fragment_payment_history_child_item_tv_name);
            holder.fragment_history_child_item_tv_day_count = (TextView) row.findViewById(R.id.fragment_history_child_item_tv_day_count);
            holder.fragment_history_child_item_tv_paid_amount = (TextView) row.findViewById(R.id.fragment_history_child_item_tv_paid_amount);
            row.setTag(holder);
        }
        holder = (ChildHolder) row.getTag();
        holder.fragment_history_child_item_tv_paid_amount.setText(String.format(HouseRentalApplication.getContext().getString(R.string.payment_history_transaction_paid_amount_text), HouseRentalUtils.toThousandVND(payment.getTotal() + payment.getDepositTotal())));
        int stay_days = payment.getStayDays();
        if (stay_days == HouseRentalUtils.dayCountOfMonth(payment.getStartDate().getMonth(), payment.getStartDate().getYear())) {
            holder.fragment_history_child_item_tv_day_count.setText(HouseRentalApplication.getContext().getString(R.string.payment_history_transaction_day_count_month_text));
        } else {
            holder.fragment_history_child_item_tv_day_count.setText(String.format(HouseRentalApplication.getContext().getString(R.string.payment_history_transaction_day_count_days_text), stay_days));
        }
        holder.fragment_payment_history_child_item_tv_name.setText(String.format("%1$s: %2$s", payment.getRoomName(), payment.getCode().toUpperCase()));
        return row;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return data.get(groupPosition).getPayments().size();
    }

//    @Override
//    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//        ChildHolder holder = null;
//        View row = convertView;
//        if (row == null) {
//            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(com.example.houserental.R.layout.fragment_payment_history_child_item, null);
//            holder = new ChildHolder();
//            holder.fragment_payment_history_child_item_tv_name = (TextView) row.findViewById(com.example.houserental.R.id.fragment_payment_history_child_item_tv_name);
//            row.setTag(holder);
//        }
//        holder = (ChildHolder) row.getTag();
//        holder.fragment_payment_history_child_item_tv_name.setText(getGroup(groupPosition).getPayments().get(childPosition).getRoomName());
//        return row;
//    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class GroupHolder {
        TextView fragment_payment_history_parent_item_tv_name;
        TextView fragment_payment_history_parent_item_tv_count;
    }

    private class ChildHolder {
        TextView fragment_payment_history_child_item_tv_name;
        TextView fragment_history_child_item_tv_day_count;
        TextView fragment_history_child_item_tv_paid_amount;
    }
}
