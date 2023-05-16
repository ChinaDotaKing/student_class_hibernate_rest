package org.example.dao.InterF;

import org.example.domain.Course;
import org.example.domain.Prerequisite;

import java.util.List;

public interface PrerequisiteDao {
    Prerequisite getPrerequisiteById(int id);

    //List<Prerequisite> getPrerequisiteByCourseId(int id);


    List<Prerequisite> getPrerequisiteByCourse(Course course);

    int getPrereqByPre(Prerequisite prerequisite);
}
