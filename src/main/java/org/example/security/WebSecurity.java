package org.example.security;

import org.example.dao.hibernateDao.StudentHibernateDao;
import org.example.service.LoginService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled=true,prePostEnabled=true)
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

        @Autowired
        LoginService loginService;

        @Autowired
        BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    CustomAuthenticationProvider customAuthenticationProvider;
        @Autowired
        StudentHibernateDao studentHibernateDao;

   //private UserDetailsService userDetailsService;

    // inject user-defined user detail service





        @Override
        protected  void configure(HttpSecurity http) throws Exception{
            http.csrf().disable()
                    .authorizeRequests()
                    .antMatchers(HttpMethod.POST,"/login")
                    .permitAll()
                    .antMatchers(HttpMethod.POST,"/signup")
                    .permitAll()
//                    .antMatchers(HttpMethod.GET,"/admin/**")
//                    .hasAuthority("admin")
                    .antMatchers(HttpMethod.GET,"/actuator/**")
                    .permitAll()
                    .antMatchers("/v2/api-docs","/configuration/**","/swagger*/**","/webjars/**","/v3/api-docs/**")
                    .permitAll()
                    .anyRequest().authenticated().and()
                    .addFilter(getAuthenticationFilter())
                    .addFilter(new AuthorizationFilter(authenticationManager(),studentHibernateDao ) )
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            ;

//            http.authorizeRequests()
//                    .antMatchers(HttpMethod.PUT,"/login")
//                    .denyAll()
//                    .antMatchers(HttpMethod.DELETE,"/login")
//                    .denyAll()
//                    .antMatchers(HttpMethod.GET,"/login")
//                    .denyAll();
        }


        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception{
            auth.userDetailsService(loginService).passwordEncoder(bCryptPasswordEncoder);
           // auth.authenticationProvider(customAuthenticationProvider);
        }

        public AuthenticationFilter getAuthenticationFilter() throws Exception{
            final AuthenticationFilter filter = new AuthenticationFilter(authenticationManager());
            filter.setFilterProcessesUrl("/login");

            return filter;
        }




}
