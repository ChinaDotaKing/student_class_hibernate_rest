package org.example.dao;

import org.example.domain.hibernate.ApplicationHibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public abstract class AbstractHibernateDao<T> {


    @Autowired
    protected SessionFactory sessionFactory;
    protected Class<T> clazz;

    public AbstractHibernateDao() {
    }

    protected final void setClazz(final Class<T> clazzToSet) {
        this.clazz = clazzToSet;
    }

    public List<T> getAll() {
        Session session = this.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(this.clazz);
        criteria.from(this.clazz);
        return session.createQuery(criteria).getResultList();
    }

    public T findById(int id) {
        return this.getCurrentSession().load(this.clazz, id);
    }

    public void add(T item) {
        this.getCurrentSession().save(item);
    }

    protected Session getCurrentSession() {
        return this.sessionFactory.getCurrentSession();
    }
}
