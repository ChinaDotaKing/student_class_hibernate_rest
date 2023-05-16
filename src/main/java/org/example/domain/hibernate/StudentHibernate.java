package org.example.domain.hibernate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
//import org.example.Entity.RoleEntity;
import org.example.domain.Student;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name="student")
public class StudentHibernate extends Student {


    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column
    private  int id;


    @Column
    private String username;





    @Column
    private String email;


    @Column
    private String encrypted_password;

    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="dept_id")
    @ToString.Exclude
    private DepartmentHibernate departmentHibernate;





    @Column(name = "is_active")
    private boolean is_active = true;


    @Column(name = "is_admin")
    private boolean is_admin =false;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "studentHibernate")// default fetch type is LAZY
    @ToString.Exclude // to avoid infinite loop
    private Set<ApplicationHibernate> applicationHibernates = new HashSet<>();


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "studentHibernate")// default fetch type is LAZY
    @ToString.Exclude // to avoid infinite loop
    private Set<EnrollmentHibernate> enrollmentHibernates = new HashSet<>();

//    @ManyToMany(cascade= { CascadeType.PERSIST },fetch=FetchType.EAGER )
//    @JoinTable(name="users_roles",joinColumns=@JoinColumn
//            (name="users_id",referencedColumnName="id"),inverseJoinColumns
//            =@JoinColumn(name="roles_id",referencedColumnName="id"))
//    private Collection<RoleEntity> roles;


    public StudentHibernate(String username, String password, String email, String firstName, String lastName, DepartmentHibernate departmentHibernate, boolean is_admin) {
        this.username = username;
        //this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.departmentHibernate = departmentHibernate;
        this.is_admin = is_admin;
//        this.setDept_id(this.departmentHibernate.getId());
    }

    public StudentHibernate(String username, String encrypted_password, String email, String firstName, String lastName, DepartmentHibernate departmentHibernate) {
        this.username = username;
        this.encrypted_password = encrypted_password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.departmentHibernate = departmentHibernate;
//        this.setDept_id(this.departmentHibernate.getId());
    }

    public StudentHibernate(int id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        //this.password = password;
        this.email = email;
//        this.setDept_id(this.departmentHibernate.getId());
    }

    public StudentHibernate(String username, String encrypted_password, String email) {
        this.username = username;
        this.encrypted_password = encrypted_password;
        this.email = email;
//        this.setDept_id(this.departmentHibernate.getId());
    }

    public StudentHibernate() {
//        this.setDept_id(this.departmentHibernate.getId());
    }
}
