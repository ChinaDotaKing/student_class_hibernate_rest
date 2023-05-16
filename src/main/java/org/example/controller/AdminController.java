package org.example.controller;


import org.example.dao.hibernateDao.DepartmentHibernateDao;
import org.example.domain.*;
import org.example.domain.hibernate.ApplicationHibernate;
//import org.example.domain.jdbc.CourseJdbc;
//import org.example.domain.jdbc.WebRegClassJdbc;
import org.example.domain.hibernate.CourseHibernate;
import org.example.domain.hibernate.DepartmentHibernate;
import org.example.domain.hibernate.WebRegClassHibernate;
import org.example.model.request.ClassCreationReq;
import org.example.model.request.CourseCreationRequestModel;
import org.example.model.response.*;
import org.example.model.result.OperationStatusModel;
import org.example.model.result.RequestOperationName;
import org.example.model.result.RequestOperationStatus;
import org.example.service.UserService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
//@PreAuthorize("hasRole('admin')")
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    UserService userService;


    @Autowired
    DepartmentHibernateDao departmentHibernateDao;


    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor =new StringTrimmerEditor(true);

        dataBinder.registerCustomEditor(String.class,stringTrimmerEditor);
    }

    @Cacheable("students")
    @GetMapping("/student/all/{page}/{limit}")
    public  List<AdminStuResp> home(@PathVariable int page,@PathVariable int limit,
                       HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
    System.out.print("hello");
            List<AdminStuResp> adminStuResps=userService.getAdminStuResps();


        int totalPages=userService.getTotalPagesAdmin(limit);
        System.out.print(totalPages);
        if(page<0) page=1;
        else if(page>=totalPages) page=totalPages;
        else page=page+1;



        return  adminStuResps.subList((page-1)*limit,Math.min((page)*limit,adminStuResps.size()));
    }


    @GetMapping("/student/{studentId}")
    public StudentDetailResp studentPage(@PathVariable int studentId,
                              HttpServletRequest request) {

        StudentDetailResp studentDetailResp=userService.getStudentDetailResp(studentId);




        return studentDetailResp;

    }

    @PatchMapping("/student/{studentId}/{isActive}")
    public String AcDeacStudent(@PathVariable int studentId,@PathVariable boolean isActive, HttpServletRequest request){


        if(isActive)
            userService.activateStudentById(studentId);

        else
            userService.deactivateStudentById(studentId);


        return "success";
    }

    @PatchMapping("/student/{studentId}/class/{classId}/{grade}")
    public OperationStatusModel passOrFail(@PathVariable int studentId,
                             @PathVariable int classId,
                             @PathVariable String grade,

                             HttpServletRequest request) {


        if(grade.equals("pass"))
            userService.passStudentByClassId(studentId,classId);

        else if(grade.equals("fail"))
            userService.failStudentByClassId(studentId,classId);;
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.PASS_FAIL.name());



        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

        return returnValue;

    }

    @GetMapping("/course/all")
    public List<AdminCourseResp> courseManagement( HttpServletRequest request) {


        List<AdminCourseResp> adminCourseResps=userService.getAdminCourseResps();


        return adminCourseResps;
    }

    @PostMapping("/course")
    public AdminCourseResp postSignup(@RequestBody CourseCreationRequestModel course, HttpServletRequest request) {
        CourseHibernate courseHibernate=new CourseHibernate(course.getCourse_name(),
                course.getCourse_code()

                ,(DepartmentHibernate)userService.getDepartmentById(course.getDepartment_id())
                , course.getDescription()

        );
        courseHibernate.setDept_id(course.getDepartment_id());

       int id= userService.createNewCourse(courseHibernate);


        AdminCourseResp adminCourseResp=userService.getAdminCourseRespById(id);

        return adminCourseResp;
    }

    @PostMapping("/class")
    public ClassDetailRespModel addClass(@Valid @RequestBody ClassCreationReq classCreationReq, BindingResult theBindingResult, HttpServletRequest request) {

        WebRegClassHibernate webRegClassHibernate=new WebRegClassHibernate();
        webRegClassHibernate.setCourse_id(classCreationReq.getCourse_id());
        webRegClassHibernate.setClassroom_id(classCreationReq.getClassroom_id());
        webRegClassHibernate.setCapacity(classCreationReq.getCapacity());
        webRegClassHibernate.setProfessor_id(classCreationReq.getProfessor_id());
        webRegClassHibernate.setSemester_id(classCreationReq.getSemester_id());
        webRegClassHibernate.setLecture_id(classCreationReq.getLecture_id());



        int res=userService.createNewClass(webRegClassHibernate
        )
        ;
        WebRegClass webRegClassHibernate2=userService.getWebRegClassById(res);
        ClassDetailRespModel classDetailRespModel=userService.getClassDetailByClass(webRegClassHibernate2);

        return classDetailRespModel;

    }

    @GetMapping("/class/all")
    public List< AdminClassManaResp> class_management_Page(HttpServletRequest request) {
        List< AdminClassManaResp> adminClassManaResps=new ArrayList<>();
        adminClassManaResps=userService.getAdminClassManaResp();






        return adminClassManaResps;
    }

    @PatchMapping("/class/{classId}/{isActive}")
    public ClassDetailRespModel addClass(@PathVariable int classId,@PathVariable boolean isActive, HttpServletRequest request){



            if(!isActive) userService.deactivateClassById(classId);
            else  userService.activateClassById(classId);
        WebRegClass webRegClass=userService.getWebRegClassById(classId);
        ClassDetailRespModel classDetailRespModel=userService.getClassDetailByClass(webRegClass);


        return classDetailRespModel;
    }







    @GetMapping("/application/all")
    public  List<AdminApplicationResp> applicationManagement(Model theModel,
            HttpServletRequest request) {
        List<AdminApplicationResp> applicationResps=null;
        applicationResps=userService.getAdminApplicationResps();

        return applicationResps;

    }

    @PatchMapping("/application/{applicationId}")
    public AdminApplicationResp RejectOrApprove(@RequestParam boolean reject,
                                    @RequestParam String feedback,
                                  @PathVariable int applicationId,
                                        HttpServletRequest request) {
        //if(!((Student)request.getSession(false).getAttribute("student")).is_admin() ) return "login";
       // String f=request.getParameter("text-input");
        //String feedback=((Application)theModel.getAttribute("application")).getFeedback();
        //String button=request.getParameter("submit-button");




            if(!reject&&userService.getRequestByApplicationId(applicationId).equals("Add"))
                userService.addEnrollment(userService.getStudentIdByApplicationId(applicationId),userService.getClassIdByApplicationId(applicationId));

           else if(!reject&&userService.getRequestByApplicationId(applicationId).equals("Withdraw"))
            userService.withdrawEnrollment(userService.getStudentIdByApplicationId(applicationId),userService.getClassIdByApplicationId(applicationId));



            String status=reject?"Reject":"Approve";
            userService.rejectOrApproveByIdFeedback(status,applicationId,feedback);

        System.out.print(feedback);


        AdminApplicationResp applicationResp=userService.getAdminApplicationRespByApplicationId(applicationId);

        return applicationResp;
    }



}
