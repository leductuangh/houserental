package com.example.houserental.model;

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

    @Column(name = "floor_id", index = true)
    private String floor_id;

    @Column(name = "floor_index")
    private int floor_index;

    @Column(name = "name")
    private String name;

    public FloorDAO() {
        super();
    }

    public FloorDAO(String id, String name, int floor_index) {
        super();
        this.floor_id = id;
        this.name = name;
        this.floor_index = floor_index;
    }

    public String getFloorId() {
        return floor_id;
    }

    public void setFloorId(String id) {
        this.floor_id = id;
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
}
