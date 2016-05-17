package com.example.houserental.function.model;

import java.util.List;

/**
 * Created by Tyrael on 3/15/16.
 */
public class Payment {

    private String name;
    private List<PaymentDAO> payments;

    public Payment(String name, List<PaymentDAO> payments) {
        this.name = name;
        this.payments = payments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PaymentDAO> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentDAO> payments) {
        this.payments = payments;
    }
}
