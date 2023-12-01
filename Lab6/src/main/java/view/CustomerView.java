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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Book;
import service.book.BookService;


public class CustomerView {
    private final TableView<Book> tableView = new TableView<>();
    private final TextField textField = new TextField();
    private final BookService bookService;
    private Button buyButton;
    private final Button logOutButton = new Button("Log Out");


    public CustomerView(Stage primaryStage, BookService bookService)
    {
        this.bookService = bookService;
        primaryStage.setTitle("Book Store");
        GridPane gridPane = new GridPane();

        initializeGridPane(gridPane);
        initializeTable();
        initializeBuyButton();
        initializeTextField();
        initializeLogOutButton();

        Label title = new Label("Books available now");
        title.setFont(Font.font(null, FontWeight.BOLD, 16));

        Label quantityLabel = new Label("Quantity:");
        quantityLabel.setFont(Font.font(null,FontWeight.BOLD,16));

        HBox inputHBox = new HBox(10);
        inputHBox.setAlignment(Pos.BOTTOM_CENTER);
        inputHBox.getChildren().addAll(quantityLabel,textField, buyButton,logOutButton);

        gridPane.add(title,0,1);
        gridPane.add(tableView,0,2);
        gridPane.add(inputHBox, 0, 3);

        Scene scene = new Scene(gridPane, 720, 480);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeGridPane(GridPane gridPane){
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    private void initializeTable()
    {
        TableColumn<Book,String> author = new TableColumn<>("author");
        TableColumn<Book, String> title = new TableColumn<>("title");
        TableColumn<Book, String> publishedDate = new TableColumn<>("publishedDate");
        TableColumn<Book, Integer> stock = new TableColumn<>("stock");

        publishedDate.setMinWidth(100);
        author.setMinWidth(100);
        title.setMinWidth(100);
        stock.setMinWidth(100);

        author.setCellValueFactory(new PropertyValueFactory<>("author"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        publishedDate.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));
        stock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        tableView.getColumns().addAll(author,title,publishedDate,stock);

        ObservableList<Book> books = FXCollections.observableArrayList();
        books.addAll(bookService.findAll());
        tableView.setItems(books);
        tableView.setFocusTraversable(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void initializeBuyButton()
    {
        buyButton = new Button("Buy");
        buyButton.setFocusTraversable(false);
    }

    private void initializeLogOutButton() {
        logOutButton.setFocusTraversable(false);
        logOutButton.setPrefSize(100, 50);
    }

    private void initializeTextField(){
        textField.setFocusTraversable(false);
    }

    public void addBuyButtonListener(EventHandler<ActionEvent> buyButtonListener)
    {
        buyButton.setOnAction(buyButtonListener);
    }

    public void addLogOutButtonListener(EventHandler<ActionEvent> logOutButtonListener) {
        logOutButton.setOnAction(logOutButtonListener);
    }

    public TableView<Book> getTable() { return tableView;}

    public TextField getTextField() {return textField;}

}
