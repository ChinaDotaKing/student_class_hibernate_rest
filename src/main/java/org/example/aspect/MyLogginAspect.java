package org.example.aspect;



import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;

import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyLogginAspect {
     Logger logger=LoggerFactory.getLogger(this.getClass());


     @Pointcut("execution(* org.example.controller..*(..))")
     void getter(){}
        long time=0;
    @Before("getter()")
    public void beforeControllerAdvice(JoinPoint joinPoint) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        logger.info("\n"+signature.getMethod().getName()+" start time is:"+System.currentTimeMillis()+"\n");
        time=System.currentTimeMillis();
        //System.out.println("\n=========>> Executing @Before advice on method");
    }

    @After("getter()")
    public void AfterControllerAdvice(JoinPoint joinPoint) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        logger.info("\n"+signature.getMethod().getName()+" finish time is:"+System.currentTimeMillis()+"\n");
        long elapsetime=System.currentTimeMillis()-time;
        logger.info("\nIt took:"+elapsetime+"milliseconds");
        //System.out.println("\n=========>> Executing @Before advice on method");
    }
}
