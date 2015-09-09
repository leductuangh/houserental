package com.example.commonframe.util.gcm;

import android.app.IntentService;
import android.content.Intent;

import com.example.commonframe.util.DLog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by Tyrael on 9/9/15.
 */
public class GcmRegistrationService extends IntentService {

    public static final String TAG = "GcmRegistrationService";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public GcmRegistrationService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }


    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getApplicationContext());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                DLog.d(TAG, "Please install google play services");
            } else {
                DLog.d(TAG, "This device is not supported.");
            }
            return false;
        }
        return true;
    }
}
