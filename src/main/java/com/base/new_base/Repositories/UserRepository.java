package com.base.new_base.Repositories;

import com.base.new_base.Entiti.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findAll();
    User findById(int id);
    User findUserByEmail(String email);
    Optional<User> findByEmail(String email);
    User findByKeyForConfirmEmail(String key);

}
