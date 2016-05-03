package com.example.houserental.function.floor;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.houserental.R;
import com.example.houserental.function.model.DAOManager;

import core.base.BaseApplication;
import core.base.BaseDialog;
import core.util.SingleClick;

/**
 * Created by Tyrael on 5/2/16.
 */
public class FloorInsertDialog extends BaseDialog implements SingleClick.SingleClickListener, TextWatcher {

    private EditText dialog_floor_insert_et_name;
    private Button dialog_floor_insert_bt_ok;

    public FloorInsertDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_floor_insert);
    }

    @Override
    protected void onBaseCreate() {
        getSingleClick().setListener(this);
    }

    @Override
    protected void onBindView() {
        dialog_floor_insert_et_name = (EditText) findViewById(R.id.dialog_floor_insert_et_name);
        dialog_floor_insert_bt_ok = (Button) findViewById(R.id.dialog_floor_insert_bt_ok);
        findViewById(R.id.dialog_floor_insert_bt_cancel);
        dialog_floor_insert_et_name.addTextChangedListener(this);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        dialog_floor_insert_bt_ok.setEnabled(s.length() > 0);
    }

    @Override
    public void onSingleClick(View v) {
        switch (v.getId()) {
            case com.example.houserental.R.id.dialog_floor_insert_bt_ok:
                DAOManager.addFloor(dialog_floor_insert_et_name.getText().toString().trim(), DAOManager.getNextFloorIndex());
                String success = String.format(BaseApplication.getContext().getString(R.string.floor_insert_success), dialog_floor_insert_et_name.getText().toString().trim());
                Toast.makeText(BaseApplication.getActiveActivity(), success, Toast.LENGTH_SHORT).show();
            case com.example.houserental.R.id.dialog_floor_insert_bt_cancel:
                dismiss();
                break;
        }
    }
}
