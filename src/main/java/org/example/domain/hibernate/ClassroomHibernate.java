package org.example.domain.hibernate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.domain.Classroom;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name ="Classroom")
public class ClassroomHibernate extends Classroom {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column
    private int id;


    @Column
    private String name;

    @Column
    private String building;

    @Column
    private int capacity;


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "classroomHibernate")// default fetch type is LAZY
    @ToString.Exclude // to avoid infinite loop
    private Set<WebRegClassHibernate> webRegClassHibernates = new HashSet<>();

}
