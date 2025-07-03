package com.library.management.dao;

import com.library.management.model.BorrowingRecord;
import com.library.management.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BorrowingRecordDao {

    public BorrowingRecord addBorrowingRecord(BorrowingRecord record) {
        String sql = "INSERT INTO borrowing_records (book_id, user_id, borrow_date, due_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, record.getBookId());
            pstmt.setInt(2, record.getUserId());
            pstmt.setDate(3, record.getBorrowDate());
            pstmt.setDate(4, record.getDueDate());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        record.setRecordId(generatedKeys.getInt(1));
                        return record;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateBorrowingRecord(BorrowingRecord record) {
        String sql = "UPDATE borrowing_records SET return_date = ?, fine_amount = ? WHERE record_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, record.getReturnDate());
            pstmt.setBigDecimal(2, record.getFineAmount());
            pstmt.setInt(3, record.getRecordId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<BorrowingRecord> findBorrowingRecordById(int recordId) {
        String sql = "SELECT * FROM borrowing_records WHERE record_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, recordId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToBorrowingRecord(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<BorrowingRecord> findActiveBorrowingRecordsByUserId(int userId) {
        List<BorrowingRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM borrowing_records WHERE user_id = ? AND return_date IS NULL";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    records.add(mapRowToBorrowingRecord(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    public List<BorrowingRecord> findBorrowingHistoryByUserId(int userId) {
        List<BorrowingRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM borrowing_records WHERE user_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    records.add(mapRowToBorrowingRecord(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    private BorrowingRecord mapRowToBorrowingRecord(ResultSet rs) throws SQLException {
        BorrowingRecord record = new BorrowingRecord();
        record.setRecordId(rs.getInt("record_id"));
        record.setBookId(rs.getInt("book_id"));
        record.setUserId(rs.getInt("user_id"));
        record.setBorrowDate(rs.getDate("borrow_date"));
        record.setDueDate(rs.getDate("due_date"));
        record.setReturnDate(rs.getDate("return_date"));
        record.setFineAmount(rs.getBigDecimal("fine_amount"));
        return record;
    }
} 