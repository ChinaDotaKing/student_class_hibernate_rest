package org.example.dao.InterF;

import org.example.domain.Course;
import org.example.domain.Department;
import org.example.domain.Professor;
import org.example.domain.Student;

public interface DepartmentDao {
    Department getDepartmentById(int id);


    Department getDepartmentByStudent(Student c);

    Department getDepartmentByProfessor(Professor c);

    Department getDepartmentByCourse(Course course);
}
