package com.example.houserental.function.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * Created by leductuan on 3/6/16.
 */
@SuppressWarnings("ALL")
@Table(name = "Floor")
public class FloorDAO extends Model implements Serializable {

    @Column(name = "floor_index")
    private int floor_index;

    @Column(name = "name")
    private String name;

    private int room_count;

    private int user_count;

    private int device_count;

    private int rented_room_count;

    public FloorDAO() {
        super();
    }

    public FloorDAO(String name, int floor_index) {
        super();
        this.name = name;
        this.floor_index = floor_index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFloorIndex() {
        return floor_index;
    }

    public void setFloorIndex(int floor_index) {
        this.floor_index = floor_index;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getRoomCount() {
        return room_count;
    }

    public void setRoomCount(int room_count) {
        this.room_count = room_count;
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

    public int getRentedRoomCount() {
        return rented_room_count;
    }

    public void setRentedRoomCount(int rented_room_count) {
        this.rented_room_count = rented_room_count;
    }
}
