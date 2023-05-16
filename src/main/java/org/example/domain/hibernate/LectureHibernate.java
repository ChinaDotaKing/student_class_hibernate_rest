package org.example.domain.hibernate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.domain.Lecture;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.sql.Time;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name="Lecture" )
public class LectureHibernate extends Lecture {


    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column
    private int id;

    @Column(name="day_of_week")
    private int day_of_week;

    @Column(name ="start_time")
    private Time start_time;


    @Column(name ="end_time")
    private Time end_time;


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "lectureHibernate")// default fetch type is LAZY
    @ToString.Exclude // to avoid infinite loop
    private Set<WebRegClassHibernate> webRegClassHibernates = new HashSet<>();
}
