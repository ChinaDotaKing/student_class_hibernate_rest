package org.example.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserLoginRequestModel {

    @Size(min = 2, max = 10, message = "name is too long or short")
    String username;

    @Size(min = 8, max = 16, message = "password too long or short")
    String password;
}
