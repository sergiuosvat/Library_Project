package repository.user;
import model.User;
import model.builder.UserBuilder;
import model.validator.Notification;
import repository.security.RightsRolesRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static database.Constants.Tables.USER;
import static java.util.Collections.singletonList;

public class UserRepositoryMySQL implements UserRepository {

    private final Connection connection;
    private final RightsRolesRepository rightsRolesRepository;


    public UserRepositoryMySQL(Connection connection, RightsRolesRepository rightsRolesRepository) {
        this.connection = connection;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public Notification<User> findByUsernameAndPassword(String username, String password) {

        Notification<User> findByUsernameAndPasswordNotification = new Notification<>();

        try {

            String fetchUserSql =
                    "Select * from " + USER + " where username = ? " + " and password = ? ";

            PreparedStatement preparedStatement = connection.prepareStatement(fetchUserSql);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            ResultSet userResultSet = preparedStatement.executeQuery();
            if(userResultSet.next()){
                User user = new UserBuilder()
                        .setUsername(userResultSet.getString("username"))
                        .setPassword(userResultSet.getString("password"))
                        .setRoles(rightsRolesRepository.findRolesForUser(userResultSet.getLong("id")))
                        .build();
                findByUsernameAndPasswordNotification.setResult(user);
            } else {
                findByUsernameAndPasswordNotification.addError("Invalid username or password!");
                return findByUsernameAndPasswordNotification;
            }


        } catch (SQLException e) {
            System.out.println(e.toString());
            findByUsernameAndPasswordNotification.addError("Something is wrong with the Database!");
        }
        return findByUsernameAndPasswordNotification;
    }

    @Override
    public Notification<Boolean> save(User user) {

        Notification<Boolean> saveNotification = new Notification<>();
        try {
            PreparedStatement insertUserStatement = connection
                    .prepareStatement("INSERT INTO user values (null, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertUserStatement.setString(1, user.getUsername());
            insertUserStatement.setString(2, user.getPassword());

            if(existsByUsername(user.getUsername()))
            {
                saveNotification.addError("Email already exists in the database!");
                saveNotification.setResult(Boolean.FALSE);
            }else{
                insertUserStatement.executeUpdate();

                ResultSet rs = insertUserStatement.getGeneratedKeys();
                rs.next();
                long userId = rs.getLong(1);
                if(userId == 0)
                {
                    saveNotification.addError("Eroare la obtinerea id-ului");
                    saveNotification.setResult(Boolean.FALSE);
                }
                user.setId(userId);
                rightsRolesRepository.addRolesToUser(user, user.getRoles());
                saveNotification.setResult(Boolean.TRUE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            saveNotification.addError("Something is wrong with the database!");
        }
        return saveNotification;
    }

    @Override
    public void removeAll() {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from user where id >= 0";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean existsByUsername(String email) {
        try {

            String fetchUserSql =
                    "Select * from " + USER + " where username = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(fetchUserSql);
            preparedStatement.setString(1,email);

            ResultSet userResultSet = preparedStatement.executeQuery();
            return userResultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}