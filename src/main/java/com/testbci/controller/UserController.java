package com.testbci.controller;

import com.testbci.configuration.JwtUtil;
import com.testbci.controller.request.UserRequest;
import com.testbci.controller.response.UserResponse;
import com.testbci.exception.UserException;
import com.testbci.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping(path = "/sign-up", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> signUp(@Valid @RequestBody UserRequest user) throws UserException {

        UserResponse createdUser = userService.signUp(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> login(@RequestHeader("Authorization") String token,  @Valid @RequestBody UserRequest userRequest) throws UserException {
        String jwt = token.substring(7);
        String email = jwtUtil.extractUsername(jwt);

        if (!jwtUtil.validateToken(jwt, email)) {

           return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }

        UserResponse userFound = userService.login(userRequest);
        return new ResponseEntity<>(userFound, HttpStatus.OK);
    }
}
