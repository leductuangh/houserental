package com.example.houserental.function.user;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.core.core.base.BaseApplication;
import com.core.core.base.BaseDialog;
import com.core.util.SingleClick;
import com.example.houserental.R;
import com.example.houserental.function.model.DAOManager;

/**
 * Created by leductuan on 3/12/16.
 */
public class UserDetailInsertDeviceDialog extends BaseDialog implements SingleClick.SingleClickListener, TextWatcher, View.OnFocusChangeListener, KeyboardView.OnKeyboardActionListener {

    private static final String MAC_FORMAT = "%s:%s:%s:%s:%s:%s:%s:%s:%s:%s";
    private EditText dialog_device_insert_tv_MAC_1, dialog_device_insert_tv_MAC_2, dialog_device_insert_tv_MAC_3, dialog_device_insert_tv_MAC_4, dialog_device_insert_tv_MAC_5, dialog_device_insert_tv_MAC_6, dialog_device_insert_tv_MAC_7, dialog_device_insert_tv_MAC_8, dialog_device_insert_tv_MAC_9, dialog_device_insert_tv_MAC_10;
    private EditText current_focus;
    private Button dialog_device_insert_bt_ok, dialog_device_insert_bt_cancel;
    private KeyboardView dialog_device_kb_mac;
    private Keyboard keyboard;
    private String user;

    public UserDetailInsertDeviceDialog(Context context, String user) {
        super(context);
        this.user = user;
    }

    public UserDetailInsertDeviceDialog(Context context, int theme, String user) {
        super(context, theme);
        this.user = user;
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
        current_focus = dialog_device_insert_tv_MAC_1 = (EditText) findViewById(R.id.dialog_device_insert_tv_MAC_1);
        dialog_device_insert_tv_MAC_2 = (EditText) findViewById(R.id.dialog_device_insert_tv_MAC_2);
        dialog_device_insert_tv_MAC_3 = (EditText) findViewById(R.id.dialog_device_insert_tv_MAC_3);
        dialog_device_insert_tv_MAC_4 = (EditText) findViewById(R.id.dialog_device_insert_tv_MAC_4);
        dialog_device_insert_tv_MAC_5 = (EditText) findViewById(R.id.dialog_device_insert_tv_MAC_5);
        dialog_device_insert_tv_MAC_6 = (EditText) findViewById(R.id.dialog_device_insert_tv_MAC_6);
        dialog_device_insert_tv_MAC_7 = (EditText) findViewById(R.id.dialog_device_insert_tv_MAC_7);
        dialog_device_insert_tv_MAC_8 = (EditText) findViewById(R.id.dialog_device_insert_tv_MAC_8);
        dialog_device_insert_tv_MAC_9 = (EditText) findViewById(R.id.dialog_device_insert_tv_MAC_9);
        dialog_device_insert_tv_MAC_10 = (EditText) findViewById(R.id.dialog_device_insert_tv_MAC_10);
        dialog_device_insert_tv_MAC_1.addTextChangedListener(this);
        dialog_device_insert_tv_MAC_2.addTextChangedListener(this);
        dialog_device_insert_tv_MAC_3.addTextChangedListener(this);
        dialog_device_insert_tv_MAC_4.addTextChangedListener(this);
        dialog_device_insert_tv_MAC_5.addTextChangedListener(this);
        dialog_device_insert_tv_MAC_6.addTextChangedListener(this);
        dialog_device_insert_tv_MAC_7.addTextChangedListener(this);
        dialog_device_insert_tv_MAC_8.addTextChangedListener(this);
        dialog_device_insert_tv_MAC_9.addTextChangedListener(this);
        dialog_device_insert_tv_MAC_10.addTextChangedListener(this);


        dialog_device_insert_bt_ok = (Button) findViewById(R.id.dialog_device_insert_bt_ok);
        dialog_device_insert_bt_cancel = (Button) findViewById(R.id.dialog_device_insert_bt_cancel);
        dialog_device_insert_bt_ok.setOnClickListener(getSingleClick());
        dialog_device_insert_bt_cancel.setOnClickListener(getSingleClick());
        dialog_device_insert_bt_ok.setEnabled(false);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
                        dialog_device_insert_tv_MAC_6.getText().toString().trim(),
                        dialog_device_insert_tv_MAC_7.getText().toString().trim(),
                        dialog_device_insert_tv_MAC_8.getText().toString().trim(),
                        dialog_device_insert_tv_MAC_9.getText().toString().trim(),
                        dialog_device_insert_tv_MAC_10.getText().toString().trim());
                String message = "";
                if (DAOManager.isDeviceExist(MAC)) {
                    message = BaseApplication.getContext().getString(R.string.user_device_exist);
                } else {
                    message = BaseApplication.getContext().getString(R.string.user_device_insert_success);
                    DAOManager.addDevice(MAC, user);
                    dismiss();
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
                && dialog_device_insert_tv_MAC_6.getText().toString().length() == 2
                && dialog_device_insert_tv_MAC_7.getText().toString().length() == 2
                && dialog_device_insert_tv_MAC_8.getText().toString().length() == 2
                && dialog_device_insert_tv_MAC_9.getText().toString().length() == 2
                && dialog_device_insert_tv_MAC_10.getText().toString().length() == 2);
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
            dialog_device_insert_tv_MAC_7.requestFocus();
            current_focus = dialog_device_insert_tv_MAC_7;
        } else if (dialog_device_insert_tv_MAC_7.equals(current_focus)) {
            dialog_device_insert_tv_MAC_8.requestFocus();
            current_focus = dialog_device_insert_tv_MAC_8;
        } else if (dialog_device_insert_tv_MAC_8.equals(current_focus)) {
            dialog_device_insert_tv_MAC_9.requestFocus();
            current_focus = dialog_device_insert_tv_MAC_9;
        } else if (dialog_device_insert_tv_MAC_9.equals(current_focus)) {
            dialog_device_insert_tv_MAC_10.requestFocus();
            current_focus = dialog_device_insert_tv_MAC_10;
        } else if (dialog_device_insert_tv_MAC_10.equals(current_focus)) {
            dialog_device_insert_tv_MAC_1.requestFocus();
            current_focus = dialog_device_insert_tv_MAC_1;
        }
    }

    private void moveFocusLeft() {
        if (dialog_device_insert_tv_MAC_10.equals(current_focus)) {
            dialog_device_insert_tv_MAC_9.requestFocus();
            current_focus = dialog_device_insert_tv_MAC_9;
        } else if (dialog_device_insert_tv_MAC_9.equals(current_focus)) {
            dialog_device_insert_tv_MAC_8.requestFocus();
            current_focus = dialog_device_insert_tv_MAC_8;
        } else if (dialog_device_insert_tv_MAC_8.equals(current_focus)) {
            dialog_device_insert_tv_MAC_7.requestFocus();
            current_focus = dialog_device_insert_tv_MAC_7;
        } else if (dialog_device_insert_tv_MAC_7.equals(current_focus)) {
            dialog_device_insert_tv_MAC_6.requestFocus();
            current_focus = dialog_device_insert_tv_MAC_6;
        } else if (dialog_device_insert_tv_MAC_6.equals(current_focus)) {
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
            dialog_device_insert_tv_MAC_10.requestFocus();
            current_focus = dialog_device_insert_tv_MAC_10;
        }
    }
}
