package com.example.demo;

import com.example.demo.model.User;
import com.example.demo.resource.UserService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");

        UserService userService = (context.getBean("userService", UserService.class));
        AutomatedTellerMachine atm = new AutomatedTellerMachine(userService);
        userService.addUser(new User("user1", 1032));
        userService.addUser(new User("user2", 313));

        atm.setUpSession("user3");
        atm.topUp(1200);
        atm.setUpSession("user1");
        atm.sendCash("user3", 200);
        System.out.println("Banknotes: " + atm.withdraw(600));
        atm.setUpSession("user3");
        atm.sendCash("user2", 100);

        context.close();
    }
}
