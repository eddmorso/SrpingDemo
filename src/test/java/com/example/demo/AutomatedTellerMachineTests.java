package com.example.demo;

import com.example.demo.model.User;
import com.example.demo.resource.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class AutomatedTellerMachineTests {
    private final String USER_NAME1 = "user1";
    private final String USER_NAME2 = "user2";

    private User user1;
    private User user2;

    @MockBean
    private UserService userService;

    @Autowired
    private AutomatedTellerMachine atm;

    @BeforeEach
    public void prepareAtm() {
        Mockito.when(userService.getUser(USER_NAME1)).thenReturn(
                user1 = new User(USER_NAME1, 1230));
        Mockito.when(userService.getUser(USER_NAME2)).thenReturn(
                user2 = new User(USER_NAME2, 1000));

        atm = new AutomatedTellerMachine(userService);
        atm.setUpSession(USER_NAME1);
    }

    @Test
    public void testSendCash_ReceiverUserNameAndAmount_ChangedBalance() {
        atm.sendCash(USER_NAME2, 200.0);

        assertEquals(user1.getBalance(), 1030);
        assertEquals(user2.getBalance(), 1200);
    }

    @Test
    public void testSendCash_ReceiverUserNameAndNegativeAmount_ThrowRuntimeException() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            atm.sendCash(USER_NAME2, -200.0);
        });

        String expectedMessage = "The amount must not be negative or 0";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testWithdraw_Amount_MapOfBanknotes() {
        Map<Integer, Integer> banknotesExpected = new HashMap<>();
        banknotesExpected.put(100, 1);
        banknotesExpected.put(200, 1);
        banknotesExpected.put(500, 1);

        Map<Integer, Integer> banknotesResult = atm.withdraw(800);

        assertEquals(banknotesResult, banknotesExpected);
    }

    @Test
    public void testWithdraw_NegativeAmount_ThrowRuntimeException() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            atm.withdraw(-200.0);
        });

        String expectedMessage = "The amount must be a multiple of 100, 200, 500. Recommended sum: 1200.0";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testWithdraw_UnsupportedAmount_ThrowRuntimeException() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            atm.withdraw(230.0);
        });

        String expectedMessage = "The amount must be a multiple of 100, 200, 500. Recommended sum: 1200.0";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testTopUP_Amount_ChangedBalance() {
        atm.topUp(200.0);

        assertEquals(user1.getBalance(), 1430);
    }

    @Test
    public void testTopUP_UnsupportedAmount_ThrowRuntimeException() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            atm.topUp(230.0);
        });

        String expectedMessage = "The amount must be a multiple of 100, 200, 500 and > 0";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testTopUP_NegativeAmount_ThrowRuntimeException() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            atm.topUp(-230.0);
        });

        String expectedMessage = "The amount must be a multiple of 100, 200, 500 and > 0";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
