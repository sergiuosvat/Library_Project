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
import model.Order;
import model.User;
import service.order.OrderService;
import service.security.RightsRolesService;
import service.user.AuthenticationService;
import service.user.UserService;
import view.AdminView;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AdminController {
    private final AdminView adminView;
    private final UserService userService;
    private final OrderService orderService;
    private final User user = new User();
    private final AuthenticationService authenticationService;
    private final RightsRolesService rightsRolesService;

    public AdminController(AdminView adminView, UserService userService, OrderService orderService, AuthenticationService authenticationService, RightsRolesService rightsRolesService){
        this.adminView = adminView;
        this.userService = userService;
        this.orderService = orderService;
        this.authenticationService = authenticationService;
        this.rightsRolesService = rightsRolesService;

        this.adminView.addDeleteButtonListener(new AdminController.DeleteButtonListener());
        this.adminView.addCreateUserButtonListener(new AdminController.CreateUserButtonListener());
        this.adminView.addEmployeeSalesToPdfButtonListener(new AdminController.OrderToPdfButtonListener());
        this.adminView.addUpdateUserButtonListener(new AdminController.UpdateButtonListener());
        onChangeTextArea();
    }

    private void onChangeTextArea() {

        adminView.getTextFieldUsername().textProperty().addListener((obs, oldText, newText) -> {
            if (!Objects.equals(newText, "")) user.setUsername(newText);
        });

        adminView.getTextFieldPassword().textProperty().addListener((obs, oldText, newText) -> {
            if (!Objects.equals(newText, "")) user.setPassword(authenticationService.hashPassword(newText));
        });

        adminView.getComboBox().valueProperty().addListener((obs, oldSel, newSel) -> {
            if(!Objects.equals(newSel, null)){
                user.setRoles(Collections.singletonList(rightsRolesService.findRoleByTitle(newSel)));
            }
        });


        adminView.getTable().getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                adminView.getTextFieldUsername().setText(newSelection.getUsername());
                adminView.getTextFieldPassword().setText(newSelection.getPassword());
                adminView.getComboBox().setValue(rightsRolesService.findRoleForUserString(newSelection.getId()));

                user.setId(newSelection.getId());
                user.setUsername(newSelection.getUsername());
                user.setPassword(newSelection.getPassword());
                user.setRoles(Collections.singletonList(rightsRolesService.findRoleByTitle(adminView.getComboBox().getValue())));
            }
        });
    }

    private void clearTextAreas() {
        adminView.getTextFieldPassword().clear();
        adminView.getTextFieldUsername().clear();
        adminView.getComboBox().setValue(null);
    }

    private void refreshTableView() {
        adminView.getTable().getItems().clear();
        List<User> updatedBooks = userService.findAll();
        adminView.getTable().getItems().addAll(updatedBooks);
    }

    private class DeleteButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(javafx.event.ActionEvent event) {
            userService.removeById(user.getId());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "The user was deleted successfully!");
            refreshTableView();
            clearTextAreas();
            alert.show();
        }
    }

    private class UpdateButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(javafx.event.ActionEvent event) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "The user was updated successfully!");
            refreshTableView();
            clearTextAreas();
            alert.show();
        }
    }

    private class CreateUserButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            userService.save(user);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "The user was added successfully!");
            refreshTableView();
            clearTextAreas();
            alert.show();
        }
    }

    private class OrderToPdfButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            List<Order> orders = orderService.findAllEmployee();

            try {
                String dest = "SalesEmployee.pdf";
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
}
