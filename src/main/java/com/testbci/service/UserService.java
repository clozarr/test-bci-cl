package com.testbci.service;

import com.testbci.controller.request.UserRequest;
import com.testbci.controller.response.UserResponse;
import com.testbci.exception.UserException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

public interface UserService {

      UserResponse signUp(UserRequest userRequest) throws DataIntegrityViolationException;
      UserResponse login (UserRequest userRequest) throws UserException;

      Optional<UserResponse> findByEmail(String email) throws UserException;

}
