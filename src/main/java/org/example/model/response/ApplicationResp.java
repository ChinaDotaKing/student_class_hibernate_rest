package org.example.model.response;


import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ApplicationResp {
    String course_name;
    String course_code;
    String semester_name;
    Timestamp creationTime;
    String request;
    String status;
    String feedback;
}
