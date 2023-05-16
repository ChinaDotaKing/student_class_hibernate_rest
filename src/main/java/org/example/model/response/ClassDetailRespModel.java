package org.example.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClassDetailRespModel {


    CourseRespModel course;

    SemesterRespModel semester;

    LectureRespModel lecture;

    ProfessorRespModel professor;

    ClassroomRespModel classroom;


    List<PrereqRespModel> prereqs;


    List<CurEnrolledStudentRespModel> curEnrolledStudents;
}
