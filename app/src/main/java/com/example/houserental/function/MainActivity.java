package com.example.houserental.function;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.houserental.R;
import com.example.houserental.function.floor.FloorListScreen;
import com.example.houserental.function.home.HomeScreen;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.FloorDAO;
import com.example.houserental.function.model.RoomTypeDAO;
import com.example.houserental.function.payment.PaymentRecordScreen;
import com.example.houserental.function.room.RoomListScreen;
import com.example.houserental.function.setting.SettingScreen;
import com.example.houserental.function.user.UserListScreen;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import core.base.BaseMultipleFragmentActivity;
import core.data.DataSaver;
import core.dialog.GeneralDialog;
import core.util.Constant;

/**
 * Created by leductuan on 3/5/16.
 */
public class MainActivity extends BaseMultipleFragmentActivity implements GeneralDialog.DecisionListener, AdapterView.OnItemClickListener {


    private MainMenuAdapter activity_main_menu_adapter;
    private ListView activity_main_lv_menu;
    private DrawerLayout activity_main_dl;
    private TextView activity_main_tv_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            if (!DataSaver.getInstance().isEnabled(DataSaver.Key.INITIALIZED)) {
                DataSaver.getInstance().setEnabled(DataSaver.Key.INITIALIZED, true);
                initDB();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initDB() {
        for (int i = 1; i < 3; ++i) {
            DAOManager.addFloor(getString(R.string.common_floor) + " " + i, i);
        }

        RoomTypeDAO level_1 = new RoomTypeDAO("Cap 4", 1600);
        level_1.save();
        RoomTypeDAO level_2 = new RoomTypeDAO("Cap 3", 1800);
        level_2.save();
        RoomTypeDAO level_3 = new RoomTypeDAO("Cap 2", 2000);
        level_3.save();
        RoomTypeDAO level_4 = new RoomTypeDAO("Cap 1", 2500);
        level_4.save();


        List<FloorDAO> floors = DAOManager.getAllFloors();
        int floor_count = 0;
        for (FloorDAO floor : floors) {
            for (int i = 1; i < 11; ++i) {
                DAOManager.addRoom(getString(R.string.common_room) + " " + (floor_count + i), 16, null, false, null, 0, 0, floor.getId());
            }
            floor_count += 10;
        }
    }

    @Override
    protected void onInitializeFragments() {
        addFragment(R.id.activity_main_container, HomeScreen.getInstance(), HomeScreen.TAG);
    }

    @Override
    protected void onLastFragmentBack(int containerId) {
        if (closeMenu()) return;

        showDecisionDialog(this, Constant.EXIT_APPLICATION_DIALOG,
                -1,
                getString(R.string.application_exit_dialog_title),
                getString(R.string.application_exit_dialog_message),
                getString(R.string.common_ok),
                getString(R.string.common_cancel), null, this);
    }

    @Override
    public void onBackPressed() {
        if (closeMenu()) return;
        super.onBackPressed();
    }

    @Override
    public void onBaseCreate() {
        activity_main_menu_adapter = new MainMenuAdapter(new ArrayList<>(Lists.newArrayList(getResources().getStringArray(R.array.home_menu))));
    }

    @Override
    public void onDeepLinking(Intent data) {

    }

    @Override
    public void onNotification(Intent data) {

    }

    @Override
    public void onBindView() {
        activity_main_dl = (DrawerLayout) findViewById(R.id.activity_main_dl);
        activity_main_lv_menu = (ListView) findViewById(R.id.activity_main_lv_menu);
        activity_main_lv_menu.setAdapter(activity_main_menu_adapter);
        activity_main_lv_menu.setOnItemClickListener(this);
        activity_main_tv_header = (TextView) findViewById(R.id.activity_main_tv_header);
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

    @Override
    public void onAgreed(int id) {
        switch (id) {
            case Constant.EXIT_APPLICATION_DIALOG:
                finish();
                break;
        }
    }

    @Override
    public void onDisAgreed(int id) {
        switch (id) {
            case Constant.EXIT_APPLICATION_DIALOG:
                break;
        }
    }

    @Override
    public void onNeutral(int id) {
        switch (id) {
            case Constant.EXIT_APPLICATION_DIALOG:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        closeMenu();
        String currentFragmentTag = getTopFragment(getMainContainerId()).getTag();
        switch (position) {
            case 0:
                // Home screen
                if (currentFragmentTag.equals(HomeScreen.TAG))
                    return;
                replaceFragment(R.id.activity_main_container, HomeScreen.getInstance(), HomeScreen.TAG, true);
                break;
            case 1:
                // FloorDAO list screen
                if (currentFragmentTag.equals(FloorListScreen.TAG))
                    return;
                replaceFragment(R.id.activity_main_container, FloorListScreen.getInstance(), FloorListScreen.TAG, true);
                break;
            case 2:
                // RoomDAO list screen
                if (currentFragmentTag.equals(RoomListScreen.TAG))
                    return;
                replaceFragment(R.id.activity_main_container, RoomListScreen.getInstance(null), RoomListScreen.TAG, true);
                break;
            case 3:
                // UserDAO list screen
                if (currentFragmentTag.equals(UserListScreen.TAG))
                    return;
                replaceFragment(R.id.activity_main_container, UserListScreen.getInstance(null), UserListScreen.TAG, true);
                break;
            case 4:
                // Payment history screen
                if (currentFragmentTag.equals(PaymentRecordScreen.TAG))
                    return;
                replaceFragment(R.id.activity_main_container, PaymentRecordScreen.getInstance(), PaymentRecordScreen.TAG, true);
                break;
            case 5:
                // Setting screen
                if (currentFragmentTag.equals(SettingScreen.TAG))
                    return;
                replaceFragment(R.id.activity_main_container, SettingScreen.getInstance(), SettingScreen.TAG, true);
                break;
            case 6:
                // Exit
                showDecisionDialog(this, Constant.EXIT_APPLICATION_DIALOG,
                        -1,
                        getString(R.string.application_exit_dialog_title),
                        getString(R.string.application_exit_dialog_message),
                        getString(R.string.common_ok),
                        getString(R.string.common_cancel), null, this);
                break;
        }
    }

    private boolean closeMenu() {
        if (activity_main_dl != null && activity_main_dl.isDrawerOpen(Gravity.LEFT)) {
            activity_main_dl.closeDrawer(Gravity.LEFT);
            return true;
        }
        return false;
    }

    public void setScreenHeader(String header) {
        if (activity_main_tv_header != null)
            activity_main_tv_header.setText(header);
    }
}
