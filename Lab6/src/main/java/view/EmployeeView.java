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
import model.Book;
import service.book.BookService;

public class EmployeeView {
    private final TableView<Book> tableView = new TableView<>();
    private final TextField textFieldId = new TextField();
    private final TextField textFieldTitle = new TextField();
    private final TextField textFieldAuthor = new TextField();
    private final TextField textFieldPublishedDate = new TextField();
    private final TextField textFieldStock = new TextField();
    private final TextField textFieldUserId = new TextField();
    private final TextField textFieldQuantity = new TextField();
    private final Label title = new Label("Books available now");
    private final Label bookId = new Label("id");
    private final Label bookAuthor = new Label("author");
    private final Label bookTitle = new Label("title");
    private final Label bookPublishedDate = new Label("date");
    private final Label bookStock = new Label("stock");
    private final Label userId = new Label("user id");
    private final Label bookQuantity = new Label("quantity");
    private final HBox bottomBox = new HBox(10);
    private final HBox tableBox = new HBox(tableView);
    private final Button sellButton = new Button("Sell");
    private final Button updateButton = new Button("Update");
    private final Button deleteButton = new Button("Delete");
    private final Button orderToPdfButton = new Button("OrderToPdf");
    private final BookService bookService;


    public EmployeeView(Stage primaryStage, BookService bookService) {
        this.bookService = bookService;
        primaryStage.setTitle("Book Store");
        GridPane gridPane = new GridPane();

        initializeGridPane(gridPane);
        initializeTable();
        initializeSellButton();
        initializeDeleteButton();
        initializeOrderToPdfButton();
        initializeUpdateButton();
        initializeTextFieldId();
        initializeTextFieldTitle();
        initializeTextFieldAuthor();
        initializeTextFieldPublishedDate();
        initializeTextFieldQuantity();
        initializeTextFieldUserId();
        initializeTextFieldStock();
        initializeLabels();
        initializeBottomBox();

        HBox titleBox = new HBox(title);
        titleBox.setAlignment(Pos.CENTER);

        GridPane gridPaneMain = new GridPane();
        gridPaneMain.setHgap(10);
        gridPaneMain.setVgap(10);
        gridPaneMain.setPadding(new Insets(25, 25, 25, 25));

        VBox vBox = new VBox(sellButton, updateButton, deleteButton, orderToPdfButton);
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

    private void initializeBottomBox() {
        VBox vBoxId = new VBox(textFieldId, bookId);
        vBoxId.setSpacing(10);
        vBoxId.setAlignment(Pos.CENTER);

        VBox vBoxAuthor = new VBox(textFieldAuthor, bookAuthor);
        vBoxAuthor.setSpacing(10);
        vBoxAuthor.setAlignment(Pos.CENTER);

        VBox vBoxBookTitle = new VBox(textFieldTitle,bookTitle);
        vBoxBookTitle.setSpacing(10);
        vBoxBookTitle.setAlignment(Pos.CENTER);

        VBox vBoxPublishedDate = new VBox(textFieldPublishedDate,bookPublishedDate);
        vBoxPublishedDate.setSpacing(10);
        vBoxPublishedDate.setAlignment(Pos.CENTER);

        VBox vBoxStock = new VBox(textFieldStock,bookStock);
        vBoxStock.setSpacing(10);
        vBoxStock.setAlignment(Pos.CENTER);

        VBox vBoxUserId = new VBox(textFieldUserId,userId);
        vBoxUserId.setSpacing(10);
        vBoxUserId.setAlignment(Pos.CENTER);

        VBox vBoxQuantity = new VBox(textFieldQuantity,bookQuantity);
        vBoxQuantity.setSpacing(10);
        vBoxQuantity.setAlignment(Pos.CENTER);

        bottomBox.setAlignment(Pos.BOTTOM_CENTER);
        bottomBox.getChildren().addAll(vBoxId, vBoxAuthor, vBoxBookTitle,
                vBoxPublishedDate, vBoxStock, vBoxUserId, vBoxQuantity);
    }

    private void initializeLabels() {
        title.setFont(Font.font(null, FontWeight.BOLD, 16));

        bookId.setFont(Font.font(null, FontWeight.BOLD, 16));
        bookId.setAlignment(Pos.CENTER);

        bookAuthor.setFont(Font.font(null, FontWeight.BOLD, 16));
        bookAuthor.setAlignment(Pos.CENTER);

        bookTitle.setFont(Font.font(null, FontWeight.BOLD, 16));
        bookTitle.setAlignment(Pos.CENTER);

        bookPublishedDate.setFont(Font.font(null, FontWeight.BOLD, 16));
        bookPublishedDate.setAlignment(Pos.CENTER);

        bookStock.setFont(Font.font(null, FontWeight.BOLD, 16));
        bookStock.setAlignment(Pos.CENTER);

        userId.setFont(Font.font(null, FontWeight.BOLD, 16));
        userId.setAlignment(Pos.CENTER);

        bookQuantity.setFont(Font.font(null, FontWeight.BOLD, 16));
        bookQuantity.setAlignment(Pos.CENTER);
    }

    private void initializeGridPane(GridPane gridPane) {
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

    private void initializeTable() {
        TableColumn<Book, String> id = new TableColumn<>("id");
        TableColumn<Book, String> author = new TableColumn<>("author");
        TableColumn<Book, String> title = new TableColumn<>("title");
        TableColumn<Book, String> publishedDate = new TableColumn<>("publishedDate");
        TableColumn<Book, Integer> stock = new TableColumn<>("stock");

        publishedDate.setMaxWidth(150);
        author.setMaxWidth(100);
        title.setMaxWidth(100);
        stock.setMaxWidth(50);
        id.setMaxWidth(50);

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        author.setCellValueFactory(new PropertyValueFactory<>("author"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        publishedDate.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));
        stock.setCellValueFactory(new PropertyValueFactory<>("stock"));


        tableView.getColumns().addAll(id, author, title, publishedDate, stock);

        ObservableList<Book> books = FXCollections.observableArrayList();
        books.addAll(bookService.findAll());
        tableView.setItems(books);
        tableView.setFocusTraversable(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        tableView.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        ScrollPane tableScrollPane = new ScrollPane(tableView);
        tableScrollPane.setFitToWidth(true);

        tableBox.setAlignment(Pos.CENTER);
        tableBox.getChildren().addAll(tableScrollPane);
    }

    private void initializeSellButton() {
        sellButton.setFocusTraversable(false);
        sellButton.setPrefSize(100, 50);
    }

    private void initializeUpdateButton() {
        updateButton.setFocusTraversable(false);
        updateButton.setPrefSize(100, 50);
    }

    private void initializeDeleteButton() {
        deleteButton.setFocusTraversable(false);
        deleteButton.setPrefSize(100, 50);
    }

    private void initializeOrderToPdfButton() {
        orderToPdfButton.setFocusTraversable(false);
        orderToPdfButton.setPrefSize(100, 50);
    }

    private void initializeTextFieldId() {
        textFieldId.setFocusTraversable(false);
        textFieldId.setPromptText("Id");
        textFieldId.maxWidth(10);
    }

    private void initializeTextFieldTitle() {
        textFieldTitle.setFocusTraversable(false);
        textFieldTitle.setPromptText("Title");
        textFieldTitle.maxWidth(50);
    }

    private void initializeTextFieldAuthor() {
        textFieldAuthor.setFocusTraversable(false);
        textFieldAuthor.setPromptText("Author");
        textFieldAuthor.maxWidth(50);
    }

    private void initializeTextFieldPublishedDate() {
        textFieldPublishedDate.setFocusTraversable(false);
        textFieldPublishedDate.setPromptText("PublishedDate");
        textFieldPublishedDate.maxWidth(50);
    }

    private void initializeTextFieldStock() {
        textFieldStock.setFocusTraversable(false);
        textFieldStock.setPromptText("Stock");
        textFieldStock.maxWidth(10);
    }

    private void initializeTextFieldUserId() {
        textFieldUserId.setFocusTraversable(false);
        textFieldUserId.setPromptText("UserId");
        textFieldUserId.maxWidth(10);
    }

    private void initializeTextFieldQuantity() {
        textFieldQuantity.setFocusTraversable(false);
        textFieldQuantity.setPromptText("Quantity");
        textFieldQuantity.maxWidth(10);
    }

    public void addSellButtonListener(EventHandler<ActionEvent> sellButtonListener) {
        sellButton.setOnAction(sellButtonListener);
    }

    public void addUpdateButtonListener(EventHandler<ActionEvent> updateButtonListener) {
        updateButton.setOnAction(updateButtonListener);
    }

    public void addDeleteButtonListener(EventHandler<ActionEvent> deleteButtonListener) {
        deleteButton.setOnAction(deleteButtonListener);
    }

    public void addOrderToPdfButtonListener(EventHandler<ActionEvent> orderToPdfButtonListener) {
        orderToPdfButton.setOnAction(orderToPdfButtonListener);
    }

    public TableView<Book> getTable() {
        return tableView;
    }

    public TextField getTextFieldStock() {
        return textFieldStock;
    }

    public TextField getTextFieldId() {
        return textFieldId;
    }

    public TextField getTextFieldTitle() {
        return textFieldTitle;
    }

    public TextField getTextFieldAuthor() {
        return textFieldAuthor;
    }

    public TextField getTextFieldUserId() {
        return textFieldUserId;
    }

    public TextField getTextFieldPublishedDate() {
        return textFieldPublishedDate;
    }

    public TextField getTextFieldQuantity() {
        return textFieldQuantity;
    }
}
