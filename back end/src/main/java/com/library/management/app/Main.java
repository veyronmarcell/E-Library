package com.library.management.app;

import com.library.management.model.Book;
import com.library.management.model.User;
import com.library.management.service.BookService;
import com.library.management.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static UserService userService = new UserService();
    private static BookService bookService = new BookService();
    private static Optional<User> currentUser = Optional.empty();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to the Library Management System!");
        // Note: You need to manually create the 'library_db' database in MySQL
        // and replace the placeholder credentials in DatabaseUtil.java

        while (true) {
            if (currentUser.isPresent()) {
                showRoleMenu();
            } else {
                showMainMenu();
            }
        }
    }

    private static void showMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                register();
                break;
            case 3:
                System.out.println("Exiting...");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private static void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        currentUser = userService.loginUser(username, password);
        if (currentUser.isPresent()) {
            System.out.println("Login successful! Welcome, " + currentUser.get().getFullName());
        } else {
            System.out.println("Login failed. Invalid credentials.");
        }
    }

    private static void register() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter full name: ");
        String fullName = scanner.nextLine();
        // All new registrations are for members by default
        User newUser = userService.registerUser(username, password, fullName, User.Role.MEMBER);
        if (newUser != null) {
            System.out.println("Registration successful for " + newUser.getUsername());
        } else {
            System.out.println("Registration failed.");
        }
    }

    private static void logout() {
        currentUser = Optional.empty();
        System.out.println("You have been logged out.");
    }
    
    private static void showRoleMenu() {
        User user = currentUser.get();
        if (user.getRole() == User.Role.LIBRARIAN) {
            showLibrarianMenu();
        } else {
            showMemberMenu();
        }
    }

    private static void showLibrarianMenu() {
        System.out.println("\n--- Librarian Menu ---");
        System.out.println("1. Add Book");
        System.out.println("2. View All Books");
        System.out.println("3. Logout");
        System.out.print("Choose an option: ");
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1:
                addBook();
                break;
            case 2:
                viewAllBooks();
                break;
            case 3:
                logout();
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private static void showMemberMenu() {
        System.out.println("\n--- Member Menu ---");
        System.out.println("1. View All Books");
        System.out.println("2. Borrow Book");
        System.out.println("3. Return Book");
        System.out.println("4. Reserve Book");
        System.out.println("5. Logout");
        System.out.print("Choose an option: ");
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1:
                viewAllBooks();
                break;
            case 2:
                borrowBook();
                break;
            case 3:
                returnBook();
                break;
            case 4:
                reserveBook();
                break;
            case 5:
                logout();
                break;
            default:
                System.out.println("Invalid option.");
        }
    }
    
    private static void viewAllBooks() {
        List<Book> books = bookService.getAllBooks();
        System.out.println("\n--- All Books ---");
        for (Book book : books) {
            System.out.printf("ID: %d, Title: %s, Author: %s, Status: %s\n",
                    book.getBookId(), book.getTitle(), book.getAuthor(), book.getAvailabilityStatus());
        }
    }

    private static void borrowBook() {
        System.out.print("Enter Book ID to borrow: ");
        int bookId = Integer.parseInt(scanner.nextLine());
        if (bookService.borrowBook(bookId, currentUser.get().getUserId())) {
            System.out.println("Book borrowed successfully.");
        } else {
            System.out.println("Failed to borrow book.");
        }
    }

    private static void returnBook() {
        System.out.print("Enter Book ID to return: ");
        int bookId = Integer.parseInt(scanner.nextLine());
        if (bookService.returnBook(bookId, currentUser.get().getUserId())) {
            System.out.println("Book returned successfully.");
        } else {
            System.out.println("Failed to return book.");
        }
    }

    private static void reserveBook() {
        System.out.print("Enter Book ID to reserve: ");
        int bookId = Integer.parseInt(scanner.nextLine());
        bookService.reserveBook(bookId, currentUser.get().getUserId());
    }

    private static void addBook() {
        System.out.println("\n--- Add New Book ---");
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Author: ");
        String author = scanner.nextLine();
        System.out.print("Enter Genre: ");
        String genre = scanner.nextLine();
        System.out.print("Enter Publisher: ");
        String publisher = scanner.nextLine();
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();

        Book newBook = bookService.addBook(title, author, genre, publisher, isbn);
        if (newBook != null) {
            System.out.println("Book added successfully with ID: " + newBook.getBookId());
        } else {
            System.out.println("Failed to add the book.");
        }
    }
} 