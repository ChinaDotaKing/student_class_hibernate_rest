package org.example.dao.hibernateDao;

import lombok.Getter;
import lombok.Setter;
import org.example.dao.AbstractHibernateDao;
import org.example.dao.InterF.ApplicationDao;
import org.example.dao.InterF.EnrollmentDao;
import org.example.dao.InterF.LectureDao;
import org.example.domain.Application;
import org.example.domain.Enrollment;
import org.example.domain.Lecture;
import org.example.domain.WebRegClass;
import org.example.domain.hibernate.EnrollmentHibernate;
import org.example.domain.hibernate.LectureHibernate;
import org.example.domain.hibernate.WebRegClassHibernate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Getter
@Setter
@Repository("LectureDaoHibernate")
@Transactional
public class LectureHibernateDao extends AbstractHibernateDao<LectureHibernate> implements LectureDao {


    @Override
    public Lecture getLectureById(int id) {
        LectureHibernate lectureHibernate=sessionFactory.getCurrentSession().get(LectureHibernate.class,id);
        return  lectureHibernate;
    }

    @Override
    public Lecture getLectureByClass(WebRegClass c) {
        WebRegClassHibernate webRegClassHibernate=sessionFactory.getCurrentSession().get(WebRegClassHibernate.class,c.getId());

        return webRegClassHibernate.getLectureHibernate();
    }

//    @Override
//    public Lecture getLectureByClassId(int id) {
//        return null;
//    }


}
