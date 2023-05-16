package org.example.dao.hibernateDao;

import lombok.Getter;
import lombok.Setter;
import org.example.dao.AbstractHibernateDao;
import org.example.dao.InterF.PrerequisiteDao;
import org.example.dao.InterF.ProfessorDao;
import org.example.domain.Professor;
import org.example.domain.WebRegClass;
import org.example.domain.hibernate.PrerequisiteHibernate;
import org.example.domain.hibernate.ProfessorHibernate;
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
@Repository("ProfessorDaoHibernate")
@Transactional
public class ProfessorHibernateDao extends AbstractHibernateDao<ProfessorHibernate> implements ProfessorDao {


    @Override
    public Professor getProfessorById(int id) {
        ProfessorHibernate professorHibernate=sessionFactory.getCurrentSession().get(ProfessorHibernate.class,id);
        return  professorHibernate;
    }

    @Override
    public Professor getProfessorByClass(WebRegClass c) {
        Session session=sessionFactory.getCurrentSession();

        WebRegClassHibernate webRegClassHibernate=session.get(WebRegClassHibernate.class,c.getId());

        return webRegClassHibernate.getProfessorHibernate();
    }


}
