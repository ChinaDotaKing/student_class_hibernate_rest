package org.example.dao.hibernateDao;


import lombok.Getter;
import lombok.Setter;
import org.example.dao.AbstractHibernateDao;
import org.example.dao.InterF.WebRegClassDao;
import org.example.domain.*;
import org.example.domain.hibernate.*;

import org.example.exceptions.UserServiceException;
import org.hibernate.Session;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
@Setter
@Repository("WebRegClassDaoHibernate")
@Transactional
public class WebRegClassHibernateDao extends AbstractHibernateDao<WebRegClassHibernate> implements WebRegClassDao {

    @Override
    public WebRegClass getWebRegClassById(int id) {
        WebRegClassHibernate webRegClassHibernate=sessionFactory.getCurrentSession().get(WebRegClassHibernate.class,id);
        return  webRegClassHibernate;
    }

    @Override
    public List<WebRegClass> getWebRegClassesByCourseId(int id) {
        Session session=sessionFactory.getCurrentSession();

//        Transaction transaction = session.beginTransaction();


        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        // result class

        CourseHibernate courseHibernate=session.load(CourseHibernate.class,id);

        List<WebRegClass> webRegClasses=new ArrayList<>(courseHibernate.getWebRegClassHibernates());

        //List<WebRegClass> items =cr.list();
//        session.close();
        return webRegClasses;
    }

    @Override
    public List<WebRegClass> getWebRegClassesByStudentId(int id) {

        Session session=sessionFactory.getCurrentSession();

//        Transaction transaction = session.beginTransaction();
        //System.out.print(session.get(StudentHibernate.class,id).getId());

        List<EnrollmentHibernate> entityList=new ArrayList<>(session.get(StudentHibernate.class,id).getEnrollmentHibernates());

        List<WebRegClass> webRegClasses=entityList.stream().map(c->
        {
            return c.getWebRegClassHibernate();
        }).collect(Collectors.toList());
//        transaction.commit();
//        session.close();
         System.out.print(webRegClasses.size());

        return webRegClasses;
    }

    @Override
    public List<WebRegClass> getAllActiveClasses() {
        Session session=sessionFactory.getCurrentSession();

//        Transaction transaction = session.beginTransaction();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        // result class
        CriteriaQuery<WebRegClassHibernate> criteriaQuery = criteriaBuilder.createQuery(WebRegClassHibernate.class);
        Root<WebRegClassHibernate> aRoot=criteriaQuery.from(WebRegClassHibernate.class);


        Predicate predicateA=criteriaBuilder.equal(aRoot.get("is_active"), true);
        criteriaQuery.where(predicateA);
        List<WebRegClassHibernate> items = session.createQuery(criteriaQuery).getResultList();
        List<WebRegClassHibernate> res1=items.stream().sorted((a,b)->a.getSemesterHibernate().getStart_date().after(b.getSemesterHibernate().getStart_date())?-1:1).collect(Collectors.toList());

        List<WebRegClass> res=res1.stream().filter(a->a.getSemesterHibernate().getEnd_date().after(new Date(System.currentTimeMillis()))).collect(Collectors.toList());
//        for(WebRegClassHibernate webRegClassHibernate:res1){
//            //System.out.print(webRegClassHibernate.getCourseHibernate().getCourse_name()+"\n\n\n\n" );
//            if(webRegClassHibernate.getSemesterHibernate().getEnd_date().after(new Date(System.currentTimeMillis())))
//                continue;
//
//
//                else res.add(webRegClassHibernate);
//        }
        return res;

//        session.close();
        //return res;
    }
    @Cacheable("classes")
    @Override
    public List<WebRegClass> getAllClasses() {
        Session session=sessionFactory.getCurrentSession();

//        Transaction transaction = session.beginTransaction();
//
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        // result class
        CriteriaQuery<WebRegClassHibernate> criteriaQuery = criteriaBuilder.createQuery(WebRegClassHibernate.class);
//        Root<WebRegClassHibernate> aRoot=criteriaQuery.from(WebRegClassHibernate.class);
        criteriaQuery.from(WebRegClassHibernate.class);

//        Predicate predicateA=criteriaBuilder.equal(aRoot.get("is_active"), true);
//        criteriaQuery.where(predicateA);
        List<WebRegClassHibernate> items = session.createQuery(criteriaQuery).getResultList();

        List<WebRegClass> res=items.stream().filter(a->
                        a.getSemesterHibernate().getEnd_date().after(new Date(System.currentTimeMillis())))
                .sorted((a,b)->a.getSemesterHibernate().getStart_date().after(b.getSemesterHibernate().getStart_date())?1:-1).collect(Collectors.toList());

//        session.close();
        return res;
    }

