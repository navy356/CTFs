package org.shatranj.diwana.shatranjserver.services;

import org.shatranj.diwana.shatranjserver.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    UserRepository userRepository;

    @Autowired
    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //var opUser = userRepository.findByUsername(username);
        //opUser.orElseThrow(() -> new UsernameNotFoundException("Couldn't find " + username + "."));
        return null; //opUser.map(MyUserDetails::new).get();
    }
}
