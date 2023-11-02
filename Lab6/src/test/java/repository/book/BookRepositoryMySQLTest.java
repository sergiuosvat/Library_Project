package repository.book;

import model.Book;
import model.builder.BookBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BookRepositoryMySQLTest {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/";

    private static final String SCHEMA = "test_library";

    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static Connection connection;

    @BeforeAll
    public static void setUp(){
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL+SCHEMA , USER, PASSWORD);

            Book book = new BookBuilder()
                    .setAuthor("Cezar Petrecu")
                    .setTitle("Fram Ursul Polar")
                    .setPublishedDate(LocalDate.of(2010, 6, 2))
                    .build();

            BookRepository bookRepository = new BookRepositoryMySQL(connection);
            bookRepository.save(book);

        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    void save() {
        Book book = new BookBuilder()
                .setAuthor("Cezar Petrecu")
                .setTitle("Fram Ursul Polar")
                .setPublishedDate(LocalDate.of(2010, 6, 2))
                .build();

        BookRepository bookRepository = new BookRepositoryMySQL(connection);
        bookRepository.save(book);

        Optional<Book> savedBook = bookRepository.findById(1L);
        assertTrue(savedBook.isPresent());
    }

    @Test
    void findAll() {
        BookRepository bookRepository = new BookRepositoryMySQL(connection);
        List<Book> books = bookRepository.findAll();
        assertNotNull(books);
    }

    @Test
    void findById() {
        BookRepository bookRepository = new BookRepositoryMySQL(connection);
        Optional<Book> book = bookRepository.findById(1L);
        assertTrue(book.isPresent());
    }

    @Test
    void removeAll() {
        BookRepository bookRepository = new BookRepositoryMySQL(connection);
        bookRepository.removeAll();
        List<Book> books = bookRepository.findAll();
        assertTrue(books.isEmpty());
    }
}