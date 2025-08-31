package com.standingash.jettystick.orm;

import com.standingash.jettystick.core.ApplicationContext;
import com.standingash.jettystick.orm.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class OrmTest {

    // commonly given
    private final ApplicationContext context = new ApplicationContext(
            "com.standingash.jettystick"
    );

    @Test
    public void testSaveAndDelete() {
        // when
        User.objects.save(new User("name 1", "id_1"));
        User.objects.save(new User("name 2", "id_2"));
        List<User> allUsers = User.objects.findAll();

        // then
        System.out.println("testing findAll() ...");
        for (User user : allUsers) {
            System.out.println(user.getName());
            System.out.println(user.getUserId());
        }

        for (User user : allUsers) {
            User.objects.delete(user);
        }
        allUsers = User.objects.findAll();
        Assertions.assertEquals(0, allUsers.size(), "delete() failed");
    }

    @Test
    public void testFindBy() {
        // when
        User.objects.save(new User("name 1", "id_1"));
        User.objects.save(new User("name 2", "id_2"));
        List<User> nameUsers = User.objects.findByName("name 1");
        List<User> nameAndIdUsers = User.objects.findByNameAndUserId("name 2", "id_2");
        List<User> nameOrIdUsers = User.objects.findByNameOrUserId("name 1", "id_2");

        // then
        System.out.println("testing findByName() ...");
        for (User user : nameUsers) {
            System.out.println(user.getName());
        }

        System.out.println("testing findByNameAndUserId() ...");
        for (User user : nameAndIdUsers) {
            System.out.println(user.getName());
        }

        System.out.println("testing findByNameOrUserId() ...");
        for (User user : nameOrIdUsers) {
            System.out.println(user.getName());
        }
    }
}
