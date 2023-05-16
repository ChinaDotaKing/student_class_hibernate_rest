package org.example.dao.hibernateDao;


import lombok.Getter;
import lombok.Setter;
import org.example.dao.AbstractHibernateDao;
import org.example.dao.InterF.EnrollmentDao;
import org.example.domain.Application;
import org.example.domain.Enrollment;
import org.example.domain.Student;
import org.example.domain.WebRegClass;
import org.example.domain.hibernate.*;
import org.example.exceptions.UserServiceException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@Repository("EnrollmentDaoHibernate")
@Transactional
public class EnrollmentHibernateDao extends AbstractHibernateDao<EnrollmentHibernate> implements EnrollmentDao {


    @Override
    public Enrollment getEnrollmentById(int id) {
        EnrollmentHibernate enrollmentHibernate=sessionFactory.getCurrentSession().load(EnrollmentHibernate.class,id);
        return  enrollmentHibernate;
    }

    @Override
    public List<Enrollment> getActiveEnrollmentsByClassId(int classId) {
        Session session=sessionFactory.getCurrentSession();



        List<Enrollment> items1= new ArrayList<>(session.get(WebRegClassHibernate.class,classId).getEnrollmentHibernates());

        List<Enrollment> items2=items1.stream().filter(c->c.getStatus().equals("ongoing" )).collect(Collectors.toList());
        return items2;
    }

    @Override
    public boolean getEnrollmentStatus(int studentId, int classId) {
        Session session=sessionFactory.getCurrentSession();

//        Transaction transaction = session.beginTransaction();

//        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
//        // result class
//        CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
//        Root<EnrollmentHibernate> aRoot=criteriaQuery.from(EnrollmentHibernate.class);
//        aRoot.join("Student", JoinType.INNER);
//        aRoot.join("WebRegClass", JoinType.INNER);
//
//        criteriaQuery.multiselect(
//                aRoot.get("status")
//        );
//
//
//        Predicate predicateA = criteriaBuilder.equal(aRoot.get("Student").get("id"), studentId);
//        Predicate predicateB = criteriaBuilder.equal(aRoot.get("WebRegClass").get("id"), classId);
//
//        Predicate predicateC=criteriaBuilder.and(predicateB,predicateA);
//        criteriaQuery.where(predicateC);


        StudentHibernate items = session.get(StudentHibernate.class,studentId);

        for(EnrollmentHibernate item:items.getEnrollmentHibernates()){
            if(item.getStatus().equals("ongoing")&&item.getWebRegClassHibernate().getId()==classId) return true;
        }

        return false;


                //session.createQuery(criteriaQuery).getResultList();

//        session.close();
//        return items.size()!=0?true:false;
    }

    @Override
    public String getStatus(int studentId, int classId) {
        Session session=sessionFactory.getCurrentSession();

//

        StudentHibernate items = session.get(StudentHibernate.class,studentId);

        for(EnrollmentHibernate item:items.getEnrollmentHibernates()){
            if(item.getWebRegClassHibernate().getId()==classId) return item.getStatus();
        }

        return null;
    }

    @Override
    public List<Enrollment> getClassesByStudentId(int id) {
        Session session=sessionFactory.getCurrentSession();

//
        StudentHibernate studentHibernate=session.get(StudentHibernate.class,id);

        List<Enrollment> items = studentHibernate.getEnrollmentHibernates().stream().filter(c->c.getStatus().equals("ongoing")||c.getStatus().equals("pass")).collect(Collectors.toList());



        return items;
    }

    @Override
    public int getEnrollmentNumByClassId(int id) {
        Session session=sessionFactory.getCurrentSession();



        WebRegClassHibernate webRegClassHibernate=session.load(WebRegClassHibernate.class,id);

        return webRegClassHibernate.getEnrollmentHibernates().stream().filter(c->c.getStatus().equals("ongoing")).collect(Collectors.toList()).size();

    }

    @Override
    public void addEnrollment(int studentId, int classId) {
        Session session=sessionFactory.getCurrentSession();

//        Transaction transaction = session.beginTransaction();
        StudentHibernate studentHibernate=session.get(StudentHibernate.class,studentId);
        WebRegClassHibernate webRegClassHibernate=session.get(WebRegClassHibernate.class,classId);
        EnrollmentHibernate enrollmentHibernate=new EnrollmentHibernate();

        enrollmentHibernate.setStudentHibernate(studentHibernate);
        enrollmentHibernate.setWebRegClassHibernate(webRegClassHibernate);
        enrollmentHibernate.setStatus("ongoing");


        session.saveOrUpdate(enrollmentHibernate);
//        transaction.commit();

//        session.close();
    }

    @Override
    public void dropEnrollment(int studentId, int classId) {
        Session session=sessionFactory.getCurrentSession();

//

        StudentHibernate items = session.get(StudentHibernate.class,studentId);

        for(EnrollmentHibernate item:items.getEnrollmentHibernates()){
            if(item.getStatus().equals("ongoing")&&item.getWebRegClassHibernate().getId()==classId) session.delete(item);
        }
//        transaction.commit();
//        session.close();

    }

