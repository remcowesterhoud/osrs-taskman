package com.westerhoud.osrs.taskman.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Role {
    ADMIN,
    USER;

    public List<GrantedAuthority> getAuthorities() {
        return this == ADMIN
                ? Arrays.stream(Role.values())
                    .map(role -> new SimpleGrantedAuthority(role.toString()))
                    .collect(Collectors.toList())
                :
                    List.of(new SimpleGrantedAuthority(USER.toString()));
    }
}
