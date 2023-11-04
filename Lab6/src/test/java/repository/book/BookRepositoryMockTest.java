package repository.book;

import model.Book;
import model.builder.BookBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.BookRepositoryMockService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookRepositoryMockTest {

    private BookRepositoryMock bookRepositoryMock;
    private BookRepositoryMockService bookRepositoryMockService;
    private List<Book> books;

    // not sure if this is the right use of Mockito
    @BeforeEach
    void setup(){
        bookRepositoryMock = mock(BookRepositoryMock.class);
        bookRepositoryMockService = new BookRepositoryMockService(bookRepositoryMock);
        Book book1 = new BookBuilder()
                .setId(1L)
                .setAuthor("Cezar Petrecu")
                .setTitle("Fram Ursul Polar")
                .setPublishedDate(LocalDate.of(2010, 6, 2))
                .build();
        Book book2 = new BookBuilder()
                .setId(2L)
                .setAuthor("Jeff Vandermeer")
                .setTitle("Acceptare")
                .setPublishedDate(LocalDate.of(2011, 6, 2))
                .build();
        books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
    }

    @Test
    void checkFindAll()
    {
        when(bookRepositoryMock.findAll()).thenReturn(books);
        assertFalse(bookRepositoryMockService.findAll().isEmpty());
    }

    @Test
    void checkFindById()
    {
        when(bookRepositoryMock.findById(1L)).thenReturn(Optional.ofNullable(books.get(0)));
        assertTrue(bookRepositoryMockService.findById(1L).isPresent());
        assertFalse(bookRepositoryMockService.findById(2L).isPresent());
    }

    @Test
    void checkSave()
    {
        List<Book> booksList = new ArrayList<>();
        when(bookRepositoryMock.save(books.get(0))).thenAnswer(invocation -> booksList.add(books.get(0)));
        boolean temp = bookRepositoryMockService.save(books.get(0));
        assertTrue(temp);
        assertTrue(booksList.contains(books.get(0)));
    }

    @Test
    void checkRemoveAll()
    {
        when(bookRepositoryMockService.removeAll()).thenAnswer(invocation -> {
            books.clear();
            return null;
        });
        bookRepositoryMockService.removeAll();
        assertTrue(books.isEmpty());
    }
}