    @Override
    public void withdrawEnrollment(int studentId, int classId) {

        Session session=sessionFactory.getCurrentSession();

//        Transaction transaction = session.beginTransaction();
//
//
//        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
//        // result class
//        CriteriaQuery<Integer> criteriaQuery = criteriaBuilder.createQuery(Integer.class);
//        Root<EnrollmentHibernate> aRoot=criteriaQuery.from(EnrollmentHibernate.class);
//        aRoot.join("Student", JoinType.INNER);
//        aRoot.join("WebRegClass", JoinType.INNER);
//
//        criteriaQuery.multiselect(
//                aRoot.get("id")
//        );
//
//
//        Predicate predicateA = criteriaBuilder.equal(aRoot.get("WebRegClass").get("id"), classId);
//        Predicate predicateB = criteriaBuilder.equal(aRoot.get("Student").get("id"), studentId);
//        criteriaQuery.where(criteriaBuilder.and(predicateB,predicateA));
//        List<Integer> items = session.createQuery(criteriaQuery).getResultList();
//        EnrollmentHibernate enrollmentHibernate=session.load(EnrollmentHibernate.class,items.get(0));
        StudentHibernate items = session.get(StudentHibernate.class,studentId);

        for(EnrollmentHibernate item:items.getEnrollmentHibernates()){
            if(item.getStatus().equals("ongoing")&&item.getWebRegClassHibernate().getId()==classId)
        item.setStatus("withdraw");
        }
        session.save(items);
//        transaction.commit();
//        session.close();

    }

    @Override
    public List<Enrollment> getEnrollmentsByStudentClassId(int studentId, int classId) {
        Session session=sessionFactory.getCurrentSession();

//        Transaction transaction = session.beginTransaction();


//

        StudentHibernate items = session.get(StudentHibernate.class,studentId);
        List<Enrollment> res=new ArrayList<>();
        for(EnrollmentHibernate item:items.getEnrollmentHibernates()){
            if(item.getWebRegClassHibernate().getId()==classId) res.add(item);
        }
        //session.close();
        return res;

    }

    @Override
    public void failStudentByClassId(int studentId, int classId) {
        Session session=sessionFactory.getCurrentSession();

//        Transaction transaction = session.beginTransaction();


//
        StudentHibernate items = session.get(StudentHibernate.class,studentId);

        if(items==null) throw new UserServiceException("Student not found!!");
        List<EnrollmentHibernate> enrollmentHibernates=items.getEnrollmentHibernates().stream().filter(c->c.getWebRegClassHibernate().getId()==classId).collect(Collectors.toList());
        if(enrollmentHibernates.size()==0) throw new UserServiceException("Student is not enrollment in the class!!");

        for(EnrollmentHibernate item:items.getEnrollmentHibernates()){
            if(item.getWebRegClassHibernate().getId()==classId) item.setStatus("fail");
        }
        session.save(items);
//        transaction.commit();
//        session.close();
    }

    @Override
    public void passStudentByClassId(int studentId, int classId) {

        Session session=sessionFactory.getCurrentSession();

        StudentHibernate items = session.get(StudentHibernate.class,studentId);
        if(items==null) throw new UserServiceException("Student not found!!");
        List<EnrollmentHibernate> enrollmentHibernates=items.getEnrollmentHibernates().stream().filter(c->c.getWebRegClassHibernate().getId()==classId).collect(Collectors.toList());
        if(enrollmentHibernates.size()==0) throw new UserServiceException("Student is not enrollment in the class!!");

        for(EnrollmentHibernate item:items.getEnrollmentHibernates()){
            if(item.getWebRegClassHibernate().getId()==classId) item.setStatus("pass");
        }
        session.save(items);


    }

    @Override
    public List<Enrollment> getEnrollmentByStudentId(int id) {

        Session session=sessionFactory.getCurrentSession();

//        Transaction transaction = session.beginTransaction();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        // result class
        CriteriaQuery<EnrollmentHibernate> criteriaQuery = criteriaBuilder.createQuery(EnrollmentHibernate.class);
        criteriaQuery.from(EnrollmentHibernate.class);
        List<Enrollment> items = session.createQuery(criteriaQuery).getResultList().stream().filter(c->c.getStudentHibernate().getId()==id).collect(Collectors.toList());

//        session.close();
        return items;

    }

    @Override
    public List<Enrollment> getEnrollmentsByClass(WebRegClass c) {

        WebRegClassHibernate webRegClassHibernate=sessionFactory.getCurrentSession().get(WebRegClassHibernate.class,c.getId());
        List<Enrollment> enrollments=webRegClassHibernate.getEnrollmentHibernates().stream().collect(Collectors.toList());

        return enrollments;
    }


}
