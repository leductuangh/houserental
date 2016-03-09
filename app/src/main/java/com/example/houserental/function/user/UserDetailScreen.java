package com.example.houserental.function.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.houserental.R;
import com.example.houserental.core.base.BaseMultipleFragment;
import com.example.houserental.model.DAOManager;
import com.example.houserental.model.DeviceDAO;
import com.example.houserental.model.UserDAO;

import java.util.List;

/**
 * Created by leductuan on 3/6/16.
 */
public class UserDetailScreen extends BaseMultipleFragment {

    public static final String TAG = UserDetailScreen.class.getSimpleName();
    private static final String USER_KEY = "user_key";
    private UserDAO user;
    private List<DeviceDAO> devices;


    public static UserDetailScreen getInstance(UserDAO user) {
        UserDetailScreen screen = new UserDetailScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER_KEY, user);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_detail, container, false);
    }

    @Override
    public void onBaseCreate() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            user = (UserDAO) bundle.getSerializable(USER_KEY);
        }

        if (user != null) {
            devices = DAOManager.getDevicesOfUser(user.getUserId());
        }
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

    }

    @Override
    public void onBaseFree() {

    }

    @Override
    public void onSingleClick(View v) {

    }
}
