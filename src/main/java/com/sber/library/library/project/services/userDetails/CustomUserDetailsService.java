package com.sber.library.library.project.services.userDetails;

import com.sber.library.library.project.model.User;
import com.sber.library.library.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Value("${spring.security.user.name}")
    private String adminUsername;
    @Value("${spring.security.user.password}")
    private String adminPassword;
    @Value("${spring.security.user.roles}")
    private String adminRole;
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.equals(adminUsername)) {
            return org.springframework.security.core.userdetails.User
                    .builder()
                    .username(adminUsername)
                    .password(adminPassword)
                    .roles(adminRole)
                    .build();
        } else {
            User user = userRepository.findByUserLogin(username);
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(user.getRole().getId() == 1L ? "ROLE_USER" : "ROLE_LIBRARIAN"));
            return new CustomUserDetails(user.getId().intValue(), username, user.getUserPassword(), authorities);
        }
    }
}
