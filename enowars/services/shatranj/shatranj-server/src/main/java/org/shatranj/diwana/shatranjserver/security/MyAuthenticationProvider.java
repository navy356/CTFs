package org.shatranj.diwana.shatranjserver.security;

import org.shatranj.diwana.shatranjserver.services.AuthenticationService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Collections;

public class MyAuthenticationProvider implements AuthenticationProvider {
    private final AuthenticationService authenticationService;

    public MyAuthenticationProvider(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        var user = authenticationService.getUserFromCredentials(username, password);

        if (user != null) {
            return new UsernamePasswordAuthenticationToken
                    (user.getId(), user.getPassword(), Collections.emptyList());
        } else {
            throw new
                BadCredentialsException("External system authentication failed");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
