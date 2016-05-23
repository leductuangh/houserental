package core.data;

import android.content.Context;
import android.content.SharedPreferences;

import core.base.BaseApplication;
import core.util.Utils;

/**
 * @author Tyrael
 * @version 1.0 <br>
 *          <br>
 *          <b>Class Overview</b> <br>
 *          <br>
 *          Represents a class for storing data to the shared preference <br>
 * @since January 2014
 */

@SuppressWarnings({"BooleanMethodIsAlwaysInverted", "SameParameterValue", "UnusedReturnValue", "UnusedParameters"})
public class DataSaver {
    /**
     * The name of this storage in the system
     */
    private static final String KEY_SHARED_PREFERENCES = Utils.getSharedPreferenceKey();
    /**
     * Represent the instance of this class, only one instance can be used at a
     * time and apply for the entire application
     */
    private static DataSaver instance;
    /**
     * The reference to SharedPreferences which actually read and write the data
     * to the storage
     */
    private final SharedPreferences prefs;

    private DataSaver() {
        prefs = BaseApplication.getContext().getSharedPreferences(
                KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }

    /**
     * This method will return the instance of this class, only one instance can
     * be used at a time
     *
     * @return The instance of this class
     */
    public static synchronized DataSaver getInstance() {
        if (instance == null) {
            instance = new DataSaver();
        }
        return instance;
    }

    /**
     * This method is to set the QUEUE field to the storage.
     *
     * @param queue The queue int value
     * @return true if the process is success, false otherwise
     */
    private synchronized boolean setQueue(int queue) {
        return prefs.edit().putInt(Key.QUEUE.toString(), queue).commit();
    }

    /**
     * This method is to get the QUEUE value from the storage
     *
     * @return The QUEUE integer value, 0 if the field not presented
     */
    private synchronized int getQueue() {
        return prefs.getInt(Key.QUEUE.toString(), 0);
    }

    /**
     * This method is to set the VERSION field to the storage.
     *
     * @param version The version string value
     * @return true if the process is success, false otherwise
     */
    private synchronized boolean setVersion(String version) {
        return prefs.edit().putString(Key.VERSION.toString(), version).commit();
    }

    /**
     * This method is to get the VERSION value from the storage
     *
     * @return The VERSION string value, null if the field not presented
     */
    private synchronized String getVersion() {
        return prefs.getString(Key.VERSION.toString(), null);
    }

    /**
     * This method is to set the GCM field to the storage.
     *
     * @param gcm The gcm string value
     * @return true if the process is success, false otherwise
     */
    private synchronized boolean setGcm(String gcm) {
        return prefs.edit().putString(Key.GCM.toString(), gcm).commit();
    }

    /**
     * This method is to get the GCM value from the storage
     *
     * @return The GCM string value, null if the field not presented
     */
    private synchronized String getGcm() {
        return prefs.getString(Key.GCM.toString(), null);
    }

    /**
     * This method is to set the TOKEN field to the storage.
     *
     * @param token The token string value
     * @return true if the process is success, false otherwise
     */
    private synchronized boolean setToken(String token) {
        return prefs.edit().putString(Key.TOKEN.toString(), token).commit();
    }

    /**
     * This method is to get the TOKEN value from the storage
     *
     * @return The TOKEN string value, null if the field not presented
     */
    private synchronized String getToken() {
        return prefs.getString(Key.TOKEN.toString(), null);
    }

    /**
     * This method is to set the UPDATED status to the storage.
     *
     * @param isUpdated The updated boolean value
     * @return true if the process is success, false otherwise
     */
    private synchronized boolean setUpdated(boolean isUpdated) {
        return prefs.edit().putBoolean(Key.UPDATED.toString(), isUpdated)
                .commit();
    }

    /**
     * This method is to get the UPDATED value from the storage
     *
     * @return The UPDATED boolean value, false if the field not presented
     */
    private synchronized boolean isUpdated() {
        return prefs.getBoolean(Key.UPDATED.toString(), false);
    }

    /**
     * This method is to set the LOG status to the storage.
     *
     * @param isLogged The log boolean value
     * @return true if the process is success, false otherwise
     */
    private synchronized boolean setLogged(boolean isLogged) {
        return prefs.edit().putBoolean(Key.LOGGED.toString(), isLogged)
                .commit();
    }

    /**
     * This method is to get the INITIALIZED value from the storage
     *
     * @return The INITIALIZED boolean value, false if the field not presented
     */
    private synchronized boolean isInitialized() {
        return prefs.getBoolean(Key.INITIALIZED.toString(), false);
    }


    /**
     * This method is to set the INITIALIZED status to the storage.
     *
     * @param isInitialiezd The INITIALIZED boolean value
     * @return true if the process is success, false otherwise
     */
    private synchronized boolean setInitialized(boolean isInitialiezd) {
        return prefs.edit().putBoolean(Key.INITIALIZED.toString(), isInitialiezd)
                .commit();
    }

    /**
     * This method is to get the LOG value from the storage
     *
     * @return The LOG boolean value, false if the field not presented
     */
    private synchronized boolean isLogged() {
        return prefs.getBoolean(Key.LOGGED.toString(), false);
    }

    public synchronized int nextNotificationId() {
        int notificationId = prefs.getInt(Key.NOTIFICATION.toString(), 0);
        int nextNotificationId = notificationId + 1;
        if (nextNotificationId >= 1000000)
            nextNotificationId = 0;
        prefs.edit().putInt(Key.NOTIFICATION.toString(), nextNotificationId).commit();
        return notificationId;
    }
//
//    private synchronized boolean setWaterPrice(int value) {
//        return prefs.edit().putInt(Key.WATER_PRICE.toString(), value).commit();
//    }
//
//    private synchronized int getWaterPrice() {
//        return prefs.getInt(Key.WATER_PRICE.toString(), 0);
//    }
//
//    private synchronized boolean setDevicePrice(int value) {
//        return prefs.edit().putInt(Key.DEVICE_PRICE.toString(), value).commit();
//    }
//
//    private synchronized int getDevicePrice() {
//        return prefs.getInt(Key.DEVICE_PRICE.toString(), 0);
//    }
//
//    private synchronized int getDeposit() {
//        return prefs.getInt(Key.DEPOSIT.toString(), 0);
//    }
//
//    private synchronized boolean setDeposit(int value) {
//        return prefs.edit().putInt(Key.DEPOSIT.toString(), value).commit();
//    }
//
//    private synchronized boolean setOwner(Long value) {
//        return prefs.edit().putLong(Key.OWNER.toString(), value).commit();
//    }
//
//    private synchronized Long getOwner() {
//        return prefs.getLong(Key.OWNER.toString(), -1);
//    }
//
//    private synchronized int getWastePrice() {
//        return prefs.getInt(Key.WASTE_PRICE.toString(), 0);
//    }
//
//    private synchronized boolean setWastePrice(int value) {
//        return prefs.edit().putInt(Key.WASTE_PRICE.toString(), value).commit();
//    }
//
//    private synchronized boolean setRoomType(Long value) {
//        return prefs.edit().putLong(Key.ROOM_TYPE.toString(), value).commit();
//    }
//
//    private synchronized Long getRoomType() {
//        return prefs.getLong(Key.ROOM_TYPE.toString(), -1);
//    }

    public synchronized boolean setLong(Key key, Long value) throws Exception {
        boolean result = false;
//        switch (key) {
//            case OWNER:
//                result = setOwner(value);
//                break;
//            case ROOM_TYPE:
//                result = setRoomType(value);
//                break;
//            default:
//                throw new Exception("DataSaver:setLong: No key found!");
//        }
        return result;
    }

    public synchronized Long getLong(Key key) throws Exception {
        Long value = null;
//        switch (key) {
//            case OWNER:
//                value = getOwner();
//                break;
//            case ROOM_TYPE:
//                value = getRoomType();
//                break;
//            default:
//                throw new Exception("DataSaver:getLong: No key found!");
//        }
        return value;
    }


    /**
     * This method is to set the String value to the storage base on the KEY
     *
     * @param key   The key for the value, defined in <code>enum Key</code>
     * @param value The value for this key as a string
     * @return true if the process is success, false otherwise
     * @throws Exception if the key is not found in the storage
     */
    public synchronized boolean setString(Key key, String value)
            throws Exception {
        boolean result;
        switch (key) {
            case TOKEN:
                result = setToken(value);
                break;
            case GCM:
                result = setGcm(value);
                break;
            case VERSION:
                result = setVersion(value);
                break;
            default:
                throw new Exception("DataSaver:setString: No key found!");
        }

        return result;
    }

    /**
     * This method is to get a STRING value from the storage base on the KEY
     *
     * @param key The key for the value, defined in <code>enum Key</code>
     * @return The value of this key
     * @throws Exception if the key is not found in the storage
     */
    public synchronized String getString(Key key) throws Exception {
        String value;
        switch (key) {
            case TOKEN:
                value = getToken();
                break;
            case GCM:
                value = getGcm();
                break;
            case VERSION:
                value = getVersion();
                break;
            default:
                throw new Exception("DataSaver:getString: No key found!");
        }
        return value;
    }

    /**
     * This method is to get a INTEGER value from the storage base on the KEY
     *
     * @param key The key for the value, defined in <code>enum Key</code>
     * @return The value of this key
     * @throws Exception if the key is not found in the storage
     */
    public synchronized int getInt(Key key) throws Exception {
        int value = -1;
//        switch (key) {
//            case QUEUE:
//                value = getQueue();
//                break;
//            case ELECTRIC_PRICE:
//                value = getElectricPrice();
//                break;
//            case WATER_PRICE:
//                value = getWaterPrice();
//                break;
//            case DEVICE_PRICE:
//                value = getDevicePrice();
//                break;
//            case WASTE_PRICE:
//                value = getWastePrice();
//                break;
//            case DEPOSIT:
//                value = getDeposit();
//                break;
//            default:
//                throw new Exception("getInt: No key found!");
//        }
        return value;
    }

    /**
     * This method is to set the INTEGER value to the storage base on the KEY
     *
     * @param key   The key for the value, defined in <code>enum Key</code>
     * @param value The value for this key as a int
     * @return true if the process is success, false otherwise
     * @throws Exception if the key is not found in the storage
     */
    public synchronized boolean setInt(Key key, int value)
            throws Exception {
        boolean result = false;
//        switch (key) {
//            case QUEUE:
//                result = setQueue(value);
//                break;
//            case ELECTRIC_PRICE:
//                result = setElectricPrice(value);
//                break;
//            case WATER_PRICE:
//                result = setWaterPrice(value);
//                break;
//            case DEVICE_PRICE:
//                result = setDevicePrice(value);
//                break;
//            case WASTE_PRICE:
//                result = setWastePrice(value);
//                break;
//            case DEPOSIT:
//                result = setDeposit(value);
//                break;
//            default:
//                throw new Exception("DataSaver:setInt: No key found!");
//        }
        return result;
    }

    /**
     * This method is to set the BOOLEAN value to the storage base on the KEY
     *
     * @param key   The key for the value, defined in <code>enum Key</code>
     * @param value The value for this key as
     *              <code>true<code> or <code>false<code>
     * @return true if the process is success, false otherwise
     * @throws Exception if the key is not found in the storage
     */
    public synchronized boolean setEnabled(Key key, boolean value)
            throws Exception {
        boolean result;
        switch (key) {
            case LOGGED:
                result = setLogged(value);
                break;
            case UPDATED:
                result = setUpdated(value);
                break;
            case INITIALIZED:
                result = setInitialized(value);
                break;
            default:
                throw new Exception("DataSaver:setEnabled: No key found!");
        }

        return result;
    }

    /**
     * This method is to get a BOOLEAN value from the storage base on the KEY
     *
     * @param key The key for the value, defined in <code>enum Key</code>
     * @return The value of this key
     * @throws Exception if the key is not found in the storage
     */

    public synchronized boolean isEnabled(Key key) throws Exception {
        boolean value;
        switch (key) {
            case LOGGED:
                value = isLogged();
                break;
            case UPDATED:
                value = isUpdated();
                break;
            case INITIALIZED:
                value = isInitialized();
                break;
            default:
                throw new Exception("DataSaver:isEnabled: No key found!");
        }
        return value;
    }

    /**
     * public enum <br>
     * <b>Key</b> <br>
     * Represents the key of stored data in share preference. Each key will
     * present a field and has the value of the override <code>toString()</code>
     */
    public enum Key {
        QUEUE {
            @Override
            public String toString() {
                return "queue";
            }
        },
        TOKEN {
            @Override
            public String toString() {
                return "token";
            }
        },
        LOGGED {
            @Override
            public String toString() {
                return "logged";
            }
        },
        GCM {
            @Override
            public String toString() {
                return "gcm";
            }
        },
        VERSION {
            @Override
            public String toString() {
                return "version";
            }
        },
        UPDATED {
            @Override
            public String toString() {
                return "updated";
            }
        },
        INITIALIZED {
            @Override
            public String toString() {
                return "initialized";
            }
        },
        NOTIFICATION {
            @Override
            public String toString() {
                return "notification";
            }
        }
    }

}
