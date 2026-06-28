package library.repository;

import library.model.Book;

import java.util.List;

public interface BookRepo {
    void save(Book book);

    Book get(int id);

    Book get(String title);

    void delete(int id);

    void update(Book book);

    List<Book> getAll();

    Book getByID(int id);

    Book getByTitle(String title);
    Book getAllByAuthor(String author);
    Book getAllByYear(int year);

    void deleteByTitle(String title);

    void deleteByID(int id);
}
