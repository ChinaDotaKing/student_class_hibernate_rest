package org.example.model.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseRespModel {

    String course_name;
    String course_code;
    String department_name;
    String school_name;

    String course_description;
    int capacity;
    int enrollment_num;
    boolean is_Active;

}
