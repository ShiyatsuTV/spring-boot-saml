package com.shiyatsu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.saml2.core.Saml2X509Credential;
import org.springframework.security.saml2.provider.service.authentication.OpenSaml4AuthenticationProvider;
import org.springframework.security.saml2.provider.service.authentication.Saml2Authentication;
import org.springframework.security.saml2.provider.service.registration.InMemoryRelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistration;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrations;
import org.springframework.security.web.SecurityFilterChain;

import com.shiyatsu.providers.CustomInMemoryAuthenticationProvider;
import com.shiyatsu.providers.CustomLDAPAuthenticationProvider;
import com.shiyatsu.providers.DerbyAuthenticationProvider;

import static org.springframework.security.config.Customizer.withDefaults;

import java.io.File;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.security.x509.X509Support;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = {
	"com.shiyatsu.*",
})
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests().requestMatchers(HttpMethod.POST, "/loginprocess").permitAll()
			.and()
			.authorizeHttpRequests().requestMatchers(HttpMethod.GET, "/home").permitAll()
			.and()
			.authorizeHttpRequests().requestMatchers("/login").permitAll()
			.and()
			.authorizeHttpRequests().requestMatchers(HttpMethod.GET, "/public/users").permitAll()
			.and()
			.authorizeHttpRequests().requestMatchers(HttpMethod.GET, "/private/users").authenticated()
			.anyRequest().authenticated()
			.and()
			.saml2Login(
					saml2 -> saml2.authenticationManager(samlAuthenticationManager())
			)
			.httpBasic(
			)
			.and()
			.formLogin(
					form -> form.loginProcessingUrl("/loginprocess")
			)
			.authenticationManager(authenticationManager());
			
		return http.build();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
			.withUser("user").password("{noop}password").roles("USER")
			.and()
			.withUser("admin").password("{noop}password").roles("USER", "ADMIN");
	}
	
	@Bean
	public RelyingPartyRegistrationRepository relyingPartyRegistrations() throws Exception {
		ClassLoader classLoader = getClass().getClassLoader();
		File verificationKey = new File(classLoader.getResource("saml-certificate/ping.crt").getFile());
	    X509Certificate certificate = X509Support.decodeCertificate(verificationKey);
	    Saml2X509Credential credential = Saml2X509Credential.verification(certificate);
	    RelyingPartyRegistration registration = RelyingPartyRegistrations
	    		.fromMetadataLocation("https://auth.pingone.eu/63c07228-d053-4725-964c-8b04ba808971/saml20/metadata/7b6997b6-1f4e-4377-a5a2-c744831ff595")
	    		.registrationId("okta")
	    		.entityId("SpringSecuritySamlEntityID")
	    		.assertionConsumerServiceLocation("http://localhost:8080/login/saml2/sso/{registrationId}")
	    		.assertingPartyDetails(party -> party
		                .entityId("https://auth.pingone.eu/63c07228-d053-4725-964c-8b04ba808971")
		                .singleSignOnServiceLocation("https://auth.pingone.eu/63c07228-d053-4725-964c-8b04ba808971/saml20/idp/sso")
		                .wantAuthnRequestsSigned(false)
		                .verificationX509Credentials(c -> c.add(credential))
		            )
	    		.build();
	    return new InMemoryRelyingPartyRegistrationRepository(registration);
	}
	
	@Bean(name = "samlAuthenticationProvider")
	public OpenSaml4AuthenticationProvider samlAuthenticationProvider() {
		OpenSaml4AuthenticationProvider authenticationProvider = new OpenSaml4AuthenticationProvider();
		authenticationProvider.setAssertionValidator(OpenSaml4AuthenticationProvider.createDefaultAssertionValidator());
		authenticationProvider.setResponseAuthenticationConverter(OpenSaml4AuthenticationProvider.createDefaultResponseAuthenticationConverter());
        return authenticationProvider;
	}

	@Bean 
	public AuthenticationManager samlAuthenticationManager() {
		final List<AuthenticationProvider> providers = new ArrayList<>();
		providers.add(samlAuthenticationProvider());
		return new ProviderManager(providers);
	}
	
	@Bean
	public AuthenticationManager authenticationManager() {
		final List<AuthenticationProvider> providers = new ArrayList<>();
		providers.add(new CustomLDAPAuthenticationProvider());
		providers.add(new CustomInMemoryAuthenticationProvider());
		providers.add(new DerbyAuthenticationProvider());
		return new ProviderManager(providers);
	}
	
}
