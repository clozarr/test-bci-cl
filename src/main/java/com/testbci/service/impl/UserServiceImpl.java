package com.testbci.service.impl;

import com.testbci.controller.request.UserRequest;
import com.testbci.controller.response.UserResponse;
import com.testbci.entity.UserEntity;
import com.testbci.exception.UserException;
import com.testbci.repository.UserRepository;
import com.testbci.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Validator validator;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserResponse signUp(UserRequest userRequest)  throws DataIntegrityViolationException {

        Set<ConstraintViolation<UserRequest>> violations = validator.validate(userRequest);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        UserEntity newUserEntity = UserEntity.builder()
                .id(UUID.randomUUID())
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .created(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .isActive(Boolean.TRUE)
                .token(generateToken(userRequest.getEmail()))
                .build();


        UserEntity userEntity = userRepository.save(newUserEntity);
        return toResponse(userEntity);

    }


    @Override
    public UserResponse login(UserRequest userRequest) throws UserException {

        Optional<UserResponse> optionalUserResponse = findByEmail(userRequest.getEmail());

        return optionalUserResponse.
                orElseThrow(() -> new com.testbci.exception.UserException("Usuario no encontrado", HttpStatus.NOT_FOUND));


    }

    public Optional<UserResponse> findByEmail(String email) {

       Optional<UserEntity> optionalUserEntity=  userRepository.findByEmail(email);
       return  optionalUserEntity.map(this::toResponse);
    }

    public String generateToken(String email) {

        return "dummyToken";
    }


    private UserResponse toResponse(UserEntity userEntity){


     return   UserResponse.builder()
                .id(userEntity.getId())
                .isActive(userEntity.getIsActive())
                .token(userEntity.getToken())
                .created(userEntity.getCreated())
                .lastLogin(userEntity.getLastLogin())
                .build();

    }

}
