package cinema.dao;

import cinema.model.Order;
import cinema.model.User;
import java.util.List;

public interface OrderDao {
    Order completeOrder(Order order);

    List<Order> getOrdersHistory(User user);
}
