package com.example.commonframe.util.gcm;

import android.content.Intent;

import com.example.commonframe.util.DLog;
import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * @author Tyrael
 * @version 1.0 <br>
 * @since October 2015
 */
public class GcmInstanceIdListenerService extends InstanceIDListenerService {

    private static final String TAG = GcmInstanceIdListenerService.class.getSimpleName();

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        DLog.d(TAG, "GCM id has been refreshed");
        Intent intent = new Intent(this, GcmRegistrationService.class);
        startService(intent);
    }
    // [END refresh_token]
}