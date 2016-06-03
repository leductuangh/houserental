package com.example.houserental.function.user;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.houserental.R;
import com.example.houserental.function.HouseRentalApplication;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.DeviceDAO;
import com.example.houserental.function.model.UserDAO;

import java.util.ArrayList;

import core.base.BaseApplication;
import core.base.BaseDialog;
import core.util.SingleClick;

/**
 * Created by leductuan on 3/12/16.
 */
public class UserDeviceDialog extends BaseDialog implements SingleClick.SingleClickListener, TextWatcher, View.OnFocusChangeListener, KeyboardView.OnKeyboardActionListener, AdapterView.OnItemSelectedListener {

    private static final String MAC_FORMAT = "%s:%s:%s:%s:%s:%s";
    private EditText dialog_device_insert_tv_MAC_1, dialog_device_insert_tv_MAC_2, dialog_device_insert_tv_MAC_3, dialog_device_insert_tv_MAC_4, dialog_device_insert_tv_MAC_5, dialog_device_insert_tv_MAC_6;
    private EditText current_focus, dialog_device_insert_tv_description;
    private Button dialog_device_insert_bt_ok, dialog_device_insert_bt_cancel;
    private KeyboardView dialog_device_kb_mac;
    private Keyboard keyboard;
    private Long user;
    private Long room;
    private DeviceDAO device;
    private Spinner user_device_insert_sp_transfer;
    private TextView dialog_device_tv_title, user_device_insert_tv_transfer;

    public UserDeviceDialog(Context context, Long user) {
        super(context);
        this.user = user;
    }

