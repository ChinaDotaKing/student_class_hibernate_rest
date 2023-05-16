package org.example.dao.hibernateDao;


import lombok.Getter;
import lombok.Setter;
import org.example.dao.AbstractHibernateDao;
import org.example.dao.InterF.ApplicationDao;
import org.example.domain.Application;
import org.example.domain.hibernate.ApplicationHibernate;
import org.example.domain.hibernate.StudentHibernate;
import org.example.domain.hibernate.WebRegClassHibernate;

import org.example.exceptions.UserServiceException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@Repository("ApplicationDaoHibernate")
@Transactional
public class ApplicationHibernateDao extends AbstractHibernateDao<ApplicationHibernate>  implements ApplicationDao {



    @Autowired
    StudentHibernateDao studentHibernateDao;

    public ApplicationHibernateDao() {
        this.setClazz(ApplicationHibernate.class);
    }
    @Override
    public Application getApplicationById(int id) {
        ApplicationHibernate applicationHibernate=sessionFactory.getCurrentSession().get(ApplicationHibernate.class,id);
        return  applicationHibernate;
    }

    @Override
    public int createApplicationByStudentClassId(int id, int classId)  {
        Session session=sessionFactory.getCurrentSession();



        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        // result class
        CriteriaQuery<ApplicationHibernate> criteriaQuery = criteriaBuilder.createQuery(ApplicationHibernate.class);



        ApplicationHibernate applicationHibernate=new ApplicationHibernate();

        WebRegClassHibernate webRegClassHibernate=session.get(WebRegClassHibernate.class,classId);
        StudentHibernate studentHibernate=session.get(StudentHibernate.class,id);
        applicationHibernate.setStudent_id(id);
        applicationHibernate.setClass_id(classId);
        applicationHibernate.setWebRegClassHibernate(webRegClassHibernate);
        applicationHibernate.setCreationTime(new Timestamp(System.currentTimeMillis()));
        applicationHibernate.setStudentHibernate(studentHibernate);
        applicationHibernate.setRequest("Add");
        applicationHibernate.setStatus("Pending");
        applicationHibernate.setFeedback("");

        session.save(applicationHibernate);

//        session.close();
//        Predicate predicateA = criteriaBuilder.equal(aRoot.get("student_id"), id);
//        Predicate predicateB = criteriaBuilder.equal(aRoot.get("class_id"), classId);
//
//
//        Predicate finalPredicate
//                = criteriaBuilder.and(predicateA, predicateB);
//        criteriaQuery.where(finalPredicate);
//        List<ApplicationHibernate> items = session.createQuery(criteriaQuery).getResultList();


        return applicationHibernate.getId();
    }

    @Override
    public List<Application> getApplicationByStudentId(int studentId) {
        Session session=sessionFactory.getCurrentSession();





        StudentHibernate studentHibernate=session.get(StudentHibernate.class,studentId);

        List<Application> items =
                new ArrayList<>(studentHibernate.getApplicationHibernates()).stream().sorted((a,b)->a.getCreationTime().before(b.getCreationTime())?-1:1).collect(Collectors.toList());

        return items;

    }

    @Override
    public List<Application> getPendingApplications() {

        Session session=sessionFactory.getCurrentSession();



        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        // result class
        CriteriaQuery<ApplicationHibernate> criteriaQuery = criteriaBuilder.createQuery(ApplicationHibernate.class);
        criteriaQuery.from(ApplicationHibernate.class);
//        Root<ApplicationHibernate> aRoot=criteriaQuery.from(ApplicationHibernate.class);
//        aRoot.join("Student", JoinType.INNER);
//        aRoot.join("WebRegClass", JoinType.INNER);
//
//        criteriaQuery.multiselect(
//                aRoot.get("id"),
//                aRoot.get("Student").get("id"),
//                aRoot.get("WebRegClass").get("id"),
//                aRoot.get("creation_time"),
//                aRoot.get("request"),
//                aRoot.get("status"),
//                aRoot.get("feedback")
//        );
//
//
//        Predicate predicateA = criteriaBuilder.equal(aRoot.get("status"), "Pending");

//        criteriaQuery.where(predicateA);
        List<ApplicationHibernate> items = session.createQuery(criteriaQuery).getResultList().stream().filter(c->c.getStatus().equals("Pending")).collect(Collectors.toList());
        List<Application> items2=items.stream().sorted((a,b)->a.getCreationTime().before(b.getCreationTime())?-1:1).collect(Collectors.toList());
//        session.close();
        return items2;
    }

    @Override
    public void rejectOrApproveByIdFeedback(String status, int applicationId, String f) {
        Session session=sessionFactory.getCurrentSession();

//        Transaction transaction = session.beginTransaction();

        ApplicationHibernate applicationHibernate=session.get(ApplicationHibernate.class,applicationId);

        applicationHibernate.setStatus(status);

        applicationHibernate.setFeedback(f);


        session.save(applicationHibernate);
//        transaction.commit();

//        session.close();
    }

    @Override
    public String getRequestByApplicationId(int applicationId) {
        Session session=sessionFactory.getCurrentSession();

        ApplicationHibernate applicationHibernate=session.load(ApplicationHibernate.class,applicationId);

        String request=applicationHibernate.getRequest();
        return request;
    }

    @Override
    public int getStudentIdByApplicationId(int applicationId) {
        Session session=sessionFactory.getCurrentSession();

        ApplicationHibernate applicationHibernate=session.load(ApplicationHibernate.class,applicationId);
        StudentHibernate studentHibernate=applicationHibernate.getStudentHibernate();

        return studentHibernate.getId();
    }

    @Override
    public int getClassIdByApplicationId(int applicationId) {
        Session session=sessionFactory.getCurrentSession();


        ApplicationHibernate applicationHibernate=session.load(ApplicationHibernate.class,applicationId);
        WebRegClassHibernate weHibernate=applicationHibernate.getWebRegClassHibernate();
        return weHibernate.getId();
    }

    @Override
    public int createEnrollmentApplication(int studentId, int classId) {
        Session session=sessionFactory.getCurrentSession();



        StudentHibernate studentHibernate=session.get(StudentHibernate.class,studentId);
        WebRegClassHibernate webRegClassHibernate=session.get(WebRegClassHibernate.class,classId);


        ApplicationHibernate applicationHibernate=new ApplicationHibernate();

        applicationHibernate.setStudentHibernate(studentHibernate);
        applicationHibernate.setWebRegClassHibernate(webRegClassHibernate);
        applicationHibernate.setCreationTime(new Timestamp(System.currentTimeMillis()));
        applicationHibernate.setStatus("Pending");
        applicationHibernate.setRequest("Withdraw");
        applicationHibernate.setFeedback("");

        session.save(applicationHibernate);


        return applicationHibernate.getId();
    }

    @Override
    public void withdrawApplication(int applicationId, int id) {
        Session session=sessionFactory.getCurrentSession();



        ApplicationHibernate applicationHibernate=session.get(ApplicationHibernate.class,applicationId);
        if(applicationHibernate==null) throw new UserServiceException("Application not found");

        if(applicationHibernate.getStudentHibernate().getId()!=id) throw new UserServiceException("The application is not yours!");

        applicationHibernate.setStatus("Withdraw");

        session.save(applicationHibernate);

    }




}
