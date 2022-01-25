package org.shatranj.diwana.shatranjserver.security;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CustomBCryptPasswordEncoder extends BCryptPasswordEncoder {
    private final String SALT = "$2a$04$IYhdhc/32pjV4.BIEbk54u";

    @Override
    public String encode(CharSequence rawPassword) {
        return BCrypt.hashpw(rawPassword.toString(), SALT);
    }
}
