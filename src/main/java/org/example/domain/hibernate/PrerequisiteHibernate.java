package org.example.domain.hibernate;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.domain.Prerequisite;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="Prerequisite" )
public class PrerequisiteHibernate extends Prerequisite {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="course_id")
    @ToString.Exclude
    private CourseHibernate courseHibernate;





    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="pre_req_id" )
    @ToString.Exclude
    private CourseHibernate pre_req;




//    public PrerequisiteHibernate(){
//        this.setCourse_id(this.courseHibernate.getId());
//        this.setPre_req_id(this.pre_req.getId());
//    }
}
