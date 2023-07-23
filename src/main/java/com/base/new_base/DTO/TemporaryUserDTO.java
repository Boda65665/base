package com.base.new_base.DTO;

import com.base.new_base.Entiti.Role;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class TemporaryUserDTO {
    @Size(min = 5,max = 32,message = "Size ps min 5 max 32")
    String password;

    @Email(message = "неверный формат почты")@Size(min = 1,message = "email не должен быть пустым")
    String email;
    @NotNull(message = "Поле не должно быть пустым")

    String login;
    String keyForConfirmEmail;
    Role role;
    int id;
}
