package org.cat.model;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

/**
 * Created by kurunsk on 10-02-2016.
 */
public class ManageUsers {

    private static SessionFactory factory ;

    public static void main(String[] args) {

        try{
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex){
            ex.printStackTrace();
            System.err.println("Failed to create sessionFactory object. " );
        }

        System.out.println("Successfully Configured Hibernate...");
        ManageUsers mu = new ManageUsers();
        mu.addUser(1, "Achu", 23);
        mu.listUsers();
        mu.updateUser(1);
        mu.listUsers();
        mu.deleteUser(1);
        /*try{
            Class.forName("com.mysql.jdbc.Driver");

            Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/minestar","root","root");


            Statement stmt=con.createStatement();

            ResultSet rs=stmt.executeQuery("select * from users");

            while(rs.next())
                System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));

            con.close();

        }catch(Exception e){ System.out.println(e);}
*/
}

    public Integer addUser(int id, String name, int age){
        Transaction tx = null;
        Session session = factory.openSession();
        Integer userId = null;
        try{
            tx = session.beginTransaction();
            Users user = new Users(id, name, age);
            userId = (Integer) session.save(user);
            tx.commit();
        }catch(HibernateException hEx){
            if(tx != null) tx.rollback();
            hEx.printStackTrace();
        } finally {
            session.close();
        }
        return userId;
    }

    public void listUsers(){
        Session session = factory.openSession();
        Transaction tx = null;

        try{
            tx = session.beginTransaction();
            //List users = session.createSQLQuery("SELECT * FROM USERS").list();
            List users = session.createQuery("FROM org.cat.model.Users").list();
            for(Iterator it = users.iterator(); it.hasNext();){
                Users usrs = (Users) it.next();
                System.out.println(usrs.getId() + "   " + usrs.getName() + "   " + usrs.getAge());
            }
            tx.commit();
        }
        catch (HibernateException ex) {
            ex.printStackTrace(); if(tx!=null) tx.rollback();
        } finally {
            session.close();
        }
    }

    public void updateUser(Integer userId){
        Transaction tx = null;
        Session session = factory.openSession();

        try{
            tx = session.beginTransaction();
            Users users = (Users) session.get(Users.class, userId);
            users.setAge(50);
            session.update(users);
            tx.commit();
        }catch (HibernateException ex) {
            ex.printStackTrace();
            if(tx!=null) tx.rollback();
        } finally {
            session.close();
        }
    }

    public void deleteUser(int userId){
        Transaction tx = null;
        Session session = factory.openSession();
        try{
            tx = session.beginTransaction();
            Users users = (Users) session.get(Users.class, userId);
            session.delete(users);
            tx.commit();
        } catch (HibernateException ex){
            ex.printStackTrace(); if(tx!=null) tx.rollback();
        } finally {
            session.close();
        }
    }
}
