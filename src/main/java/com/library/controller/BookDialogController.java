package com.library.controller;

import com.library.model.Book;
import com.library.service.BookService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class BookDialogController {
    
    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField genreField;
    @FXML private TextField isbnField;
    @FXML private TextField yearField;
    
    private Book book;
    private final BookService bookService;
    
    public BookDialogController() {
        this.bookService = new BookService();
    }
    
    public void setBook(Book book) {
        this.book = book;
        if (book != null) {
            titleField.setText(book.getTitle());
            authorField.setText(book.getAuthor());
            genreField.setText(book.getGenre());
            isbnField.setText(book.getIsbn());
            yearField.setText(String.valueOf(book.getPublicationYear()));
        }
    }
    
    @FXML
    private void handleSave() {
        if (!validateInput()) {
            return;
        }
        
        try {
            if (book == null) {
                book = new Book();
            }
            
            book.setTitle(titleField.getText());
            book.setAuthor(authorField.getText());
            book.setGenre(genreField.getText());
            book.setIsbn(isbnField.getText());
            book.setPublicationYear(Integer.parseInt(yearField.getText()));
            
            if (book.getBookId() == 0) {
                bookService.addBook(book);
            } else {
                // Update existing book
                // TODO: Implement update functionality
            }
            
            closeDialog();
        } catch (SQLException e) {
            // TODO: Show error dialog
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleCancel() {
        closeDialog();
    }
    
    private boolean validateInput() {
        if (titleField.getText().isEmpty() ||
            authorField.getText().isEmpty() ||
            genreField.getText().isEmpty() ||
            isbnField.getText().isEmpty() ||
            yearField.getText().isEmpty()) {
            // TODO: Show error dialog
            return false;
        }
        
        try {
            Integer.parseInt(yearField.getText());
        } catch (NumberFormatException e) {
            // TODO: Show error dialog
            return false;
        }
        
        return true;
    }
    
    private void closeDialog() {
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }
} 
