package com.example.houserental.function.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * Created by Tyrael on 5/16/16.
 */
@SuppressWarnings("ALL")
@Table(name = "Setting")
public class SettingDAO extends Model implements Serializable {

    @Column(name = "electric_price")
    private int electric_price;

    @Column(name = "water_price")
    private int water_price;

    @Column(name = "waste_price")
    private int waste_price;

    @Column(name = "device_price")
    private int device_price;

    @Column(name = "deposit")
    private int deposit;

    @Column(name = "owner")
    private Long owner_id;

    @Column(name = "notification")
    private boolean notification;

    public SettingDAO() {
        super();
    }

    public SettingDAO(int electric_price, int water_price, int waste_price, int device_price, int deposit, Long owner, boolean notification) {
        this.electric_price = electric_price;
        this.water_price = water_price;
        this.waste_price = waste_price;
        this.device_price = device_price;
        this.deposit = deposit;
        this.owner_id = owner;
        this.notification = notification;
    }

    public int getElectriPrice() {
        return electric_price;
    }

    public void setElectricPrice(int electric_price) {
        this.electric_price = electric_price;
    }

    public int getWaterPrice() {
        return water_price;
    }

    public void setWaterPrice(int water_price) {
        this.water_price = water_price;
    }

    public int getWastePrice() {
        return waste_price;
    }

    public void setWastePrice(int waste_price) {
        this.waste_price = waste_price;
    }

    public int getDevicePrice() {
        return device_price;
    }

    public void setDevicePrice(int device_price) {
        this.device_price = device_price;
    }

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }

    public Long getOwner() {
        return owner_id;
    }

    public void setOwner(Long owner_id) {
        this.owner_id = owner_id;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }
}
