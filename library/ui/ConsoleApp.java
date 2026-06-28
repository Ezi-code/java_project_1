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
            System.out.println("3. Delete book");
            System.out.println("4. Run concurrency demo");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            switch (choice) {
                case 1 -> listBooks();
                case 2 -> addBook();
                case 3 -> deleteBook();
                case 4 -> runConcurrencyDemo();
                case 5 -> {
                    running = false;
                    System.out.println("Goodbye!");
                }
                default -> System.out.println("Invalid option.");
            }
        }
        scanner.close();
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
