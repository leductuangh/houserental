package com.example.houserental.function.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.example.houserental.R;
import com.example.houserental.function.HouseRentalApplication;

import java.io.Serializable;
import java.util.Date;

import core.base.BaseApplication;

/**
 * Created by leductuan on 3/5/16.
 */
@SuppressWarnings("ALL")
@Table(name = "User")
public class UserDAO extends Model implements Serializable {

    @Column(name = "identification")
    private String identification;
    @Column(name = "name")
    private String name;
    @Column(name = "gender")
    private int gender; // 0 is male, 1 is female
    @Column(name = "dob")
    private Date DOB;
    @Column(name = "career")
    private Career career;
    @Column(name = "room")
    private Long room;
    @Column(name = "phone")
    private String phone;
    @Column(name = "registered")
    private boolean registered;

    private int age;

    private int device_count;

    public UserDAO() {
        super();
    }

    public UserDAO(String identification, String name, int gender, Date DOB, Career career, String phone, boolean registered, Long room) {
        super();
        this.identification = identification;
        this.name = name;
        this.gender = gender;
        this.DOB = DOB;
        this.career = career;
        this.room = room;
        this.phone = phone;
        this.registered = registered;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Date getDOB() {
        return DOB;
    }

    public void setDOB(Date DOB) {
        this.DOB = DOB;
    }

    public Career getCareer() {
        return career;
    }

    public void setCareer(Career career) {
        this.career = career;
    }

    public Long getRoom() {
        return room;
    }

    public void setRoom(Long room) {
        this.room = room;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getDeviceCount() {
        return device_count;
    }

    public void setDeviceCount(int device_count) {
        this.device_count = device_count;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public enum Career {
        UNKNOWN {
            @Override
            public String toString() {
                return BaseApplication.getContext().getString(com.example.houserental.R.string.user_career_unknown);
            }
        }, STUDENT {
            @Override
            public String toString() {
                return BaseApplication.getContext().getString(com.example.houserental.R.string.user_career_student);
            }
        }, WORKER {
            @Override
            public String toString() {
                return BaseApplication.getContext().getString(com.example.houserental.R.string.user_career_worker);
            }
        }, BUSINESS {
            @Override
            public String toString() {
                return BaseApplication.getContext().getString(com.example.houserental.R.string.user_career_business);
            }
        }, WHITE_COLLAR {
            @Override
            public String toString() {
                return BaseApplication.getContext().getString(com.example.houserental.R.string.user_career_white_collar);
            }
        }, CHOOSE {
            @Override
            public String toString() {
                return HouseRentalApplication.getContext().getString(R.string.common_user_choose_career);
            }
        }
    }
}
