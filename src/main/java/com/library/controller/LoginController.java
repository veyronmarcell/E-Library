package com.library.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.library.util.DatabaseConnection;

public class LoginController {
    
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Label errorLabel;
    
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter both username and password");
            return;
        }
        
        try {
            if (validateLibrarian(username, password)) {
                loadMainScreen();
            } else {
                errorLabel.setText("Invalid username or password");
            }
        } catch (SQLException e) {
            errorLabel.setText("Database error: " + e.getMessage());
        }
    }
    
    private boolean validateLibrarian(String username, String password) throws SQLException {
        String query = "SELECT * FROM librarians WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password); // In a real application, use proper password hashing
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    private void loadMainScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Library Management System - Main Menu");
        } catch (IOException e) {
            errorLabel.setText("Error loading main screen: " + e.getMessage());
        }
    }
} 