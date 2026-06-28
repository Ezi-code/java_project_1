# Introductory Java Lessons: CLI Library Management System

A robust Command Line Interface (CLI) application for managing a library of books, built entirely with the Java Standard Library. This project serves as a comprehensive practice for core Java concepts before transitioning to frameworks like Spring Boot.

## 🚀 Goal
Build Java fluency by focusing on:
- **Object-Oriented Programming (OOP):** Encapsulation, abstraction, and clean separation of concerns.
- **Collections API:** Using `List`, `ArrayList`, and Streams for data manipulation.
- **Exception Handling:** Custom exception hierarchies for business logic errors.
- **File I/O:** Persistent storage using CSV format with data sanitization (escaping).
- **Concurrency:** Demonstrating multi-threaded operations using `ExecutorService`.
- **Clean Architecture:** Decoupling UI, Service, and Repository layers.

## ✨ Features
- **List All Books:** View all registered books, automatically sorted by ID.
- **Add New Book:** Create new book entries with automated ID generation.
- **Update Book:** Partially update existing books (title, author, or year).
- **Delete Book:** Remove books from the system by their unique ID.
- **Concurrency Demo:** A built-in demonstration of how the system handles multiple simultaneous requests.
- **Data Persistence:** All data is saved to `library/books.csv` and persists between sessions.

## 📂 Project Structure
- `library.model`: Contains the `Book` entity.
- `library.repository`: Handles data persistence (CSV-based implementation).
- `library.service`: Business logic layer and data validation.
- `library.ui`: Command-line user interface logic.
- `library.exception`: Custom exceptions like `InvalidBookException`.

## 🛠️ Getting Started

### Prerequisites
- Java Development Kit (JDK) 17 or higher.

### Compilation
From the project root, compile the source files:
```bash
javac library/**/*.java library/Main.java -d out
```

### Execution
Run the application:
```bash
java -cp out library.Main
```

## ⚙️ Technical Highlights

### Data Sanitization
The system uses a custom `escape` and `unescape` mechanism in `CsvBookRepo` to ensure that titles or authors containing commas don't break the CSV structure.

### Validation
Strict validation rules are enforced in the `BookService`:
- Titles and authors are required.
- Publication years must be between 1900 and 2100.
- Duplicate titles are prevented during creation.

### Thread Safety
Includes a concurrency demo to showcase safe handling of data in multi-threaded environments.
