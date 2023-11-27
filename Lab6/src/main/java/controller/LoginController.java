package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.Role;
import model.User;
import model.validator.Notification;
import service.book.BookService;
import service.order.OrderService;
import service.user.AuthenticationService;
import view.CustomerView;
import view.LoginView;

public class LoginController {

    private final LoginView loginView;
    private final AuthenticationService authenticationService;
    private final BookService bookService;
    private final OrderService orderService;


    public LoginController(LoginView loginView, AuthenticationService authenticationService, BookService bookService, OrderService orderService) {
        this.loginView = loginView;
        this.bookService = bookService;
        this.authenticationService = authenticationService;
        this.orderService = orderService;

        this.loginView.addLoginButtonListener(new LoginButtonListener());
        this.loginView.addRegisterButtonListener(new RegisterButtonListener());
    }

    private class LoginButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<User> loginNotification = authenticationService.login(username, password);
            if (loginNotification.hasErrors()){
                loginView.setActionTargetText(loginNotification.getFormattedErrors());
            }else{
                Role role = loginNotification.getResult().getRoles().get(0);
                switch (role.getRole()) {
                    case "administrator":
                        System.out.println("Not working");
                        break;
                    case "customer":
                        CustomerView customerView = new CustomerView(loginView.getStage(),bookService);
                        // get id from the db
                        new CustomerController(customerView, loginNotification.getResult().getId(), orderService, bookService);
                        break;
                    case "employee":
                        System.out.println("Not working employee");
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

            Notification<Boolean> registerNotification = authenticationService.register(username,password);

            if (registerNotification.hasErrors()) {
                loginView.setActionTargetText(registerNotification.getFormattedErrors());
            }else {
                loginView.setActionTargetText("Register successful!");
            }
        }
    }
}
