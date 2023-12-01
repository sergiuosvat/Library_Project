package service.user;

import model.User;
import model.validator.Notification;
import repository.user.UserRepository;

import java.util.List;

public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Notification<User> findByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username,password);
    }

    @Override
    public Notification<Boolean> save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void removeById(Long id) {
        userRepository.removeById(id);
    }

    @Override
    public void updateUser(User user) {
        userRepository.updateUser(user);
    }

}
