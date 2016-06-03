package com.example.houserental.function.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * Created by Tyrael on 3/16/16.
 */
@SuppressWarnings("ALL")
@Table(name = "Owner")
public class OwnerDAO extends Model implements Serializable {

    @Column(name = "name")
    private String name;

    @Column(name = "signature")
    private byte[] signature;

    public OwnerDAO() {
        super();
    }

    public OwnerDAO(String name, byte[] signature) {
        super();
        this.name = name;
        this.signature = signature;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }
}
