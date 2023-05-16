package org.example.service;

import org.example.dao.InterF.StudentDao;
import org.example.domain.Student;
import org.example.domain.hibernate.StudentHibernate;
import org.example.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService implements UserDetailsService {

    @Autowired
    StudentDao studentHibernateDao;


    public Optional<Student> validateLogin(String username, String password) {
        return studentHibernateDao.validateLogin(username, password);
    }


    public UserPrincipal getUserByName(String username)throws UsernameNotFoundException{
        UserPrincipal userPrincipal=new UserPrincipal(studentHibernateDao.getUserByName(username));

        //if(userPrincipal!=null)System.out.print(userPrincipal.getUsername());

        return userPrincipal;
    }

    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        UserPrincipal userPrincipal=new UserPrincipal(studentHibernateDao.getUserByName(username));

        //if(userPrincipal!=null)System.out.print(userPrincipal.getUsername());

        return userPrincipal;
    }
}
