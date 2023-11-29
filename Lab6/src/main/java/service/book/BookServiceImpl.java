package service.book;

import model.Book;
import repository.book.BookRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BookServiceImpl implements BookService{

    final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Book with id: %d not found".formatted(id)));
    }

    @Override
    public boolean save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public int getAgeOfBook(Long id) {
        Book book = this.findById(id);

        LocalDate now = LocalDate.now();

        return (int)ChronoUnit.YEARS.between(book.getPublishedDate(), now);
    }

    public void removeById(Long id)
    {
        bookRepository.removeById(id);
    }

    @Override
    public void updateStock(int quantity, Long id) {
        bookRepository.updateStock(quantity,id);
    }

    public boolean checkStock(int quantity, Long id) {
        return bookRepository.checkStock(quantity, id);
    }

    public void updateBook(Book book, Long id){
        bookRepository.updateBook(book, id);
    }
}
