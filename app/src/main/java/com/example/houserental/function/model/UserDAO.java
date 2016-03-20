package com.example.houserental.function.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.core.core.base.BaseApplication;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by leductuan on 3/5/16.
 */
@SuppressWarnings("ALL")
@Table(name = "User")
public class UserDAO extends Model implements Serializable {

    @Column(name = "user_id", index = true)
    private String user_id;
    @Column(name = "name")
    private String name;
    @Column(name = "gender")
    private int gender; // 0 is male, 1 is female
    @Column(name = "dob")
    private Date DOB;
    @Column(name = "career")
    private Career career;
    @Column(name = "room")
    private String room;

    public UserDAO() {
        super();
    }

    public UserDAO(String id, String name, int gender, Date DOB, Career career, String room) {
        super();
        this.user_id = id;
        this.name = name;
        this.gender = gender;
        this.DOB = DOB;
        this.career = career;
        this.room = room;
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String id) {
        this.user_id = id;
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

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
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
