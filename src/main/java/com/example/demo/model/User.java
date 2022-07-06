package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    @Column(name="user_name")
    private String userName;
    @Column(name="balance")
    private double balance;

    public User() {}

    public User (String userName) {
        this.userName = userName;
    }

    public User (String userName, double balance) {
        this.userName = userName;
        this.balance = balance;
    }

    public void addToBalance(double amount) {
        balance += amount;
    }

    public void subtractFromBalance(double amount) {
        if (balance - amount < 0) {
            throw new RuntimeException("Insufficient funds");
        }
        balance -= amount;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getBalance() {
        return balance;
    }

    public String getUserName() {
        return userName;
    }


}
