package com.base.new_base.Forms;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class EditEmailForm {
    String password;
    @Email(message = "неверный формат почты")@Size(min = 1,message = "email не должен быть пустым")
    String email;
}
