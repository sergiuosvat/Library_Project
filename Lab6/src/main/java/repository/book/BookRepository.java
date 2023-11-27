package repository.book;

import model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    List<Book> findAll();

    Optional<Book> findById(Long id);

    boolean save(Book book);

    void removeAll();

    void removeById(Long id);

    void updateStock(int quantity, Long id);

    boolean checkStock(int quantity, Long id);
}
