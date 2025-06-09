package com.library.controller;

import com.library.model.Member;
import com.library.service.MemberService;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class MemberDialogController {
    
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextArea addressField;
    
    private Member member;
    private final MemberService memberService;
    
    public MemberDialogController() {
        this.memberService = new MemberService();
    }
    
    public void setMember(Member member) {
        this.member = member;
        if (member != null) {
            firstNameField.setText(member.getFirstName());
            lastNameField.setText(member.getLastName());
            emailField.setText(member.getEmail());
            phoneField.setText(member.getPhone());
            addressField.setText(member.getAddress());
        }
    }
    
    @FXML
    private void handleSave() {
        if (!validateInput()) {
            return;
        }
        
        try {
            if (member == null) {
                member = new Member(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    emailField.getText(),
                    phoneField.getText(),
                    addressField.getText()
                );
                memberService.registerMember(member);
            } else {
                member.setFirstName(firstNameField.getText());
                member.setLastName(lastNameField.getText());
                member.setEmail(emailField.getText());
                member.setPhone(phoneField.getText());
                member.setAddress(addressField.getText());
                memberService.updateMember(member);
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
        if (firstNameField.getText().isEmpty() ||
            lastNameField.getText().isEmpty() ||
            emailField.getText().isEmpty()) {
            // TODO: Show error dialog
            return false;
        }
        
        // Basic email validation
        String email = emailField.getText();
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            // TODO: Show error dialog
            return false;
        }
        
        return true;
    }
    
    private void closeDialog() {
        Stage stage = (Stage) firstNameField.getScene().getWindow();
        stage.close();
    }
} 