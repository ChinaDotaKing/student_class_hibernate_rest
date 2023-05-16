package org.example.domain.hibernate;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.domain.Application;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name ="Application")
public class ApplicationHibernate extends Application {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name="student_id",nullable = false
    )
    @ToString.Exclude
    private StudentHibernate studentHibernate;





    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name="class_id",nullable = false
    )
    @ToString.Exclude
    private WebRegClassHibernate webRegClassHibernate;



    @Column(name="creation_time")
    private Timestamp creationTime;

    @Column
    private String request;

    @Column
    private String status;

    @Column
    private String feedback;

//    public ApplicationHibernate(){
//        this.setStudent_id(this.studentHibernate.getId());
//        this.setClass_id(this.webRegClassHibernate.getId());
//
//    }


}
