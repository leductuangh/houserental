package com.example.houserental.function.setting;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.example.houserental.R;
import com.example.houserental.function.HouseRentalUtils;
import com.example.houserental.function.MainActivity;
import com.example.houserental.function.RebootReceiver;
import com.example.houserental.function.ReminderReceiver;
import com.example.houserental.function.home.HomeScreen;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.FloorDAO;
import com.example.houserental.function.model.OwnerDAO;
import com.example.houserental.function.model.RoomTypeDAO;
import com.example.houserental.function.model.SettingDAO;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataChangeSet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import core.base.BaseMultipleFragment;
import core.data.DataSaver;
import core.dialog.GeneralDialog;
import core.util.Constant;
import core.util.DLog;
import core.util.Utils;

public class SettingScreen extends BaseMultipleFragment implements GeneralDialog.DecisionListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GeneralDialog.ConfirmListener, CompoundButton.OnCheckedChangeListener {

    // dien: 3000
    // nuoc: 5000
    // rac: 5000/n. Phong duoi 2 nguoi 15000
    // wifi: 15000
    // tien nha: 1600, 1800, 1900, 2000, 2500 / 30 ngay

    public static final String TAG = SettingScreen.class.getSimpleName();
    private static final int RESOLVE_CONNECTION_REQUEST_CODE = 999;
    private EditText fragment_setting_et_deposit, fragment_setting_et_water, fragment_setting_et_electric, fragment_setting_et_device, fragment_setting_et_waste;
    private TextView fragment_setting_tv_selected_owner, fragment_setting_tv_selected_room_type;
    private boolean isExportingDatabase = false;
    private boolean isImportingDatabase = false;
    private GoogleApiClient mGoogleApiClient;
    private Switch fragment_setting_sw_notification, fragment_setting_sw_sms;
    private SettingDAO setting;

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
//        initGoogleDriveAPIClient();
//        initDB();
        setting = DAOManager.getSetting();
        if (setting == null)
            setting = new SettingDAO(0, 0, 0, 0, 0, null, false);
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
        fragment_setting_et_deposit = (EditText) findViewById(R.id.fragment_setting_et_deposit);
        fragment_setting_et_water = (EditText) findViewById(R.id.fragment_setting_et_water);
        fragment_setting_et_electric = (EditText) findViewById(R.id.fragment_setting_et_electric);
        fragment_setting_et_device = (EditText) findViewById(R.id.fragment_setting_et_device);
        fragment_setting_et_waste = (EditText) findViewById(R.id.fragment_setting_et_waste);
        fragment_setting_tv_selected_owner = (TextView) findViewById(R.id.fragment_setting_tv_selected_owner);
        fragment_setting_tv_selected_room_type = (TextView) findViewById(R.id.fragment_setting_tv_selected_room_type);
        fragment_setting_sw_notification = (Switch) findViewById(R.id.fragment_setting_sw_notification);
        fragment_setting_sw_sms = (Switch) findViewById(R.id.fragment_setting_sw_sms);
        fragment_setting_sw_sms.setOnCheckedChangeListener(this);
        fragment_setting_sw_notification.setOnCheckedChangeListener(this);
        findViewById(R.id.fragment_setting_bt_save);
        View fragment_setting_bt_backup = findViewById(R.id.fragment_setting_bt_backup);
        registerSingleAction(R.id.fragment_setting_bt_restore,
                R.id.fragment_setting_bt_backup,
                R.id.fragment_setting_bt_save,
                R.id.fragment_setting_im_selected_room_type,
                R.id.fragment_setting_im_selected_owner);

