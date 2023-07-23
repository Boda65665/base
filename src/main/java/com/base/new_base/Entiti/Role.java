package com.base.new_base.Entiti;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;

public enum Role {
    USER(Set.of(Permission.USER)),//user
    ADMIN(Set.of(Permission.USER, Permission.ADMIN)),//user + admin
    UNVERIFIED(Set.of(Permission.UNVERIFIED));
    private final Set<Permission> permissions;
    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }
    public Set<SimpleGrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        for (Permission permission : getPermissions()) {
            authorities.add(new SimpleGrantedAuthority(permission.getPermission()));
        }
        return authorities;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}
