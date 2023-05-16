package org.example.dao.InterF;

import org.example.domain.Application;
import org.example.domain.Enrollment;
import org.example.domain.Student;
import org.example.domain.hibernate.StudentHibernate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

public interface StudentDao {



    void createNewStudent(StudentHibernate student);

    List<Student> getAllUsers();

    List<Student> getAllStudents();

    Optional<Student> validateLogin(String username, String password);

    Student getStudentById(int studentId);

    void activateStudentById(int studentId);

    void deactivateStudentById(int studentId);

    //int getTotalPagesAdmin(int limit);

    List<Student> getStudentsByPageLimit(int page, int limit);

    Student getStudentByEnrollment(Enrollment c);

    Student getStudentByApplication(Application c);

    StudentHibernate getUserByName(String userName);


//    void setBCryptPasswordEncoder(org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder bCryptPasswordEncoder);

//    void setPwEncoder(org.example.domain.PWEncoder pwEncoder);
}
