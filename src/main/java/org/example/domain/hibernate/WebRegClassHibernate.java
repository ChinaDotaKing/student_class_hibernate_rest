package org.example.domain.hibernate;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.domain.WebRegClass;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name="WebRegClass" )
public class WebRegClassHibernate extends WebRegClass {



    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="course_id")
    @ToString.Exclude
    private CourseHibernate courseHibernate;





    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="semester_id")
    @ToString.Exclude
    private SemesterHibernate semesterHibernate;





    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="professor_id")
    @ToString.Exclude
    private ProfessorHibernate professorHibernate;

    //@Column(name="professor_id")


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="classroom_id")
    @ToString.Exclude
    private ClassroomHibernate classroomHibernate;


    //@Column(name="classroom_id")


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="lecture_id")
    @ToString.Exclude
    private LectureHibernate lectureHibernate;



    //@Column(name="lecture_id")

    @Column
    private int capacity;


    @Column(name ="is_active")
    private boolean is_active=true;


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "webRegClassHibernate")// default fetch type is LAZY
    @ToString.Exclude // to avoid infinite loop
    private Set<ApplicationHibernate> applicationHibernates = new HashSet<>();


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "webRegClassHibernate")// default fetch type is LAZY
    @ToString.Exclude // to avoid infinite loop
    private Set<EnrollmentHibernate> enrollmentHibernates = new HashSet<>();

    public WebRegClassHibernate(){
//        this.setCourse_id(courseHibernate.getId());
//        this.setSemester_id(this.semesterHibernate.getId());
//        this.setProfessor_id(this.professorHibernate.getId());
//        this.setClassroom_id(classroomHibernate.getId());
//        this.setLecture_id(lectureHibernate.getId());
    }
    public WebRegClassHibernate(CourseHibernate courseHibernate, SemesterHibernate semesterHibernate, ProfessorHibernate professorHibernate, ClassroomHibernate classroomHibernate, LectureHibernate lectureHibernate, int capacity) {
        this.courseHibernate=courseHibernate;
        this.semesterHibernate=semesterHibernate;
        this.professorHibernate=professorHibernate;
        this.classroomHibernate=classroomHibernate;
        this.lectureHibernate=lectureHibernate;
        this.capacity=capacity;
        this.is_active=true;
    }
}
