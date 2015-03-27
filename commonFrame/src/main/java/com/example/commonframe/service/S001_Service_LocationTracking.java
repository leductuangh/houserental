package com.example.commonframe.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;

import com.example.commonframe.util.LocationTracker;
import com.example.commonframe.util.LocationTracker.LocationUpdateError;
import com.example.commonframe.util.LocationTracker.LocationUpdateListener;
import com.example.commonframe.util.LocationTracker.LocationUpdateMethod;

public class S001_Service_LocationTracking extends Service implements
		LocationUpdateListener {
	private LocationTracker tracker;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (tracker == null) {
			tracker = new LocationTracker(getApplicationContext(), 10, 20,
					true, true, true, true, LocationUpdateMethod.GPS, this);
			tracker.startUpdatingLocation();
		}
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		if (tracker != null)
			tracker.stopUpdatingLocation();
		tracker = null;
		super.onDestroy();
		// start its own when being killed by OS or user
		startService(new Intent(getApplicationContext(), S001_Service_LocationTracking.class));
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onLocationSuccess(Location location, LocationUpdateMethod type) {
		// EventBus.getDefault().post(location);
	}

	@Override
	public void onLocationFail(LocationUpdateError error) {

	}

	@Override
	public void onLocationCountDown(int time_out, int countdown) {

	}

}
