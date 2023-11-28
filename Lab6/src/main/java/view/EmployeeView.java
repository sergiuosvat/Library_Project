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
import javafx.scene.layout.*;
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
    private final TextField textFieldQuantity = new TextField();
    private final TextField textFieldUserId = new TextField();
    private final HBox tableBox = new HBox(tableView);
    private final Button sellButton = new Button("Sell");;
    private final Button updateButton = new Button("Update");;
    private final Button deleteButton = new Button("Delete");
    private final Button orderToPdfButton = new Button("OrderToPdf");
    private final BookService bookService;


    public EmployeeView(Stage primaryStage, BookService bookService)
    {
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

        HBox inputHBox = new HBox(10);
        inputHBox.setAlignment(Pos.BOTTOM_CENTER);

        Label title = new Label("Cartile disponibile acum");
        title.setFont(Font.font(null, FontWeight.BOLD,16));
        HBox labelBox = new HBox(title);
        labelBox.setAlignment(Pos.CENTER);

        GridPane gridPaneMain= new GridPane();
        gridPaneMain.setHgap(10);
        gridPaneMain.setVgap(10);
        gridPaneMain.setPadding(new Insets(25, 25, 25, 25));

        inputHBox.getChildren().addAll(textFieldId,textFieldAuthor,textFieldTitle,
                textFieldPublishedDate,textFieldUserId,textFieldQuantity);

        VBox vBox = new VBox(sellButton, updateButton, deleteButton, orderToPdfButton);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);

        gridPane.add(tableBox,0,1);
        gridPane.add(vBox,1,1);
        gridPaneMain.add(labelBox,0,1);
        gridPaneMain.add(gridPane,0,2);
        gridPaneMain.add(inputHBox,0,3);

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

    private void initializeTable()
    {
        TableColumn<Book, String> id = new TableColumn<>("id");
        TableColumn<Book,String> author = new TableColumn<>("author");
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


        tableView.getColumns().addAll(id,author,title,publishedDate,stock);

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

    private void initializeSellButton()
    {
        sellButton.setFocusTraversable(false);
        sellButton.setPrefSize(100,50);
    }

    private void initializeUpdateButton()
    {
        updateButton.setFocusTraversable(false);
        updateButton.setPrefSize(100,50);
    }

    private void initializeDeleteButton()
    {
        deleteButton.setFocusTraversable(false);
        deleteButton.setPrefSize(100,50);
    }

    private void initializeOrderToPdfButton()
    {
        orderToPdfButton.setFocusTraversable(false);
        orderToPdfButton.setPrefSize(100,50);
    }

    private void initializeTextFieldId(){
        textFieldId.setFocusTraversable(false);
        textFieldId.setPromptText("Id");
        textFieldId.maxWidth(10);
    }
    private void initializeTextFieldTitle(){
        textFieldTitle.setFocusTraversable(false);
        textFieldTitle.setPromptText("Title");
        textFieldTitle.maxWidth(50);
    }
    private void initializeTextFieldAuthor(){
        textFieldAuthor.setFocusTraversable(false);
        textFieldAuthor.setPromptText("Author");
        textFieldAuthor.maxWidth(50);
    }
    private void initializeTextFieldPublishedDate(){
        textFieldPublishedDate.setFocusTraversable(false);
        textFieldPublishedDate.setPromptText("PublishedDate");
        textFieldPublishedDate.maxWidth(50);
    }

    private void initializeTextFieldQuantity(){
        textFieldQuantity.setFocusTraversable(false);
        textFieldQuantity.setPromptText("Quantity");
        textFieldQuantity.maxWidth(10);
    }

    private void initializeTextFieldUserId()
    {
        textFieldUserId.setFocusTraversable(false);
        textFieldUserId.setPromptText("UserId");
        textFieldUserId.maxWidth(10);
    }

    public void addSellButtonListener(EventHandler<ActionEvent> sellButtonListener)
    {
        sellButton.setOnAction(sellButtonListener);
    }

    public TableView<Book> getTable() { return tableView;}

    public TextField getTextFieldQuantity() {return textFieldQuantity;}

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
}
