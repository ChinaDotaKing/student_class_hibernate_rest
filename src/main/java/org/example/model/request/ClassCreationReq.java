package org.example.model.request;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
public class ClassCreationReq {


    @Min(value = 1, message = "course_id must be greater than 0")
    int course_id;
    @Min(value = 1, message = "classroom_id must be greater than 0")
    int classroom_id;
    @Min(value = 1, message = "department_id must be greater than 0")
    int department_id;
    @Min(value = 1, message = "lecture_id must be greater than 0")
    int lecture_id;
    @Min(value = 1, message = "semester_id must be greater than 0")
    int semester_id;
    @Min(value = 1, message = "professor_id must be greater than 0")
    int professor_id;
    @Min(value = 1, message = "capacity_id must be greater than 0")
    int capacity;
}
