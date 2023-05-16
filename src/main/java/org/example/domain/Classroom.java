package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public abstract  class Classroom {


    private int id;
    private String name;
    private String building;
    private int capacity;

}
