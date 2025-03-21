package com.task.service.impl;

import com.task.entities.Users;
import com.task.exception.UserAlreadyExistsException;
import com.task.exception.UserNotFoundCustomException;
import com.task.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<Users> optionalUsers = usersRepository.findByEmail(username);
        if (!optionalUsers.isPresent()) {
            throw new UserNotFoundCustomException("User not found ! " + username);
        }
        Users users = optionalUsers.get();

        List<GrantedAuthority> authorities = users.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                users.getEmail(),
                users.getPassword(),
                authorities
        );
    }
}
