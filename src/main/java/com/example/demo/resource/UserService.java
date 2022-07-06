package com.example.demo.resource;

import com.example.demo.model.User;
import com.example.demo.repository.UserCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class UserService {
    private final UserCrudRepository userCrudRepository;

    @Autowired
    public UserService(UserCrudRepository userCrudRepository) {
        this.userCrudRepository = userCrudRepository;
    }

    public void addUser(User user) {
        userCrudRepository.save(user);
    }

    public User getUser(String userName) throws EntityNotFoundException {
        return userCrudRepository.findById(userName).orElseThrow(() ->
                new EntityNotFoundException("User with ID: " + userName + " not found."));
    }

    public void updateUser(User user) throws EntityNotFoundException {
        if (userCrudRepository.existsById(user.getUserName())) {
            userCrudRepository.save(user);
        } else {
            throw new EntityNotFoundException("User with ID: " + user.getUserName() + " not found.");
        }
    }
}
