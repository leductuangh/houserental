package com.example.houserental.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by leductuan on 3/6/16.
 */
@SuppressWarnings("ALL")
@Table(name = "Floor")
public class FloorDAO extends Model {

    @Column(name = "floor_id", index = true)
    private String floor_id;

    @Column(name = "name")
    private String name;

    public FloorDAO() {
        super();
    }

    public FloorDAO(String id, String name) {
        super();
        this.floor_id = id;
        this.name = name;
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
}
