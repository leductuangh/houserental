package com.example.houserental.function.model;

/**
 * Created by Tyrael on 5/3/16.
 */
public class FloorInfo {

    private int device_count;

    private int user_count;

    private int room_count;

    private int rented_room_count;

    public FloorInfo(int device_count, int user_count, int room_count, int rented_room_count) {
        this.device_count = device_count;
        this.user_count = user_count;
        this.room_count = room_count;
        this.rented_room_count = rented_room_count;
    }

    public int getDeviceCount() {
        return device_count;
    }

    public void setDeviceCount(int device_count) {
        this.device_count = device_count;
    }

    public int getUserCount() {
        return user_count;
    }

    public void setUserCount(int user_count) {
        this.user_count = user_count;
    }

    public int getRoomCount() {
        return room_count;
    }

    public void setRoomCount(int room_count) {
        this.room_count = room_count;
    }

    public int getRentedRoomCount() {
        return rented_room_count;
    }

    public void setRentedRoomCount(int rented_room_count) {
        this.rented_room_count = rented_room_count;
    }
}
