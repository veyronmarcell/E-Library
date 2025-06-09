package com.library.service;

import com.library.model.Book;
import com.library.model.Member;
import com.library.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BorrowingService {
    private final BookService bookService;
    private final MemberService memberService;
    
    public BorrowingService() {
        this.bookService = new BookService();
        this.memberService = new MemberService();
    }
    
    public void borrowBook(int memberId, int bookId) throws SQLException {
        Member member = memberService.getMemberById(memberId);
        Book book = bookService.getBookById(bookId);
        
        if (member == null || book == null) {
            throw new SQLException("Member or book not found");
        }
        
        if (!book.isAvailable()) {
            throw new SQLException("Book is not available");
        }
        
        if (!member.canBorrowBook()) {
            throw new SQLException("Member has reached the maximum number of borrowed books");
        }
        
        String query = "INSERT INTO borrowings (member_id, book_id, borrow_date, due_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            LocalDate borrowDate = LocalDate.now();
            LocalDate dueDate = borrowDate.plusDays(14); // 2 weeks borrowing period
            
            pstmt.setInt(1, memberId);
            pstmt.setInt(2, bookId);
            pstmt.setDate(3, Date.valueOf(borrowDate));
            pstmt.setDate(4, Date.valueOf(dueDate));
            
            pstmt.executeUpdate();
            
            // Update book availability
            bookService.updateBookAvailability(bookId, false);
        }
    }
    
    public void returnBook(int memberId, int bookId) throws SQLException {
        String query = "UPDATE borrowings SET return_date = ? WHERE member_id = ? AND book_id = ? AND return_date IS NULL";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setDate(1, Date.valueOf(LocalDate.now()));
            pstmt.setInt(2, memberId);
            pstmt.setInt(3, bookId);
            
            int updated = pstmt.executeUpdate();
            if (updated > 0) {
                bookService.updateBookAvailability(bookId, true);
            } else {
                throw new SQLException("No active borrowing found for this book and member");
            }
        }
    }
    
    public List<Book> getBorrowedBooks(int memberId) throws SQLException {
        List<Book> books = new ArrayList<>();
        String query = "SELECT b.* FROM books b " +
                      "JOIN borrowings br ON b.book_id = br.book_id " +
                      "WHERE br.member_id = ? AND br.return_date IS NULL";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book();
                    book.setBookId(rs.getInt("book_id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setGenre(rs.getString("genre"));
                    book.setIsbn(rs.getString("isbn"));
                    book.setPublicationYear(rs.getInt("publication_year"));
                    book.setAvailable(false);
                    books.add(book);
                }
            }
        }
        return books;
    }
    
    public List<Book> getOverdueBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String query = "SELECT b.* FROM books b " +
                      "JOIN borrowings br ON b.book_id = br.book_id " +
                      "WHERE br.return_date IS NULL AND br.due_date < ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setDate(1, Date.valueOf(LocalDate.now()));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book();
                    book.setBookId(rs.getInt("book_id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setGenre(rs.getString("genre"));
                    book.setIsbn(rs.getString("isbn"));
                    book.setPublicationYear(rs.getInt("publication_year"));
                    book.setAvailable(false);
                    books.add(book);
                }
            }
        }
        return books;
    }
} 