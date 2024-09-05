package com.testbci.service.impl;

import com.testbci.configuration.JwtUtil;
import com.testbci.controller.request.UserRequest;
import com.testbci.controller.response.PhoneResponse;
import com.testbci.controller.response.UserResponse;
import com.testbci.entity.PhoneEntity;
import com.testbci.entity.UserEntity;
import com.testbci.exception.UserException;
import com.testbci.repository.UserRepository;
import com.testbci.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Validator validator;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse signUp(UserRequest userRequest)  throws DataIntegrityViolationException, ConstraintViolationException {

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
                .token(jwtUtil.generateToken(userRequest.getEmail()))
                .phones(userRequest.getPhones()
                        .stream()
                        .map(phonesRequest -> PhoneEntity.builder()
                                .number(phonesRequest.getNumber())
                                .cityCode(phonesRequest.getCityCode())
                                .countryCode(phonesRequest.getCountryCode())
                                .build())
                        .collect(Collectors.toList()))
                .build();


        UserEntity userEntity = userRepository.save(newUserEntity);
        return toResponse(userEntity);

    }


    @Override
    public UserResponse login(UserRequest userRequest) throws UserException {


        Optional<UserResponse> optionalUserResponse =  userRepository
                .findByEmail(userRequest.getEmail())
                .filter(userEntity -> passwordEncoder.matches(userRequest.getPassword(),userEntity.getPassword()))
                .map(this::toResponseFull);

        return optionalUserResponse.
                orElseThrow(() -> new com.testbci.exception.UserException("Usuario no encontrado", HttpStatus.NOT_FOUND));


    }


    private UserResponse toResponse(UserEntity userEntity){


     return   UserResponse.builder()
                .id(userEntity.getId())
                .created(userEntity.getCreated())
                .lastLogin(userEntity.getLastLogin())
                .token(userEntity.getToken())
                .isActive(userEntity.getIsActive())
                .build();

    }

    private UserResponse toResponseFull(UserEntity userEntity){

        List<PhoneResponse> phoneResponseList = userEntity.getPhones()
                .stream().map(phoneEntity -> PhoneResponse.builder()
                        .number(phoneEntity.getNumber())
                        .cityCode(phoneEntity.getCityCode())
                        .countryCode(phoneEntity.getCountryCode())
                        .build()).collect(Collectors.toList());


     return   UserResponse.builder()
                .id(userEntity.getId())
                .created(userEntity.getCreated())
                .lastLogin(userEntity.getLastLogin())
                .token(userEntity.getToken())
                .isActive(userEntity.getIsActive())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
             .phones(phoneResponseList)
             .build();

    }

}
