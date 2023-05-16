package org.example.dao.InterF;

import org.example.domain.Classroom;
import org.example.domain.WebRegClass;

public interface ClassroomDao {
    Classroom getClassroomById(int id);


    Classroom getClassroomByClass(WebRegClass theWebRegClass);
}
