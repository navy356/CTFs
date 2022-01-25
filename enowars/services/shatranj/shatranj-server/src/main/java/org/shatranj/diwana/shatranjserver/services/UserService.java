package org.shatranj.diwana.shatranjserver.services;

import org.shatranj.diwana.shatranjserver.model.User;
import org.shatranj.diwana.shatranjserver.model.repositories.UserRepository;
import org.shatranj.diwana.shatranjserver.security.CustomBCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(final String username, final String password) {
        var user = new User();
        user.setUsername(username);
        user.setPassword((new CustomBCryptPasswordEncoder()).encode(password));
        user.setActive(true);
        user.setRoles("USER");
        userRepository.save(user);
    }

    public Optional<User> getUserFromId(final Integer id) {
        var opUser = userRepository.findById(id);
        return opUser;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
