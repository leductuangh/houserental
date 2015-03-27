package com.example.commonframe;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.example.commonframe.adapter.A005_Adapter_Menu;
import com.example.commonframe.base.BaseFragmentActivity;
import com.example.commonframe.fragment.F001_Fragment_Example;
import com.example.commonframe.model.local.MenuItem;
import com.example.commonframe.notification.NotificationController;
import com.example.commonframe.util.CentralApplication;
import com.example.commonframe.util.LocationTracker;
import com.example.commonframe.util.LocationTracker.LocationUpdateError;
import com.example.commonframe.util.LocationTracker.LocationUpdateListener;
import com.example.commonframe.util.LocationTracker.LocationUpdateMethod;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

@SuppressWarnings("deprecation")
@SuppressLint({ "ValidFragment", "InflateParams" })
public class A005_Activity_Howto extends BaseFragmentActivity implements
		OnItemClickListener {
	private Button header_bt_back;
	private DrawerLayout a005_dl_drawerlayout;
	private ListView a005_lv_drawer;
	private ActionBarDrawerToggle a005_tg_drawer;
	private A005_Adapter_Menu a005_adt_slidemenu;
	private ArrayList<MenuItem> a005_data;
	private LocationTracker tracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a005_activity_howto);
	}

	@Override
	protected void onInitializeFragments() {
	}

	@Override
	public void onCreateObject() {
		a005_data = new ArrayList<MenuItem>();

		a005_data.add(new MenuItem("Wheels", true));
		a005_data.add(new MenuItem("Google Map", true));
		a005_data.add(new MenuItem("Send normal notification", true));
		a005_data.add(new MenuItem("Send 2 notifications", true));
		a005_data.add(new MenuItem("Send custom notification", true));
		a005_data.add(new MenuItem("Send delayed notification", true));
		a005_data.add(new MenuItem("Send delayed custom notification", true));
		a005_adt_slidemenu = new A005_Adapter_Menu(a005_data);

		a005_tg_drawer = new ActionBarDrawerToggle(this, a005_dl_drawerlayout,
				R.drawable.drawer_menu_icon, R.string.slider_menu_open,
				R.string.slider_menu_close) {

		};
	}

	@Override
	public void onDeepLinking() {

	}

	@Override
	public void onNotification() {
		int id = getIntent().getIntExtra(
				NotificationController.NOTIFICATION_ID, -1);
		String action = getIntent().getStringExtra(
				NotificationController.NOTIFICATION_CUSTOM_ACTION);
		if (id != -1) {
			NotificationManager manager = (NotificationManager) getCentralContext()
					.getSystemService(Context.NOTIFICATION_SERVICE);
			manager.cancel(id);
			String message = "";
			if (action
					.equals(NotificationController.NOTIFICATION_CUSTOM_ACTION_OK)) {
				message += "User clicks OK";
			} else if (action
					.equals(NotificationController.NOTIFICATION_CUSTOM_ACTION_CANCEL)) {
				message += "User clicks Cancel";
			}
			showAlertDialog(this, 0, "", message, -1, null);
		}
	}

	@Override
	public void onFreeObject() {

	}

	@Override
	public void onBindView() {
		ViewStub stub = (ViewStub) findViewById(R.id.header_bar);
		stub.inflate();
		header_bt_back = (Button) findViewById(R.id.header_bt_back);
		header_bt_back.setVisibility(View.VISIBLE);

		a005_lv_drawer = (ListView) findViewById(R.id.a005_lv_drawer);

		a005_lv_drawer.setAdapter(a005_adt_slidemenu);
		a005_lv_drawer.setOnItemClickListener(this);

		a005_dl_drawerlayout = (DrawerLayout) findViewById(R.id.a005_dl_drawerlayout);
		a005_dl_drawerlayout.setDrawerListener(a005_tg_drawer);

	}

	@Override
	public void onInitializeViewData() {
	}

	@Override
	public void onResumeObject() {
	}

	@Override
	protected void onPause() {
		if (tracker != null)
			tracker.stopUpdatingLocation();
		super.onPause();
	}

	@Override
	public void onSingleClick(View v) {

		switch (v.getId()) {
		case R.id.header_bt_back:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position,
			long id) {
		a005_dl_drawerlayout.closeDrawers();
		switch (position) {
		case 0:
			replaceFragment(R.id.a005_fl_container,
					new F001_Fragment_Example(), F001_Fragment_Example.TAG,
					true);
			break;
		case 1:
			replaceFragment(R.id.a005_fl_container, new Fragment() {
				@Override
				public View onCreateView(LayoutInflater inflater,
						ViewGroup container, Bundle savedInstanceState) {
					View view = inflater
							.inflate(R.layout.a005_google_map, null);
					SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager()
							.findFragmentById(R.id.map);
					if (fragment != null) {
						final GoogleMap map = fragment.getMap();
						map.setBuildingsEnabled(true);
						map.setIndoorEnabled(true);
						map.setMyLocationEnabled(true);

						if (map != null) {
							tracker = new LocationTracker(CentralApplication
									.getActiveActivity(), -1, 10, true, true,
									true, true, LocationUpdateMethod.NETWORK,
									new LocationUpdateListener() {

										@Override
										public void onLocationSuccess(
												final Location location,
												LocationUpdateMethod type) {
											runOnUiThread(new Runnable() {

												@Override
												public void run() {
													CameraUpdate update = CameraUpdateFactory
															.newCameraPosition(new CameraPosition(
																	new LatLng(
																			location.getLatitude(),
																			location.getLongitude()),
																	25, 1, 1));
													map.animateCamera(update);
													tracker.stopUpdatingLocation();
												}
											});
										}

										@Override
										public void onLocationFail(
												LocationUpdateError error) {
										}

										@Override
										public void onLocationCountDown(
												int time_out, int countdown) {
										}
									});
							tracker.startUpdatingLocation();
						}
					}
					return view;
				}
			}, "FRAGMENT_GOOGLE_MAP", true);
			break;
		case 2:
			Intent normal = new Intent(CentralApplication.getContext(),
					A005_Activity_Howto.class);
			NotificationController.sendNotification(1, "Notification",
					"Normal notification", normal);
			break;
		case 3:
			Intent first = new Intent(CentralApplication.getContext(),
					A005_Activity_Howto.class);
			NotificationController.sendNotification(1, "Notification",
					"First notification", first);

			Intent second = new Intent(CentralApplication.getContext(),
					A005_Activity_Howto.class);
			NotificationController.sendNotification(2, "Notification",
					"Second notification", second);
			break;
		case 4:
			Intent custom = new Intent(CentralApplication.getContext(),
					A005_Activity_Howto.class);
			NotificationController.sendCustomNotification(10,
					"This is custom notification", custom);
			break;
		case 5:
			Intent src = new Intent();
			src.putExtra("KEY", "VALUE");
			NotificationController.sendDelayedNotification(15,
					"DELAYED NOTIFICATION", "This is delayed notification",
					System.currentTimeMillis() + 10000, src,
					A005_Activity_Howto.class);
			break;
		case 6:
			Intent delayedCustom = new Intent();
			delayedCustom.putExtra("KEY", "VALUE");
			NotificationController.sendDelayedCustomNotification(20,
					"This is delay custom notification",
					System.currentTimeMillis() + 10000, delayedCustom,
					A005_Activity_Howto.class);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		// a005_tg_drawer.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		// a005_tg_drawer.onConfigurationChanged(newConfig);
	}
}
