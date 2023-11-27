package model.builder;

import model.Order;

import java.time.LocalDate;

public class OrderBuilder {
    private Order order;

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
        order.setUser_id(userId);
        return this;
    }

    public OrderBuilder setQuantity(int quantity)
    {
        order.setQuantity(quantity);
        return this;
    }
    public Order build(){return order;}
}
