package com.base.new_base.Service.User;

import com.base.new_base.DTO.TemporaryUserDTO;
import com.base.new_base.DTO.UserDTO;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    final UserService userService;
    final TemporaryUserService temporaryUserService;

    public UserDetailServiceImpl(UserService userService, TemporaryUserService temporaryUserService) {
        this.userService = userService;
        this.temporaryUserService = temporaryUserService;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String password = "";
        Set<SimpleGrantedAuthority> authorities = null;
        if (temporaryUserService.findByEmail(email)!=null){
            TemporaryUserDTO userDTO = temporaryUserService.findByEmail(email);
            password=userDTO.getPassword();
            authorities=userDTO.getRole().getAuthorities();

        } else if (userService.findByEmail(email)!=null) {
            UserDTO userDTO = userService.findByEmail(email);
            password=userDTO.getPassword();
            authorities=userDTO.getRole().getAuthorities();

        }
        else throw  new UsernameNotFoundException("user not found");
        return new org.springframework.security.core.userdetails.User(
                email,
                password,
                authorities
        );
    }
}
