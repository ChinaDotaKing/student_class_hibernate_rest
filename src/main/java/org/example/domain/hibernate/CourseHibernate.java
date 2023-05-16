package org.example.domain.hibernate;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.domain.Course;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name ="Course")
public class CourseHibernate extends Course {


    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column
    private int id;

    @Column(name="course_name")
    private String course_name;

    @Column(name="course_code")
    private String course_code;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="dept_id")
    private DepartmentHibernate departmentHibernate;





    @Column
    private String description;


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "courseHibernate")// default fetch type is LAZY
    @ToString.Exclude // to avoid infinite loop
    private Set<PrerequisiteHibernate> prerequisiteHibernates = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "pre_req")// default fetch type is LAZY
    @ToString.Exclude // to avoid infinite loop
    private Set<PrerequisiteHibernate> prerequisiteHibernates2 = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "courseHibernate")// default fetch type is LAZY
    @ToString.Exclude // to avoid infinite loop
    private Set<WebRegClassHibernate> webRegClassHibernates = new HashSet<>();


    public CourseHibernate(){
//        this.setDept_id(departmentHibernate.getId());
    }
    public CourseHibernate(String course_name, String course_code, DepartmentHibernate departmentHibernate, String description) {
        this.course_name = course_name;
        this.course_code = course_code;
        this.departmentHibernate=departmentHibernate;
        this.description = description;
//        this.setDept_id(departmentHibernate.getId());
    }
}
