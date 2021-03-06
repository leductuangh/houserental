package com.example.houserental.function.setting;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import com.example.houserental.function.model.OwnerDAO;
import com.example.houserental.function.view.SignatureView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import core.base.BaseApplication;
import core.base.BaseDialog;
import core.util.SingleClick;

/**
 * Created by Tyrael on 3/16/16.
 */
public class SettingOwnerDialog extends BaseDialog implements SingleClick.SingleClickListener, TextWatcher {
    private EditText dialog_owner_insert_et_name;
    private Button dialog_owner_insert_bt_ok;
    private OwnerDAO owner;
    private TextView dialog_owner_insert_tv_title;
    private SignatureView dialog_owner_insert_sv_signature;

    public SettingOwnerDialog(Context context, OwnerDAO owner) {
        super(context);
        this.owner = owner;
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
        dialog_owner_insert_sv_signature = (SignatureView) findViewById(R.id.dialog_owner_insert_sv_signature);
        dialog_owner_insert_sv_signature.setDrawingCacheEnabled(true);
        findViewById(R.id.dialog_owner_insert_im_clear);
        dialog_owner_insert_tv_title = (TextView) findViewById(R.id.dialog_owner_insert_tv_title);
        dialog_owner_insert_et_name = (EditText) findViewById(com.example.houserental.R.id.dialog_owner_insert_et_name);
        dialog_owner_insert_et_name.addTextChangedListener(this);
        dialog_owner_insert_bt_ok = (Button) findViewById(com.example.houserental.R.id.dialog_owner_insert_bt_ok);
        findViewById(com.example.houserental.R.id.dialog_owner_insert_bt_cancel);

        if (owner != null) {
            dialog_owner_insert_et_name.setText(owner.getName());
            dialog_owner_insert_tv_title.setText(HouseRentalApplication.getContext().getString(R.string.application_owner_update_dialog_title));
            byte[] signBlob = owner.getSignature();
            if (signBlob != null) {
                Bitmap sign = BitmapFactory.decodeByteArray(signBlob, 0, signBlob.length);
                dialog_owner_insert_sv_signature.initView(sign);
            }
        }
    }

    @Override
    public void onSingleClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_owner_insert_im_clear:
                dialog_owner_insert_sv_signature.clearSignature();
                break;
            case com.example.houserental.R.id.dialog_owner_insert_bt_ok:
                Bitmap sign = dialog_owner_insert_sv_signature.getDrawingCache();
                byte[] signBlob = null;
                if (sign != null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    sign.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    try {
                        stream.flush();
                        signBlob = stream.toByteArray();
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (owner != null) {
                    // update
                    owner.setName(dialog_owner_insert_et_name.getText().toString().trim());
                    owner.setSignature(signBlob);
                    owner.save();
                    Toast.makeText(BaseApplication.getActiveActivity(), BaseApplication.getContext().getString(com.example.houserental.R.string.setting_owner_update_success), Toast.LENGTH_SHORT).show();
                } else {
                    // insert
                    DAOManager.addOwner(dialog_owner_insert_et_name.getText().toString().trim(), signBlob);
                    Toast.makeText(BaseApplication.getActiveActivity(), BaseApplication.getContext().getString(com.example.houserental.R.string.setting_owner_insert_success), Toast.LENGTH_SHORT).show();
                }
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
