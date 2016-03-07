package com.example.houserental.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.example.houserental.R;
import com.example.houserental.core.base.BaseApplication;

import java.io.Serializable;

/**
 * Created by leductuan on 3/5/16.
 */

@SuppressWarnings("ALL")
@Table(name = "Room")
public class RoomDAO extends Model implements Serializable {

    @Column(name = "room_id", index = true)
    private String room_id;
    @Column(name = "name")
    private String name;
    @Column(name = "area")
    private int area;
    @Column(name = "type")
    private Type type;
    @Column(name = "rented")
    private boolean rented;
    @Column(name = "floor")
    private String floor;

    public RoomDAO() {
        super();
    }

    public RoomDAO(String id, String name, int area, Type type, boolean rented, String floor) {
        super();
        this.room_id = id;
        this.name = name;
        this.area = area;
        this.type = type;
        this.rented = rented;
        this.floor = floor;
    }

    public String getRoomId() {
        return room_id;
    }

    public void setRoomId(String id) {
        this.room_id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isRented() {
        return rented;
    }

    public void setRented(boolean rented) {
        this.rented = rented;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public enum Type {
        FRONT {
            @Override
            public String toString() {
                return BaseApplication.getContext().getString(R.string.common_room_type_front);
            }
        }, NORMAL {
            @Override
            public String toString() {
                return BaseApplication.getContext().getString(R.string.common_room_type_normal);
            }
        }
    }
}
