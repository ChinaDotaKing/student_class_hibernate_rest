package org.example.model.response;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


@Getter
@Setter
public class AdminApplicationResp {
    String student_name;
    String email;
    String course_name;
    String course_code;
    String semester_name;
    Timestamp creationTime;
    String request;
    String status;

}
