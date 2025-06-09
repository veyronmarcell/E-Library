package com.library.controller;

import com.library.model.Member;
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

public class MembersController {
    
    @FXML private TextField searchField;
    @FXML private TableView<Member> membersTable;
    @FXML private TableColumn<Member, Integer> idColumn;
    @FXML private TableColumn<Member, String> firstNameColumn;
    @FXML private TableColumn<Member, String> lastNameColumn;
    @FXML private TableColumn<Member, String> emailColumn;
    @FXML private TableColumn<Member, String> phoneColumn;
    @FXML private TableColumn<Member, String> registrationDateColumn;
    @FXML private TableColumn<Member, Void> actionsColumn;
    @FXML private Label statusLabel;
    
    private final MemberService memberService;
    private final ObservableList<Member> members = FXCollections.observableArrayList();
    
    public MembersController() {
        this.memberService = new MemberService();
    }
    
    @FXML
    public void initialize() {
        setupTableColumns();
        loadMembers();
    }
    
    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        registrationDateColumn.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        
        // Add action buttons
        actionsColumn.setCellFactory(col -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            
            {
                editButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
                
                editButton.setOnAction(e -> {
                    Member member = getTableView().getItems().get(getIndex());
                    handleEditMember(member);
                });
                
                deleteButton.setOnAction(e -> {
                    Member member = getTableView().getItems().get(getIndex());
                    handleDeleteMember(member);
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
    
    private void loadMembers() {
        try {
            List<Member> memberList = memberService.getAllMembers();
            members.setAll(memberList);
            membersTable.setItems(members);
        } catch (SQLException e) {
            statusLabel.setText("Error loading members: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().toLowerCase();
        if (searchTerm.isEmpty()) {
            loadMembers();
            return;
        }
        
        try {
            List<Member> searchResults = memberService.searchMembers(searchTerm);
            members.setAll(searchResults);
        } catch (SQLException e) {
            statusLabel.setText("Error searching members: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleAddMember() {
        showMemberDialog(null);
    }
    
    private void handleEditMember(Member member) {
        showMemberDialog(member);
    }
    
    private void handleDeleteMember(Member member) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Member");
        alert.setContentText("Are you sure you want to delete this member?");
        
        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                memberService.deleteMember(member.getMemberId());
                members.remove(member);
                statusLabel.setText("Member deleted successfully");
            } catch (SQLException e) {
                statusLabel.setText("Error deleting member: " + e.getMessage());
            }
        }
    }
    
    private void showMemberDialog(Member member) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/member_dialog.fxml"));
            Parent root = loader.load();
            
            MemberDialogController controller = loader.getController();
            controller.setMember(member);
            
            Stage stage = new Stage();
            stage.setTitle(member == null ? "Add Member" : "Edit Member");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            loadMembers(); // Refresh the table
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