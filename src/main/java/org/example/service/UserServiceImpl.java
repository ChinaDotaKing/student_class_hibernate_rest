package org.example.service;

import org.example.dao.InterF.*;
//import org.example.dao.jdbcDao.*;
import org.example.domain.*;
import org.example.domain.hibernate.DepartmentHibernate;
import org.example.domain.hibernate.StudentHibernate;
import org.example.exceptions.UserServiceException;
import org.example.model.request.UserSignupRequestModel;
import org.example.model.response.*;
import org.example.model.result.ErrorMessages;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

//    @Autowired
//    SessionFactory sessionFactory;
//
//
//    @Autowired
//            Session session;


    StudentDao studentDao;


    WebRegClassDao webRegClassDao;


    SemesterDao semesterDao;


    ProfessorDao professorDao;


    ClassroomDao classroomDao;


    CourseDao courseDao;


    EnrollmentDao enrollmentDao;


    DepartmentDao departmentDao;


    LectureDao lectureDao;


    PrerequisiteDao prerequisiteDao;


    ApplicationDao applicationDao;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;



@Autowired
    public UserServiceImpl( @Qualifier("ormSwitch") boolean ormSwitch,

                           // @Qualifier("ApplicationDaoJdbc") ApplicationDao applicationDaoJdbc,
                            @Qualifier("ApplicationDaoHibernate") ApplicationDao applicationHibernateDao,
                            //@Qualifier("StudentDaoJdbc") StudentDao studentDaoJdbc,
                            @Qualifier("StudentDaoHibernate") StudentDao studentHibernateDao,

                           // @Qualifier("ClassroomDaoJdbc") ClassroomDao classroomDaoJdbc,
                            @Qualifier("ClassroomDaoHibernate") ClassroomDao classroomHibernateDao,

                           // @Qualifier("CourseDaoJdbc") CourseDao courseDaoJdbc,
                            @Qualifier("CourseDaoHibernate") CourseDao courseHibernateDao,
                          //  @Qualifier("DepartmentDaoJdbc") DepartmentDao departmentDaoJdbc,
                            @Qualifier("DepartmentDaoHibernate") DepartmentDao departmentHibernateDao,
                           // @Qualifier("EnrollmentDaoJdbc") EnrollmentDao enrollmentDaoJdbc,
                            @Qualifier("EnrollmentDaoHibernate") EnrollmentDao enrollmentHibernateDao,

                           // @Qualifier("LectureDaoJdbc") LectureDao lectureDaoJdbc,
                            @Qualifier("LectureDaoHibernate") LectureDao lectureHibernateDao,

                           // @Qualifier("PrerequisiteDaoJdbc") PrerequisiteDao prerequisiteDaoJdbc,
                            @Qualifier("PrerequisiteDaoHibernate") PrerequisiteDao prerequisiteHibernateDao,
                           // @Qualifier("ProfessorDaoJdbc") ProfessorDao professorDaoJdbc,
                            @Qualifier("ProfessorDaoHibernate") ProfessorDao professorHibernateDao,

                           // @Qualifier("SemesterDaoJdbc") SemesterDao semesterDaoJdbc,
                            @Qualifier("SemesterDaoHibernate") SemesterDao semesterHibernateDao,

                          //  @Qualifier("WebRegClassDaoJdbc") WebRegClassDao webRegClassDaoJdbc,
                            @Qualifier("WebRegClassDaoHibernate") WebRegClassDao webRegClassHibernateDao

                            ) {
        this.studentDao =   studentHibernateDao;
    this.applicationDao =   applicationHibernateDao;
    this.classroomDao =   classroomHibernateDao;
    this.courseDao =   courseHibernateDao;
    this.departmentDao =   departmentHibernateDao;
    this.enrollmentDao =   enrollmentHibernateDao;
    this.lectureDao =  lectureHibernateDao;
    this.prerequisiteDao =   prerequisiteHibernateDao;
    this.professorDao =   professorHibernateDao;
    this.semesterDao =   semesterHibernateDao;
    this.webRegClassDao =   webRegClassHibernateDao;

    //session=sessionFactory.getCurrentSession();

    }

    @Override
    public boolean is_admin(String username) {
    Student student=studentDao.getUserByName(username);

        return student.is_admin();
    }

    @Override
    public void createNewStudent(StudentHibernate student) {
            studentDao.createNewStudent(student);

    }

    @Override
    public void createNewStudentForRest(UserSignupRequestModel userDetails) {
        Department departmentHibernate=getDepartmentById(userDetails.getDepartment_id());
        if(departmentHibernate==null) throw new UserServiceException("department not found");

        Student student=getUserByname(userDetails.getUsername());
        if(student!=null) throw new UserServiceException("Username found in the database.");

        StudentHibernate studentHibernate=new StudentHibernate(
                userDetails.getUsername(),
                bCryptPasswordEncoder.encode(userDetails.getPassword()),
                userDetails.getEmail(),
                userDetails.getFirstname(),
                userDetails.getLastname(),
                (DepartmentHibernate) departmentHibernate
        );
        studentHibernate.setDept_id(userDetails.getDepartment_id());

        createNewStudent(studentHibernate);

    }

    @Override
    public UserSignupResponseModel getUserSignupResponseModel(UserSignupRequestModel userDetails) {

        UserSignupResponseModel userSignupResponseModel=new UserSignupResponseModel();

        userSignupResponseModel.setUsername(userDetails.getUsername());
        userSignupResponseModel.setEmail(userDetails.getEmail());
        userSignupResponseModel.setLastname(userDetails.getLastname());
        userSignupResponseModel.setFirstname(userDetails.getFirstname());
        Department departmentHibernate=departmentDao.getDepartmentById(userDetails.getDepartment_id());
        userSignupResponseModel.setDepartment_name(departmentHibernate.getName());
        userSignupResponseModel.setSchool(departmentHibernate.getSchool());
        return userSignupResponseModel;
    }

    @Override
    public Student getUserByname(String user) {
        return studentDao.getUserByName(user);
    }

    @Override
    public List<ClassRespEntity> getClassRespEntitiesFromEnrollments(List<Enrollment> enrollments) {

        List<WebRegClass> classes= null;


        List<Course> courses=null;
        List<Professor> professors=null;
        List<Semester> semesters=null;
        List<Department> departments=null;
        if(!(enrollments ==null))
        {
            classes= enrollments.stream().map(c->getClassByEnrollment(c)).collect(Collectors.toList());
            classes=classes.stream().sorted((a,b)->getSemesterByClass(a).getStart_date().before(getSemesterByClass(b).getStart_date())?1:-1).collect(Collectors.toList());

            courses=classes.stream().map(c->getCourseByClass(c)).collect(Collectors.toList());

            professors=classes.stream().map(c->getProfessorByClass(c)).collect(Collectors.toList());

            semesters=classes.stream().map(c->getSemesterByClass(c)).collect(Collectors.toList());

            departments=professors.stream().map(c->getDepartmentByProfessor(c)).collect(Collectors.toList());

        }

        List<ClassRespEntity> classRespEntities=new ArrayList<>();

        for(int i=0;i<enrollments.size();i++){
            ClassRespEntity classRespEntity=new ClassRespEntity();

            classRespEntity.setStatus(enrollments.get(i).getStatus());
            classRespEntity.setSchool_name(departments.get(i).getSchool());
            classRespEntity.setSemester_name(semesters.get(i).getName());
            classRespEntity.setDepartment_name(departments.get(i).getName());
            classRespEntity.setCourse_name(courses.get(i).getCourse_name());
            classRespEntity.setCourse_code(courses.get(i).getCourse_code());
            classRespEntities.add(classRespEntity);
        }





        return classRespEntities;
    }

    @Override
    public ClassDetailRespModel getClassDetailByClass(WebRegClass theWebRegClass) {

        Course course = getCourseByClass(theWebRegClass);
        List<Enrollment> enrollments = getActiveEnrollmentsByClassId(theWebRegClass.getId());
        Department department = getDepartmentByCourse(course);

        Lecture lecture = getLectureByClass(theWebRegClass);

        Professor professor = getProfessorByClass(theWebRegClass);
        Department pro_dept = getDepartmentByProfessor(professor);

        Classroom classroom = getClassroomByClass(theWebRegClass);
        List<Prerequisite> prerequisites = getPrerequisiteByCourse(course);
        List<Enrollment> enrollments1=getActiveEnrollmentsByClassId(theWebRegClass.getId());

        List<Student> students=null;
        List<Department> departments=null;
        Semester semester=getSemesterByClass(theWebRegClass);
        if(enrollments1!=null){
            students=enrollments1.stream().map(c->getStudentByEnrollment(c)).collect(Collectors.toList());
            departments=students.stream().map(c->getDepartmentByStudent(c)).collect(Collectors.toList());

        }

        ClassDetailRespModel classDetailRespModel=new ClassDetailRespModel();

        classDetailRespModel.setCourse(getCourseRespByCourse(course));
        classDetailRespModel.getCourse().setCapacity(theWebRegClass.getCapacity());
        classDetailRespModel.getCourse().setEnrollment_num(enrollments.size());
        classDetailRespModel.getCourse().set_Active(theWebRegClass.is_active());

        ModelMapper modelMapper=new ModelMapper();

        classDetailRespModel.setSemester(modelMapper.map(semester, SemesterRespModel.class));
        classDetailRespModel.setClassroom(modelMapper.map(classroom, ClassroomRespModel.class));
        classDetailRespModel.setLecture(modelMapper.map(lecture,LectureRespModel.class));

        classDetailRespModel.setProfessor(getProfessorRespByprofessor(professor));


        List<PrereqRespModel> prereqRespModels=new ArrayList<>();
        for(Prerequisite prereq: prerequisites){
            String prereq_name=getCourseNameById(prereq.getPre_req_id());
           prereqRespModels.add(new PrereqRespModel(prereq_name));
        }
        classDetailRespModel.setPrereqs(prereqRespModels);

        List<CurEnrolledStudentRespModel> curEnrolledStudentRespModels=new ArrayList<>();
        for(int i=0;i<students.size();i++){
            CurEnrolledStudentRespModel curEnrolledStudentRespModel=new CurEnrolledStudentRespModel();

            curEnrolledStudentRespModel.setFull_name(students.get(i).getFirstName()+" "+students.get(i).getLastName());
            curEnrolledStudentRespModel.setSchool(departments.get(i).getSchool());
            curEnrolledStudentRespModel.setEmail(students.get(i).getEmail());
            curEnrolledStudentRespModel.setDepartment_name(departments.get(i).getName());

            curEnrolledStudentRespModels.add(curEnrolledStudentRespModel);

        }
        classDetailRespModel.setCurEnrolledStudents(curEnrolledStudentRespModels);


        return classDetailRespModel;
    }

    @Override
    public List<StudentClassManaResp> getStudentClassManaResp(int id) {
        List<WebRegClass> classes = getAllActiveClasses();
        List<Course> courses =null;
        List<Professor> professors =null;
        List<Semester> semesters=null;
        List<Department> departments =null;

        List<Integer> en_num = null;
        List<String> en_status = null;

        if(classes!=null) {
            courses = classes.stream().map(c -> getCourseByClass(c)).collect(Collectors.toList());
            professors = classes.stream().map(c -> getProfessorByClass(c)).collect(Collectors.toList());
            semesters = classes.stream().map(c -> getSemesterByClass(c)).collect(Collectors.toList());
            departments = professors.stream().map(c -> getDepartmentByProfessor(c)).collect(Collectors.toList());
            en_num = classes.stream().map(c -> getActiveEnrollmentsByClass(c) == null ? 0 : getActiveEnrollmentsByClassId(c.getId()).size()).collect(Collectors.toList());

            en_status = classes.stream().map(c -> getEnrollmentStatus(id, c.getId()) ? "YES" : "NO").collect(Collectors.toList());

        }

        List<StudentClassManaResp> studentClassManaResps=new ArrayList<>();
        for(int i=0;i< classes.size();i++){
            StudentClassManaResp studentClassManaResp=new StudentClassManaResp();

            studentClassManaResp.setCapacity(classes.get(i).getCapacity());
            studentClassManaResp.setCourse_code(courses.get(i).getCourse_code());
            studentClassManaResp.setCourse_name(courses.get(i).getCourse_name());
            studentClassManaResp.setDepartment_name(departments.get(i).getName());
            studentClassManaResp.setSchool_name(departments.get(i).getSchool());
            studentClassManaResp.setSemester_name(semesters.get(i).getName());
            studentClassManaResp.setCurEnrollmentNumber(en_num.get(i));
            studentClassManaResp.setStatus(classes.get(i).is_active());

            studentClassManaResp.setIsEnrolled(en_status.get(i));
            studentClassManaResps.add(studentClassManaResp);

        }

        return studentClassManaResps;


    }

    @Override
    public List<ApplicationResp> getApplicationRespByStudentId(int id) {
        List<Application> applications= getApplicationsByStudentId(id);
        List<Course> courses=null;
        List<Professor> professors=null;
        List<Semester> semesters=null;
        List<Department> departments=null;
        List<WebRegClass> classes=null;
        List<Student> students=null;
        if(!(applications ==null))
        {
            classes=applications.stream().map(c->getWebRegClassByApplication(c)).collect(Collectors.toList());

            students=applications.stream().map(c->getStudentByApplication(c)).collect(Collectors.toList());

            courses=classes.stream().map(c->getCourseByClass(c)).collect(Collectors.toList());
            semesters=classes.stream().map(c->getSemesterByClass(c)).collect(Collectors.toList());
            professors=classes.stream().map(c->getProfessorByClass(c)).collect(Collectors.toList());
            departments=professors.stream().map(c->getDepartmentByProfessor(c)).collect(Collectors.toList());
        }

        List<ApplicationResp> applicationResps=new ArrayList<>();

        for(int i=0;i<applications.size();i++){
            ApplicationResp applicationResp=new ApplicationResp();

            applicationResp.setCourse_code(courses.get(i).getCourse_code());
            applicationResp.setCourse_name(courses.get(i).getCourse_name());
            applicationResp.setSemester_name(semesters.get(i).getName());
            applicationResp.setCreationTime(applications.get(i).getCreationTime());
            applicationResp.setRequest(applications.get(i).getRequest());
            applicationResp.setFeedback(applications.get(i).getFeedback());
            applicationResp.setStatus(applications.get(i).getStatus());

            applicationResps.add(applicationResp);

        }

        return applicationResps;
    }

    @Override
    public List<AdminStuResp> getAdminStuResps() {

    List<Student> students = getAllStudents();
    List<Department> departments=students.stream().map(c->getDepartmentByStudent(c)).collect(Collectors.toList());

    List<AdminStuResp> adminStuResps=new ArrayList<>();

    for(int i=0;i<students.size();i++){
        AdminStuResp adminStuResp=new AdminStuResp();
        System.out.print(students.get(i).getFirstName());
        adminStuResp.set_Active(students.get(i).is_active());
        adminStuResp.setSchool(departments.get(i).getSchool());
        adminStuResp.setDepartment_name(departments.get(i).getName());
        adminStuResp.setFull_name(students.get(i).getFirstName()+" "+students.get(i).getLastName());
        adminStuResp.setEmail(students.get(i).getEmail());
        adminStuResps.add(adminStuResp);
    }

        return adminStuResps;
    }

    @Override
    public StudentDetailResp getStudentDetailResp(int studentId) {

        Student theStudent=getStudentById(studentId);
        Department department=getDepartmentByStudent(theStudent);
        List<WebRegClass> classes=getAllStatusClassesbyStudentId(studentId);
        List<Course> courses=null;
        List<Professor> professors=null;
        List<Semester> semesters=null;
        List<Department> departments=null;
        List<String> en_status=null;
        if(!(classes ==null)) {
            courses = classes.stream().map(c -> getCourseByClass(c)).collect(Collectors.toList());
            professors = classes.stream().map(c -> getProfessorByClass(c)).collect(Collectors.toList());
            semesters = classes.stream().map(c -> getSemesterByClass(c)).collect(Collectors.toList());
            departments = professors.stream().map(c -> getDepartmentByProfessor(c)).collect(Collectors.toList());
            en_status = classes.stream().map(c -> getClassStatus(theStudent.getId(), c.getId())).collect(Collectors.toList());
        }

        StudentDetailResp studentDetailResp=new StudentDetailResp();
        StudentInfoResp studentInfoResp=new StudentInfoResp();

        studentInfoResp.setSchool(department.getSchool());
        studentInfoResp.setFull_name(theStudent.getFirstName()+" "+theStudent.getLastName());
        studentInfoResp.setEmail(theStudent.getEmail());
        studentInfoResp.setDepartment_name(department.getName());


        List<ClassRespEntity > classRespEntities=new ArrayList<>();

        for(int i=0;i<en_status.size();i++){
            ClassRespEntity classRespEntity=new ClassRespEntity();

            classRespEntity.setStatus(en_status.get(i));
            classRespEntity.setSchool_name(departments.get(i).getSchool());
            classRespEntity.setSemester_name(semesters.get(i).getName());
            classRespEntity.setDepartment_name(departments.get(i).getName());
            classRespEntity.setCourse_name(courses.get(i).getCourse_name());
            classRespEntity.setCourse_code(courses.get(i).getCourse_code());
            classRespEntities.add(classRespEntity);
        }
        studentDetailResp.setStudentInfoResp(studentInfoResp);
        studentDetailResp.setClassRespEntities(classRespEntities);

        return studentDetailResp;
    }

    @Override
    public List<AdminCourseResp> getAdminCourseResps() {

        List<Course> courses=getAllCourses();
        List<Department> departments=courses.stream().map(c->getDepartmentByCourse(c)).collect(Collectors.toList());


        List<AdminCourseResp> adminCourseResps=new ArrayList<>();

        for(int i=0;i<courses.size();i++){
            AdminCourseResp adminCourseResp=new AdminCourseResp();
            adminCourseResp.setCourse_description(courses.get(i).getDescription());
            adminCourseResp.setCourse_name(courses.get(i).getCourse_name());
            adminCourseResp.setCourse_code(courses.get(i).getCourse_code());
            adminCourseResp.setDepartment_name(departments.get(i).getName());
            adminCourseResp.setSchool_name(departments.get(i).getSchool());

            adminCourseResps.add(adminCourseResp);
        }
        return adminCourseResps;
    }

    @Override
    public AdminCourseResp getAdminCourseRespById(int id) {
            Course course=getCourseById(id);
            AdminCourseResp adminCourseResp=new AdminCourseResp();
            Department department=getDepartmentByCourse(course);

            adminCourseResp.setCourse_name(course.getCourse_name());
            adminCourseResp.setCourse_code(course.getCourse_code());
            adminCourseResp.setCourse_description(course.getDescription());
            adminCourseResp.setSchool_name(department.getSchool());
            adminCourseResp.setDepartment_name(department.getName());
        return adminCourseResp;
    }

    @Override
    public List<AdminClassManaResp> getAdminClassManaResp() {

        List<WebRegClass> classes = getAllClasses();

            List<Course> courses = classes.stream().map(c -> getCourseByClass(c)).collect(Collectors.toList());
            List<Professor> professors = classes.stream().map(c -> getProfessorByClass(c)).collect(Collectors.toList());
            List<Semester> semesters = classes.stream().map(c -> getSemesterByClass(c)).collect(Collectors.toList());
            List<Department> departments = professors.stream().map(c -> getDepartmentByProfessor(c)).collect(Collectors.toList());
            List<Integer> en_num = classes.stream().map(c -> getActiveEnrollmentsByClass(c) == null ? 0 : getActiveEnrollmentsByClassId(c.getId()).size()).collect(Collectors.toList());
            //List<Enrollment> enrollments = classes.stream().map(c -> userService.getEnrollmentByClassStudentId(c,student.getId())).collect(Collectors.toList());

        List<AdminClassManaResp> adminClassManaResps=new ArrayList<>();

        for(int i=0;i<classes.size();i++){
            AdminClassManaResp adminClassManaResp=new AdminClassManaResp();
            adminClassManaResp.setCourse_name(courses.get(i).getCourse_name());
            adminClassManaResp.setCourse_code(courses.get(i).getCourse_code());
            adminClassManaResp.setDepartment_name(departments.get(i).getName());
            adminClassManaResp.setSchool_name(departments.get(i).getSchool());
            adminClassManaResp.setSemester_name(semesters.get(i).getName());
            adminClassManaResp.setCurEnrollmentNumber(en_num.get(i));
            adminClassManaResp.setCapacity(classes.get(i).getCapacity());
            adminClassManaResp.setStatus(classes.get(i).is_active());
            adminClassManaResps.add(adminClassManaResp);
        }
        return adminClassManaResps;





    }

    @Override
    public List<AdminApplicationResp> getAdminApplicationResps() {

        List<AdminApplicationResp> applicationResps=new ArrayList<>();
        List<Application> applications= getAllPendingApplications();

        List<Course> courses=null;
        List<Professor> professors=null;
        List<Semester> semesters=null;
        List<Department> departments=null;
        List<WebRegClass> classes=null;

        List<Student> students=null;

        if(!(applications ==null))
        {
            classes=applications.stream().map(c->getWebRegClassByApplication(c)).collect(Collectors.toList());

            students=applications.stream().map(c->getStudentByApplication(c)).collect(Collectors.toList());

            courses=classes.stream().map(c->getCourseByClass(c)).collect(Collectors.toList());
            semesters=classes.stream().map(c->getSemesterByClass(c)).collect(Collectors.toList());
            professors=classes.stream().map(c->getProfessorByClass(c)).collect(Collectors.toList());
            departments=professors.stream().map(c->getDepartmentByProfessor(c)).collect(Collectors.toList());
        }

        for(int i=0;i<applications.size();i++){
            AdminApplicationResp applicationResp=new AdminApplicationResp();

            applicationResp.setEmail(students.get(i).getEmail());
            applicationResp.setStudent_name(students.get(i).getFirstName()+" "+students.get(i).getLastName());
            applicationResp.setCourse_code(courses.get(i).getCourse_code());
            applicationResp.setCourse_name(courses.get(i).getCourse_name());
            applicationResp.setCreationTime(applications.get(i).getCreationTime());
            applicationResp.setStatus(applications.get(i).getStatus());
            applicationResp.setRequest(applications.get(i).getRequest());
            applicationResp.setSemester_name(semesters.get(i).getName());
            applicationResps.add(applicationResp);

        }


        return applicationResps;
    }

    @Override
    public AdminApplicationResp getAdminApplicationRespByApplicationId(int applicationId) {

    Application application=getApplicationById(applicationId);

    AdminApplicationResp adminApplicationResp=new AdminApplicationResp();

    Student student=getStudentByApplication(application);
    WebRegClass webRegClass=getWebRegClassByApplication(application);
    Semester semester=getSemesterByClass(webRegClass);
    Course course=getCourseByClass(webRegClass);

    adminApplicationResp.setSemester_name(semester.getName());
    adminApplicationResp.setStudent_name(student.getFirstName()+" "+student.getLastName());
    adminApplicationResp.setCreationTime(application.getCreationTime());
    adminApplicationResp.setEmail(student.getEmail());
    adminApplicationResp.setCourse_name(course.getCourse_name());
    adminApplicationResp.setCourse_code(course.getCourse_code());
    adminApplicationResp.setStatus(application.getStatus());
    adminApplicationResp.setRequest(application.getRequest());
        return adminApplicationResp;
    }

    private ProfessorRespModel getProfessorRespByprofessor(Professor professor) {


        ProfessorRespModel professorRespModel=new ProfessorRespModel();
        professorRespModel.setEmail(professor.getEmail());
        professorRespModel.setFull_name(professor.getFirstName()+" "+professor.getLastName());
        Department departmentHibernate=getDepartmentByProfessor(professor);

        professorRespModel.setSchool_name(departmentHibernate.getSchool());
        professorRespModel.setDepartment_name(departmentHibernate.getName());
        return professorRespModel;

    }

    private CourseRespModel getCourseRespByCourse(Course course) {

    CourseRespModel courseRespModel=new CourseRespModel();

    Department departmentHibernate=getDepartmentByCourse(course);

    courseRespModel.setCourse_name(course.getCourse_name());
    courseRespModel.setCourse_code(course.getCourse_code());
    courseRespModel.setCourse_description(course.getDescription());
    courseRespModel.setDepartment_name(departmentHibernate.getName());
    courseRespModel.setSchool_name(departmentHibernate.getSchool());


    return courseRespModel;

    }


