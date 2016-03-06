package com.example.houserental.function.user;

import android.content.Intent;
import android.view.View;

import com.example.houserental.R;
import com.example.houserental.core.base.BaseMultipleFragment;
import com.example.houserental.function.MainActivity;

/**
 * Created by leductuan on 3/5/16.
 */
public class UserListScreen extends BaseMultipleFragment {

    public static final String TAG = UserListScreen.class.getSimpleName();

    public static UserListScreen getInstance() {
        return new UserListScreen();
    }

    @Override
    public void onBaseCreate() {

    }

    @Override
    public void onDeepLinking(Intent data) {

    }

    @Override
    public void onNotification(Intent data) {

    }

    @Override
    public void onBindView() {

    }

    @Override
    public void onInitializeViewData() {

    }

    @Override
    public void onBaseResume() {
        ((MainActivity) getActiveActivity()).setScreenHeader(getString(R.string.main_header_user));
    }

    @Override
    public void onBaseFree() {

    }

    @Override
    public void onSingleClick(View v) {

    }
}
