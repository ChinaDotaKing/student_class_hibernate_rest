package org.example.security;

import io.jsonwebtoken.Jwts;
import org.apache.tomcat.util.http.parser.Authorization;
import org.example.dao.hibernateDao.StudentHibernateDao;
import org.example.domain.hibernate.StudentHibernate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    private final StudentHibernateDao studentHibernateDao;

    public AuthorizationFilter(AuthenticationManager authenticationManager
            ,StudentHibernateDao studentHibernateDao){
        super(authenticationManager);
        this.studentHibernateDao=studentHibernateDao;

    }




    @Override
    protected void doFilterInternal(
            HttpServletRequest req,
            HttpServletResponse resp,
            FilterChain chain) throws IOException, ServletException {

        String header =req.getHeader(SecurityConstants.HEADER_STRING);

        if(header==null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)){
            chain.doFilter(req,resp);
            return ;
        }

        UsernamePasswordAuthenticationToken authentication
                =getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(req,resp);
        //resp.setHeader(SecurityConstants.HEADER_STRING,req.getHeader(SecurityConstants.HEADER_STRING));
    }


    private UsernamePasswordAuthenticationToken getAuthentication(
            HttpServletRequest request){
        String token =request.getHeader(SecurityConstants.HEADER_STRING);

        if(token!=null){
            token=token.replace(SecurityConstants.TOKEN_PREFIX,"");

            String user = Jwts.parser()
                    .setSigningKey(SecurityConstants.TOKEN_SECRET)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();


            if(user !=null){
                StudentHibernate studentHibernate=studentHibernateDao.getUserByName(user);
                System.out.print(studentHibernate.getUsername());
                if(studentHibernate ==null) return null;

                UserPrincipal userPrincipal=new UserPrincipal(studentHibernate);

                return new UsernamePasswordAuthenticationToken(
                        userPrincipal,null,userPrincipal.getAuthorities());

            }

            return null;
        }

        return null;
    }

}


