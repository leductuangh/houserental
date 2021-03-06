package com.example.houserental.function.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Tyrael on 3/14/16.
 */
@SuppressWarnings("ALL")
@Table(name = "Payment")
public class PaymentDAO extends Model implements Serializable {

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

    @Column(name = "user_count")
    private int user_count;

    @Column(name = "waste_price")
    private int waste_price;

    @Column(name = "device_price")
    private int device_price;

    @Column(name = "room_price")
    private int room_price;

    @Column(name = "owner")
    private String owner;

    @Column(name = "payer")
    private String payer;

    @Column(name = "room_id")
    private Long room_id;

    @Column(name = "room_name")
    private String room_name;

    @Column(name = "start_date")
    private Date start_date;

    @Column(name = "end_date")
    private Date end_date;

    @Column(name = "electric_total")
    private int electric_total;

    @Column(name = "water_total")
    private int water_total;

    @Column(name = "device_total")
    private int device_total;

    @Column(name = "waste_total")
    private int waste_total;

    @Column(name = "total")
    private int total;

    @Column(name = "stay_days")
    private int stay_days;

    @Column(name = "deposit_total")
    private int deposit_total;

    @Column(name = "deposit_in_room")
    private int deposit_in_room;

    @Column(name = "owner_signature")
    private byte[] owner_signature;

    @Column(name = "payer_signature")
    private byte[] payer_signature;

    @Column(name = "code")
    private String code;

    public PaymentDAO() {
        super();
    }


    public PaymentDAO(Long room_id, String room_name, String owner, String payer, int room_price, int previous_electric_number, int previous_water_number, int current_electric_number, int current_water_number, int device_count, int electric_price, int water_price, int device_price, int waste_price, int user_count, Date start_date, Date end_date, int stay_days, int deposit_total, int deposit_in_room) {
        this.room_id = room_id;
        this.room_name = room_name;
        this.owner = owner;
        this.payer = payer;
        this.room_price = room_price;
        this.previous_electric_number = previous_electric_number;
        this.previous_water_number = previous_water_number;
        this.current_electric_number = current_electric_number;
        this.current_water_number = current_water_number;
        this.device_count = device_count;
        this.electric_price = electric_price;
        this.water_price = water_price;
        this.device_price = device_price;
        this.waste_price = waste_price;
        this.user_count = user_count;
        this.start_date = start_date;
        this.end_date = end_date;
        this.stay_days = stay_days;
        this.deposit_total = deposit_total;
        this.deposit_in_room = deposit_in_room;
        this.payer_signature = new byte[0];
        this.owner_signature = new byte[0];
        this.code = RandomStringUtils.randomAlphanumeric(6);
    }

    public Date getEndDate() {
        return end_date;
    }

    public void setEndDate(Date end_date) {
        this.end_date = end_date;
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

    public int getDevicePrice() {
        return device_price;
    }

    public void setDevicePrice(int device_price) {
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

    public Long getRoomId() {
        return room_id;
    }

    public void setRoomId(Long room_id) {
        this.room_id = room_id;
    }

    public String getRoomName() {
        return room_name;
    }

    public void setRoomName(String room_name) {
        this.room_name = room_name;
    }

    public int getUserCount() {
        return user_count;
    }

    public void setUserCount(int user_count) {
        this.user_count = user_count;
    }

    public int getWastePrice() {
        return waste_price;
    }

    public void setWastePrice(int waste_price) {
        this.waste_price = waste_price;
    }

    public Date getStartDate() {
        return start_date;
    }

    public void setStartDate(Date start_date) {
        this.start_date = start_date;
    }

    public int getElectricTotal() {
        return electric_total;
    }

    public void setElectricTotal(int electric_total) {
        this.electric_total = electric_total;
    }

    public int getWaterTotal() {
        return water_total;
    }

    public void setWaterTotal(int water_total) {
        this.water_total = water_total;
    }

    public int getDeviceTotal() {
        return device_total;
    }

    public void setDeviceTotal(int device_total) {
        this.device_total = device_total;
    }

    public int getWasteTotal() {
        return waste_total;
    }

    public void setWasteTotal(int waste_total) {
        this.waste_total = waste_total;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStayDays() {
        return stay_days;
    }

    public void setStayDays(int stay_days) {
        this.stay_days = stay_days;
    }

    public int getDepositTotal() {
        return deposit_total;
    }

    public void setDepositTotal(int deposit_total) {
        this.deposit_total = deposit_total;
    }

    public int getDepositInRoom() {
        return deposit_in_room;
    }

    public void setDepositInRoom(int deposit_in_room) {
        this.deposit_in_room = deposit_in_room;
    }

    public byte[] getOwnerSignature() {
        return owner_signature;
    }

    public void setOwnerSignature(byte[] owner_signature) {
        this.owner_signature = owner_signature;
    }

    public byte[] getPayerSignature() {
        return payer_signature;
    }

    public void setPayerSignature(byte[] payer_signature) {
        this.payer_signature = payer_signature;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
