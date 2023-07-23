package com.base.new_base.Service.User;

import com.base.new_base.Converters.TemporaryUserConverter;
import com.base.new_base.DTO.TemporaryUserDTO;
import com.base.new_base.DTO.UserDTO;
import com.base.new_base.Entiti.TemporaryUser;
import com.base.new_base.Entiti.User;
import com.base.new_base.Repositories.TemporaryUserRepository;
import org.springframework.stereotype.Service;

@Service
public class TemporaryUserService {
    final TemporaryUserRepository repository;
    final TemporaryUserConverter converter = new TemporaryUserConverter();

    public TemporaryUserService(TemporaryUserRepository repository) {
        this.repository = repository;
    }
    public TemporaryUserDTO findByKey(String code){
        TemporaryUser user = repository.findByKeyForConfirmEmail(code);
        if(user==null) return null;
        return converter.InDTO(user);
    }
    public void save(TemporaryUserDTO userDTO){
        TemporaryUser user = converter.InTemporaryUser(userDTO);
        repository.save(user);
    }
    public void deleteByID(int id){
        repository.deleteById(id);

    }
    public void update(TemporaryUserDTO userDTO){
        TemporaryUser user = converter.InTemporaryUser(userDTO);
        repository.save(user);
    }


    public TemporaryUserDTO findByEmail(String email) {
        TemporaryUser user = repository.findTemporaryUserByEmail(email);
        if (user==null) return null;
        return converter.InDTO(user);
    }
}
