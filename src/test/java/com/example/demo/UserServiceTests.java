package com.example.demo;

import com.example.demo.model.User;
import com.example.demo.repository.UserCrudRepository;
import com.example.demo.resource.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@SpringBootTest
class UserServiceTests {
    @MockBean
    private UserCrudRepository userCrudRepository;

    @Autowired
    private UserService userService;

    @Test
    public void testAddUser_User_calledMethodSave() {
        User testUser = getTestUser();

        userService.addUser(testUser);

        Mockito.verify(userCrudRepository, times(1)).save(testUser);
    }

    @Test
    public void testGetUser_UserName_User() {
        User testUser = getTestUser();
        Mockito.when(userCrudRepository.findById("user")).thenReturn(Optional.of(testUser));

        assertEquals((userService.getUser("user")), testUser);
    }

    @Test
    public void testUpdateUser_User_CalledMethodSave() {
        User testUser = getTestUser();
        Mockito.when(userCrudRepository.existsById("user")).thenReturn(true);

        userService.updateUser(testUser);

        Mockito.verify(userCrudRepository, times(1)).save(testUser);
    }

    @Test
    public void testGetUser_UserName_ThrowEntityNotFoundException() {
        Mockito.when(userCrudRepository.findById("user")).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.getUser("user");
        });

        String expectedMessage = "User with ID: user not found.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUpdateUser_User_ThrowEntityNotFoundException() {
        Mockito.when(userCrudRepository.existsById("user")).thenReturn(false);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.updateUser(getTestUser());
        });

        String expectedMessage = "User with ID: user not found.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    private User getTestUser() {
        return new User("user");
    }
}
