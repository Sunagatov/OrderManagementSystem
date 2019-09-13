package com.zufar.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${service.credentials.admin-login}")
    private String ADMIN_LOGIN;
    @Value("${service.credentials.admin-password}")
    private String ADMIN_PASSWORD;
    @Value("${service.credentials.user-login}")
    private String USER_LOGIN;
    @Value("${service.credentials.user-password}")
    private String USER_PASSWORD;
    @Value("${service.credentials.admin-role}")
    private String ADMIN_ROLE;
    @Value("${service.credentials.user-role}")
    private String USER_ROLE;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public void configureUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> detailsManagerConfigurer = auth.inMemoryAuthentication();

        final String adminEncryptedPassword = passwordEncoder.encode(ADMIN_PASSWORD);
        UserDetails adminDetails = User.withUsername(ADMIN_LOGIN).password(adminEncryptedPassword).roles(ADMIN_ROLE).build();
        detailsManagerConfigurer.withUser(adminDetails);

        final String userEncryptedPassword = passwordEncoder.encode(USER_PASSWORD);
        UserDetails userDetails = User.withUsername(USER_LOGIN).password(userEncryptedPassword).roles(USER_ROLE).build();
        detailsManagerConfigurer.withUser(userDetails);
    }
    
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().anyRequest().authenticated();
        http.httpBasic().authenticationEntryPoint(authenticationEntryPoint);
    }
}

