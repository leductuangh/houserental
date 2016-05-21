package com.example.houserental.function.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.houserental.function.MainActivity;
import com.example.houserental.function.model.DAOManager;

import core.base.BaseMultipleFragment;

/**
 * Created by leductuan on 3/14/16.
 */
public class PaymentHistoryScreen extends BaseMultipleFragment {

    public static final String TAG = PaymentHistoryScreen.class.getSimpleName();
    private ExpandableListView fragment_payment_el_monthly_payment;
    private PaymentHistoryAdapter adapter;


    public static PaymentHistoryScreen getInstance() {
        PaymentHistoryScreen screen = new PaymentHistoryScreen();
        Bundle bundle = new Bundle();
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(com.example.houserental.R.layout.fragment_payment_history, container, false);
    }

    @Override
    public void onBaseCreate() {
        adapter = new PaymentHistoryAdapter(DAOManager.getAllMonthlyPayments());
//        adapter = DAOManager.getAllPayments();
    }

    @Override
    public void onDeepLinking(Intent data) {

    }

    @Override
    public void onNotification(Intent data) {

    }

    @Override
    public void onBindView() {
        fragment_payment_el_monthly_payment = (ExpandableListView) findViewById(com.example.houserental.R.id.fragment_payment_el_monthly_payment);
        fragment_payment_el_monthly_payment.setAdapter(adapter);
    }

    @Override
    public void onInitializeViewData() {

    }

    @Override
    public void onBaseResume() {
        ((MainActivity) getActiveActivity()).setScreenHeader(getString(com.example.houserental.R.string.main_header_payment_history));
    }

    @Override
    public void onBaseFree() {

    }

    @Override
    public void onSingleClick(View v) {

    }
}
