package com.example.houserental.function.setting;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.houserental.function.model.DAOManager;

import core.base.BaseApplication;
import core.base.BaseDialog;
import core.util.SingleClick;

/**
 * Created by Tyrael on 3/16/16.
 */
public class SettingInsertOwnerDialog extends BaseDialog implements SingleClick.SingleClickListener, TextWatcher {
    private EditText dialog_owner_insert_et_name;
    private Button dialog_owner_insert_bt_ok;

    public SettingInsertOwnerDialog(Context context) {
        super(context);
    }

    public SettingInsertOwnerDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.houserental.R.layout.dialog_owner_insert);
    }

    @Override
    protected void onBaseCreate() {
        getSingleClick().setListener(this);
    }

    @Override
    protected void onBindView() {
        dialog_owner_insert_et_name = (EditText) findViewById(com.example.houserental.R.id.dialog_owner_insert_et_name);
        dialog_owner_insert_et_name.addTextChangedListener(this);
        dialog_owner_insert_bt_ok = (Button) findViewById(com.example.houserental.R.id.dialog_owner_insert_bt_ok);
        findViewById(com.example.houserental.R.id.dialog_owner_insert_bt_cancel);
    }

    @Override
    public void onSingleClick(View v) {
        switch (v.getId()) {
            case com.example.houserental.R.id.dialog_owner_insert_bt_ok:
                DAOManager.addOwner(dialog_owner_insert_et_name.getText().toString().trim());
                Toast.makeText(BaseApplication.getActiveActivity(), BaseApplication.getContext().getString(com.example.houserental.R.string.setting_owner_insert_success), Toast.LENGTH_SHORT).show();
            case com.example.houserental.R.id.dialog_owner_insert_bt_cancel:
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
        dialog_owner_insert_bt_ok.setEnabled(s.length() > 0);
    }
}