        try {
            if (!DataSaver.getInstance().isEnabled(DataSaver.Key.INITIALIZED)) {
                fragment_setting_bt_backup.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInitializeViewData() {
        try {
            fragment_setting_et_deposit.setText(String.valueOf(setting.getDeposit()));
            fragment_setting_et_water.setText(String.valueOf(setting.getWaterPrice()));
            fragment_setting_et_electric.setText(String.valueOf(setting.getElectriPrice()));
            fragment_setting_et_device.setText(String.valueOf(setting.getDevicePrice()));
            fragment_setting_et_waste.setText(String.valueOf(setting.getWastePrice()));
            fragment_setting_sw_notification.setChecked(setting.isNotification());
            fragment_setting_sw_sms.setChecked(setting.isSms());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void revokeReminderService() {
        AlarmManager manager = (AlarmManager) getActiveActivity().getSystemService(Context.ALARM_SERVICE);
        ComponentName rebootReceiver = new ComponentName(getActiveActivity(), RebootReceiver.class);
        ComponentName alarmReceiver = new ComponentName(getActiveActivity(), ReminderReceiver.class);
        PackageManager pm = getActiveActivity().getPackageManager();
        Calendar afternoonSection = Calendar.getInstance();
        afternoonSection.setTimeInMillis(System.currentTimeMillis());
        afternoonSection.set(Calendar.HOUR_OF_DAY, 17);

        Calendar morningSection = Calendar.getInstance();
        morningSection.setTimeInMillis(System.currentTimeMillis());
        morningSection.set(Calendar.HOUR_OF_DAY, 9);

        Intent intent = new Intent(getActiveActivity(), ReminderReceiver.class);
        intent.setAction(Constant.REMINDER_ACTION);
        PendingIntent pIn = PendingIntent.getBroadcast(getActiveActivity(), 999, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (setting != null && setting.isNotification()) {
            pm.setComponentEnabledSetting(rebootReceiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
            pm.setComponentEnabledSetting(alarmReceiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, afternoonSection.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pIn);

            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, morningSection.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pIn);
        } else {
            manager.cancel(pIn);
            pIn.cancel();
            pm.setComponentEnabledSetting(rebootReceiver,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
            pm.setComponentEnabledSetting(alarmReceiver,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
        }
    }

    @Override
    public void onBaseResume() {
        ((MainActivity) getActiveActivity()).setScreenHeader(getString(R.string.main_header_setting));
        refreshOwner();
        refreshRoomType();
        connectGoogleApiClient();
    }

    @Override
    public void onBaseFree() {
        disconnectGoogleApiClient();
    }

    @Override
    public void onSingleClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_setting_im_selected_owner:
                addFragment(R.id.activity_main_container, SettingOwnerListScreen.getInstance(setting), SettingOwnerListScreen.TAG);
                break;
            case R.id.fragment_setting_im_selected_room_type:
                addFragment(R.id.activity_main_container, SettingRoomTypeListScreen.getInstance(), SettingRoomTypeListScreen.TAG);
                break;
            case R.id.fragment_setting_bt_save:
                if (validated()) {
                    try {
                        setting.setDeposit(Integer.parseInt(fragment_setting_et_deposit.getText().toString().trim()));
                        setting.setElectricPrice(Integer.parseInt(fragment_setting_et_electric.getText().toString().trim()));
                        setting.setWaterPrice(Integer.parseInt(fragment_setting_et_water.getText().toString().trim()));
                        setting.setWastePrice(Integer.parseInt(fragment_setting_et_waste.getText().toString().trim()));
                        setting.setDevicePrice(Integer.parseInt(fragment_setting_et_device.getText().toString().trim()));
                        setting.save();
                        revokeReminderService();
                        showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title),
                                getString(R.string.room_alert_dialog_update_success),
                                getString((R.string.common_ok)), null, this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.fragment_setting_bt_backup:
                if (!isExportingDatabase) {
                    new ExportDatabase().execute();
                }
                break;
            case R.id.fragment_setting_bt_restore:
                if (!isImportingDatabase) {
                    showDecisionDialog(getActiveActivity(),
                            Constant.RESTORE_DATABASE_DIALOG, getGeneralDialogLayoutResource(),
                            -1,
                            getString(R.string.application_alert_dialog_title),
                            getString(R.string.application_alert_dialog_restore_confirm),
                            getString(R.string.common_ok),
                            getString(R.string.common_cancel),
                            null, null, this);
                }
                break;
        }
    }

    private void initGoogleDriveAPIClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActiveActivity())
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    private boolean validated() {
        try {
            Long owner = setting.getOwner();

            if (owner == null) {
                showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.setting_owner_select_error), getString(R.string.common_ok), null, null);
                return false;
            }

            if (DAOManager.getRoomTypeCount() <= 0) {
                showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.setting_room_type_select_error), getString(R.string.common_ok), null, null);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if (Utils.isEmpty(fragment_setting_et_electric.getText().toString().trim())) {
            showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.setting_insert_electric_error), getString(R.string.common_ok), null, null);
            return false;
        }

        if (Utils.isEmpty(fragment_setting_et_water.getText().toString().trim())) {
            showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.setting_insert_water_error), getString(R.string.common_ok), null, null);
            return false;
        }

