package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.User;
import service.user.UserService;

import static database.Constants.Roles.*;

public class AdminView {
    private final TableView<User> tableView = new TableView<>();
    private final UserService userService;
    private final Label title = new Label("Available users");
    private final TextField textFieldUsername = new TextField();
    private final TextField textFieldPassword = new TextField();
    private final Label usernameLabel = new Label("username");
    private final Label passwordLabel = new Label("password");
    private final Label roleLabel = new Label("role");
    private final HBox tableBox = new HBox(tableView);
    private final HBox bottomBox = new HBox(10);
    private final Button createUserButton = new Button("Create");
    private final Button updateButton = new Button("Update");
    private final Button deleteButton = new Button("Delete");
    private final Button logOutButton = new Button("Log Out");
    private final Button employeeSalesToPdfButton = new Button("EmployeeSales");
    private final ComboBox<String> comboBox = new ComboBox<>();

    public AdminView(Stage primaryStage, UserService userService)
    {
        this.userService = userService;
        primaryStage.setTitle("Book Store");
        GridPane gridPane = new GridPane();

        initializeGridPane(gridPane);
        initializeTable();
        initializeAddUserButton();
        initializeBottomBox();
        initializeLabels();
        initializeEmployeeSalesToPdfButton();
        initializeDeleteButton();
        initializeUpdateButton();
        initializeTextFieldPassword();
        initializeTextFieldUsername();
        initializeLogOutButton();

        HBox titleBox = new HBox(title);
        titleBox.setAlignment(Pos.CENTER);

        GridPane gridPaneMain = new GridPane();
        gridPaneMain.setHgap(10);
        gridPaneMain.setVgap(10);
        gridPaneMain.setPadding(new Insets(25, 25, 25, 25));

        VBox vBox = new VBox(createUserButton, updateButton, deleteButton, employeeSalesToPdfButton,logOutButton);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);

        gridPane.add(tableBox, 0, 1);
        gridPane.add(vBox, 1, 1);
        gridPaneMain.add(titleBox, 0, 1);
        gridPaneMain.add(gridPane, 0, 2);
        gridPaneMain.add(bottomBox, 0, 3);

        Scene scene = new Scene(gridPaneMain, 800, 480);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeGridPane(GridPane gridPane){
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(70);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(30);
        gridPane.getColumnConstraints().addAll(column1, column2);
    }

    private void initializeBottomBox() {


        comboBox.getItems().addAll(ADMINISTRATOR,EMPLOYEE,CUSTOMER);
        comboBox.setFocusTraversable(false);

        VBox vBoxRole = new VBox(comboBox,roleLabel);
        vBoxRole.setAlignment(Pos.CENTER);
        vBoxRole.setSpacing(10);

        VBox vBoxAuthor = new VBox(textFieldUsername, usernameLabel);
        vBoxAuthor.setSpacing(10);
        vBoxAuthor.setAlignment(Pos.CENTER);

        VBox vBoxBookTitle = new VBox(textFieldPassword,passwordLabel);
        vBoxBookTitle.setSpacing(10);
        vBoxBookTitle.setAlignment(Pos.CENTER);

        bottomBox.setAlignment(Pos.BOTTOM_CENTER);
        bottomBox.getChildren().addAll(vBoxAuthor, vBoxBookTitle,vBoxRole);
    }

    private void initializeLabels()
    {
        title.setFont(Font.font(null, FontWeight.BOLD, 16));

        usernameLabel.setFont(Font.font(null, FontWeight.BOLD, 16));
        usernameLabel.setAlignment(Pos.CENTER);

        passwordLabel.setFont(Font.font(null, FontWeight.BOLD, 16));
        passwordLabel.setAlignment(Pos.CENTER);

        roleLabel.setFont(Font.font(null, FontWeight.BOLD, 16));
        roleLabel.setAlignment(Pos.CENTER);
    }


    private void initializeTable()
    {
        TableColumn<User,String> idUser = new TableColumn<>("id");
        TableColumn<User, String> usernameUser = new TableColumn<>("username");
        TableColumn<User, String> passwordUser = new TableColumn<>("password");

        usernameUser.setMinWidth(200);
        idUser.setMinWidth(30);
        passwordUser.setMinWidth(200);

        idUser.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameUser.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordUser.setCellValueFactory(new PropertyValueFactory<>("password"));


        tableView.getColumns().addAll(idUser,usernameUser,passwordUser);

        ObservableList<User> users = FXCollections.observableArrayList();
        users.addAll(userService.findAll());
        tableView.setItems(users);
        tableView.setFocusTraversable(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        tableView.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        ScrollPane tableScrollPane = new ScrollPane(tableView);
        tableScrollPane.setFitToWidth(true);

        tableBox.setAlignment(Pos.CENTER);
        tableBox.getChildren().addAll(tableScrollPane);
    }

    private void initializeAddUserButton()
    {
        createUserButton.setPrefSize(100,50);
        createUserButton.setFocusTraversable(false);
    }

    private void initializeUpdateButton() {
        updateButton.setFocusTraversable(false);
        updateButton.setPrefSize(100, 50);
    }

    private void initializeDeleteButton() {
        deleteButton.setFocusTraversable(false);
        deleteButton.setPrefSize(100, 50);
    }

    private void initializeEmployeeSalesToPdfButton() {
        employeeSalesToPdfButton.setFocusTraversable(false);
        employeeSalesToPdfButton.setPrefSize(100, 50);
    }

    private void initializeLogOutButton() {
        logOutButton.setFocusTraversable(false);
        logOutButton.setPrefSize(100, 50);
    }

    private void initializeTextFieldUsername() {
        textFieldUsername.setFocusTraversable(false);
        textFieldUsername.setPromptText("Username");
        textFieldUsername.maxWidth(50);
    }

    private void initializeTextFieldPassword() {
        textFieldPassword.setFocusTraversable(false);
        textFieldPassword.setPromptText("Password");
        textFieldPassword.maxWidth(50);
    }

    public void addCreateUserButtonListener(EventHandler<ActionEvent> createUserButtonListener)
    {
        createUserButton.setOnAction(createUserButtonListener);
    }

    public void addUpdateUserButtonListener(EventHandler<ActionEvent> updateUserButtonListener)
    {
        updateButton.setOnAction(updateUserButtonListener);
    }

    public void addDeleteButtonListener(EventHandler<ActionEvent> deleteButtonListener) {
        deleteButton.setOnAction(deleteButtonListener);
    }

    public void addEmployeeSalesToPdfButtonListener(EventHandler<ActionEvent> employeeSalesToPdfButtonListener) {
        employeeSalesToPdfButton.setOnAction(employeeSalesToPdfButtonListener);
    }

    public void addLogOutButtonListener(EventHandler<ActionEvent> logOutButtonListener) {
        logOutButton.setOnAction(logOutButtonListener);
    }

    public TableView<User> getTable() { return tableView;}


    public TextField getTextFieldUsername() {
        return textFieldUsername;
    }

    public TextField getTextFieldPassword() {
        return textFieldPassword;
    }
    public ComboBox<String> getComboBox() {
        return comboBox;
    }

}
