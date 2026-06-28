package library.service;

import library.exception.InvalidBookException;
import library.model.Book;
import library.repository.BookRepo;

import java.util.List;

public class BookService {
    private final BookRepo repository;

    public BookService(BookRepo repository) {
        this.repository = repository;
    }

    public void addBook(Book book) {
        validateBook(book);
        if (repository.getByTitle(book.getTitle()) != null) {
            throw new InvalidBookException("Book with that title already exists.");
        }
        repository.save(book);
    }

    public void deleteBook(int id) {
        if (repository.get(id) == null) {
            throw new InvalidBookException("No book found with id " + id);
        }
        repository.delete(id);
    }

    public List<Book> getAllBooks() {
        return repository.getAll();
    }

    private void validateBook(Book book) {
        if (book == null) {
            throw new InvalidBookException("Book cannot be null.");
        }
        if (book.getTitle() == null || book.getTitle().isBlank()) {
            throw new InvalidBookException("Title is required.");
        }
        if (book.getAuthor() == null || book.getAuthor().isBlank()) {
            throw new InvalidBookException("Author is required.");
        }
        if (book.getYear() < 1900 || book.getYear() > 2100) {
            throw new InvalidBookException("Year must be between 1900 and 2100.");
        }
    }
}
