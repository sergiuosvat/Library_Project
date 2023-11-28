package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import model.Book;
import model.Order;
import model.builder.OrderBuilder;
import service.book.BookService;
import service.order.OrderService;
import view.EmployeeView;

import java.util.List;

public class EmployeeController {
    private final EmployeeView employeeView;
    private final OrderService orderService;
    private final BookService bookService;
    private final Long employeeId;

    public EmployeeController(EmployeeView employeeView,Long employeeId, OrderService orderService, BookService bookService){
        this.employeeView = employeeView;
        this.employeeId = employeeId;
        this.employeeView.addSellButtonListener(new EmployeeController.SellButtonListener());
        this.orderService = orderService;
        this.bookService = bookService;
    }
    private class SellButtonListener implements EventHandler<ActionEvent>
    {
        @Override
        public void handle(javafx.event.ActionEvent event) {
            List<Book> cart = employeeView.getTable().getSelectionModel().getSelectedItems();
            int quantity = Integer.parseInt(employeeView.getTextFieldQuantity().getText());
            for(Book book : cart)
            {
                if(!bookService.checkStock(quantity, book.getId()))
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            String.format("Cartea cu numele %s nu este in cantitatea dorita",book.getTitle()));
                    alert.show();
                    return;
                }
                if(employeeView.getTextFieldUserId().getText().isEmpty())
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR,"Nu ati introdus id-ul user-ului care cumpara cartea");
                    alert.show();
                    return;
                }
                Order order = new OrderBuilder().
                        setAuthor(book.getAuthor())
                        .setTitle(book.getTitle())
                        .setPublishedDate(book.getPublishedDate())
                        .setUserId(Long.parseLong(employeeView.getTextFieldUserId().getText()))
                        .setQuantity(quantity)
                        .setEmployeeId(employeeId)
                        .build();
                orderService.save(order);
                bookService.updateStock(quantity,book.getId());
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Cartea a fost adaugata cu succes!");
            refreshTableView();
            employeeView.getTextFieldQuantity().clear();
            alert.show();
        }
        private void refreshTableView()
        {
            employeeView.getTable().getItems().clear();
            List<Book> updatedBooks = bookService.findAll();
            employeeView.getTable().getItems().addAll(updatedBooks);
        }
    }

}
