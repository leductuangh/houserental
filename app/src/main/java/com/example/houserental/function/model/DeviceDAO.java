package com.example.houserental.function.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * Created by leductuan on 3/6/16.
 */
@SuppressWarnings("ALL")
@Table(name = "Device")
public class DeviceDAO extends Model implements Serializable {

    @Column(name = "mac", index = true)
    private String MAC;

    @Column(name = "user")
    private Long user;

    @Column(name = "description")
    private String description;

    public DeviceDAO() {
        super();
    }

    public DeviceDAO(String MAC, String description, Long user) {
        super();
        this.MAC = MAC;
        this.user = user;
        this.description = description;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
