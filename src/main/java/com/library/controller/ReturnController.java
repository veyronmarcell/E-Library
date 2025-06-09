package com.library.controller;

import com.library.model.Book;
import com.library.model.Member;
import com.library.service.BorrowingService;
import com.library.service.MemberService;
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

public class ReturnController {
    
    @FXML private TextField memberIdField;
    @FXML private Label memberNameLabel;
    @FXML private TableView<Book> borrowedBooksTable;
    @FXML private TableColumn<Book, Integer> idColumn;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, String> genreColumn;
    @FXML private TableColumn<Book, String> isbnColumn;
    @FXML private TableColumn<Book, Integer> yearColumn;
    @FXML private TableColumn<Book, String> borrowDateColumn;
    @FXML private TableColumn<Book, String> dueDateColumn;
    @FXML private TableColumn<Book, Void> actionColumn;
    @FXML private Label statusLabel;
    
    private final MemberService memberService;
    private final BorrowingService borrowingService;
    private final ObservableList<Book> borrowedBooks = FXCollections.observableArrayList();
    private Member currentMember;
    
    public ReturnController() {
        this.memberService = new MemberService();
        this.borrowingService = new BorrowingService();
    }
    
    @FXML
    public void initialize() {
        setupTableColumns();
    }
    
    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("publicationYear"));
        
        // Add return button
        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button returnButton = new Button("Return");
            
            {
                returnButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
                returnButton.setOnAction(e -> {
                    Book book = getTableView().getItems().get(getIndex());
                    handleReturnBook(book);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(returnButton);
                }
            }
        });
    }
    
    @FXML
    private void handleFindMember() {
        String memberIdText = memberIdField.getText();
        if (memberIdText.isEmpty()) {
            statusLabel.setText("Please enter a member ID");
            return;
        }
        
        try {
            int memberId = Integer.parseInt(memberIdText);
            currentMember = memberService.getMemberById(memberId);
            
            if (currentMember != null) {
                memberNameLabel.setText(currentMember.getFirstName() + " " + currentMember.getLastName());
                loadBorrowedBooks(memberId);
                statusLabel.setText("Member found");
            } else {
                memberNameLabel.setText("");
                borrowedBooks.clear();
                statusLabel.setText("Member not found");
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid member ID");
        } catch (SQLException e) {
            statusLabel.setText("Error finding member: " + e.getMessage());
        }
    }
    
    private void loadBorrowedBooks(int memberId) throws SQLException {
        List<Book> books = borrowingService.getBorrowedBooks(memberId);
        borrowedBooks.setAll(books);
        borrowedBooksTable.setItems(borrowedBooks);
    }
    
    private void handleReturnBook(Book book) {
        if (currentMember == null) {
            statusLabel.setText("Please find a member first");
            return;
        }
        
        try {
            borrowingService.returnBook(currentMember.getMemberId(), book.getBookId());
            borrowedBooks.remove(book);
            statusLabel.setText("Book returned successfully");
        } catch (SQLException e) {
            statusLabel.setText("Error returning book: " + e.getMessage());
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