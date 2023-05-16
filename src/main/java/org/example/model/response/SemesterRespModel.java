package org.example.model.response;


import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class SemesterRespModel {


    Date start_date;
    Date end_date;
    String name;


}
