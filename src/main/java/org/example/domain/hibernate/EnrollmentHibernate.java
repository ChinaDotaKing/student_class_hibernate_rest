package org.example.domain.hibernate;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.domain.Enrollment;
import org.springframework.stereotype.Component;

import javax.persistence.*;


@Getter
@Setter
@Entity
@Table(name="Enrollment" )
public class EnrollmentHibernate extends Enrollment {


    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column
    private int id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="student_id")
    @ToString.Exclude
    private StudentHibernate studentHibernate;




    @ManyToOne(fetch =FetchType.EAGER)
    @JoinColumn(name ="class_id")
    @ToString.Exclude
    private WebRegClassHibernate webRegClassHibernate;





    @Column(name ="status")
    private String status;


//    public EnrollmentHibernate(){
//        this.setClass_id(this.webRegClassHibernate.getId());
//        this.setStudent_id(this.studentHibernate.getId());
//    }

}
