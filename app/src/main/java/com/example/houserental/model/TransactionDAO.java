package com.example.houserental.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * Created by Tyrael on 3/14/16.
 */
@SuppressWarnings("ALL")
@Table(name = "Transaction")
public class TransactionDAO extends Model {

    @Column(name = "transaction_id", index = true)
    private String transaction_id;

    @Column(name = "previous_electric_number")
    private int previous_electric_number;

    @Column(name = "previous_water_number")
    private int previous_water_number;

    @Column(name = "current_electric_number")
    private int current_electric_number;

    @Column(name = "current_water_number")
    private int current_water_number;

    @Column(name = "electric_price")
    private int electric_price;

    @Column(name = "water_price")
    private int water_price;

    @Column(name = "device_count")
    private int device_count;

    @Column(name = "device_price")
    private int device_price;

    @Column(name = "room_price")
    private int room_price;

    @Column(name = "owner")
    private String owner;

    @Column(name = "payer")
    private String payer;

    @Column(name = "room_id")
    private String room_id;

    @Column(name = "room_name")
    private String room_name;

    @Column(name = "created_date")
    private Date created_date;

    public TransactionDAO(String transaction_id, String room_id, String room_name, String owner, String payer, int room_price, int previous_electric_number, int previous_water_number, int current_electric_number, int current_water_number, int device_count, int electric_price, int water_price, int device_price, Date created_date) {
        this.transaction_id = transaction_id;
        this.previous_electric_number = previous_electric_number;
        this.previous_water_number = previous_water_number;
        this.current_electric_number = current_electric_number;
        this.current_water_number = current_water_number;
        this.electric_price = electric_price;
        this.water_price = water_price;
        this.device_count = device_count;
        this.device_price = device_price;
        this.room_price = room_price;
        this.owner = owner;
        this.payer = payer;
        this.room_id = room_id;
        this.room_name = room_name;
        this.created_date = created_date;
    }

    public Date getCreatedDate() {
        return created_date;
    }

    public void setCreatedDate(Date created_date) {
        this.created_date = created_date;
    }

    public String getTransactionId() {
        return transaction_id;
    }

    public void setTransactionId(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public int getPreviousElectricNumber() {
        return previous_electric_number;
    }

    public void setPreviousElectricNumber(int previous_electric_number) {
        this.previous_electric_number = previous_electric_number;
    }

    public int getPreviousWaterNumber() {
        return previous_water_number;
    }

    public void setPreviousWaterNumber(int previous_water_number) {
        this.previous_water_number = previous_water_number;
    }

    public int getCurrentElectricNumber() {
        return current_electric_number;
    }

    public void setCurrentElectricNumber(int current_electric_number) {
        this.current_electric_number = current_electric_number;
    }

    public int getCurrentWaterNumber() {
        return current_water_number;
    }

    public void setCurrentWaterNumber(int current_water_number) {
        this.current_water_number = current_water_number;
    }

    public int getElectricPrice() {
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

    public int getDeviceCount() {
        return device_count;
    }

    public void setDeviceCount(int device_count) {
        this.device_count = device_count;
    }

    public int getDevice_price() {
        return device_price;
    }

    public void setDevice_price(int device_price) {
        this.device_price = device_price;
    }

    public int getRoomPrice() {
        return room_price;
    }

    public void setRoomPrice(int room_price) {
        this.room_price = room_price;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getRoomId() {
        return room_id;
    }

    public void setRoomId(String room_id) {
        this.room_id = room_id;
    }

    public String getRoomName() {
        return room_name;
    }

    public void setRoomName(String room_name) {
        this.room_name = room_name;
    }
}
