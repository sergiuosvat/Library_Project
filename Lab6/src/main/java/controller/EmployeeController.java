package controller;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import model.Book;
import model.Order;
import model.builder.OrderBuilder;
import service.book.BookService;
import service.order.OrderService;
import view.EmployeeView;

import java.io.FileNotFoundException;
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
        this.employeeView.addCreateBookButtonListener(new EmployeeController.CreateBookButtonListener());
        this.employeeView.addOrderToPdfButtonListener(new EmployeeController.OrderToPdfButtonListener());
        this.employeeView.addLogOutButtonListener(new EmployeeController.LogOutButtonListener());

        this.orderService = orderService;
        this.bookService = bookService;
        onChangeTextArea();
    }

    private void onChangeTextArea() {

        employeeView.getTextFieldAuthor().textProperty().addListener((obs, oldText, newText) -> {
            if (!Objects.equals(newText, "")) book.setAuthor(newText);
        });

        employeeView.getTextFieldTitle().textProperty().addListener((obs, oldText, newText) -> {
            if (!Objects.equals(newText, "")) book.setTitle(newText);
        });

        employeeView.getTextFieldPublishedDate().textProperty().addListener((obs, oldText, newText) -> {
            if (!Objects.equals(newText, "")) {
                String dateFormatRegex = "\\d{4}-\\d{2}-\\d{2}";
                if (newText.matches(dateFormatRegex))
                {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate localDate = LocalDate.parse(newText, formatter);
                    book.setPublishedDate(localDate);
                }
            }
        });

        employeeView.getTextFieldStock().textProperty().addListener((obs, oldText, newText) -> {
            if (!Objects.equals(newText, ""))
            {
                if(newText.contains("-"))
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR,"Stocul nu poate fi negativ!");
                    alert.show();
                    return;
                }
                book.setStock(Integer.parseInt(newText));
            }
        });
        employeeView.getTable().getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
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
        employeeView.getTextFieldAuthor().clear();
        employeeView.getTextFieldTitle().clear();
        employeeView.getTextFieldPublishedDate().clear();
        employeeView.getTextFieldStock().clear();
        employeeView.getTextFieldUserId().clear();
        employeeView.getTextFieldQuantity().clear();
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
            if(quantity < 0)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Cantitatea nu poate fi negativa!");
                alert.show();
                employeeView.getTextFieldQuantity().clear();
                return;
            }
            for (Book book : cart) {
                if (bookService.checkStock(quantity, book.getId())) {
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

    private class CreateBookButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            bookService.save(book);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "The book was added successfully!");
            refreshTableView();
            clearTextAreas();
            alert.show();
        }
    }

    private class OrderToPdfButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            List<Order> orders = orderService.findAll();

            try {
                String dest = "Sales.pdf";
                PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
                Document document = new Document(pdfDoc);
                Table table = new Table(7);
                addTableHeaders(table);
                for (Order order : orders)
                {
                    addTableRows(table,order);
                }

                document.add(table);
                document.close();
                pdfDoc.close();

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        private void addTableHeaders(Table table)
        {
            table.addHeaderCell("id");
            table.addHeaderCell("author");
            table.addHeaderCell("title");
            table.addHeaderCell("publishedDate");
            table.addHeaderCell("user-id");
            table.addHeaderCell("quantity");
            table.addHeaderCell("employee-id");
        }

        private void addTableRows(Table table, Order order)
        {
            table.addCell(new Cell().add(new Paragraph(order.getId().toString())));
            table.addCell(new Cell().add(new Paragraph(order.getAuthor())));
            table.addCell(new Cell().add(new Paragraph(order.getTitle())));
            table.addCell(new Cell().add(new Paragraph(order.getPublishedDate().toString())));
            table.addCell(new Cell().add(new Paragraph(order.getUserId().toString())));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(order.getQuantity()))));
            table.addCell(new Cell().add(new Paragraph(order.getEmployeeId().toString())));
        }
    }

    private static class LogOutButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(javafx.event.ActionEvent event) {
            LogOutController.logOut();
        }
    }
}
