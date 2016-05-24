package com.example.houserental.function;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.houserental.R;
import com.example.houserental.function.floor.FloorListScreen;
import com.example.houserental.function.home.HomeScreen;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.FloorDAO;
import com.example.houserental.function.model.RoomTypeDAO;
import com.example.houserental.function.payment.PaymentHistoryScreen;
import com.example.houserental.function.room.RoomListScreen;
import com.example.houserental.function.setting.SettingScreen;
import com.example.houserental.function.user.UserListScreen;
import com.google.common.collect.Lists;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import core.base.BaseMultipleFragmentActivity;
import core.data.DataSaver;
import core.dialog.GeneralDialog;
import core.util.Constant;

/**
 * Created by leductuan on 3/5/16.
 */
public class MainActivity extends BaseMultipleFragmentActivity implements GeneralDialog.DecisionListener, AdapterView.OnItemClickListener, DrawerLayout.DrawerListener {

    public static final String TAG = MainActivity.class.getName();
    private MainMenuAdapter activity_main_menu_adapter;
    private ListView activity_main_lv_menu;
    private DrawerLayout activity_main_dl;
    private TextView activity_main_tv_header, activity_main_tv_time;
    private DrawerArrowDrawable activity_main_menu_arrow_drawable;
    private ValueAnimator animator;
    private ImageView activity_main_im_menu_toggle;
    private SimpleDateFormat formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void initDB() {
        for (int i = 1; i < 3; ++i) {
            DAOManager.addFloor(getString(R.string.common_floor) + " " + i, i);
        }

        RoomTypeDAO level_1 = new RoomTypeDAO("Cấp 1", 1600);
        level_1.save();
        RoomTypeDAO level_2 = new RoomTypeDAO("Cấp 2", 1800);
        level_2.save();
        RoomTypeDAO level_3 = new RoomTypeDAO("Cấp 3", 2000);
        level_3.save();
        RoomTypeDAO level_4 = new RoomTypeDAO("Cấp 4", 2500);
        level_4.save();


        List<FloorDAO> floors = DAOManager.getAllFloors();
        int floor_count = 0;
        for (FloorDAO floor : floors) {
            for (int i = 1; i < 11; ++i) {
                DAOManager.addRoom(getString(R.string.common_room) + " " + (floor_count + i), 16, level_1.getId(), false, null, 0, 0, 0, floor.getId());
            }
            floor_count += 10;
        }
    }

