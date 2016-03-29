package com.example.houserental.function.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.example.houserental.R;
import com.example.houserental.function.MainActivity;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.OwnerDAO;
import com.example.houserental.function.model.RoomTypeDAO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import core.base.BaseApplication;
import core.base.BaseMultipleFragment;
import core.data.DataSaver;
import core.dialog.GeneralDialog;
import core.util.Constant;
import core.util.Utils;

/**
 * Created by leductuan on 3/6/16.
 */
public class SettingScreen extends BaseMultipleFragment implements DialogInterface.OnDismissListener, OwnerListAdapter.OnDeleteOwnerListener, AdapterView.OnItemClickListener, SettingRoomTypeAdapter.OnDeleteRoomTypeListener, GeneralDialog.DecisionListener {

    // dien: 3000
    // nuoc: 5000
    // rac: 5000/n. Phong duoi 2 nguoi 15000
    // wifi: 10000
    // tien nha: 1600, 1800, 1900, 2000, 2500 / 30 ngay

    public static final String TAG = SettingScreen.class.getSimpleName();
    private EditText fragment_setting_et_water, fragment_setting_et_electric, fragment_setting_et_device, fragment_setting_et_waste;
    private Spinner fragment_setting_sn_owner;
    private ListView fragment_setting_lv_room_type;
    private OwnerListAdapter adapter;
    private SettingRoomTypeAdapter type_adapter;
    private List<OwnerDAO> owners;
    private List<RoomTypeDAO> types;
    private SettingInsertOwnerDialog dialog;
    private SettingInsertRoomTypeDialog type_dialog;
    private int deleted_room_type_position;
    private boolean isExportingDatabase = false;

