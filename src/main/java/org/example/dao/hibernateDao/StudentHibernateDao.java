package org.example.dao.hibernateDao;

import lombok.Getter;
import lombok.Setter;
import org.example.dao.AbstractHibernateDao;
import org.example.dao.InterF.StudentDao;
import org.example.domain.Application;
import org.example.domain.Enrollment;
import org.example.domain.Student;
import org.example.domain.hibernate.*;
import org.example.exceptions.UserServiceException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
@Repository("StudentDaoHibernate")
@Transactional
public class StudentHibernateDao extends AbstractHibernateDao<StudentHibernate> implements StudentDao {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    public Student getById(int id){
        Session session=sessionFactory.getCurrentSession();
        StudentHibernate studentHibernate=session.get(StudentHibernate.class,id);
        return studentHibernate;
    }
    @Override
    public void createNewStudent(StudentHibernate student) {
        Session session=sessionFactory.getCurrentSession();

//        Transaction transaction = session.beginTransaction();


        StudentHibernate studentHibernate=new StudentHibernate();


        DepartmentHibernate departmentHibernate=session.get(DepartmentHibernate.class,student.getDept_id());

        studentHibernate.setUsername(student.getUsername());
        studentHibernate.setEncrypted_password(student.getEncrypted_password());

        studentHibernate.setEmail(student.getEmail());
        studentHibernate.setFirstName(student.getFirstName());
        studentHibernate.setLastName(student.getLastName());
        studentHibernate.setDepartmentHibernate(departmentHibernate);
        studentHibernate.set_active(true);
        studentHibernate.set_admin(false);

        session.save(studentHibernate);

//        transaction.commit();
//        session.close();
    }

    @Override
    public List<Student> getAllUsers() {
        Session session=sessionFactory.getCurrentSession();

//        Transaction transaction = session.beginTransaction();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        // result class
        CriteriaQuery<StudentHibernate> criteriaQuery = criteriaBuilder.createQuery(StudentHibernate.class);
        criteriaQuery.from(StudentHibernate.class);




        List<StudentHibernate> items = session.createQuery(criteriaQuery).getResultList();
        List<Student> res=items.stream().collect(Collectors.toList());
//        session.close();

        return res;
    }


    @Cacheable("students")
    @Override
    public List<Student> getAllStudents() {
        Session session=sessionFactory.getCurrentSession();

//        Transaction transaction = session.beginTransaction();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        // result class
        CriteriaQuery<StudentHibernate> criteriaQuery = criteriaBuilder.createQuery(StudentHibernate.class);
        criteriaQuery.from(StudentHibernate.class);
        List<Student> items = session.createQuery(criteriaQuery).getResultList().stream().filter(c->!c.is_admin()).collect(Collectors.toList());

//        session.close();
        return items;
    }

    @Override
    public Optional<Student> validateLogin(String username, String password) {
        return getAllUsers().stream()
                .filter(a -> a.getUsername().equals(username)
                        && bCryptPasswordEncoder.matches(password,a.getEncrypted_password()) )
                .findAny();
    }

    @Override
    public Student getStudentById(int studentId) {
        StudentHibernate studentHibernate=sessionFactory.getCurrentSession().get(StudentHibernate.class,studentId);
        if(studentHibernate==null) throw new UserServiceException("Student not found!!");
        return  studentHibernate;
    }

    @Override
    public void activateStudentById(int studentId) {
        Session session=sessionFactory.getCurrentSession();

//        Transaction transaction = session.beginTransaction();

        StudentHibernate studentHibernate=session.get(StudentHibernate.class,studentId);
        if(studentHibernate==null) throw new UserServiceException("Student not found!!");
        studentHibernate.set_active(true);

        session.update(studentHibernate);
//        transaction.commit();

//        session.close();
    }

    @Override
    public void deactivateStudentById(int studentId) {
        Session session=sessionFactory.getCurrentSession();

//        Transaction transaction = session.beginTransaction();

        StudentHibernate studentHibernate=session.get(StudentHibernate.class,studentId);
        if(studentHibernate==null) throw new UserServiceException("Student not found!!");
        studentHibernate.set_active(false);

        session.update(studentHibernate);
//        transaction.commit();

//        session.close();
    }

//    @Override
//    public int getTotalPagesAdmin(int limit) {
//        List<Student> students=getAllStudents();
//
//        if(students==null) return 0;
//        if(students.size()%limit==0) return students.size()/limit;
//        else return students.size()/limit+1;
//    }

    @Override
    public List<Student> getStudentsByPageLimit(int page, int limit) {
//        Session session=sessionFactory.getCurrentSession();
//
//        Transaction transaction = session.beginTransaction();
//
//        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
//        // result class
//        CriteriaQuery<Student> criteriaQuery = criteriaBuilder.createQuery(Student.class);
//
//        Root<StudentHibernate> aRoot=criteriaQuery.from(StudentHibernate.class);
//        aRoot.join("DepartmentHibernate", JoinType.INNER);
//
//        criteriaQuery.multiselect(
//                aRoot.get("id"),
//                aRoot.get("username"),
//                aRoot.get("encrypted_password"),
//                aRoot.get("email"),
//                aRoot.get("first_name"),
//                aRoot.get("last_name"),
//                aRoot.get("DepartmentHibernate").get("id"),
//                aRoot.get("is_active"),
//                aRoot.get("is_admin")
//        );
//
//
//        Predicate predicateA=criteriaBuilder.equal(aRoot.get("is_admin"), false);
//        criteriaQuery.where(predicateA);
        List<Student> items = getAllStudents();//session.createQuery(criteriaQuery).setFirstResult((page-1)*limit).setMaxResults(limit).getResultList();


        return items.subList((page-1)*limit,Math.min((page-1)*limit,items.size()));
    }

    @Override
    public Student getStudentByEnrollment(Enrollment c) {

        EnrollmentHibernate enrollmentHibernate=sessionFactory.getCurrentSession().get(EnrollmentHibernate.class,c.getId());

        return enrollmentHibernate.getStudentHibernate();
    }

    @Override
    public Student getStudentByApplication(Application c) {
        ApplicationHibernate applicationHibernateHibernate=sessionFactory.getCurrentSession().get(ApplicationHibernate.class,c.getId());

        return applicationHibernateHibernate.getStudentHibernate();
    }

    @Override
    public StudentHibernate getUserByName(String userName) {
        Session session=sessionFactory.getCurrentSession();

//        Transaction transaction = session.beginTransaction();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        // result class
        CriteriaQuery<StudentHibernate> criteriaQuery = criteriaBuilder.createQuery(StudentHibernate.class);
        criteriaQuery.from(StudentHibernate.class);
        List<StudentHibernate> items = session.createQuery(criteriaQuery).getResultList().stream().filter(c->c.getUsername().equals(userName)).collect(Collectors.toList());
        if(items.size()==0) System.out.print(items.get(0).getUsername());

        return items.size()==0?null: items.get(0);
    }


//    @Override
//    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
//        return null;
//    }

//    @Override
//    public PWEncoder getPwEncoder() {
//        return null;
//    }


//    @Override
//    public void setBCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
//
//    }

//    @Override
//    public void setPwEncoder(PWEncoder pwEncoder) {
//
//    }
}
