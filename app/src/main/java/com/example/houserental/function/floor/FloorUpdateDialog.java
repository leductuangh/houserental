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
import com.example.houserental.function.model.FloorDAO;

import core.base.BaseApplication;
import core.base.BaseDialog;
import core.util.SingleClick;

/**
 * Created by Tyrael on 5/2/16.
 */
public class FloorUpdateDialog extends BaseDialog implements SingleClick.SingleClickListener, TextWatcher {

    private Button dialog_floor_update_bt_ok;
    private EditText dialog_floor_update_et_name;
    private FloorDAO floor;

    public FloorUpdateDialog(Context context, FloorDAO floor) {
        super(context);
        setContentView(R.layout.dialog_floor_update);
        this.floor = floor;
    }

    @Override
    protected void onBaseCreate() {
        getSingleClick().setListener(this);
    }

    @Override
    protected void onBindView() {
        dialog_floor_update_et_name = (EditText) findViewById(R.id.dialog_floor_update_et_name);
        dialog_floor_update_et_name.addTextChangedListener(this);
        dialog_floor_update_bt_ok = (Button) findViewById(R.id.dialog_floor_update_bt_ok);
        findViewById(R.id.dialog_floor_update_bt_cancel);
    }

    @Override
    public void onSingleClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_floor_update_bt_ok:
                DAOManager.updateFloor(floor.getId(), dialog_floor_update_et_name.getText().toString().trim(), floor.getFloorIndex());
                String success = String.format(BaseApplication.getContext().getString(R.string.floor_update_success), floor.getName());
                Toast.makeText(BaseApplication.getActiveActivity(), success, Toast.LENGTH_SHORT).show();
            case R.id.dialog_floor_update_bt_cancel:
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
        dialog_floor_update_bt_ok.setEnabled(s.length() > 0);
    }
}
