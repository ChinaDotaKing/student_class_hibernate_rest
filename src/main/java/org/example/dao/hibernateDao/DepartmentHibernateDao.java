package org.example.dao.hibernateDao;

import lombok.Getter;
import lombok.Setter;
import org.example.dao.AbstractHibernateDao;
import org.example.dao.InterF.ApplicationDao;
import org.example.dao.InterF.DepartmentDao;
import org.example.domain.*;
import org.example.domain.hibernate.*;

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
@Repository("DepartmentDaoHibernate")
@Transactional
public class DepartmentHibernateDao extends AbstractHibernateDao<DepartmentHibernate> implements DepartmentDao {


    @Override
    public Department getDepartmentById(int id) {
        DepartmentHibernate departmentHibernate=sessionFactory.getCurrentSession().get(DepartmentHibernate.class,id);
        if(departmentHibernate!=null)System.out.print(departmentHibernate.getName());
        return  departmentHibernate;
    }

    @Override
    public Department getDepartmentByStudent(Student c) {

        Session session=sessionFactory.getCurrentSession();

        StudentHibernate studentHibernate=session.get(StudentHibernate.class,c.getId());

        return studentHibernate.getDepartmentHibernate();
    }

    @Override
    public Department getDepartmentByProfessor(Professor c) {
        Session session=sessionFactory.getCurrentSession();

        ProfessorHibernate professorHibernate=session.get(ProfessorHibernate.class,c.getId());

        return professorHibernate.getDepartmentHibernate();
    }

    @Override
    public Department getDepartmentByCourse(Course course) {
        Session session=sessionFactory.getCurrentSession();

        CourseHibernate courseHibernate=session.get(CourseHibernate.class,course.getId());

        return courseHibernate.getDepartmentHibernate();

    }


}
