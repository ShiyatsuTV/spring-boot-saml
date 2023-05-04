package com.shiyatsu.providers;

import java.util.ArrayList;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.shiyatsu.basic.CustomPasswordEncoder;

public class CustomInMemoryAuthenticationProvider implements AuthenticationProvider {

	private static final String internalWsUser = "internalWebServiceConsumer";
	private static final String internalWsPassword = CustomPasswordEncoder.encode("$2a$10$VxECUlRqJ2ugxCORcBGmceMc0p7/O43PyzsyTJwrRfSmACc1MPpIq");
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		try {
			final String name = authentication.getName();
            final String password = authentication.getCredentials().toString();
            if (doAuthenticate(name, password)) {
            	return new UsernamePasswordAuthenticationToken(name, password, new ArrayList<>());
            } else {
            	return null;
            }
            
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
	private boolean doAuthenticate(String name, String password) {
		if (name == null || password == null || name.isEmpty() || password.isEmpty()) {
			return false;
		}
		return (name.equals(internalWsUser) && CustomPasswordEncoder.matches(password, internalWsPassword));
	}

}
