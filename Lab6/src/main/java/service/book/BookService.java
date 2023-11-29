package service.book;

import model.Book;

import java.util.List;

public interface BookService {

    List<Book> findAll();

    Book findById(Long id);

    boolean save(Book book);

    int getAgeOfBook(Long id);

    void removeById(Long id);

    void updateStock(int quantity, Long id);

    boolean checkStock(int quantity, Long id);

    void updateBook(Book book, Long id);
}
