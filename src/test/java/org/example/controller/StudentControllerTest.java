package org.example.controller;

import com.google.gson.Gson;
import org.example.dao.hibernateDao.StudentHibernateDao;
import org.example.model.request.UserSignupRequestModel;
import org.example.model.response.ClassRespEntity;
import org.example.model.response.UserSignupResponseModel;
import org.example.service.LoginService;
import org.example.service.UserService;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class StudentControllerTest {

    @MockBean
    private UserService userService;
    @MockBean
    private LoginService loginService;
    @MockBean
    StudentHibernateDao studentHibernateDao;
    @MockBean
    SessionFactory sessionFactory;

    @Autowired
    private MockMvc mockMvc;

//    @Test
//    void test_home() throws Exception {
//        ClassRespEntity expected = new ClassRespEntity(
//                "Ba 101", "CS5302","Engineering",
//                "stanford","2023 Spring","ongoing");
//        Mockito.when(userService.getUserSignupResponseModel(
//                Mockito.isA(UserSignupRequestModel.class)
//        )).thenReturn(expected);
//
//        UserSignupRequestModel user=new UserSignupRequestModel ("test1",
//                "123456789",
//                "Reic",
//                "Peter",
//                "test1@test.com",
//                1
//        );
//
//        Gson gson = new Gson();
//        String expectedJson = gson.toJson(expected);
//        String input=gson.toJson(user);
//        System.out.println(expectedJson);
//
//        // 1st way - using andExpect
//        mockMvc.perform(MockMvcRequestBuilders.post("/signup").contentType(MediaType.APPLICATION_JSON)
//                        .content(input))
//                .andExpect(MockMvcResultMatchers.status().isOk()) // status code 200
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.content().json(
//                        expectedJson));
//
//
//    }
}
