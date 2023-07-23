package com.base.new_base.Forms;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class RestorePasswordForm {
    @Size(min = 5,max = 32,message = "Size ps min 5 max 32")
    String password;
    String password_again;
}
