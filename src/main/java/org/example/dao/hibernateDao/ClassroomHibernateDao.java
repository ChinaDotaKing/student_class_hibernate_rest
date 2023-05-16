package org.example.dao.hibernateDao;


import lombok.Getter;
import lombok.Setter;
import org.example.dao.AbstractHibernateDao;
import org.example.dao.InterF.ApplicationDao;
import org.example.dao.InterF.ClassroomDao;
import org.example.domain.Application;
import org.example.domain.Classroom;
import org.example.domain.WebRegClass;
import org.example.domain.hibernate.ApplicationHibernate;
import org.example.domain.hibernate.ClassroomHibernate;
import org.example.domain.hibernate.WebRegClassHibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Getter
@Setter
@Repository("ClassroomDaoHibernate")
@Transactional
public class ClassroomHibernateDao extends AbstractHibernateDao<ClassroomHibernate> implements ClassroomDao {


    @Override
    public Classroom getClassroomById(int id) {
        ClassroomHibernate classroomHibernate=sessionFactory.getCurrentSession().get(ClassroomHibernate.class,id);
        return  classroomHibernate;
    }

    @Override
    public Classroom getClassroomByClass(WebRegClass theWebRegClass) {
        WebRegClassHibernate webRegClassHibernate=sessionFactory.getCurrentSession().get(WebRegClassHibernate.class,theWebRegClass.getId());

        return webRegClassHibernate.getClassroomHibernate();
    }


}
