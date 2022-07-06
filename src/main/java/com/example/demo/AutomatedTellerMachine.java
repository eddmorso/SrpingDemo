package com.example.demo;

import com.example.demo.model.User;
import com.example.demo.resource.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AutomatedTellerMachine {
    private final UserService userService;
    private User loggedInUser;

    @Autowired
    public AutomatedTellerMachine(UserService userService) {
        this.userService = userService;
    }

    public void setUpSession(String userName) {
        try {
            loggedInUser = userService.getUser(userName);
        } catch (EntityNotFoundException entityNotFoundException) {
            loggedInUser = new User(userName);
            addUser(loggedInUser);
        }
    }

    public void addUser(User user) {
        userService.addUser(user);
    }

    public void sendCash(String receiverUserName, double amount) {
        if (amount > 0) {
            User receiver = userService.getUser(receiverUserName);

            loggedInUser.subtractFromBalance(amount);
            receiver.addToBalance(amount);

            userService.updateUser(loggedInUser);
            userService.updateUser(receiver);
        } else {
            throw new RuntimeException("The amount must not be negative or 0");
        }
    }

    public Map<Integer, Integer> withdraw(double amount) {
        if (isAdequateAmount(amount)) {
            loggedInUser.subtractFromBalance(amount);
            userService.updateUser(loggedInUser);
        } else {
            throw new RuntimeException("The amount must be a multiple of 100, 200, 500. Recommended sum: " +
                    getRecommendedSum(loggedInUser.getBalance()));
        }
        return getBillsList(amount);
    }

    public void topUp(double amount) {
        if (isAdequateAmount(amount)) {
            loggedInUser.addToBalance(amount);
            userService.updateUser(loggedInUser);
        } else {
            throw new RuntimeException("The amount must be a multiple of 100, 200, 500 and > 0");
        }
    }

    private boolean isAdequateAmount(double amount) {
        return amount % 100.00 == 0 && amount > 0;
    }

    private double getRecommendedSum(double usersBalance) {
        return usersBalance - usersBalance % 100;
    }

    private Map<Integer, Integer> getBillsList(double amount) {
        Map<Integer, Integer> bills = new HashMap<>();

        int numberOf500 = (int) amount / 500;
        bills.put(500, numberOf500);
        amount -= 500 * numberOf500;

        int numberOf200 = (int) amount / 200;
        bills.put(200, numberOf200);
        amount -= 200 * numberOf200;

        int numberOf100 = (int) amount / 100;
        bills.put(100, numberOf100);

        return bills;
    }
}
