package org.example.storage;

import org.example.core.HibernateUtils;
import org.example.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


public class CRUD {
     private SessionFactory sessionFactory;
     public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";


     public CRUD() {
          sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE, User.class);
     }

     public void create(User user){
          try (Session session = sessionFactory.openSession()){
               session.getTransaction().begin();
               session.persist(user);
               session.getTransaction().commit();
          }
          catch (Exception e){
               e.printStackTrace();
          }

     }

     public User read(Long id){
          try (Session session = sessionFactory.openSession()){
               session.getTransaction().begin();

               User selected = session.get(User.class, id);
               return selected;
          }

     }

     public void update(User user){
          try (Session session = sessionFactory.openSession()){
               session.getTransaction().begin();

               session.update(user);
               session.getTransaction().commit();
          }
     }

     public void delete(User user){
          try (Session session = sessionFactory.openSession()) {
               session.getTransaction().begin();

               session.delete(user);
               session.getTransaction().commit();
          }

     }
}
