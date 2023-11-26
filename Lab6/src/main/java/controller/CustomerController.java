package controller;

import service.book.BookService;
import view.CustomerView;

public class CustomerController {
    private final CustomerView customerView;

    public CustomerController(CustomerView customerView){
        this.customerView = customerView;
    }

}
