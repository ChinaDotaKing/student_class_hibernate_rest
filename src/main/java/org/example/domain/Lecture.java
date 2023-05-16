package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.sql.Time;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public abstract class Lecture {


    private int id;
    private int day_of_week;
    private Time start_time;
    private Time end_time;
}
