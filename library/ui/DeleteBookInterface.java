package library.ui;
import library.service.BookService;

import java.util.Scanner;


public class DeleteBookInterface {

    private final BookService bookService;
    private final Scanner scanner;

    public DeleteBookInterface(BookService bookService, Scanner scanner) {
        this.bookService = bookService;
        this.scanner = scanner;
    }
    public void deleteInterface(){
        System.out.println("Select delete option: ");
        System.out.println("1. Delete book by id");
        System.out.println("2. Delete book by title");
        System.out.print("Enter option: ");
        int choice = Integer.parseInt(scanner.nextLine().trim());
        System.out.println();


        switch (choice) {
            case 1:
                deleteBook();
                break;
            case 2:
                deleteBookByTitle();
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

    private void deleteBookByTitle(){
        System.out.println("Enter book title:");
        String title = scanner.nextLine().trim();
        bookService.deleteBookByTitle(title);

    }
}
