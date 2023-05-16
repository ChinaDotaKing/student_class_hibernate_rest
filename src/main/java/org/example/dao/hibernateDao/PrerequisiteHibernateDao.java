package org.example.dao.hibernateDao;


import lombok.Getter;
import lombok.Setter;
import org.example.dao.AbstractHibernateDao;
import org.example.dao.InterF.ApplicationDao;
import org.example.dao.InterF.PrerequisiteDao;
import org.example.domain.Application;
import org.example.domain.Course;
import org.example.domain.Enrollment;
import org.example.domain.Prerequisite;
import org.example.domain.hibernate.CourseHibernate;
import org.example.domain.hibernate.EnrollmentHibernate;
import org.example.domain.hibernate.LectureHibernate;

import org.example.domain.hibernate.PrerequisiteHibernate;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@Repository("PrerequisiteDaoHibernate")
@Transactional
public class PrerequisiteHibernateDao extends AbstractHibernateDao<PrerequisiteHibernate> implements PrerequisiteDao {


    @Override
    public Prerequisite getPrerequisiteById(int id) {
        PrerequisiteHibernate prerequisteHibernate=sessionFactory.getCurrentSession().load(PrerequisiteHibernate.class,id);
        return  prerequisteHibernate;
    }

    @Override
    public List<Prerequisite> getPrerequisiteByCourse(Course course) {
        Session session=sessionFactory.getCurrentSession();

//        Transaction transaction = session.beginTransaction();


        CourseHibernate cour=session.get(CourseHibernate.class, course.getId());

        List<Prerequisite> pres=new ArrayList<>(cour.getPrerequisiteHibernates2());;//.stream().collect(Collectors.toList());

        //List<Prerequisite> items =cr.list();
//        transaction.commit();
//        session.close();
        return pres;
    }

    @Override
    public int getPrereqByPre(Prerequisite prerequisite) {
        PrerequisiteHibernate prerequisiteHibernate=sessionFactory.getCurrentSession().get(PrerequisiteHibernate.class,prerequisite.getId());

        return prerequisiteHibernate.getPre_req().getId();
    }


}
