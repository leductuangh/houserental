package com.example.houserental.function.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * Created by leductuan on 3/17/16.
 */
@Table(name = "RoomType")
public class RoomTypeDAO extends Model implements Serializable {

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private int price;

    private int room_count;

    public RoomTypeDAO() {
        super();
    }

    public RoomTypeDAO(String name, int price) {
        super();
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRoomCount() {
        return room_count;
    }

    public void setRoomCount(int room_count) {
        this.room_count = room_count;
    }
}
