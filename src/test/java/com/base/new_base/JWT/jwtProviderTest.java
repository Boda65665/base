package com.base.new_base.JWT;

import com.base.new_base.DTO.UserDTO;
import com.base.new_base.Entiti.Role;
import com.base.new_base.configs.NewBaseApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SpringBootTest(classes = {NewBaseApplication.class})

class jwtProviderTest {
    final jwtProvider jwtProvider;
    @Autowired
    jwtProviderTest(com.base.new_base.JWT.jwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }


//    @Test
//    String generateToken() {
//        UserDTO userDTO = new UserDTO();
//        userDTO.setPassword("DDDD");
//        userDTO.setLogin("dddedd");
//        userDTO.setEmail("harasukb@gmail.com");
//
////        String token = jwtProvider.generateToken(userDTO.getEmail(),userDTO.getRole());
//
////        Assert.notNull(token);
////        return token;
//
//    }

//    @Test
//    void validateToken() {
//        String token = generateToken();
//        System.out.println(token);
//        System.out.println(token.equals("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoYXJhc3VrYkBnbWFpbC5jb20iLCJpYXQiOjE2ODk0MjM0MTksImV4cCI6MTY4OTQyNTIxOX0.NYXU2ge2X6tyF6ILEs9umMRIjDM2NlAm1-xfgdApzgo"));
//        Assert.isTrue(jwtProvider.validateToken(token+"s"));
//    }

    @Test
    void getRole(HttpServletRequest request) {
        System.out.println(jwtProvider.getRole(jwtProvider.resolveToken(request)));
        Assert.isTrue(jwtProvider.getRole(jwtProvider.resolveToken(request))== Role.UNVERIFIED);
    }
}