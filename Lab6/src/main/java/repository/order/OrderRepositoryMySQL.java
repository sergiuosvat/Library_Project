package repository.order;

import model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderRepositoryMySQL implements OrderRepository{
    private final Connection connection;

    public OrderRepositoryMySQL(Connection connection)
    {
        this.connection = connection;
    }


    public boolean save(Order order) {
        String sql = "INSERT INTO orders VALUES(null, ?, ?, ?, ?);";

        try{

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, order.getAuthor());
            preparedStatement.setString(2, order.getTitle());
            preparedStatement.setDate(3, java.sql.Date.valueOf(order.getPublishedDate()));
            preparedStatement.setLong(4,order.getUser_id());

            int rowsInserted = preparedStatement.executeUpdate();

            return rowsInserted == 1;

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

}
