package com.example.houserental.function.setting;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.houserental.R;
import com.example.houserental.function.HouseRentalApplication;
import com.example.houserental.function.model.WifiHost;

import java.util.ArrayList;
import java.util.List;

import core.base.BaseApplication;
import core.base.BaseDialog;
import core.util.SingleClick;

/**
 * Created by Tyrael on 3/18/16.
 */
public class SettingWifiDialog extends BaseDialog implements SingleClick.SingleClickListener, TextWatcher {

    private EditText dialog_wifi_host_insert_et_PIN, dialog_wifi_host_insert_et_name, dialog_wifi_host_insert_et_username, dialog_wifi_host_insert_et_password, dialog_wifi_host_insert_et_MAC;
    private TextView dialog_wifi_host_insert_tv_title;
    private Button dialog_wifi_host_insert_bt_ok;
    private WifiHost wifi;
    private List<WifiHost> data;

    public SettingWifiDialog(Context context, List<WifiHost> data, int position) {
        super(context);
        this.data = data;
        if (position > 0)
            this.wifi = data.get(position);
        setContentView(R.layout.dialog_wifi_host_insert);
    }

    @Override
    protected void onBaseCreate() {
        getSingleClick().setListener(this);
    }

    @Override
    protected void onBindView() {

        dialog_wifi_host_insert_et_name = (EditText) findViewById(R.id.dialog_wifi_host_insert_et_name);
        dialog_wifi_host_insert_et_name.addTextChangedListener(this);

        dialog_wifi_host_insert_et_username = (EditText) findViewById(R.id.dialog_wifi_host_insert_et_username);
        dialog_wifi_host_insert_et_username.addTextChangedListener(this);

        dialog_wifi_host_insert_et_password = (EditText) findViewById(R.id.dialog_wifi_host_insert_et_password);
        dialog_wifi_host_insert_et_password.addTextChangedListener(this);

        dialog_wifi_host_insert_et_MAC = (EditText) findViewById(R.id.dialog_wifi_host_insert_et_MAC);
        dialog_wifi_host_insert_et_MAC.addTextChangedListener(this);

        dialog_wifi_host_insert_et_PIN = (EditText) findViewById(R.id.dialog_wifi_host_insert_et_PIN);
        dialog_wifi_host_insert_et_PIN.addTextChangedListener(this);

        dialog_wifi_host_insert_tv_title = (TextView) findViewById(R.id.dialog_wifi_host_insert_tv_title);


        dialog_wifi_host_insert_bt_ok = (Button) findViewById(R.id.dialog_wifi_host_insert_bt_ok);
        findViewById(R.id.dialog_wifi_host_insert_bt_cancel);

        if (wifi != null) {
            dialog_wifi_host_insert_et_name.setText(wifi.getHost());
            dialog_wifi_host_insert_et_username.setText(wifi.getUsername());
            dialog_wifi_host_insert_et_password.setText(wifi.getPassword());
            dialog_wifi_host_insert_et_MAC.setText(wifi.getMAC());
            dialog_wifi_host_insert_et_PIN.setText(wifi.getPIN());
            dialog_wifi_host_insert_tv_title.setText(HouseRentalApplication.getContext().getString(R.string.setting_wifi_host_update_dialog_title));
        }
    }

    @Override
    public void onSingleClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_wifi_host_insert_bt_ok:
                if (wifi != null) {
                    wifi.setHost(dialog_wifi_host_insert_et_name.getText().toString().trim());
                    wifi.setUsername(dialog_wifi_host_insert_et_username.getText().toString().toLowerCase().trim());
                    wifi.setPassword(dialog_wifi_host_insert_et_password.getText().toString().trim());
                    wifi.setMAC(dialog_wifi_host_insert_et_MAC.getText().toString().trim());
                    wifi.setPIN(dialog_wifi_host_insert_et_PIN.getText().toString().trim());
                    Toast.makeText(BaseApplication.getActiveActivity(), BaseApplication.getContext().getString(R.string.setting_room_type_update_success), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BaseApplication.getActiveActivity(), BaseApplication.getContext().getString(R.string.setting_room_type_insert_success), Toast.LENGTH_SHORT).show();
                    data.add(new WifiHost(dialog_wifi_host_insert_et_name.getText().toString().trim(),
                            dialog_wifi_host_insert_et_username.getText().toString().toLowerCase().trim(),
                            dialog_wifi_host_insert_et_password.getText().toString().trim(),
                            dialog_wifi_host_insert_et_MAC.getText().toString().trim(),
                            dialog_wifi_host_insert_et_PIN.getText().toString().trim(),
                            false));
                    WifiHost.save((ArrayList<WifiHost>) data);
//                    DAOManager.addRoomType(dialog_room_type_insert_et_name.getText().toString().trim(), Integer.parseInt(dialog_room_type_insert_et_price.getText().toString().trim()));
                }
            case R.id.dialog_wifi_host_insert_bt_cancel:
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
        dialog_wifi_host_insert_bt_ok.setEnabled(dialog_wifi_host_insert_et_name.getText().toString().trim().length() > 0
                && dialog_wifi_host_insert_et_name.getText().toString().trim().length() > 0
                && dialog_wifi_host_insert_et_username.getText().toString().trim().length() > 0
                && dialog_wifi_host_insert_et_password.getText().toString().trim().length() > 0);
    }
}
