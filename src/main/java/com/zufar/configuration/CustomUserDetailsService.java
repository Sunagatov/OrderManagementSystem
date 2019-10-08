package com.zufar.configuration;

import com.zufar.user.UserDTO;
import com.zufar.user.UserService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LogManager.getLogger(CustomUserDetailsService.class);

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public CustomUserDetailsService(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        final UserDTO user;
        try {
            user = userService.getByLogin(login);
        } catch (Exception exc) {
            final String errorMessage = "Loading user details is impossible.";
            LOGGER.error(exc.getMessage() + errorMessage, exc);
            throw new UsernameNotFoundException(errorMessage, exc);
        }
        return CustomUserDetails.builder()
                .username(user.getName())
                .password(passwordEncoder.encode(user.getPassword()))
                .authorities(user.getRoles())
                .credentialsNonExpired(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .enabled(true)
                .build();
    }
}
