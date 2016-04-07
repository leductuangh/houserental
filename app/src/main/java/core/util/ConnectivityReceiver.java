package core.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.WeakHashMap;

import core.base.BaseApplication;


@SuppressWarnings("unused")
public class ConnectivityReceiver extends BroadcastReceiver {

    private static final WeakHashMap<Object, ConnectivityListener> listeners = new WeakHashMap<>();
    private static final ConnectivityManager manager = (ConnectivityManager) BaseApplication
            .getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    private static boolean connected = true;
    private static boolean disconnected = true;

    public static void registerListener(ConnectivityListener listener) {
        if (listener != null) {
            listeners.put(listener, listener);
        }
    }

    public static void removeListener(ConnectivityListener listener) {
        listeners.remove(listener);
    }

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

    public interface ConnectivityListener {

        void onConnected();

        void onDisconnected();
    }
}
