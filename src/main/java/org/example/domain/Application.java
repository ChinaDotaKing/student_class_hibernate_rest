package org.example.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Application {

    private int id;
    private int student_id;
    private int class_id;
    private Timestamp creationTime;
    private String request;

    private String status;
    private String feedback;

}
