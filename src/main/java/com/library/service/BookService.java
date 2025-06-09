package com.library.service;

import com.library.model.Book;
import com.library.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookService {
    
    public void addBook(Book book) throws SQLException {
        String query = "INSERT INTO books (title, author, genre, isbn, publication_year, available) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getGenre());
            pstmt.setString(4, book.getIsbn());
            pstmt.setInt(5, book.getPublicationYear());
            pstmt.setBoolean(6, book.isAvailable());
            
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    book.setBookId(generatedKeys.getInt(1));
                }
            }
        }
    }
    
    public void removeBook(int bookId) throws SQLException {
        String query = "DELETE FROM books WHERE book_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, bookId);
            pstmt.executeUpdate();
        }
    }
    
    public List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Book book = new Book();
                book.setBookId(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setGenre(rs.getString("genre"));
                book.setIsbn(rs.getString("isbn"));
                book.setPublicationYear(rs.getInt("publication_year"));
                book.setAvailable(rs.getBoolean("available"));
                books.add(book);
            }
        }
        return books;
    }
    
    public List<Book> getAvailableBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books WHERE available = true";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Book book = new Book();
                book.setBookId(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setGenre(rs.getString("genre"));
                book.setIsbn(rs.getString("isbn"));
                book.setPublicationYear(rs.getInt("publication_year"));
                book.setAvailable(true);
                books.add(book);
            }
        }
        return books;
    }
    
    public void updateBookAvailability(int bookId, boolean available) throws SQLException {
        String query = "UPDATE books SET available = ? WHERE book_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setBoolean(1, available);
            pstmt.setInt(2, bookId);
            pstmt.executeUpdate();
        }
    }
    
    public Book getBookById(int bookId) throws SQLException {
        String query = "SELECT * FROM books WHERE book_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, bookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Book book = new Book();
                    book.setBookId(rs.getInt("book_id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setGenre(rs.getString("genre"));
                    book.setIsbn(rs.getString("isbn"));
                    book.setPublicationYear(rs.getInt("publication_year"));
                    book.setAvailable(rs.getBoolean("available"));
                    return book;
                }
            }
        }
        return null;
    }
} 