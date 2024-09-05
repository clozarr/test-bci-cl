package com.testbci.controller;

import com.testbci.controller.request.PhonesRequest;
import com.testbci.controller.request.UserRequest;
import com.testbci.controller.response.PhoneResponse;
import com.testbci.controller.response.UserResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class DataSet {


    public static UserRequest getUserRequest(){

       return UserRequest.builder()
                .name("Dummy Martinez")
                .email("dummy@martinez.org")
                .password("Hunter123*")
                .phones(List.of(PhonesRequest.builder()
                        .number(74528L)
                        .cityCode(4)
                        .countryCode("57")
                        .build()))
                .build();
    }

    public static UserResponse getUserResponse(){
        return  UserResponse.builder()
                .id(UUID.fromString("3571b848-dff6-4461-bfb9-a100b9b513c6"))
                .created(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .token("dummy-token-testing")
                .isActive(Boolean.TRUE)
                .build();

    }

    public static UserResponse getUserResponseFull(){

        return  UserResponse.builder()
                .id(UUID.fromString("3571b848-dff6-4461-bfb9-a100b9b513c6"))
                .created(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .token("dummy-token-testing")
                .isActive(Boolean.TRUE)
                .name("Dummy Martinez")
                .email("dummy@martinez.org")
                .password("Hunter123*")
                .phones(List.of(PhoneResponse.builder()
                        .number(74528L)
                        .cityCode(4)
                        .countryCode("57")
                        .build()))
                .build();

    }

    public static UserRequest getUserRequestLogin(){

        return UserRequest.builder()
                .email("dummy@martinez.org")
                .password("Hunter123*")
                .build();
    }
}
