package com.base.new_base.Service;

import com.base.new_base.DTO.UserDTO;
import com.base.new_base.Service.User.UserService;
import com.base.new_base.configs.NewBaseApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest(classes = {NewBaseApplication.class})

class UserServiceTest {
    @Autowired
    UserService userService;




    @Test
    void update() {


        UserDTO userDTO = userService.findByEmail("harasukb@gmail.com");
        userDTO.setKeyForConfirmEmail("KOD");
        userService.update(userDTO);
        userDTO = userService.findByEmail("harasukb@gmail.com");
        Assert.isTrue(userDTO.getKeyForConfirmEmail()!=null);
    }
}