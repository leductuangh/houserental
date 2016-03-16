package com.example.houserental.function.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Tyrael on 3/16/16.
 */
@SuppressWarnings("ALL")
@Table(name = "Owner")
public class OwnerDAO extends Model {

    @Column(name = "name")
    private String name;

    public OwnerDAO() {
        super();
    }

    public OwnerDAO(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
