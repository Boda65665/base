package com.base.new_base.Converters;

import com.base.new_base.DTO.TemporaryUserDTO;
import com.base.new_base.DTO.UserDTO;
import com.base.new_base.Entiti.TemporaryUser;
import com.base.new_base.Entiti.User;

public class TemporaryUserConverter {

    public TemporaryUserDTO InDTO( TemporaryUser user){
        TemporaryUserDTO user_convert = new TemporaryUserDTO();
        user_convert.setEmail(user.getEmail());
        user_convert.setLogin(user.getLogin());
        user_convert.setPassword(user.getPassword());
        user_convert.setRole(user.getRole());
        user_convert.setId(user.getId());


        user_convert.setKeyForConfirmEmail(user.getKeyForConfirmEmail());
        return user_convert;

    }
    public TemporaryUser InTemporaryUser( TemporaryUserDTO user){
        TemporaryUser user_convert = new TemporaryUser();
        user_convert.setEmail(user.getEmail());
        user_convert.setLogin(user.getLogin());
        user_convert.setId(user.getId());

        user_convert.setRole(user.getRole());

        user_convert.setPassword(user.getPassword());
        user_convert.setKeyForConfirmEmail(user.getKeyForConfirmEmail());
        return user_convert;
    }
}
