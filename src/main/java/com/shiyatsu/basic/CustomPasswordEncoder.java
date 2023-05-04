package com.shiyatsu.basic;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomPasswordEncoder {

	private static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static final String encode(CharSequence rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public static final Boolean matches(CharSequence rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
	
}
