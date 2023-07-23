package com.base.new_base.configs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication()
@ComponentScan("com.base.new_base")
@EnableJpaRepositories("com.base.new_base.Repositories")
@EntityScan("com.base.new_base.Entiti")
public class NewBaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewBaseApplication.class, args);
    }

}
