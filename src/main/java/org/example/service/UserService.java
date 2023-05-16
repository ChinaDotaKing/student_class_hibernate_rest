package org.example.service;

import org.example.domain.*;
import org.example.domain.hibernate.StudentHibernate;
import org.example.model.request.UserSignupRequestModel;
import org.example.model.response.*;

import java.util.List;

public interface UserService {

//    void createNewStudent(Student student);


    boolean is_admin(String username);

    void createNewStudent(StudentHibernate student);



    List<WebRegClass> getClassesbyStudentId(int id);

    Course getCourseById(int id);



    Department getDepartmentById(int id);

    WebRegClass getWebRegClassById(int id);

    List<Enrollment> getActiveEnrollmentsByClassId(int id);

    //Lecture getLectureByClassId(int id);

    Lecture getLectureByClass(WebRegClass theClass);


    //List<Prerequisite> getPrerequisitesByCourseId(int id);

    List<WebRegClass> getAllActiveClasses();

    boolean getEnrollmentStatus(int studentId, int classId);
    String getClassStatus(int studentId,int classId);
    Student getStudentById(int studentId);

    boolean checkIfStudentEligible(Student student,WebRegClass theClass);

    boolean checkIfStudentPassCourseById(Student student,int courseId);

    boolean checkIfStudentPassCourseById(Student student, Prerequisite prerequisite);

    boolean checkTimeConflict(Student student, WebRegClass theClass);

    List<Course> getAllCourses();

    int createNewCourse(Course course);

    boolean checkIfClassIsFull(WebRegClass theClass);

    void addEnrollment(int studentId, int classId);

    boolean checkIfDroppable(int studentId,WebRegClass theClass);


    WebRegClass getWebRegClassByApplication(Application application);

    void dropEnrollments(int studentId, int classId);


    void withdrawEnrollment(int studentId,int classId);
    boolean checkIfWithdrawable(int studentId,WebRegClass theClass);

    boolean checkIfWithdrawn(int studentId,int classId);


    int createEnrollmentApplication(int studentId, int classId);
    List<Student> getAllStudents();

    List<WebRegClass> getAllStatusClassesbyStudentId(int studentId);

    int createNewClass(WebRegClass theClass);

    List<WebRegClass> getAllClasses();

    void deactivateClassById(int classId);

    void activateClassById(int classId);

    int createApplicationByStudentClassId(int id, int classId) ;

    List<Application> getApplicationsByStudentId(int id);

    List<Application> getAllPendingApplications();

    void rejectOrApproveByIdFeedback(String status, int applicationId, String f);

    void activateStudentById(int studentId);

    void deactivateStudentById(int studentId);

    void passStudentByClassId(int studentId, int classId);

    void failStudentByClassId(int studentId, int classId);

    
    String getRequestByApplicationId(int applicationId);

    int getStudentIdByApplicationId(int applicationId);

    int getClassIdByApplicationId(int applicationId);


    int getTotalPages(int studentId,int limit);

    //List<WebRegClass> getClassesbyStudentIdPageLimit(int id, int page, int limit);

    int getTotalPagesAdmin(int limit);

   // List<Student> getStudentsByPageLimit(int page, int limit);

    String getCourseNameById(int courseId);

   // Lecture getLectureById(int lecture_id);

    void withdrawApplication(int applicationId, int id);

    Application getApplicationById(int applicationId);



    Department getDepartmentByStudent(Student c);

    Course getCourseByClass(WebRegClass c);

    Professor getProfessorByClass(WebRegClass c);

    Semester getSemesterByClass(WebRegClass c);

    Department getDepartmentByProfessor(Professor c);

    List<Enrollment> getEnrollmentByStudentId(int id);

   // List<Enrollment> getEnrollmentByStudentIdPageLimit(int id, int page, int limit);

    WebRegClass getClassByEnrollment(Enrollment c);


    Integer getActiveEnrollmentsByClass(WebRegClass c);

    Department getDepartmentByCourse(Course course);

    Classroom getClassroomByClass(WebRegClass theWebRegClass);

    List<Prerequisite> getPrerequisiteByCourse(Course course);

    Student getStudentByEnrollment(Enrollment c);

    Student getStudentByApplication(Application c);

    void createNewStudentForRest(UserSignupRequestModel userDetails);

    UserSignupResponseModel getUserSignupResponseModel(UserSignupRequestModel userDetails);

    Student getUserByname(String user);


    List<ClassRespEntity> getClassRespEntitiesFromEnrollments(List<Enrollment> enrollments);

    ClassDetailRespModel getClassDetailByClass(WebRegClass theWebRegClass);

    List<StudentClassManaResp> getStudentClassManaResp(int id);

    List<ApplicationResp> getApplicationRespByStudentId(int id);

    List<AdminStuResp> getAdminStuResps();

    StudentDetailResp getStudentDetailResp(int studentId);

    List<AdminCourseResp> getAdminCourseResps();

    AdminCourseResp getAdminCourseRespById(int id);

    List<AdminClassManaResp> getAdminClassManaResp();

    List<AdminApplicationResp> getAdminApplicationResps();

    AdminApplicationResp getAdminApplicationRespByApplicationId(int applicationId);
}
