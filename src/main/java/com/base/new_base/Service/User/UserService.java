package com.base.new_base.Service.User;

import com.base.new_base.Converters.UserConverters;
import com.base.new_base.DTO.UserDTO;
import com.base.new_base.Entiti.User;
import com.base.new_base.Repositories.TemporaryUserRepository;
import com.base.new_base.Repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService{
    final UserConverters userConverters = new UserConverters();
    final UserRepository userRepository;

    public UserService(UserRepository userRepository, TemporaryUserRepository temporaryUserRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO findById(int id){
        User user = userRepository.findById(id);
        return userConverters.InDTO(user);
    }
    public UserDTO findByEmail(String email){
        User user = userRepository.findUserByEmail(email);
        if (user==null) return null;
        return userConverters.InDTO(user);
    }
    public List<UserDTO> findAll(){
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOS = new ArrayList<>();
        for(User user: users){
            userDTOS.add(userConverters.InDTO(user));
        }
        return userDTOS;
    }
    public void save(UserDTO userDTO){
        User user = userConverters.InUser(userDTO);
        userRepository.save(user);

    }
    public void update(UserDTO userDTO){
        User user = userConverters.InUser(userDTO);
        userRepository.save(user);
    }

    public UserDTO findByKey(String key){
        User user = userRepository.findByKeyForConfirmEmail(key);
        if (user==null)return null;
        return userConverters.InDTO(user);
    }



}
