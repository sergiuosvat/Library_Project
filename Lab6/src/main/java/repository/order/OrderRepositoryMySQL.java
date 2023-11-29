package repository.order;

import model.Order;
import model.builder.OrderBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepositoryMySQL implements OrderRepository{
    private final Connection connection;

    public OrderRepositoryMySQL(Connection connection)
    {
        this.connection = connection;
    }

    public List<Order> findAll()
    {
        String sql = "SELECT * FROM orders;";

        List<Order> orders = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                orders.add(getOrderFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }
    public boolean save(Order order) {
        int rowsInserted;
        try{
            if(order.hasEmployeeId())
            {
                String sql = "INSERT INTO orders VALUES(null, ?, ?, ?, ?, ?, ?);";

                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, order.getAuthor());
                preparedStatement.setString(2, order.getTitle());
                preparedStatement.setDate(3, java.sql.Date.valueOf(order.getPublishedDate()));
                preparedStatement.setLong(4,order.getUserId());
                preparedStatement.setInt(5,order.getQuantity());
                preparedStatement.setLong(6,order.getEmployeeId());

                rowsInserted = preparedStatement.executeUpdate();
            }else{
                String sql = "INSERT INTO orders VALUES(null, ?, ?, ?, ?, ?, 0);";

                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, order.getAuthor());
                preparedStatement.setString(2, order.getTitle());
                preparedStatement.setDate(3, java.sql.Date.valueOf(order.getPublishedDate()));
                preparedStatement.setLong(4,order.getUserId());
                preparedStatement.setInt(5,order.getQuantity());

                rowsInserted = preparedStatement.executeUpdate();
            }

            return rowsInserted == 1;

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    private Order getOrderFromResultSet(ResultSet resultSet) throws SQLException {
        return new OrderBuilder()
                .setId(resultSet.getLong("id"))
                .setTitle(resultSet.getString("title"))
                .setAuthor(resultSet.getString("author"))
                .setPublishedDate(new java.sql.Date(resultSet.getDate("publishedDate").getTime()).toLocalDate())
                .setUserId(resultSet.getLong("user_id"))
                .setQuantity(resultSet.getInt("quantity"))
                .setEmployeeId(resultSet.getLong("employee_id"))
                .build();
    }

}
