package org.example.dao.InterF;

import org.example.domain.Lecture;
import org.example.domain.WebRegClass;

public interface LectureDao {
    Lecture getLectureById(int id);


    Lecture getLectureByClass(WebRegClass c);
}