//    @Override
//    public List<WebRegClass> getClassesbyCourseId(int id) {
//        return webRegClassDao.getWebRegClassesByCourseId(id);
//    }

    @Override
    public List<WebRegClass> getClassesbyStudentId(int id) {

        return webRegClassDao.getWebRegClassesByStudentId(id);
    }



    public Course getCourseById(int id) {

        return courseDao.getCourseById(id);
    }





    @Override
    public Department getDepartmentById(int id) {
        return departmentDao.getDepartmentById(id);
    }

    @Override
    public WebRegClass getWebRegClassById(int id) {
        return webRegClassDao.getWebRegClassById(id);
    }

    @Override
    public List<Enrollment> getActiveEnrollmentsByClassId(int id) {
        return enrollmentDao.getActiveEnrollmentsByClassId(id);
    }



    @Override
    public Lecture getLectureByClass(WebRegClass theClass) {
        return lectureDao.getLectureByClass(theClass);
    }

//    @Override
//    public Classroom getClassroomById(int id) {
//        return classroomDao.getClassroomById(id);
//    }



    @Override
    public List<WebRegClass> getAllActiveClasses() {
        return webRegClassDao.getAllActiveClasses();
    }

    @Override
    public boolean getEnrollmentStatus(int studentId, int classId) {
        return enrollmentDao.getEnrollmentStatus(studentId,classId);
    }

    @Override
    public String getClassStatus(int studentId, int classId) {
        return enrollmentDao.getStatus(studentId,classId);
    }

    @Override
    public Student getStudentById(int studentId) {
        return studentDao.getStudentById(studentId);
    }

    @Override
    public boolean checkIfStudentEligible(Student student,WebRegClass theClass) {
    String ex="";
        if(!student.is_active()) {

                ex+=ErrorMessages.STUDENT_NOT_ACTIVE.getErrorMessage()+"\\n";


        }
        if(!theClass.is_active()){
            ex+=ErrorMessages.CLASS_NOT_ACTIVE.getErrorMessage()+"\\n";


        }
        List<Prerequisite> prerequisites=getPrerequisitesByClass(theClass);

        if (!(prerequisites==null))
        for(Prerequisite prerequisite:prerequisites){
            if(!checkIfStudentPassCourseById(student,prerequisite)) {
                ex+=ErrorMessages.PASS_THE_PREREQUISITE.getErrorMessage()+prerequisite.getPre_req_id()+"\\n";

            }
        }

        if(checkIfWithdrawn(student.getId(),theClass.getId())) {

            ex+=ErrorMessages.WITHDRAWN.getErrorMessage()+"\\n";
        }

        Course course=getCourseByClass(theClass);
        if(checkIfStudentPassCourseById(student,course.getId())) {
            ex+=ErrorMessages.PASSED.getErrorMessage()+"\\n";
        }

        if(checkTimeConflict(student,theClass)) {
            ex+=ErrorMessages.Time_CONFLICT.getErrorMessage()+"\\n";
        }

        if(ex.length()==0) return true;
        else throw new UserServiceException(ex);
    }

    @Override
    public boolean checkIfStudentPassCourseById(Student student, int courseId) {
        List<Enrollment> Enrollments= enrollmentDao.getClassesByStudentId(student.getId());
        if(Enrollments==null) return false;


        for(Enrollment enrollment: Enrollments) if(enrollment.getStatus().equals("pass")
                &&getCourseByClass(webRegClassDao.getClassByEnrollment(enrollment)).getId()==courseId)return true;

        return false;
    }

    private List<Prerequisite> getPrerequisitesByClass(WebRegClass theClass) {

    Course course=getCourseByClass(theClass);
    List<Prerequisite> prerequisites=prerequisiteDao.getPrerequisiteByCourse(course);
    return prerequisites;
    }

    @Override
    public boolean checkIfStudentPassCourseById(Student student, Prerequisite prerequisite) {
        List<Enrollment> Enrollments= enrollmentDao.getClassesByStudentId(student.getId());
       // System.out.print(Enrollments.size());
        if(Enrollments==null) return false;
        int courseId=prerequisiteDao.getPrereqByPre(prerequisite);
        List<WebRegClass> classes=Enrollments.stream().map(c->webRegClassDao.getClassByEnrollment(c)).collect(Collectors.toList());

        if(classes==null) return false;

        for(Enrollment enrollment: Enrollments) if(enrollment.getStatus().equals("pass")&&getCourseByClass(webRegClassDao.getClassByEnrollment(enrollment)).getId()==courseId)return true;


        return false;
    }

    @Override
    public boolean checkTimeConflict(Student student, WebRegClass theClass) {
        //return false;
        List<Enrollment> enrollments= enrollmentDao.getClassesByStudentId(student.getId());
        List<Enrollment> Enrollments=enrollments.stream().filter(c->c.getStatus().equals("ongoing")).collect(Collectors.toList());
        if(Enrollments==null) return false;

        List<WebRegClass> classes=Enrollments.stream().map(c->webRegClassDao.getClassByEnrollment(c)).collect(Collectors.toList());
        if(classes==null) return false;

        List<Lecture> lectures=classes.stream().map(c->lectureDao.getLectureByClass(c)).collect(Collectors.toList());
        if(lectures==null) return false;

        Lecture theLecture=lectureDao.getLectureByClass(theClass);
        if(theLecture==null) return false;

        for(int i=0;i<classes.size();i++){
            if(getSemesterByClass(classes.get(i)).getId()!=getSemesterByClass(theClass).getId()) continue;

            if(lectures.get(i).getEnd_time().before(theLecture.getStart_time())  ||
                    lectures.get(i).getStart_time().after(theLecture.getEnd_time())||
                    lectures.get(i).getDay_of_week()!=theLecture.getDay_of_week()
            ) continue;
            else return true;
        }
        return false;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseDao.getAllCourses();
    }

    @Override
    public int createNewCourse(Course course) {

    return courseDao.createNewCourse(course);
    }

    @Override
    public boolean checkIfClassIsFull(WebRegClass theClass) {
        return enrollmentDao.getEnrollmentNumByClassId(theClass.getId())==theClass.getCapacity()?true:false;

    }

    public boolean checkIfDroppable(int studentId,WebRegClass theClass){
        Semester semester=semesterDao.getSemesterByClass(theClass);
        if(enrollmentDao.getStatus(studentId,theClass.getId())==null||!enrollmentDao.getStatus(studentId,theClass.getId()).equals("ongoing")) return false;

        Calendar c = Calendar.getInstance();
        c.setTime(semester.getStart_date());
        c.add(Calendar.DATE,14);

        Date date=c.getTime();

        Date d=new java.sql.Date(System.currentTimeMillis());

        if(!d.before(date)) throw new UserServiceException("it's been 14 days after class started.");

        return true;



    }
    public boolean checkIfWithdrawable(int studentId,WebRegClass theClass){

        Semester semester=semesterDao.getSemesterByClass(theClass);
        if(enrollmentDao.getStatus(studentId,theClass.getId())==null||!enrollmentDao.getStatus(studentId,theClass.getId()).equals("ongoing")) return false;

        Calendar c = Calendar.getInstance();
        c.setTime(semester.getStart_date());
        c.add(Calendar.DATE,14);

        Date date=c.getTime();

        Date d=new java.sql.Date(System.currentTimeMillis());

        if(!d.after(date)) throw new UserServiceException("it's less than 14 days before class started.");

        return true;
    }

    @Override
    public boolean checkIfWithdrawn(int studentId, int classId) {

        List<Enrollment> enrollments=enrollmentDao.getEnrollmentsByStudentClassId(studentId,classId);

        if(enrollments==null) return false;

        for(Enrollment enrollment:enrollments){
            if(enrollment.getStatus().equals("withdraw")) return true;
        }

        return false;
    }

    @Override
    public List<Student> getAllStudents() {
        return studentDao.getAllStudents();
    }

    @Override
    public List<WebRegClass> getAllStatusClassesbyStudentId(int studentId) {
        return webRegClassDao.getAllStatusClassesByStudentId(studentId);
    }

    @Override
    public int createNewClass(WebRegClass theClass) {
        return webRegClassDao.createNewClass(theClass);

    }

    @Override
    public List<WebRegClass> getAllClasses() {
        return webRegClassDao.getAllClasses();
    }

    @Override
    public void deactivateClassById(int classId) {
        webRegClassDao.deactivateClassById(classId);
    }

    @Override
    public void activateClassById(int classId) {
        webRegClassDao.activateClassById(classId);
    }

    @Override
    public int createApplicationByStudentClassId(int id, int classId)  {
        int id2=applicationDao.createApplicationByStudentClassId(id,classId);
        return id2;
    }

    @Override
    public List<Application> getApplicationsByStudentId(int id) {
        return applicationDao.getApplicationByStudentId(id);
    }

    @Override
    public List<Application> getAllPendingApplications() {
        return applicationDao.getPendingApplications();
    }

    @Override
    public void rejectOrApproveByIdFeedback(String status, int applicationId, String f) {
        applicationDao.rejectOrApproveByIdFeedback(status,applicationId,f);
    }

    @Override
    public void activateStudentById(int studentId) {
        studentDao.activateStudentById(studentId);
    }

    @Override
    public void deactivateStudentById(int studentId) {
        studentDao.deactivateStudentById(studentId);
    }

    @Override
    public void passStudentByClassId(int studentId, int classId) {
        enrollmentDao.passStudentByClassId(studentId,classId);
    }

    @Override
    public void failStudentByClassId(int studentId, int classId) {
        enrollmentDao.failStudentByClassId(studentId,classId);
    }

    @Override
    public String getRequestByApplicationId(int applicationId) {
        return applicationDao.getRequestByApplicationId(applicationId);
    }

    @Override
    public int getStudentIdByApplicationId(int applicationId) {
        return applicationDao.getStudentIdByApplicationId(applicationId);
    }

    @Override
    public int getClassIdByApplicationId(int applicationId) {
        return applicationDao.getClassIdByApplicationId(applicationId);
    }

    @Override
    public int getTotalPages(int studentId,int limit) {

            List<WebRegClass> classes=getClassesbyStudentId(studentId);
            if(classes==null) return 0;
            if(classes.size()%limit==0) return classes.size()/limit;
            else return classes.size()/limit+1;

    }

