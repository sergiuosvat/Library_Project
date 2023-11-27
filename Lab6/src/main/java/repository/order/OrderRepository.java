package repository.order;

import model.Order;

public interface OrderRepository {

    public boolean save(Order order);
}
