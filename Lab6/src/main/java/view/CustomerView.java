package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Book;
import service.book.BookService;


public class CustomerView {
    private final TableView<Book> tableView = new TableView<>();
    private final BookService bookService;
    private Button buyButton;
    private HBox buyButtonHBox;
    public CustomerView(Stage primaryStage, BookService bookService)
    {
        this.bookService = bookService;
        primaryStage.setTitle("Book Store");
        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);
        initializeTable();
        initializeBuyButton();
        gridPane.add(buyButtonHBox, 0, 2);
        gridPane.add(tableView,0,1);
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

        publishedDate.setMinWidth(200);
        author.setMinWidth(200);
        title.setMinWidth(200);

        author.setCellValueFactory(new PropertyValueFactory<>("author"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        publishedDate.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));

        tableView.getColumns().addAll(author,title,publishedDate);

        ObservableList<Book> books = FXCollections.observableArrayList();
        books.addAll(bookService.findAll());
        tableView.setItems(books);
        tableView.setFocusTraversable(false);
    }

    private void initializeBuyButton()
    {
        buyButton = new Button("Buy");
        buyButton.setFocusTraversable(false);
        buyButtonHBox = new HBox(10);
        buyButtonHBox.setAlignment(Pos.BOTTOM_CENTER);
        buyButtonHBox.getChildren().add(buyButton);

    }

    public void addBuyButtonListener(EventHandler<ActionEvent> buyButtonListener)
    {
        buyButton.setOnAction(buyButtonListener);
    }

    public TableView<Book> getTable() { return tableView; }

}
