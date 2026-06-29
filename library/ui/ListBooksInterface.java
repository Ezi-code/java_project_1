package library.ui;

import library.model.Book;
import library.service.BookService;

import java.util.List;
import java.util.Scanner;

public class ListBooksInterface {

    private final BookService bookService;
    private final Scanner scanner;

    public ListBooksInterface(BookService bookService, Scanner scanner) {
        this.bookService = bookService;
        this.scanner = scanner;
    }

    public void listInterface(){
        // give user list options (all, id, title, author, year)
        System.out.println("Library Listing Menu");
        System.out.println("1. List all books");
        System.out.println("2. List books by id");
        System.out.println("3. List books by title");
        System.out.println("4. List books by author");
        System.out.println("5. List books by year");
        System.out.print("Choose an option: ");

        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException ex) {
            System.out.println("Please enter a valid number.");
            return;
        }

        switch (choice) {
            case 1 -> listBooks();
            case 2 -> listById();
            case 3 -> listByTitle();
            case 4 -> listByAuthor();
            case 5 -> listByYear();
            default -> System.out.println("Invalid option.");
        }
    }

    private void listById(){
        System.out.println("Provide book id:");
        int id = Integer.parseInt(scanner.nextLine().trim());

        System.out.println(bookService.getBookByID(id));
    }

    private void listByTitle(){
        System.out.println("Enter book title:");
        String title = scanner.nextLine().trim();

        System.out.println(bookService.getBookByTitle(title));
    }

    private void listByAuthor(){
        System.out.println("Enter book author:");
        String author = scanner.nextLine().trim();

        System.out.println(bookService.getAllBooksByAuthor(author));
    }
    private void listByYear(){
        System.out.println("Enter book year:");
        int year = Integer.parseInt(scanner.nextLine().trim());

        System.out.println(bookService.getAllBooksByYear(year));
    }

    private void listBooks() {
        List<Book> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }
        for (Book book : books) {
            System.out.println(book);
        }
    }
}
