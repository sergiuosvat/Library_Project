package controller;

import javafx.stage.Stage;
import service.book.BookService;
import service.order.OrderService;
import service.security.RightsRolesService;
import service.user.AuthenticationService;
import service.user.UserService;
import view.LoginView;

public class LogOutController {
    private static AuthenticationService authenticationService;
    private static BookService bookService;
    private static OrderService orderService;
    private static UserService userService;
    private static RightsRolesService rightsRolesService;
    private static Stage stage;

    public static void setStage(Stage stage) {
        LogOutController.stage = stage;
    }

    public static void setAuthenticationService(AuthenticationService authenticationService) {
        LogOutController.authenticationService = authenticationService;
    }

    public static void setBookService(BookService bookService) {
        LogOutController.bookService = bookService;
    }

    public static void setOrderService(OrderService orderService) {
        LogOutController.orderService = orderService;
    }

    public static void setUserService(UserService userService) {
        LogOutController.userService = userService;
    }

    public static void setRightsRolesService(RightsRolesService rightsRolesService) {
        LogOutController.rightsRolesService = rightsRolesService;
    }

    public static void logOut()
    {
        new LoginController(new LoginView(stage),authenticationService,bookService,orderService,userService,rightsRolesService);
    }
}
