package org.example.domain.hibernate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.domain.Professor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name="Professor" )
public class ProfessorHibernate extends Professor {


    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column
    private int id;


    @Column
    private String firstName;


    @Column
    private String lastName;

    @Column
    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="dept_id")
    @ToString.Exclude
    private DepartmentHibernate departmentHibernate;




    @OneToMany(fetch = FetchType.EAGER, mappedBy = "professorHibernate")// default fetch type is LAZY
    @ToString.Exclude // to avoid infinite loop
    private Set<WebRegClassHibernate> webRegClassHibernates = new HashSet<>();


    public ProfessorHibernate(){
//        this.setDept_id(this.departmentHibernate.getId());
    }
}
