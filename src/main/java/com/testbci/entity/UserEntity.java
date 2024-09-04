package com.testbci.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    private String token;
    private Boolean isActive;
    private LocalDateTime created;
    private LocalDateTime lastLogin;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PhoneEntity> phones;


}