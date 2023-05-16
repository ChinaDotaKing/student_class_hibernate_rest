package org.example.security;

import java.util.ArrayList;
import java.util.List;

import org.example.dao.hibernateDao.StudentHibernateDao;
import org.example.domain.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class CustomAuthenticationProvider implements AuthenticationProvider{

    @Autowired
    private StudentHibernateDao studentHibernateDao;

    @Autowired
    private PasswordEncoder pEncoder;

    @Override
    public  Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        Student student = studentHibernateDao.getUserByName(username);
        if (student!=null) {
            if (pEncoder.matches(password, student.getEncrypted_password())) {
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(student.is_admin()?"admin":"user"));
                return new UsernamePasswordAuthenticationToken(username, password, authorities);
            }else  {
                throw new BadCredentialsException("Invalid password");
            }
        } else {
            throw new BadCredentialsException("No user registered with this details");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
