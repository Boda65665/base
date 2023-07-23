package com.base.new_base.Service;

import com.base.new_base.DTO.TemporaryUserDTO;
import com.base.new_base.Service.User.TemporaryUserService;
import com.base.new_base.configs.NewBaseApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest(classes = {NewBaseApplication.class})

class TemporaryUserServiceTest {
    @Autowired
    TemporaryUserService temporaryUserService;
    @Test
    void deleteByID() {
        temporaryUserService.deleteByID(39);
        Assert.isTrue(temporaryUserService.findByEmail("harasukb@gmail.com")==null);

    }
    @Test
    void update(){
        TemporaryUserDTO temporaryUserDTO = temporaryUserService.findByEmail("harasuk@gmail.com");
        temporaryUserDTO.setEmail("harasuka@gmail.com");
        temporaryUserService.update(temporaryUserDTO);
        temporaryUserDTO = temporaryUserService.findByEmail("harasuka@gmail.com");
        Assert.isTrue(temporaryUserDTO!=null);
    }
}