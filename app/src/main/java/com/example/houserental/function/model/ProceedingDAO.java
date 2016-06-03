package com.example.houserental.function.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Tyrael on 6/3/16.
 */
@Table(name = "Proceeding")
public class ProceedingDAO extends Model implements Serializable {

    @Column(name = "room")
    private Long room;

    @Column(name = "payer")
    private Long payer;

    @Column(name = "paid_date")
    private Date paid_date;

    @Column(name = "electric_number")
    private int electric_number;

    @Column(name = "water_number")
    private int water_number;

    public ProceedingDAO() {
        super();
    }

    public ProceedingDAO(Long room, Long payer, Date paid_date, int electric_number, int water_number) {
        super();
        this.room = room;
        this.payer = payer;
        this.paid_date = paid_date;
        this.electric_number = electric_number;
        this.water_number = water_number;
    }

    public Long getRoom() {
        return room;
    }

    public void setRoom(Long room) {
        this.room = room;
    }

    public Long getPayer() {
        return payer;
    }

    public void setPayer(Long payer) {
        this.payer = payer;
    }

    public Date getPaidDate() {
        return paid_date;
    }

    public void setPaidDate(Date paid_date) {
        this.paid_date = paid_date;
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
}
