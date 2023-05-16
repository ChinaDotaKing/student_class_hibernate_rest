package org.example.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public abstract class Enrollment {

    private int id;
    private int student_id;
    private int class_id;
    private String status;


}
