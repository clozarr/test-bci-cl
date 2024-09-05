package com.testbci.controller;

import com.testbci.configuration.JwtUtil;
import com.testbci.controller.request.UserRequest;
import com.testbci.controller.response.UserResponse;
import com.testbci.exception.UserException;
import com.testbci.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;


    @BeforeEach
    void setUp() {
        jwtUtil = mock(JwtUtil.class);
        methodArgumentNotValidException = mock(MethodArgumentNotValidException.class);
        userController = new UserController(userService,jwtUtil);
    }

    @Test
    void testSignUpSuccess() throws UserException {


        UserRequest userRequest = DataSet.getUserRequest();
        UserResponse userResponse = DataSet.getUserResponse();

        when(userService.signUp(any())).thenReturn(userResponse);

        // Llamar al método
        ResponseEntity<UserResponse> response = userController.signUp(userRequest);

        // Verificar que la respuesta sea correcta
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(UUID.fromString("3571b848-dff6-4461-bfb9-a100b9b513c6"), Objects.requireNonNull(response.getBody()).getId());
        assertEquals("dummy-token-testing", Objects.requireNonNull(response.getBody()).getToken());
        assertEquals(Boolean.TRUE, Objects.requireNonNull(response.getBody().getIsActive()));
        
        // Verificar que se haya llamado al servicio
        verify(userService, times(1)).signUp(any(UserRequest.class));

    }

    @Test
    void testLoginSuccess() throws UserException {

        //Data
        String token = "dummy-token-testing";
        String email = "dummy@martinez.org";
        UserRequest userRequest = DataSet.getUserRequestLogin();
        UserResponse userResponseFull = DataSet.getUserResponseFull();


        when(jwtUtil.extractUsername(any())).thenReturn(email);
        when(jwtUtil.validateToken(any(),any())).thenReturn(Boolean.TRUE);
        when(userService.login(userRequest)).thenReturn(userResponseFull);

        ResponseEntity<UserResponse> response = userController.login(token,userRequest);

        // Verificar que la respuesta sea correcta
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(UUID.fromString("3571b848-dff6-4461-bfb9-a100b9b513c6"), Objects.requireNonNull(response.getBody()).getId());
        assertEquals("dummy-token-testing", Objects.requireNonNull(response.getBody()).getToken());
        assertEquals(Boolean.TRUE, Objects.requireNonNull(response.getBody().getIsActive()));
        assertEquals("Dummy Martinez", Objects.requireNonNull(response.getBody().getName()));
        assertEquals("dummy@martinez.org", Objects.requireNonNull(response.getBody().getEmail()));
        assertEquals("Hunter123*", Objects.requireNonNull(response.getBody().getPassword()));
        assertFalse(Objects.requireNonNull(response.getBody()).getPhones().isEmpty());


        verify(userService, times(1)).login(any(UserRequest.class));
        verify(jwtUtil, times(1)).extractUsername(any());
        verify(jwtUtil, times(1)).validateToken(any(),any());

    }

    @Test
    void testLoginFailInvalidToken() throws UserException {

        //Data
        String token = "invalid-token-testing";
        String email = "dummy2@martinez.org";
        UserRequest userRequest = DataSet.getUserRequestLogin();

        when(jwtUtil.extractUsername(any())).thenReturn(email);
        when(jwtUtil.validateToken(any(),any())).thenReturn(Boolean.FALSE);

        ResponseEntity<UserResponse> response = userController.login(token,userRequest);
        assertEquals(403, response.getStatusCodeValue());

        verify(jwtUtil, times(1)).extractUsername(any());
        verify(jwtUtil, times(1)).validateToken(any(),any());

    }
}