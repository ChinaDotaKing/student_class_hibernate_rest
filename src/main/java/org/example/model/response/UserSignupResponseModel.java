package org.example.model.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSignupResponseModel {

    String username;
    String firstname;
    String lastname;
    String email;
    String department_name;
    String school;
}
