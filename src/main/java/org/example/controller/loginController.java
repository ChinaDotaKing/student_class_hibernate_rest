package org.example.controller;


import lombok.Setter;
import org.example.domain.*;
import org.example.domain.hibernate.DepartmentHibernate;
import org.example.domain.hibernate.StudentHibernate;
//import org.example.domain.jdbc.StudentJdbc;
import org.example.model.request.UserLoginRequestModel;
import org.example.model.request.UserSignupRequestModel;
import org.example.model.response.UserSignupResponseModel;
import org.example.service.LoginService;
import org.example.service.UserService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Setter
@RestController
@RequestMapping("/")
public class loginController {


    private final LoginService loginService;

    @Autowired
    UserService userService;


    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;



    @Autowired
    public loginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor =new StringTrimmerEditor(true);

        dataBinder.registerCustomEditor(String.class,stringTrimmerEditor);
    }





    @PostMapping("/signup")
    public UserSignupResponseModel postSignup(@Valid @RequestBody UserSignupRequestModel userDetails) {

        userService.createNewStudentForRest(userDetails);

        UserSignupResponseModel userSignupResponseModel=userService.getUserSignupResponseModel(userDetails);

        return userSignupResponseModel;

    }




}
