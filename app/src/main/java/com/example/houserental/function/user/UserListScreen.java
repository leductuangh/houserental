package com.example.houserental.function.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.houserental.R;
import com.example.houserental.function.MainActivity;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.RoomDAO;
import com.example.houserental.function.model.UserDAO;

import java.util.List;

import core.base.BaseMultipleFragment;
import core.dialog.GeneralDialog;
import core.util.Constant;

/**
 * Created by leductuan on 3/5/16.
 */
public class UserListScreen extends BaseMultipleFragment implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener, GeneralDialog.DecisionListener {

    public static final String TAG = UserListScreen.class.getSimpleName();
    private static final String ROOM_KEY = "room_key";
    private RoomDAO room;
    private List<UserDAO> data;
    private UserListAdapter adapter;
    private ListView fragment_user_list_lv;
    private Long deleted_user;

    public static UserListScreen getInstance(RoomDAO room) {
        UserListScreen screen = new UserListScreen();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ROOM_KEY, room);
        screen.setArguments(bundle);
        return screen;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_list, container, false);
    }

    @Override
    public void onBaseCreate() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            room = (RoomDAO) bundle.getSerializable(ROOM_KEY);
        }

        if (room == null)
            data = DAOManager.getAllUsers();
        else
            data = DAOManager.getUsersOfRoom(room.getId());
        adapter = new UserListAdapter(data, false);
    }

    @Override
    public void onDeepLinking(Intent data) {

    }

    @Override
    public void onNotification(Intent data) {

    }

    @Override
    public void onBindView() {
        super.onBindView();
        fragment_user_list_lv = (ListView) findViewById(R.id.fragment_user_list_lv);
        fragment_user_list_lv.setAdapter(adapter);
        fragment_user_list_lv.setOnItemClickListener(this);
        registerSingleAction(R.id.fragment_user_list_fab_add);
    }

    @Override
    public void onInitializeViewData() {

    }

    @Override
    public void onBaseResume() {
        String room_name = "";
        if (room != null)
            room_name = room.getName();
        ((MainActivity) getActiveActivity()).setScreenHeader(getString(R.string.main_header_user) + " " + room_name);
        refreshUserList();
    }

    @Override
    public void onBaseFree() {

    }

    @Override
    public void onSingleClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_user_list_fab_add:
                addFragment(R.id.activity_main_container, UserInsertScreen.getInstance(room));
                break;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0)
            return true;
        deleted_user = ((UserDAO) parent.getItemAtPosition(position)).getId();

        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        addFragment(R.id.activity_main_container, UserDetailScreen.getInstance((UserDAO) parent.getItemAtPosition(position)));
    }

    private void refreshUserList() {
        if (data != null) {
            data.clear();
            if (room == null)
                data.addAll(DAOManager.getAllUsers());
            else
                data.addAll(DAOManager.getUsersOfRoom(room.getId()));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAgreed(int id, Object onWhat) {
        if (id == Constant.DELETE_USER_DIALOG) {
            DAOManager.deleteUser(deleted_user);
            refreshUserList();
        }
    }

    @Override
    public void onDisAgreed(int id, Object onWhat) {

    }

    @Override
    public void onNeutral(int id, Object onWhat) {

    }
}
