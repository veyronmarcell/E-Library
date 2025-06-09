package com.library.controller;

import com.library.model.Book;
import com.library.service.BookService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class BooksController {
    
    @FXML private TextField searchField;
    @FXML private TableView<Book> booksTable;
    @FXML private TableColumn<Book, Integer> idColumn;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, String> genreColumn;
    @FXML private TableColumn<Book, String> isbnColumn;
    @FXML private TableColumn<Book, Integer> yearColumn;
    @FXML private TableColumn<Book, Boolean> availableColumn;
    @FXML private TableColumn<Book, Void> actionsColumn;
    @FXML private Label statusLabel;
    
    private final BookService bookService;
    private final ObservableList<Book> books = FXCollections.observableArrayList();
    
    public BooksController() {
        this.bookService = new BookService();
    }
    
    @FXML
    public void initialize() {
        setupTableColumns();
        loadBooks();
    }
    
    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("publicationYear"));
        availableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));
        
        // Add action buttons
        actionsColumn.setCellFactory(col -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            
            {
                editButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
                
                editButton.setOnAction(e -> {
                    Book book = getTableView().getItems().get(getIndex());
                    handleEditBook(book);
                });
                
                deleteButton.setOnAction(e -> {
                    Book book = getTableView().getItems().get(getIndex());
                    handleDeleteBook(book);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5, editButton, deleteButton);
                    setGraphic(buttons);
                }
            }
        });
    }
    
    private void loadBooks() {
        try {
            List<Book> bookList = bookService.getAllBooks();
            books.setAll(bookList);
            booksTable.setItems(books);
        } catch (SQLException e) {
            statusLabel.setText("Error loading books: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().toLowerCase();
        if (searchTerm.isEmpty()) {
            loadBooks();
            return;
        }
        
        books.setAll(books.filtered(book ->
            book.getTitle().toLowerCase().contains(searchTerm) ||
            book.getAuthor().toLowerCase().contains(searchTerm) ||
            book.getGenre().toLowerCase().contains(searchTerm) ||
            book.getIsbn().toLowerCase().contains(searchTerm)
        ));
    }
    
    @FXML
    private void handleAddBook() {
        showBookDialog(null);
    }
    
    private void handleEditBook(Book book) {
        showBookDialog(book);
    }
    
    private void handleDeleteBook(Book book) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Book");
        alert.setContentText("Are you sure you want to delete this book?");
        
        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                bookService.removeBook(book.getBookId());
                books.remove(book);
                statusLabel.setText("Book deleted successfully");
            } catch (SQLException e) {
                statusLabel.setText("Error deleting book: " + e.getMessage());
            }
        }
    }
    
    private void showBookDialog(Book book) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/book_dialog.fxml"));
            Parent root = loader.load();
            
            BookDialogController controller = loader.getController();
            controller.setBook(book);
            
            Stage stage = new Stage();
            stage.setTitle(book == null ? "Add Book" : "Edit Book");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            loadBooks(); // Refresh the table
        } catch (IOException e) {
            statusLabel.setText("Error showing dialog: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Library Management System - Main Menu");
        } catch (IOException e) {
            statusLabel.setText("Error returning to main menu: " + e.getMessage());
        }
    }
} 