    public static SettingScreen getInstance() {
        return new SettingScreen();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onBaseCreate() {
        owners = DAOManager.getAllOwners();
        types = DAOManager.getAllRoomTypes();
        types.add(0, null);
        type_adapter = new SettingRoomTypeAdapter(types, this);
        adapter = new OwnerListAdapter(owners, this);
    }

    @Override
    public void onDeepLinking(Intent data) {

    }

    @Override
    public void onNotification(Intent data) {

    }

    @Override
    public void onBindView() {
        fragment_setting_et_water = (EditText) findViewById(R.id.fragment_setting_et_water);
        fragment_setting_et_electric = (EditText) findViewById(R.id.fragment_setting_et_electric);
        fragment_setting_et_device = (EditText) findViewById(R.id.fragment_setting_et_device);
        fragment_setting_et_waste = (EditText) findViewById(R.id.fragment_setting_et_waste);
        fragment_setting_sn_owner = (Spinner) findViewById(R.id.fragment_setting_sn_owner);
        fragment_setting_lv_room_type = (ListView) findViewById(R.id.fragment_setting_lv_room_type);
        fragment_setting_lv_room_type.setOnItemClickListener(this);
        findViewById(R.id.fragment_setting_bt_add_owner);
        findViewById(R.id.fragment_setting_bt_save);
        findViewById(R.id.fragment_setting_bt_backup);
    }

    @Override
    public void onInitializeViewData() {
        try {
            fragment_setting_et_water.setText(DataSaver.getInstance().getInt(DataSaver.Key.WATER_PRICE) + "");
            fragment_setting_et_electric.setText(DataSaver.getInstance().getInt(DataSaver.Key.ELECTRIC_PRICE) + "");
            fragment_setting_et_device.setText(DataSaver.getInstance().getInt(DataSaver.Key.DEVICE_PRICE) + "");
            fragment_setting_et_waste.setText(DataSaver.getInstance().getInt(DataSaver.Key.WASTE_PRICE) + "");
            fragment_setting_sn_owner.setAdapter(adapter);
            String owner = DataSaver.getInstance().getString(DataSaver.Key.OWNER);
            for (int i = 0; i < owners.size(); ++i) {
                if (owners.get(i).getName().equals(owner)) {
                    fragment_setting_sn_owner.setSelection(i);
                    break;
                }
            }
            fragment_setting_lv_room_type.setAdapter(type_adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBaseResume() {
        ((MainActivity) getActiveActivity()).setScreenHeader(getString(R.string.main_header_setting));
    }

    @Override
    public void onBaseFree() {

    }

    @Override
    public void onSingleClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_setting_bt_save:
                if (validated()) {
                    try {
                        DataSaver.getInstance().setInt(DataSaver.Key.ELECTRIC_PRICE, Integer.parseInt(fragment_setting_et_electric.getText().toString().trim()));
                        DataSaver.getInstance().setInt(DataSaver.Key.WATER_PRICE, Integer.parseInt(fragment_setting_et_water.getText().toString().trim()));
                        DataSaver.getInstance().setInt(DataSaver.Key.DEVICE_PRICE, Integer.parseInt(fragment_setting_et_device.getText().toString().trim()));
                        DataSaver.getInstance().setInt(DataSaver.Key.WASTE_PRICE, Integer.parseInt(fragment_setting_et_waste.getText().toString().trim()));
                        if (fragment_setting_sn_owner.getSelectedItem() != null)
                            DataSaver.getInstance().setString(DataSaver.Key.OWNER, ((OwnerDAO) fragment_setting_sn_owner.getSelectedItem()).getName());
                        showAlertDialog(getActiveActivity(), -1, -1, getString(R.string.application_alert_dialog_title),
                                getString(R.string.room_alert_dialog_update_success),
                                getString((R.string.common_ok)), null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.fragment_setting_bt_add_owner:
                dialog = new SettingInsertOwnerDialog(getActiveActivity());
                dialog.setOnDismissListener(this);
                dialog.show();
                break;
            case R.id.fragment_setting_bt_backup:
                if (!isExportingDatabase) {
                    new ExportDatabase().execute();
                }

                break;
        }
    }

    private boolean validated() {
        if (Utils.isEmpty(fragment_setting_et_electric.getText().toString().trim())) {
            showAlertDialog(getActiveActivity(), -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.setting_insert_electric_error), getString(R.string.common_ok), null);
            return false;
        }

        if (Utils.isEmpty(fragment_setting_et_water.getText().toString().trim())) {
            showAlertDialog(getActiveActivity(), -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.setting_insert_water_error), getString(R.string.common_ok), null);
            return false;
        }

        if (Utils.isEmpty(fragment_setting_et_device.getText().toString().trim())) {
            showAlertDialog(getActiveActivity(), -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.setting_insert_device_error), getString(R.string.common_ok), null);
            return false;
        }

        if (Utils.isEmpty(fragment_setting_et_waste.getText().toString().trim())) {
            showAlertDialog(getActiveActivity(), -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.setting_insert_waste_error), getString(R.string.common_ok), null);
            return false;
        }
        return true;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (dialog.equals(this.dialog)) {
            if (owners != null)
                owners.clear();
            owners.addAll(DAOManager.getAllOwners());
            adapter.notifyDataSetChanged();
            fragment_setting_sn_owner.setSelection(adapter.getCount() - 1);
        } else if (dialog.equals(this.type_dialog)) {
            if (types != null)
                types.clear();
            types.addAll(DAOManager.getAllRoomTypes());
            types.add(0, null);
            type_adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDeleteOwner(int position) {
        DAOManager.deleteOwner(adapter.getItem(position).getId());
        owners.remove(position);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            type_dialog = new SettingInsertRoomTypeDialog(BaseApplication.getActiveActivity());
            type_dialog.setOnDismissListener(this);
            type_dialog.show();
        }
    }

    @Override
    public void onDeleteRoomType(int position) {
        deleted_room_type_position = position;
        showDecisionDialog(getActiveActivity(), Constant.DELETE_ROOM_TYPE_DIALOG, -1, getString(R.string.application_alert_dialog_title), getString(R.string.setting_room_type_delete_alert), getString(R.string.common_ok), getString(R.string.common_cancel), null, this);
    }

    @Override
    public void onAgreed(int id) {
        if (id == Constant.DELETE_ROOM_TYPE_DIALOG) {
            DAOManager.deleteRoomType(type_adapter.getItem(deleted_room_type_position).getId());
            types.remove(deleted_room_type_position);
            type_adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDisAgreed(int id) {

    }

    @Override
    public void onNeutral(int id) {

    }

    private class ExportDatabase extends AsyncTask<Void, Void, Boolean> {
        private SimpleDateFormat formatter;

        public ExportDatabase() {
            super();
            formatter = new SimpleDateFormat("dd-MMM-yyyy_HH_mm_ss");
        }

        @Override
        protected void onPreExecute() {
            isExportingDatabase = true;
            super.onPreExecute();
            showLoadingDialog(getActiveActivity());
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            closeLoadingDialog();
            if (result) {
                Toast.makeText(getActiveActivity(), getString(R.string.application_alert_dialog_backup_success), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActiveActivity(), getString(R.string.application_alert_dialog_error_general), Toast.LENGTH_SHORT).show();
            }
            isExportingDatabase = false;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                String fromPath = ActiveAndroid.getDatabase().getPath();
                String toPath = Environment.getExternalStorageDirectory()
                        .getPath()
                        + "/"
                        + BaseApplication.getContext().getPackageName()
                        .replace(".", "_") + "/houserental_" + formatter.format(new Date()) + ".db";
                File fromFile = new File(fromPath);
                File toFile = new File(toPath);
                copy(fromFile, toFile);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        private void copy(File src, File dst) throws IOException {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dst);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }
}