        if (Utils.isEmpty(fragment_setting_et_device.getText().toString().trim())) {
            showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.setting_insert_device_error), getString(R.string.common_ok), null, null);
            return false;
        }

        if (Utils.isEmpty(fragment_setting_et_waste.getText().toString().trim())) {
            showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.setting_insert_waste_error), getString(R.string.common_ok), null, null);
            return false;
        }

        if (Utils.isEmpty(fragment_setting_et_deposit.getText().toString().trim())) {
            showAlertDialog(getActiveActivity(), -1, -1, -1, getString(R.string.application_alert_dialog_title), getString(R.string.setting_insert_deposit_error), getString(R.string.common_ok), null, null);
            return false;
        }
        return true;
    }

    @Override
    public void onAgreed(int id, Object onWhat) {
        if (id == Constant.RESTORE_DATABASE_DIALOG) {
            new ImportDatabase().execute();
        }
    }

    @Override
    public void onDisAgreed(int id, Object onWhat) {

    }

    @Override
    public void onNeutral(int id, Object onWhat) {

    }

    @Override
    public void onConnected(Bundle bundle) {
        DLog.d(TAG, "Google Api Client is connected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        DLog.d(TAG, "Google Api Client is suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(getActiveActivity(), RESOLVE_CONNECTION_REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                // Unable to resolve, message user appropriately
            }
        } else {
            GoogleApiAvailability.getInstance().getErrorDialog(getActiveActivity(), connectionResult.getErrorCode(), 0).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESOLVE_CONNECTION_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    connectGoogleApiClient();
                }
                break;
        }
    }

    private void connectGoogleApiClient() {
        if (mGoogleApiClient != null) {
            if (!(mGoogleApiClient.isConnecting() || mGoogleApiClient.isConnected())) {
                mGoogleApiClient.connect(GoogleApiClient.SIGN_IN_MODE_REQUIRED);
            }
        }
    }

    private void disconnectGoogleApiClient() {
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnecting() || mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }
        }
    }

    public void uploadFileToDrive(final File file) {
        Drive.DriveApi.newDriveContents(mGoogleApiClient).setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
            @Override
            public void onResult(DriveApi.DriveContentsResult driveContentsResult) {
                if (!driveContentsResult.getStatus().isSuccess()) {
                    DLog.i(TAG, "Failed to create new contents.");
                    return;
                }
                try {
                    final DriveContents driveContents = driveContentsResult.getDriveContents();
                    new Thread() {
                        @Override
                        public void run() {
                            // write content to DriveContents
                            OutputStream os = driveContents.getOutputStream();

                            try {
//                                Files.copy(file, os);
                                os.flush();
                                os.close();
                                MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                                        .setTitle(file.getName())
                                        .setStarred(true).build();

                                // create a file on root folder
                                Drive.DriveApi.getRootFolder(mGoogleApiClient)
                                        .createFile(mGoogleApiClient, changeSet, driveContents)
                                        .setResultCallback(new ResultCallback<DriveFolder.DriveFileResult>() {
                                            @Override
                                            public void onResult(DriveFolder.DriveFileResult driveFileResult) {
                                                if (!driveFileResult.getStatus().isSuccess()) {
                                                    DLog.e(TAG, "Error while trying to create the file");
                                                    return;
                                                }
                                                DLog.e(TAG, "Created a file with content: " + driveFileResult.getDriveFile().getDriveId());
                                            }
                                        });
                            } catch (IOException e) {
                                DLog.e(TAG, e.getMessage());
                            }
                        }
                    }.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void refreshOwner() {
        String selected_owner = getString(R.string.setting_not_selected);
        OwnerDAO owner = null;
        Long owner_id = setting.getOwner();
        if (owner_id != null) {
            owner = DAOManager.getOwner(setting.getOwner());
            if (owner != null)
                selected_owner = owner.getName();
        }

        fragment_setting_tv_selected_owner.setText(selected_owner);
    }

    private void refreshRoomType() {
        List<RoomTypeDAO> types = DAOManager.getAllRoomTypes();
        if (types != null && types.size() > 0) {
            RoomTypeDAO smallest = types.get(0);
            RoomTypeDAO largest = types.get(types.size() - 1);
            String room_type_text = String.format(getString(R.string.setting_room_type_text), types.size(), HouseRentalUtils.toThousandVND(smallest.getPrice()), HouseRentalUtils.toThousandVND(largest.getPrice()));
            fragment_setting_tv_selected_room_type.setText(room_type_text);
        }
    }

    @Override
    public void onConfirmed(int id, Object onWhat) {
        try {
            if (!DataSaver.getInstance().isEnabled(DataSaver.Key.INITIALIZED)) {
                DataSaver.getInstance().setEnabled(DataSaver.Key.INITIALIZED, true);
                ((MainActivity) getActiveActivity()).unlockMenu();
                replaceFragment(R.id.activity_main_container, HomeScreen.getInstance(), HomeScreen.TAG, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.fragment_setting_sw_notification:
                setting.setNotification(isChecked);
                break;
            case R.id.fragment_setting_sw_sms:
                setting.setSms(isChecked);
                break;
        }
    }

    private void initDB() {
        for (int i = 1; i < 3; ++i) {
            DAOManager.addFloor(getString(R.string.common_floor) + " " + i, i);
        }

        RoomTypeDAO level_1 = new RoomTypeDAO("Cấp 1", 1600);
        level_1.save();
        RoomTypeDAO level_2 = new RoomTypeDAO("Cấp 2", 1800);
        level_2.save();
        RoomTypeDAO level_3 = new RoomTypeDAO("Cấp 3", 2000);
        level_3.save();
        RoomTypeDAO level_4 = new RoomTypeDAO("Cấp 4", 2500);
        level_4.save();


        List<FloorDAO> floors = DAOManager.getAllFloors();
        int floor_count = 0;
        for (FloorDAO floor : floors) {
            for (int i = 1; i < 12; ++i) {
                DAOManager.addRoom(getString(R.string.common_room) + " " + (floor_count + i), 16, level_1.getId(), false, null, 0, 0, 0, floor.getId());
            }
            floor_count += 10;
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

    private class ImportDatabase extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            isImportingDatabase = true;
            super.onPreExecute();
            showLoadingDialog(getActiveActivity(), -1, getString(R.string.loading_dialog_tv));
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String path = Constant.BACKUP_PATH;
            File root = new File(path);
            if (root != null && root.exists()) {
                File[] list = root.listFiles();
                Date lastModified = null;
                File picked = null;
                for (File file : list) {
                    if (lastModified == null) {
                        lastModified = new Date(file.lastModified());
                        picked = file;
                    }
                    Date current = new Date(file.lastModified());
                    if (current.after(lastModified)) {
                        lastModified = current;
                        picked = file;
                    }
                }
                if (picked != null) {
                    // restore pick
                    File db = new File(ActiveAndroid.getDatabase().getPath());
                    try {
                        if (db != null && db.exists())
                            db.delete();
                        copy(picked, db);
                        return true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            closeLoadingDialog();
            if (aBoolean) {
                try {
                    DataSaver.getInstance().setEnabled(DataSaver.Key.INITIALIZED, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                AlarmManager mgr = (AlarmManager) getActiveActivity().getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, PendingIntent.getActivity(getCentralContext(), 0, new Intent(getActiveActivity().getIntent()), PendingIntent.FLAG_UPDATE_CURRENT));
                android.os.Process.killProcess(android.os.Process.myPid());
            } else {
                Toast.makeText(getActiveActivity(), getString(R.string.application_alert_dialog_error_general), Toast.LENGTH_SHORT).show();
            }
            isImportingDatabase = false;
        }
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
            showLoadingDialog(getActiveActivity(), -1, getString(R.string.loading_dialog_tv));
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
                String toPath = Constant.BACKUP_PATH + "houserental_" + formatter.format(new Date()) + ".db";
                File bk_folder = new File(Constant.BACKUP_PATH);
                if (bk_folder != null && !bk_folder.exists())
                    bk_folder.mkdirs();
                File fromFile = new File(fromPath);
                File toFile = new File(toPath);
                copy(fromFile, toFile);
//                uploadFileToDrive(toFile);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
