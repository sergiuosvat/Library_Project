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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class EmployeeController {
    private final EmployeeView employeeView;
    private final OrderService orderService;
    private final BookService bookService;
    private final Long employeeId;
    private final Book book = new Book();

    public EmployeeController(EmployeeView employeeView, Long employeeId, OrderService orderService, BookService bookService) {
        this.employeeView = employeeView;
        this.employeeId = employeeId;
        this.employeeView.addSellButtonListener(new EmployeeController.SellButtonListener());
        this.employeeView.addDeleteButtonListener(new EmployeeController.DeleteButtonListener());
        this.employeeView.addUpdateButtonListener(new EmployeeController.UpdateButtonListener());
        this.orderService = orderService;
        this.bookService = bookService;
        onChangeTextArea();
    }

    private void onChangeTextArea() {
        employeeView.getTextFieldId().textProperty().addListener((obs, oldText, newText) -> {
            if (!Objects.equals(newText, "")) book.setId(Long.parseLong(newText));
        });

        employeeView.getTextFieldAuthor().textProperty().addListener((obs, oldText, newText) -> {
            if (!Objects.equals(newText, "")) book.setAuthor(newText);
        });

        employeeView.getTextFieldTitle().textProperty().addListener((obs, oldText, newText) -> {
            if (!Objects.equals(newText, "")) book.setTitle(newText);
        });

        employeeView.getTextFieldPublishedDate().textProperty().addListener((obs, oldText, newText) -> {
            if (!Objects.equals(newText, "")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate localDate = LocalDate.parse(newText, formatter);
                book.setPublishedDate(localDate);
            }
        });

        employeeView.getTextFieldStock().textProperty().addListener((obs, oldText, newText) -> {
            if (!Objects.equals(newText, "")) book.setStock(Integer.parseInt(newText));
        });
        employeeView.getTable().getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                employeeView.getTextFieldId().setText(String.valueOf(newSelection.getId()));
                employeeView.getTextFieldTitle().setText(newSelection.getTitle());
                employeeView.getTextFieldAuthor().setText(newSelection.getAuthor());
                employeeView.getTextFieldPublishedDate().setText(newSelection.getPublishedDate().toString());
                employeeView.getTextFieldStock().setText(String.valueOf(newSelection.getStock()));

                book.setId(newSelection.getId());
                book.setTitle(newSelection.getTitle());
                book.setAuthor(newSelection.getAuthor());
                book.setPublishedDate(newSelection.getPublishedDate());
                book.setStock(newSelection.getStock());
            }
        });
    }

    private void clearTextAreas() {
        employeeView.getTextFieldId().clear();
        employeeView.getTextFieldAuthor().clear();
        employeeView.getTextFieldTitle().clear();
        employeeView.getTextFieldPublishedDate().clear();
        employeeView.getTextFieldStock().clear();
        employeeView.getTextFieldUserId().clear();
    }

    private void refreshTableView() {
        employeeView.getTable().getItems().clear();
        List<Book> updatedBooks = bookService.findAll();
        employeeView.getTable().getItems().addAll(updatedBooks);
    }

    private class SellButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(javafx.event.ActionEvent event) {
            List<Book> cart = employeeView.getTable().getSelectionModel().getSelectedItems();
            int quantity = Integer.parseInt(employeeView.getTextFieldQuantity().getText());
            for (Book book : cart) {
                if (!bookService.checkStock(quantity, book.getId())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, String.format("The book named %s is not available in the desired quantity", book.getTitle()));
                    alert.show();
                    return;
                }
                if (employeeView.getTextFieldUserId().getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "User Id is blank!");
                    alert.show();
                    return;
                }
                if (employeeView.getTextFieldQuantity().getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Quantity is 0!");
                    alert.show();
                    return;
                }
                Order order = new OrderBuilder()
                        .setAuthor(book.getAuthor())
                        .setTitle(book.getTitle())
                        .setPublishedDate(book.getPublishedDate())
                        .setUserId(Long.parseLong(employeeView.getTextFieldUserId().getText()))
                        .setQuantity(quantity)
                        .setEmployeeId(employeeId).build();
                orderService.save(order);
                bookService.updateStock(quantity, book.getId());
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "The order has been placed!");
            refreshTableView();
            clearTextAreas();
            alert.show();
        }
    }

    private class DeleteButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(javafx.event.ActionEvent event) {
            List<Book> cart = employeeView.getTable().getSelectionModel().getSelectedItems();
            for (Book book : cart) {
                bookService.removeById(book.getId());
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "The book was deleted successfully!");
            refreshTableView();
            clearTextAreas();
            alert.show();
        }
    }

    private class UpdateButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(javafx.event.ActionEvent event) {
            List<Book> cart = employeeView.getTable().getSelectionModel().getSelectedItems();
            if (cart.size() > 1) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Select only one object at a time!");
                alert.show();
                return;
            }
            bookService.updateBook(book, book.getId());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "The book was updated successfully!");
            refreshTableView();
            clearTextAreas();
            alert.show();
        }
    }

}
