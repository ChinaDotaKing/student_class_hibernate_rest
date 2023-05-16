package org.example.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.User;
import org.example.SpringApplicationContext;
import org.example.domain.Lecture;
import org.example.domain.Student;
import org.example.domain.hibernate.StudentHibernate;
import org.example.model.request.UserLoginRequestModel;
import org.example.service.LoginService;
import org.example.service.UserService;
import org.example.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    @Autowired
    LoginService loginService;

    @Autowired
    UserService userService;


//    @Autowired
//    CustomAuthenticationProvider customAuthenticationProvider;
    private final AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager=authenticationManager;
    }



    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest req, HttpServletResponse resp

            ) throws BadCredentialsException {
        try {
            UserLoginRequestModel creds
                    =new ObjectMapper().readValue(req.getInputStream(),
                        UserLoginRequestModel.class
                    );



            List<GrantedAuthority> grantedAuthorityList=new ArrayList<>();
////            Student student=userService.getUserByname(creds.getUsername());
//            if( creds.getUsername().equals("admin")) grantedAuthorityList.add(new GrantedAuthority() {
//                @Override
//                public String getAuthority() {
//                    return "admin";
//                }
//            });
//            customAuthenticationProvider
            Authentication authentication= authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword()
                            , grantedAuthorityList
                    ));
//            if(!(authentication.getPrincipal()==null)) throw  new BadCredentialsException("bad credentials");
//            else

                return authentication;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse resp,
                                            FilterChain chain,
                                            Authentication auth
                                            ){
        String userName=((UserPrincipal) auth.getPrincipal() ).getUsername();

        try {
            String token=Jwts.builder()
                    .setSubject(userName)
                    .setExpiration(new Date(System.currentTimeMillis()
                    +SecurityConstants.EXPIRATION_TIME
                    )).signWith(SignatureAlgorithm.HS512,SecurityConstants.TOKEN_SECRET)
            .compact();



            //if(!userPrincipal) System.out.print("no user found");

            //UserPrincipal userPrincipal=loginService.getUserByName(userName);
            resp.addHeader(SecurityConstants.HEADER_STRING,SecurityConstants.TOKEN_PREFIX+token);


            //resp.addHeader("userId",String.valueOf(userPrincipal.get()));
            //resp.addHeader("userName",userPrincipal.getUsername());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
