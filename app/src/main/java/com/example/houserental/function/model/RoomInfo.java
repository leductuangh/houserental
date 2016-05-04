package com.example.houserental.function.model;

/**
 * Created by Tyrael on 5/4/16.
 */
public class RoomInfo {

    private int user_count;

    private int device_count;

    public RoomInfo(int user_count, int device_count) {
        this.user_count = user_count;
        this.device_count = device_count;
    }

    public int getUserCount() {
        return user_count;
    }

    public void setUserCount(int user_count) {
        this.user_count = user_count;
    }

    public int getDeviceCount() {
        return device_count;
    }

    public void setDeviceCount(int device_count) {
        this.device_count = device_count;
    }
}
