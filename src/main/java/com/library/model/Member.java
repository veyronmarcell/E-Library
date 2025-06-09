package com.library.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Member {
    private int memberId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private LocalDate registrationDate;
    private List<Book> borrowedBooks;

    public Member() {
        this.borrowedBooks = new ArrayList<>();
    }

    public Member(String firstName, String lastName, String email, String phone, String address) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.registrationDate = LocalDate.now();
    }

    // Getters and Setters
    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }
    
    public List<Book> getBorrowedBooks() { return borrowedBooks; }
    public void setBorrowedBooks(List<Book> borrowedBooks) { this.borrowedBooks = borrowedBooks; }

    public boolean canBorrowBook() {
        return borrowedBooks.size() < 3;
    }

    public void addBorrowedBook(Book book) {
        if (canBorrowBook()) {
            borrowedBooks.add(book);
        }
    }

    public void removeBorrowedBook(Book book) {
        borrowedBooks.remove(book);
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
} 