//    @Override
//    public List<WebRegClass> getClassesbyStudentIdPageLimit(int id, int page, int limit) {
//        List<WebRegClass> webRegClasses=webRegClassDao.getWebRegClassesByStudentId(id);
//
//        return webRegClasses.subList((page-1)*limit,Math.min((page)*limit,webRegClasses.size()));
//                //webRegClassDao.getWebRegClassesByStudentIdPageLimit(id,  page, limit);
//    }

    @Override
    public int getTotalPagesAdmin(int limit) {
        List<Student> students=studentDao.getAllStudents();
        if(students==null) return 0;
        if(students.size()%limit==0) return students.size()/limit;
        else return students.size()/limit+1;
                //getTotalPagesAdmin(limit);
    }

//    @Override
//    public List<Student> getStudentsByPageLimit(int page, int limit) {
//        List<Student> items = studentDao.getAllStudents();
//
//        return items.subList((page-1)*limit,Math.min((page)*limit,items.size()));
//    }

    @Override
    public String getCourseNameById(int courseId) {
        return courseDao.getCourseNameById(courseId);
    }

//    @Override
//    public Lecture getLectureById(int lecture_id) {
//        return lectureDao.getLectureById(lecture_id);
//    }

    @Override
    public void withdrawApplication(int applicationId, int id) {
        applicationDao.withdrawApplication(applicationId,id);
    }

    @Override
    public Application getApplicationById(int applicationId) {
        return applicationDao.getApplicationById(applicationId);
    }



    @Override
    public Department getDepartmentByStudent(Student c) {
        return departmentDao.getDepartmentByStudent(c);
    }

    @Override
    public Course getCourseByClass(WebRegClass c) {
      return courseDao.getCourseByClass(c);
       // return null;
    }

    @Override
    public Professor getProfessorByClass(WebRegClass c) {
        return professorDao.getProfessorByClass(c);
    }

    @Override
    public Semester getSemesterByClass(WebRegClass c) {
        return semesterDao.getSemesterByClass(c);
    }

    @Override
    public Department getDepartmentByProfessor(Professor c) {
        return departmentDao.getDepartmentByProfessor(c);
    }

    @Override
    public List<Enrollment> getEnrollmentByStudentId(int id) {
        return enrollmentDao.getEnrollmentByStudentId(id);
    }

