package library.repository;

import library.model.Book;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class CsvBookRepo implements BookRepo {
    private final Path filePath = Paths.get("library", "books.csv");

    @Override
    public void save(Book book) {
        ensureFileExists();
        String line = String.format("%d,%s,%s,%d%n", book.getId(), escape(book.getTitle()), escape(book.getAuthor()),
                book.getYear());
        try {
            Files.writeString(filePath, line, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to write book to CSV", e);
        }
    }

    @Override
    public Book get(int id) {
        return getByID(id);
    }

    @Override
    public Book get(String title) {
        return getByTitle(title);
    }

    @Override
    public void delete(int id) {
        deleteByID(id);
    }

    @Override
    public void update(Book book) {
        ensureFileExists();
        int id = book.getId();
        Book existing  = getByID(id);

        if (existing != null){
            try {

                if (book.getTitle() != null) {
                    existing.setTitle(book.getTitle());
                }
                if (book.getAuthor() != null) {
                    existing.setAuthor(book.getAuthor());
                }
                if (book.getYear() != 0) {
                    existing.setYear(book.getYear());
                }
            } catch (Exception e) {
                System.out.println("Error updating book");
            }
        }else{
            System.out.println("Book not found");
        }
    }

    @Override
    public List<Book> getAll() {
        ensureFileExists();
        List<Book> books = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            for (String rawLine : lines) {
                String line = rawLine.trim();
                if (line.isEmpty() || line.startsWith("id,")) {
                    continue;
                }
                String[] parts = line.split(",", -1);
                if (parts.length < 4) {
                    continue;
                }
                try {
                    int id = Integer.parseInt(parts[0].trim());
                    int year = Integer.parseInt(parts[3].trim());
                    String title = unescape(parts[1].trim());
                    String author = unescape(parts[2].trim());
                    books.add(new Book(id, title, author, year));
                } catch (NumberFormatException ignored) {
                    continue;
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read books from CSV", e);
        }

        return sortById(books);
    }

    @Override
    public Book getByID(int id) {
        return getAll().stream().filter(book -> book.getId() == id).findFirst().orElse(null);
    }

    @Override
    public Book getByTitle(String title) {
        return getAll().stream().filter(book -> book.getTitle().equalsIgnoreCase(title)).findFirst().orElse(null);
    }

    @Override
    public void deleteByTitle(String title) {
        List<Book> books = getAll();
        books.removeIf(book -> book.getTitle().equalsIgnoreCase(title));
        rewriteFromList(books);
    }

    @Override
    public void deleteByID(int id) {
        List<Book> books = getAll();
        books.removeIf(book -> book.getId() == id);
        rewriteFromList(books);
    }

    private void ensureFileExists() {
        try {
            if (!Files.exists(filePath)) {
                Files.createDirectories(filePath.getParent());
                Files.writeString(filePath, "id,title,author,year\n", StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to initialize CSV file", e);
        }
    }

    private void rewriteFromList(List<Book> books) {
        List<String> lines = new ArrayList<>();
        lines.add("id,title,author,year");
        for (Book book : books) {
            lines.add(formatBook(book));
        }
        writeAll(lines);
    }

    private void writeAll(List<String> lines) {
        try {
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to rewrite CSV file", e);
        }
    }

    private String formatBook(Book book) {
        return String.format("%d,%s,%s,%d", book.getId(), escape(book.getTitle()), escape(book.getAuthor()),
                book.getYear());
    }

    private String escape(String value) {
        return value.replace(",", "\\,");
    }

    private String unescape(String value) {
        return value.replace("\\,", ",");
    }

    private List<Book> sortById(List<Book> books){
        List<Book> sortedBooks = new ArrayList<>(books);
        int n = sortedBooks.size();

        for(int i=0;i < n -1 ;i++){
            for(int j=0;j < n -i -1;j++){
                if(sortedBooks.get(j).getId() > sortedBooks.get(j+1).getId()){
                    Book temp = sortedBooks.get(j);
                    sortedBooks.set(j, sortedBooks.get(j+1));
                    sortedBooks.set(j+1, temp);
                }
            }
        }
        return sortedBooks;
    }
}
