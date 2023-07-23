package com.base.new_base.Entiti;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "login")
    private String login;
    @Column(name = "email_confirmation_key")
    private String keyForConfirmEmail;
    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private Role role;





}
