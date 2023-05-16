package org.example.model.request;


import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSignupRequestModel {

    String username;

    @Size(min = 8, max = 16, message = "password too long or short")
    String password;

    @NonNull
    @Size(min=1,max=15)
    String firstname;
    @Size(min=1,max=15)
    String lastname;

    @Size(min=1,max=15)
    String email;

    @Min(value = 1, message = "department_id must be greater than 0")
    int department_id;

}
