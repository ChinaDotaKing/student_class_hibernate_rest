package org.example.domain.hibernate;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.domain.Department;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name="Department" )
public class DepartmentHibernate extends Department {


    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column
    private int id;

    @Column
    private String name;

    @Column
    private String school;


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "departmentHibernate")// default fetch type is LAZY
    @ToString.Exclude // to avoid infinite loop
    private Set<CourseHibernate> courseHibernates = new HashSet<>();


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "departmentHibernate")// default fetch type is LAZY
    @ToString.Exclude // to avoid infinite loop
    private Set<StudentHibernate> studentHibernates = new HashSet<>();


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "departmentHibernate")// default fetch type is LAZY
    @ToString.Exclude // to avoid infinite loop
    private Set<ProfessorHibernate> professorHibernates = new HashSet<>();

}
