import database.JDBConnectionWrapper;
import model.AudioBook;
import model.Book;
import model.EBook;
import model.builder.AudioBookBuilder;
import model.builder.BookBuilder;
import model.builder.EBookBuilder;
import repository.book.BookRepository;
import repository.book.BookRepositoryMock;
import repository.book.BookRepositoryMySQL;
import service.BookService;
import service.BookServiceImpl;

import java.time.LocalDate;
import java.util.Date;

public class Main {
    public static void main(String[] args){
        System.out.println("Hello world!");

        JDBConnectionWrapper connectionWrapper = new JDBConnectionWrapper("test_library");



        BookRepository bookRepository = new BookRepositoryMySQL(connectionWrapper.getConnection());

        Book book = new BookBuilder()
                .setAuthor("Cezar Petrecu")
                .setTitle("Fram Ursul Polar")
                .setPublishedDate(LocalDate.of(2010, 6, 2))
                .build();
        EBook ebook = (EBook) new EBookBuilder()
                .setFormat("PDF")
                .setAuthor("Stephen King")
                .setTitle("Un strop de sange")
                .setPublishedDate(LocalDate.of(2010, 6, 2))
                .build();

        AudioBook audioBook = (AudioBook) new AudioBookBuilder()
                .setRunTime(60)
                .setAuthor("Dan Brown")
                .setTitle("Inger si demon")
                .setPublishedDate(LocalDate.of(2010, 6, 2))
                .build();

        BookService bookService = new BookServiceImpl(bookRepository);

        bookService.removeAll();

        bookService.save(ebook);
        bookService.save(book);
        bookService.save(audioBook);
        System.out.println(bookService.findAll());

    }
}
