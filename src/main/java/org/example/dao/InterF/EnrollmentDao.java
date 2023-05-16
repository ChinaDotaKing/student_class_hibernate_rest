package org.example.dao.InterF;

import org.example.domain.Enrollment;
import org.example.domain.WebRegClass;
import org.example.domain.hibernate.EnrollmentHibernate;

import java.util.List;

public interface EnrollmentDao {
    Enrollment getEnrollmentById(int id);

    List<Enrollment> getActiveEnrollmentsByClassId(int classId);

    boolean getEnrollmentStatus(int studentId, int classId);

    String getStatus(int studentId, int classId);

    List<Enrollment> getClassesByStudentId(int id);

    int getEnrollmentNumByClassId(int id);

    void addEnrollment(int studentId, int classId);

    void dropEnrollment(int studentId, int classId);

    void withdrawEnrollment(int studentId, int classId);

    List<Enrollment> getEnrollmentsByStudentClassId(int studentId, int classId);

    void failStudentByClassId(int studentId, int classId);

    void passStudentByClassId(int studentId, int classId);


    List<Enrollment> getEnrollmentByStudentId(int id);

    List<Enrollment> getEnrollmentsByClass(WebRegClass c);
}