    @Override
    protected void onInitializeFragments() {
        try {
            if (DataSaver.getInstance().isEnabled(DataSaver.Key.INITIALIZED)) {
                addFragment(R.id.activity_main_container, HomeScreen.getInstance(), HomeScreen.TAG);
                activity_main_im_menu_toggle.setVisibility(View.VISIBLE);
            } else {
                initDB();
                addFragment(R.id.activity_main_container, SettingScreen.getInstance(), SettingScreen.TAG);
                activity_main_dl.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                activity_main_im_menu_toggle.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onLastFragmentBack(int containerId) {
        if (closeMenu()) return;

        showDecisionDialog(this, Constant.EXIT_APPLICATION_DIALOG, getGeneralDialogLayoutResource(),
                -1,
                getString(R.string.application_exit_dialog_title),
                getString(R.string.application_exit_dialog_message),
                getString(R.string.common_ok),
                getString(R.string.common_cancel), null, null, this);
    }

    @Override
    public void onBackPressed() {
        if (closeMenu()) return;
        super.onBackPressed();
    }

    @Override
    public void onBaseCreate() {
        formatter = new SimpleDateFormat("dd-MMM-yyyy");
        activity_main_menu_adapter = new MainMenuAdapter(new ArrayList<>(Lists.newArrayList(getResources().getStringArray(R.array.home_menu))));
        activity_main_menu_arrow_drawable = new DrawerArrowDrawable(this);
        animator = ValueAnimator.ofFloat(0f, 1f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                activity_main_menu_arrow_drawable.setProgress((Float) animation.getAnimatedValue());
            }
        });
    }

    @Override
    public void onDeepLinking(Intent data) {

    }

    @Override
    public void onNotification(Intent data) {

    }

    @Override
    public void onBindView() {
        checkTimeZoneAndLocale();
        activity_main_tv_time = (TextView) findViewById(R.id.activity_main_tv_time);
        activity_main_im_menu_toggle = (ImageView) findViewById(R.id.activity_main_im_menu_toggle);
        activity_main_dl = (DrawerLayout) findViewById(R.id.activity_main_dl);
        activity_main_lv_menu = (ListView) findViewById(R.id.activity_main_lv_menu);
        activity_main_lv_menu.setAdapter(activity_main_menu_adapter);
        activity_main_lv_menu.setOnItemClickListener(this);
        activity_main_tv_header = (TextView) findViewById(R.id.activity_main_tv_header);
        activity_main_im_menu_toggle.setImageDrawable(activity_main_menu_arrow_drawable);
        activity_main_dl.addDrawerListener(this);
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
        switch (v.getId()) {
            case R.id.activity_main_im_menu_toggle:
                if (activity_main_dl.isDrawerOpen(Gravity.LEFT)) {
                    activity_main_dl.closeDrawer(Gravity.LEFT);
                } else {
                    activity_main_dl.openDrawer(Gravity.LEFT);
                }
                break;
        }
    }

    @Override
    public void onAgreed(int id, Object onWhat) {
        switch (id) {
            case Constant.EXIT_APPLICATION_DIALOG:
                finish();
                break;
        }
    }

    @Override
    public void onDisAgreed(int id, Object onWhat) {
        switch (id) {
            case Constant.EXIT_APPLICATION_DIALOG:
                break;
        }
    }

    @Override
    public void onNeutral(int id, Object onWhat) {
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
                if (currentFragmentTag.equals(PaymentHistoryScreen.TAG))
                    return;
                replaceFragment(R.id.activity_main_container, PaymentHistoryScreen.getInstance(), PaymentHistoryScreen.TAG, true);
                break;
            case 5:
                // Setting screen
                if (currentFragmentTag.equals(SettingScreen.TAG))
                    return;
                replaceFragment(R.id.activity_main_container, SettingScreen.getInstance(), SettingScreen.TAG, true);
                break;
            case 6:
                // Exit
                showDecisionDialog(this, Constant.EXIT_APPLICATION_DIALOG, getGeneralDialogLayoutResource(),
                        -1,
                        getString(R.string.application_exit_dialog_title),
                        getString(R.string.application_exit_dialog_message),
                        getString(R.string.common_ok),
                        getString(R.string.common_cancel), null, null, this);
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
        Calendar now = Calendar.getInstance();
        activity_main_tv_time.setText(formatter.format(now.getTime()));
    }

    @Override
    public int getGeneralDialogLayoutResource() {
        return R.layout.house_rental_general_dialog;
    }

    public void unlockMenu() {
        activity_main_dl.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        activity_main_im_menu_toggle.setVisibility(View.VISIBLE);
    }

    private void checkTimeZoneAndLocale() {

        if (!TimeZone.getDefault().getID().equals("Asia/Bangkok")) {
            Toast.makeText(this, getString(R.string.application_wrong_timezone_message), Toast.LENGTH_LONG).show();
            TimeZone.setDefault(new SimpleTimeZone(7, "Asia/Bankok"));
//            finish();
        }
        if (!(Locale.getDefault().getCountry().equals("VN") && Locale.getDefault().getLanguage().equals("vi"))) {
            Toast.makeText(this, getString(R.string.application_wrong_locale_message), Toast.LENGTH_LONG).show();
            Locale locale = new Locale("vi");
            Locale.setDefault(locale);
            Configuration config = getResources().getConfiguration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
//            finish();
        }
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {
        if (newState == DrawerLayout.STATE_SETTLING) {
            if (!activity_main_dl.isDrawerOpen(Gravity.LEFT)) {
                // starts opening
                animator.start();
            } else {
                // closing drawer
                animator.reverse();
            }
        }
    }
}
