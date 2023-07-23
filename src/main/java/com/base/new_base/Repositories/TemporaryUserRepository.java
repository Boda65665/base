package com.base.new_base.Repositories;

import com.base.new_base.DTO.TemporaryUserDTO;
import com.base.new_base.Entiti.TemporaryUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TemporaryUserRepository extends JpaRepository<TemporaryUser,Integer> {
    TemporaryUser findByKeyForConfirmEmail(String code);
    TemporaryUser findTemporaryUserByEmail(String email);
    @Transactional
    @Modifying @Query(value = "UPDATE temporary_users SET email = :newEmail WHERE email = :oldEmail",nativeQuery = true)
    void update(@Param("newEmail") String oldEmail, @Param("oldEmail") String newEmail);

}
