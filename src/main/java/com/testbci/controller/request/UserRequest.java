package com.testbci.controller.request;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

    private String name;
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$" , message = "Correo no válido")
    private String email;
    @Pattern(regexp = "^(?=.*[A-Z])(?=(.*\\d){2})(?=.*[a-z]).{8,12}$", message = "Contraseña no cumple con los requisitos")
    private String password;

    private List<PhonesRequest> phones;
}
