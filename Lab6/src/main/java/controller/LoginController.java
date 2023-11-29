package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.Role;
import model.User;
import model.validator.Notification;
import service.book.BookService;
import service.order.OrderService;
import service.user.AuthenticationService;
import service.user.UserService;
import view.AdminView;
import view.CustomerView;
import view.EmployeeView;
import view.LoginView;

import static database.Constants.Roles.*;

public class LoginController {

    private final LoginView loginView;
    private final AuthenticationService authenticationService;
    private final BookService bookService;
    private final OrderService orderService;
    private final UserService userService;


    public LoginController(LoginView loginView, AuthenticationService authenticationService, BookService bookService, OrderService orderService, UserService userService) {
        this.loginView = loginView;
        this.bookService = bookService;
        this.authenticationService = authenticationService;
        this.orderService = orderService;
        this.userService = userService;

        this.loginView.addLoginButtonListener(new LoginButtonListener());
        this.loginView.addRegisterButtonListener(new RegisterButtonListener());
    }

    private class LoginButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<User> loginNotification = authenticationService.login(username, password);
            if (loginNotification.hasErrors()) {
                loginView.setActionTargetText(loginNotification.getFormattedErrors());
            } else {
                Role role = loginNotification.getResult().getRoles().get(0);
                switch (role.getRole()) {
                    case ADMINISTRATOR:
                        AdminView adminView = new AdminView(loginView.getStage(), userService);
                        new AdminController(adminView,userService,orderService);
                        break;
                    case CUSTOMER:
                        CustomerView customerView = new CustomerView(loginView.getStage(), bookService);
                        new CustomerController(customerView, loginNotification.getResult().getId(), orderService, bookService);
                        break;
                    case EMPLOYEE:
                        EmployeeView employeeView = new EmployeeView(loginView.getStage(), bookService);
                        new EmployeeController(employeeView,loginNotification.getResult().getId(), orderService, bookService);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private class RegisterButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<Boolean> registerNotification = authenticationService.register(username, password);

            if (registerNotification.hasErrors()) {
                loginView.setActionTargetText(registerNotification.getFormattedErrors());
            } else {
                loginView.setActionTargetText("Register successful!");
            }
        }
    }
}
