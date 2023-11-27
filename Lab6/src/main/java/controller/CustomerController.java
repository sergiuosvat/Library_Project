package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import model.Book;
import model.Order;
import model.builder.OrderBuilder;
import service.book.BookService;
import service.order.OrderService;
import view.CustomerView;

import java.util.List;

public class CustomerController {
    private final CustomerView customerView;
    private final Long user_id;
    private final OrderService orderService;
    private final BookService bookService;

    public CustomerController(CustomerView customerView, Long user_id, OrderService orderService, BookService bookService){
        this.customerView = customerView;
        this.customerView.addBuyButtonListener(new BuyButtonListener());
        this.user_id = user_id;
        this.orderService = orderService;
        this.bookService = bookService;
    }

    private class BuyButtonListener implements EventHandler<ActionEvent>
    {
        @Override
        public void handle(javafx.event.ActionEvent event) {
            List<Book> cart = customerView.getTable().getSelectionModel().getSelectedItems();
            int quantity = Integer.parseInt(customerView.getTextField().getText());
            for(Book book : cart)
            {
                if(!bookService.checkStock(quantity, book.getId()))
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            String.format("Cartea cu numele %s nu este in cantitatea dorita",book.getTitle()));
                    alert.show();
                    return;
                }
                Order order = new OrderBuilder().
                        setAuthor(book.getAuthor())
                        .setTitle(book.getTitle())
                        .setPublishedDate(book.getPublishedDate())
                        .setUserId(user_id)
                        .setQuantity(quantity)
                        .build();
                orderService.save(order);
                bookService.updateStock(quantity,book.getId());
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Cartea a fost adaugata cu succes!");
            refreshTableView();
            customerView.getTextField().clear();
            alert.show();
        }
        private void refreshTableView()
        {
            customerView.getTable().getItems().clear();
            List<Book> updatedBooks = bookService.findAll();
            customerView.getTable().getItems().addAll(updatedBooks);
        }
    }

}
