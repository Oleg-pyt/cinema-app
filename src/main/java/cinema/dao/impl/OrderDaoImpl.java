package cinema.dao.impl;

import cinema.dao.OrderDao;
import cinema.exception.DataProcessingException;
import cinema.lib.Dao;
import cinema.model.Order;
import cinema.model.User;
import cinema.util.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@Dao
public class OrderDaoImpl implements OrderDao {
    @Override
    public Order completeOrder(Order order) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(order);
            transaction.commit();
            return order;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can`t save order "
                    + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        String query = "FROM Order o "
                + "LEFT JOIN FETCH o.tickets t "
                + "LEFT JOIN FETCH o.user u "
                + "LEFT JOIN FETCH t.movieSession ms "
                + "LEFT JOIN FETCH ms.movie "
                + "LEFT JOIN FETCH ms.cinemaHall "
                + "WHERE o.user = :user ";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> getAllOrdersQuery = session.createQuery(query, Order.class);
            getAllOrdersQuery.setParameter("user", user);
            return getAllOrdersQuery.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can`t get order history by user " + user, e);
        }
    }
}
