package repository.user;

import model.Book;
import model.User;
import model.validator.Notification;

import java.util.*;

public interface UserRepository {

    List<User> findAll();

    Notification<User> findByUsernameAndPassword(String username, String password);

    Notification<Boolean> save(User user);

    void removeAll();

    boolean existsByUsername(String username);
    void removeById(Long id);

    void updateUser(User user);
}