    @Override
    public List<WebRegClass> getAllStatusClassesByStudentId(int studentId) {
        Session session=sessionFactory.getCurrentSession();

//        Transaction transaction = session.beginTransaction();

//        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
//        // result class
//        CriteriaQuery<WebRegClass> criteriaQuery = criteriaBuilder.createQuery(WebRegClass.class);
//        Root<WebRegClassHibernate> aRoot=criteriaQuery.from(WebRegClassHibernate.class);
//
//        criteriaQuery.from(WebRegClassHibernate.class);
//
//        Predicate predicateA=criteriaBuilder.equal(aRoot.get("is_active"), true);
////        criteriaQuery.where(predicateA);
//        List<WebRegClass> items = session.createQuery(criteriaQuery).getResultList();

        Set<EnrollmentHibernate> enrollmentHibernates=session.get(StudentHibernate.class,studentId).getEnrollmentHibernates();

        List<WebRegClass> webRegClassHibernates=new ArrayList<>();

        for(EnrollmentHibernate enrollmentHibernate:enrollmentHibernates){
            webRegClassHibernates.add(enrollmentHibernate.getWebRegClassHibernate());
        }

//        session.close();
        return webRegClassHibernates;
    }

    @Override
    public int createNewClass(WebRegClass theClass) {
        Session session=sessionFactory.getCurrentSession();

//        Transaction transaction = session.beginTransaction();


        CourseHibernate courseHibernate=session.get(CourseHibernate.class,theClass.getCourse_id());
        if(courseHibernate==null) throw new UserServiceException("Course not found");
        SemesterHibernate semsterHibernate=session.get(SemesterHibernate.class,theClass.getSemester_id());
        if(semsterHibernate==null) throw new UserServiceException("Semester not found");
        ProfessorHibernate professorHibernate=session.get(ProfessorHibernate.class,theClass.getProfessor_id());
        if(professorHibernate==null) throw new UserServiceException("professor not found");
        ClassroomHibernate classroomHibernate=session.get(ClassroomHibernate.class,theClass.getClassroom_id());
        if(classroomHibernate==null) throw new UserServiceException("classroom not found");
        LectureHibernate lectureHibernate=session.get(LectureHibernate.class,theClass.getLecture_id());
        if(lectureHibernate==null) throw new UserServiceException("lecture not found");


        WebRegClassHibernate webRegClassHibernate=new WebRegClassHibernate();

        webRegClassHibernate.setCourseHibernate(courseHibernate);
        webRegClassHibernate.setSemesterHibernate(semsterHibernate);
        webRegClassHibernate.setProfessorHibernate(professorHibernate);
        webRegClassHibernate.setClassroomHibernate(classroomHibernate);
        webRegClassHibernate.setCapacity(theClass.getCapacity());
        webRegClassHibernate.set_active(true);
        webRegClassHibernate.setLectureHibernate(lectureHibernate);


        session.save(webRegClassHibernate);

//        transaction.commit();
//        session.close();
        return webRegClassHibernate.getId();
    }

    @Override
    public void deactivateClassById(int classId) {
        Session session=sessionFactory.getCurrentSession();

//        Transaction transaction = session.beginTransaction();

        WebRegClassHibernate webRegClassHibernate=session.get(WebRegClassHibernate.class,classId);

        webRegClassHibernate.set_active(false);

        session.update(webRegClassHibernate);
//        transaction.commit();

//        session.close();
    }

    @Override
    public void activateClassById(int classId) {
        Session session=sessionFactory.getCurrentSession();

//        Transaction transaction = session.beginTransaction();

        WebRegClassHibernate webRegClassHibernate=session.get(WebRegClassHibernate.class,classId);

        webRegClassHibernate.set_active(true);

        session.update(webRegClassHibernate);
//        transaction.commit();

//        session.close();
    }

    @Override
    public WebRegClass getClassByEnrollment(Enrollment c) {
        EnrollmentHibernate enrollmentHibernate= sessionFactory.getCurrentSession().get(EnrollmentHibernate.class,c.getId());

        return enrollmentHibernate.getWebRegClassHibernate();
    }

    @Override
    public WebRegClass getWebRegClassByApplication(Application application) {
        ApplicationHibernate applicationHibernate=sessionFactory.getCurrentSession().get(ApplicationHibernate.class,application.getId());


        return applicationHibernate.getWebRegClassHibernate();
    }


}
