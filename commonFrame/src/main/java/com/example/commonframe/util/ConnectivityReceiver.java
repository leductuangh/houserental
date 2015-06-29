package com.example.commonframe.util;

import java.util.WeakHashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityReceiver extends BroadcastReceiver {

	public interface ConnectivityListener {

		public void onConnected();

		public void onDisconnected();
	}

	private static final WeakHashMap<Object, ConnectivityListener> listeners = new WeakHashMap<Object, ConnectivityReceiver.ConnectivityListener>();
	private static final ConnectivityManager manager = (ConnectivityManager) CentralApplication
			.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
	private static boolean connected = true;
	private static boolean disconnected = true;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

			final NetworkInfo activeNetInfo = manager.getActiveNetworkInfo();
			if (activeNetInfo != null) {
				if (connected) {
					connected = false;
					disconnected = true;
					if (activeNetInfo.isConnected()) {
						for (ConnectivityListener listener : listeners.values())
							if (listener != null)
								listener.onConnected();
					}
				}
			} else {
				if (disconnected) {
					connected = true;
					disconnected = false;
					for (ConnectivityListener listener : listeners.values())
						if (listener != null)
							listener.onDisconnected();
				}
			}
		}
	}

	public static void registerListener(ConnectivityListener listener) {
		if (listener != null) {
			listeners.put(listener, listener);
		}
	}

	public static void removeListener(ConnectivityListener listener) {
		listeners.remove(listener);
	}
}
