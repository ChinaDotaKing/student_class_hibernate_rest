package org.example.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;

@Getter
@Setter
@AllArgsConstructor
@Component
public abstract class WebRegClass {

    private int id;

    private int course_id;
    private int semester_id;
    private int professor_id;
    private int classroom_id;
    private int lecture_id;
    private int capacity;
    private boolean is_active=true;

    public WebRegClass(){}
    public WebRegClass(int course_id, int semester_id,int professor_id,int classroom_id,int lecture_id,int capacity) {
        this.course_id=course_id;
        this.semester_id=semester_id;
        this.professor_id=professor_id;
        this.classroom_id=classroom_id;
        this.lecture_id=lecture_id;
        this.capacity=capacity;
        this.is_active=true;
    }
}
