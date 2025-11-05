package com.sampleDataBase.auth;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserNameProvider implements UserDetails {


    private  Users user;
    public UserNameProvider(Users user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(roles ->new SimpleGrantedAuthority("ROLE_"+roles))
                .toList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}



//| Method                      | Purpose                                                 |
//        | --------------------------- | ------------------------------------------------------- |
//        | `getUsername()`             | Returns the login identifier (username or email).       |
//        | `getPassword()`             | Returns the encrypted password.                         |
//        | `getAuthorities()`          | Returns user roles/permissions (used in authorization). |
//        | `isAccountNonExpired()`     | Checks if the account is still valid.                   |
//        | `isAccountNonLocked()`      | Checks if account is locked (after failed attempts).    |
//        | `isCredentialsNonExpired()` | Checks if password is still valid.                      |
//        | `isEnabled()`               | Checks if account is active.                            |

