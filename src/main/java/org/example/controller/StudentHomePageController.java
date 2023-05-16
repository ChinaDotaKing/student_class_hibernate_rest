package org.example.controller;

import io.jsonwebtoken.Jwts;
import lombok.Setter;
import org.example.domain.*;
import org.example.domain.hibernate.StudentHibernate;
import org.example.exceptions.UserServiceException;
import org.example.model.response.*;
import org.example.model.result.ErrorMessages;
import org.example.model.result.OperationStatusModel;
import org.example.model.result.RequestOperationName;
import org.example.model.result.RequestOperationStatus;
import org.example.security.SecurityConstants;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Setter
@RestController
@RequestMapping("/")
public class StudentHomePageController {
    @Autowired
    UserService userService;


    @GetMapping("/class/all/{page}/{limit}")
    public List<ClassRespEntity> home(@PathVariable int page,@PathVariable int limit, HttpServletRequest request) {
        //page+=1;
        Student student=null;
        String token =request.getHeader(SecurityConstants.HEADER_STRING);
        System.out.print(token);
        if(token!=null){
            token=token.replace(SecurityConstants.TOKEN_PREFIX,"");

            String user = Jwts.parser()
                    .setSigningKey(SecurityConstants.TOKEN_SECRET)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();


            if(user !=null)
                student= userService.getUserByname(user);
        }



        int totalPages=userService.getTotalPages(student.getId(),limit);
        System.out.print(totalPages);
        if(page<0) page=1;
        else if(page>=totalPages) page=totalPages;
        else page=page+1;

            List<Enrollment> enrollments=userService.getEnrollmentByStudentId( student.getId() );

            List<ClassRespEntity> classRespEntities=userService.getClassRespEntitiesFromEnrollments(enrollments);




        return classRespEntities.subList((page-1)*limit,Math.min((page)*limit,classRespEntities.size()));
    }


    @GetMapping("/class/all")
    public List<StudentClassManaResp> class_management_Page(HttpServletRequest request) {

//        System.out.print(userService.checkIfStudentPassCourseById(student,2));
//        System.out.print(student.getId());

        Student student=null;
        String token =request.getHeader(SecurityConstants.HEADER_STRING);
        System.out.print(token);
        if(token!=null){
            token=token.replace(SecurityConstants.TOKEN_PREFIX,"");

            String user = Jwts.parser()
                    .setSigningKey(SecurityConstants.TOKEN_SECRET)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();


            if(user !=null)
                student= userService.getUserByname(user);
        }

        List<StudentClassManaResp> studentClassManaResps=userService.getStudentClassManaResp(student.getId());

        return studentClassManaResps;
    }


    @GetMapping("/class/{classId}")
    public ClassDetailRespModel classPage(@PathVariable int classId,
                            HttpServletRequest request) {



        Optional<WebRegClass> theClass = Optional.ofNullable(userService.getWebRegClassById(classId));
        ClassDetailRespModel classDetailRespModel=null;
        Student student=null;
        if (theClass.isPresent()) {

        try{
            WebRegClass theWebRegClass = theClass.get();
            classDetailRespModel=userService.getClassDetailByClass(theWebRegClass);
            String token =request.getHeader(SecurityConstants.HEADER_STRING);

            if(token!=null){
                token=token.replace(SecurityConstants.TOKEN_PREFIX,"");

                String user = Jwts.parser()
                        .setSigningKey(SecurityConstants.TOKEN_SECRET)
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject();


                if(user !=null)
                    student= userService.getUserByname(user);

                if(!student.is_admin()) classDetailRespModel.setCurEnrolledStudents(null);
            }


        }
        catch (NullPointerException e){
            e.printStackTrace();
        }

        }

        else throw new UserServiceException(ErrorMessages.CLASS_NOT_FOUNT.getErrorMessage());

        return classDetailRespModel;


    }

