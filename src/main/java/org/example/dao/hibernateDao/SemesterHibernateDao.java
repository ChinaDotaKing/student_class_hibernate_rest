package org.example.dao.hibernateDao;

import lombok.Getter;
import lombok.Setter;
import org.example.dao.AbstractHibernateDao;
import org.example.dao.InterF.ProfessorDao;
import org.example.dao.InterF.SemesterDao;
import org.example.domain.Semester;
import org.example.domain.WebRegClass;
import org.example.domain.hibernate.ProfessorHibernate;
import org.example.domain.hibernate.SemesterHibernate;
import org.example.domain.hibernate.WebRegClassHibernate;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Getter
@Setter
@Repository("SemesterDaoHibernate")
@Transactional
public class SemesterHibernateDao extends AbstractHibernateDao<SemesterHibernate> implements SemesterDao {


    @Override
    public Semester getSemesterById(int id) {
        SemesterHibernate semesterHibernate=sessionFactory.getCurrentSession().get(SemesterHibernate.class,id);
        return  semesterHibernate;
    }

    @Override
    public Semester getSemesterByClass(WebRegClass c) {
        Session session=sessionFactory.getCurrentSession();

        WebRegClassHibernate webRegClassHibernate=session.get(WebRegClassHibernate.class,c.getId());

        return webRegClassHibernate.getSemesterHibernate();
    }


}
