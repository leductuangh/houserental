package com.example.houserental.function.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * Created by Tyrael on 3/14/16.
 */
@SuppressWarnings("ALL")
@Table(name = "Payment")
public class PaymentDAO extends Model {

    @Column(name = "payment_id", index = true)
    private String payment_id;

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

    @Column(name = "start_date")
    private Date start_date;

    @Column(name = "end_date")
    private Date end_date;

    public PaymentDAO(String payment_id, String room_id, String room_name, String owner, String payer, int room_price, int previous_electric_number, int previous_water_number, int current_electric_number, int current_water_number, int device_count, int electric_price, int water_price, int device_price, Date start_date, Date end_date) {
        this.payment_id = payment_id;
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
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public Date getEndDate() {
        return end_date;
    }

    public void setEndDate(Date end_date) {
        this.end_date = end_date;
    }

    public String getTransactionId() {
        return payment_id;
    }

    public void setTransactionId(String payment_id) {
        this.payment_id = payment_id;
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

    public Date getStartDate() {
        return start_date;
    }

    public void setStartDate(Date start_date) {
        this.start_date = start_date;
    }
}