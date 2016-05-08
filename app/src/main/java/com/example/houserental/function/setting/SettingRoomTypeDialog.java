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
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.RoomTypeDAO;

import core.base.BaseApplication;
import core.base.BaseDialog;
import core.util.SingleClick;

/**
 * Created by Tyrael on 3/18/16.
 */
public class SettingRoomTypeDialog extends BaseDialog implements SingleClick.SingleClickListener, TextWatcher {

    private EditText dialog_room_type_insert_et_name, dialog_room_type_insert_et_price;
    private TextView dialog_room_type_insert_tv_title;
    private Button dialog_room_type_insert_bt_ok;
    private RoomTypeDAO type;

    public SettingRoomTypeDialog(Context context, RoomTypeDAO type) {
        super(context);
        this.type = type;
        setContentView(R.layout.dialog_room_type_insert);
    }

    @Override
    protected void onBaseCreate() {
        getSingleClick().setListener(this);
    }

    @Override
    protected void onBindView() {
        dialog_room_type_insert_tv_title = (TextView) findViewById(R.id.dialog_room_type_insert_tv_title);
        dialog_room_type_insert_et_name = (EditText) findViewById(R.id.dialog_room_type_insert_et_name);
        dialog_room_type_insert_et_name.addTextChangedListener(this);
        dialog_room_type_insert_et_price = (EditText) findViewById(R.id.dialog_room_type_insert_et_price);
        dialog_room_type_insert_et_price.addTextChangedListener(this);
        dialog_room_type_insert_bt_ok = (Button) findViewById(R.id.dialog_room_type_insert_bt_ok);
        findViewById(R.id.dialog_room_type_insert_bt_cancel);

        if (type != null) {
            dialog_room_type_insert_et_name.setText(type.getName());
            dialog_room_type_insert_et_price.setText(type.getPrice() + "");
            dialog_room_type_insert_tv_title.setText(HouseRentalApplication.getContext().getString(R.string.application_room_type_update_dialog_title));
        }
    }

    @Override
    public void onSingleClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_room_type_insert_bt_ok:
                if (type != null) {
                    type.setName(dialog_room_type_insert_et_name.getText().toString().trim());
                    type.setPrice(Integer.parseInt(dialog_room_type_insert_et_price.getText().toString().trim()));
                    type.save();
                    Toast.makeText(BaseApplication.getActiveActivity(), BaseApplication.getContext().getString(R.string.setting_room_type_update_success), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BaseApplication.getActiveActivity(), BaseApplication.getContext().getString(R.string.setting_room_type_insert_success), Toast.LENGTH_SHORT).show();
                    DAOManager.addRoomType(dialog_room_type_insert_et_name.getText().toString().trim(), Integer.parseInt(dialog_room_type_insert_et_price.getText().toString().trim()));
                }
            case R.id.dialog_room_type_insert_bt_cancel:
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
        dialog_room_type_insert_bt_ok.setEnabled(dialog_room_type_insert_et_name.getText().toString().trim().length() > 0 && dialog_room_type_insert_et_price.getText().toString().trim().length() > 0);
    }
}
