package org.example.model.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseCreationRequestModel {

    String course_name;

    String course_code;
    String description;


    int department_id;

}
