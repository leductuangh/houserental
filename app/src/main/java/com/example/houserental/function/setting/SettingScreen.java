package com.example.houserental.function.setting;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.example.houserental.R;
import com.example.houserental.function.MainActivity;
import com.example.houserental.function.model.DAOManager;
import com.example.houserental.function.model.OwnerDAO;
import com.example.houserental.function.model.RoomTypeDAO;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import core.base.BaseMultipleFragment;
import core.data.DataSaver;
import core.dialog.GeneralDialog;
import core.util.Constant;
import core.util.DLog;
import core.util.Utils;

public class SettingScreen extends BaseMultipleFragment implements GeneralDialog.DecisionListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // dien: 3000
    // nuoc: 5000
    // rac: 5000/n. Phong duoi 2 nguoi 15000
    // wifi: 10000
    // tien nha: 1600, 1800, 1900, 2000, 2500 / 30 ngay

    public static final String TAG = SettingScreen.class.getSimpleName();
    private static final int RESOLVE_CONNECTION_REQUEST_CODE = 999;
    private EditText fragment_setting_et_water, fragment_setting_et_electric, fragment_setting_et_device, fragment_setting_et_waste;
    private TextView fragment_setting_tv_selected_owner, fragment_setting_tv_selected_room_type;
    private boolean isExportingDatabase = false;
    private boolean isImportingDatabase = false;
    private GoogleApiClient mGoogleApiClient;

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
        fragment_setting_tv_selected_owner = (TextView) findViewById(R.id.fragment_setting_tv_selected_owner);
        fragment_setting_tv_selected_room_type = (TextView) findViewById(R.id.fragment_setting_tv_selected_room_type);
        findViewById(R.id.fragment_setting_bt_save);
        findViewById(R.id.fragment_setting_bt_backup);
        findViewById(R.id.fragment_setting_bt_restore);
        findViewById(R.id.fragment_setting_im_select_owner);
        findViewById(R.id.fragment_setting_im_select_room_type);
    }

    @Override
    public void onInitializeViewData() {
        try {
            fragment_setting_et_water.setText(DataSaver.getInstance().getInt(DataSaver.Key.WATER_PRICE) + "");
            fragment_setting_et_electric.setText(DataSaver.getInstance().getInt(DataSaver.Key.ELECTRIC_PRICE) + "");
            fragment_setting_et_device.setText(DataSaver.getInstance().getInt(DataSaver.Key.DEVICE_PRICE) + "");
            fragment_setting_et_waste.setText(DataSaver.getInstance().getInt(DataSaver.Key.WASTE_PRICE) + "");
        } catch (Exception e) {
            e.printStackTrace();
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
            case R.id.fragment_setting_im_select_owner:
                addFragment(R.id.activity_main_container, SettingOwnerListScreen.getInstance(), SettingOwnerListScreen.TAG);
                break;
            case R.id.fragment_setting_im_select_room_type:
                addFragment(R.id.activity_main_container, SettingRoomTypeListScreen.getInstance(), SettingRoomTypeListScreen.TAG);
                break;
            case R.id.fragment_setting_bt_save:
                if (validated()) {
                    try {
                        DataSaver.getInstance().setInt(DataSaver.Key.ELECTRIC_PRICE, Integer.parseInt(fragment_setting_et_electric.getText().toString().trim()));
                        DataSaver.getInstance().setInt(DataSaver.Key.WATER_PRICE, Integer.parseInt(fragment_setting_et_water.getText().toString().trim()));
                        DataSaver.getInstance().setInt(DataSaver.Key.DEVICE_PRICE, Integer.parseInt(fragment_setting_et_device.getText().toString().trim()));
                        DataSaver.getInstance().setInt(DataSaver.Key.WASTE_PRICE, Integer.parseInt(fragment_setting_et_waste.getText().toString().trim()));
                        showAlertDialog(getActiveActivity(), -1, -1, getString(R.string.application_alert_dialog_title),
                                getString(R.string.room_alert_dialog_update_success),
                                getString((R.string.common_ok)), null);
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
                            Constant.RESTORE_DATABASE_DIALOG,
                            -1,
                            getString(R.string.application_alert_dialog_title),
                            getString(R.string.application_alert_dialog_restore_confirm),
                            getString(R.string.common_ok),
                            getString(R.string.common_cancel),
                            null, this);
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
    public void onAgreed(int id) {
        if (id == Constant.RESTORE_DATABASE_DIALOG) {
            new ImportDatabase().execute();
        }
    }

    @Override
    public void onDisAgreed(int id) {

    }

    @Override
    public void onNeutral(int id) {

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
                                Files.copy(file, os);
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
        try {
            owner = DAOManager.getOwner(DataSaver.getInstance().getLong(DataSaver.Key.OWNER));
            if (owner != null)
                selected_owner = owner.getName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragment_setting_tv_selected_owner.setText(selected_owner);
    }

    private void refreshRoomType() {
        String selected_room_type = getString(R.string.setting_not_selected);
        RoomTypeDAO roomType = null;
        try {
            roomType = DAOManager.getRoomType(DataSaver.getInstance().getLong(DataSaver.Key.ROOM_TYPE));
            if (roomType != null)
                selected_room_type = roomType.getName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragment_setting_tv_selected_room_type.setText(selected_room_type);
    }

    private class ImportDatabase extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            isImportingDatabase = true;
            super.onPreExecute();
            showLoadingDialog(getActiveActivity());
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
                        Files.copy(picked, db);
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
                String toPath = Constant.BACKUP_PATH + "houserental_" + formatter.format(new Date()) + ".db";
                File bk_folder = new File(Constant.BACKUP_PATH);
                if (bk_folder != null && !bk_folder.exists())
                    bk_folder.mkdirs();
                File fromFile = new File(fromPath);
                File toFile = new File(toPath);
                Files.copy(fromFile, toFile);
                uploadFileToDrive(toFile);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
