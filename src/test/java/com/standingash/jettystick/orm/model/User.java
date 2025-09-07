package com.standingash.jettystick.orm.model;

import com.standingash.jettystick.orm.annotations.Model;

import java.util.List;

@Model
public class User {

    private String name;
    private String userId;

    public static UserRepository repository;

    public User(String name, String userId) {
        this.name = name;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public interface UserRepository {
        void save(User user);
        void delete(User user);
        List<User> findAll();
        List<User> findByName(String name);
        User findByNameAndUserId(String name, String userId);
        List<User> findByNameOrUserId(String name, String userId);
    }
}