//    @Override
//    public List<Enrollment> getEnrollmentByStudentIdPageLimit(int id, int page, int limit) {
//    List<Enrollment> items=enrollmentDao.getEnrollmentByStudentId(id);
//        return items.subList((page-1)*limit,Math.min((page)*limit,items.size()));
//    }

    @Override
    public WebRegClass getClassByEnrollment(Enrollment c) {

        return webRegClassDao.getClassByEnrollment(c);
    }

    @Override
    public Integer getActiveEnrollmentsByClass(WebRegClass c) {

        return enrollmentDao.getEnrollmentsByClass(c).stream().filter(s->s.getStatus().equals("ongoing")).collect(Collectors.toList()).size();
    }

    @Override
    public Department getDepartmentByCourse(Course course) {
        return departmentDao.getDepartmentByCourse(course);
    }

    @Override
    public Classroom getClassroomByClass(WebRegClass theWebRegClass) {
        return classroomDao.getClassroomByClass(theWebRegClass);
    }

    @Override
    public List<Prerequisite> getPrerequisiteByCourse(Course course) {
        return prerequisiteDao.getPrerequisiteByCourse(course);
    }

    @Override
    public Student getStudentByEnrollment(Enrollment c) {
        return studentDao.getStudentByEnrollment(c);
    }

    @Override
    public Student getStudentByApplication(Application c) {
        return studentDao.getStudentByApplication(c);

    }



    @Override
    public WebRegClass getWebRegClassByApplication(Application application) {
        return webRegClassDao.getWebRegClassByApplication(application);
    }



    @Override
    public void dropEnrollments(int studentId, int classId) {
        enrollmentDao.dropEnrollment(studentId,classId);
    }

    @Override
    public void withdrawEnrollment(int studentId, int classId) {



        enrollmentDao.withdrawEnrollment(studentId,classId);
    }

    @Override
    public int createEnrollmentApplication(int studentId, int classId) {
        int id=applicationDao.createEnrollmentApplication(studentId,classId);
        return id;
    }

    @Override
    public void addEnrollment(int studentId, int classId) {
            enrollmentDao.addEnrollment(studentId, classId);
    }


}
