package com.example.houserental.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by leductuan on 3/6/16.
 */
@SuppressWarnings("ALL")
@Table(name = "Device")
public class DeviceDAO extends Model {

    @Column(name = "mac", index = true)
    private String MAC;

    @Column(name = "user")
    private String user;

    public DeviceDAO() {
        super();
    }

    public DeviceDAO(String MAC, String user) {
        super();
        this.MAC = MAC;
        this.user = user;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
