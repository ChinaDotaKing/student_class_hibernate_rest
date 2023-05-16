package org.example.dao.InterF;

import org.example.domain.Semester;
import org.example.domain.WebRegClass;

public interface SemesterDao {
    Semester getSemesterById(int id);


    Semester getSemesterByClass(WebRegClass c);
}
