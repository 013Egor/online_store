package com.restauran.delivery.service;

import java.util.Iterator;

import com.restauran.delivery.entity.User;
import com.restauran.delivery.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserPrincipalService implements UserDetailsService {

    @Autowired
    PasswordEncoder passwordEncoder;

    UserRepository userRepository;

    public UserPrincipalService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = findByName(username);

        if (user == null) {
            throw new UsernameNotFoundException("Cannot find user with name: " + username);
        }

        UserPrincipal userPrincipal = new UserPrincipal(user);

        return userPrincipal;
    }

    private User findByName(String name) {
        
        Iterable<User> users = userRepository.findAll();
        Iterator<User> it = users.iterator();
        User tempUser = null;

        while (it.hasNext()) {
            tempUser = it.next();
            if (tempUser.getUsername().equals(name)) {
                return tempUser;
            }
        }

        return null;
    }

    public User saveUser(User user) {

        if (findByName(user.getUsername()) != null) {
            return null;
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles("ROLE_USER");
        user = userRepository.save(user);

        return user;
    }

    public User savePassword(int id, String password, String newPassword) {
        
        User user = userRepository.findById(id).orElseThrow();

        if (passwordEncoder.matches(password, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            user = userRepository.save(user);
            return user;
        } else {
            return null;
        }
    }

    public void delete(int id) {
        userRepository.deleteById(id);
    }
}
