package com.restauran.delivery.repositories;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.restauran.delivery.entity.User;

@Service
public class DbInit implements CommandLineRunner {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public DbInit(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Iterable<User> all = userRepository.findAll();
        boolean adminExist = false;
        boolean customerExist = false;

        for (User user : all) {
            if (user.getUsername().equals("admin")){
                adminExist = true;
            }
            if (user.getUsername().equals("customer")){
                customerExist = true;
            }
        }

        if (adminExist == false) {
            User user = new User("admin", passwordEncoder.encode("password"), "ROLE_ADMIN");
            userRepository.save(user);
        }

        if (customerExist == false) {
            User user = new User("customer", passwordEncoder.encode("password"), "ROLE_CUSTOMER");
            userRepository.save(user);
        }
    }
}