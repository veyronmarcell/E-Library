package com.library.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    
    @FXML
    private Label statusLabel;
    
    @FXML
    private void handleManageBooks() {
        loadScreen("/fxml/books.fxml", "Manage Books");
    }
    
    @FXML
    private void handleManageMembers() {
        loadScreen("/fxml/members.fxml", "Manage Members");
    }
    
    @FXML
    private void handleBorrowBooks() {
        loadScreen("/fxml/borrow.fxml", "Borrow Books");
    }
    
    @FXML
    private void handleReturnBooks() {
        loadScreen("/fxml/return.fxml", "Return Books");
    }
    
    @FXML
    private void handleExit() {
        System.exit(0);
    }
    
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Library Management System");
        alert.setContentText("Version 1.0\n\nA simple library management system for managing books and members.");
        alert.showAndWait();
    }
    
    private void loadScreen(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Library Management System - " + title);
        } catch (IOException e) {
            statusLabel.setText("Error loading screen: " + e.getMessage());
        }
    }
} 