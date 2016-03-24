package com.example.houserental.function.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by leductuan on 3/5/16.
 */

@SuppressWarnings("ALL")
@Table(name = "Room")
public class RoomDAO extends Model implements Serializable {

    @Column(name = "name")
    private String name;
    @Column(name = "area")
    private int area;
    @Column(name = "type")
    private Long type_id;
    @Column(name = "rented")
    private boolean rented;
    @Column(name = "rent_date")
    private Date rent_date;
    @Column(name = "payment_start_date")
    private Date payment_start_date;
    @Column(name = "electric_number")
    private int electric_number;
    @Column(name = "water_number")
    private int water_number;
    @Column(name = "floor")
    private Long floor;
    @Column(name = "deposit")
    private int deposit;

    public RoomDAO() {
        super();
    }

    public RoomDAO(String name, int area, Long type_id, boolean rented, Date rent_date, int electric_number, int water_number, int deposit, Long floor) {
        super();
        this.name = name;
        this.area = area;
        this.type_id = type_id;
        this.rented = rented;
        this.floor = floor;
        this.payment_start_date = this.rent_date = rent_date;
        this.electric_number = electric_number;
        this.water_number = water_number;
        this.deposit = deposit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public RoomTypeDAO getType() {
        return DAOManager.getRoomType(type_id);
    }

    public void setType(Long type_id) {
        this.type_id = type_id;
    }

    public boolean isRented() {
        return rented;
    }

    public void setRented(boolean rented) {
        this.rented = rented;
    }

    public Long getFloor() {
        return floor;
    }

    public void setFloor(Long floor) {
        this.floor = floor;
    }

    public Date getRentDate() {
        return rent_date;
    }

    public void setRentDate(Date rent_date) {
        this.rent_date = rent_date;
    }

    public int getElectricNumber() {
        return electric_number;
    }

    public void setElectricNumber(int electric_number) {
        this.electric_number = electric_number;
    }

    public int getWaterNumber() {
        return water_number;
    }

    public void setWaterNumber(int water_number) {
        this.water_number = water_number;
    }

    public Date getPaymentStartDate() {
        return payment_start_date;
    }

    public void setPaymentStartDate(Date payment_start_date) {
        this.payment_start_date = payment_start_date;
    }

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }
}
