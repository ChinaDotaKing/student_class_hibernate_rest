package org.example.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminStuResp {

    String full_name;
    String email;
    String department_name;
    String school;
    boolean is_Active;
}
