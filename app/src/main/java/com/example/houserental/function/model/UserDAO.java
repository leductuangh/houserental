package com.example.houserental.function.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

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

    public UserDAO() {
        super();
    }

    public UserDAO(String identification, String name, int gender, Date DOB, Career career, Long room) {
        super();
        this.identification = identification;
        this.name = name;
        this.gender = gender;
        this.DOB = DOB;
        this.career = career;
        this.room = room;
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
        }
    }
}
