package org.example.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@Component
public abstract class Course {

    private int id;
    private String course_name;
    private String course_code;
    private int dept_id;
    private String description;
    public Course(){

    }
    public Course(String course_name, String course_code, int dept_id, String description) {
        this.course_name = course_name;
        this.course_code = course_code;
        this.dept_id = dept_id;
        this.description = description;
    }
}