    @PostMapping("/class/{classId}/{button}")
    public Object classPagePost(@PathVariable int classId,
                            @PathVariable String button,
                            HttpServletRequest request) {

        Student student=null;
        WebRegClass theClass;


        String token =request.getHeader(SecurityConstants.HEADER_STRING);

        if(token!=null){
            token=token.replace(SecurityConstants.TOKEN_PREFIX,"");

            String user = Jwts.parser()
                    .setSigningKey(SecurityConstants.TOKEN_SECRET)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();


            if(user !=null)
                student= userService.getUserByname(user);
        }


        theClass=userService.getWebRegClassById(classId);
        if(theClass==null) throw new UserServiceException(ErrorMessages.CLASS_NOT_FOUNT.getErrorMessage());
        OperationStatusModel returnValue = new OperationStatusModel();

        if (button.equals("add")) {

            returnValue.setOperationName(RequestOperationName.ADD_CLASS.name());




            if(!userService.checkIfStudentEligible(student,theClass)) {
                returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
                return returnValue;
            }

            if(!userService.checkIfClassIsFull(theClass)) {
            userService.addEnrollment(student.getId(),theClass.getId());
                returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

                return returnValue;
            }
            else
                button = "apply";

        }
        else if (button.equals("drop")) {

            returnValue.setOperationName(RequestOperationName.DROP_CLASS.name());


            if(!userService.checkIfDroppable(student.getId(),theClass)) {
                returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
                return returnValue;
            }
            else {
                userService.dropEnrollments(student.getId(),classId);
                returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

                return returnValue;
            }
        }
        else if (button.equals("withdraw")) {

            returnValue.setOperationName(RequestOperationName.WITHDRAW_CLASS.name());
            List<Application> application=userService.getApplicationsByStudentId(student.getId());
            for(Application application1:application)
            {
                if(application1.getRequest().equals("Withdraw")&&application1.getStatus().equals("ongoing") )
                    throw new UserServiceException("The withdraw application is ongoing");
                if(application1.getRequest().equals("Withdraw")&&application1.getStatus().equals("Reject") )
                    throw new UserServiceException("The withdraw application has been rejected");
            }

            if(!userService.checkIfWithdrawable(student.getId(),theClass)) {
                returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
                return returnValue;
            }
            else {
                int id=userService.createEnrollmentApplication(student.getId(),theClass.getId());
                //create withdraw application
                AdminApplicationResp adminApplicationResp=userService.getAdminApplicationRespByApplicationId(id);
                return adminApplicationResp;
            }
        }
        if (button.equals("apply")) {

            returnValue.setOperationName(RequestOperationName.APPLY.name());

            List<Application> application=userService.getApplicationsByStudentId(student.getId());
            for(Application application1:application)
            {
                if(application1.getRequest().equals("Add")&&application1.getStatus().equals("ongoing") )
                    throw new UserServiceException("The Add application is ongoing");
                if(application1.getRequest().equals("Add")&&application1.getStatus().equals("Reject") )
                    throw new UserServiceException("The Add application has been rejected");
            }
                int id2=userService.createApplicationByStudentClassId(student.getId(),classId);
                AdminApplicationResp applicationResp=userService.getAdminApplicationRespByApplicationId(id2);

            return applicationResp;

        }

        return button;
    }


    @GetMapping("/application/all")
    public  List<ApplicationResp> applicationManagement(
                                        HttpServletRequest request) {
        String token =request.getHeader(SecurityConstants.HEADER_STRING);

        Student student=null;
        if(token!=null){
            token=token.replace(SecurityConstants.TOKEN_PREFIX,"");

            String user = Jwts.parser()
                    .setSigningKey(SecurityConstants.TOKEN_SECRET)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();


            if(user !=null)
                student= userService.getUserByname(user);
        }

        List<ApplicationResp> applicationResps=userService.getApplicationRespByStudentId(student.getId());







        return applicationResps;

    }


    @DeleteMapping("/application/{applicationId}")
    public OperationStatusModel applicationdelete( @PathVariable int applicationId, HttpServletRequest request){

        String token =request.getHeader(SecurityConstants.HEADER_STRING);

        Student student=null;
        if(token!=null){
            token=token.replace(SecurityConstants.TOKEN_PREFIX,"");

            String user = Jwts.parser()
                    .setSigningKey(SecurityConstants.TOKEN_SECRET)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();


            if(user !=null)
                student= userService.getUserByname(user);
        }



                userService.withdrawApplication(applicationId,student.getId());
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.CANCEL_APPLICATION.name());
        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        return returnValue;
        }



}
