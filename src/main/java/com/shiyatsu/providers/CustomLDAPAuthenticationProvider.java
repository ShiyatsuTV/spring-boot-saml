package com.shiyatsu.providers;

import java.util.ArrayList;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

public class CustomLDAPAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		try {
			final String name = authentication.getName();
            final String password = authentication.getCredentials().toString();
            
            if (doAuthenticate()) {
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

	private boolean doAuthenticate() {
		return false;
	}
	
}
