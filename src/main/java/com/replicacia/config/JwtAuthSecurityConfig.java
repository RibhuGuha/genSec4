package com.replicacia.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.replicacia.model.Role;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class JwtAuthSecurityConfig extends WebSecurityConfigurerAdapter {

	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final UserDetailsService jwtUserDetailsService;
	private final JwtRequestFilter jwtRequestFilter;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// configure AuthenticationManager so that it knows from where to load
		// user for matching credentials
		// Use BCryptPasswordEncoder
		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {

		httpSecurity.headers().frameOptions().disable();
		httpSecurity.cors();
		httpSecurity.csrf().disable()// disabled cors and csrf
				// Only admin can perform HTTP delete operation
				// .authorizeRequests().antMatchers(HttpMethod.DELETE).hasRole(Role.ADMIN)
				// any authenticated user can perform all other operations
				.authorizeRequests().antMatchers("/app/product/**").hasAnyAuthority(
						Role.PRODUCT_READ, Role.PRODUCT_UPDATE, Role.PRODUCT_WRITE, Role.PRODUCT_DELETE,
						Role.PRODUCT_LIST)
				.and().authorizeRequests().antMatchers("/app/customer/**").hasAnyAuthority(
						Role.CUSTOMER_READ, Role.CUSTOMER_UPDATE, Role.CUSTOMER_WRITE, Role.CUSTOMER_DELETE,
						Role.CUSTOMER_LIST)
				.and().authorizeRequests().antMatchers("/app/user/**").hasAnyAuthority(
						Role.CUSTOMER_READ, Role.CUSTOMER_UPDATE, Role.CUSTOMER_WRITE, Role.CUSTOMER_DELETE,
						Role.CUSTOMER_LIST)
/*				.and().authorizeRequests().antMatchers("/app/role/**").hasAnyAuthority(
						Role.USER_READ, Role.USER_WRITE, Role.USER_UPDATE, Role.USER_DELETE,
						Role.USER_LIST)*/
				// Permit all other request without authentication
				.and().authorizeRequests().anyRequest().permitAll()
				// Reject every unauthenticated request and send error code 401.
				.and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
				// We don't need sessions to be created.
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// Add a filter to validate the tokens with every request
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
}