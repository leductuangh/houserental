package com.example.houserental.function.payment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.core.core.base.BaseApplication;
import com.example.houserental.function.model.Payment;
import com.example.houserental.function.model.PaymentDAO;

import java.util.List;

/**
 * Created by Tyrael on 3/15/16.
 */
public class PaymentHistoryAdapter extends BaseExpandableListAdapter {

    private List<Payment> data;

    public PaymentHistoryAdapter(List<Payment> data) {
        this.data = data;
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(groupPosition).getPayments().size();
    }

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
        if (row == null) {
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(com.example.houserental.R.layout.fragment_payment_history_parent_item, null);
            holder = new GroupHolder();
            holder.fragment_payment_history_parent_item_tv_name = (TextView) row.findViewById(com.example.houserental.R.id.fragment_payment_history_parent_item_tv_name);
            row.setTag(holder);
        }
        holder = (GroupHolder) row.getTag();
        holder.fragment_payment_history_parent_item_tv_name.setText(getGroup(groupPosition).getName());
        return row;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder holder = null;
        View row = convertView;
        if (row == null) {
            row = BaseApplication.getActiveActivity().getLayoutInflater().inflate(com.example.houserental.R.layout.fragment_payment_history_child_item, null);
            holder = new ChildHolder();
            holder.fragment_payment_history_child_item_tv_name = (TextView) row.findViewById(com.example.houserental.R.id.fragment_payment_history_child_item_tv_name);
            row.setTag(holder);
        }
        holder = (ChildHolder) row.getTag();
        holder.fragment_payment_history_child_item_tv_name.setText(getGroup(groupPosition).getName());
        return row;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class GroupHolder {
        TextView fragment_payment_history_parent_item_tv_name;
    }

    private class ChildHolder {
        TextView fragment_payment_history_child_item_tv_name;
    }
}
