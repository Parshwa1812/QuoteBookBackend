package com.sunbeam.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.sunbeam.Pojos.*;
import com.sunbeam.Repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository; // Define a UserRepository for accessing user data

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Load user details from your data source (e.g., database) based on the username
        UserPojo user = userRepository.findByEmail(username).get();

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // Create a UserDetails object using the retrieved user data
        return User.builder()
            .username(user.getEmail())
            .password(user.getPassword())
            .roles("USER") // You can define user roles/authorities here
            .build();
    }
}
