package service;

import model.Book;
import repository.book.BookRepository;

import java.util.List;
import java.util.Optional;

public class BookRepositoryMockService implements BookService{
    BookRepository bookRepository;
    public BookRepositoryMockService(BookRepository bookRepository)
    {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public boolean save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public boolean removeAll() {
        bookRepository.removeAll();
        return true;
    }

    @Override
    public int getAgeOfBook(Long id) {
        return 0;
    }
}
