package library.ui;

import library.exception.InvalidBookException;
import library.model.Book;
import library.repository.BookRepo;
import library.repository.CsvBookRepo;
import library.service.BookService;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConsoleApp {
    private final BookService bookService;
    private final Scanner scanner;

    public ConsoleApp() {
        BookRepo repository = new CsvBookRepo();
        this.bookService = new BookService(repository);
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\nLibrary Menu");
            System.out.println("1. List books");
            System.out.println("2. Add book");
            System.out.println("3. Update book");
            System.out.println("4. Delete book");
            System.out.println("5. Run concurrency demo");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            switch (choice) {
                case 1 -> listBooksInterface();
                case 2 -> addBook();
                case 3 -> updateBook();
                case 4 -> deleteBook();
                case 5 -> runConcurrencyDemo();
                case 6 -> {
                    running = false;
                    System.out.println("Goodbye!");
                }
                default -> System.out.println("Invalid option.");
            }
        }
        scanner.close();
    }

    private void listBooksInterface(){
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

    private void addBook() {
        try {
            System.out.print("Enter title: ");
            String title = scanner.nextLine();
            System.out.print("Enter author: ");
            String author = scanner.nextLine();
            System.out.print("Enter year: ");
            int year = Integer.parseInt(scanner.nextLine());

            Book book = new Book(title, author, year);
            bookService.addBook(book);
            System.out.println("Book added successfully.");
        } catch (NumberFormatException ex) {
            System.out.println("Year must be a number.");
        } catch (InvalidBookException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void deleteBook() {
        try {
            System.out.print("Enter book id: ");
            int id = Integer.parseInt(scanner.nextLine());
            bookService.deleteBook(id);
            System.out.println("Delete request processed.");
        } catch (NumberFormatException ex) {
            System.out.println("Id must be a number.");
        }
    }

    private void updateBook() {
        try {
            System.out.print("Enter book id: ");
            String idInput = scanner.nextLine().trim();
            if (idInput.isEmpty()) {
                System.out.println("ID cannot be empty.");
                return;
            }
            int id = Integer.parseInt(idInput);

            System.out.print("Enter new title (leave blank to skip): ");
            String title = scanner.nextLine().trim();

            System.out.print("Enter new author (leave blank to skip): ");
            String author = scanner.nextLine().trim();

            System.out.print("Enter new year (0 or blank to skip): ");
            String yearInput = scanner.nextLine().trim();
            int year = (yearInput.isEmpty()) ? 0 : Integer.parseInt(yearInput);

            // We create a "partial" book object to send to the service
            Book book = new Book(id, title, author, year);
            bookService.updateBook(book);
            System.out.println("Book updated successfully.");
        } catch (NumberFormatException ex) {
            System.out.println("Id and Year must be numbers.");
        } catch (InvalidBookException ex) {
            System.out.println("Update failed: " + ex.getMessage());
        }
    }

    private void runConcurrencyDemo() {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            executor.submit(() -> {
                try {
                    Thread.sleep(200);
                    bookService.addBook(new Book("Concurrent Book " + Thread.currentThread().getName(), "Demo", 2026));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        executor.shutdown();
        try {
            if (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Concurrency demo completed.");
    }
}
