package com.base.new_base.Converters;

import com.base.new_base.DTO.UserDTO;
import com.base.new_base.Entiti.User;

public class UserConverters {
    public UserDTO InDTO(User user){
        UserDTO user_convert = new UserDTO();
        user_convert.setId(user.getId());
        user_convert.setRole(user.getRole());
        user_convert.setEmail(user.getEmail());
        user_convert.setLogin(user.getLogin());
        user_convert.setPassword(user.getPassword());
        user_convert.setKeyForConfirmEmail(user.getKeyForConfirmEmail());

        return user_convert;

    }
    public User InUser(UserDTO user){
        User user_convert = new User();
        user_convert.setRole(user.getRole());
        user_convert.setId(user.getId());

        user_convert.setEmail(user.getEmail());
        user_convert.setLogin(user.getLogin());
        user_convert.setKeyForConfirmEmail(user.getKeyForConfirmEmail());
        user_convert.setPassword(user.getPassword());
        return user_convert;
    }
}