    public UserDeviceDialog(Context context, DeviceDAO device, Long room) {
        super(context);
        this.device = device;
        this.room = room;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_device_insert);
    }

    @Override
    protected void onBaseCreate() {
        getSingleClick().setListener(this);
        keyboard = new Keyboard(BaseApplication.getActiveActivity(), R.xml.mac);
    }

    @Override
    protected void onBindView() {
        dialog_device_kb_mac = (KeyboardView) findViewById(R.id.dialog_device_kb_mac);
        dialog_device_kb_mac.setKeyboard(keyboard);
        dialog_device_kb_mac.setOnKeyboardActionListener(this);
        user_device_insert_sp_transfer = (Spinner) findViewById(R.id.user_device_insert_sp_transfer);
        user_device_insert_tv_transfer = (TextView) findViewById(R.id.user_device_insert_tv_transfer);
        dialog_device_insert_tv_description = (EditText) findViewById(R.id.dialog_device_insert_tv_description);
        current_focus = dialog_device_insert_tv_MAC_1 = (EditText) findViewById(R.id.dialog_device_insert_tv_MAC_1);
        dialog_device_insert_tv_MAC_2 = (EditText) findViewById(R.id.dialog_device_insert_tv_MAC_2);
        dialog_device_insert_tv_MAC_3 = (EditText) findViewById(R.id.dialog_device_insert_tv_MAC_3);
        dialog_device_insert_tv_MAC_4 = (EditText) findViewById(R.id.dialog_device_insert_tv_MAC_4);
        dialog_device_insert_tv_MAC_5 = (EditText) findViewById(R.id.dialog_device_insert_tv_MAC_5);
        dialog_device_insert_tv_MAC_6 = (EditText) findViewById(R.id.dialog_device_insert_tv_MAC_6);
        dialog_device_tv_title = (TextView) findViewById(R.id.dialog_device_tv_title);
        dialog_device_insert_tv_MAC_1.addTextChangedListener(this);
        dialog_device_insert_tv_MAC_2.addTextChangedListener(this);
        dialog_device_insert_tv_MAC_3.addTextChangedListener(this);
        dialog_device_insert_tv_MAC_4.addTextChangedListener(this);
        dialog_device_insert_tv_MAC_5.addTextChangedListener(this);
        dialog_device_insert_tv_MAC_6.addTextChangedListener(this);

        dialog_device_insert_bt_ok = (Button) findViewById(R.id.dialog_device_insert_bt_ok);
        dialog_device_insert_bt_cancel = (Button) findViewById(R.id.dialog_device_insert_bt_cancel);
        dialog_device_insert_bt_ok.setOnClickListener(getSingleClick());
        dialog_device_insert_bt_cancel.setOnClickListener(getSingleClick());
        dialog_device_insert_bt_ok.setEnabled(false);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (device != null) {
            String[] MACs = device.getMAC().split(":");
            dialog_device_insert_tv_MAC_1.setText(MACs[0]);
            dialog_device_insert_tv_MAC_2.setText(MACs[1]);
            dialog_device_insert_tv_MAC_3.setText(MACs[2]);
            dialog_device_insert_tv_MAC_4.setText(MACs[3]);
            dialog_device_insert_tv_MAC_5.setText(MACs[4]);
            dialog_device_insert_tv_MAC_6.setText(MACs[5]);
            dialog_device_insert_tv_description.setText(device.getDescription());
            dialog_device_tv_title.setText(HouseRentalApplication.getContext().getString(R.string.application_device_update_dialog_title));
            ArrayList<UserDAO> data = (ArrayList<UserDAO>) DAOManager.getUsersOfRoom(room);
            user_device_insert_sp_transfer.setAdapter(new UserDeviceTransferAdapter(data));
            user_device_insert_sp_transfer.setOnItemSelectedListener(this);
            for (int i = 0; i < data.size(); ++i) {
                UserDAO userDAO = data.get(i);
                if (userDAO.getId() == device.getUser())
                    user_device_insert_sp_transfer.setSelection(i, false);
            }
        } else {
            user_device_insert_sp_transfer.setVisibility(View.GONE);
            user_device_insert_tv_transfer.setVisibility(View.GONE);
        }
    }


    @Override
    public void onSingleClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_device_insert_bt_ok:
                String MAC = String.format(MAC_FORMAT,
                        dialog_device_insert_tv_MAC_1.getText().toString().trim(),
                        dialog_device_insert_tv_MAC_2.getText().toString().trim(),
                        dialog_device_insert_tv_MAC_3.getText().toString().trim(),
                        dialog_device_insert_tv_MAC_4.getText().toString().trim(),
                        dialog_device_insert_tv_MAC_5.getText().toString().trim(),
                        dialog_device_insert_tv_MAC_6.getText().toString().trim());

                String message = "";

                if (device != null) {
                    // update
                    if (DAOManager.isDeviceExist(MAC) && !MAC.equals(device.getMAC())) {
                        message = BaseApplication.getContext().getString(R.string.user_device_exist);
                    } else {
                        device.setDescription(dialog_device_insert_tv_description.getText().toString().trim());
                        device.setMAC(MAC);
                        device.setUser(user);
                        device.save();
                        message = BaseApplication.getContext().getString(R.string.user_device_update_success);
                        dismiss();
                    }
                } else {
                    // insert
                    if (DAOManager.isDeviceExist(MAC)) {
                        message = BaseApplication.getContext().getString(R.string.user_device_exist);
                    } else {
                        message = BaseApplication.getContext().getString(R.string.user_device_insert_success);
                        DAOManager.addDevice(MAC, dialog_device_insert_tv_description.getText().toString().trim(), user);
                        dismiss();
                    }
                }
                Toast.makeText(BaseApplication.getActiveActivity(), message, Toast.LENGTH_LONG).show();
                break;
            case R.id.dialog_device_insert_bt_cancel:
                dismiss();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().length() >= 2) {
            moveFocusRight();
        }

        dialog_device_insert_bt_ok.setEnabled(dialog_device_insert_tv_MAC_1.getText().toString().length() == 2
                && dialog_device_insert_tv_MAC_2.getText().toString().length() == 2
                && dialog_device_insert_tv_MAC_3.getText().toString().length() == 2
                && dialog_device_insert_tv_MAC_4.getText().toString().length() == 2
                && dialog_device_insert_tv_MAC_5.getText().toString().length() == 2
                && dialog_device_insert_tv_MAC_6.getText().toString().length() == 2);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {

        }
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {

        if (primaryCode == -1) {
            // left
            moveFocusLeft();
        } else if (primaryCode == -2) {
            // right
            moveFocusRight();
        } else {
            if (current_focus != null) {
                if (current_focus.getSelectionEnd() == 2)
                    current_focus.setText("");
                Editable editable = current_focus.getText();
                int start = current_focus.getSelectionStart();
                editable.insert(start, Character.toString((char) primaryCode));
            }
        }


    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }


    private void moveFocusRight() {
        if (dialog_device_insert_tv_MAC_1.equals(current_focus)) {
            dialog_device_insert_tv_MAC_2.requestFocus();
            current_focus = dialog_device_insert_tv_MAC_2;
        } else if (dialog_device_insert_tv_MAC_2.equals(current_focus)) {
            dialog_device_insert_tv_MAC_3.requestFocus();
            current_focus = dialog_device_insert_tv_MAC_3;
        } else if (dialog_device_insert_tv_MAC_3.equals(current_focus)) {
            dialog_device_insert_tv_MAC_4.requestFocus();
            current_focus = dialog_device_insert_tv_MAC_4;
        } else if (dialog_device_insert_tv_MAC_4.equals(current_focus)) {
            dialog_device_insert_tv_MAC_5.requestFocus();
            current_focus = dialog_device_insert_tv_MAC_5;
        } else if (dialog_device_insert_tv_MAC_5.equals(current_focus)) {
            dialog_device_insert_tv_MAC_6.requestFocus();
            current_focus = dialog_device_insert_tv_MAC_6;
        } else if (dialog_device_insert_tv_MAC_6.equals(current_focus)) {
            dialog_device_insert_tv_MAC_1.requestFocus();
            current_focus = dialog_device_insert_tv_MAC_1;
        }
    }

    private void moveFocusLeft() {
        if (dialog_device_insert_tv_MAC_6.equals(current_focus)) {
            dialog_device_insert_tv_MAC_5.requestFocus();
            current_focus = dialog_device_insert_tv_MAC_5;
        } else if (dialog_device_insert_tv_MAC_5.equals(current_focus)) {
            dialog_device_insert_tv_MAC_4.requestFocus();
            current_focus = dialog_device_insert_tv_MAC_4;
        } else if (dialog_device_insert_tv_MAC_4.equals(current_focus)) {
            dialog_device_insert_tv_MAC_3.requestFocus();
            current_focus = dialog_device_insert_tv_MAC_3;
        } else if (dialog_device_insert_tv_MAC_3.equals(current_focus)) {
            dialog_device_insert_tv_MAC_2.requestFocus();
            current_focus = dialog_device_insert_tv_MAC_2;
        } else if (dialog_device_insert_tv_MAC_2.equals(current_focus)) {
            dialog_device_insert_tv_MAC_1.requestFocus();
            current_focus = dialog_device_insert_tv_MAC_1;
        } else if (dialog_device_insert_tv_MAC_1.equals(current_focus)) {
            dialog_device_insert_tv_MAC_6.requestFocus();
            current_focus = dialog_device_insert_tv_MAC_6;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        UserDAO userDAO = (UserDAO) parent.getSelectedItem();
        if (userDAO != null) {
            user = userDAO.getId();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
