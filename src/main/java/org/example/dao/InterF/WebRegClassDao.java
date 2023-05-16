package org.example.dao.InterF;

import org.example.domain.Application;
import org.example.domain.Enrollment;
import org.example.domain.WebRegClass;

import java.util.List;

public interface WebRegClassDao {
    WebRegClass getWebRegClassById(int id);

    List<WebRegClass> getWebRegClassesByCourseId(int id);

    List<WebRegClass> getWebRegClassesByStudentId(int id);

    List<WebRegClass> getAllActiveClasses();

    List<WebRegClass> getAllClasses();

    List<WebRegClass> getAllStatusClassesByStudentId(int studentId);

    int createNewClass(WebRegClass theClass);

    void deactivateClassById(int classId);

    void activateClassById(int classId);


    WebRegClass getClassByEnrollment(Enrollment c);


    WebRegClass getWebRegClassByApplication(Application application);
}
