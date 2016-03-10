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
import com.example.houserental.core.base.BaseMultipleFragmentActivity;
import com.example.houserental.data.DataSaver;
import com.example.houserental.dialog.GeneralDialog;
import com.example.houserental.function.floor.FloorListScreen;
import com.example.houserental.function.home.HomeScreen;
import com.example.houserental.function.room.RoomListScreen;
import com.example.houserental.function.setting.SettingScreen;
import com.example.houserental.function.user.UserListScreen;
import com.example.houserental.model.DAOManager;
import com.example.houserental.model.RoomDAO;
import com.example.houserental.util.Constant;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

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
            DAOManager.addFloor("floor_" + i, getString(R.string.common_floor) + " " + i, i + 1);
        }
        for (int i = 1; i < 21; ++i) {
            int floor_index = Math.round(i / 10) + 1;
            int room_index = i;
            if (room_index > 11)
                room_index -= 10;
            DAOManager.addRoom("F_" + floor_index + "_R_" + room_index, getString(R.string.common_room) + " " + i, 16, RoomDAO.Type.NORMAL, false, "floor_" + floor_index);
        }

        List<RoomDAO> rooms = DAOManager.getRoomsOfFloor("floor_1");

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
        activity_main_menu_adapter = new MainMenuAdapter(new ArrayList<String>(Lists.newArrayList(getResources().getStringArray(R.array.home_menu))));
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
        activity_main_lv_menu.setItemChecked(0, true);

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

        if (activity_main_lv_menu != null) {
            if (activity_main_lv_menu.getSelectedItemPosition() == position)
                return;
        }

        switch (position) {
            case 0:
                // Home screen
                replaceFragment(R.id.activity_main_container, HomeScreen.getInstance(), HomeScreen.TAG, true);
                break;
            case 1:
                // FloorDAO list screen
                replaceFragment(R.id.activity_main_container, FloorListScreen.getInstance(), FloorListScreen.TAG, true);
                break;
            case 2:
                // RoomDAO list screen
                replaceFragment(R.id.activity_main_container, RoomListScreen.getInstance(null), RoomListScreen.TAG, true);
                break;
            case 3:
                // UserDAO list screen
                replaceFragment(R.id.activity_main_container, UserListScreen.getInstance(null), UserListScreen.TAG, true);
                break;
            case 4:
                // Setting screen
                replaceFragment(R.id.activity_main_container, SettingScreen.getInstance(), SettingScreen.TAG, true);
                break;
            case 5:
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
