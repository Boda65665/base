package com.base.new_base.DTO;

import com.base.new_base.Entiti.Role;
import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserDTO {
    int id;
    String password;
    String email;
    Role role;
    String login;
    String keyForConfirmEmail;


}
