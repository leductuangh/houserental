package com.example.houserental.function.model;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;

import core.data.DataSaver;

public class WifiHost {

    private String host;
    private String username;
    private String password;
    private String MAC;
    private String PIN;
    private boolean isMain;

    public WifiHost() {
    }

    public WifiHost(String host, String username, String password, String MAC, String PIN, boolean isMain) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.MAC = MAC;
        this.PIN = PIN;
        this.isMain = isMain;
    }

    public static void save(ArrayList<WifiHost> list) {
        JSONArray array = new JSONArray();
        try {
            if (list != null) {
                for (int i = 0; i < list.size(); ++i) {
                    array.put(i, list.get(i));
                }
                DataSaver.getInstance().setString(DataSaver.Key.WIFI, array.toString());
            } else {
                DataSaver.getInstance().setString(DataSaver.Key.WIFI, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<WifiHost> get() {
        ArrayList wifis = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(DataSaver.getInstance().getString(DataSaver.Key.WIFI));
            Gson gson = new Gson();
            for (int i = 0; i < array.length(); ++i) {
                WifiHost wifi = gson.fromJson(String.valueOf(array.getString(i)), WifiHost.class);
                if (wifi != null)
                    wifis.add(wifi);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wifis;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public String getPIN() {
        return PIN;
    }

    public void setPIN(String PIN) {
        this.PIN = PIN;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        String json = gson.toJson(this, WifiHost.class);
        return json;
    }
}
