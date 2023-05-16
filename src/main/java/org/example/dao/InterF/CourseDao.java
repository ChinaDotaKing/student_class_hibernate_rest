package org.example.dao.InterF;

import org.example.domain.Course;
import org.example.domain.WebRegClass;

import java.util.List;

public interface CourseDao {


    Course getCourseById(int id);

    List<Course> getAllCourses();

    int createNewCourse(Course course);

    String getCourseNameById(int courseId);

    Course getCourseByClass(WebRegClass c);
}
