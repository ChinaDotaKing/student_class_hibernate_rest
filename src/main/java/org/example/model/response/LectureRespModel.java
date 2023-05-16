package org.example.model.response;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;


@Getter
@Setter
public class LectureRespModel {

    int day_of_week;

    Time start_time;
    Time end_time;

}
