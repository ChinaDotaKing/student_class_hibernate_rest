package org.example.model.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminClassManaResp {

    String course_name;
    String course_code;
    String department_name;
    String school_name;
    String semester_name;
    boolean status;

    int curEnrollmentNumber;

    int capacity;

}
