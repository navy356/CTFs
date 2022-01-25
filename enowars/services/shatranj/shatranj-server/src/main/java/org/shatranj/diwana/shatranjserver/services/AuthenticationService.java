package org.shatranj.diwana.shatranjserver.services;

import org.shatranj.diwana.shatranjserver.model.User;
import org.shatranj.diwana.shatranjserver.model.repositories.UserRepository;
import org.shatranj.diwana.shatranjserver.security.CustomBCryptPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private UserRepository userRepository;
    private CustomBCryptPasswordEncoder passwordEncoder;


    private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new CustomBCryptPasswordEncoder();
    }

    public User getUserFromCredentials(String username, String password) {
        long startTime = System.currentTimeMillis();
        var users = userRepository.findAllByUsernameAndPassword(username, passwordEncoder.encode(password));
        long estimatedTime = System.currentTimeMillis() - startTime;


        if (users.isEmpty()) {
            return null;
        } else {
            return users.stream().findAny().get();
        }
    }
}
