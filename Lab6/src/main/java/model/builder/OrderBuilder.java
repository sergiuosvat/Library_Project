package model.builder;

import model.Order;

import java.time.LocalDate;

public class OrderBuilder {
    private final Order order;

    public OrderBuilder()
    {
        order = new Order();
    }

    public OrderBuilder setId(Long id){
        order.setId(id);
        return this;
    }

    public OrderBuilder setAuthor(String author){
        order.setAuthor(author);
        return this;
    }

    public OrderBuilder setTitle(String title){
        order.setTitle(title);
        return this;
    }

    public OrderBuilder setPublishedDate(LocalDate publishedDate){
        order.setPublishedDate(publishedDate);
        return this;
    }

    public OrderBuilder setUserId(Long userId)
    {
        order.setUserId(userId);
        return this;
    }

    public OrderBuilder setQuantity(int quantity)
    {
        order.setQuantity(quantity);
        return this;
    }

    public OrderBuilder setEmployeeId(Long employeeId)
    {
        order.setEmployeeId(employeeId);
        return this;
    }
    public Order build(){return order;}
}
