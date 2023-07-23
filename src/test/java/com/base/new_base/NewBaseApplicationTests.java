package com.base.new_base;

import com.base.new_base.JWT.jwtProvider;
import com.base.new_base.Service.User.UserService;
import com.base.new_base.configs.NewBaseApplication;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import java.security.NoSuchAlgorithmException;

@SpringBootTest(classes = {NewBaseApplication.class})
class NewBaseApplicationTests {
    @Autowired
    UserService service;
    @Autowired
    jwtProvider provider;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Test
    void contextLoads() throws NoSuchAlgorithmException {
        System.out.println(passwordEncoder.encode("Boda1006"));
        String hash = "35454B055CC325EA1AF2126E27707052";
        String password = "ILoveJava";

        String md5Hex = DigestUtils
                .md5Hex(password).toUpperCase();


        Assert.isTrue(hash.equals(md5Hex));

    }

}
