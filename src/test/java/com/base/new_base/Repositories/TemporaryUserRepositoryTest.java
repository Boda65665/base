package com.base.new_base.Repositories;

import com.base.new_base.Entiti.TemporaryUser;
import com.base.new_base.configs.NewBaseApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(classes = {NewBaseApplication.class})

class TemporaryUserRepositoryTest {
    @Autowired
    TemporaryUserRepository repository;

    @Test
    void save() {
        TemporaryUser user = new TemporaryUser();
//        user.setCode(32432);
        user.setLogin("deewdfw");
        user.setPassword("ddaewdw");
        user.setEmail("ddwdw");
        repository.save(user);
        Assert.notEmpty(repository.findAll());
    }
}